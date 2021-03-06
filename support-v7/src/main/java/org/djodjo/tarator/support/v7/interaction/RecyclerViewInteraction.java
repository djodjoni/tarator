package org.djodjo.tarator.support.v7.interaction;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.assertj.android.recyclerview.v7.api.widget.RecyclerViewAssert;
import org.djodjo.tarator.FailureHandler;
import org.djodjo.tarator.GraphHolder;
import org.djodjo.tarator.PerformException;
import org.djodjo.tarator.Root;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewFinder;
import org.djodjo.tarator.base.MainThread;
import org.djodjo.tarator.iteraction.AbstractViewInteraction;
import org.djodjo.tarator.support.v7.action.MatchedItem;
import org.djodjo.tarator.support.v7.action.RecyclerViewActions;
import org.djodjo.tarator.support.v7.action.ScrollToViewAction;
import org.djodjo.tarator.util.HumanReadables;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import static org.djodjo.tarator.support.v7.action.RecyclerViewActions.scrollToPosition;

/**
 * Provides the primary interface for test authors to perform actions or asserts on views.
 * <p/>
 * Each interaction is associated with a view identified by a view matcher. All view actions and
 * asserts are performed on the UI thread (thus ensuring sequential execution). The same goes for
 * retrieval of views (this is done to ensure that view state is "fresh" prior to execution of each
 * operation).
 * <p/>
 */
public class RecyclerViewInteraction extends AbstractViewInteraction<RecyclerViewInteraction, RecyclerViewAssert> {

    private static final String TAG = RecyclerViewInteraction.class.getSimpleName();


    @Inject
    RecyclerViewInteraction(UiController uiController, ViewFinder viewFinder, @MainThread Executor mainThreadExecutor, FailureHandler failureHandler, Matcher<View> viewMatcher, AtomicReference<Matcher<Root>> rootMatcherRef) {
        super(uiController, viewFinder, mainThreadExecutor, failureHandler, viewMatcher, rootMatcherRef, RecyclerViewInteraction.class);
    }

    public ViewHolderInteraction onViewHolderAtPosition(final int position) {
        final RecyclerView.ViewHolder[] vh = new RecyclerView.ViewHolder[1];
        final RecyclerView targetView = (RecyclerView) findTargetView();
        runSynchronouslyOnUiThread(new Runnable() {
            @Override
            public void run() {
                vh[0] = findViewHolder(targetView, position);
                targetView.getAdapter().bindViewHolder(vh[0], position);
            }
        });

        return GraphHolder.graph().plus(new ViewHolderInteractionModule(vh[0])).get(ViewHolderInteraction.class);
    }

    public ViewHolderInteraction onViewHolder(final Matcher<? extends RecyclerView.ViewHolder> viewHolderMatcher) {
        final int atPosition = -1;
        final RecyclerView.ViewHolder vh = matchViewHolderSynchronouslyOnUiThread(viewHolderMatcher, atPosition);
        return GraphHolder.graph().plus(new ViewHolderInteractionModule(vh)).get(ViewHolderInteraction.class);
    }

    public ViewHolderInteraction onViewHolder(final Matcher<? extends RecyclerView.ViewHolder> viewHolderMatcher, final int atPosition) {
        final RecyclerView.ViewHolder vh = matchViewHolderSynchronouslyOnUiThread(viewHolderMatcher, atPosition);
        return GraphHolder.graph().plus(new ViewHolderInteractionModule(vh)).get(ViewHolderInteraction.class);
    }

    private RecyclerView.ViewHolder matchViewHolderSynchronouslyOnUiThread(final Matcher<? extends RecyclerView.ViewHolder> viewHolderMatcher, final int atPosition) {
        final RecyclerView.ViewHolder[] vh = new RecyclerView.ViewHolder[1];
        final RecyclerView targetView = (RecyclerView) findTargetView();
        runSynchronouslyOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ScrollToViewAction(viewHolderMatcher, atPosition).perform(uiController, targetView);
                uiController.loopMainThreadUntilIdle();
                int e = atPosition == -1 ? 2 : atPosition + 1;
                int selectIndex = atPosition == -1 ? 0 : atPosition;
                List matchedItems = RecyclerViewActions.itemsMatching(targetView, viewHolderMatcher, e);
                MatchedItem item =  (MatchedItem) matchedItems.get(selectIndex);
                vh[0] = findViewHolder(targetView, item.position);
            }
        });
        return vh[0];
    }

    private RecyclerView.ViewHolder findViewHolder(final RecyclerView targetView, final int position) {
        RecyclerView.ViewHolder vh = null;
        scrollToPosition(position).perform(uiController, targetView);
        uiController.loopMainThreadUntilIdle();
        vh = targetView.findViewHolderForAdapterPosition(position);
        if (vh == null) {
            throw (new PerformException.Builder()).withActionDescription(this.toString())
                    .withViewDescription(HumanReadables.describe(targetView))
                    .withCause(new IllegalStateException("No view holder at position: " + position))
                    .build();
        }

        return vh;
    }


    @Override
    public RecyclerViewAssert assertThat() {
        return assertThat(RecyclerViewAssert.class, RecyclerView.class);
    }
}
