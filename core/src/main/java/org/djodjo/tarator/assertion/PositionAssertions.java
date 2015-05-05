package org.djodjo.tarator.assertion;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import org.djodjo.tarator.AmbiguousViewMatcherException;
import org.djodjo.tarator.NoMatchingViewException;
import org.djodjo.tarator.ViewAssertion;
import org.djodjo.tarator.matcher.ViewMatchers;
import org.djodjo.tarator.util.HumanReadables;
import org.djodjo.tarator.util.TreeIterables;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;

import java.util.Iterator;

public final class PositionAssertions {
    private static final String TAG = "PositionAssertions";

    private PositionAssertions() {
    }

    public static ViewAssertion isLeftOf(Matcher<View> matcher) {
        return relativePositionOf(matcher, PositionAssertions.Position.LEFT_OF);
    }

    public static ViewAssertion isRightOf(Matcher<View> matcher) {
        return relativePositionOf(matcher, PositionAssertions.Position.RIGHT_OF);
    }

    public static ViewAssertion isLeftAlignedWith(Matcher<View> matcher) {
        return relativePositionOf(matcher, PositionAssertions.Position.LEFT_ALIGNED);
    }

    public static ViewAssertion isRightAlignedWith(Matcher<View> matcher) {
        return relativePositionOf(matcher, PositionAssertions.Position.RIGHT_ALIGNED);
    }

    public static ViewAssertion isAbove(Matcher<View> matcher) {
        return relativePositionOf(matcher, PositionAssertions.Position.ABOVE);
    }

    public static ViewAssertion isBelow(Matcher<View> matcher) {
        return relativePositionOf(matcher, PositionAssertions.Position.BELOW);
    }

    public static ViewAssertion isBottomAlignedWith(Matcher<View> matcher) {
        return relativePositionOf(matcher, PositionAssertions.Position.BOTTOM_ALIGNED);
    }

    public static ViewAssertion isTopAlignedWith(Matcher<View> matcher) {
        return relativePositionOf(matcher, PositionAssertions.Position.TOP_ALIGNED);
    }

    static ViewAssertion relativePositionOf(final Matcher<View> viewMatcher, final PositionAssertions.Position position) {
        Preconditions.checkNotNull(viewMatcher);
        return new ViewAssertion() {
            public void check(View foundView, NoMatchingViewException noViewException) {
                StringDescription description = new StringDescription();
                if(noViewException != null) {
                    description.appendText(String.format("\' check could not be performed because view \'%s\' was not found.\n", new Object[]{noViewException.getViewMatcherDescription()}));
                    Log.e("PositionAssertions", description.toString());
                    throw noViewException;
                } else {
                    description.appendText("View:").appendText(HumanReadables.describe(foundView)).appendText(" is not ").appendText(position.toString()).appendText(" view ").appendText(viewMatcher.toString());
                    ViewMatchers.assertThat(description.toString(), Boolean.valueOf(PositionAssertions.isRelativePosition(foundView, PositionAssertions.findView(viewMatcher, PositionAssertions.getTopViewGroup(foundView)), position)), Matchers.is(Boolean.valueOf(true)));
                }
            }
        };
    }

    static View findView(final Matcher<View> toView, View root) {
        Preconditions.checkNotNull(toView);
        Preconditions.checkNotNull(root);
        Predicate viewPredicate = new Predicate<View>() {
            public boolean apply(View input) {
                return toView.matches(input);
            }
        };
        Iterator matchedViewIterator = Iterables.filter(TreeIterables.breadthFirstViewTraversal(root), viewPredicate).iterator();

        View matchedView;
        for(matchedView = null; matchedViewIterator.hasNext(); matchedView = (View)matchedViewIterator.next()) {
            if(matchedView != null) {
                throw (new AmbiguousViewMatcherException.Builder()).withRootView(root).withViewMatcher(toView).withView1(matchedView).withView2((View)matchedViewIterator.next()).withOtherAmbiguousViews((View[]) Iterators.toArray(matchedViewIterator, View.class)).build();
            }
        }

        if(matchedView == null) {
            throw (new NoMatchingViewException.Builder()).withViewMatcher(toView).withRootView(root).build();
        } else {
            return matchedView;
        }
    }

    private static ViewGroup getTopViewGroup(View view) {
        ViewParent currentParent = view.getParent();

        ViewGroup topView;
        for(topView = null; currentParent != null; currentParent = currentParent.getParent()) {
            if(currentParent instanceof ViewGroup) {
                topView = (ViewGroup)currentParent;
            }
        }

        return topView;
    }

    static boolean isRelativePosition(View view1, View view2, PositionAssertions.Position position) {
        int[] location1 = new int[2];
        int[] location2 = new int[2];
        view1.getLocationOnScreen(location1);
        view2.getLocationOnScreen(location2);
        switch(position) {
            case LEFT_OF:
                return location1[0] + view1.getWidth() <= location2[0];
            case RIGHT_OF:
                return location2[0] + view2.getWidth() <= location1[0];
            case ABOVE:
                return location1[1] + view1.getHeight() <= location2[1];
            case BELOW:
                return location2[1] + view2.getHeight() <= location1[1];
            case LEFT_ALIGNED:
                return location1[0] == location2[0];
            case RIGHT_ALIGNED:
                return location1[0] + view1.getWidth() == location2[0] + view2.getWidth();
            case TOP_ALIGNED:
                return location1[1] == location2[1];
            case BOTTOM_ALIGNED:
                return location1[1] + view1.getHeight() == location2[1] + view2.getHeight();
            default:
                return false;
        }
    }

    static enum Position {
        LEFT_OF("left of"),
        RIGHT_OF("right of"),
        ABOVE("above"),
        BELOW("below"),
        LEFT_ALIGNED("aligned left with"),
        RIGHT_ALIGNED("aligned right with"),
        TOP_ALIGNED("aligned top with"),
        BOTTOM_ALIGNED("aligned bottom with");

        private final String positionValue;

        private Position(String value) {
            this.positionValue = value;
        }

        public String toString() {
            return this.positionValue;
        }
    }
}
