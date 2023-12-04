package com.bin.david.smarttable.bean;


import com.bin.david.form.annotation.SmartColumn;

import java.util.List;

/**
 * Created by huang on 2017/11/1.
 */

public class ChildListData {

    @SmartColumn(id =55,name = "子类",autoCount = true)
    private List child;

    public ChildListData() {
    }
    public ChildListData(List child) {
        this.child = child;
    }

    public List getChildList() {
        return child;
    }

    public void setChildList(List child) {
        this.child = child;
    }
}
