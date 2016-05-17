package org.djodjo.tarator.action;

import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewAction;
import org.hamcrest.Matcher;

import static org.djodjo.tarator.matcher.ViewMatchers.isAssignableFrom;
import static org.djodjo.tarator.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;

/**
 * Clears view text by setting {@link EditText}s text property to "".
 */
public final class SetProgressAction implements ViewAction {

    private final int progressToBeSet;
    private final ProgressType progressType;
    public enum ProgressType {
        PRIMARY,
        SECONDARY;
    }

    public SetProgressAction(int progress, ProgressType progressType) {
        this.progressToBeSet = progress;
        this.progressType = progressType;
    }

  @SuppressWarnings("unchecked")
  @Override
  public Matcher<View> getConstraints() {
    return allOf(isDisplayed(), isAssignableFrom(SeekBar.class));
  }

  @Override
  public void perform(UiController uiController, View view) {
    if(ProgressType.PRIMARY.equals(progressType)) {
        ((SeekBar) view).setProgress(progressToBeSet);
    } else {
        ((SeekBar) view).setSecondaryProgress(progressToBeSet);
    }
  }

  @Override
  public String getDescription() {
    return "Set a progress on a SeekBar";
  }
}
