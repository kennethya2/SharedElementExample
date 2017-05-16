package com.leafplain.demo.sharedelementexample.activities.recycler_to_swipe_activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alexvasilkov.gestures.animation.ViewPosition;
import com.alexvasilkov.gestures.animation.ViewPositionAnimator.PositionUpdateListener;
import com.leafplain.demo.sharedelementexample.MyApplication;
import com.leafplain.demo.sharedelementexample.R;
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
        ListItemInfo info = (ListItemInfo) extras.getSerializable(RecyclerSwipeFromActivity.EXTRA_ITEM);
        String photoURL = (String) info.data;
        binding.descTV.setText(photoURL);

        String transitionName = extras.getString(RecyclerSwipeFromActivity.EXTRA_IMAGE_TRANSITION_NAME);
        Log.d(TAG,"transitionName:"+transitionName);

        String positionStr = extras.getString(RecyclerSwipeFromActivity.EXTRA_POSITION);
        Log.d(TAG,"positionStr:"+positionStr);

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
//                Log.d(TAG,"position:"+position);
//                Log.d(TAG,"isLeaving:"+isLeaving);
                binding.backgroundLayout.setVisibility(position == 0f ? View.INVISIBLE : View.VISIBLE);
                binding.backgroundLayout.getBackground().setAlpha((int) (255 * position));

                if (position == 0f && isLeaving) { // Exit finished
                    Log.i(TAG,"isLeaving:"+isLeaving);
                    // Finishing activity
                    finish();
//                    onBackPressed();
                }
            }
        });

//        binding.gestureLayout2.getPositionAnimator().enter(false);
        ViewPosition position = ViewPosition.unpack(positionStr);
        binding.gestureLayout2.getPositionAnimator().enter(position, false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
