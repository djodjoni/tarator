package io.apptik.tarator.yogurt.widget;


import android.annotation.TargetApi;
import android.widget.TextView;

import io.apptik.tarator.yogurt.view.AbstractViewAssert;

public abstract class AbstractTextViewAssert<
        AC extends AbstractTextViewAction<AC, AS, I, A>,
        AS extends AbstractTextViewAssert<AC, AS, I, A>,
        I extends AbstractTextViewInteraction<AC, AS, I, A>,
        A extends TextView>
        extends AbstractViewAssert<AC, AS, I, A> {

    org.assertj.android.api.widget.AbstractTextViewAssert<? extends org.assertj.android.api.widget.AbstractTextViewAssert, A> asserter;

    protected AbstractTextViewAssert(A actual, I interaction) {
        super(actual, interaction);
        asserter = (org.assertj.android.api.widget.AbstractTextViewAssert<?, A>) getAsserter();
    }

    @TargetApi(11)
    public AS hasAlpha2(float alpha) {
        asserter.hasAlpha(alpha);
        return interaction.assertThat();
    }
}
