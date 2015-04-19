package org.djodjo.tarator.matcher;

import android.text.Layout;
import android.widget.TextView;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public final class LayoutMatchers {
    private LayoutMatchers() {
    }

    public static Matcher hasEllipsizedText() {
        return new TypeSafeMatcher<TextView>() {
            public void describeTo(Description description) {
                description.appendText("has ellipsized text");
            }

            public boolean matchesSafely(TextView tv) {
                Layout layout = tv.getLayout();
                if(layout == null) {
                    return false;
                } else {
                    int lines = layout.getLineCount();
                    return lines > 0 && layout.getEllipsisCount(lines - 1) > 0;
                }
            }
        };
    }

    public static Matcher hasMultilineText() {
        return new TypeSafeMatcher<TextView>() {
            public void describeTo(Description description) {
                description.appendText("has more than one line of text");
            }

            public boolean matchesSafely(TextView tv) {
                return tv.getLineCount() > 1;
            }
        };
    }
}
