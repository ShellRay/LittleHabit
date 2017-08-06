package com.example.frescogif.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/6.
 */

public class GiftDialogBean implements Serializable{

    public String path;
    public int giftNum;


    public GiftDialogBean(String path, int i) {
        this.path = path;
        this.giftNum = i;
    }
}
