package org.djodjo.tarator.testapp.test;

import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.Tarator.registerIdlingResources;
import static org.djodjo.tarator.action.ViewActions.click;
import static org.djodjo.tarator.assertion.ViewAssertions.matches;
import static org.djodjo.tarator.matcher.ViewMatchers.withId;
import static org.djodjo.tarator.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkNotNull;

import org.djodjo.tarator.base.CountingIdlingResource;
import org.djodjo.tarator.testapp.R;
import org.djodjo.tarator.testapp.SyncActivity;
import org.djodjo.tarator.testapp.SyncActivity.HelloWorldServer;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

/**
 * Example for {@link CountingIdlingResource}. Demonstrates how to wait on a delayed response from
 * request before continuing with a test.
 */
@LargeTest
public class AdvancedSynchronizationTest extends ActivityInstrumentationTestCase2<SyncActivity> {

  private class DecoratedHelloWorldServer implements HelloWorldServer {
    private final HelloWorldServer realHelloWorldServer;
    private final CountingIdlingResource helloWorldServerIdlingResource;

    private DecoratedHelloWorldServer(HelloWorldServer realHelloWorldServer,
        CountingIdlingResource helloWorldServerIdlingResource) {
      this.realHelloWorldServer = checkNotNull(realHelloWorldServer);
      this.helloWorldServerIdlingResource = checkNotNull(helloWorldServerIdlingResource);
    }

    @Override
    public String getHelloWorld() {
      // Use CountingIdlingResource to track in-flight calls to getHelloWorld (a simulation of a
      // network call). Whenever the count goes to zero, Tarator will be notified that this
      // resource is idle and the test will be able to proceed.
      helloWorldServerIdlingResource.increment();
      try {
        return realHelloWorldServer.getHelloWorld();
      } finally {
        helloWorldServerIdlingResource.decrement();
      }
    }
  }

  @SuppressWarnings("deprecation")
  public AdvancedSynchronizationTest() {
    // This constructor was deprecated - but we want to support lower API levels.
    super("org.djodjo.tarator.testapp", SyncActivity.class);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    SyncActivity activity = getActivity();
    HelloWorldServer realServer = activity.getHelloWorldServer();
    // Here, we use CountingIdlingResource - a common convenience class - to track the idle state of
    // the server. You could also do this yourself, by implementing the IdlingResource interface.
    CountingIdlingResource countingResource = new CountingIdlingResource("HelloWorldServerCalls");
    activity.setHelloWorldServer(new DecoratedHelloWorldServer(realServer, countingResource));
    registerIdlingResources(countingResource);
  }

  public void testCountingIdlingResource() {
    // Request the "hello world!" text by clicking on the request button.
    onView(withId(R.id.request_button)).perform(click());

    // Tarator waits for the resource to go idle and then continues.

    // The check if the text is visible can pass now.
    onView(withId(R.id.status_text)).check(matches(withText(R.string.hello_world)));
  }
}
