package org.djodjo.tarator.example.api;


import android.content.Context;

import org.djodjo.comm.jus.RequestQueue;
import org.djodjo.comm.jus.toolbox.ImageLoader;
import org.djodjo.comm.jus.toolbox.Jus;

public class MyJus {
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;


    private MyJus() {
        // no instances
    }


    public static void init(Context context) {
        mRequestQueue = Jus.newRequestQueue(context);

        mImageLoader = new ImageLoader(mRequestQueue,
                // new NoCache()
                new BitmapLruCache()
        );
    }


    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }


    /**
     * Returns instance of ImageLoader initialized with {@see FakeImageCache} which effectively means
     * that no memory caching is used. This is useful for images that you know that will be show
     * only once.
     *
     * @return
     */
    public static ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }
}
