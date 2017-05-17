package com.leafplain.demo.sharedelementexample.activities.ex1_to_activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.leafplain.demo.sharedelementexample.MyApplication;
import com.leafplain.demo.sharedelementexample.R;
import com.leafplain.demo.sharedelementexample.databinding.ActivityItemDetailBinding;
import com.leafplain.demo.sharedelementexample.datamodel.info.ListItemInfo;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ItemDetailActivity extends AppCompatActivity {

    private String TAG = "ItemDetailActivity";

    private ActivityItemDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportPostponeEnterTransition();
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_detail);

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
    }
}
