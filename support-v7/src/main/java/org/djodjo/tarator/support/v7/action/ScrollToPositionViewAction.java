package org.djodjo.tarator.support.v7.action;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewAction;
import org.djodjo.tarator.matcher.ViewMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;


public final class ScrollToPositionViewAction implements ViewAction {
    private final int position;
    private final boolean smooth;

    public ScrollToPositionViewAction(int position, boolean smooth) {
        this.position = position;
        this.smooth = smooth;
    }

    public Matcher<View> getConstraints() {
        return Matchers.allOf(new Matcher[]{ViewMatchers.isAssignableFrom(RecyclerView.class), ViewMatchers.isDisplayed()});
    }

    public String getDescription() {
        return "scroll RecyclerView to position: " + this.position;
    }

    public void perform(UiController uiController, View view) {
        RecyclerView recyclerView = (RecyclerView) view;
        if (smooth) {
            recyclerView.smoothScrollToPosition(this.position);
        } else {
            recyclerView.scrollToPosition(this.position);
        }
    }
}
