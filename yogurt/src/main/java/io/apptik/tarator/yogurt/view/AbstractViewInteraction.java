package io.apptik.tarator.yogurt.view;


import android.view.View;

import io.apptik.tarator.yogurt.AbstractInteraction;

public abstract class AbstractViewInteraction <AC extends AbstractViewAction<AC, AS, I, A>,
        AS extends AbstractViewAssert<AC, AS, I, A>,
        I extends AbstractViewInteraction<AC, AS, I, A>,
        A extends View> extends AbstractInteraction<AC, AS, I, A> {


    protected AbstractViewInteraction(A actual, Class<?> selfType) {
        super(actual, selfType);
    }
}
