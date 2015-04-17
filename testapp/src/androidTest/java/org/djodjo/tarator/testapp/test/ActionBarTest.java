package org.djodjo.tarator.testapp.test;

import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.Tarator.openActionBarOverflowOrOptionsMenu;
import static org.djodjo.tarator.Tarator.openContextualActionModeOverflowMenu;
import static org.djodjo.tarator.action.ViewActions.click;
import static org.djodjo.tarator.assertion.ViewAssertions.matches;
import static org.djodjo.tarator.matcher.ViewMatchers.withId;
import static org.djodjo.tarator.matcher.ViewMatchers.withText;

import org.djodjo.tarator.testapp.ActionBarTestActivity;
import org.djodjo.tarator.testapp.R;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

/**
 * Demonstrates Tarator with action bar and contextual action mode.
 * {@link openActionBarOverflowOrOptionsMenu()} opens the overflow menu from an action bar.
 * {@link openContextualActionModeOverflowMenu()} opens the overflow menu from an contextual action
 * mode.
 */
@LargeTest
public class ActionBarTest extends ActivityInstrumentationTestCase2<ActionBarTestActivity> {

  public ActionBarTest() {
    super(ActionBarTestActivity.class);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    // Tarator will not launch our activity for us, we must launch it via getActivity().
    getActivity();
  }

  @SuppressWarnings("unchecked")
  public void testClickActionBarItem() {
    onView(withId(R.id.hide_contextual_action_bar))
      .perform(click());

    onView(withId(R.id.action_save))
      .perform(click());

    onView(withId(R.id.text_action_bar_result))
      .check(matches(withText("Save")));
  }

  @SuppressWarnings("unchecked")
  public void testClickActionModeItem() {
    onView(withId(R.id.show_contextual_action_bar))
      .perform(click());

    onView((withId(R.id.action_lock)))
      .perform(click());

    onView(withId(R.id.text_action_bar_result))
      .check(matches(withText("Lock")));
  }


  @SuppressWarnings("unchecked")
  public void testActionBarOverflow() {
    onView(withId(R.id.hide_contextual_action_bar))
      .perform(click());

    // Open the overflow menu from action bar
    openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

    onView(withText("World"))
      .perform(click());

    onView(withId(R.id.text_action_bar_result))
      .check(matches(withText("World")));
  }

  @SuppressWarnings("unchecked")
  public void testActionModeOverflow() {
    onView(withId(R.id.show_contextual_action_bar))
      .perform(click());

    // Open the overflow menu from contextual action mode.
    openContextualActionModeOverflowMenu();

    onView(withText("Key"))
      .perform(click());

    onView(withId(R.id.text_action_bar_result))
      .check(matches(withText("Key")));
  }
}
