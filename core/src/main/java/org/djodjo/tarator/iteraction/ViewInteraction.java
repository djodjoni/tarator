package org.djodjo.tarator.iteraction;

import android.util.Log;
import android.view.View;

import org.assertj.android.api.view.ViewAssert;
import org.djodjo.tarator.FailureHandler;
import org.djodjo.tarator.NoMatchingViewException;
import org.djodjo.tarator.Root;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewFinder;
import org.djodjo.tarator.base.MainThread;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

/**
 * Provides the primary interface for test authors to perform actions or asserts on views.
 * <p>
 * Each interaction is associated with a view identified by a view matcher. All view actions and
 * asserts are performed on the UI thread (thus ensuring sequential execution). The same goes for
 * retrieval of views (this is done to ensure that view state is "fresh" prior to execution of each
 * operation).
 * <p>
 */
public class ViewInteraction extends AbstractViewInteraction<ViewAssert> {

  private static final String TAG = ViewInteraction.class.getSimpleName();


  @Inject
  ViewInteraction(UiController uiController, ViewFinder viewFinder, @MainThread Executor mainThreadExecutor, FailureHandler failureHandler, Matcher<View> viewMatcher, AtomicReference<Matcher<Root>> rootMatcherRef) {
    super(uiController, viewFinder, mainThreadExecutor, failureHandler, viewMatcher, rootMatcherRef);
  }

  public ViewAssert assertThat() {
    final ViewAssert[] tva = {null};
    runSynchronouslyOnUiThread(new Runnable() {
      @Override
      public void run() {
        uiController.loopMainThreadUntilIdle();

        View targetView = null;
        NoMatchingViewException missingViewException = null;
        try {
          targetView = viewFinder.getView();
        } catch (NoMatchingViewException nmve) {
          missingViewException = nmve;
        }
        StringDescription description = new StringDescription();
        description.appendText("'");
        viewMatcher.describeTo(description);
        if (missingViewException != null) {
          description.appendText(String.format(
                  "' check could not be performed because view '%s' was not found.\n", viewMatcher));
          Log.e(TAG, description.toString());
          throw missingViewException;
        }
        tva[0] = new ViewAssert((View)targetView);
      }
    });
    return tva[0];
  }

}
