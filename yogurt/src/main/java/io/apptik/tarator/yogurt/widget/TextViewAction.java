package io.apptik.tarator.yogurt.widget;


import android.widget.TextView;

import io.apptik.tarator.yogurt.view.AbstractViewAction;

public class TextViewAction extends AbstractTextViewAction<TextViewAction, TextViewAssert, TextViewInteraction, TextView> {

    public TextViewAction(TextView actual, TextViewInteraction interaction) {
        super(actual, interaction);
    }
}
