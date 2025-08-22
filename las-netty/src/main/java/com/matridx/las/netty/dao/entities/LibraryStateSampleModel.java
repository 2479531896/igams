package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value = "LibraryStateSampleModel")
public class LibraryStateSampleModel {

	private String isHave;
	private String SaveArea;
	private String SampleName;
	private String StockSolution;
	private String Diluent;
	private String IsFith;
	private String ID;
	private String X;
	private String Y;
	public String getIsHave() {
		return isHave;
	}
	public void setIsHave(String isHave) {
		this.isHave = isHave;
	}
	public String getSaveArea() {
		return SaveArea;
	}
	public void setSaveArea(String saveArea) {
		SaveArea = saveArea;
	}
	public String getSampleName() {
		return SampleName;
	}
	public void setSampleName(String sampleName) {
		SampleName = sampleName;
	}
	public String getStockSolution() {
		return StockSolution;
	}
	public void setStockSolution(String stockSolution) {
		StockSolution = stockSolution;
	}
	public String getDiluent() {
		return Diluent;
	}
	public void setDiluent(String diluent) {
		Diluent = diluent;
	}
	public String getIsFith() {
		return IsFith;
	}
	public void setIsFith(String isFith) {
		IsFith = isFith;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getX() {
		return X;
	}
	public void setX(String x) {
		X = x;
	}
	public String getY() {
		return Y;
	}
	public void setY(String y) {
		Y = y;
	}

}
