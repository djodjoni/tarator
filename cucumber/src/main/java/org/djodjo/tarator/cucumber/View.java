package org.djodjo.tarator.cucumber;


import cucumber.api.java.en.When;

import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.action.ViewActions.click;
import static org.djodjo.tarator.matcher.ViewMatchers.withId;

public class View {

    @When("^tap on view with id (d+)$")
    public void tap_on_view_with_id(int id) throws Throwable {
        onView(withId(id)).perform(click());
    }
}
