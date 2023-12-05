package com.bin.david.smarttable.bean;

import java.util.List;

/**
 * Created by huang on 2017/11/1.
 */
public class UserInfoDemo {
    private String name;
    private int age;
    private long time;
    private ChildListData childListData;
    private boolean isCheck;
    private String url;
    private List operationList;


    public UserInfoDemo(String name, int age, long time, boolean isCheck, List operationList,ChildListData childListData) {
        this.name = name;
        this.age = age;
        this.time = time;
        this.childListData = childListData;
        this.isCheck = isCheck;
        this.operationList = operationList;
    }


    public List getOperationList() {
        return operationList;
    }

    public void setOperationList(List operationList) {
        this.operationList = operationList;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ChildListData getChildListData() {
        return childListData;
    }

    public void setChildListData(ChildListData childListData) {
        this.childListData = childListData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
