package com.matridx.las.frame.connect.dll.printDll.service.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="DataProject")
public class DataProject {
    private String name;
    private BasicElement DataNumber;
    private BasicElement DataProjectName;
    private BasicElement CodePrintColumn;
    private BasicElement LabelWidthEdit;
    private BasicElement LabelHeightEdit;
    //标签X轴的坐标偏移值（针对第二和第三个条码）
    private List<LabelWidthOffset> LabelWidthOffsets;
    private List<LabelContentParam> LabelContentParams;
    private List<QrContentParam2D> QrContentParam2Ds;
    private List<QrContentParamBar> QrContentParamBars;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BasicElement getDataNumber() {
        return DataNumber;
    }

    public void setDataNumber(BasicElement dataNumber) {
        DataNumber = dataNumber;
    }

    public BasicElement getDataProjectName() {
        return DataProjectName;
    }

    public void setDataProjectName(BasicElement dataProjectName) {
        DataProjectName = dataProjectName;
    }

    public BasicElement getCodePrintColumn() {
        return CodePrintColumn;
    }

    public void setCodePrintColumn(BasicElement codePrintColumn) {
        CodePrintColumn = codePrintColumn;
    }

    public BasicElement getLabelWidthEdit() {
        return LabelWidthEdit;
    }

    public void setLabelWidthEdit(BasicElement labelWidthEdit) {
        LabelWidthEdit = labelWidthEdit;
    }

    public BasicElement getLabelHeightEdit() {
        return LabelHeightEdit;
    }

    public void setLabelHeightEdit(BasicElement labelHeightEdit) {
        LabelHeightEdit = labelHeightEdit;
    }

    public List<LabelWidthOffset> getLabelWidthOffsets() {
        return LabelWidthOffsets;
    }

    public void setLabelWidthOffsets(List<LabelWidthOffset> labelWidthOffsets) {
        LabelWidthOffsets = labelWidthOffsets;
    }

    public List<LabelContentParam> getLabelContentParams() {
        return LabelContentParams;
    }

    public void setLabelContentParams(List<LabelContentParam> labelContentParams) {
        LabelContentParams = labelContentParams;
    }

    public List<QrContentParam2D> getQrContentParam2Ds() {
        return QrContentParam2Ds;
    }

    public void setQrContentParam2Ds(List<QrContentParam2D> qrContentParam2Ds) {
        QrContentParam2Ds = qrContentParam2Ds;
    }

    public List<QrContentParamBar> getQrContentParamBars() {
        return QrContentParamBars;
    }

    public void setQrContentParamBars(List<QrContentParamBar> qrContentParamBars) {
        QrContentParamBars = qrContentParamBars;
    }
}
