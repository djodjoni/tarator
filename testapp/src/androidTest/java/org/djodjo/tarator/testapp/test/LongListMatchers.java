package org.djodjo.tarator.testapp.test;

import org.djodjo.tarator.matcher.BoundedMatcher;
import org.djodjo.tarator.testapp.LongListActivity;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Static utility methods to create {@link org.hamcrest.Matcher} instances that can be applied to the data
 * objects created by {@link org.djodjo.tarator.testapp.LongListActivity}.
 * <p>
 * These matchers are used by the
 * {@link org.djodjo.tarator.Tarator#onData(org.hamcrest.Matcher)} API and are
 * applied against the data exposed by @{link android.widget.ListView#getAdapter()}.
 * </p>
 * <p>
 * In LongListActivity's case - each row is a Map containing 2 key value pairs. The key "STR" is
 * mapped to a String which will be rendered into a TextView with the R.id.item_content. The other
 * key "LEN" is an Integer which is the length of the string "STR" refers to. This length is
 * rendered into a TextView with the id R.id.item_size.
 * </p>
 */
public final class LongListMatchers {

  private LongListMatchers() { }


  /**
   * Creates a matcher against the text stored in R.id.item_content. This text is roughly
   * "item: $row_number".
   */
  public static Matcher<Object> withItemContent(String expectedText) {
    // use preconditions to fail fast when a test is creating an invalid matcher.
    checkNotNull(expectedText);
    return withItemContent(equalTo(expectedText));
  }

  /**
   * Creates a matcher against the text stored in R.id.item_content. This text is roughly
   * "item: $row_number".
   */
  @SuppressWarnings("rawtypes")
  public static Matcher<Object> withItemContent(final Matcher<String> itemTextMatcher) {
    // use preconditions to fail fast when a test is creating an invalid matcher.
    checkNotNull(itemTextMatcher);
    return new BoundedMatcher<Object, Map>(Map.class) {
      @Override
      public boolean matchesSafely(Map map) {
        return hasEntry(equalTo("STR"), itemTextMatcher).matches(map);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("with item content: ");
        itemTextMatcher.describeTo(description);
      }
    };
  }

  /**
   * Creates a matcher against the text stored in R.id.item_size. This text is the size of the text
   * printed in R.id.item_content.
   */
  public static Matcher<Object> withItemSize(int itemSize) {
    // use preconditions to fail fast when a test is creating an invalid matcher.
    checkArgument(itemSize > -1);
    return withItemSize(equalTo(itemSize));
  }

  /**
   * Creates a matcher against the text stored in R.id.item_size. This text is the size of the text
   * printed in R.id.item_content.
   */
  @SuppressWarnings("rawtypes")
  public static Matcher<Object> withItemSize(final Matcher<Integer> itemSizeMatcher) {
    // use preconditions to fail fast when a test is creating an invalid matcher.
    checkNotNull(itemSizeMatcher);
    return new BoundedMatcher<Object, Map>(Map.class) {
      @Override
      public boolean matchesSafely(Map map) {
        return hasEntry(equalTo("LEN"), itemSizeMatcher).matches(map);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("with item size: ");
        itemSizeMatcher.describeTo(description);
      }
    };
  }

  /**
   * Creates a matcher against the footer of this list view.
   */
  @SuppressWarnings("unchecked")
  public static Matcher<Object> isFooter() {
    // This depends on LongListActivity.FOOTER being passed as data in the addFooterView method.
    return allOf(Matchers.<Object>is(instanceOf(String.class)), Matchers.<Object>is(LongListActivity.FOOTER));
  }

}
