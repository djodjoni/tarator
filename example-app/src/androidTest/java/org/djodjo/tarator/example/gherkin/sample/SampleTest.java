package org.djodjo.tarator.example.gherkin.sample;


import android.test.ActivityInstrumentationTestCase2;

import org.djodjo.tarator.example.MainActivity;


public class SampleTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public SampleTest() {
        super(MainActivity.class);
    }

//    @Given("^we are at section (\\d+) screen$")
//    public void we_are_at_section_screen(int arg1) throws Throwable {
//        int sectionStringID = 0;
//        switch (arg1) {
//            case 1: sectionStringID = R.string.title_section1; break;
//            case 2: sectionStringID = R.string.title_section2; break;
//            case 3: sectionStringID = R.string.title_section3; break;
//        }
//        Activity mainActivity = getActivity();
//        assertNotNull(mainActivity);
//        onView(withText(getInstrumentation().getTargetContext().getString(sectionStringID))).perform(click());
//    }
//
//    @When("^press example action$")
//    public void press_example_action() throws Throwable {
//        // Write code here that turns the phrase above into concrete actions
//       // throw new PendingException();
//    }
//
//    @Then("^we must pass$")
//    public void we_must_pass() throws Throwable {
//        // Write code here that turns the phrase above into concrete actions
//      //  throw new PendingException();
//    }
//    @Then("^we must fail$")
//    public void we_must_fail() throws Throwable {
//        // Write code here that turns the phrase above into concrete actions
//      //  throw new PendingException();
//    }


 }
