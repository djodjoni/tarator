package org.djodjo.tarator.testapp.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import org.djodjo.tarator.testapp.R;
import org.djodjo.tarator.testapp.SimpleActivity;
import org.hamcrest.Matchers;

import static org.djodjo.tarator.Tarator.onData;
import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.Tarator.pressBack;
import static org.djodjo.tarator.action.ViewActions.click;
import static org.djodjo.tarator.action.ViewActions.closeSoftKeyboard;
import static org.djodjo.tarator.action.ViewActions.typeText;
import static org.djodjo.tarator.assertion.ViewAssertions.matches;
import static org.djodjo.tarator.matcher.ViewMatchers.withId;
import static org.djodjo.tarator.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Highlights basic
 * {@link org.djodjo.tarator.Tarator#onView(org.hamcrest.Matcher)}
 * functionality.
 */
@LargeTest
public class BasicTest extends ActivityInstrumentationTestCase2<SimpleActivity> {

  public BasicTest() {
    super(SimpleActivity.class);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    // Tarator will not launch our activity for us, we must launch it via getActivity().
    getActivity();
  }

  public void testSimpleClickAndCheckText() {
    onView(withId(R.id.button_simple))
        .perform(click());

    onView(withId(R.id.text_simple))
        .check(matches(withText("Hello Tarator!")));
  }

  public void testTypingAndPressBack() {
    // Close soft keyboard after type to avoid issues on devices with soft keyboard.
    onView(withId(R.id.sendtext_simple))
        .perform(typeText("Have a cup of Tarator."), closeSoftKeyboard());

    onView(withId(R.id.send_simple))
        .perform(click());

    // Clicking launches a new activity that shows the text entered above. You don't need to do
    // anything special to handle the activity transitions. Tarator takes care of waiting for the
    // new activity to be resumed and its view hierarchy to be laid out.
    onView(withId(R.id.display_data))
        .check(matches(withText(("Have a cup of Tarator."))));

    // Going back to the previous activity - lets make sure our text was perserved.
    pressBack();

    onView(withId(R.id.sendtext_simple))
        .check(matches(withText(containsString("Tarator"))));
  }

  @SuppressWarnings("unchecked")
  public void testClickOnSpinnerItemAmericano(){
    // Open the spinner.
    onView(withId(R.id.spinner_simple))
      .perform(click());
    // Spinner creates a List View with its contents - this can be very long and the element not
    // contributed to the ViewHierarchy - by using onData we force our desired element into the
    // view hierarchy.
    onData(Matchers.<Object>allOf(is(instanceOf(String.class)), Matchers.<Object>is("Yogurt")))
      .perform(click());

    onView(withId(R.id.spinnertext_simple))
      .check(matches(withText(containsString("Yogurt"))));
  }
}

