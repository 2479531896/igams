package com.matridx.server.wechat.dao.entities;

public class WeChatUserModel {
	//订阅 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
	private String subscribe;
	//用户ID 用户的标识，对当前公众号唯一
	private String openid;
	//昵称
	private String nickname;
	//性别，值为1时是男性，值为2时是女性，值为0时是未知
	private String sex;
	//语言，简体中文为zh_CN
	private String language;
	//城市
	private String city;
	//省份
	private String province;
	//国家
	private String country;
	//个人头像
	private String headimgurl;
	//用户关注公众号时间,注意:单位为秒,不是毫秒
	private String subscribe_time;
	//备注
	private String remark;
	//groupid
	private String groupid;
	//tagid_list
	private String tagid_list;
	//subscribe_scene 返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，
	//ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENEPROFILE LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，
	//ADD_SCENE_PAID 支付后关注，ADD_SCENE_OTHERS 其他
	private String subscribe_scene;
	//qr_scene 二维码扫码场景（开发者自定义）
	private String qr_scene;
	//qr_scene_str 二维码扫码场景描述（开发者自定义）
	private String qr_scene_str;
	//unionid 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
	private String unionid;
	//访问用户信息用的accesstoken
	private String access_token;
	//目标用户
	private String target_user;
	//错误代码
	private String errcode;
	//错误信息
	private String errmsg;
	//关注平台
	private String gzpt;
	//消息内容
	private String content;
	
	public String getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getSubscribe_time() {
		return subscribe_time;
	}
	public void setSubscribe_time(String subscribe_time) {
		this.subscribe_time = subscribe_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getTagid_list() {
		return tagid_list;
	}
	public void setTagid_list(String tagid_list) {
		this.tagid_list = tagid_list;
	}
	public String getSubscribe_scene() {
		return subscribe_scene;
	}
	public void setSubscribe_scene(String subscribe_scene) {
		this.subscribe_scene = subscribe_scene;
	}
	public String getQr_scene() {
		return qr_scene;
	}
	public void setQr_scene(String qr_scene) {
		this.qr_scene = qr_scene;
	}
	public String getQr_scene_str() {
		return qr_scene_str;
	}
	public void setQr_scene_str(String qr_scene_str) {
		this.qr_scene_str = qr_scene_str;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getTarget_user() {
		return target_user;
	}
	public void setTarget_user(String target_user) {
		this.target_user = target_user;
	}
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getGzpt() {
		return gzpt;
	}
	public void setGzpt(String gzpt) {
		this.gzpt = gzpt;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
