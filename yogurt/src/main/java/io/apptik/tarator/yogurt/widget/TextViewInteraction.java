package io.apptik.tarator.yogurt.widget;


import android.widget.TextView;

public final class TextViewInteraction extends AbstractTextViewInteraction<TextViewAction, TextViewAssert, TextViewInteraction, TextView> {


    public TextViewInteraction(TextView actual) {
        super(actual, TextViewInteraction.class);
    }

    @Override
    protected TextViewAction getAction(TextView actual) {
        return new TextViewAction(actual, this);
    }

    @Override
    protected TextViewAssert getAssertion(TextView actual) {
        return new TextViewAssert(actual, this);
    }
}
