package io.apptik.tarator.yogurt.widget;


import android.widget.TextView;

import io.apptik.tarator.yogurt.view.AbstractViewInteraction;

public abstract class AbstractTextViewInteraction<AC extends AbstractTextViewAction<AC, AS, I, A>,
        AS extends AbstractTextViewAssert<AC, AS, I, A>,
        I extends AbstractTextViewInteraction<AC, AS, I, A>,
        A extends TextView>
        extends AbstractViewInteraction<AC, AS, I, A> {


    protected AbstractTextViewInteraction(A actual, Class<?> selfType) {
        super(actual, selfType);
    }
}
