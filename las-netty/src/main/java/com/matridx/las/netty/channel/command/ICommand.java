package com.matridx.las.netty.channel.command;

import java.util.Map;

public interface ICommand {
	/**
     * Command是否有效
     * @return
     */
    boolean isAvailable();

    /**
	 * 错误信息
     * @return
     */
    String getError();

    /**
     * 获取停止标记
     * @return
     */
    boolean isStop();

    /**
     * 设置停止标记
     * @param stop
     */
    void setStop(boolean stop);

    /**
     * 获取暂停标记
     * @return
     */
    boolean isPause();

    /**
     * 设置暂停标记
     * @param pause
     */
    void setPause(boolean pause);

    /**
     * 获取异常信息
     * @return
     */
    Exception getException();

    /**
     * 获取重复次数
     * @return
     */
    int getMaxRetryCount();

    /**
     * 设置重复次数
     * @param maxRetryCount
     */
    void setMaxRetryCount(int maxRetryCount);

    /**
     * 返回结果
     * @return
     */
    Object getResult();

    /**
     * 获取命令所使用的参数
     * @return
     */
    Map<String, Object> getParams();

    /**
     * 设置命令所使用的参数
     * @param params
     */
    void setParams(Map<String, Object> params);

    /**
     * 用于执行实际命令
     * @param throwException
     * @return
     */
    boolean onExecuting(boolean throwException);

    /// <summary>
    /// 处理Can的第一次返回的信息
    /// </summary>
    /// <param name="times">返回次数：1 第一次  2 第二次</param>
    /// <param name="canFrameModel">返回的帧信息</param>
    /// <param name="ProtocolName">协议名称</param>
    /// <returns></returns>
    boolean executReceive(int times, FrameModel recModel, FrameModel sendModel);

    /// <summary>
    /// 命令未成功时的再次运行处理
    /// </summary>
    /// <returns></returns>
    boolean retry();

    /// <summary>
    /// 异常发生时的处理
    /// </summary>
    /// <returns></returns>
    boolean exceptionHappend();

    /// <summary>
    /// Command前处理
    /// </summary>
    /// <returns></returns>
    boolean preDeal();
}
