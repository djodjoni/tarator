package org.djodjo.tarator.action;

import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;

import com.google.common.base.Preconditions;

import org.djodjo.tarator.NoMatchingViewException;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewAction;
import org.djodjo.tarator.ViewAssertion;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A collection of common {@link ViewActions}.
 */
public final class ViewActions {

  private static final float EDGE_FUZZ_FACTOR = 0.083F;
  private static Set<Pair<String, ViewAssertion>> globalAssertions = new CopyOnWriteArraySet();


  public static void addGlobalAssertion(String name, ViewAssertion viewAssertion) {
    Preconditions.checkNotNull(name);
    Preconditions.checkNotNull(viewAssertion);
    Pair vaPair = new Pair(name, viewAssertion);
    Preconditions.checkArgument(!globalAssertions.contains(vaPair), "ViewAssertion with name %s is already in the global assertions!", new Object[]{name});
    globalAssertions.add(vaPair);
  }

  public static void removeGlobalAssertion(ViewAssertion viewAssertion) {
    boolean removed = false;
    Iterator i$ = globalAssertions.iterator();

    while(i$.hasNext()) {
      Pair vaPair = (Pair)i$.next();
      if(viewAssertion != null && viewAssertion.equals(vaPair.second)) {
        removed = removed || globalAssertions.remove(vaPair);
      }
    }

    Preconditions.checkArgument(removed, "ViewAssertion was not in global assertions!");
  }

  public static void clearGlobalAssertions() {
    globalAssertions.clear();
  }

  public static ViewAction actionWithAssertions(final ViewAction viewAction) {
    return globalAssertions.isEmpty()?viewAction:new ViewAction() {
      public String getDescription() {
        StringBuilder msg = new StringBuilder("Running view assertions[");
        Iterator iterator = ViewActions.globalAssertions.iterator();

        while(iterator.hasNext()) {
          Pair vaPair = (Pair)iterator.next();
          msg.append((String)vaPair.first);
          msg.append(", ");
        }

        msg.append("] and then running: ");
        msg.append(viewAction.getDescription());
        return msg.toString();
      }

      public Matcher<View> getConstraints() {
        return viewAction.getConstraints();
      }

      public void perform(UiController uic, View view) {
        Iterator iterator = ViewActions.globalAssertions.iterator();

        while(iterator.hasNext()) {
          Pair vaPair = (Pair)iterator.next();
          Log.i("ViewAssertion", "Asserting " + (String) vaPair.first);
          ((ViewAssertion)vaPair.second).check(view, (NoMatchingViewException)null);
        }

        viewAction.perform(uic, view);
      }
    };
  }

  private ViewActions() {}

  /**
   * Returns an action that clears text on the view.<br>
   * <br>
   * View constraints:
   * <ul>
   * <li>must be displayed on screen
   * <ul>
   */
  public static ViewAction clearText() {
    return actionWithAssertions(new ClearTextAction());
  }

  /**
   * Returns an action that clicks the view.<br>
   * <br>
   * View constraints:
   * <ul>
   * <li>must be displayed on screen
   * <ul>
   */
  public static ViewAction click() {
    return actionWithAssertions(new GeneralClickAction(Tap.SINGLE, GeneralLocation.VISIBLE_CENTER, Press.FINGER));
  }

  /**
   * Returns an action that performs a single click on the view.
   *
   * If the click takes longer than the 'long press' duration (which is possible) the provided
   * rollback action is invoked on the view and a click is attempted again.
   *
   * This is only necessary if the view being clicked on has some different behaviour for long press
   * versus a normal tap.
   *
   * For example - if a long press on a particular view element opens a popup menu -
   * ViewActions.pressBack() may be an acceptable rollback action.
   *
   * <br>
   * View constraints:
   * <ul>
   * <li>must be displayed on screen</li>
   * <li>any constraints of the rollbackAction</li>
   * <ul>
   */
  public static ViewAction click(ViewAction rollbackAction) {
    checkNotNull(rollbackAction);
    return new GeneralClickAction(Tap.SINGLE, GeneralLocation.VISIBLE_CENTER, Press.FINGER,
        rollbackAction);
  }

  /**
   * Returns an action that performs a swipe right-to-left across the vertical center of the
   * view.<br>
   * <br>
   * View constraints:
   * <ul>
   * <li>must be displayed on screen
   * <ul>
   */
  public static ViewAction swipeLeft() {
    return actionWithAssertions(new GeneralSwipeAction(Swipe.FAST, GeneralLocation.translate(GeneralLocation.CENTER_RIGHT, -EDGE_FUZZ_FACTOR, 0.0F),
        GeneralLocation.CENTER_LEFT, Press.FINGER));
  }

  /**
   * Returns an action that performs a swipe left-to-right across the vertical center of the
   * view.<br>
   * <br>
   * View constraints:
   * <ul>
   * <li>must be displayed on screen
   * <ul>
   */
  public static ViewAction swipeRight() {
    return actionWithAssertions(new GeneralSwipeAction(Swipe.FAST, GeneralLocation.translate(GeneralLocation.CENTER_LEFT, EDGE_FUZZ_FACTOR, 0.0F),
        GeneralLocation.CENTER_RIGHT, Press.FINGER));
  }

  public static ViewAction swipeDown() {
    return actionWithAssertions(new GeneralSwipeAction(Swipe.FAST, GeneralLocation.translate(GeneralLocation.TOP_CENTER, 0.0F, EDGE_FUZZ_FACTOR),
            GeneralLocation.BOTTOM_CENTER, Press.FINGER));
  }

  public static ViewAction swipeUp() {
    return actionWithAssertions(new GeneralSwipeAction(Swipe.FAST, GeneralLocation.translate(GeneralLocation.BOTTOM_CENTER, 0.0F, -EDGE_FUZZ_FACTOR),
            GeneralLocation.TOP_CENTER, Press.FINGER));
  }

  /**
   * Returns an action that closes soft keyboard. If the keyboard is already closed, it is a no-op.
   */
  public static ViewAction closeSoftKeyboard() {
    return actionWithAssertions(new CloseKeyboardAction());
  }

  /**
   * Returns an action that presses the current action button (next, done, search, etc) on the IME
   * (Input Method Editor). The selected view will have its onEditorAction method called.
   */
  public static ViewAction pressImeActionButton() {
    return actionWithAssertions(new EditorAction());
  }

  /**
   * Returns an action that clicks the back button.
   */
  public static ViewAction pressBack() {
    return actionWithAssertions(pressKey(KeyEvent.KEYCODE_BACK));
  }

  /**
   * Returns an action that presses the hardware menu key.
   */
  public static ViewAction pressMenuKey() {
    return actionWithAssertions(pressKey(KeyEvent.KEYCODE_MENU));
  }

  /**
   * Returns an action that presses the key specified by the keyCode (eg. Keyevent.KEYCODE_BACK).
   */
  public static ViewAction pressKey(int keyCode) {
    return actionWithAssertions(new KeyEventAction(new TaratorKey.Builder().withKeyCode(keyCode).build()));
  }

  /**
   * Returns an action that presses the specified key with the specified modifiers.
   */
  public static ViewAction pressKey(TaratorKey key) {
    return actionWithAssertions(new KeyEventAction(key));
  }

  /**
   * Returns an action that double clicks the view.<br>
   * <br>
   * View preconditions:
   * <ul>
   * <li>must be displayed on screen
   * <ul>
   */
  public static ViewAction doubleClick() {
    return actionWithAssertions(new GeneralClickAction(Tap.DOUBLE, GeneralLocation.VISIBLE_CENTER, Press.FINGER));
  }

  /**
   * Returns an action that long clicks the view.<br>
   *
   * <br>
   * View preconditions:
   * <ul>
   * <li>must be displayed on screen
   * <ul>
   */
  public static ViewAction longClick() {
    return actionWithAssertions(new GeneralClickAction(Tap.LONG, GeneralLocation.VISIBLE_CENTER, Press.FINGER));
  }

  /**
   * Returns an action that scrolls to the view.<br>
   * <br>
   * View preconditions:
   * <ul>
   * <li>must be a descendant of ScrollView
   * <li>must have visibility set to View.VISIBLE
   * <ul>
   */
  public static ViewAction scrollTo() {
    return actionWithAssertions(new ScrollToAction());
  }

  /**
   * Returns an action that types the provided string into the view.
   * Appending a \n to the end of the string translates to a ENTER key event. Note: this method
   * does not change cursor position in the focused view - text is inserted at the location where
   * the cursor is currently pointed.<br>
   * <br>
   * View preconditions:
   * <ul>
   * <li>must be displayed on screen
   * <li>must support input methods
   * <li>must be already focused
   * <ul>
   */
  public static ViewAction typeTextIntoFocusedView(String stringToBeTyped) {
    return actionWithAssertions(new TypeTextAction(stringToBeTyped, false /* tapToFocus */));
  }

  /**
   * Returns an action that selects the view (by clicking on it) and types the provided string into
   * the view. Appending a \n to the end of the string translates to a ENTER key event. Note: this
   * method performs a tap on the view before typing to force the view into focus, if the view 
   * already contains text this tap may place the cursor at an arbitrary position within the text.
   * <br>
   * <br>
   * View preconditions:
   * <ul>
   * <li>must be displayed on screen
   * <li>must support input methods
   * <ul>
   */
  public static ViewAction typeText(String stringToBeTyped) {
    return actionWithAssertions(new TypeTextAction(stringToBeTyped));
  }

  public static ViewAction replaceText(@Nonnull String stringToBeSet) {
    return actionWithAssertions(new ReplaceTextAction(stringToBeSet));
  }

  public static ViewAction openLinkWithText(String linkText) {
    return actionWithAssertions(openLinkWithText(Matchers.is(linkText)));
  }

  public static ViewAction openLinkWithText(Matcher<String> linkTextMatcher) {
    return actionWithAssertions(openLink(linkTextMatcher, Matchers.any(Uri.class)));
  }

  public static ViewAction openLinkWithUri(String uri) {
    return actionWithAssertions(openLinkWithUri(Matchers.is(Uri.parse(uri))));
  }

  public static ViewAction openLinkWithUri(Matcher<Uri> uriMatcher) {
    return actionWithAssertions(openLink(Matchers.any(String.class), uriMatcher));
  }

  public static ViewAction openLink(Matcher<String> linkTextMatcher, Matcher<Uri> uriMatcher) {
    Preconditions.checkNotNull(linkTextMatcher);
    Preconditions.checkNotNull(uriMatcher);
    return actionWithAssertions(new OpenLinkAction(linkTextMatcher, uriMatcher));
  }

  public static ViewAction setProgress(int progressToBeSet) {
    return new SetProgressAction(progressToBeSet, SetProgressAction.ProgressType.PRIMARY);
  }

  public static ViewAction setSecondaryProgress(int progressToBeSet) {
    return new SetProgressAction(progressToBeSet, SetProgressAction.ProgressType.SECONDARY);
  }


}
