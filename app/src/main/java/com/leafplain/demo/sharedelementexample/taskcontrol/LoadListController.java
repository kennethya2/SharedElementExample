package com.leafplain.demo.sharedelementexample.taskcontrol;

import android.os.Handler;

import com.leafplain.demo.sharedelementexample.Demo;
import com.leafplain.demo.sharedelementexample.base.basecontrol.ParsingControllable;
import com.leafplain.demo.sharedelementexample.datamodel.info.ListItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kennethyeh on 2017/5/4.
 */

public class LoadListController implements ParsingControllable<LoadListController.ResponseListener> {

    private static final int TOTAL_SIZE = 25;
    private List<ListItemInfo> mList = new ArrayList<>();
    private ResponseListener mListener=null;

    public interface ResponseListener{
        void onResultList(List<ListItemInfo> listResult);
    }

    @Override
    public void setResponseListener(ResponseListener listener) {
        mListener = listener;
    }

    @Override
    public void startParsing() {
        mHandler.postDelayed(mRunnable, 200);
    }
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            for(int i =0 ; i<TOTAL_SIZE ; i++){
                String photoURL = Demo.Demo_Pic[i%Demo.Demo_Pic.length];
                ListItemInfo mListItemInfo = new ListItemInfo();
                mListItemInfo.type=ListItemInfo.ListType.PHOTO_PIC;
                mListItemInfo.data=photoURL;
                mList.add(mListItemInfo);
            }
            if(mListener!=null){
                mListener.onResultList(mList);
            }
        }
    };

    @Override
    public void cancel() {

    }

}
