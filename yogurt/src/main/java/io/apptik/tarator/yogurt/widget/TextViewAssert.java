package io.apptik.tarator.yogurt.widget;


import android.widget.TextView;

import org.assertj.core.api.Assert;

import io.apptik.tarator.yogurt.view.AbstractViewAssert;

public class TextViewAssert extends AbstractTextViewAssert<TextViewAction, TextViewAssert, TextViewInteraction, TextView> {

    public TextViewAssert(TextView actual, TextViewInteraction interaction) {
        super(actual, interaction);
    }

    @Override
    protected Assert getAsserter() {
        return new org.assertj.android.api.widget.TextViewAssert(actual);
    }

}
