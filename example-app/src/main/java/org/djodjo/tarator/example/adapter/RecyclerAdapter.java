/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.djodjo.tarator.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import org.djodjo.comm.jus.toolbox.NetworkImageView;
import org.djodjo.json.JsonArray;
import org.djodjo.tarator.example.ImageListFragment;
import org.djodjo.tarator.example.R;
import org.djodjo.tarator.example.api.MyJus;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final String TAG = "RecyclerAdapter";

    //view anim
    protected static final long ANIM_DEFAULT_SPEED = 1000L;

    protected Interpolator interpolator;
    protected ImageListFragment.ListScrollListener scrollListener;
    protected SparseBooleanArray positionsMapper;
    protected int lastPosition;
    protected int height;
    // protected EstateResultScrollListener scrollListener;

    protected double speed;
    protected long animDuration;
    //

    private static final int ITEM_PLACE = 0;
    private static final int ITEM_TOWN = 1;
    private static final int ITEM_MUSEUM = 2;

    private JsonArray jarr;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
       // private final TextView textView;

       public final NetworkImageView niv ;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            niv = (NetworkImageView) v.findViewById(R.id.img1);
          //  textView = (TextView) v.findViewById(R.id.textView);
        }

    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     */
    public RecyclerAdapter(JsonArray jsonArray, Activity activity, ImageListFragment.ListScrollListener scrollListener) {
        jarr = jsonArray;
        this.scrollListener = scrollListener;
        animDuration = ANIM_DEFAULT_SPEED;
        lastPosition = -1;
        positionsMapper = new SparseBooleanArray(jarr.size());

        interpolator = new DecelerateInterpolator();

        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        height = windowManager.getDefaultDisplay().getHeight();
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rv_card, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.niv.setImageUrl(jarr.get(position).asJsonObject().getString("pic"), MyJus.getImageLoader());

        /// COOL ANIM
        View v = (View) viewHolder.niv.getParent().getParent();
        if (v != null && !positionsMapper.get(position) && position > lastPosition) {
            speed = scrollListener.getSpeed();
            animDuration = (((int) speed) == 0) ? ANIM_DEFAULT_SPEED : (long) ((1 / speed) * 3000);
            Log.d(TAG, "scroll speed/dur : " + speed + " / " + animDuration);
            //animDuration = ANIM_DEFAULT_SPEED;

            if (animDuration > ANIM_DEFAULT_SPEED)
                animDuration = ANIM_DEFAULT_SPEED;

            lastPosition = position;

            v.setTranslationX(0.0F);
            v.setTranslationY(height);
            v.setRotationX(35.0F);
            v.setScaleX(0.7F);
            v.setScaleY(0.55F);

            ViewPropertyAnimator localViewPropertyAnimator =
                    v.animate().rotationX(0.0F).rotationY(0.0F).translationX(0).translationY(0).setDuration(animDuration).scaleX(
                            1.0F).scaleY(1.0F).setInterpolator(interpolator);

            localViewPropertyAnimator.setStartDelay(0).start();
            positionsMapper.put(position, true);
        }

    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return jarr.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
