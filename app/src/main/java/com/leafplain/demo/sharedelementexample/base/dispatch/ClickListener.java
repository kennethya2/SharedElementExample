package com.leafplain.demo.sharedelementexample.base.dispatch;

/**
 * Created by kennethyeh on 2017/5/11.
 */

public interface ClickListener {

    interface ClickItemListener<T>{
        void onItemClick(T info);
    }

    interface ClickWithViewListener<T1, T2>{
        void onItemClick(T1 info, T2 view);
    }

    interface ClickListItemListener<T1, T2, T3>{
        void onItemClick(T1 pos, T2 info, T3 view);
    }
}
