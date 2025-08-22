package com.matridx.igams.experiment.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.FjcfbDto;

@Alias(value="XmjdrwDto")
public class XmjdrwDto extends XmjdrwModel{
	//任务级别
	private String rwjbmc;
	//任务标签
	private String rwbqmc;
	//子任务
	public List<XmjdrwDto> xmjdzrwDto;
	//已完成子任务
	private String finishnum;
	//未完成子任务
	private String unfinishnum;
	//完成度
	private String rwwcd;
	//查看任务标记
	private String myflg;
	//负责人名称
	private String fzrmc;
	//创建人名称
	private String cjrmc;
	//开始时间
	private String starttime;
	//开始时间
	private String endtime;
	//背景颜色
	private String bgcolor;
	//完成标记
	private String wcbj;
	//当前进度 （用于修改gzgl表）
	private String dqjd;
	//做外层rwid 用于添加gzgl表rwid
	private String zwcrwid;
	//项目名称
	private String xmmc;
	//任务留言list
	private List<RwlyDto> rwlyDtos;
	//钉钉id
	private String ddid;
	//任务级别序号
	private String rwjbxh;
	//留言信息
	private String lyxx;
	//扩展参数1
	private String cskz1;
	//模板名称
	private String mbmc;
	//项目类别
	private String xmlb;
	//模板ID
	private String mbid;
	//项目公开性
	private String xmgkx;
	//项目分组
	private String xmfz;
	//阶段名称
	private String jdmc;
	//获取用户ID
	private String yhid;
	//获取用户名yhm
	private String yhm;
	//获取项目idList
	private List<String> xmids;
	//获取项目成员
	private String yhids;
	//用于获取项目研发列表中指定dto信息
	private int ordernum;
	//下一个阶段的JDID
	private String xygxmjdid;
	//下一个阶段的阶段名称
	private String xygxmjdmc;
	//项目研发列表判断子任务负责人不存在与项目成员时消息内容若：1为不存在
	private String mbzrwfzrxx;
	//项目成员集合
	private String xmcylist;
	//文件类型
	private String ywlx;
	//附件ID复数
	private List<String> fjids;
	//阶段id
	private String jdid;
	//阶段序号
	private String jdxh;
	//阶段项目阶段ID
	private String jdxmjdid;
	//附件
	private List<FjcfbDto> fjcfbDtos;
	//用于删除该任务以及该任务下的所有任务
	private List<String> rwids;
	//计划开始日期
	private String jhksrq;
	//计划结束日期
	private String jhjsrq;
	//实际开始日期
	private String sjksrq;
	//实际结束日期
	private String sjjsrq;
	//移动前阶段id
	private String prejdid;
	//项目简介
	private String xmjj;
	//移动后阶段(排序)
	private List<String> newids;
	//截止日期
	private String jzrq;
	//真实姓名
	private String zsxm;
	//分数比例
	private String fsbl;
	//阶段分数
	private String jdfs;
	//各阶段分数
	private List<RwrqDto> list;

	public List<RwrqDto> getList() {
		return list;
	}

	public void setList(List<RwrqDto> list) {
		this.list = list;
	}

	public String getJdfs() {
		return jdfs;
	}

	public void setJdfs(String jdfs) {
		this.jdfs = jdfs;
	}

	public String getFsbl() {
		return fsbl;
	}

	public void setFsbl(String fsbl) {
		this.fsbl = fsbl;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getJzrq() {
		return jzrq;
	}

	public void setJzrq(String jzrq) {
		this.jzrq = jzrq;
	}

	public String getCjrmc() {
		return cjrmc;
	}

	public void setCjrmc(String cjrmc) {
		this.cjrmc = cjrmc;
	}

	public List<String> getNewids() {
		return newids;
	}

	public void setNewids(List<String> newids) {
		this.newids = newids;
	}
	public String getXmjj() {
		return xmjj;
	}

	public void setXmjj(String xmjj) {
		this.xmjj = xmjj;
	}

	public String getJhksrq() {
		return jhksrq;
	}

	public void setJhksrq(String jhksrq) {
		this.jhksrq = jhksrq;
	}

	public String getJhjsrq() {
		return jhjsrq;
	}

	public void setJhjsrq(String jhjsrq) {
		this.jhjsrq = jhjsrq;
	}

	public String getSjksrq() {
		return sjksrq;
	}

	public void setSjksrq(String sjksrq) {
		this.sjksrq = sjksrq;
	}

	public String getSjjsrq() {
		return sjjsrq;
	}

	public void setSjjsrq(String sjjsrq) {
		this.sjjsrq = sjjsrq;
	}

	public List<String> getRwids() {
		return rwids;
	}

	public void setRwids(List<String> rwids) {
		this.rwids = rwids;
	}

	public String getJdid() {
		return jdid;
	}

	public void setJdid(String jdid) {
		this.jdid = jdid;
	}

	public String getXmcylist()
	{
		return xmcylist;
	}

	public void setXmcylist(String xmcylist)
	{
		this.xmcylist = xmcylist;
	}

	public String getMbzrwfzrxx() {
		return mbzrwfzrxx;
	}

	public void setMbzrwfzrxx(String mbzrwfzrxx) {
		this.mbzrwfzrxx = mbzrwfzrxx;
	}

	public String getXygxmjdmc() {
		return xygxmjdmc;
	}

	public void setXygxmjdmc(String xygxmjdmc) {
		this.xygxmjdmc = xygxmjdmc;
	}

	public String getXygxmjdid() {
		return xygxmjdid;
	}

	public void setXygxmjdid(String xygxmjdid) {
		this.xygxmjdid = xygxmjdid;
	}

	public int getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}

	public String getYhids() {
		return yhids;
	}

	public void setYhids(String yhids) {
		this.yhids = yhids;
	}

	public List<String> getXmids() {
		return xmids;
	}

	public void setXmids(List<String> xmids) {
		this.xmids = xmids;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getMbmc() {
		return mbmc;
	}

	public void setMbmc(String mbmc) {
		this.mbmc = mbmc;
	}

	public String getXmlb() {
		return xmlb;
	}

	public void setXmlb(String xmlb) {
		this.xmlb = xmlb;
	}

	public String getMbid() {
		return mbid;
	}

	public void setMbid(String mbid) {
		this.mbid = mbid;
	}

	public String getXmgkx() {
		return xmgkx;
	}

	public void setXmgkx(String xmgkx) {
		this.xmgkx = xmgkx;
	}

	public String getXmfz() {
		return xmfz;
	}

	public void setXmfz(String xmfz) {
		this.xmfz = xmfz;
	}

	public String getJdmc() {
		return jdmc;
	}

	public void setJdmc(String jdmc) {
		this.jdmc = jdmc;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}
	public String getLyxx()
	{
		return lyxx;
	}

	public void setLyxx(String lyxx)
	{
		this.lyxx = lyxx;
	}

	public List<RwlyDto> getRwlyDtos()
	{
		return rwlyDtos;
	}

	public void setRwlyDtos(List<RwlyDto> rwlyDtos)
	{
		this.rwlyDtos = rwlyDtos;
	}

	public String getDdid()
	{
		return ddid;
	}

	public void setDdid(String ddid)
	{
		this.ddid = ddid;
	}

	public String getXmmc()
	{
		return xmmc;
	}

	public void setXmmc(String xmmc)
	{
		this.xmmc = xmmc;
	}

	public String getZwcrwid()
	{
		return zwcrwid;
	}

	public void setZwcrwid(String zwcrwid)
	{
		this.zwcrwid = zwcrwid;
	}

	public String getDqjd()
	{
		return dqjd;
	}

	public void setDqjd(String dqjd)
	{
		this.dqjd = dqjd;
	}

	public String getWcbj()
	{
		return wcbj;
	}

	public void setWcbj(String wcbj)
	{
		this.wcbj = wcbj;
	}

	public String getBgcolor()
	{
		return bgcolor;
	}

	public void setBgcolor(String bgcolor)
	{
		this.bgcolor = bgcolor;
	}

	public String getFzrmc()
	{
		return fzrmc;
	}

	public void setFzrmc(String fzrmc)
	{
		this.fzrmc = fzrmc;
	}

	public String getRwwcd(){
		return rwwcd;
	}

	public void setRwwcd(String rwwcd){
		this.rwwcd = rwwcd;
	}

	public String getFinishnum()
	{
		return finishnum;
	}

	public void setFinishnum(String finishnum)
	{
		this.finishnum = finishnum;
	}

	public String getUnfinishnum()
	{
		return unfinishnum;
	}

	public void setUnfinishnum(String unfinishnum)
	{
		this.unfinishnum = unfinishnum;
	}
	
	public List<XmjdrwDto> getXmjdzrwDto()
	{
		return xmjdzrwDto;
	}

	public void setXmjdzrwDto(List<XmjdrwDto> xmjdzrwDto)
	{
		this.xmjdzrwDto = xmjdzrwDto;
	}

	public String getRwjbmc()
	{
		return rwjbmc;
	}

	public void setRwjbmc(String rwjbmc)
	{
		this.rwjbmc = rwjbmc;
	}

	public String getRwbqmc()
	{
		return rwbqmc;
	}

	public void setRwbqmc(String rwbqmc)
	{
		this.rwbqmc = rwbqmc;
	}

	public String getRwjbxh() {
		return rwjbxh;
	}

	public void setRwjbxh(String rwjbxh) {
		this.rwjbxh = rwjbxh;
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

	public List<FjcfbDto> getFjcfbDtos() {
		return fjcfbDtos;
	}

	public void setFjcfbDtos(List<FjcfbDto> fjcfbDtos) {
		this.fjcfbDtos = fjcfbDtos;
	}

	public String getJdxh() {
		return jdxh;
	}

	public void setJdxh(String jdxh) {
		this.jdxh = jdxh;
	}

	public String getJdxmjdid() {
		return jdxmjdid;
	}

	public void setJdxmjdid(String jdxmjdid) {
		this.jdxmjdid = jdxmjdid;
	}

	public String getPrejdid() {
		return prejdid;
	}

	public void setPrejdid(String prejdid) {
		this.prejdid = prejdid;
	}

	public String getMyflg() {
		return myflg;
	}

	public void setMyflg(String myflg) {
		this.myflg = myflg;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
