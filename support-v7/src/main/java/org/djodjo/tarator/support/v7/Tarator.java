package org.djodjo.tarator.support.v7;


import android.view.View;

import org.djodjo.tarator.GraphHolder;
import org.djodjo.tarator.support.v7.interaction.RecyclerViewInteraction;
import org.djodjo.tarator.support.v7.interaction.ViewV7InteractionModule;
import org.hamcrest.Matcher;

public class Tarator {

    public static RecyclerViewInteraction onRecyclerView(final Matcher<View> viewMatcher) {
        return GraphHolder.graph().plus(new ViewV7InteractionModule(viewMatcher)).get(RecyclerViewInteraction.class);
    }
}
