package org.djodjo.tarator.example.junit;


import android.app.Activity;

import org.djodjo.tarator.example.MainActivity;
import org.djodjo.tarator.example.R;
import org.djodjo.tarator.iteraction.TextViewInteraction;
import org.djodjo.tarator.support.v7.action.RecyclerViewActions;

import static org.djodjo.tarator.Tarator.onTextView;
import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.assertion.ViewAssertions.matches;
import static org.djodjo.tarator.matcher.ViewMatchers.hasDescendant;
import static org.djodjo.tarator.matcher.ViewMatchers.isDisplayed;
import static org.djodjo.tarator.matcher.ViewMatchers.withId;
import static org.djodjo.tarator.matcher.ViewMatchers.withText;
import static org.djodjo.tarator.support.v4.action.DrawerActions.closeDrawer;
import static org.djodjo.tarator.support.v7.Tarator.onRecyclerView;
import static org.djodjo.tarator.support.v7.matcher.ViewHolderMatchers.itemViewMatcher;

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

      //  onView(withText(getInstrumentation().getTargetContext().getString(R.string.title_section1))).perform(click());
        closeDrawer(R.id.drawer_layout);

        onView(withId(R.id.list_images))
                .perform(RecyclerViewActions.smoothScrollToPosition(32));
        Thread.sleep(3000);
        onRecyclerView(withId(R.id.list_images)).onViewHolderAtPosition(41).assertThat().hasItemViewType(1);
        onRecyclerView(withId(R.id.list_images)).onViewHolderAtPosition(64).assertThat().hasItemViewType(0);
        onRecyclerView(withId(R.id.list_images)).onViewHolderAtPosition(75).onSubView(withText("title")).check(matches(isDisplayed())).assertThat().isVisible();
        onRecyclerView(withId(R.id.list_images)).onViewHolder(itemViewMatcher(hasDescendant(withText("pos: " + 83)))).onSubView(withText("title")).check(matches(isDisplayed())).assertThat().isVisible();
        //   .<RecyclerViewInteraction>perform(RecyclerViewActions.smoothScrollToPosition(64));

        //        .perform(RecyclerViewActions.actionOnItemAtPosition(32, ViewActions.click()))
        ;
        Thread.sleep(7000);


    }
}
