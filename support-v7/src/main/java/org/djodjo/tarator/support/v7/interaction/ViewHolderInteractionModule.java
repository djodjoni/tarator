package org.djodjo.tarator.support.v7.interaction;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.djodjo.tarator.AmbiguousViewMatcherException;
import org.djodjo.tarator.GraphHolder;
import org.djodjo.tarator.NoMatchingViewException;
import org.djodjo.tarator.Root;
import org.djodjo.tarator.ViewFinder;
import org.djodjo.tarator.matcher.RootMatchers;
import org.hamcrest.Matcher;

import java.util.concurrent.atomic.AtomicReference;

import dagger.Module;
import dagger.Provides;

import static org.djodjo.tarator.Checks.checkNotNull;

/**
 * Adds the user interaction v7 scope to the Tarator graph.
 */
@Module(
        addsTo = GraphHolder.TaratorModule.class,
        injects = {
                ViewHolderInteraction.class
        })
public class ViewHolderInteractionModule {

    private final RecyclerView.ViewHolder viewHolder;
    private final AtomicReference<Matcher<Root>> rootMatcher =
            new AtomicReference<Matcher<Root>>(RootMatchers.DEFAULT);

    //TODO do we use ViewHolderFinder instead ??
    public ViewHolderInteractionModule(RecyclerView.ViewHolder viewHolder) {
        this.viewHolder = checkNotNull(viewHolder);
    }

    @Provides
    AtomicReference<Matcher<Root>> provideRootMatcher() {
        return rootMatcher;
    }

    @Provides
    ViewFinder provideViewFinder() {
        return new ViewFinder() {
            @Override
            public View getView() throws AmbiguousViewMatcherException, NoMatchingViewException {
                return viewHolder.itemView;
            }
        };
    }

    @Provides
    public RecyclerView.ViewHolder provideViewHolder() {
        return viewHolder;
    }
}
