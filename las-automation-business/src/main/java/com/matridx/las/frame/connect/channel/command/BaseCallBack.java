package com.matridx.las.frame.connect.channel.command;

public interface BaseCallBack {
	/**
	 * 再最后的流程里，还原数据
	 * @param yqid
	 * @param robotid
	 * @param commd
	 */
	public void initInstrument(String yqid, String robotid, String commd);
}
