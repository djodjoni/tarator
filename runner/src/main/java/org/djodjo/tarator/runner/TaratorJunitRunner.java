package org.djodjo.tarator.runner;


import android.os.Bundle;
import android.support.test.internal.runner.tracker.UsageTracker;
import android.support.test.internal.runner.tracker.UsageTrackerRegistry;
import android.support.test.runner.AndroidJUnitRunner;
import android.util.Log;

public class TaratorJunitRunner extends AndroidJUnitRunner {

    private static final String TAG="TaratorJunitRunner";
    private Bundle mArguments;

    @Override
    public void onCreate(Bundle bundle)
    {
        Log.d(TAG, "tarator-junit-runner on Create");
        this.mArguments = bundle;
        super.onCreate(bundle);
    }

    public void onStart() {
        super.onStart();
        if(!this.getBooleanArgument("disableAnalytics") && null != this.getTargetContext()) {
            UsageTracker usageTracker = (new TaratorAnalyticsUsageTracker.Builder(this.getTargetContext())).buildIfPossible();
            if(null != usageTracker) {
                UsageTrackerRegistry.registerInstance(usageTracker);
            }
        }
    }
    private Bundle getArguments() {
        return this.mArguments;
    }

    private boolean getBooleanArgument(String tag) {
        String tagString = this.getArguments().getString(tag);
        return tagString != null && Boolean.parseBoolean(tagString);
    }

}
