package com.matridx.server.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.SjycfkModel;
import com.matridx.igams.common.dao.entities.User;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="SjycfkWechatDto")
public class SjycfkWechatDto extends SjycfkWechatModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//确认人
	private String qrr;
	//反馈人员
	private String fkry;
	//确认人名称
	private String qrrmc;
	//反馈人员名称
	private String fkrymc;
	//父反馈信息
	private String ffkxx;
	//评论数
	private String pls;
	//回复时间
	private String hfsj;
	//回复时间差
	private String hfsjc;
	//附件IDs
	private List<String> fjids;
	//业务类型
	private String ywlx;
	//是否上传附件
	private boolean sfscfj;
	//查看标记
	private String ckbj;
	//根反馈回复次数
	private String ghfcs;
	//反馈人员头像路径
	private String fkrytx;
	//异常评论附件
	private List<FjcfbDto> fjcfbDtos;
	//小程序存放图片路径
	private List<String> fjList;
	//是否允许撤回
	private String sfyxch;
	//用户ID
	private String yhid;
	//附件数
	private String fjs;
	//附件保存标记
	private String fjbcbj;
	private String ddid;
	private String fkryddid;
	private String tzrys;
	private String ybbh;
	private String sjid;
	private String sfjs;
	private String ycbt;
	//反馈人员用户名
	private String fkryyhm;
	private String fjcfparam;
	public String getFkryyhm() {
		return fkryyhm;
	}

	public void setFkryyhm(String fkryyhm) {
		this.fkryyhm = fkryyhm;
	}

	public String getYcbt() {
		return ycbt;
	}

	public void setYcbt(String ycbt) {
		this.ycbt = ycbt;
	}

	public String getSfjs() {
		return sfjs;
	}

	public void setSfjs(String sfjs) {
		this.sfjs = sfjs;
	}

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}
	
	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getTzrys() {
		return tzrys;
	}

	public void setTzrys(String tzrys) {
		this.tzrys = tzrys;
	}

	public String getFkryddid() {
		return fkryddid;
	}

	public void setFkryddid(String fkryddid) {
		this.fkryddid = fkryddid;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getFjbcbj() {
		return fjbcbj;
	}

	public void setFjbcbj(String fjbcbj) {
		this.fjbcbj = fjbcbj;
	}

	public String getFjs() {
		return fjs;
	}

	public void setFjs(String fjs) {
		this.fjs = fjs;
	}

	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid) {
		this.yhid = yhid;
	}
	public String getSfyxch() {
		return sfyxch;
	}
	public void setSfyxch(String sfyxch) {
		this.sfyxch = sfyxch;
	}
	public List<String> getFjList() {
		return fjList;
	}
	public void setFjList(List<String> fjList) {
		this.fjList = fjList;
	}
	public List<FjcfbDto> getFjcfbDtos() {
		return fjcfbDtos;
	}
	public void setFjcfbDtos(List<FjcfbDto> fjcfbDtos) {
		this.fjcfbDtos = fjcfbDtos;
	}
	public String getFkrytx() {
		return fkrytx;
	}
	public void setFkrytx(String fkrytx) {
		this.fkrytx = fkrytx;
	}
	public String getGhfcs() {
		return ghfcs;
	}
	public void setGhfcs(String ghfcs) {
		this.ghfcs = ghfcs;
	}
	public String getCkbj() {
		return ckbj;
	}
	public void setCkbj(String ckbj) {
		this.ckbj = ckbj;
	}
	public boolean isSfscfj() {
		return sfscfj;
	}
	public void setSfscfj(boolean sfscfj) {
		this.sfscfj = sfscfj;
	}
	public String getYwlx() {
		return ywlx;
	}
	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}
	public List<String> getFjids() {
		return fjids;
	}
	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}
	public String getHfsjc() {
		return hfsjc;
	}
	public void setHfsjc(String hfsjc) {
		this.hfsjc = hfsjc;
	}
	public String getHfsj() {
		return hfsj;
	}
	public void setHfsj(String hfsj) {
		this.hfsj = hfsj;
	}
	public String getPls() {
		return pls;
	}
	public void setPls(String pls) {
		this.pls = pls;
	}
	public String getQrr() {
		return qrr;
	}
	public void setQrr(String qrr) {
		this.qrr = qrr;
	}
	public String getFkry() {
		return fkry;
	}
	public void setFkry(String fkry) {
		this.fkry = fkry;
	}
	public String getFfkxx() {
		return ffkxx;
	}
	public void setFfkxx(String ffkxx) {
		this.ffkxx = ffkxx;
	}
	public String getFkrymc() {
		return fkrymc;
	}
	public void setFkrymc(String fkrymc) {
		this.fkrymc = fkrymc;
	}
	public String getQrrmc() {
		return qrrmc;
	}
	public void setQrrmc(String qrrmc) {
		this.qrrmc = qrrmc;
	}

	public String getFjcfparam() {
		return fjcfparam;
	}

	public void setFjcfparam(String fjcfparam) {
		this.fjcfparam = fjcfparam;
	}
}
