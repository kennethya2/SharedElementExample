package com.leafplain.demo.sharedelementexample.activities.recycler_to_swipe_activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alexvasilkov.events.Events;
import com.alexvasilkov.gestures.animation.ViewPositionAnimator.PositionUpdateListener;
import com.leafplain.demo.sharedelementexample.MyApplication;
import com.leafplain.demo.sharedelementexample.R;
import com.leafplain.demo.sharedelementexample.activities.recycler_to_activity.RecyclerActivity;
import com.leafplain.demo.sharedelementexample.activities.swipe_to_activity.SwipeFromActivity;
import com.leafplain.demo.sharedelementexample.databinding.ActivityRecyclerSwipeToBinding;
import com.leafplain.demo.sharedelementexample.datamodel.info.ListItemInfo;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class RecyclerSwipeToActivity extends AppCompatActivity {

    private String TAG = "RecyclerSwipeToActivity";
    private ActivityRecyclerSwipeToBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportPostponeEnterTransition();
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_swipe_to);

        Bundle extras = getIntent().getExtras();
        ListItemInfo info = (ListItemInfo) extras.getSerializable(RecyclerActivity.EXTRA_ITEM);
        String photoURL = (String) info.data;
        binding.descTV.setText(photoURL);

        String transitionName = extras.getString(RecyclerActivity.EXTRA_IMAGE_TRANSITION_NAME);
        Log.d(TAG,"transitionName:"+transitionName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.photoPicIV.setTransitionName(transitionName);
        }

        MyApplication.imageLoader.displayImage(photoURL,
                binding.photoPicIV,
                MyApplication.options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        supportStartPostponedEnterTransition();
                    }
                });




        binding.gestureLayout2.getPositionAnimator().addPositionUpdateListener(new PositionUpdateListener() {
            @Override
            public void onPositionUpdate(float position, boolean isLeaving) {
                Log.d(TAG,"position:"+position);
                Log.d(TAG,"isLeaving:"+isLeaving);
                binding.backgroundLayout.setVisibility(position == 0f ? View.INVISIBLE : View.VISIBLE);
                binding.backgroundLayout.getBackground().setAlpha((int) (255 * position));
//                if(isLeaving){
//                    Log.i(TAG,"isLeaving:"+isLeaving);
//                    onBackPressed();
//                }

                if (position == 0f && isLeaving) { // Exit finished
                    Log.i(TAG,"isLeaving:"+isLeaving);
                    // Asking previous activity to show back original image
                    Events.create(SwipeFromActivity.EVENT_SHOW_IMAGE).param(true).post();

                    // By default end of exit animation will return GestureImageView into
                    // fullscreen state, this will make the image blink. So we need to hack this
                    // behaviour and keep image in exit state until activity is finished.
//                    binding.photoPicIV.getController().getSettings().disableBounds();
//                    binding.photoPicIV.getPositionAnimator().setState(0f, false, false);

                    // Finishing activity
//                    finish();
//                    overridePendingTransition(0, 0); // No activity animation
                    onBackPressed();
                }
            }
        });

        binding.gestureLayout2.getPositionAnimator().enter(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
