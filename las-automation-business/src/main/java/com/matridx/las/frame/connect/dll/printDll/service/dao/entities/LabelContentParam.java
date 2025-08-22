package com.matridx.las.frame.connect.dll.printDll.service.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="LabelContentParam")
public class LabelContentParam{
    private String name;
    private BasicElement Number;
    //:字体
    private BasicElement Type;
    //名称
    private BasicElement Name;
    //字体X轴
    private BasicElement FontXPosEdit;
    //字体Y轴
    private BasicElement FontYPosEdit;
    //字体的偏移量
    private BasicElement FontOffset;
    //字体类型
    private BasicElement FontTypeCombox;
    //字体宽度
    private BasicElement FontWidth;
    //字体高度
    private BasicElement FontHeight;
    //字体旋转方向
    private BasicElement FontComboBoxRotate;

    public String getName() {
        return name;
    }

    public void setName(BasicElement name) {
        Name = name;
    }

    public BasicElement getFontXPosEdit() {
        return FontXPosEdit;
    }

    public void setFontXPosEdit(BasicElement fontXPosEdit) {
        FontXPosEdit = fontXPosEdit;
    }

    public BasicElement getFontYPosEdit() {
        return FontYPosEdit;
    }

    public void setFontYPosEdit(BasicElement fontYPosEdit) {
        FontYPosEdit = fontYPosEdit;
    }

    public BasicElement getFontOffset() {
        return FontOffset;
    }

    public void setFontOffset(BasicElement fontOffset) {
        FontOffset = fontOffset;
    }

    public BasicElement getFontTypeCombox() {
        return FontTypeCombox;
    }

    public void setFontTypeCombox(BasicElement fontTypeCombox) {
        FontTypeCombox = fontTypeCombox;
    }

    public BasicElement getFontWidth() {
        return FontWidth;
    }

    public void setFontWidth(BasicElement fontWidth) {
        FontWidth = fontWidth;
    }

    public BasicElement getFontHeight() {
        return FontHeight;
    }

    public void setFontHeight(BasicElement fontHeight) {
        FontHeight = fontHeight;
    }

    public BasicElement getFontComboBoxRotate() {
        return FontComboBoxRotate;
    }

    public void setFontComboBoxRotate(BasicElement fontComboBoxRotate) {
        FontComboBoxRotate = fontComboBoxRotate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BasicElement getNumber() {
        return Number;
    }

    public void setNumber(BasicElement number) {
        Number = number;
    }

    public BasicElement getType() {
        return Type;
    }

    public void setType(BasicElement type) {
        Type = type;
    }
}