package org.djodjo.tarator.testapp.test;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;

import org.djodjo.tarator.FailureHandler;
import org.djodjo.tarator.NoMatchingViewException;
import org.djodjo.tarator.base.DefaultFailureHandler;
import org.djodjo.tarator.testapp.MainActivity;
import org.hamcrest.Matcher;

import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.Tarator.setFailureHandler;
import static org.djodjo.tarator.action.ViewActions.click;
import static org.djodjo.tarator.matcher.ViewMatchers.withText;

/**
 * A sample of how to set a non-default {@link FailureHandler}.
 */
@LargeTest
public class CustomFailureHandlerTest extends ActivityInstrumentationTestCase2<MainActivity> {

  private static final String TAG = "CustomFailureHandlerTest";

  public CustomFailureHandlerTest() {
    super( MainActivity.class);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    getActivity();
    setFailureHandler(new CustomFailureHandler(getInstrumentation().getTargetContext()));
  }

  public void testWithCustomFailureHandler() {
    try {
      onView(withText("does not exist")).perform(click());
    } catch (MySpecialException expected) {
      Log.e(TAG, "Special exception is special and expected: ", expected);
    }
  }

  /**
   * A {@link FailureHandler} that re-throws {@link NoMatchingViewException} as
   * {@link MySpecialException}. All other functionality is delegated to
   * {@link DefaultFailureHandler}.
   */
  private static class CustomFailureHandler implements FailureHandler {
    private final FailureHandler delegate;

    public CustomFailureHandler(Context targetContext) {
      delegate = new DefaultFailureHandler(targetContext);
    }

    @Override
    public void handle(Throwable error, Matcher<View> viewMatcher) {
      try {
        delegate.handle(error, viewMatcher);
      } catch (NoMatchingViewException e) {
        throw new MySpecialException(e);
      }
    }
  }

  private static class MySpecialException extends RuntimeException {
    MySpecialException(Throwable cause) {
      super(cause);
    }
  }
}
