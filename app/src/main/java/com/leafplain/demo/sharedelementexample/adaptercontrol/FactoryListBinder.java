package com.leafplain.demo.sharedelementexample.adaptercontrol;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.leafplain.demo.sharedelementexample.BR;
import com.leafplain.demo.sharedelementexample.MyApplication;
import com.leafplain.demo.sharedelementexample.base.AdapterControllable;
import com.leafplain.demo.sharedelementexample.base.dispatch.ClickListener;
import com.leafplain.demo.sharedelementexample.databinding.HolderPhotoPicBinding;
import com.leafplain.demo.sharedelementexample.databinding.HolderTitleBinding;
import com.leafplain.demo.sharedelementexample.datamodel.info.ListItemInfo;
import com.leafplain.demo.sharedelementexample.util.helper.TransitionNameHelper;

/**
 * Created by kennethyeh on 2017/5/5.
 */

public class FactoryListBinder implements AdapterControllable.BinderFactory<ListItemInfo>{


    private ClickListener.ClickListItemListener mItemClickListener = null;
    public void setItemClickListener(ClickListener.ClickListItemListener itemClickListener){
        mItemClickListener = itemClickListener;
    }

    @Override
    public void bindHolder(final int pos, RecyclerView.ViewHolder viewHolder, ListItemInfo item) {
        int type = item.type;
        if(type == ListItemInfo.ListType.TITLE){
            BaseCustomHolder holder = (BaseCustomHolder) viewHolder;
            HolderTitleBinding binding = (HolderTitleBinding)holder.getBinding();
            binding.setTitle((String) item.data);
//            binding.titleTV.setText((String)item.data);
        }

        if(type == ListItemInfo.ListType.PHOTO){
            BaseBindingHolder holder = (BaseBindingHolder) viewHolder;
            holder.getBinding().setVariable(BR.photoUrl, item.data);
            holder.getBinding().executePendingBindings();
        }

        if(type == ListItemInfo.ListType.PHOTO_PIC){
            BaseCustomHolder holder = (BaseCustomHolder) viewHolder;
            HolderPhotoPicBinding mHolderPhotoPicBinding = (HolderPhotoPicBinding) holder.getBinding();
            String url = (String) item.data;
            MyApplication.imageLoader.displayImage(url,
                    mHolderPhotoPicBinding.photoPicIV,
                    MyApplication.options,
                    MyApplication.animateFirstListener);

            String transitionName = TransitionNameHelper.urlTrans(url);
            ViewCompat.setTransitionName(mHolderPhotoPicBinding.photoPicIV, transitionName);
            ClickItem mClickItem = new ClickItem();
            mClickItem.view = mHolderPhotoPicBinding.photoPicIV;
            mClickItem.item = item;
            mHolderPhotoPicBinding.photoPicContainer.setTag(mClickItem);
            mHolderPhotoPicBinding.photoPicContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mItemClickListener!=null){
                        ClickItem mClickItem = (ClickItem) view.getTag();
                        mItemClickListener.onItemClick(pos, mClickItem.item, mClickItem.view);
                    }
                }
            });
        }
    }

    public class ClickItem{
        public ImageView view;
        public ListItemInfo item;
    }
}
