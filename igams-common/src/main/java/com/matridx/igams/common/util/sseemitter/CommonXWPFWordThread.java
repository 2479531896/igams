package com.matridx.igams.common.util.sseemitter;

import com.matridx.igams.common.util.CommonXWPFWordUtil;

/**
 * @author:JYK
 */
public class CommonXWPFWordThread extends Thread{
    private final CommonXWPFWordUtil commonXWPFWordUtil;
    private final String wjid;

    public CommonXWPFWordThread(CommonXWPFWordUtil commonXWPFWordUtil,String wjid) {
        this.commonXWPFWordUtil=commonXWPFWordUtil;
        this.wjid=wjid;
    }
    @Override
    public void run(){
        commonXWPFWordUtil.replaceFinanceTemplate();
    }
}
