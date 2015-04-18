package org.djodjo.tarator.action;

import android.view.View;
import android.widget.EditText;

import com.android.support.test.deps.guava.base.Preconditions;

import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewAction;
import org.djodjo.tarator.matcher.ViewMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public final class ReplaceTextAction implements ViewAction {
    private final String stringToBeSet;

    public ReplaceTextAction(String value) {
        Preconditions.checkNotNull(value);
        this.stringToBeSet = value;
    }

    public Matcher<View> getConstraints() {
        return Matchers.allOf(new Matcher[]{
                ViewMatchers.isDisplayed(),
                ViewMatchers.isAssignableFrom(EditText.class)});
    }

    public void perform(UiController uiController, View view) {
        ((EditText)view).setText(this.stringToBeSet);
    }

    public String getDescription() {
        return "replace text";
    }
}
