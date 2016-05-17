package org.djodjo.tarator.assertion;

import android.graphics.Rect;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import junit.framework.AssertionFailedError;

import org.djodjo.tarator.NoMatchingViewException;
import org.djodjo.tarator.ViewAssertion;
import org.djodjo.tarator.matcher.LayoutMatchers;
import org.djodjo.tarator.matcher.ViewMatchers;
import org.djodjo.tarator.util.HumanReadables;
import org.djodjo.tarator.util.TreeIterables;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.Iterator;
import java.util.LinkedList;

public final class LayoutAssertions {
    private LayoutAssertions() {
    }

    public static ViewAssertion noEllipsizedText() {
        return ViewAssertions.selectedDescendantsMatch(ViewMatchers.isAssignableFrom(TextView.class), Matchers.not(LayoutMatchers.hasEllipsizedText()));
    }

    public static ViewAssertion noMultilineButtons() {
        return ViewAssertions.selectedDescendantsMatch(ViewMatchers.isAssignableFrom(Button.class), Matchers.not(LayoutMatchers.hasMultilineText()));
    }

    public static ViewAssertion noOverlaps(final Matcher<View> selector) {
        Preconditions.checkNotNull(selector);
        return new ViewAssertion() {
            public void check(View view, NoMatchingViewException noViewException) {
                Predicate viewPredicate = new Predicate<View>() {
                    public boolean apply(View input) {
                        return selector.matches(input);
                    }
                };
                if (noViewException != null) {
                    throw noViewException;
                } else {
                    Iterator selectedViewIterator = Iterables.filter(TreeIterables.breadthFirstViewTraversal(view), viewPredicate).iterator();
                    LinkedList prevViews = new LinkedList();
                    StringBuilder errorMessage = new StringBuilder();

                    while (selectedViewIterator.hasNext()) {
                        View selectedView = (View) selectedViewIterator.next();
                        Rect viewRect = LayoutAssertions.getRect(selectedView);
                        if (!viewRect.isEmpty() && (!(selectedView instanceof TextView) || ((TextView) selectedView).getText().length() != 0)) {
                            Iterator iterator = prevViews.iterator();

                            while (iterator.hasNext()) {
                                View prevView = (View) iterator.next();
                                if (!(selectedView instanceof ImageView) || !(prevView instanceof ImageView)) {
                                    Rect prevRect = LayoutAssertions.getRect(prevView);
                                    if (Rect.intersects(viewRect, prevRect)) {
                                        if (errorMessage.length() > 0) {
                                            errorMessage.append(",\n\n");
                                        }

                                        errorMessage.append(String.format("%s overlaps\n%s", new Object[]{HumanReadables.describe(selectedView), HumanReadables.describe(prevView)}));
                                        break;
                                    }
                                }
                            }

                            prevViews.add(selectedView);
                        }
                    }

                    if (errorMessage.length() > 0) {
                        throw new AssertionFailedError(errorMessage.toString());
                    }
                }
            }
        };
    }

    public static ViewAssertion noOverlaps() {
        return noOverlaps(Matchers.allOf(new Matcher[]{ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE), Matchers.anyOf(new Matcher[]{ViewMatchers.isAssignableFrom(TextView.class), ViewMatchers.isAssignableFrom(ImageView.class)})}));
    }

    private static Rect getRect(View view) {
        int[] coords = new int[]{0, 0};
        view.getLocationOnScreen(coords);
        return new Rect(coords[0], coords[1], coords[0] + view.getWidth() - 1, coords[1] + view.getHeight() - 1);
    }
}