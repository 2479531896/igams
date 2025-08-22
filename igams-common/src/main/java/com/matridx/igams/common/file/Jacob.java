package com.matridx.igams.common.file;

import java.io.File;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * 备注：使用该类时，要在jdk->jre->bin中加入jacob-1.18-x64.dll，
 * 该文件可以在maven仓库中找到（com\jacob\jacob\1.18\jacob-1.18-x64.dll）
 * @param source
 * @param target
 * @return
 */
public class Jacob {
	static final int wdDoNotSaveChanges = 0;// 不保存待定的更改。  
    static final int wdFormatPDF = 17;// word转PDF 格式  
  
    /**
          * 文件转换，从word 转换成pdf
     * @param source
     * @param target
     * @return
     */
    public static boolean word2pdf(String source, String target) {
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", false);
            Dispatch docs = app.getProperty("Documents").toDispatch();
            Dispatch doc = Dispatch.call(docs, "Open", source, false, true).toDispatch();
            File tofile = new File(target);
            if (tofile.exists()) {
                tofile.delete();
            }
//            Dispatch.call(doc, "SaveAs", target, wdFormatPDF);
            Dispatch.invoke(doc,"SaveAs",Dispatch.Method,new Object[] {target,new Variant(17)},new int[1]);
            Dispatch.call(doc, "Close", false);
            long end = System.currentTimeMillis();
            System.out.println("转换完成，用时：" + (end - start) + "ms");
            return true;
        } catch (Exception e) {
            System.out.println("Word转PDF出错：" + e.getMessage());
            return false;
        } finally {
            if (app != null) {
                app.invoke("Quit", wdDoNotSaveChanges);
            }
        }
    }

    public static boolean pdf2pdf(String source, String target) {
        ActiveXComponent app = null;
        Dispatch ppt = null;
        try {
            app = new ActiveXComponent("PowerPoint.Application");
            Dispatch ppts = app.getProperty("Presentations").toDispatch();
            File tofile = new File(target);
            if (tofile.exists()) {
                tofile.delete();
            }
//            Dispatch.call(doc, "SaveAs", target, wdFormatPDF);
            ppt = Dispatch.call(ppts, "Open", source, true,true, false).toDispatch();
            Dispatch.call(ppt, "SaveAs", target, 32);
            return true;
        } catch (Exception e) {
            System.out.println("Word转PDF出错：" + e.getMessage());
            return false;
        } finally {
            if (app != null) {
                app.invoke("Quit");
            }
        }
    }
  
    public static void main(String[] args) {
        Jacob.pdf2pdf("C:\\Users\\DELL\\Desktop\\dist(4)\\前台屏幕显示更新2.pptx", "C:\\Users\\DELL\\Desktop\\dist(4)\\前台屏幕显示更新2.pdf");
    }
}
