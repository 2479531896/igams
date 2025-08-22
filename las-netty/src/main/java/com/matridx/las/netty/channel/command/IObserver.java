package com.matridx.las.netty.channel.command;

/**
 * command 观察者
 * @author linwu
 *
 */
public interface IObserver {
	/**
     * command 接收到命令时的回调方法
     * @param obj
     */
    boolean onReceive(Object obj);
}
