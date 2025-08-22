package com.matridx.las.frame.connect.dll.printDll.service.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="LabelWidthOffset")
public class LabelWidthOffset{
    private String name;
    private BasicElement WidthOffset;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BasicElement getWidthOffset() {
        return WidthOffset;
    }

    public void setWidthOffset(BasicElement widthOffset) {
        WidthOffset = widthOffset;
    }
}