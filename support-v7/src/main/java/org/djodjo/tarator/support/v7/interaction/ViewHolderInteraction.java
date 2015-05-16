package org.djodjo.tarator.support.v7.interaction;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.assertj.android.recyclerview.v7.api.widget.RecyclerViewViewHolderAssert;
import org.djodjo.tarator.FailureHandler;
import org.djodjo.tarator.Root;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewFinder;
import org.djodjo.tarator.base.MainThread;
import org.djodjo.tarator.iteraction.AbstractViewInteraction;
import org.hamcrest.Matcher;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class ViewHolderInteraction
{

    private static final String TAG = AbstractViewInteraction.class.getSimpleName();

    protected final RecyclerView.ViewHolder viewHolder;
    protected final UiController uiController;
    protected final ViewFinder viewSubItemFinder;
    protected final Executor mainThreadExecutor;
    protected volatile FailureHandler failureHandler;
    protected final AtomicReference<Matcher<Root>> rootMatcherRef;

    @Inject
    protected ViewHolderInteraction(UiController uiController,RecyclerView.ViewHolder viewHolder, ViewFinder viewSubItemFinder, @MainThread Executor mainThreadExecutor, FailureHandler failureHandler, AtomicReference<Matcher<Root>> rootMatcherRef) {
        this.viewHolder= checkNotNull(viewHolder);
        this.viewSubItemFinder = checkNotNull(viewSubItemFinder);
        this.uiController = checkNotNull(uiController);
        this.failureHandler = checkNotNull(failureHandler);
        this.mainThreadExecutor = checkNotNull(mainThreadExecutor);
        this.rootMatcherRef = checkNotNull(rootMatcherRef);
    }

    public AbstractViewInteraction onSubView(Matcher<View> viewSubItemMatcher) {
        AbstractViewInteraction vi = null;
        vi = new AbstractViewInteraction(uiController, viewSubItemFinder, mainThreadExecutor, failureHandler, viewSubItemMatcher, rootMatcherRef);
        return vi;
    }

    public RecyclerViewViewHolderAssert assertThat() {
        return new RecyclerViewViewHolderAssert(viewHolder);
    }

}
