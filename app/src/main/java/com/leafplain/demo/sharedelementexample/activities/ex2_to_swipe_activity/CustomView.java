package com.leafplain.demo.sharedelementexample.activities.ex2_to_swipe_activity;

import android.content.Context;
import android.os.Handler;
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

    // ref:https://read01.com/NJ48xB.html

    float y_first   = -1 ;
    float range     = 200  ;

    public boolean dispatchTouchEvent(MotionEvent event){

        if(mDragStateListener!=null){

            int listPosition = mDragStateListener.getListPosition();
            Log.i(TAG,"listPosition:" +listPosition);

        }

//        Log.i(TAG,"@@ state :" +state );
//        if(state == EVENT_CHILD){
//            return super.dispatchTouchEvent(event);
//        }else if(state == EVENT_CURRENT){
//            return true;
//        }else  if(state == EVENT_PARENT){
//            return super.dispatchTouchEvent(event);
////            return false;
//        }
        return super.dispatchTouchEvent(event);
    }

    private static final int EVENT_CHILD = 1;
    private static final int EVENT_PARENT = 2;
    private static final int EVENT_CURRENT = 3;
    private int state = EVENT_CHILD;

    private android.os.Handler Handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mPassTouchEventsUp = true;
            Log.i(TAG,"-- @@ -- runnable mPassTouchEventsUp:"+mPassTouchEventsUp );
        }
    };


    private SwipeToActivity activity=null;
    public void setActivity(SwipeToActivity activity){
        this.activity = activity;
    }

    private SwipeToActivity.DragStateListener mDragStateListener = null;
    public void setDragStateListener(SwipeToActivity.DragStateListener listener){
        mDragStateListener = listener;
    }



    private boolean mPassTouchEventsUp = false;
    public synchronized void setParentTouchEvents(boolean passTouchEventsUp){
//        Log.d(TAG, "@@ setParentTouchEvents:"+passTouchEventsUp);
        mPassTouchEventsUp = passTouchEventsUp;
    }
}