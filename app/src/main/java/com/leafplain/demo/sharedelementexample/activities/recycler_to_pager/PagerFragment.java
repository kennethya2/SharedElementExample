package com.leafplain.demo.sharedelementexample.activities.recycler_to_pager;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leafplain.demo.sharedelementexample.R;
import com.leafplain.demo.sharedelementexample.databinding.FragmentPagerBinding;
import com.leafplain.demo.sharedelementexample.datamodel.info.ListItemInfo;

import java.io.Serializable;
import java.util.List;

public class PagerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String PARAM_POS  = "param_pos";
    public static final String PARAM_LIST = "param_list";
    public static final String PARAM_TRANSITION_NAME = "param_transition_name";

    // TODO: Rename and change types of parameters
    private int pos;
    private List<ListItemInfo> list;
    private String transitionName;

    private FragmentPagerBinding binding;
    private View currentLayout = null;

    public PagerFragment() {
        // Required empty public constructor
    }

    public static PagerFragment newInstance(int pos, List<ListItemInfo> list, String transitionName) {
        PagerFragment fragment = new PagerFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_POS, pos);
        args.putSerializable(PARAM_LIST, (Serializable) list);
        args.putString(PARAM_TRANSITION_NAME, transitionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pos     = getArguments().getInt(PARAM_POS, 0);
            list    = (List<ListItemInfo>) getArguments().getSerializable(PARAM_LIST);
            transitionName = getArguments().getString(PARAM_TRANSITION_NAME);
        }
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View nowLayout = null;
        if(currentLayout == null){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pager, container, false);
            nowLayout = binding.getRoot();
            currentLayout = nowLayout;
        }else{
            ViewGroup parent = (ViewGroup) currentLayout.getParent();
            if (parent != null){
                parent.removeView(currentLayout);
            }
            nowLayout = currentLayout;
        }
        return nowLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), list);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setCurrentItem(pos);

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        private List<ListItemInfo> list;

        PagerAdapter(FragmentManager fm, List<ListItemInfo> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            ListItemInfo item = list.get(position);
            return ItemFragment.newInstance(item, transitionName);
        }

        @Override
        public int getCount() {
            return list.size();
        }

    }
}
