package org.djodjo.tarator.support.v7.interaction;

import android.view.View;

import org.djodjo.tarator.GraphHolder;
import org.djodjo.tarator.Root;
import org.djodjo.tarator.ViewFinder;
import org.djodjo.tarator.base.RootViewPicker;
import org.djodjo.tarator.base.ViewFinderImpl;
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
                RecyclerViewInteraction.class
        })
public class ViewInteractionModule  {

    private final Matcher<View> viewMatcher;
    private final AtomicReference<Matcher<Root>> rootMatcher =
            new AtomicReference<Matcher<Root>>(RootMatchers.DEFAULT);

    public ViewInteractionModule(Matcher<View> viewMatcher) {
        this.viewMatcher = checkNotNull(viewMatcher);
    }


    @Provides
    AtomicReference<Matcher<Root>> provideRootMatcher() {
        return rootMatcher;
    }

    @Provides
    Matcher<View> provideViewMatcher() {
        return viewMatcher;
    }

    @Provides
    ViewFinder provideViewFinder(ViewFinderImpl impl) {
        return impl;
    }

    @Provides
    public View provideRootView(RootViewPicker rootViewPicker) {
        // RootsOracle acts as a provider, but returning Providers is illegal, so delegate.
        return rootViewPicker.get();
    }
}
