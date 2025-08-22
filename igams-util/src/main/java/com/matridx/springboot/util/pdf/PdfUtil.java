package com.matridx.springboot.util.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.util.List;


/**
 * @author hmz
 * {@code @date2022/08/12} version V1.0
 **/
public class PdfUtil {

    private static final Logger log = LoggerFactory.getLogger(PdfUtil.class);

    /**
     * 合并多分PDF
     */
    public boolean mergePdfFiles(List<String> files, String newfile){
        boolean retValue = false;
        Document document = null;
        PdfCopy copy = null;
        PdfReader reader = null;
        try {
            document = new Document(new PdfReader(files.get(0)).getPageSize(1));
            copy = new PdfCopy(document, new FileOutputStream(newfile));
            document.open();
            for (String file : files) {
                reader = new PdfReader(file);
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
            }
            retValue = true;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (copy != null) {
                copy.close();
            }
            if (document != null) {
                document.close();
            }
        }
        return retValue;
    }

}
