package org.djodjo.tarator.support.v7.action;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.djodjo.tarator.PerformException;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewAction;
import org.djodjo.tarator.matcher.ViewMatchers;
import org.djodjo.tarator.util.HumanReadables;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;


final class ActionOnItemAtPositionViewAction<VH extends RecyclerView.ViewHolder> implements ViewAction {
    private final int position;
    private final ViewAction viewAction;

    ActionOnItemAtPositionViewAction(int position, ViewAction viewAction) {
        this.position = position;
        this.viewAction = viewAction;
    }

    public Matcher<View> getConstraints() {
        return Matchers.allOf(new Matcher[]{ViewMatchers.isAssignableFrom(RecyclerView.class), ViewMatchers.isDisplayed()});
    }

    public String getDescription() {
        return "actionOnItemAtPosition performing ViewAction: " + this.viewAction.getDescription() + " on item at position: " + this.position;
    }

    public void perform(UiController uiController, View view) {
        RecyclerView recyclerView = (RecyclerView) view;
        (new ScrollToPositionViewAction(this.position, false)).perform(uiController, view);
        uiController.loopMainThreadUntilIdle();
        RecyclerView.ViewHolder viewHolderForPosition = recyclerView.findViewHolderForPosition(this.position);
        if (null == viewHolderForPosition) {
            throw (new PerformException.Builder()).withActionDescription(this.toString()).withViewDescription(HumanReadables.describe(view)).withCause(new IllegalStateException("No view holder at position: " + this.position)).build();
        } else {
            View viewAtPosition = viewHolderForPosition.itemView;
            if (null == viewAtPosition) {
                throw (new PerformException.Builder()).withActionDescription(this.toString()).withViewDescription(HumanReadables.describe(viewAtPosition)).withCause(new IllegalStateException("No view at position: " + this.position)).build();
            } else {
                this.viewAction.perform(uiController, viewAtPosition);
            }
        }
    }
}
