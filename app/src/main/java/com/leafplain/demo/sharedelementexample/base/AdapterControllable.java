package com.leafplain.demo.sharedelementexample.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by kennethyeh on 2017/5/11.
 */

public interface AdapterControllable {

    interface HolderFactory{
        RecyclerView.ViewHolder getHolder(ViewGroup parent, int viewType);
    }

    interface BinderFactory<T>{
        void bindHolder(int pos, RecyclerView.ViewHolder viewHolder, T item);
    }
}
