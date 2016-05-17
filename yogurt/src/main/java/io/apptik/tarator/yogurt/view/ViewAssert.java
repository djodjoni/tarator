package io.apptik.tarator.yogurt.view;


import android.view.View;

import org.assertj.core.api.Assert;

public class ViewAssert extends AbstractViewAssert<ViewAction, ViewAssert, ViewInteraction, View> {

    public ViewAssert(android.view.View actual, ViewInteraction interaction) {
        super(actual, interaction);
    }

    @Override
    protected Assert getAsserter() {
        return new org.assertj.android.api.view.ViewAssert(actual);
    }
}
