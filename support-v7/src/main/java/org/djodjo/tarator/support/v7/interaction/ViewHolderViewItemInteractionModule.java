package org.djodjo.tarator.support.v7.interaction;

import android.view.View;

import org.djodjo.tarator.GraphHolder;
import org.djodjo.tarator.Root;
import org.djodjo.tarator.ViewFinder;
import org.djodjo.tarator.base.RootViewPicker;
import org.djodjo.tarator.base.ViewFinderImpl;
import org.djodjo.tarator.iteraction.ButtonInteraction;
import org.djodjo.tarator.iteraction.CompoundButtonInteraction;
import org.djodjo.tarator.iteraction.TextViewInteraction;
import org.djodjo.tarator.iteraction.ViewInteraction;
import org.djodjo.tarator.matcher.RootMatchers;
import org.hamcrest.Matcher;

import java.util.concurrent.atomic.AtomicReference;

import dagger.Module;
import dagger.Provides;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Adds the user interaction scope to the Tarator graph.
 */
@Module(
        addsTo = GraphHolder.TaratorModule.class,
        injects = {
                ViewInteraction.class,
                TextViewInteraction.class,
                ButtonInteraction.class,
                CompoundButtonInteraction.class
        })
public class ViewHolderViewItemInteractionModule {

    private final Matcher<View> viewMatcher;
    private final View rootView;
    private final AtomicReference<Matcher<Root>> rootMatcher =
            new AtomicReference<Matcher<Root>>(RootMatchers.DEFAULT);

    public ViewHolderViewItemInteractionModule(Matcher<View> viewMatcher, View rootView) {
        this.viewMatcher = checkNotNull(viewMatcher);
        this.rootView = rootView;
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
        return rootView;
    }
}
