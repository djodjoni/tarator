package org.djodjo.tarator.example.junit;


import android.app.Activity;

import org.djodjo.tarator.example.MainActivity;
import org.djodjo.tarator.example.R;

import static org.djodjo.tarator.Tarator.onTextView;
import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.action.ViewActions.click;
import static org.djodjo.tarator.assertion.ViewAssertions.matches;
import static org.djodjo.tarator.matcher.ViewMatchers.isDisplayed;
import static org.djodjo.tarator.matcher.ViewMatchers.withText;
import static org.djodjo.tarator.support.v4.action.DrawerActions.closeDrawer;
import static org.djodjo.tarator.support.v4.action.DrawerActions.openDrawer;


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
        closeDrawer(R.id.drawer_layout);
        openDrawer(R.id.drawer_layout);
        onTextView(
                withText(getInstrumentation().getTargetContext().getString(R.string.title_section1)))
                .check(matches(isDisplayed()))
                .assertThat().isVisible()
        ;


        onView(withText(getInstrumentation().getTargetContext().getString(R.string.title_section1)))
                .perform(click());
        //must be closed anyway
        closeDrawer(R.id.drawer_layout);
        openDrawer(R.id.drawer_layout);
        closeDrawer(R.id.drawer_layout);


    }


}
