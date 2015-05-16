package org.djodjo.tarator.support.v7.action;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.SparseArray;
import android.view.View;

import org.djodjo.tarator.ViewAction;
import org.djodjo.tarator.util.HumanReadables;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.List;

import static org.djodjo.tarator.support.v7.matcher.ViewHolderMatchers.itemViewMatcher;


public class RecyclerViewActions {
    private static final int NO_POSITION = -1;

    private RecyclerViewActions() {
    }

    public static <VH extends ViewHolder> PositionableRecyclerViewAction scrollToHolder(Matcher<VH> viewHolderMatcher) {
        return new ScrollToViewAction(viewHolderMatcher);
    }

    public static <VH extends ViewHolder> PositionableRecyclerViewAction scrollTo(Matcher<View> viewMatcher) {
        Matcher viewHolderMatcher = itemViewMatcher(viewMatcher);
        return new ScrollToViewAction(viewHolderMatcher);
    }

    public static <VH extends ViewHolder> ViewAction scrollToPosition(int position) {
        return new ScrollToPositionViewAction(position, false);
    }

    public static <VH extends ViewHolder> ViewAction smoothScrollToPosition(int position) {
        return new ScrollToPositionViewAction(position, true);
    }

    public static <VH extends ViewHolder> PositionableRecyclerViewAction actionOnItem(Matcher<View> viewMatcher, ViewAction viewAction) {
        Matcher viewHolderMatcher = itemViewMatcher(viewMatcher);
        return new ActionOnItemViewAction(viewHolderMatcher, viewAction);
    }

    public static <VH extends ViewHolder> PositionableRecyclerViewAction actionOnHolderItem(Matcher<VH> viewHolderMatcher, ViewAction viewAction) {
        return new ActionOnItemViewAction(viewHolderMatcher, viewAction);
    }

    public static <VH extends ViewHolder> ViewAction actionOnItemAtPosition(int position, ViewAction viewAction) {
        return new ActionOnItemAtPositionViewAction(position, viewAction);
    }

    public static <T extends VH, VH extends ViewHolder> List<MatchedItem> itemsMatching(RecyclerView recyclerView, Matcher<VH> viewHolderMatcher, int max) {
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
                matchedItems.add(new MatchedItem(position, HumanReadables.getViewHierarchyErrorMessage(cachedViewHolder.itemView, (List) null, "\n\n*** Matched ViewHolder item at position: " + position + " ***", (String) null)));
                if(matchedItems.size() == max) {
                    break;
                }
            }
        }

        return matchedItems;
    }


}
