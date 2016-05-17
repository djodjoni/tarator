package org.djodjo.tarator.assertion;

import android.util.Log;
import android.view.View;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import junit.framework.AssertionFailedError;

import org.djodjo.tarator.NoMatchingViewException;
import org.djodjo.tarator.ViewAssertion;
import org.djodjo.tarator.util.HumanReadables;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.djodjo.tarator.matcher.ViewMatchers.assertThat;
import static org.djodjo.tarator.util.TreeIterables.breadthFirstViewTraversal;
import static org.hamcrest.Matchers.is;

/**
 * A collection of common {@link ViewAssertion}s.
 */
public final class ViewAssertions {

  private static final String TAG = ViewAssertions.class.getSimpleName();


  private ViewAssertions() {}

  /**
   * Returns an assert that ensures the view matcher does not find any matching view in the
   * hierarchy.
   */
  public static ViewAssertion doesNotExist() {
    return new ViewAssertion() {
      @Override
      public void check(View view, NoMatchingViewException noView) {
        if (view != null) {
          assertThat("View is present in the hierarchy: " + HumanReadables.describe(view),
              true, is(false));
        }
      }
    };
  }

  /**
   * Returns a generic {@link ViewAssertion} that asserts that a view exists in the view hierarchy
   * and is matched by the given view matcher.
   */
  public static ViewAssertion matches(final Matcher<? super View> viewMatcher) {
    checkNotNull(viewMatcher);
    return new ViewAssertion() {
      @Override
      public void check(View view, NoMatchingViewException noViewException) {
        StringDescription description = new StringDescription();
        description.appendText("'");
        viewMatcher.describeTo(description);
        if (noViewException != null) {
          description.appendText(String.format(
              "' check could not be performed because view '%s' was not found.\n", viewMatcher));
          Log.e(TAG, description.toString());
          throw noViewException;
        } else {
          // TODO(user): ideally, we should append the matcher used to find the view
          // This can be done in DefaultFailureHandler (just like we currently to with
          // PerformException)
          description.appendText("' doesn't match the selected view.");
          assertThat(description.toString(), view, viewMatcher);
        }
      }
    };
  }


  /**
   * Returns a generic {@link ViewAssertion} that asserts that the descendant views selected by the
   * selector match the specified matcher.
   *
   *  Example: onView(rootView).check(selectedDescendantsMatch(
   * not(isAssignableFrom(TextView.class)), hasContentDescription()));
   */
  public static ViewAssertion selectedDescendantsMatch(
      final Matcher<View> selector, final Matcher<View> matcher) {
    return new ViewAssertion() {
      @SuppressWarnings("unchecked")
      @Override
      public void check(View view, NoMatchingViewException noViewException) {
        Preconditions.checkNotNull(view);
        View rootView = view;

        final Predicate<View> viewPredicate = new Predicate<View>() {
          @Override
          public boolean apply(View input) {
            return selector.matches(input);
          }
        };

        Iterator<View> selectedViewIterator =
            Iterables.filter(breadthFirstViewTraversal(rootView), viewPredicate).iterator();

        List<View> nonMatchingViews = new ArrayList<View>();
        while (selectedViewIterator.hasNext()) {
          View selectedView = selectedViewIterator.next();

          if (!matcher.matches(selectedView)) {
            nonMatchingViews.add(selectedView);
          }
        }

        if (nonMatchingViews.size() > 0) {
          String errorMessage = HumanReadables.getViewHierarchyErrorMessage(rootView,
              nonMatchingViews,
              String.format("At least one view did not match the required matcher: %s", matcher),
              "****DOES NOT MATCH****");
          throw new AssertionFailedError(errorMessage);
        }
      }
    };
  }
}
