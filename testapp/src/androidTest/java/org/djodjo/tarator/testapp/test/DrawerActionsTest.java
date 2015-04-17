package org.djodjo.tarator.testapp.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import org.djodjo.tarator.support.v4.action.DrawerActions;
import org.djodjo.tarator.testapp.DrawerActivity;
import org.djodjo.tarator.testapp.R;
import org.hamcrest.Matchers;

import static org.djodjo.tarator.Tarator.onData;
import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.action.ViewActions.click;
import static org.djodjo.tarator.assertion.ViewAssertions.matches;
import static org.djodjo.tarator.matcher.ViewMatchers.withId;
import static org.djodjo.tarator.matcher.ViewMatchers.withText;
import static org.djodjo.tarator.support.v4.action.DrawerActions.closeDrawer;
import static org.djodjo.tarator.support.v4.action.DrawerActions.openDrawer;
import static org.djodjo.tarator.support.v4.matcher.DrawerMatchers.isClosed;
import static org.djodjo.tarator.support.v4.matcher.DrawerMatchers.isOpen;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Demonstrates use of {@link DrawerActions}.
 */
@LargeTest
public class DrawerActionsTest  extends ActivityInstrumentationTestCase2<DrawerActivity> {

  public DrawerActionsTest() {
    super(DrawerActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    getActivity();
  }

  public void testOpenAndCloseDrawer() {
    // Drawer should not be open to start.
    onView(withId(R.id.drawer_layout)).check(matches(isClosed()));

    openDrawer(R.id.drawer_layout);

    // The drawer should now be open.
    onView(withId(R.id.drawer_layout)).check(matches(isOpen()));

    closeDrawer(R.id.drawer_layout);

    // Drawer should be closed again.
    onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
  }

  @SuppressWarnings("unchecked")
  public void testDrawerOpenAndClick() {
    openDrawer(R.id.drawer_layout);

    onView(withId(R.id.drawer_layout)).check(matches(isOpen()));

    // Click an item in the drawer. We use onData because the drawer is backed by a ListView, and
    // the item may not necessarily be visible in the view hierarchy.
    int rowIndex = 2;
    String rowContents = DrawerActivity.DRAWER_CONTENTS[rowIndex];
    onData(Matchers.<Object>allOf(is(instanceOf(String.class)), Matchers.<Object>is(rowContents))).perform(click());

    // clicking the item should close the drawer.
    onView(withId(R.id.drawer_layout)).check(matches(isClosed()));

    // The text view will now display "You picked: Pickle"
    onView(withId(R.id.drawer_text_view)).check(matches(withText("You picked: " + rowContents)));
  }
}
