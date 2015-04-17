package org.djodjo.tarator.testapp.test;

import static org.djodjo.tarator.Tarator.onData;
import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.action.ViewActions.click;
import static org.djodjo.tarator.assertion.ViewAssertions.matches;
import static org.djodjo.tarator.matcher.ViewMatchers.withId;
import static org.djodjo.tarator.matcher.ViewMatchers.withText;
import static org.djodjo.tarator.testapp.test.LongListMatchers.withItemContent;
import static org.djodjo.tarator.testapp.test.LongListMatchers.withItemSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import org.djodjo.tarator.matcher.ViewMatchers;
import org.djodjo.tarator.testapp.*;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

/**
 * Demonstrates the usage of
 * {@link org.djodjo.tarator.Tarator#onData(org.hamcrest.Matcher)}
 * to match data within list views.
 */
@LargeTest
public class AdapterViewTest extends ActivityInstrumentationTestCase2<LongListActivity> {

  @SuppressWarnings("deprecation")
  public AdapterViewTest() {
    // This constructor was deprecated - but we want to support lower API levels.
    super("org.djodjo.tarator.testapp", LongListActivity.class);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    getActivity();
  }

  public void testClickOnItem50() {
    // The text view "item: 50" may not exist if we haven't scrolled to it.
    // By using onData api we tell Tarator to look into the Adapter for an item matching
    // the matcher we provide it. Tarator will then bring that item into the view hierarchy
    // and we can click on it.

    onData(LongListMatchers.withItemContent("item: 50"))
      .perform(click());

    onView(ViewMatchers.withId(org.djodjo.tarator.testapp.R.id.selection_row_value))
      .check(matches(withText("50")));
  }

  public void testClickOnSpecificChildOfRow60() {
    onData(LongListMatchers.withItemContent("item: 60"))
      .onChildView(ViewMatchers.withId(org.djodjo.tarator.testapp.R.id.item_size)) // resource id of second column from xml layout
      .perform(click());

    onView(ViewMatchers.withId(org.djodjo.tarator.testapp.R.id.selection_row_value))
      .check(matches(withText("60")));

    onView(ViewMatchers.withId(org.djodjo.tarator.testapp.R.id.selection_column_value))
      .check(matches(withText("2")));
  }

  public void testClickOnFirstAndFifthItemOfLength8() {
    onData(Matchers.is(LongListMatchers.withItemSize(8)))
      .atPosition(0)
      .perform(click());

    onView(ViewMatchers.withId(org.djodjo.tarator.testapp.R.id.selection_row_value))
      .check(matches(withText("10")));

    onData(Matchers.is(LongListMatchers.withItemSize(8)))
      .atPosition(4)
      .perform(click());

    onView(ViewMatchers.withId(org.djodjo.tarator.testapp.R.id.selection_row_value))
      .check(matches(withText("14")));
  }

  @SuppressWarnings("unchecked")
  public void testClickFooter() {
    onData(LongListMatchers.isFooter())
      .perform(click());

    onView(ViewMatchers.withId(org.djodjo.tarator.testapp.R.id.selection_row_value))
      .check(matches(withText("100")));
  }

  @SuppressWarnings("unchecked")
  public void testDataItemNotInAdapter(){
    onView(ViewMatchers.withId(org.djodjo.tarator.testapp.R.id.list))
      .check(matches(not(withAdaptedData(LongListMatchers.withItemContent("item: 168")))));
  }

  private static Matcher<View> withAdaptedData(final Matcher<Object> dataMatcher) {
    return new TypeSafeMatcher<View>() {

      @Override
      public void describeTo(Description description) {
        description.appendText("with class name: ");
        dataMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof AdapterView)) {
          return false;
        }
        @SuppressWarnings("rawtypes")
        Adapter adapter = ((AdapterView) view).getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
          if (dataMatcher.matches(adapter.getItem(i))) {
            return true;
          }
        }
        return false;
      }
    };
  }
}
