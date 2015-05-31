package org.djodjo.tarator.iteraction;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.common.base.Preconditions;

import org.assertj.android.api.view.AbstractViewAssert;
import org.djodjo.tarator.FailureHandler;
import org.djodjo.tarator.NoMatchingViewException;
import org.djodjo.tarator.PerformException;
import org.djodjo.tarator.Root;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewAction;
import org.djodjo.tarator.ViewAssertion;
import org.djodjo.tarator.ViewFinder;
import org.djodjo.tarator.action.ScrollToAction;
import org.djodjo.tarator.base.MainThread;
import org.djodjo.tarator.util.HumanReadables;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.djodjo.tarator.matcher.ViewMatchers.isAssignableFrom;
import static org.djodjo.tarator.matcher.ViewMatchers.isDescendantOfA;

/**
 * Provides the primary interface for test authors to perform actions or asserts on views.
 * <p>
 * Each interaction is associated with a view identified by a view matcher. All view actions and
 * asserts are performed on the UI thread (thus ensuring sequential execution). The same goes for
 * retrieval of views (this is done to ensure that view state is "fresh" prior to execution of each
 * operation).
 * <p>
 */
public abstract class AbstractViewInteraction<S extends AbstractViewInteraction<S, A>, A extends AbstractViewAssert> {

  private static final String TAG = AbstractViewInteraction.class.getSimpleName();

  protected final UiController uiController;
  protected final ViewFinder viewFinder;
  protected final Executor mainThreadExecutor;
  protected volatile FailureHandler failureHandler;
  protected final Matcher<View> viewMatcher;
  protected final AtomicReference<Matcher<Root>> rootMatcherRef;
  protected final S myself;


  protected AbstractViewInteraction(
          UiController uiController,
          ViewFinder viewFinder,
          @MainThread Executor mainThreadExecutor,
          FailureHandler failureHandler,
          Matcher<View> viewMatcher,
          AtomicReference<Matcher<Root>> rootMatcherRef
          , Class<?> selfType) {
    this.viewFinder = checkNotNull(viewFinder);
    this.uiController = checkNotNull(uiController);
    this.failureHandler = checkNotNull(failureHandler);
    this.mainThreadExecutor = checkNotNull(mainThreadExecutor);
    this.viewMatcher = checkNotNull(viewMatcher);
    this.rootMatcherRef = checkNotNull(rootMatcherRef);
    myself = (S) selfType.cast(this);
  }

  /**
   * Performs the given action(s) on the view selected by the current view matcher. If more than one
   * action is provided, actions are executed in the order provided with precondition checks running
   * prior to each action.
   *
   * @param viewActions one or more actions to execute.
   * @return this interaction for further perform/verification calls.
   */
  public S perform(final ViewAction... viewActions) {
    checkNotNull(viewActions);
    for (ViewAction action : viewActions) {
      doPerform(action);
    }
    return myself;
  }


  public S withFailureHandler(FailureHandler failureHandler) {
    this.failureHandler = (FailureHandler) Preconditions.checkNotNull(failureHandler);
    return myself;
  }

  /**
   * Makes this ViewInteraction scoped to the root selected by the given root matcher.
   */
  public S inRoot(Matcher<Root> rootMatcher) {
    this.rootMatcherRef.set(checkNotNull(rootMatcher));
    return myself;
  }

  private void doPerform(final ViewAction viewAction) {
    checkNotNull(viewAction);
    final Matcher<? extends View> constraints = checkNotNull(viewAction.getConstraints());
    runSynchronouslyOnUiThread(new Runnable() {

      @Override
      public void run() {
        uiController.loopMainThreadUntilIdle();
        View targetView = viewFinder.getView();
        Log.i(TAG, String.format(
                "Performing '%s' action on view %s", viewAction.getDescription(), viewMatcher));
        if (!constraints.matches(targetView)) {
          // TODO(user): update this to describeMismatch once hamcrest is updated to new
          StringDescription stringDescription = new StringDescription(new StringBuilder(
                  "Action will not be performed because the target view "
                          + "does not match one or more of the following constraints:\n"));
          constraints.describeTo(stringDescription);
          stringDescription.appendText("\nTarget view: ")
                  .appendValue(HumanReadables.describe(targetView));

          if (viewAction instanceof ScrollToAction
                  && isDescendantOfA(isAssignableFrom((AdapterView.class))).matches(targetView)) {
            stringDescription.appendText(
                    "\nFurther Info: ScrollToAction on a view inside an AdapterView will not work. "
                            + "Use Tarator.onData to load the view.");
          }
          throw new PerformException.Builder()
                  .withActionDescription(viewAction.getDescription())
                  .withViewDescription(viewMatcher.toString())
                  .withCause(new RuntimeException(stringDescription.toString()))
                  .build();
        } else {
          viewAction.perform(uiController, targetView);
        }
      }
    });
  }

  /**
   * Checks the given {@link ViewAssertion} on the the view selected by the current view matcher.
   *
   * @param viewAssert the assertion to perform.
   * @return this interaction for further perform/verification calls.
   */
  public S check(final ViewAssertion viewAssert) {
    checkNotNull(viewAssert);
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
        viewAssert.check(targetView, missingViewException);
      }
    });
    return myself;
  }

  protected void runSynchronouslyOnUiThread(Runnable action) {
    FutureTask<Void> uiTask = new FutureTask<Void>(action, null);
    mainThreadExecutor.execute(uiTask);
    try {
      uiTask.get();
    } catch (InterruptedException ie) {
      throw new RuntimeException("Interrupted  running UI task", ie);
    } catch (ExecutionException ee) {
      failureHandler.handle(ee.getCause(), viewMatcher);
    }
  }

  protected A assertThat(final Class<?> assertClass, final Class<?> actualClass) {
    A va = null;
    final View targetView = getTargetView();
    try {
      va = (A) assertClass.getDeclaredConstructor(actualClass).newInstance(targetView);
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    return  va;
  }

  public abstract A assertThat();

  /**
   * Get matching target view in order to retrieve view properties.
   * This has been deprecated from its very creation and not to be used unless completely out of
   * solutions.
   * Manipulating this view out of a ViewInteraction is absolutely discouraged as there are no
   * guarantees of safe execution.
   * @return matching target view
   */
  @Deprecated
  public View getTargetView() {
    return findTargetView();
  }


  protected View findTargetView() {
    final View[] targetView = {null};
    runSynchronouslyOnUiThread(new Runnable() {
      @Override
      public void run() {
        uiController.loopMainThreadUntilIdle();

        NoMatchingViewException missingViewException = null;
        try {
          targetView[0] = viewFinder.getView();
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

      }
    });
    return targetView[0];
  }
}
