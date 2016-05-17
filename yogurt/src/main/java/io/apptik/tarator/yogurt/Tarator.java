package io.apptik.tarator.yogurt;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.base.ViewFinderImpl;
import android.view.View;

import org.hamcrest.Matcher;

import io.apptik.tarator.yogurt.view.ViewInteraction;

public class Tarator {

    private Tarator() {}

    public static ViewInteraction onView(final Matcher<View> viewMatcher) {
        android.support.test.espresso.ViewInteraction eViewInteraction = Espresso.onView(viewMatcher);
        //todo get view finder usign reflection

    }
}
