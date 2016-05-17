package io.apptik.tarator.yogurt.view;


import android.view.View;

public class ViewAction extends AbstractViewAction<ViewAction, ViewAssert, ViewInteraction, View> {

    public ViewAction(android.view.View actual, ViewInteraction interaction) {
        super(actual, interaction);
    }
}
