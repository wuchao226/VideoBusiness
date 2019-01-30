package com.wuc.yuku.module.recommend;

import com.wuc.yuku.module.BaseModel;

import java.util.ArrayList;

/**
 * @author: wuchao
 * @date: 2018/12/6 15:37
 * @desciption:
 */
public class RecommendHeadValue extends BaseModel {
    public ArrayList<String> ads;
    public ArrayList<String> middle;
    public ArrayList<RecommendFooterValue> footer;
}
