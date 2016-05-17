package org.djodjo.tarator.iteraction;

import android.view.View;
import android.widget.TextView;

import org.assertj.android.api.widget.TextViewAssert;
import org.djodjo.tarator.FailureHandler;
import org.djodjo.tarator.Root;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewFinder;
import org.djodjo.tarator.base.MainThread;
import org.hamcrest.Matcher;

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
public class TextViewInteraction extends AbstractViewInteraction<TextViewInteraction, TextViewAssert> {

  private static final String TAG = TextViewInteraction.class.getSimpleName();


  @Inject
  TextViewInteraction(UiController uiController, ViewFinder viewFinder, @MainThread Executor mainThreadExecutor, FailureHandler failureHandler, Matcher<View> viewMatcher, AtomicReference<Matcher<Root>> rootMatcherRef) {
    super(uiController, viewFinder, mainThreadExecutor, failureHandler, viewMatcher, rootMatcherRef, TextViewInteraction.class);
  }

  @Override
  public TextViewAssert assertThat() {
    return assertThat(TextViewAssert.class, TextView.class);
  }

}
