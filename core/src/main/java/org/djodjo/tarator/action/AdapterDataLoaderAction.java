package org.djodjo.tarator.action;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import org.djodjo.tarator.PerformException;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewAction;
import org.djodjo.tarator.util.HumanReadables;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static org.djodjo.tarator.matcher.ViewMatchers.isAssignableFrom;
import static org.djodjo.tarator.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;

/**
 * Forces an AdapterView to ensure that the data matching a provided data matcher
 * is loaded into the current view hierarchy.
 *
 */
public final class AdapterDataLoaderAction implements ViewAction {
  private final Matcher<Object> dataToLoadMatcher;
  private final AdapterViewProtocol adapterViewProtocol;
  private final Optional<Integer> atPosition;
  private AdapterViewProtocol.AdaptedData adaptedData;
  private boolean performed = false;
  private Object dataLock = new Object();

  public AdapterDataLoaderAction(Matcher<Object> dataToLoadMatcher, Optional<Integer> atPosition,
      AdapterViewProtocol adapterViewProtocol) {
    this.dataToLoadMatcher = checkNotNull(dataToLoadMatcher);
    this.atPosition = checkNotNull(atPosition);
    this.adapterViewProtocol = checkNotNull(adapterViewProtocol);
  }

  public AdapterViewProtocol.AdaptedData getAdaptedData() {
    synchronized (dataLock) {
      checkState(performed, "perform hasn't been called yet!");
      return adaptedData;
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Matcher<View> getConstraints() {
    return allOf(isAssignableFrom(AdapterView.class), isDisplayed());
  }

  @SuppressWarnings("unchecked")
  @Override
  public void perform(UiController uiController, View view) {
    AdapterView<? extends Adapter> adapterView = (AdapterView<? extends Adapter>) view;
    List<AdapterViewProtocol.AdaptedData> matchedDataItems = Lists.newArrayList();

    for (AdapterViewProtocol.AdaptedData data : adapterViewProtocol.getDataInAdapterView(
        adapterView)) {

      if (dataToLoadMatcher.matches(data.data)) {
        matchedDataItems.add(data);
      }
    }

    if (matchedDataItems.size() == 0) {
      StringDescription dataMatcherDescription = new StringDescription();
      dataToLoadMatcher.describeTo(dataMatcherDescription);

      if (matchedDataItems.isEmpty()) {
        dataMatcherDescription.appendText(" contained values: ");
          dataMatcherDescription.appendValue(
              adapterViewProtocol.getDataInAdapterView(adapterView));
        throw new PerformException.Builder()
          .withActionDescription(this.getDescription())
          .withViewDescription(HumanReadables.describe(view))
          .withCause(new RuntimeException("No data found matching: " + dataMatcherDescription))
          .build();
      }
    }

    synchronized (dataLock) {
      checkState(!performed, "perform called 2x!");
      performed = true;
      if (atPosition.isPresent()) {
        int matchedDataItemsSize = matchedDataItems.size() - 1;
        if (atPosition.get() > matchedDataItemsSize) {
          throw new PerformException.Builder()
            .withActionDescription(this.getDescription())
            .withViewDescription(HumanReadables.describe(view))
            .withCause(new RuntimeException(String.format(
                "There are only %d elements that matched but requested %d element.",
                matchedDataItemsSize, atPosition.get())))
            .build();
        } else {
          adaptedData = matchedDataItems.get(atPosition.get());
        }
      } else {
        if (matchedDataItems.size() != 1) {
          StringDescription dataMatcherDescription = new StringDescription();
          dataToLoadMatcher.describeTo(dataMatcherDescription);
          throw new PerformException.Builder()
            .withActionDescription(this.getDescription())
            .withViewDescription(HumanReadables.describe(view))
            .withCause(new RuntimeException("Multiple data elements " +
                "matched: " + dataMatcherDescription + ". Elements: " + matchedDataItems))
            .build();
        } else {
          adaptedData = matchedDataItems.get(0);
        }
      }
    }

    int requestCount = 0;
    while (!adapterViewProtocol.isDataRenderedWithinAdapterView(adapterView, adaptedData)) {
      if (requestCount > 1) {
        if ((requestCount % 50) == 0) {
          // sometimes an adapter view will receive an event that will block its attempts to scroll.
          adapterView.invalidate();
          adapterViewProtocol.makeDataRenderedWithinAdapterView(adapterView, adaptedData);
        }
      } else {
        adapterViewProtocol.makeDataRenderedWithinAdapterView(adapterView, adaptedData);
      }
      uiController.loopMainThreadForAtLeast(100);
      requestCount++;
    }
  }

  @Override
  public String getDescription() {
    return "load adapter data";
  }
}
