package com.leafplain.demo.sharedelementexample.activities.ex2_to_swipe_activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

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

    private String TAG = "RecyclerActivity";


    public static final String EXTRA_ITEM = "item";
    public static final String EXTRA_IMAGE_TRANSITION_NAME = "image_transition_name";
    public static final String EXTRA_POSITION = "position";

    private ActivityRecyclerBinding binding;
    private RecyclerSamplePresenter mRecyclerSamplePresenter;
    private RecyclerBindingViewModel mViewModel;

    private SwipeFromActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recycler);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler);
        context = this;
        mViewModel = new RecyclerBindingViewModel();
        binding.setRecyclerDemoViewModel(mViewModel);
//
//
        mRecyclerSamplePresenter = new RecyclerSamplePresenter(SwipeFromActivity.this);
        binding.setPresenter(mRecyclerSamplePresenter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.recyclerView.setLayoutManager(gridLayoutManager);
//        mRecyclerBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        private ClickListener.ClickListItemListener clickItemListener
                = new ClickListener.ClickListItemListener<Integer, ListItemInfo, ImageView>(){
            @Override
            public void onItemClick(Integer pos, ListItemInfo info, ImageView view) {
                Log.d(TAG,"view==null:"+ (view==null));
                Log.d(TAG,"url:"+ info.data);
                String transitionName = ViewCompat.getTransitionName(view);
                Log.d(TAG,"getTransitionName:"+ transitionName);
                ViewPosition position = ViewPosition.from(view);
                Log.d(TAG,"positionStr:"+ position.pack());

                Intent intent = new Intent(context, SwipeToActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable(EXTRA_ITEM, info);
                extras.putString(EXTRA_IMAGE_TRANSITION_NAME, transitionName);
                extras.putString(EXTRA_POSITION, position.pack());
                intent.putExtras(extras);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context,
                        view,
                        transitionName);
                startActivity(intent, options.toBundle());
            }
        };

    }
}