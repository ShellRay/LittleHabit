package com.example.frescogif.bean;

import java.io.Serializable;

/**
 * Created by GG on 2018/3/7.
 */

public class TagBean implements Serializable {
    private int tag_num;
    private int tagid;
    private String tag_name;
    private String tag_rgb;

    public int getTagid() {
        return tagid;
    }

    public void setTagid(int tagid) {
        this.tagid = tagid;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getTag_rgb() {
        return tag_rgb;
    }

    public void setTag_rgb(String tag_rgb) {
        this.tag_rgb = tag_rgb;
    }

    public int getTag_num() {
        return tag_num;
    }

    public void setTag_num(int tag_num) {
        this.tag_num = tag_num;
    }
}
