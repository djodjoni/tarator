package org.djodjo.tarator.action;

import android.view.View;
import android.widget.EditText;

import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewAction;
import org.hamcrest.Matcher;

import static org.djodjo.tarator.matcher.ViewMatchers.isAssignableFrom;
import static org.djodjo.tarator.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;

/**
 * Clears view text by setting {@link EditText}s text property to "".
 */
public final class ClearTextAction implements ViewAction {
  @SuppressWarnings("unchecked")
  @Override
  public Matcher<View> getConstraints() {
    return allOf(isDisplayed(), isAssignableFrom(EditText.class));
  }

  @Override
  public void perform(UiController uiController, View view) {
    ((EditText) view).setText("");
  }

  @Override
  public String getDescription() {
    return "clear text";
  }
}
