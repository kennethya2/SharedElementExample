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

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportPostponeEnterTransition();
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_swipe_to);
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
                    y_first=-1;
                    dragState = DRAG_NOTHING;
                }
            }
        });

//        binding.gestureLayout2.getPositionAnimator().enter(false);
//        ViewPosition position = ViewPosition.unpack(positionStr);
        ViewPosition resizePosition = resizePosition(positionStr);
        binding.gestureLayout2.getPositionAnimator().enter(resizePosition, false);//

        setRecyclerView();
    }

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
        binding.recyclerView.addOnScrollListener(mOnScrollListener);
        binding.recyclerView.setOnTouchListener(mOnTouchListener);
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


    public interface DragStateListener{
        int getListPosition();
        int getDragState();
    }
    private DragStateListener mDragStateListener = new DragStateListener(){
        @Override
        public int getListPosition() {
            return listPosition;
        }

        @Override
        public int getDragState() {
            return dragState;
        }
    };

    private int totalItemCount;
    public static final int ON_TOP_DRAG=1;
    public static final int ON_BOTTOM_DRAG=2;
    public static final int DRAG_NOTHING=3;
    private int dragState = DRAG_NOTHING;

    public static final int POS_TOP     =1;
    public static final int POS_BOTTOM  =2;
    public static final int POS_OTHER   =3;
    private int listPosition = POS_TOP;

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager   = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            totalItemCount  = layoutManager.getItemCount();
            if(layoutManager.findFirstCompletelyVisibleItemPosition()==0 && dy<=0){
                Log.i(TAG,"POS_TOP");
                listPosition = POS_TOP;
//                dragState = WAIT_TOP_DRAG;
            }else if(layoutManager.findLastCompletelyVisibleItemPosition()==totalItemCount-1 && dy>0){
                Log.i(TAG,"POS_BOTTOM");
                listPosition = POS_BOTTOM;
//                dragState = WAIT_BOTTOM_DRAG;
            }else{
//                Log.d(TAG,"WAIT_NOTHING");
                listPosition = POS_OTHER;
//                dragState = WAIT_NOTHING;
            }
//            Log.d(TAG,"@@ ----------");
        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
    };

    float y_first   = -1 ;
    float range     = 100  ;
    private RecyclerView.OnTouchListener mOnTouchListener = new RecyclerView.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    Log.d("move","ACTION_DOWN");
                    break;

                case MotionEvent.ACTION_UP:
                    Log.d("move","ACTION_UP");
                    y_first=-1;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if(y_first!=-1){

                        if(listPosition == POS_TOP && (event.getY()-y_first)>range){
//                            dragState = ON_TOP_DRAG;
                            Log.i(TAG,"gesture scroll down:"+ (event.getY()-y_first) );
//                            binding.recyclerView.setParentScroll(true);
                        }

                        if(listPosition == POS_BOTTOM && (y_first-event.getY())>range){
//                            dragState = ON_BOTTOM_DRAG;
                            Log.d(TAG,"gesture scroll up:"+ (y_first-event.getY()) );
//                            binding.recyclerView.setParentScroll(true);
                        }
                    }else{
                        y_first = event.getY();
                    }
                    break;
            }
            return false;
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
