package com.matridx.igams.common.util;

/**
 * @author WYX
 * @version 1.0
 * @className DataToExcelThread
 * @description TODO
 * @date 16:11 2023/8/11
 **/
public class DataToExcelThread extends Thread{

    private final DataToExcel dataToExcel;

    public DataToExcelThread(DataToExcel dataToExcel){
        this.dataToExcel = dataToExcel;
    }

    @Override
    public void run() {
        if ("1".equals(dataToExcel.getDclx())){
            dataToExcel.createExcel();
        }else if ("2".equals(dataToExcel.getDclx())){
            dataToExcel.createRevenueCostExcel();
        }
    }
}
