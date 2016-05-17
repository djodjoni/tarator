package io.apptik.tarator.yogurt.view;


import android.view.View;

public final class ViewInteraction extends AbstractViewInteraction<ViewAction, ViewAssert, ViewInteraction, View> {


    public ViewInteraction(View actual) {
        super(actual, ViewInteraction.class);
    }

    @Override
    protected ViewAction getAction(View actual) {
        return new ViewAction(actual, this);
    }

    @Override
    protected ViewAssert getAssertion(View actual) {
        return new ViewAssert(actual, this);
    }
}
