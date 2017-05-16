package com.leafplain.demo.sharedelementexample.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.leafplain.demo.sharedelementexample.MainActivity;
import com.leafplain.demo.sharedelementexample.activities.recycler_to_activity.RecyclerActivity;
import com.leafplain.demo.sharedelementexample.activities.recycler_to_pager.RecyclerToPagerFromActivity;
import com.leafplain.demo.sharedelementexample.activities.recycler_to_swipe_activity.RecyclerSwipeFromActivity;
import com.leafplain.demo.sharedelementexample.activities.swipe_to_activity.SwipeFromActivity;
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
            case MainActivity.MainOpenType.RECYCLER_TO_ACTIVITY:
                Toast.makeText(context, "RECYCLER_TO_ACTIVITY",Toast.LENGTH_SHORT).show();
                intent.setClass(context, RecyclerActivity.class);
//                intent.setAction(ACTION_PRE+"PersonalProfileActivity");
                break;

            case MainActivity.MainOpenType.RECYCLER_TO_SWIPE_ACTIVITY:
                Toast.makeText(context, "RECYCLER_TO_SWIPE_ACTIVITY",Toast.LENGTH_SHORT).show();
                intent.setClass(context, RecyclerSwipeFromActivity.class);
//                intent.setAction(ACTION_PRE+"PersonalProfileActivity");
                break;

            case MainActivity.MainOpenType.RECYCLER_TO_PAGER:
                Toast.makeText(context, "RECYCLER_TO_PAGER",Toast.LENGTH_SHORT).show();
                intent.setClass(context, RecyclerToPagerFromActivity.class);
//                intent.setAction(ACTION_PRE+"PersonalProfileActivity");
                break;

            case MainActivity.MainOpenType.SWIPE_TO_ACTIVITY:
                Toast.makeText(context, "SWIPE_TO_ACTIVITY",Toast.LENGTH_SHORT).show();
                intent.setClass(context, SwipeFromActivity.class);
                break;
        }
        context.startActivity(intent);
    }


}