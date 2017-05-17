package com.leafplain.demo.sharedelementexample.activities.ex3_to_pager_activity;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leafplain.demo.sharedelementexample.MyApplication;
import com.leafplain.demo.sharedelementexample.R;
import com.leafplain.demo.sharedelementexample.databinding.FragmentItemBinding;
import com.leafplain.demo.sharedelementexample.datamodel.info.ListItemInfo;
import com.leafplain.demo.sharedelementexample.util.helper.TransitionNameHelper;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemFragment extends Fragment {
    private String TAG = "ItemFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PARAM_INFO = "param_info";
    private static final String PARAM_TRANSITION_NAME = "param_transitin_name";

    // TODO: Rename and change types of parameters
    private ListItemInfo item;
    private String transitionName;
    private String itemTransitionName;

    private FragmentItemBinding binding;

    public ItemFragment() {
        // Required empty public constructor
    }

    private AppCompatActivity context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (AppCompatActivity) context;
    }

    public static ItemFragment newInstance(ListItemInfo item, String transitionName) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM_INFO, item);
        args.putString(PARAM_TRANSITION_NAME, transitionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item            = (ListItemInfo) getArguments().getSerializable(PARAM_INFO);
            transitionName  = getArguments().getString(PARAM_TRANSITION_NAME);
            itemTransitionName = TransitionNameHelper.urlTrans((String) item.data);
        }
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View nowLayout = null;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item, container, false);
        nowLayout = binding.getRoot();
        return nowLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String photoURL = (String) item.data;
        binding.descTV.setText(photoURL);
        Log.d(TAG,"transName:"+transitionName);
        Log.d(TAG,"itemTransName:"+itemTransitionName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.photoPicIV.setTransitionName(itemTransitionName);
        }

        MyApplication.imageLoader.displayImage(photoURL,
                binding.photoPicIV,
                MyApplication.options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        context.supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        context.supportStartPostponedEnterTransition();
                    }
                });
    }

}
