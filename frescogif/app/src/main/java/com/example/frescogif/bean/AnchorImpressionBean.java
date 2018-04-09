package com.example.frescogif.bean;

/**
 * Created by GG on 2018/3/6.
 */

public class AnchorImpressionBean {
    private int tagId;
    private String tagName;
    private String tagRGB;
    private int tagSort;
    private int tagType;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagRGB() {
        return tagRGB;
    }

    public void setTagRGB(String tagRGB) {
        this.tagRGB = tagRGB;
    }

    public int getTagSort() {
        return tagSort;
    }

    public void setTagSort(int tagSort) {
        this.tagSort = tagSort;
    }

    public int getTagType() {
        return tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }
}
