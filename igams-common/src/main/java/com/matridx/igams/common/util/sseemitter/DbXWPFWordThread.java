package com.matridx.igams.common.util.sseemitter;

import com.matridx.igams.common.util.CommonXWPFWordUtil;
import com.matridx.igams.common.util.DbXWPFWordUtil;

/**
 * @author:JYK
 */
public class DbXWPFWordThread extends Thread{
    private final DbXWPFWordUtil dbXWPFWordUtil;
    private final String wjid;

    public DbXWPFWordThread(DbXWPFWordUtil dbXWPFWordUtil, String wjid) {
        this.dbXWPFWordUtil=dbXWPFWordUtil;
        this.wjid=wjid;
    }
    @Override
    public void run(){
        dbXWPFWordUtil.replaceFinanceTemplate();
    }
}
