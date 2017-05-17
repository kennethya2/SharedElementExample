package com.leafplain.demo.sharedelementexample.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.leafplain.demo.sharedelementexample.MainActivity;
import com.leafplain.demo.sharedelementexample.activities.ex1_to_activity.RecyclerActivity;
import com.leafplain.demo.sharedelementexample.activities.ex3_to_pager_activity.PagerFromActivity;
import com.leafplain.demo.sharedelementexample.activities.ex2_to_swipe_activity.SwipeFromActivity;
import com.leafplain.demo.sharedelementexample.activities.to_swipe_image_activity.SwipeImageFromActivity;
import com.leafplain.demo.sharedelementexample.base.basecontract.MainContract;

/**
 * Created by kennethyeh on 2017/5/10.
 */

public class MainPresenter implements MainContract.Presenter<Integer>{

    private static final String ACTION_PRE = "com.leafplain.demo.action.";

    private Context context;
    public MainPresenter(Context context){
        this.context = context;
    }
    @Override
    public void onTypeClick(Integer clickInfo) {
        Intent intent = new Intent();
        switch (clickInfo){
            case MainActivity.MainOpenType.TO_ACTIVITY:
                Toast.makeText(context, "TO_ACTIVITY",Toast.LENGTH_SHORT).show();
                intent.setClass(context, RecyclerActivity.class);
//                intent.setAction(ACTION_PRE+"PersonalProfileActivity");
                break;

            case MainActivity.MainOpenType.TO_SWIPE_ACTIVITY:
                Toast.makeText(context, "TO_SWIPE_ACTIVITY",Toast.LENGTH_SHORT).show();
                intent.setClass(context, SwipeFromActivity.class);
//                intent.setAction(ACTION_PRE+"PersonalProfileActivity");
                break;

            case MainActivity.MainOpenType.TO_PAGER_ACTIVITY:
                Toast.makeText(context, "TO_PAGER_ACTIVITY",Toast.LENGTH_SHORT).show();
                intent.setClass(context, PagerFromActivity.class);
//                intent.setAction(ACTION_PRE+"PersonalProfileActivity");
                break;

            case MainActivity.MainOpenType.TO_SWIPE_IMAGE_ACTIVITY:
                Toast.makeText(context, "TO_SWIPE_IMAGE_ACTIVITY",Toast.LENGTH_SHORT).show();
                intent.setClass(context, SwipeImageFromActivity.class);
                break;
        }
        context.startActivity(intent);
    }


}