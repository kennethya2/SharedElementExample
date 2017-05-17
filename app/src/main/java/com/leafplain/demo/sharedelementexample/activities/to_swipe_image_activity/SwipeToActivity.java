package com.leafplain.demo.sharedelementexample.activities.to_swipe_image_activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;

import com.alexvasilkov.gestures.animation.ViewPosition;
import com.alexvasilkov.gestures.animation.ViewPositionAnimator.PositionUpdateListener;
import com.leafplain.demo.sharedelementexample.MyApplication;
import com.leafplain.demo.sharedelementexample.R;
import com.leafplain.demo.sharedelementexample.databinding.ActivitySwipeToBinding;
import com.leafplain.demo.sharedelementexample.datamodel.info.ListItemInfo;

import org.greenrobot.eventbus.EventBus;

public class SwipeToActivity extends AppCompatActivity {

    private String TAG = "SwipeToActivity";

    private static final String EXTRA_POSITION = "position";
    private static final String EXTRA_ITEM = "item";

    private String positionStr = "";
    private ListItemInfo item;

    private ActivitySwipeToBinding binding;

    public static void open(Activity from, ViewPosition position, ListItemInfo item) {
        Intent intent = new Intent(from, SwipeToActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_POSITION, position.pack());
        extras.putSerializable(EXTRA_ITEM, item);
        intent.putExtras(extras);
        from.startActivity(intent);
        from.overridePendingTransition(0, 0); // No activity animation
    }
//    private GestureImageView image;
    private boolean hideOrigImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_swipe_to);

        Bundle extras = getIntent().getExtras();
        positionStr = extras.getString(EXTRA_POSITION, "");
        item = (ListItemInfo) extras.getSerializable(EXTRA_ITEM);
        String url = (String) item.data;
//        Log.d(TAG,"positionStr:"+positionStr);
//        Log.d(TAG,"url:"+url);

        MyApplication.imageLoader.displayImage(url,
                binding.photoPicIV,
                MyApplication.options,
                MyApplication.animateFirstListener);


        // Ensuring that original image is hidden as soon as image is loaded and drawn
        binding.photoPicIV.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (hideOrigImage) {
                    binding.photoPicIV.getViewTreeObserver().removeOnPreDrawListener(this);
                    // Asking previous activity to hide original image
//                    Events.create(SwipeFromActivity.EVENT_SHOW_IMAGE).param(false).post();
                    EventBus.getDefault().post(SwipeFromActivity.ImageVisibility.getInstnce().setVisibility(false));
                } else if (binding.photoPicIV.getDrawable() != null) {
                    // Requesting hiding original image after first drawing
                    hideOrigImage = true;
                }
                return true;
            }
        });

        // Listening for end of exit animation
        binding.photoPicIV.getPositionAnimator().addPositionUpdateListener(new PositionUpdateListener() {
            @Override
            public void onPositionUpdate(float position, boolean isLeaving) {
                binding.backgroundLayout.setVisibility(position == 0f ? View.INVISIBLE : View.VISIBLE);
                binding.backgroundLayout.getBackground().setAlpha((int) (255 * position));
                if (position == 0f && isLeaving) { // Exit finished
                    // Asking previous activity to show back original image
                    EventBus.getDefault().post(SwipeFromActivity.ImageVisibility.getInstnce().setVisibility(true));
                    Log.d(TAG,"isLeaving...");

                    // By default end of exit animation will return GestureImageView into
                    // fullscreen state, this will make the image blink. So we need to hack this
                    // behaviour and keep image in exit state until activity is finished.
                    binding.photoPicIV.getController().getSettings().disableBounds();
                    binding.photoPicIV.getPositionAnimator().setState(0f, false, false);
                    // later finish avoid previous activity original image visible blink
                    // Finishing activity
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            overridePendingTransition(0, 0); // No activity animation
                            Log.i(TAG,"finish");
                        }
                    }, 100);
//                    finish();
//                    overridePendingTransition(0, 0); // No activity animation
                }
            }
        });

        // Playing enter animation from provided position
        ViewPosition position = ViewPosition.unpack(positionStr);
        boolean animate = savedInstanceState == null; // No animation when restoring activity
        binding.photoPicIV.getPositionAnimator().enter(position, animate);
    }

    @Override
    public void onBackPressed() {
        if (!binding.photoPicIV.getPositionAnimator().isLeaving()) {
            binding.photoPicIV.getPositionAnimator().exit(true);
        }
//        super.onBackPressed(); // no back animation
    }
}
