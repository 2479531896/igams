package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SampleResultModel")
public class SampleResultModel {
 private String X;
 private String Y;
 private String State;
 private String Type;
 private LibraryStateSampleModel LibrarySampleModel;
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
public String getState() {
	return State;
}
public void setState(String state) {
	State = state;
}
public String getType() {
	return Type;
}
public void setType(String type) {
	Type = type;
}
public LibraryStateSampleModel getLibrarySampleModel() {
	return LibrarySampleModel;
}
public void setLibrarySampleModel(LibraryStateSampleModel librarySampleModel) {
	LibrarySampleModel = librarySampleModel;
}

}
