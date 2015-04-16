package org.djodjo.tarator.testapp.test;

import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.action.ViewActions.click;
import static org.djodjo.tarator.action.ViewActions.scrollTo;
import static org.djodjo.tarator.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

import org.djodjo.tarator.testapp.R;
import org.djodjo.tarator.testapp.ScrollActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

/**
 * Demonstrates the usage of
 * {@link org.djodjo.tarator.action.ViewActions#scrollTo()}.
 */
@LargeTest
public class ScrollToTest extends ActivityInstrumentationTestCase2<ScrollActivity> {

  @SuppressWarnings("deprecation")
  public ScrollToTest() {
    // This constructor was deprecated - but we want to support lower API levels.
    super("org.djodjo.tarator.testapp", ScrollActivity.class);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    // Tarator will not launch our activity for us, we must launch it via getActivity().
    getActivity();
  }

  // You can pass more than one action to perform. This is useful if you are performing two actions
  // back-to-back on the same view.
  // Note - scrollTo is a no-op if the view is already displayed on the screen.
  public void testScrollToInScrollView() {
    onView(withId(is(R.id.bottom_left)))
      .perform(scrollTo(), click());
  }
}
