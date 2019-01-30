package com.wuc.yuku.module.recommend;

import com.wuc.yuku.module.BaseModel;

import java.util.ArrayList;

/**
 * @author: wuchao
 * @date: 2018/12/6 15:36
 * @desciption: 产品实体
 */
public class RecommendModel extends BaseModel {

    public ArrayList<RecommendBodyValue> list;
    public RecommendHeadValue head;
}
