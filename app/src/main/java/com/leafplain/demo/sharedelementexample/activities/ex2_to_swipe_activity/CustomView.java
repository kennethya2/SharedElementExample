package com.leafplain.demo.sharedelementexample.activities.ex2_to_swipe_activity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by kennethyeh on 2017/5/17.
 */

public class CustomView extends RelativeLayout {

    private final String TAG = "CustomView";
    public CustomView(Context context) {
        super(context);

    }
    public CustomView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(final MotionEvent ev) {

        if (mPassTouchEventsUp) {
            Log.i(TAG, "onInterceptTouchEvent:"+mPassTouchEventsUp);
            return true;
        } else {
            Log.d(TAG, "onInterceptTouchEvent:"+mPassTouchEventsUp);
            return false;
//            return super.onInterceptTouchEvent(ev);
        }
    }

    private boolean mPassTouchEventsUp = false;
    public synchronized void setParentTouchEvents(boolean passTouchEventsUp){
        Log.d(TAG, "@@ setParentTouchEvents:"+passTouchEventsUp);
        mPassTouchEventsUp = passTouchEventsUp;
    }
}