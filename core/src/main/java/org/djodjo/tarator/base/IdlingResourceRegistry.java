package org.djodjo.tarator.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.djodjo.tarator.IdlingPolicies;
import org.djodjo.tarator.IdlingPolicy;
import org.djodjo.tarator.IdlingResource;
import org.djodjo.tarator.IdlingResource.ResourceCallback;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Keeps track of user-registered {@link IdlingResource}s.
 */
@Singleton
public final class IdlingResourceRegistry {
  private static final String TAG = IdlingResourceRegistry.class.getSimpleName();

  private static final int DYNAMIC_RESOURCE_HAS_IDLED = 1;
  private static final int TIMEOUT_OCCURRED = 2;
  private static final int IDLE_WARNING_REACHED = 3;
  private static final int POSSIBLE_RACE_CONDITION_DETECTED = 4;
  private static final Object TIMEOUT_MESSAGE_TAG = new Object();

  private static final IdleNotificationCallback NO_OP_CALLBACK = new IdleNotificationCallback() {

    @Override
    public void allResourcesIdle() {}

    @Override
    public void resourcesStillBusyWarning(List<String> busys) {}

    @Override
    public void resourcesHaveTimedOut(List<String> busys) {}
  };

  // resources and idleState should only be accessed on main thread
  private final List<IdlingResource> resources = Lists.newArrayList();
  // idleState.get(i) == true indicates resources.get(i) is idle, false indicates it's busy
  private final BitSet idleState = new BitSet();
  private final Looper looper;
  private final Handler handler;
  private final Dispatcher dispatcher;
  private IdleNotificationCallback idleNotificationCallback = NO_OP_CALLBACK;

  @Inject
  public IdlingResourceRegistry(Looper looper) {
    this.idleNotificationCallback = NO_OP_CALLBACK;
    this.looper = looper;
    this.dispatcher = new Dispatcher();
    this.handler = new Handler(looper, dispatcher);
  }

  /**
   * Registers the given resources.
   */
  public boolean registerResources(final List<? extends IdlingResource> resourceList) {
    if(Looper.myLooper() != this.looper) {
      return ((Boolean)this.runSynchronouslyOnMainThread(new Callable() {
        public Boolean call() {
          return Boolean.valueOf(IdlingResourceRegistry.this.registerResources(resourceList));
        }
      })).booleanValue();
    } else {
      boolean allRegisteredSuccesfully = true;
      Iterator i$ = resourceList.iterator();

      while(i$.hasNext()) {
        IdlingResource resource = (IdlingResource)i$.next();
        Preconditions.checkNotNull(resource.getName(), "IdlingResource.getName() should not be null");
        boolean duplicate = false;
        Iterator position = this.resources.iterator();

        while(true) {
          if(position.hasNext()) {
            IdlingResource oldResource = (IdlingResource)position.next();
            if(!resource.getName().equals(oldResource.getName())) {
              continue;
            }

            Log.e(TAG, String.format("Attempted to register resource with same names: %s. R1: %s R2: %s.\nDuplicate resource registration will be ignored.", new Object[]{resource.getName(), resource, oldResource}));
            duplicate = true;
          }

          if(!duplicate) {
            this.resources.add(resource);
            int position1 = this.resources.size() - 1;
            this.registerToIdleCallback(resource, position1);
            this.idleState.set(position1, resource.isIdleNow());
          } else {
            allRegisteredSuccesfully = false;
          }
          break;
        }
      }

      return allRegisteredSuccesfully;
    }
  }

  public boolean unregisterResources(final List<? extends IdlingResource> resourceList) {
    if(Looper.myLooper() != this.looper) {
      return ((Boolean)this.runSynchronouslyOnMainThread(new Callable() {
        public Boolean call() {
          return Boolean.valueOf(IdlingResourceRegistry.this.unregisterResources(resourceList));
        }
      })).booleanValue();
    } else {
      boolean allUnregisteredSuccesfully = true;
      Iterator i$ = resourceList.iterator();

      while(i$.hasNext()) {
        IdlingResource resource = (IdlingResource)i$.next();
        int resourceIndex = this.resources.indexOf(resource);
        if(resourceIndex != -1) {
          for(int i = resourceIndex; i < this.resources.size(); ++i) {
            this.idleState.set(i, this.idleState.get(i + 1));
          }

          this.resources.remove(resourceIndex);
        } else {
          allUnregisteredSuccesfully = false;
          Log.e(TAG, String.format("Attempted to unregister resource that is not registered: \'%s\'. Resource list: %s", new Object[]{resource.getName(), this.resources}));
        }
      }

      return allUnregisteredSuccesfully;
    }
  }



  public void registerLooper(Looper looper, boolean considerWaitIdle) {
    checkNotNull(looper);
    checkArgument(Looper.getMainLooper() != looper, "Not intended for use with main looper!");
    registerResources(Lists.newArrayList(new LooperIdlingResource[]{new LooperIdlingResource(looper, considerWaitIdle)}));
  }

  private void registerToIdleCallback(final IdlingResource resource, final int position) {
    resource.registerIdleTransitionCallback(new ResourceCallback() {
      @Override
      public void onTransitionToIdle() {
        Message m = handler.obtainMessage(DYNAMIC_RESOURCE_HAS_IDLED);
        m.arg1 = position;
        handler.sendMessage(m);
      }
    });
  }

  public List<IdlingResource> getResources() {
    return (List)(Looper.myLooper() != this.looper?(List)this.runSynchronouslyOnMainThread(new Callable() {
      public List<IdlingResource> call() {
        return IdlingResourceRegistry.this.getResources();
      }
    }): ImmutableList.copyOf(this.resources));
  }

  boolean allResourcesAreIdle() {
    checkState(Looper.myLooper() == looper);
    for (int i = idleState.nextSetBit(0); i >= 0 && i < resources.size();
        i = idleState.nextSetBit(i + 1)) {
      idleState.set(i, resources.get(i).isIdleNow());
    }
    return idleState.cardinality() == resources.size();
  }

  interface IdleNotificationCallback {
    public void allResourcesIdle();

    public void resourcesStillBusyWarning(List<String> busyResourceNames);

    public void resourcesHaveTimedOut(List<String> busyResourceNames);
  }

  void notifyWhenAllResourcesAreIdle(IdleNotificationCallback callback) {
    checkNotNull(callback);
    checkState(Looper.myLooper() == looper);
    checkState(idleNotificationCallback == NO_OP_CALLBACK, "Callback has already been registered.");
    if (allResourcesAreIdle()) {
      callback.allResourcesIdle();
    } else {
      idleNotificationCallback = callback;
      scheduleTimeoutMessages();
    }
  }

  void cancelIdleMonitor() {
    dispatcher.deregister();
  }

  private <T> T runSynchronouslyOnMainThread(Callable<T> task) {
    FutureTask futureTask = new FutureTask(task);
    this.handler.post(futureTask);

    try {
      return (T) futureTask.get();
    } catch (CancellationException var4) {
      throw new RuntimeException(var4);
    } catch (ExecutionException var5) {
      throw new RuntimeException(var5);
    } catch (InterruptedException var6) {
      throw new RuntimeException(var6);
    }
  }

  private void scheduleTimeoutMessages() {
    IdlingPolicy warning = IdlingPolicies.getDynamicIdlingResourceWarningPolicy();
    Message timeoutWarning = handler.obtainMessage(IDLE_WARNING_REACHED, TIMEOUT_MESSAGE_TAG);
    handler.sendMessageDelayed(timeoutWarning, warning.getIdleTimeoutUnit().toMillis(
        warning.getIdleTimeout()));
    Message timeoutError = handler.obtainMessage(TIMEOUT_OCCURRED, TIMEOUT_MESSAGE_TAG);
    IdlingPolicy error = IdlingPolicies.getDynamicIdlingResourceErrorPolicy();

    handler.sendMessageDelayed(timeoutError, error.getIdleTimeoutUnit().toMillis(
        error.getIdleTimeout()));
  }

  private List<String> getBusyResources() {
    List<String> busyResourceNames = Lists.newArrayList();
    List<Integer> racyResources = Lists.newArrayList();

    for (int i = 0; i < resources.size(); i++) {
      IdlingResource resource = resources.get(i);
      if (!idleState.get(i)) {
        if (resource.isIdleNow()) {
          // We have not been notified of a BUSY -> IDLE transition, but the resource is telling us
          // its that its idle. Either it's a race condition or is this resource buggy.
          racyResources.add(i);
        } else {
          busyResourceNames.add(resource.getName());
        }
      }
    }

    if (!racyResources.isEmpty()) {
      Message raceBuster = handler.obtainMessage(POSSIBLE_RACE_CONDITION_DETECTED,
          TIMEOUT_MESSAGE_TAG);
      raceBuster.obj = racyResources;
      handler.sendMessage(raceBuster);
      return null;
    } else {
      return busyResourceNames;
    }
  }


  private class Dispatcher implements Handler.Callback {
    @Override
    public boolean handleMessage(Message m) {
      switch (m.what) {
        case DYNAMIC_RESOURCE_HAS_IDLED:
          handleResourceIdled(m);
          break;
        case IDLE_WARNING_REACHED:
          handleTimeoutWarning();
          break;
        case TIMEOUT_OCCURRED:
          handleTimeout();
          break;
        case POSSIBLE_RACE_CONDITION_DETECTED:
          handleRaceCondition(m);
          break;
        default:
          Log.w(TAG, "Unknown message type: " + m);
          return false;
      }
      return true;
    }

    private void handleResourceIdled(Message m) {
      int position = m.arg1;
      IdlingResource resource = (IdlingResource)m.obj;
      if(position < IdlingResourceRegistry.this.resources.size() && IdlingResourceRegistry.this.resources.get(position) == resource) {
        IdlingResourceRegistry.this.idleState.set(position, true);
        if(IdlingResourceRegistry.this.idleState.cardinality() == IdlingResourceRegistry.this.resources.size()) {
          try {
            IdlingResourceRegistry.this.idleNotificationCallback.allResourcesIdle();
          } finally {
            this.deregister();
          }
        }

      } else {
        Log.i(IdlingResourceRegistry.TAG, "Ignoring message from unregistered resource: " + resource);
      }
    }

    private void handleTimeoutWarning() {
      List<String> busyResources = getBusyResources();
      if (busyResources == null) {
        // null indicates that there is either a race or a programming error
        // a race detector message has been inserted into the q.
        // reinsert the idle_warning_reached message into the q directly after it
        // so we generate warnings if the system is still sane.
        handler.sendMessage(handler.obtainMessage(IDLE_WARNING_REACHED, TIMEOUT_MESSAGE_TAG));
      } else {
        IdlingPolicy warning = IdlingPolicies.getDynamicIdlingResourceWarningPolicy();
        idleNotificationCallback.resourcesStillBusyWarning(busyResources);
        handler.sendMessageDelayed(
            handler.obtainMessage(IDLE_WARNING_REACHED, TIMEOUT_MESSAGE_TAG),
            warning.getIdleTimeoutUnit().toMillis(warning.getIdleTimeout()));
      }
    }

    private void handleTimeout() {
      List<String> busyResources = getBusyResources();
      if (busyResources == null) {
        // detected a possible race... we've enqueued a race busting message
        // so either that'll resolve the race or kill the app because it's buggy.
        // if the race resolves, we need to timeout properly.
        handler.sendMessage(handler.obtainMessage(TIMEOUT_OCCURRED, TIMEOUT_MESSAGE_TAG));
      } else {
        try {
          idleNotificationCallback.resourcesHaveTimedOut(busyResources);
        } finally {
          deregister();
        }
      }
    }

    @SuppressWarnings("unchecked")
    private void handleRaceCondition(Message m) {
      for (Integer i : (List<Integer>) m.obj) {
        if (idleState.get(i)) {
          // it was a race... i is now idle, everything is fine...
        } else {
          throw new IllegalStateException(String.format(
              "Resource %s isIdleNow() is returning true, but a message indicating that the "
              + "resource has transitioned from busy to idle was never sent.",
              resources.get(i).getName()));
        }
      }
    }

    private void deregister() {
      handler.removeCallbacksAndMessages(TIMEOUT_MESSAGE_TAG);
      idleNotificationCallback = NO_OP_CALLBACK;
    }
  }
}
