package com.leafplain.demo.sharedelementexample.presenter.recyclersmaple;

import android.util.Log;

import com.leafplain.demo.sharedelementexample.base.basecontract.LoadingContract;
import com.leafplain.demo.sharedelementexample.datamodel.info.ListItemInfo;
import com.leafplain.demo.sharedelementexample.taskcontrol.LoadListController;

import java.util.List;


/**
 * Created by kennethyeh on 2017/5/2.
 */

public class RecyclerSamplePresenter implements LoadingContract.Presenter{

    private String TAG = "RecyclerSamplePresenter";

    private LoadingContract.View view;
//    private ActivityRecyclerBinding binding;
    private LoadListController mParsingListTask;

    public RecyclerSamplePresenter(LoadingContract.View view){
        this.view       = view;
    }

    @Override
    public void start() {
        view.onProgress();

        mParsingListTask = new LoadListController();
        mParsingListTask.setResponseListener(listener);
        mParsingListTask.startParsing();
    }

    private LoadListController.ResponseListener listener = new LoadListController.ResponseListener(){

        @Override
        public void onResultList(List<ListItemInfo> listResult) {
            for(ListItemInfo item: listResult){
                Log.d(TAG,"type:"+item.type);
                Log.d(TAG,"data:"+item.data);
            }
            Log.d(TAG,"------");
            view.onFinished(listResult);
        }
    };



    @Override
    public void cancel() {
        view.onCancel();
    }
}
