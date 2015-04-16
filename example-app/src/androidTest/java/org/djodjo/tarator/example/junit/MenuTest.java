package org.djodjo.tarator.example.junit;


import android.app.Activity;

import org.djodjo.tarator.example.MainActivity;
import org.djodjo.tarator.example.R;

import java.util.ArrayList;

import static org.djodjo.tarator.Tarator.onButton;
import static org.djodjo.tarator.Tarator.onTextView;
import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.action.ViewActions.click;
import static org.djodjo.tarator.assertion.ViewAssertions.matches;
import static org.djodjo.tarator.matcher.ViewMatchers.isDisplayed;
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
        ArrayList<Integer> items = new ArrayList<>();

        items.add(R.string.title_section1);
//        items.add(R.string.title_section2);
//        items.add(R.string.title_section3);


        for (int item : items) {
            onTextView(withText(getInstrumentation().getTargetContext().getString(item))).check(matches(isDisplayed()));
            onButton(withText(getInstrumentation().getTargetContext().getString(item))).assertThat().isNotEmpty().hasTextSize(50);
            Thread.sleep(2000);
            onView(withText(getInstrumentation().getTargetContext().getString(item))).perform(click());


        }

    }
}
