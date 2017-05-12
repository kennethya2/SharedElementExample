package com.leafplain.demo.sharedelementexample.datamodel.info;

import java.io.Serializable;

/**
 * Created by kennethyeh on 2017/5/4.
 */

public class ListItemInfo implements Serializable{

    public interface ListType{
        int TITLE       = 1;
        int PHOTO       = 2;
        int PHOTO_PIC   = 3;
    }

    public int type;
    public Object data;

}
