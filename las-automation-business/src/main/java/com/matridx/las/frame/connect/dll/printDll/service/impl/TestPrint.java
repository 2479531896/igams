package com.matridx.las.frame.connect.dll.printDll.service.impl;

import com.matridx.las.frame.connect.dll.printDll.service.svcinterface.IPrintLibrary;


/**
 * @author jld
 */
public class TestPrint {
    public static void main(String[] args) {
        int a1 = IPrintLibrary.PRINT_SERVICE.OpenPort(7);
        if (a1 != 0)
        {
            System.out.print("打开打印机端口："+ a1);
        }


        int a2 = IPrintLibrary.PRINT_SERVICE.SetPCComPort(9600,  true);
        if (a2 != 0)
        {
            System.out.print("设置波特率："+a2);
        }
        int a3 = IPrintLibrary.PRINT_SERVICE.PTK_ClearBuffer();           //清空缓冲区
        if (a3 != 0)
        {
            System.out.print("清空缓冲区："+ a3);
        }
        int a4 = IPrintLibrary.PRINT_SERVICE.PTK_SetPrintSpeed(4);        //设置打印速度
        if (a4 != 0)
        {
            System.out.print("设置打印速度："+ a4);
        }
        int a5 = IPrintLibrary.PRINT_SERVICE.PTK_SetDarkness(10);         //设置打印黑度
        if (a5 != 0)
        {
            System.out.print("设置打印黑度："+ a5);
        }
        int a6 = IPrintLibrary.PRINT_SERVICE.PTK_SetLabelHeight(240, 16, 0, false);     //设置标签的高度和定位间隙\黑线\穿孔的高度
        if (a6 != 0)
        {
            System.out.print("设置标签的高度和定位间隙："+ a6);
        }
        int a7 = IPrintLibrary.PRINT_SERVICE.PTK_SetLabelWidth(2285);      //设置标签的宽度
        if (a7 != 0)
        {
            System.out.print("设置标签的宽度："+a7);
        }
        int a8 = IPrintLibrary.PRINT_SERVICE.PTK_SetDirection('T');      //设置标签打印方向
        if (a8 != 0)
        {
            System.out.print("设置标签打印方向："+ a8);
        }


        int a14 = IPrintLibrary.PRINT_SERVICE.PTK_DrawBar2D_QR(520,10,100,100,0,7,1,0,8,"ceshi");
        if(a14 != 0){
            System.out.print("打印结果："+ a14);
        }


        int a11 = IPrintLibrary.PRINT_SERVICE.PTK_DrawTextTrueTypeW(500,180,67,0,"宋体",3,700,false,
                false,false,"A4","GXJ20231115");
        if(a11 != 0){
            System.out.print("打印结果："+ a11);
        }


        int a12 = IPrintLibrary.PRINT_SERVICE.PTK_DrawTextTrueTypeW(500,120,67,0,"宋体",3,700,false,
                false,false,"A5","TWO22222");
        if(a12 != 0){
            System.out.print("打印结果："+ a12);
        }

        int a13 = IPrintLibrary.PRINT_SERVICE.PTK_DrawTextTrueTypeW(500,60,67,0,"宋体",3,700,false,
                false,false,"A6","disanhang3333");
        if(a13 !=0){
            System.out.print("打印结果："+ a13);
        }

//        int a15 = PrintService.PRINT_SERVICE.PTK_DrawBarcodeEx(75,3,0,"1A",6,12,180,'N',"123456",false);
//        if(a15 !=0){
//            System.out.print("打印结果："+ a15);
//        }
        int a10 = IPrintLibrary.PRINT_SERVICE.PTK_PrintLabel(1,1);
        if(a10 !=0){
            System.out.print("执行打印："+ a10);
            }


    }
}
