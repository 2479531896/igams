package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WksyypModel")
public class WksyypModel extends BaseModel{
	//原横坐标
	private String x;
	//原纵坐标
	private String y;
	//状态
	private String state;
	//类型
	private String type;
	//null
	private String ishave;
	//null
	private String savearea;
	//null
	private String stocksolution;
	//稀释液
	private String diluent;
	//是否完成
	private String isfith;
	//id
	private String id;
	//新横坐标
	private String xx;
	//新纵坐标
	private String yx;
	//试验样品id
	private String sjypid;
	//文库实验id
	private String wkid;
	private String nbbh;
	private String wkyynd;
	private String quantity;
	private String wjyytj;
	private String wjxsyjytj;
	//管道id
	private String gdid;
	//托盘编号
	private String tpbh;
	//卡盒编号
	private String khbh;
	
	
	
	public String getTpbh() {
		return tpbh;
	}
	public void setTpbh(String tpbh) {
		this.tpbh = tpbh;
	}
	public String getKhbh() {
		return khbh;
	}
	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}
	public String getGdid() {
		return gdid;
	}
	public void setGdid(String gdid) {
		this.gdid = gdid;
	}
	//原横坐标
	public String getX() {
		return x;
	}
	public void setX(String x){
		this.x = x;
	}
	//原纵坐标
	public String getY() {
		return y;
	}
	public void setY(String y){
		this.y = y;
	}
	//状态
	public String getState() {
		return state;
	}
	public void setState(String state){
		this.state = state;
	}
	//类型
	public String getType() {
		return type;
	}
	public void setType(String type){
		this.type = type;
	}
	//null
	public String getIshave() {
		return ishave;
	}
	public void setIshave(String ishave){
		this.ishave = ishave;
	}
	//null
	public String getSavearea() {
		return savearea;
	}
	public void setSavearea(String savearea){
		this.savearea = savearea;
	}
	//null
	public String getStocksolution() {
		return stocksolution;
	}
	public void setStocksolution(String stocksolution){
		this.stocksolution = stocksolution;
	}
	//稀释液
	public String getDiluent() {
		return diluent;
	}
	public void setDiluent(String diluent){
		this.diluent = diluent;
	}
	//是否完成
	public String getIsfith() {
		return isfith;
	}
	public void setIsfith(String isfith){
		this.isfith = isfith;
	}
	//id
	public String getId() {
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	//新横坐标
	public String getXx() {
		return xx;
	}
	public void setXx(String xx){
		this.xx = xx;
	}
	//新纵坐标
	public String getYx() {
		return yx;
	}
	public void setYx(String yx){
		this.yx = yx;
	}
	//试验样品id
	public String getSjypid() {
		return sjypid;
	}
	public void setSjypid(String sjypid){
		this.sjypid = sjypid;
	}
	//文库实验id
	public String getWkid() {
		return wkid;
	}
	public void setWkid(String wkid) {
		this.wkid = wkid;
	}

	public String getNbbh() {
		return nbbh;
	}
	public void setNbbh(String nbbh) {
		this.nbbh = nbbh;
	}
	public String getWkyynd() {
		return wkyynd;
	}
	public void setWkyynd(String wkyynd) {
		this.wkyynd = wkyynd;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getWjyytj() {
		return wjyytj;
	}
	public void setWjyytj(String wjyytj) {
		this.wjyytj = wjyytj;
	}
	public String getWjxsyjytj() {
		return wjxsyjytj;
	}
	public void setWjxsyjytj(String wjxsyjytj) {
		this.wjxsyjytj = wjxsyjytj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
}
