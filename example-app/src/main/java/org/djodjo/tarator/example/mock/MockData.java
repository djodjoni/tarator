package org.djodjo.tarator.example.mock;

import android.app.Activity;

import org.djodjo.json.JsonArray;
import org.djodjo.json.JsonElement;
import org.djodjo.json.JsonObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by sic on 22/04/15.
 */
public class MockData {

    public static JsonArray getMockJsonArray(int noElements, int picSize) {
        JsonArray res = new JsonArray();
        Random rand = new Random();

        for (int i = 0; i < noElements; i++) {
            int cc = (int) (Math.random() * 0x1000000);
            int cc2 = 0xFFFFFF00 ^ cc;
            String color = Integer.toHexString(cc);
            String color2 = Integer.toHexString((0xFFFFFF - cc));
            // String color2 = Integer.toHexString(cc2);
            double lat = 50 + rand.nextInt(300) / 100d;
            double lon = 4 + rand.nextInt(300) / 100d;
            res.add(
                    new JsonObject()
                            .put("pic", "http://dummyimage.com/" + picSize + "/" + color + "/" + color2)
                            .put("title", "Item - " + i)
                            .put("info", "info - " + color)
                            .put("info2", "info - " + color2)
                            .put("info3", "info - " + lat + ":" + lon)
                            .put("loc", new JsonArray().put(lat).put(lon))
            );
        }

        return res;
    }

    public static JsonArray getAssetsMock(Activity activity) {
        JsonArray res = new JsonArray();
        try {
            res = JsonElement.readFrom(new InputStreamReader(activity.getAssets()
                    .open("pics.json"))).asJsonArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;

    }
}
