package org.djodjo.tarator;

import android.view.View;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import org.djodjo.tarator.util.HumanReadables;
import org.hamcrest.Matcher;

import java.util.List;

/**
 * Indicates that a given matcher did not match any elements in the view hierarchy.
 * <p>
 * Contains details about the matcher and the current view hierarchy to aid in debugging.
 * </p>
 * <p>
 * Since this is usually an unrecoverable error this exception is a runtime exception.
 * </p>
 * <p>
 * References to the view and failing matcher are purposefully not included in the state of this
 * object - since it will most likely be created on the UI thread and thrown on the instrumentation
 * thread, it would be invalid to touch the view on the instrumentation thread. Also the view
 * hierarchy may have changed since exception creation (leading to more confusion).
 * </p>
 */
public final class NoMatchingViewException extends RuntimeException implements TaratorException {

  private Matcher<? super View> viewMatcher;
  private View rootView;
  private List<View> adapterViews;
  private boolean includeViewHierarchy;
  private Optional<String> adapterViewWarning;

  private NoMatchingViewException(String description) {
    super(description);
    this.adapterViews = Lists.newArrayList();
    this.includeViewHierarchy = true;
    this.adapterViewWarning = Optional.absent();
  }

  private NoMatchingViewException(NoMatchingViewException.Builder builder) {
    super(getErrorMessage(builder));
    this.adapterViews = Lists.newArrayList();
    this.includeViewHierarchy = true;
    this.adapterViewWarning = Optional.absent();
    this.viewMatcher = builder.viewMatcher;
    this.rootView = builder.rootView;
    this.adapterViews = builder.adapterViews;
    this.adapterViewWarning = builder.adapterViewWarning;
    this.includeViewHierarchy = builder.includeViewHierarchy;
  }

  public String getViewMatcherDescription() {
    String viewMatcherDescription = "unknown";
    if(null != this.viewMatcher) {
      viewMatcherDescription = this.viewMatcher.toString();
    }

    return viewMatcherDescription;
  }

  private static String getErrorMessage(NoMatchingViewException.Builder builder) {
    String errorMessage = "";
    if(builder.includeViewHierarchy) {
      String message = String.format("No views in hierarchy found matching: %s", new Object[]{builder.viewMatcher});
      if(builder.adapterViewWarning.isPresent()) {
        message = message + (String)builder.adapterViewWarning.get();
      }

      errorMessage = HumanReadables.getViewHierarchyErrorMessage(builder.rootView, (List)null, message, (String)null);
    } else {
      errorMessage = String.format("Could not find a view that matches %s", new Object[]{builder.viewMatcher});
    }

    return errorMessage;
  }

  public static class Builder {
    private Matcher<? super View> viewMatcher;
    private View rootView;
    private List<View> adapterViews = Lists.newArrayList();
    private boolean includeViewHierarchy = true;
    private Optional<String> adapterViewWarning = Optional.absent();

    public Builder() {
    }

    public NoMatchingViewException.Builder from(NoMatchingViewException exception) {
      this.viewMatcher = exception.viewMatcher;
      this.rootView = exception.rootView;
      this.adapterViews = exception.adapterViews;
      this.adapterViewWarning = exception.adapterViewWarning;
      this.includeViewHierarchy = exception.includeViewHierarchy;
      return this;
    }

    public NoMatchingViewException.Builder withViewMatcher(Matcher<? super View> viewMatcher) {
      this.viewMatcher = viewMatcher;
      return this;
    }

    public NoMatchingViewException.Builder withRootView(View rootView) {
      this.rootView = rootView;
      return this;
    }

    public NoMatchingViewException.Builder withAdapterViews(List<View> adapterViews) {
      this.adapterViews = adapterViews;
      return this;
    }

    public NoMatchingViewException.Builder includeViewHierarchy(boolean includeViewHierarchy) {
      this.includeViewHierarchy = includeViewHierarchy;
      return this;
    }

    public NoMatchingViewException.Builder withAdapterViewWarning(Optional<String> adapterViewWarning) {
      this.adapterViewWarning = adapterViewWarning;
      return this;
    }

    public NoMatchingViewException build() {
      Preconditions.checkNotNull(this.viewMatcher);
      Preconditions.checkNotNull(this.rootView);
      Preconditions.checkNotNull(this.adapterViews);
      Preconditions.checkNotNull(this.adapterViewWarning);
      return new NoMatchingViewException(this);
    }
  }
}
