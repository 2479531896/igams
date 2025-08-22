package com.matridx.las.frame.connect.dll.printDll.service.dao.entities;
import org.apache.ibatis.type.Alias;

@Alias(value="BasicElement")
public class BasicElement {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
