package com.leafplain.demo.sharedelementexample.activities.recycler_to_pager;


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

import com.leafplain.demo.sharedelementexample.R;
import com.leafplain.demo.sharedelementexample.adaptercontrol.FactoryListBinder;
import com.leafplain.demo.sharedelementexample.adaptercontrol.FactoryListHolder;
import com.leafplain.demo.sharedelementexample.base.basecontract.LoadingContract;
import com.leafplain.demo.sharedelementexample.base.dispatch.ClickListener;
import com.leafplain.demo.sharedelementexample.databinding.ActivityRecyclerToPagerFromBinding;
import com.leafplain.demo.sharedelementexample.datamodel.info.ListItemInfo;
import com.leafplain.demo.sharedelementexample.datamodel.view.RecyclerBindingViewModel;
import com.leafplain.demo.sharedelementexample.presenter.recyclersmaple.RecyclerSamplePresenter;

import java.io.Serializable;
import java.util.List;

import static com.leafplain.demo.sharedelementexample.activities.recycler_to_pager.PagerFragment.PARAM_LIST;
import static com.leafplain.demo.sharedelementexample.activities.recycler_to_pager.PagerFragment.PARAM_POS;
import static com.leafplain.demo.sharedelementexample.activities.recycler_to_pager.PagerFragment.PARAM_TRANSITION_NAME;

public class RecyclerToPagerFromActivity extends AppCompatActivity
        implements LoadingContract.View<List<ListItemInfo>, String>{

    private String TAG = "PagerFromActivity";

    private ActivityRecyclerToPagerFromBinding binding;
    private RecyclerSamplePresenter mRecyclerSamplePresenter;
    private RecyclerBindingViewModel mViewModel;

    private RecyclerToPagerFromActivity context;
    private List<ListItemInfo> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recycler);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_to_pager_from);
        context = this;
        mViewModel = new RecyclerBindingViewModel();
        binding.setRecyclerDemoViewModel(mViewModel);
//
//
        mRecyclerSamplePresenter = new RecyclerSamplePresenter(RecyclerToPagerFromActivity.this);
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

    }

    @Override
    public void onFinished(List<ListItemInfo> result) {
        resultList = result;
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
                Log.d(TAG,"pos:"+ pos);
                showPager(pos, view, transitionName);
            }
        };

    }
    private void showPager(int pos, ImageView view, String transitionName){

        Intent intent = new Intent(context, RecyclerToPagerToActivity.class);
        Bundle extras = new Bundle();
        extras.putInt(PARAM_POS, pos);
        extras.putSerializable(PARAM_LIST, (Serializable) resultList);
        extras.putString(PARAM_TRANSITION_NAME, transitionName);
        intent.putExtras(extras);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context,
                view,
                transitionName);

        startActivity(intent, options.toBundle());
    }
}
