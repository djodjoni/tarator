package org.djodjo.tarator;


import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

public class CountingIdlingResource implements IdlingResource {
    private static final String TAG = "CountingIdlingResource";
    private final String resourceName;
    private final AtomicInteger counter;
    private final boolean debugCounting;
    private volatile ResourceCallback resourceCallback;
    private volatile long becameBusyAt;
    private volatile long becameIdleAt;

    public CountingIdlingResource(String resourceName) {
        this(resourceName, false);
    }

    public CountingIdlingResource(String resourceName, boolean debugCounting) {
        this.counter = new AtomicInteger(0);
        this.becameBusyAt = 0L;
        this.becameIdleAt = 0L;
        this.resourceName = (String)Checks.checkNotNull(resourceName);
        this.debugCounting = debugCounting;
    }

    public String getName() {
        return this.resourceName;
    }

    public boolean isIdleNow() {
        return this.counter.get() == 0;
    }

    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    public void increment() {
        int counterVal = this.counter.getAndIncrement();
        if(0 == counterVal) {
            this.becameBusyAt = SystemClock.uptimeMillis();
        }

        if(this.debugCounting) {
            Log.i("CountingIdlingResource", "Resource: " + this.resourceName + " in-use-count incremented to: " + (counterVal + 1));
        }

    }

    public void decrement() {
        int counterVal = this.counter.decrementAndGet();
        if(counterVal == 0) {
            if(null != this.resourceCallback) {
                this.resourceCallback.onTransitionToIdle();
            }

            this.becameIdleAt = SystemClock.uptimeMillis();
        }

        if(this.debugCounting) {
            if(counterVal == 0) {
                Log.i("CountingIdlingResource", "Resource: " + this.resourceName + " went idle! (Time spent not idle: " + (this.becameIdleAt - this.becameBusyAt) + ")");
            } else {
                Log.i("CountingIdlingResource", "Resource: " + this.resourceName + " in-use-count decremented to: " + counterVal);
            }
        }

        Checks.checkState(counterVal > -1, "Counter has been corrupted!", new Object[0]);
    }

    public void dumpStateToLogs() {
        StringBuilder message = (new StringBuilder("Resource: ")).append(this.resourceName).append(" inflight transaction count: ").append(this.counter.get());
        if(0L == this.becameBusyAt) {
            Log.i("CountingIdlingResource", message.append(" and has never been busy!").toString());
        } else {
            message.append(" and was last busy at: ").append(this.becameBusyAt);
            if(0L == this.becameIdleAt) {
                Log.w("CountingIdlingResource", message.append(" AND NEVER WENT IDLE!").toString());
            } else {
                message.append(" and last went idle at: ").append(this.becameIdleAt);
                Log.i("CountingIdlingResource", message.toString());
            }
        }

    }
}
