package org.djodjo.tarator.support.v7.action;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.djodjo.tarator.Checks;
import org.djodjo.tarator.PerformException;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.matcher.ViewMatchers;
import org.djodjo.tarator.util.HumanReadables;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.Iterator;
import java.util.List;


public final class ScrollToViewAction<VH extends RecyclerView.ViewHolder> implements PositionableRecyclerViewAction {
    private final Matcher<VH> viewHolderMatcher;
    private final int atPosition;

    ScrollToViewAction(Matcher<VH> viewHolderMatcher) {
        this(viewHolderMatcher, -1);
    }

    public ScrollToViewAction(Matcher<VH> viewHolderMatcher, int atPosition) {
        this.viewHolderMatcher = viewHolderMatcher;
        this.atPosition = atPosition;
    }

    public PositionableRecyclerViewAction atPosition(int position) {
        Checks.checkArgument(position >= 0, "%d is used as an index - must be >= 0", new Object[]{Integer.valueOf(position)});
        return new ScrollToViewAction(this.viewHolderMatcher, position);
    }

    public Matcher<View> getConstraints() {
        return Matchers.allOf(new Matcher[]{ViewMatchers.isAssignableFrom(RecyclerView.class), ViewMatchers.isDisplayed()});
    }

    public String getDescription() {
        return this.atPosition == -1 ? "scroll RecyclerView to: " + this.viewHolderMatcher : String.format("scroll RecyclerView to the: %dth matching %s.", new Object[]{Integer.valueOf(this.atPosition), this.viewHolderMatcher});
    }

    public void perform(UiController uiController, View view) {
        RecyclerView recyclerView = (RecyclerView) view;

        try {
            int e = this.atPosition == -1 ? 2 : this.atPosition + 1;
            int selectIndex = this.atPosition == -1 ? 0 : this.atPosition;
            Log.w("TT", "tt - 11");
            List matchedItems = RecyclerViewActions.itemsMatching(recyclerView, this.viewHolderMatcher, e);
            Log.w("TT", "tt - 12");
            if (selectIndex >= matchedItems.size()) {
                Log.w("TT", "tt - 1F1");
                throw new RuntimeException(String.format("Found %d items matching %s, but position %d was requested.", new Object[]{Integer.valueOf(matchedItems.size()), this.viewHolderMatcher.toString(), Integer.valueOf(this.atPosition)}));
            } else if (this.atPosition == -1 && matchedItems.size() == 2) {
                Log.w("TT", "tt - 1F2");
                StringBuilder ambiguousViewError = new StringBuilder();
                ambiguousViewError.append(String.format("Found more than one sub-view matching %s", new Object[]{this.viewHolderMatcher}));
                Iterator iterator = matchedItems.iterator();

                while (iterator.hasNext()) {
                    MatchedItem item = (MatchedItem) iterator.next();
                    ambiguousViewError.append(item + "\n");
                }

                throw new RuntimeException(ambiguousViewError.toString());
            } else {
                Log.w("TT", "tt - 1 GOOD");
                recyclerView.scrollToPosition(((MatchedItem) matchedItems.get(selectIndex)).position);
                uiController.loopMainThreadUntilIdle();
            }
        } catch (RuntimeException var10) {
            throw (new PerformException.Builder()).withActionDescription(this.getDescription()).withViewDescription(HumanReadables.describe(view)).withCause(var10).build();
        }
    }
}
