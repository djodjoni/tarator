package org.djodjo.tarator.support.v7.matcher;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public final class ViewHolderMatchers {

    private ViewHolderMatchers(){};

    public static <VH extends RecyclerView.ViewHolder> Matcher<VH> itemViewMatcher(final Matcher<View> itemViewMatcher) {
        return new TypeSafeMatcher<VH>() {
            public boolean matchesSafely(RecyclerView.ViewHolder viewHolder) {
                return itemViewMatcher.matches(viewHolder.itemView);
            }

            public void describeTo(Description description) {
                description.appendText("holder with view: ");
                itemViewMatcher.describeTo(description);
            }
        };
    }
    
}
