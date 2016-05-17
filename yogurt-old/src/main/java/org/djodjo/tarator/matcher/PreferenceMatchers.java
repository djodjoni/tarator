package org.djodjo.tarator.matcher;

import android.content.res.Resources;
import android.preference.Preference;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.is;

/**
 * A collection of hamcrest matchers that match {@link Preference}s.
 */
public final class PreferenceMatchers {

  private PreferenceMatchers() {}

  public static Matcher<Preference> withSummary(final int resourceId) {
    return new TypeSafeMatcher<Preference>() {
      private String resourceName = null;
      private String expectedText = null;

      @Override
      public void describeTo(Description description) {
        description.appendText(" with summary string from resource id: ");
        description.appendValue(resourceId);
        if (null != resourceName) {
          description.appendText("[");
          description.appendText(resourceName);
          description.appendText("]");
        }
        if (null != expectedText) {
          description.appendText(" value: " );
          description.appendText(expectedText);
        }
      }

      @Override
      public boolean matchesSafely(Preference preference) {
        if (null == expectedText) {
          try {
            expectedText = preference.getContext().getResources().getString(resourceId);
            resourceName = preference.getContext().getResources().getResourceEntryName(resourceId);
          } catch (Resources.NotFoundException ignored) {
            /* view could be from a context unaware of the resource id. */
          }
        }
        if (null != expectedText) {
          return expectedText.equals(preference.getSummary().toString());
        } else {
          return false;
        }
      }
    };
  }

  public static Matcher<Preference> withSummaryText(String summary) {
    return withSummaryText(is(summary));
  }

  public static Matcher<Preference> withSummaryText(final Matcher<String> summaryMatcher) {
    return new TypeSafeMatcher<Preference>() {
      @Override
      public void describeTo(Description description) {
        description.appendText(" a preference with summary matching: ");
        summaryMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(Preference pref) {
        String summary = pref.getSummary().toString();
        return summaryMatcher.matches(summary);
      }
    };
  }

  public static Matcher<Preference> withTitle(final int resourceId) {
    return new TypeSafeMatcher<Preference>() {
      private String resourceName = null;
      private String expectedText = null;

      public void describeTo(Description description) {
        description.appendText(" with title string from resource id: ");
        description.appendValue(Integer.valueOf(resourceId));
        if(null != this.resourceName) {
          description.appendText("[");
          description.appendText(this.resourceName);
          description.appendText("]");
        }

        if(null != this.expectedText) {
          description.appendText(" value: ");
          description.appendText(this.expectedText);
        }

      }

      public boolean matchesSafely(Preference preference) {
        if(null == this.expectedText) {
          try {
            this.expectedText = preference.getContext().getResources().getString(resourceId);
            this.resourceName = preference.getContext().getResources().getResourceEntryName(resourceId);
          } catch (Resources.NotFoundException var3) {
            ;
          }
        }

        return null != this.expectedText && preference.getTitle() != null?this.expectedText.equals(preference.getTitle().toString()):false;
      }
    };
  }

  public static Matcher<Preference> withTitleText(String title) {
    return withTitleText(Matchers.is(title));
  }

  public static Matcher<Preference> withTitleText(final Matcher<String> titleMatcher) {
    return new TypeSafeMatcher<Preference>() {
      public void describeTo(Description description) {
        description.appendText(" a preference with title matching: ");
        titleMatcher.describeTo(description);
      }

      public boolean matchesSafely(Preference pref) {
        if(pref.getTitle() == null) {
          return false;
        } else {
          String title = pref.getTitle().toString();
          return titleMatcher.matches(title);
        }
      }
    };
  }

  public static Matcher<Preference> isEnabled() {
    return new TypeSafeMatcher<Preference>() {
      @Override
      public void describeTo(Description description) {
        description.appendText(" is an enabled preference");
      }

      @Override
      public boolean matchesSafely(Preference pref) {
        return pref.isEnabled();
      }
    };
  }

  public static Matcher<Preference> withKey(String key) {
    return withKey(is(key));
  }

  public static Matcher<Preference> withKey(final Matcher<String> keyMatcher) {
    return new TypeSafeMatcher<Preference>() {
      @Override
      public void describeTo(Description description) {
        description.appendText(" preference with key matching: ");
        keyMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(Preference pref) {
        return keyMatcher.matches(pref.getKey());
      }
    };
  }
}
