package org.djodjo.tarator.support.v7.interaction;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.assertj.android.recyclerview.v7.api.widget.RecyclerViewViewHolderAssert;
import org.djodjo.tarator.FailureHandler;
import org.djodjo.tarator.GraphHolder;
import org.djodjo.tarator.Root;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewFinder;
import org.djodjo.tarator.base.MainThread;
import org.djodjo.tarator.iteraction.*;
import org.djodjo.tarator.iteraction.ViewInteractionModule;
import org.hamcrest.Description;
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

    public ViewInteraction onSubView(Matcher<View> viewSubItemMatcher) {
        return GraphHolder.graph().plus(new ViewHolderViewItemInteractionModule(viewSubItemMatcher, viewHolder.itemView)).get(ViewInteraction.class);
    }

    public TextViewInteraction onSubTextView(Matcher<View> viewSubItemMatcher) {
        return GraphHolder.graph().plus(new ViewHolderViewItemInteractionModule(viewSubItemMatcher, viewHolder.itemView)).get(TextViewInteraction.class);
    }

    public ButtonInteraction onSubButton(Matcher<View> viewSubItemMatcher) {
        return GraphHolder.graph().plus(new ViewHolderViewItemInteractionModule(viewSubItemMatcher, viewHolder.itemView)).get(ButtonInteraction.class);
    }

    public CompoundButtonInteraction onSubCompoundButton(Matcher<View> viewSubItemMatcher) {
        return GraphHolder.graph().plus(new ViewHolderViewItemInteractionModule(viewSubItemMatcher, viewHolder.itemView)).get(CompoundButtonInteraction.class);
    }

    public ViewInteraction onViewIem() {
        ViewInteraction vi = null;
        vi = GraphHolder.graph().plus(new ViewInteractionModule(
         new Matcher<View>() {
            @Override
            public boolean matches(Object item) {
                return true;
            }

            @Override
            public void describeMismatch(Object item, Description mismatchDescription) {

            }

            @Override
            public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

            }

            @Override
            public void describeTo(Description description) {

            }
        })).get(ViewInteraction.class);
        return vi;
    }

    public RecyclerViewViewHolderAssert assertThat() {
        return new RecyclerViewViewHolderAssert(viewHolder);
    }

    /**
     * Get matching target ViewHolder in order to retrieve its properties.
     * This has been deprecated from its very creation and not to be used unless completely out of
     * solutions.
     * Manipulating this ViewHolder out of a ViewHolderInteraction or RecyclerViewInteraction is absolutely discouraged as there are no
     * guarantees of safe execution.
     * @return matching target view
     */
    @Deprecated
    public RecyclerView.ViewHolder getViewHolder() {
        return this.viewHolder;
    }

}
