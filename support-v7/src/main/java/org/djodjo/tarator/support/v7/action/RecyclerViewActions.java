package org.djodjo.tarator.support.v7.action;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.SparseArray;
import android.view.View;

import org.djodjo.tarator.Checks;
import org.djodjo.tarator.PerformException;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewAction;
import org.djodjo.tarator.matcher.ViewMatchers;
import org.djodjo.tarator.util.HumanReadables;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class RecyclerViewActions {
    private static final int NO_POSITION = -1;

    private RecyclerViewActions() {
    }

    public static <VH extends ViewHolder> RecyclerViewActions.PositionableRecyclerViewAction scrollToHolder(Matcher<VH> viewHolderMatcher) {
        return new RecyclerViewActions.ScrollToViewAction(viewHolderMatcher);
    }

    public static <VH extends ViewHolder> RecyclerViewActions.PositionableRecyclerViewAction scrollTo(Matcher<View> itemViewMatcher) {
        Matcher viewHolderMatcher = viewHolderMatcher(itemViewMatcher);
        return new RecyclerViewActions.ScrollToViewAction(viewHolderMatcher);
    }

    public static <VH extends ViewHolder> ViewAction scrollToPosition(int position) {
        return new RecyclerViewActions.ScrollToPositionViewAction(position);
    }

    public static <VH extends ViewHolder> RecyclerViewActions.PositionableRecyclerViewAction actionOnItem(Matcher<View> itemViewMatcher, ViewAction viewAction) {
        Matcher viewHolderMatcher = viewHolderMatcher(itemViewMatcher);
        return new RecyclerViewActions.ActionOnItemViewAction(viewHolderMatcher, viewAction);
    }

    public static <VH extends ViewHolder> RecyclerViewActions.PositionableRecyclerViewAction actionOnHolderItem(Matcher<VH> viewHolderMatcher, ViewAction viewAction) {
        return new RecyclerViewActions.ActionOnItemViewAction(viewHolderMatcher, viewAction);
    }

    public static <VH extends ViewHolder> ViewAction actionOnItemAtPosition(int position, ViewAction viewAction) {
        return new RecyclerViewActions.ActionOnItemAtPositionViewAction(position, viewAction);
    }

    private static <T extends VH, VH extends ViewHolder> List<MatchedItem> itemsMatching(RecyclerView recyclerView, Matcher<VH> viewHolderMatcher, int max) {
        Adapter adapter = recyclerView.getAdapter();
        SparseArray viewHolderCache = new SparseArray();
        ArrayList matchedItems = new ArrayList();

        for(int position = 0; position < adapter.getItemCount(); ++position) {
            int itemType = adapter.getItemViewType(position);
            ViewHolder cachedViewHolder = (ViewHolder)viewHolderCache.get(itemType);
            if(null == cachedViewHolder) {
                cachedViewHolder = adapter.createViewHolder(recyclerView, itemType);
                viewHolderCache.put(itemType, cachedViewHolder);
            }

            adapter.bindViewHolder(cachedViewHolder, position);
            if(viewHolderMatcher.matches(cachedViewHolder)) {
                matchedItems.add(new RecyclerViewActions.MatchedItem(position, HumanReadables.getViewHierarchyErrorMessage(cachedViewHolder.itemView, (List) null, "\n\n*** Matched ViewHolder item at position: " + position + " ***", (String) null)));
                if(matchedItems.size() == max) {
                    break;
                }
            }
        }

        return matchedItems;
    }

    private static <VH extends ViewHolder> Matcher<VH> viewHolderMatcher(final Matcher<View> itemViewMatcher) {
        return new TypeSafeMatcher<VH>() {
            public boolean matchesSafely(ViewHolder viewHolder) {
                return itemViewMatcher.matches(viewHolder.itemView);
            }

            public void describeTo(Description description) {
                description.appendText("holder with view: ");
                itemViewMatcher.describeTo(description);
            }
        };
    }

    private static class MatchedItem {
        public final int position;
        public final String description;

        private MatchedItem(int position, String description) {
            this.position = position;
            this.description = description;
        }

        public String toString() {
            return this.description;
        }
    }

    private static final class ScrollToPositionViewAction implements ViewAction {
        private final int position;

        private ScrollToPositionViewAction(int position) {
            this.position = position;
        }

        public Matcher<View> getConstraints() {
            return Matchers.allOf(new Matcher[]{ViewMatchers.isAssignableFrom(RecyclerView.class), ViewMatchers.isDisplayed()});
        }

        public String getDescription() {
            return "scroll RecyclerView to position: " + this.position;
        }

        public void perform(UiController uiController, View view) {
            RecyclerView recyclerView = (RecyclerView)view;
            recyclerView.scrollToPosition(this.position);
        }
    }

    private static final class ScrollToViewAction<VH extends ViewHolder> implements RecyclerViewActions.PositionableRecyclerViewAction {
        private final Matcher<VH> viewHolderMatcher;
        private final int atPosition;

        private ScrollToViewAction(Matcher<VH> viewHolderMatcher) {
            this(viewHolderMatcher, -1);
        }

        private ScrollToViewAction(Matcher<VH> viewHolderMatcher, int atPosition) {
            this.viewHolderMatcher = viewHolderMatcher;
            this.atPosition = atPosition;
        }

        public RecyclerViewActions.PositionableRecyclerViewAction atPosition(int position) {
            Checks.checkArgument(position >= 0, "%d is used as an index - must be >= 0", new Object[]{Integer.valueOf(position)});
            return new RecyclerViewActions.ScrollToViewAction(this.viewHolderMatcher, position);
        }

        public Matcher<View> getConstraints() {
            return Matchers.allOf(new Matcher[]{ViewMatchers.isAssignableFrom(RecyclerView.class), ViewMatchers.isDisplayed()});
        }

        public String getDescription() {
            return this.atPosition == -1?"scroll RecyclerView to: " + this.viewHolderMatcher:String.format("scroll RecyclerView to the: %dth matching %s.", new Object[]{Integer.valueOf(this.atPosition), this.viewHolderMatcher});
        }

        public void perform(UiController uiController, View view) {
            RecyclerView recyclerView = (RecyclerView)view;

            try {
                int e = this.atPosition == -1?2:this.atPosition + 1;
                int selectIndex = this.atPosition == -1?0:this.atPosition;
                List matchedItems = RecyclerViewActions.itemsMatching(recyclerView, this.viewHolderMatcher, e);
                if(selectIndex >= matchedItems.size()) {
                    throw new RuntimeException(String.format("Found %d items matching %s, but position %d was requested.", new Object[]{Integer.valueOf(matchedItems.size()), this.viewHolderMatcher.toString(), Integer.valueOf(this.atPosition)}));
                } else if(this.atPosition == -1 && matchedItems.size() == 2) {
                    StringBuilder ambiguousViewError = new StringBuilder();
                    ambiguousViewError.append(String.format("Found more than one sub-view matching %s", new Object[]{this.viewHolderMatcher}));
                    Iterator i$ = matchedItems.iterator();

                    while(i$.hasNext()) {
                        RecyclerViewActions.MatchedItem item = (RecyclerViewActions.MatchedItem)i$.next();
                        ambiguousViewError.append(item + "\n");
                    }

                    throw new RuntimeException(ambiguousViewError.toString());
                } else {
                    recyclerView.scrollToPosition(((RecyclerViewActions.MatchedItem)matchedItems.get(selectIndex)).position);
                    uiController.loopMainThreadUntilIdle();
                }
            } catch (RuntimeException var10) {
                throw (new PerformException.Builder()).withActionDescription(this.getDescription()).withViewDescription(HumanReadables.describe(view)).withCause(var10).build();
            }
        }
    }

    private static final class ActionOnItemAtPositionViewAction<VH extends ViewHolder> implements ViewAction {
        private final int position;
        private final ViewAction viewAction;

        private ActionOnItemAtPositionViewAction(int position, ViewAction viewAction) {
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
            RecyclerView recyclerView = (RecyclerView)view;
            (new RecyclerViewActions.ScrollToPositionViewAction(this.position)).perform(uiController, view);
            uiController.loopMainThreadUntilIdle();
            ViewHolder viewHolderForPosition = recyclerView.findViewHolderForPosition(this.position);
            if(null == viewHolderForPosition) {
                throw (new PerformException.Builder()).withActionDescription(this.toString()).withViewDescription(HumanReadables.describe(view)).withCause(new IllegalStateException("No view holder at position: " + this.position)).build();
            } else {
                View viewAtPosition = viewHolderForPosition.itemView;
                if(null == viewAtPosition) {
                    throw (new PerformException.Builder()).withActionDescription(this.toString()).withViewDescription(HumanReadables.describe(viewAtPosition)).withCause(new IllegalStateException("No view at position: " + this.position)).build();
                } else {
                    this.viewAction.perform(uiController, viewAtPosition);
                }
            }
        }
    }

    private static final class ActionOnItemViewAction<VH extends ViewHolder> implements RecyclerViewActions.PositionableRecyclerViewAction {
        private final Matcher<VH> viewHolderMatcher;
        private final ViewAction viewAction;
        private final int atPosition;
        private final RecyclerViewActions.ScrollToViewAction<VH> scroller;

        private ActionOnItemViewAction(Matcher<VH> viewHolderMatcher, ViewAction viewAction) {
            this(viewHolderMatcher, viewAction, -1);
        }

        private ActionOnItemViewAction(Matcher<VH> viewHolderMatcher, ViewAction viewAction, int atPosition) {
            this.viewHolderMatcher = (Matcher)Checks.checkNotNull(viewHolderMatcher);
            this.viewAction = (ViewAction)Checks.checkNotNull(viewAction);
            this.atPosition = atPosition;
            this.scroller = new RecyclerViewActions.ScrollToViewAction(viewHolderMatcher, atPosition);
        }

        public Matcher<View> getConstraints() {
            return Matchers.allOf(new Matcher[]{ViewMatchers.isAssignableFrom(RecyclerView.class), ViewMatchers.isDisplayed()});
        }

        public RecyclerViewActions.PositionableRecyclerViewAction atPosition(int position) {
            Checks.checkArgument(position >= 0, "%d is used as an index - must be >= 0", new Object[]{Integer.valueOf(position)});
            return new RecyclerViewActions.ActionOnItemViewAction(this.viewHolderMatcher, this.viewAction, position);
        }

        public String getDescription() {
            return this.atPosition == -1?String.format("performing ViewAction: %s on item matching: %s", new Object[]{this.viewAction.getDescription(), this.viewHolderMatcher}):String.format("performing ViewAction: %s on %d-th item matching: %s", new Object[]{this.viewAction.getDescription(), Integer.valueOf(this.atPosition), this.viewHolderMatcher});
        }

        public void perform(UiController uiController, View root) {
            RecyclerView recyclerView = (RecyclerView)root;

            try {
                this.scroller.perform(uiController, root);
                uiController.loopMainThreadUntilIdle();
                int e = this.atPosition == -1?2:this.atPosition + 1;
                int selectIndex = this.atPosition == -1?0:this.atPosition;
                List matchedItems = RecyclerViewActions.itemsMatching(recyclerView, this.viewHolderMatcher, e);
                RecyclerViewActions.actionOnItemAtPosition(((RecyclerViewActions.MatchedItem)matchedItems.get(selectIndex)).position, this.viewAction).perform(uiController, root);
                uiController.loopMainThreadUntilIdle();
            } catch (RuntimeException var7) {
                throw (new PerformException.Builder()).withActionDescription(this.getDescription()).withViewDescription(HumanReadables.describe(root)).withCause(var7).build();
            }
        }
    }

    public interface PositionableRecyclerViewAction extends ViewAction {
        RecyclerViewActions.PositionableRecyclerViewAction atPosition(int var1);
    }
}
