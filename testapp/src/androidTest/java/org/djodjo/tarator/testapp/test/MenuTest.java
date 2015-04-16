package org.djodjo.tarator.testapp.test;

import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.action.ViewActions.click;
import static org.djodjo.tarator.action.ViewActions.longClick;
import static org.djodjo.tarator.action.ViewActions.pressMenuKey;
import static org.djodjo.tarator.assertion.ViewAssertions.doesNotExist;
import static org.djodjo.tarator.assertion.ViewAssertions.matches;
import static org.djodjo.tarator.matcher.ViewMatchers.isDisplayed;
import static org.djodjo.tarator.matcher.ViewMatchers.isRoot;
import static org.djodjo.tarator.matcher.ViewMatchers.withId;
import static org.djodjo.tarator.matcher.ViewMatchers.withText;

import org.djodjo.tarator.testapp.MenuActivity;
import org.djodjo.tarator.testapp.R;

import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

/**
 * Ensures view root ordering works properly.
 */
@LargeTest
public class MenuTest extends ActivityInstrumentationTestCase2<MenuActivity> {
  @SuppressWarnings("deprecation")
  public MenuTest() {
    // This constructor was deprecated - but we want to support lower API levels.
    super("org.djodjo.tarator.testapp", MenuActivity.class);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    getActivity();
  }

  public void testPopupMenu() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
      // popup menus are post honeycomb.
      return;
    }
    onView(withText(R.string.popup_item_1_text)).check(doesNotExist());
    onView(withId(R.id.popup_button)).perform(click());
    onView(withText(R.string.popup_item_1_text)).check(matches(isDisplayed())).perform(click());

    onView(withId(R.id.text_menu_result)).check(matches(withText(R.string.popup_item_1_text)));
  }

  public void testContextMenu() {
    onView(withText(R.string.context_item_2_text)).check(doesNotExist());
    onView(withId(R.id.text_context_menu)).perform(longClick());
    onView(withText(R.string.context_item_2_text)).check(matches(isDisplayed())).perform(click());

    onView(withId(R.id.text_menu_result)).check(matches(withText(R.string.context_item_2_text)));
  }

  public void testOptionMenu() {
    onView(withText(R.string.options_item_3_text)).check(doesNotExist());
    onView(isRoot()).perform(pressMenuKey());
    onView(withText(R.string.options_item_3_text)).check(matches(isDisplayed())).perform(click());

    onView(withId(R.id.text_menu_result)).check(matches(withText(R.string.options_item_3_text)));
  }
}
