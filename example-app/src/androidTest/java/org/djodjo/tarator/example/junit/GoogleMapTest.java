package org.djodjo.tarator.example.junit;


import android.app.Activity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import org.djodjo.tarator.example.MainActivity;
import org.djodjo.tarator.example.R;

import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.action.ViewActions.click;
import static org.djodjo.tarator.matcher.ViewMatchers.withContentDescription;
import static org.djodjo.tarator.matcher.ViewMatchers.withId;
import static org.djodjo.tarator.matcher.ViewMatchers.withText;
import static org.djodjo.tarator.support.v4.action.DrawerActions.openDrawer;

public class GoogleMapTest extends BaseActivityTest {

    public GoogleMapTest() {
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
         onView(withText(getInstrumentation().getTargetContext().getString(R.string.title_section2))).perform(click());
        Thread.sleep(3000);
        View mapContainer  = onView(withId(R.id.map)).getTargetView();
        Log.d("TT", "tt map container - " + mapContainer);
        TextureView map = (TextureView) onView(withContentDescription("Google Map")).getTargetView();
        Log.d("TT", "tt map - " + map.getClass().getInterfaces()[0].getSuperclass());
        Log.d("TT", "tt map - " + map.getClass().getSuperclass().getInterfaces()[1].getSuperclass());

//        for(int i=0;i<map.getChildCount();i++) {
//            View marker = map.getChildAt(i);
//            Log.d("TT", "tt marker - " + marker);
//        }


        View marker = onView(withContentDescription("Item - 1.")).getTargetView();
        Log.d("TT", "tt marker - " + marker);

    }


}
