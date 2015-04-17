package org.djodjo.tarator.action;

import android.os.SystemClock;
import android.support.test.internal.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import org.djodjo.tarator.InjectEventSecurityException;
import org.djodjo.tarator.NoActivityResumedException;
import org.djodjo.tarator.PerformException;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewAction;
import org.djodjo.tarator.util.HumanReadables;
import org.hamcrest.Matcher;

import static android.support.test.internal.util.Checks.checkNotNull;
import static org.djodjo.tarator.matcher.ViewMatchers.isDisplayed;


/**
 * Enables pressing KeyEvents on views.
 */
public final class KeyEventAction implements ViewAction {
  private static final String TAG = KeyEventAction.class.getSimpleName();

  private final TaratorKey key;

  public KeyEventAction(TaratorKey key) {
    this.key = checkNotNull(key);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Matcher<View> getConstraints() {
    return isDisplayed();
  }

  @Override
  public void perform(UiController uiController, View view) {
    try {
      if (!sendKeyEvent(uiController, view)) {
        Log.e(TAG, "Failed to inject key event: " + this.key);
        throw new PerformException.Builder()
          .withActionDescription(this.getDescription())
          .withViewDescription(HumanReadables.describe(view))
          .withCause(new RuntimeException("Failed to inject key event " + this.key))
          .build();
      }
    } catch (InjectEventSecurityException e) {
      Log.e(TAG, "Failed to inject key event: " + this.key);
      throw new PerformException.Builder()
        .withActionDescription(this.getDescription())
        .withViewDescription(HumanReadables.describe(view))
        .withCause(e)
        .build();
    }
  }

  private final boolean sendKeyEvent(UiController controller, View view)
      throws InjectEventSecurityException {

    boolean injected = false;
    long eventTime = SystemClock.uptimeMillis();
    for (int attempts = 0; !injected && attempts < 4; attempts++) {
      injected = controller.injectKeyEvent(new KeyEvent(eventTime,
          eventTime,
          KeyEvent.ACTION_DOWN,
          this.key.getKeyCode(),
          0,
          this.key.getMetaState()));
    }

    if (!injected) {
      // it is not a transient failure... :(
      return false;
    }

    injected = false;
    eventTime = SystemClock.uptimeMillis();
    for (int attempts = 0; !injected && attempts < 4; attempts++) {
      injected = controller.injectKeyEvent(
          new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_UP, this.key.getKeyCode(), 0));
    }


    if (this.key.getKeyCode() == KeyEvent.KEYCODE_BACK) {
      controller.loopMainThreadUntilIdle();
      boolean activeActivities = !ActivityLifecycleMonitorRegistry.getInstance()
          .getActivitiesInStage(Stage.RESUMED)
          .isEmpty();
      if (!activeActivities) {
        Throwable cause = new PerformException.Builder()
          .withActionDescription(this.getDescription())
          .withViewDescription(HumanReadables.describe(view))
          .build();
        throw new NoActivityResumedException("Pressed back and killed the app", cause);
      }
    }

    return injected;
  }

  @Override
  public String getDescription() {
    return String.format("send %s key event", this.key);
  }
}
