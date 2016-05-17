package io.apptik.tarator.yogurt.view;


import android.view.View;

import io.apptik.tarator.yogurt.AbstractAction;

public abstract class AbstractViewAction<AC extends AbstractViewAction<AC, AS, I, A>,
        AS extends AbstractViewAssert<AC, AS, I, A>,
        I extends AbstractViewInteraction<AC, AS, I, A>, A extends View> extends AbstractAction<AC, AS, I, A> {

    protected AbstractViewAction(A actual, I interaction) {
        super(actual, interaction);
    }
}
