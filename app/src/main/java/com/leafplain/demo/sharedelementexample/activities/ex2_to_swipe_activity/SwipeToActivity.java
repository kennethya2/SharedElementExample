package com.leafplain.demo.sharedelementexample.activities.ex2_to_swipe_activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.alexvasilkov.gestures.animation.ViewPosition;
import com.alexvasilkov.gestures.animation.ViewPositionAnimator.PositionUpdateListener;
import com.leafplain.demo.sharedelementexample.MyApplication;
import com.leafplain.demo.sharedelementexample.R;
import com.leafplain.demo.sharedelementexample.adaptercontrol.BaseCustomHolder;
import com.leafplain.demo.sharedelementexample.adaptercontrol.FactoryListHolder;
import com.leafplain.demo.sharedelementexample.databinding.ActivityRecyclerSwipeToBinding;
import com.leafplain.demo.sharedelementexample.databinding.HolderPhotoPicBinding;
import com.leafplain.demo.sharedelementexample.datamodel.info.ListItemInfo;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import static com.leafplain.demo.sharedelementexample.activities.ex2_to_swipe_activity.SwipeFromActivity.EXTRA_POSITION;

public class SwipeToActivity extends AppCompatActivity {

    private String TAG = "SwipeToActivity";
    private SwipeToActivity context;
    private ActivityRecyclerSwipeToBinding binding;
    private ListItemInfo info;

    private CustomView mParentLayot;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportPostponeEnterTransition();
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_swipe_to);
        mParentLayot = binding.parentLayot;
        mRecyclerView = binding.recyclerView;
        Bundle extras = getIntent().getExtras();
        info = (ListItemInfo) extras.getSerializable(SwipeFromActivity.EXTRA_ITEM);
        String photoURL = (String) info.data;
        binding.descTV.setText(photoURL);

        String transitionName = extras.getString(SwipeFromActivity.EXTRA_IMAGE_TRANSITION_NAME);
//        Log.d(TAG,"transitionName:"+transitionName);

        String positionStr = extras.getString(EXTRA_POSITION);
//        Log.d(TAG,"positionStr:"+positionStr);

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
                binding.backgroundLayout.setVisibility(position == 0f ? View.INVISIBLE : View.VISIBLE);
                binding.backgroundLayout.getBackground().setAlpha((int) (255 * position));
                if (position == 0f && isLeaving) { // Exit finished
                    // Finishing activity
                    finish();
                }else if(position==1L){
                    Log.d(TAG,"Stay Here!");
//                    mParentLayot.setParentTouchEvents(false);
//                    resetLayoutManager();
                }
            }
        });

//        binding.gestureLayout2.getPositionAnimator().enter(false);
//        ViewPosition position = ViewPosition.unpack(positionStr);
        ViewPosition resizePosition = resizePosition(positionStr);
        binding.gestureLayout2.getPositionAnimator().enter(resizePosition, false);//

        setRecyclerView();
    }

    //
    private final int shrinkValue = 6;
    private final int shrinkRatio = shrinkValue*2;
    private ViewPosition resizePosition(String oldPositionStr){
        String newPositionStr = "";
        ViewPosition position = ViewPosition.unpack(oldPositionStr);
        Rect rect = position.view;
        int newLeft     = rect.centerX()-(rect.width()/shrinkRatio);
        int newRight    = rect.centerX()+(rect.width()/shrinkRatio);
        int newTop      = rect.centerY()-(rect.height()/shrinkRatio);
        int newBottom   = rect.centerY()+(rect.height()/shrinkRatio);
        newPositionStr = ""+newLeft+" "+ newTop+" "+newRight+" "+newBottom;
        newPositionStr = newPositionStr+"#"+newPositionStr+"#"+newPositionStr;
//        Log.d(TAG,"newPositionStr:"+newPositionStr);
        ViewPosition newPosition = ViewPosition.unpack(newPositionStr);
        return newPosition;
    }

    private static final int TOTAL_SIZE = 8;
    private List<ListItemInfo> mList = new ArrayList<>();
    private void setRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
//        binding.recyclerView.addOnScrollListener(mOnScrollListener);
//        binding.recyclerView.setOnTouchListener(mOnTouchListener);
        for(int i =0 ; i<TOTAL_SIZE ; i++){
            ListItemInfo mListItemInfo = new ListItemInfo();
            mListItemInfo.type=ListItemInfo.ListType.PHOTO_PIC;
            mListItemInfo.data=(String) info.data;
            mList.add(mListItemInfo);
        }
        binding.recyclerView.setAdapter(new RecyclerViewAdapter(mList));
    }
    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<ListItemInfo> result;
        private FactoryListHolder listHolderFactory = new FactoryListHolder();

        public RecyclerViewAdapter(List<ListItemInfo> result){
            this.result=result;
        }

        @Override
        public int getItemCount() {
            return result.size();
        }

        @Override
        public int getItemViewType(int position) {
            return result.get(position).type;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return listHolderFactory.getHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            ListItemInfo item = result.get(position);
            BaseCustomHolder holder = (BaseCustomHolder) viewHolder;
            HolderPhotoPicBinding mHolderPhotoPicBinding = (HolderPhotoPicBinding) holder.getBinding();
            String url = (String) item.data;
            MyApplication.imageLoader.displayImage(url,
                    mHolderPhotoPicBinding.photoPicIV,
                    MyApplication.options,
                    MyApplication.animateFirstListener);
        }
    }

    private int totalItemCount;
    private int firstVisibleItem;
    private int lastVisibleItem;
    private int yMotion; // yMotion<0:go to top  yMotion>0:to to bottom
    private static final int WAIT_TOP_DRAG=1;
    private static final int WAIT_BOTTOM_DRAG=2;
    private static final int WAIT_NOTHING=3;
    private int dragState = WAIT_TOP_DRAG;
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager   = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            totalItemCount  = layoutManager.getItemCount();
            firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            if(layoutManager.findFirstCompletelyVisibleItemPosition()==0 && dy<=0){
                Log.i(TAG,"WAIT_TOP_DRAG");
                dragState = WAIT_TOP_DRAG;
            }else if(layoutManager.findLastCompletelyVisibleItemPosition()==totalItemCount-1 && dy>0){
                Log.i(TAG,"WAIT_BOTTOM_DRAG");
                dragState = WAIT_BOTTOM_DRAG;
            }else{
//                Log.d(TAG,"WAIT_NOTHING");
                dragState = WAIT_NOTHING;
            }
//            Log.d(TAG,"@@ ----------");
        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
    };
    float y_down ;
    float y_last =-1 ;
    private RecyclerView.OnTouchListener mOnTouchListener = new RecyclerView.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch(event.getAction())
            {
                case MotionEvent.ACTION_UP:
                    y_down = event.getY();
                    Log.d("move","ACTION_UP");
                    y_last =-1;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if(y_last!=-1){
                        if(event.getY()>y_last){
                            if( dragState == WAIT_TOP_DRAG){
                                mParentLayot.setParentTouchEvents(true);
                                Log.i(TAG,"Parent Top Grag");
                            }
                            Log.d("move","swipe to down");
                        }else{
                            if( dragState == WAIT_BOTTOM_DRAG){
                                mParentLayot.setParentTouchEvents(true);
                                Log.d(TAG,"Parent Bottom Grag");
                            }
                            Log.i("move","swipe to up");
                        }
                    }
                    y_last = event.getY();
                    break;

            }
            return false;
        }
    };

    private void resetLayoutManager(){
        Log.i(TAG, "resetListener");
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
