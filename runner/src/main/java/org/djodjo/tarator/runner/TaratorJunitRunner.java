package org.djodjo.tarator.runner;


import android.support.test.internal.runner.tracker.UsageTracker;
import android.support.test.internal.runner.tracker.UsageTrackerRegistry;
import android.support.test.runner.AndroidJUnitRunner;

public class TaratorJunitRunner extends AndroidJUnitRunner {

    public void onStart() {
        super.onStart();
        if(!this.getBooleanArgument("disableAnalytics") && null != this.getTargetContext()) {
            UsageTracker usageTracker = (new TaratorAnalyticsUsageTracker.Builder(this.getTargetContext())).buildIfPossible();
            if(null != usageTracker) {
                UsageTrackerRegistry.registerInstance(usageTracker);
            }
        }
    }

    private boolean getBooleanArgument(String tag) {
        String tagString = this.getArguments().getString(tag);
        return tagString != null && Boolean.parseBoolean(tagString);
    }

}
