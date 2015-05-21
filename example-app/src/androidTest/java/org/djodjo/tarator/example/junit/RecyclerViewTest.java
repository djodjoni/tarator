package org.djodjo.tarator.example.junit;


import android.app.Activity;

import org.djodjo.tarator.example.MainActivity;
import org.djodjo.tarator.example.R;
import org.djodjo.tarator.support.v7.action.RecyclerViewActions;
import org.junit.Ignore;

import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.action.ViewActions.click;
import static org.djodjo.tarator.assertion.ViewAssertions.matches;
import static org.djodjo.tarator.matcher.ViewMatchers.hasDescendant;
import static org.djodjo.tarator.matcher.ViewMatchers.isDisplayed;
import static org.djodjo.tarator.matcher.ViewMatchers.withId;
import static org.djodjo.tarator.matcher.ViewMatchers.withText;
import static org.djodjo.tarator.support.v4.action.DrawerActions.openDrawer;
import static org.djodjo.tarator.support.v7.Tarator.onRecyclerView;
import static org.djodjo.tarator.support.v7.matcher.ViewHolderMatchers.itemViewMatcher;
@Ignore
public class RecyclerViewTest extends BaseActivityTest {

    public RecyclerViewTest() {
        super(MainActivity.class);
    }

    Activity act;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        act = getActivity();
    }

    public void testSelection() throws Exception {
        openDrawer(R.id.drawer_layout);
        onView(withText(getInstrumentation().getTargetContext().getString(R.string.title_section1))).perform(click());
        onView(withId(R.id.list_images))
                .perform(RecyclerViewActions.smoothScrollToPosition(32));
        Thread.sleep(3000);
        onRecyclerView(withId(R.id.list_images)).onViewHolderAtPosition(41).assertThat().hasItemViewType(1);
        onRecyclerView(withId(R.id.list_images)).onViewHolderAtPosition(64).assertThat().hasItemViewType(0);
        onRecyclerView(withId(R.id.list_images)).onViewHolderAtPosition(75).onSubView(withText("title")).check(matches(withText("title"))).assertThat().isVisible();
        onRecyclerView(withId(R.id.list_images)).onViewHolder(itemViewMatcher(hasDescendant(withText("pos: " + 83)))).onSubView(withText("title")).check(matches(isDisplayed())).assertThat().isVisible();
        //   .<RecyclerViewInteraction>perform(RecyclerViewActions.smoothScrollToPosition(64));

        //        .perform(RecyclerViewActions.actionOnItemAtPosition(32, ViewActions.click()))
        ;
    }
}
