package org.djodjo.tarator.support.v7.action;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.djodjo.tarator.Checks;
import org.djodjo.tarator.PerformException;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewAction;
import org.djodjo.tarator.matcher.ViewMatchers;
import org.djodjo.tarator.util.HumanReadables;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.List;


final class ActionOnItemViewAction<VH extends RecyclerView.ViewHolder> implements PositionableRecyclerViewAction {
    private final Matcher<VH> viewHolderMatcher;
    private final ViewAction viewAction;
    private final int atPosition;
    private final ScrollToViewAction<VH> scroller;

    ActionOnItemViewAction(Matcher<VH> viewHolderMatcher, ViewAction viewAction) {
        this(viewHolderMatcher, viewAction, -1);
    }

    private ActionOnItemViewAction(Matcher<VH> viewHolderMatcher, ViewAction viewAction, int atPosition) {
        this.viewHolderMatcher = (Matcher) Checks.checkNotNull(viewHolderMatcher);
        this.viewAction = (ViewAction) Checks.checkNotNull(viewAction);
        this.atPosition = atPosition;
        this.scroller = new ScrollToViewAction(viewHolderMatcher, atPosition);
    }

    public Matcher<View> getConstraints() {
        return Matchers.allOf(new Matcher[]{ViewMatchers.isAssignableFrom(RecyclerView.class), ViewMatchers.isDisplayed()});
    }

    public PositionableRecyclerViewAction atPosition(int position) {
        Checks.checkArgument(position >= 0, "%d is used as an index - must be >= 0", new Object[]{Integer.valueOf(position)});
        return new ActionOnItemViewAction(this.viewHolderMatcher, this.viewAction, position);
    }

    public String getDescription() {
        return this.atPosition == -1 ? String.format("performing ViewAction: %s on item matching: %s", new Object[]{this.viewAction.getDescription(), this.viewHolderMatcher}) : String.format("performing ViewAction: %s on %d-th item matching: %s", new Object[]{this.viewAction.getDescription(), Integer.valueOf(this.atPosition), this.viewHolderMatcher});
    }

    public void perform(UiController uiController, View root) {
        RecyclerView recyclerView = (RecyclerView) root;

        try {
            this.scroller.perform(uiController, root);
            uiController.loopMainThreadUntilIdle();
            int e = this.atPosition == -1 ? 2 : this.atPosition + 1;
            int selectIndex = this.atPosition == -1 ? 0 : this.atPosition;
            List matchedItems = RecyclerViewActions.itemsMatching(recyclerView, this.viewHolderMatcher, e);
            RecyclerViewActions.actionOnItemAtPosition(((MatchedItem) matchedItems.get(selectIndex)).position, this.viewAction).perform(uiController, root);
            uiController.loopMainThreadUntilIdle();
        } catch (RuntimeException ex) {
            throw (new PerformException.Builder()).withActionDescription(this.getDescription()).withViewDescription(HumanReadables.describe(root)).withCause(ex).build();
        }
    }
}
