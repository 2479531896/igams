package com.matridx.las.frame.connect.dll.printDll.service.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="QrContentParamBar")
public class QrContentParamBar {

    private String name;
    //编码
    private BasicElement Number;
    //:条形码
    private BasicElement Type;
    //名称
    private BasicElement Name;
    //条形码X轴
    private BasicElement QrBarCodeXPosEdit;
    //条形码Y轴
    private BasicElement QrBarCodeYPosEdit;
    //条形码高度
    private BasicElement QrBarCodePVertical;
    //条形码方向
    private BasicElement QrBarComboxRotate;
    //条形码类型
    private BasicElement QrBarCodeType;
    //条形码中窄单元的宽度
    private BasicElement QrBarCodeNarrowWidth;
    //条形码中宽单元的宽度
    private BasicElement QrBarCodePHorizontal;
    //条码Ascll类型
    private BasicElement QrBarCodePtext;

    public String getName() {
        return name;
    }

    public void setName(BasicElement name) {
        Name = name;
    }

    public BasicElement getQrBarCodeXPosEdit() {
        return QrBarCodeXPosEdit;
    }

    public void setQrBarCodeXPosEdit(BasicElement qrBarCodeXPosEdit) {
        QrBarCodeXPosEdit = qrBarCodeXPosEdit;
    }

    public BasicElement getQrBarCodeYPosEdit() {
        return QrBarCodeYPosEdit;
    }

    public void setQrBarCodeYPosEdit(BasicElement qrBarCodeYPosEdit) {
        QrBarCodeYPosEdit = qrBarCodeYPosEdit;
    }

    public BasicElement getQrBarCodePVertical() {
        return QrBarCodePVertical;
    }

    public void setQrBarCodePVertical(BasicElement qrBarCodePVertical) {
        QrBarCodePVertical = qrBarCodePVertical;
    }

    public BasicElement getQrBarComboxRotate() {
        return QrBarComboxRotate;
    }

    public void setQrBarComboxRotate(BasicElement qrBarComboxRotate) {
        QrBarComboxRotate = qrBarComboxRotate;
    }

    public BasicElement getQrBarCodeType() {
        return QrBarCodeType;
    }

    public void setQrBarCodeType(BasicElement qrBarCodeType) {
        QrBarCodeType = qrBarCodeType;
    }

    public BasicElement getQrBarCodeNarrowWidth() {
        return QrBarCodeNarrowWidth;
    }

    public void setQrBarCodeNarrowWidth(BasicElement qrBarCodeNarrowWidth) {
        QrBarCodeNarrowWidth = qrBarCodeNarrowWidth;
    }

    public BasicElement getQrBarCodePHorizontal() {
        return QrBarCodePHorizontal;
    }

    public void setQrBarCodePHorizontal(BasicElement qrBarCodePHorizontal) {
        QrBarCodePHorizontal = qrBarCodePHorizontal;
    }

    public BasicElement getQrBarCodePtext() {
        return QrBarCodePtext;
    }

    public void setQrBarCodePtext(BasicElement qrBarCodePtext) {
        QrBarCodePtext = qrBarCodePtext;
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
