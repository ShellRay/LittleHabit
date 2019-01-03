package com.example.frescogif.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/6.
 */

public class GiftDialogBean implements Serializable{

    public String path;
    public String giftName;
    public int giftNum;


    public GiftDialogBean(String path, int i ,String giftName) {
        this.path = path;
        this.giftNum = i;
        this.giftName = giftName;
    }
}
