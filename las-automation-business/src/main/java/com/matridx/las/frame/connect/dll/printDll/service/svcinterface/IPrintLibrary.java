package com.matridx.las.frame.connect.dll.printDll.service.svcinterface;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @author jld
 */
public interface IPrintLibrary extends Library {
    // 调用DLL动态库中的函数
    //PrintService PRINT_SERVICE = Native.load("D:\\production\\igams\\las-automation-business\\src\\main\\java\\com\\matridx\\las\\connect\\dll\\printDll\\dllFile\\CDFPSK.dll", PrintService.class);
    IPrintLibrary PRINT_SERVICE = Native.load("CDFPSK.dll", IPrintLibrary.class);

    int OpenPort(int PortFlag); //设置打印机端口
    int SetPCComPort(int BaudRate, boolean HandShake); //设置波特率
    int PTK_ClearBuffer (); //清空缓冲区
    int PTK_SetPrintSpeed (int px); //设置打印速度
    int PTK_SetDarkness (int id); //设置打印黑度
    int PTK_SetLabelHeight (int lheight, int gapH,
                            int gapOffset, boolean bFlag);  //设置标签的高度和定位间隙\黑线\穿孔的高度
    int PTK_SetLabelWidth (int lwidth); //设置标签的宽度
    int PTK_SetDirection (char direct); //设置标签打印方向
    int PTK_GetInfo(); //得到本 API 函数库的版本信息
    int PTK_PrintLabel ( int number,  int cpnumber); //执行打印
    /*
        x：设置 X 坐标，以点(dots)为单位；
        y：设置 X 坐标，以点(dots)为单位；
        FHeight：字型高度，以点(dots)为单位；
        FWidth：字型宽度，以点(dots)为单位；
        * 如果想打印正常比例的字体，需将 FWidth 设置为 0；
        FType：字型名称；
        Fspin：字体旋转角度: 1->居左 0 度, 2->居左 90 度, 3->居左 180 度, 4->居左 270 度
        5->居中 0 度, 6->居中 90 度, 7->居中 180 度, 8->居中 270 度
        Fweight：字体粗细。
        0 and 400 -> 400 标准、
        100 -> 非常细、200 -> 极细、
        300 -> 细 、500 -> 中等、
        600 -> 半粗 、700 -> 粗 、
        800 -> 特粗 、900 -> 黑体。
        Fitalic：斜体，0 -> FALSE、1 -> TRUE；
        Funline：文字加底线，0 -> FALSE、1 -> TRUE；
        FstrikeOut：文字加删除线，0 -> FALSE、1 -> TRUE；
        id_name： 识别名称，因为一行 TrueType 文字将被转换成 PCX 格式数据以 id_name 作为 PCX 格
        居左旋转效果 居中旋转效果
        X,Y 坐标在此处 X,Y 坐标在此处
        POSTEK PPLE API 函数手册
        函数详细说明
        式图形的名称存放到打印机内，在关机前都可以多次通过 PTK_DrawPcxGraphics( )
        调用 id_name 打印这行文字；(当 data 参数或其他参数不同时，请务必设定不同的
        id_name 值)
        data：字符串内容。
     */
    int PTK_DrawTextTrueTypeW(int x, int y,
                              int FHeight,  int FWidth,
                              String  FType,  int Fspin,
                              int FWeight,boolean FItalic,
                              boolean FUnline, boolean FStrikeOut,
                              String id_name,String data);  //设置打印文字
    /*
        x: ●X 座标。
        y: ●Y 座标。备注：1 dot = 0.125 mm。
        w: ●最大打印宽度，单位 dots。
        v: ●最大打印高度，单位 dots。
        o: ●设置旋转方向, 范围：0～3。
        （0--0°，1--90°，2--180°，3--270°）
        r: ●设置放大倍数，以点(dots)为单位,范围值：(1 - 99)。
        （1--放大 1 倍， 2--放大 2 倍，3--放大 3 倍⋯ ⋯ ）
            若放大倍数在 1-99 范围外，默认设置为放大 1 倍
        m: ●QR 码编码模式选择,范围值(0 - 4)。
        0 是选择数字模式
        1 是选择数字字母模式
        2 是选择字节模式 0~256
        3 是选择中国汉字模式
        4 是选择混合模式
         g: ●QR 码纠错等级选择,范围值(0 - 3)。
        0 是'L'等级
        1 是'M'等级
        2 是'Q1'等级
        3 是'H1'等级
         s: ●QR 码掩模图形选择,范围值(0 - 8)。
        0 - 是掩模图形 000
        1 - 是掩模图形 001
        2 - 是掩模图形 010
        3 - 是掩模图形 011
        4 - 是掩模图形 100
        5 - 是掩模图形 101
     */
    int PTK_DrawBar2D_QR (   int x,  int y,
                             int w,  int v,
                             int o,  int r,
                             int m,  int g,
                             int s, String pstr); //打印一个 QR 条码（指令方式）。
    /*
        px: 设置 X 坐标,以点(dots)为单位.
        py: 设置 Y 坐标,以点(dots)为单位.
        pdirec:选择条码的打印方向. 0—不旋转;1—旋转 90°; 2—旋转 180°; 3—旋转 270°.
        pCode: 选择要打印的条码类型. (不同类型条码有字符限制或字符个数等限制，请参考具体标准)
        NarrowWidth: 设置条码中窄单元的宽度,以点(dots)为单位.
        pHorizontal: 设置条码中宽单元的宽度,以点(dots)为单位.
        pVertical: 设置条码高度,以点(dots)为单位.
        ptext: 选’N’ 对应 ASCII 值 78 则不打印条码下面的人可识别文字,
        选’B’ 对应 ASCII 值 66 则打印条码下面的人可识别文字.
        pstr：一个长度为 1-100 的字符串。用户可以用”DATA”,Cn,Vn 自由排列组合成一个组合字符串,
         “DATA”: 常量字符串，必须用‘”’作为起始和结束符号，如“POSTEK Printer”。
        POSTEK PPLE API 函数手册
        21
        函数详细说明
         Cn: 序列号数值，此序列号必须已经定义,请参考 PTK_DrawTextEx 函数范例。
        Vn: 变量字符串，此变量字符串必须已经定义,请参考 PTK_DrawTextEx 函数范例。。
        如: ”data1”CnVn”data2”。
        Varible：true 表示当前字符串当中包含有变量操作,需加‘\”’将打印常量字符串包含。
        例如：PTKDrawBarcodeEx (50,30,0,”1A”,1,1,10,’N’,”\”123456\””,true);
        false 表示当前字符串当中不包含有变量操作，不需加‘\”’。
        例如：PTKDrawBarcodeEx (50,30,0,”1A”,1,1,10,’N’,”123456”,false);和
        PTKDrawBarcode(50,30,0,”1A”,1,1,10,’N’,”123456”)效果相同；

     */
    int PTK_DrawBarcodeEx ( int px,int py,
                            int pdirec,String pCode,
                            int NarrowWidth,
                            int pHorizontal,
                            int pVertical,char ptext, String pstr, boolean Varible);//打印条形码






}
