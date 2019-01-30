package com.wuc.yuku.module.recommend;

import com.wuc.sdk.module.monitor.Monitor;
import com.wuc.sdk.module.monitor.emevent.EMEvent;
import com.wuc.yuku.module.BaseModel;

import java.util.ArrayList;

/**
 * @author: wuchao
 * @date: 2018/12/6 15:37
 * @desciption: 搜索实体
 */
public class RecommendBodyValue extends BaseModel {

    public int type;
    public String logo;
    public String title;
    public String info;
    public String price;
    public String text;
    public String site;
    public String from;
    public String zan;
    public ArrayList<String> url;

    //视频专用
    public String thumb;
    public String resource;
    public String resourceID;
    public String adid;
    public ArrayList<Monitor> startMonitor;
    public ArrayList<Monitor> middleMonitor;
    public ArrayList<Monitor> endMonitor;
    public String clickUrl;
    public ArrayList<Monitor> clickMonitor;
    public EMEvent event;
}
