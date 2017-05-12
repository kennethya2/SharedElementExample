package com.leafplain.demo.sharedelementexample.activities.swipe_to_activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.alexvasilkov.events.Events;
import com.alexvasilkov.gestures.animation.ViewPosition;
import com.leafplain.demo.sharedelementexample.R;
import com.leafplain.demo.sharedelementexample.adaptercontrol.FactoryListBinder;
import com.leafplain.demo.sharedelementexample.adaptercontrol.FactoryListHolder;
import com.leafplain.demo.sharedelementexample.base.basecontract.LoadingContract;
import com.leafplain.demo.sharedelementexample.base.dispatch.ClickListener;
import com.leafplain.demo.sharedelementexample.databinding.ActivityRecyclerBinding;
import com.leafplain.demo.sharedelementexample.datamodel.info.ListItemInfo;
import com.leafplain.demo.sharedelementexample.datamodel.view.RecyclerBindingViewModel;
import com.leafplain.demo.sharedelementexample.presenter.recyclersmaple.RecyclerSamplePresenter;

import java.util.List;

public class SwipeFromActivity extends AppCompatActivity
        implements LoadingContract.View<List<ListItemInfo>, String>{

    private String TAG = "SwipeFromActivity";

    public static final String EVENT_SHOW_IMAGE = "show_image";
    public static final String EVENT_POSITION_CHANGED = "position_changed";

    private ActivityRecyclerBinding binding;
    private RecyclerSamplePresenter mRecyclerSamplePresenter;
    private RecyclerBindingViewModel mViewModel;

    private SwipeFromActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler);
        context = this;
        mViewModel = new RecyclerBindingViewModel();
        binding.setRecyclerDemoViewModel(mViewModel);
        mRecyclerSamplePresenter = new RecyclerSamplePresenter(SwipeFromActivity.this);
        binding.setPresenter(mRecyclerSamplePresenter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.recyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerSamplePresenter.start();

    }

    @Override
    public void onProgress() {
        binding.getRecyclerDemoViewModel().isLoading.set(true);
    }

    @Override
    public void onCancel() {
        binding.getRecyclerDemoViewModel().isLoading.set(false);
    }

    @Override
    public void onFinished(List<ListItemInfo> result) {
        binding.getRecyclerDemoViewModel().isLoading.set(false);
        binding.recyclerView.setAdapter(new RecyclerViewAdapter(result));
    }

    @Override
    public void onFail(String fail) {
        binding.getRecyclerDemoViewModel().isLoading.set(false);
    }


    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<ListItemInfo> result;
        private FactoryListHolder listHolderFactory = new FactoryListHolder();
        private FactoryListBinder listBinderFactory = new FactoryListBinder();

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
            listBinderFactory.setItemClickListener(clickItemListener);
            listBinderFactory.bindHolder(position, viewHolder, result.get(position));
        }
    }

    private ClickListener.ClickListItemListener clickItemListener
            = new ClickListener.ClickListItemListener<Integer, ListItemInfo, ImageView>(){
        @Override
        public void onItemClick(Integer pos, ListItemInfo info, ImageView view) {
            observer(view);
            // Requesting opening image in new activity and animating it from current position.
            ViewPosition position = ViewPosition.from(view);
            SwipeToActivity.open(SwipeFromActivity.this, position, info);
        }
    };

    private void observer(final ImageView image){
        image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Notifying fullscreen image activity about image position changes.
                ViewPosition position = ViewPosition.from(image);
                Events.create(EVENT_POSITION_CHANGED).param(position).post();
            }
        });
    }

//    @Events.Subscribe(EVENT_SHOW_IMAGE)
//    private void showImage(boolean show) {
//        // Fullscreen activity requested to show or hide original image
//        image.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
//    }
}
