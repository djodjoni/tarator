package org.djodjo.tarator.example.junit;


import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import org.djodjo.tarator.example.MainActivity;


@LargeTest
public abstract class BaseActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    Activity act;

    public BaseActivityTest(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getInstrumentation ().getTargetContext());
        preferences.edit().clear().commit();
    }
}
