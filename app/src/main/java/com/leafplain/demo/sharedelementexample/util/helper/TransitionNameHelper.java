package com.leafplain.demo.sharedelementexample.util.helper;

/**
 * Created by kennethyeh on 2017/5/12.
 */

public class TransitionNameHelper {

    public static String urlTrans(String url){
        return  url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."));
    }
}
