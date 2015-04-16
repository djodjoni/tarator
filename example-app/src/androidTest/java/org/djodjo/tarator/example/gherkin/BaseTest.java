package org.djodjo.tarator.example.gherkin;


import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import org.djodjo.tarator.example.MainActivity;
import org.djodjo.tarator.example.R;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.action.ViewActions.click;
import static org.djodjo.tarator.matcher.ViewMatchers.withText;


@CucumberOptions(features = "features", glue = "org.djodjo.tarator.example.test", tags = { "~@skip"}, format = { "json:/sdcard/tarator-example/report.json", "html:/sdcard/tarator-example/report-html"})

public class BaseTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public BaseTest() {
        super(MainActivity.class);
    }
//
//    @Override
//    public void setUp() throws Exception {
//        super.setUp();
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getInstrumentation ().getTargetContext());
//        preferences.edit().clear().commit();
//    }

    @Given("^we are at section (\\d+) screen$")
    public void we_are_at_section_screen(int arg1) throws Throwable {
        int sectionStringID = 0;
        switch (arg1) {
            case 1: sectionStringID = R.string.title_section1; break;
            case 2: sectionStringID = R.string.title_section2; break;
            case 3: sectionStringID = R.string.title_section3; break;
        }
        Activity mainActivity = getActivity();
        assertNotNull(mainActivity);
        onView(withText(getInstrumentation().getTargetContext().getString(sectionStringID))).perform(click());
        Thread.sleep(2000);
    }


    @When("^press example action$")
    public void press_example_action() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
       // throw new PendingException();
    }

    @Then("^we must pass$")
    public void we_must_pass() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
      //  throw new PendingException();
    }
    @Then("^we must fail$")
    public void we_must_fail() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
      //  throw new PendingException();
    }


 }
