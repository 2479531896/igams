package com.matridx.las.frame.connect.channel.command;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public abstract class ACommand implements ICommand,IObserver {

    private boolean available;
    //错误信息
    private String error;
    //异常信息
    private Exception exception;
    //最大重试次数
    private int maxRetryCount;
    //返回结果
    private Object result;
    //是否停止
    private boolean stop;
    //是否暂停
    private boolean pause;
    //参数
    private Map<String,Object> params = new HashMap<>();

    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public java.lang.Exception getException() {
        return exception;
    }

    @Override
    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    @Override
    public Object getResult() {
        return result;
    }

    public boolean isStop() {
        return stop;
    }

    public boolean isPause() {
        return pause;
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    @Override
    public void setStop(boolean stop) {
        this.stop = stop;
    }

    @Override
    public void setPause(boolean pause) {
        this.pause = pause;
    }

    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    protected BlockingQueue<FrameModel> cmdQueue;

    /**
     * 异常发生时的处理
     * @return
     */
    public abstract boolean exceptionHappend();

    /**
     * 用于编写实际命令,默认不抛出异常
     * @return
     */
    protected  boolean executing(){
        return executing(false);
    }

    /**
     * 用于编写实际命令
     * @param throwException 是否抛出异常
     * @return
     */
    protected abstract boolean executing(boolean throwException);

    /**
     * 处理Can的第一次返回的信息
     * @param times 返回次数：1 第一次  2 第二次
     * @param canFrameModel 返回的帧信息
     * @param cmdModel 协议名称
     * @return
     */
    public abstract boolean executReceive(int times, FrameModel recModel, FrameModel sendModel);

    /**
     * Command前处理
     * @return
     */
    public abstract boolean preDeal();

    /**
     * 命令未成功时的再次运行处理
     * @return
     */
    public abstract boolean retry();

    /**
     * 命令执行时的处理,默认不抛异常
     * @return
     */
    public boolean onExecuting()
    {
        return onExecuting(false);
    }

    /**
     * 命令执行时的处理
     * @param throwException 是否抛异常
     * @return
     */
    public boolean onExecuting(boolean throwException)
    {
        return executing(throwException);
    }

    /**
     * 其他命令执行后，自己作为观察者需要处理的业务
     * @param command
     */
    public void onCommandExecuted(ICommand command)
    {
        commandExecutedImpl(command);
    }

    /**
     * 为方便子类扩展观察者的业务，特对观察者执行后的方法进行扩展
     * @param command
     */
    protected void commandExecutedImpl(ICommand command)
    {

    }

    /**
     * 其他命令执行时，自己作为观察者需要处理的业务
     * @param command
     */
    public void onCommandExecuting(ICommand command)
    {
        commandExecutingImpl(command);
    }

    /**
     * 为方便子类扩展观察者的业务，特对观察者执行时的方法进行扩展
     * @param command
     */
    protected void commandExecutingImpl(ICommand command)
    {

    }

    /**
     * 其他命令执行发生异常，自己作为观察者需要处理的业务
     * @param command
     * @param ex
     */
    public void onExceptionOccurred(ICommand command, Exception ex)
    {
        exceptionOccurredImpl(command, ex);
    }

    /**
     * 为方便子类扩展观察者的业务，特对观察者执行异常时的方法进行扩展
     * @param command
     * @param ex
     */
    protected void exceptionOccurredImpl(ICommand command, Exception ex)
    {

    }
    /**
     * 命令从暂停中恢复时应该处理的业务
     */
    public void onResume()
    {
        return;
    }
}
