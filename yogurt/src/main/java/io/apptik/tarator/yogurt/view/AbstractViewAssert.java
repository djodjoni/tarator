package io.apptik.tarator.yogurt.view;


import android.annotation.TargetApi;
import android.view.View;

import io.apptik.tarator.yogurt.AbstractAssert;

public abstract class AbstractViewAssert<
        AC extends AbstractViewAction<AC, AS, I, A>,
        AS extends AbstractViewAssert<AC, AS, I, A>,
        I extends AbstractViewInteraction<AC, AS, I, A>,
        A extends View>
        extends AbstractAssert<AC, AS, I, A> {

    org.assertj.android.api.view.AbstractViewAssert<? extends org.assertj.android.api.view.AbstractViewAssert, A> asserter;

    protected AbstractViewAssert(A actual, I interaction) {
        super(actual, interaction);
        asserter = (org.assertj.android.api.view.AbstractViewAssert<?, A>) getAsserter();
    }

    @TargetApi(11)
    public AS hasAlpha(float alpha) {
        asserter.hasAlpha(alpha);
        return interaction.assertThat();
    }
}
