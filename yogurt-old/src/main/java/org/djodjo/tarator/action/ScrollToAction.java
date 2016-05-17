package org.djodjo.tarator.action;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import org.djodjo.tarator.PerformException;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewAction;
import org.djodjo.tarator.matcher.ViewMatchers.Visibility;
import org.djodjo.tarator.util.HumanReadables;
import org.hamcrest.Matcher;

import static org.djodjo.tarator.matcher.ViewMatchers.isAssignableFrom;
import static org.djodjo.tarator.matcher.ViewMatchers.isDescendantOfA;
import static org.djodjo.tarator.matcher.ViewMatchers.isDisplayingAtLeast;
import static org.djodjo.tarator.matcher.ViewMatchers.withEffectiveVisibility;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;

/**
 * Enables scrolling to the given view. View must be a descendant of a ScrollView.
 */
public final class ScrollToAction implements ViewAction {
  private static final String TAG = ScrollToAction.class.getSimpleName();

  @SuppressWarnings("unchecked")
  @Override
  public Matcher<View> getConstraints() {
    return allOf(withEffectiveVisibility(Visibility.VISIBLE), isDescendantOfA(anyOf(
        isAssignableFrom(ScrollView.class), isAssignableFrom(HorizontalScrollView.class))));
  }

  @Override
  public void perform(UiController uiController, View view) {
    if (isDisplayingAtLeast(90).matches(view)) {
      Log.i(TAG, "View is already displayed. Returning.");
      return;
    }
    Rect rect = new Rect();
    view.getDrawingRect(rect);
    if (!view.requestRectangleOnScreen(rect, true /* immediate */)) {
      Log.w(TAG, "Scrolling to view was requested, but none of the parents scrolled.");
    }
    uiController.loopMainThreadUntilIdle();
    if (!isDisplayingAtLeast(90).matches(view)) {
      throw new PerformException.Builder()
        .withActionDescription(this.getDescription())
        .withViewDescription(HumanReadables.describe(view))
        .withCause(new RuntimeException(
              "Scrolling to view was attempted, but the view is not displayed"))
        .build();
    }
  }

  @Override
  public String getDescription() {
    return "scroll to";
  }
}
