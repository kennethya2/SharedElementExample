package com.leafplain.demo.sharedelementexample.activities.ex2_to_swipe_activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by kennethyeh on 2017/5/22.
 */

public class ParentGestureRecyclerView extends RecyclerView{

    private final String TAG = "CustomRecycle";
    public ParentGestureRecyclerView(Context context) {
        super(context);

    }
    public ParentGestureRecyclerView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    public ParentGestureRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if(parentScroll){
            Log.i(TAG,"parentScroll:"+parentScroll);
            return false;
        }else{
            Log.d(TAG,"parentScroll:"+parentScroll);
            return super.dispatchTouchEvent(event);
        }
    }




    private boolean parentScroll = false;
    public synchronized void setParentScroll(boolean parentCanScroll){
//        parentScroll = parentCanScroll;
        Log.i(TAG,"@@ parentScroll:"+parentScroll);
    }
}
