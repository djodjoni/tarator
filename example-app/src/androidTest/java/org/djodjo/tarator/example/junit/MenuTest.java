package org.djodjo.tarator.example.junit;


import android.app.Activity;

import org.djodjo.tarator.example.MainActivity;
import org.djodjo.tarator.example.R;
import org.djodjo.tarator.iteraction.TextViewInteraction;
import org.djodjo.tarator.support.v7.action.RecyclerViewActions;

import static org.djodjo.tarator.Tarator.onTextView;
import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.action.ViewActions.click;
import static org.djodjo.tarator.assertion.ViewAssertions.matches;
import static org.djodjo.tarator.matcher.ViewMatchers.isDisplayed;
import static org.djodjo.tarator.matcher.ViewMatchers.withId;
import static org.djodjo.tarator.matcher.ViewMatchers.withText;

public class MenuTest extends BaseActivityTest {

    public MenuTest() {
        super(MainActivity.class);
    }

    Activity act;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        act = getActivity();
    }

    public void testSelection() throws Exception {


        onTextView(withText(getInstrumentation().getTargetContext().getString(R.string.title_section1))).<TextViewInteraction>check(matches(isDisplayed())).assertThat().isVisible();
        // onTextView(withText(getInstrumentation().getTargetContext().getString(item))).check(matches(isDisplayed())).assertThat().isVisible();

        //  onButton(withText(getInstrumentation().getTargetContext().getString(item))).assertThat().isNotEmpty().hasTextSize(50);
        //   Thread.sleep(2000);

        onView(withText(getInstrumentation().getTargetContext().getString(R.string.title_section1))).perform(click());
        onView(withId(R.id.list_images))
                .perform(RecyclerViewActions.scrollToPosition(32))

        //        .perform(RecyclerViewActions.actionOnItemAtPosition(32, ViewActions.click()))
        ;
        Thread.sleep(7000);


    }
}
