package io.apptik.tarator.yogurt.widget;


import android.widget.TextView;

import io.apptik.tarator.yogurt.view.AbstractViewAction;

public abstract class AbstractTextViewAction<
        AC extends AbstractTextViewAction<AC, AS, I, A>,
        AS extends AbstractTextViewAssert<AC, AS, I, A>,
        I extends AbstractTextViewInteraction<AC, AS, I, A>,
        A extends TextView> 
        extends AbstractViewAction<AC, AS, I, A> {

    protected AbstractTextViewAction(A actual, I interaction) {
        super(actual, interaction);
    }
}
