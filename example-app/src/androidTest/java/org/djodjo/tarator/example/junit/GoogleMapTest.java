package org.djodjo.tarator.example.junit;


import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import org.djodjo.tarator.example.MainActivity;
import org.djodjo.tarator.example.R;

import java.util.ArrayList;

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
        Thread.sleep(5000);
        View mapContainer  = onView(withId(R.id.map)).getTargetView();
        Log.d("TT", "tt map container - " + mapContainer);
        TextureView map = (TextureView) onView(withContentDescription("Google Map")).getTargetView();
        Log.d("TT", "tt map - " + map.getClass());
        Log.d("TT", "tt map - " + map.getClass().getSuperclass());



        ArrayList<View> inViews =  map.getTouchables();

        for( View inner : inViews) {
            Log.d("TT", "tt inner - " + inner.getClass());
//            Log.d("TT", "tt inner touchables - " + ((TextureView) touchable).getTouchables().get(0).getClass());
        }

//        for(int i=0;i<map.getChildCount();i++) {
//            View marker = map.getChildAt(i);
//            Log.d("TT", "tt marker - " + marker);
//        }


//        for(int i=0;i<500;i++) {
//            onView(withContentDescription("Item - "+ i+". ")).perform(click());
//        }

        GoogleMap theMap = ((SupportMapFragment)((AppCompatActivity)act).getSupportFragmentManager()
                .findFragmentById(R.id.container)
                .getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        Log.d("TT", "tt the map - " + theMap);


//        UiDevice device = UiDevice.getInstance(getInstrumentation());
//        UiObject markerr = device.findObject(new UiSelector().descriptionContains("Item - 32").instance(1));
//        Log.d("TT", "tt the marker CD - >" + markerr.getContentDescription() + "<");
//        markerr.clickAndWaitForNewWindow();


        onView(withContentDescription("Item - 323. ")).perform(click());

    }


}
