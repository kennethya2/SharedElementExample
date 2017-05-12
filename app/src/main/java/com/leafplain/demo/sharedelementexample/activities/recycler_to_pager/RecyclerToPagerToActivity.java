package com.leafplain.demo.sharedelementexample.activities.recycler_to_pager;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.leafplain.demo.sharedelementexample.R;
import com.leafplain.demo.sharedelementexample.databinding.ActivityRecyclerToPagerToBinding;
import com.leafplain.demo.sharedelementexample.datamodel.info.ListItemInfo;

import java.util.List;

import static com.leafplain.demo.sharedelementexample.activities.recycler_to_pager.PagerFragment.PARAM_LIST;
import static com.leafplain.demo.sharedelementexample.activities.recycler_to_pager.PagerFragment.PARAM_POS;
import static com.leafplain.demo.sharedelementexample.activities.recycler_to_pager.PagerFragment.PARAM_TRANSITION_NAME;

public class RecyclerToPagerToActivity extends AppCompatActivity {

    private String TAG = "PagerToActivity";



    private ActivityRecyclerToPagerToBinding binding;

    private int pos;
    private List<ListItemInfo> list;
    private String transitionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recycler_to_pager_to);
        supportPostponeEnterTransition();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_to_pager_to);

        Bundle extras = getIntent().getExtras();
        pos             = extras.getInt(PARAM_POS,0);
        list            = (List<ListItemInfo>) extras.getSerializable(PARAM_LIST);
        transitionName  = extras.getString(PARAM_TRANSITION_NAME);
        Log.d(TAG,"pos:"+ pos);
        Log.d(TAG,"transitionName:"+ transitionName);
        showPager();
    }

    private void showPager(){

        Fragment frag = PagerFragment.newInstance(pos, list, transitionName);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.addSharedElement(view, transitionName);
        transaction.replace(R.id.pagerContainer, frag);
        transaction.commit();
    }
}
