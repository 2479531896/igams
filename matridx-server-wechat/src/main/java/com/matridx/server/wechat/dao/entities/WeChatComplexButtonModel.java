package com.matridx.server.wechat.dao.entities;

/**
 * 拥有子菜单的按钮
 * @author linwu
 *
 */
public class WeChatComplexButtonModel extends WeChatButtonModel{
	//子菜单
	private WeChatButtonModel[] sub_button;

	public WeChatButtonModel[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(WeChatButtonModel[] sub_button) {
		this.sub_button = sub_button;
	} 
}
