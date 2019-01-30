package com.wuc.sdk.module;
import com.wuc.sdk.module.monitor.Monitor;
import com.wuc.sdk.module.monitor.emevent.EMEvent;

import java.util.ArrayList;
/**
 * @author: wuchao
 * @date: 2018/12/21 13:53
 * @desciption: 广告json value节点， 节点名字记得修改一下
 */
public class AdValue {

    public String resourceID;
    public String adid;
    public String resource;
    public String thumb;
    public ArrayList<Monitor> startMonitor;
    public ArrayList<Monitor> middleMonitor;
    public ArrayList<Monitor> endMonitor;
    public String clickUrl;
    public ArrayList<Monitor> clickMonitor;
    public EMEvent event;
    public String type;
}
