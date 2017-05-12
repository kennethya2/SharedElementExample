package com.leafplain.demo.sharedelementexample.base.basecontract;

/**
 * Created by kennethyeh on 2017/5/2.
 */

public interface LoadingContract {

    interface View<T1, T2>{
        void onProgress();
        void onCancel();
        void onFinished(T1 result);
        void onFail(T2 fail);
    }

    interface Presenter{
        void start();
        void cancel();
    }
}
