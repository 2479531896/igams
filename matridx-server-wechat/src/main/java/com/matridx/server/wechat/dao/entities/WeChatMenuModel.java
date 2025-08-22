package com.matridx.server.wechat.dao.entities;

/**
 * 菜单
 * @author linwu
 *
 */
public class WeChatMenuModel {
	private WeChatButtonModel[] button;
	private WeChatUserMenuMatchrule matchrule;
	
	public WeChatButtonModel[] getButton() {
		return button;
	}

	public void setButton(WeChatButtonModel[] button) {
		this.button = button;
	}

	public WeChatUserMenuMatchrule getMatchrule() {
		return matchrule;
	}

	public void setMatchrule(WeChatUserMenuMatchrule matchrule) {
		this.matchrule = matchrule;
	}
	
}
