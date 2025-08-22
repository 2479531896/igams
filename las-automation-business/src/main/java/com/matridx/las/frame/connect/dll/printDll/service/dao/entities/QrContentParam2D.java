package com.matridx.las.frame.connect.dll.printDll.service.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="QrContentParam2D")
public class QrContentParam2D {
    private String name;
    //编码
    private BasicElement Number;
    //:二维码
    private BasicElement Type;
    //名称
    private BasicElement Name;
    //二维码X轴
    private BasicElement QrCodeXPosEdit;
    //二维码Y轴
    private BasicElement QrCodeYPosEdit;
    //二维码方向
    private BasicElement QrComboxRotate;
    //二维码最大宽度
    private BasicElement QrCodeMaxWidthEdit;
    //二维码最大高度
    private BasicElement QrCodeMaxHeightEdit;
    //二维码放大倍数
    private BasicElement QrCodeEnlargeEdit;

    public String getName() {
        return name;
    }

    public void setName(BasicElement name) {
        Name = name;
    }

    public BasicElement getQrCodeXPosEdit() {
        return QrCodeXPosEdit;
    }

    public void setQrCodeXPosEdit(BasicElement qrCodeXPosEdit) {
        QrCodeXPosEdit = qrCodeXPosEdit;
    }

    public BasicElement getQrCodeYPosEdit() {
        return QrCodeYPosEdit;
    }

    public void setQrCodeYPosEdit(BasicElement qrCodeYPosEdit) {
        QrCodeYPosEdit = qrCodeYPosEdit;
    }

    public BasicElement getQrComboxRotate() {
        return QrComboxRotate;
    }

    public void setQrComboxRotate(BasicElement qrComboxRotate) {
        QrComboxRotate = qrComboxRotate;
    }

    public BasicElement getQrCodeMaxWidthEdit() {
        return QrCodeMaxWidthEdit;
    }

    public void setQrCodeMaxWidthEdit(BasicElement qrCodeMaxWidthEdit) {
        QrCodeMaxWidthEdit = qrCodeMaxWidthEdit;
    }

    public BasicElement getQrCodeMaxHeightEdit() {
        return QrCodeMaxHeightEdit;
    }

    public void setQrCodeMaxHeightEdit(BasicElement qrCodeMaxHeightEdit) {
        QrCodeMaxHeightEdit = qrCodeMaxHeightEdit;
    }

    public BasicElement getQrCodeEnlargeEdit() {
        return QrCodeEnlargeEdit;
    }

    public void setQrCodeEnlargeEdit(BasicElement qrCodeEnlargeEdit) {
        QrCodeEnlargeEdit = qrCodeEnlargeEdit;
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
