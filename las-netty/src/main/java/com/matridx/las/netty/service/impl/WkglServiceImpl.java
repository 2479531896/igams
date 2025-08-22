package com.matridx.las.netty.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matridx.igams.common.enums.YesNotEnum;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.las.netty.dao.entities.WkglDto;
import com.matridx.las.netty.dao.entities.WkglModel;
import com.matridx.las.netty.dao.entities.WkmxDto;
import com.matridx.las.netty.dao.post.IWkglDao;
import com.matridx.las.netty.service.svcinterface.IWkglService;
import com.matridx.las.netty.service.svcinterface.IWkmxService;
import com.matridx.las.netty.service.svcinterface.IWksyService;
import com.matridx.las.netty.util.XWPFLibraryUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Service
public class WkglServiceImpl extends BaseBasicServiceImpl<WkglDto, WkglModel, IWkglDao> implements IWkglService {
    @Autowired
    IWkmxService wkmxService;

    @Value("${matridx.fileupload.releasePath}")
    private String releaseFilePath;

    @Value("${matridx.fileupload.tempPath}")
    private String tempPath;

    @Autowired
    IFjcfbService fjcfbService;

    @Value("${matridx.ftp.url:}")
    private String FTP_URL = null;

    @Autowired
    IJcsjService jcsjService;

    @Autowired
    IWksyService wksyService;

    @Autowired
    IWkglDao wkglDao;

    private Logger log = LoggerFactory.getLogger(XWPFLibraryUtil.class);

    /**
     * 生成文库文件
     *
     * @param wkglDto
     * @return
     */
    @Override
    public Map<String, Object> getParamForLibrary(WkglDto wkglDto) {
        XWPFLibraryUtil xwpfLibraryUtil = new XWPFLibraryUtil();
        Map<String, Object> map = new HashMap<String, Object>();
        List<WkmxDto> wksyypList = wkmxService.generateReportList(wkglDto.getWkid());
        map.put("wksyypList", wksyypList);
        map.put("releaseFilePath", releaseFilePath); // 正式文件路径
        map.put("tempPath", tempPath); // 临时文件路径
        map.put("wkid", wkglDto.getWkid());
        //获取文件路径
        JcsjDto jcsjDto = new JcsjDto();
        jcsjDto.setJclb("LIBRARY_TEMPLATE");
        jcsjDto.setCsdm("Library");
        jcsjDto = jcsjService.getDto(jcsjDto);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtil.isNotBlank(jcsjDto.getCsid())) {
            FjcfbDto fjcfbDto = new FjcfbDto();
            fjcfbDto.setYwid(jcsjDto.getCsid());
            fjcfbDto = fjcfbService.getDto(fjcfbDto);
            map.put("wjlj", fjcfbDto.getWjlj());
            resultMap = xwpfLibraryUtil.replaceDetection(map, fjcfbService, FTP_URL);
        } else {
            resultMap.put("fail", false);
        }
        String status = (String) resultMap.get("status");
        if (StringUtil.isNotBlank(status) && !"error".equals(status)) {
            //更新文库名称
            boolean result = wkmxService.updateWkmc(wkglDto.getWkid());
            if (!result) {
                resultMap.put("status", "error");
            }
        }
        return resultMap;
    }

    /**
     * 导入文库信息
     *
     * @param wkglDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> modSaveLibrary(WkglDto wkglDto) throws BusinessException {
        Map<String, Object> resMap = new HashMap<>();
        boolean flag = true;
        //查询文库管理的内容
        WkglDto wkglDto1 = dao.getWkglDtoBywkid(wkglDto);
        String sfdr = YesNotEnum.NOT.getCode();
        if (wkglDto1 != null && YesNotEnum.YES.getCode().equals(wkglDto1.getSfdr())) {
            sfdr = YesNotEnum.YES.getCode();
        }
        //文件复制到正式文件夹，插入信息至正式表
        if (wkglDto.getFjids() != null && wkglDto.getFjids().size() > 0) {
            for (int i = 0; i < wkglDto.getFjids().size(); i++) {
                fjcfbService.save2RealFile(wkglDto.getFjids().get(i), wkglDto.getWkid());
            }
        }
        //获取文件路径
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwid(wkglDto.getWkid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_LIBRARY_EXCEL_IN.getCode());
        fjcfbDto = fjcfbService.getDto(fjcfbDto);

        //获取更新数据
        if (fjcfbDto == null) {
            throw new BusinessException("msg", "未找到上传附件!");
        }
        DBEncrypt dbEncrypt = new DBEncrypt();
        String path = dbEncrypt.dCode(fjcfbDto.getWjlj());
        log.error(path);
        boolean result = fjcfbService.delete(fjcfbDto);

        if (!result) {
            throw new BusinessException("msg", "附件信息删除失败!");
        }

        File templateFile = new File(path);
        log.error(path);
        List<WkmxDto> wkmxList = new ArrayList<WkmxDto>();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(templateFile);
            @SuppressWarnings("resource")
            XSSFWorkbook sheets = new XSSFWorkbook(fileInputStream);
            // 获取第一个表单Sheet
            XSSFSheet sheetAt = sheets.getSheetAt(0);
            //得到Excel工作表的行数
            int rowcount = sheetAt.getLastRowNum();
            //从第4行开始
            for (int i = 3; i < rowcount; i++) {
                String libraryName = sheetAt.getRow(i).getCell((short) 0).getStringCellValue();
                if (StringUtil.isNotBlank(libraryName)) {
                    //处理文库名称
                    WkmxDto wkmxDto = new WkmxDto();
                    wkmxDto.setWkmc(libraryName);
                    Double wjyytj = sheetAt.getRow(i).getCell((short) 15).getNumericCellValue();
                    //四舍五入保留两位
                    String wjyytj_b = new BigDecimal(wjyytj).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    wkmxDto.setWkyyjytj(wjyytj_b);
                    Double wjxsyjytj = sheetAt.getRow(i).getCell((short) 16).getNumericCellValue();
                    //四舍五入保留两位
                    String wjxsyjytj_b = new BigDecimal(wjxsyjytj).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    wkmxDto.setWkxsyjytj(wjxsyjytj_b);
                    wkmxList.add(wkmxDto);
                } else {
                    break;
                }
            }
            //获取变性液值
            XSSFCell cella = sheetAt.getRow(9).getCell((short) 25);
            Double denaturing = cella.getNumericCellValue();
            //四舍五入保留两位
            String denaturing_b = new BigDecimal(denaturing).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            //获取平衡混合液
            XSSFCell cellb = sheetAt.getRow(10).getCell((short) 25);
            Double miscible = cellb.getNumericCellValue();
            //四舍五入保留两位
            String miscible_b = new BigDecimal(miscible).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            //获取杂交缓冲液
            XSSFCell cellc = sheetAt.getRow(12).getCell((short) 25);
            Double buffer = cellc.getNumericCellValue();
            //四舍五入保留两位
            String buffer_b = new BigDecimal(buffer).setScale(2, BigDecimal.ROUND_HALF_UP).toString();

            wkglDto.setBxy(denaturing_b);
            wkglDto.setPhhhy(miscible_b);
            wkglDto.setZjhcytj(buffer_b);
            wkglDto.setWkid(wkglDto.getWkid());
            wkglDto.setSfdr(YesNotEnum.YES.getCode());

            //关闭流
            fileInputStream.close();

            //更新文库实验
            result = update(wkglDto);
            if (!result) {
                throw new BusinessException("msg", "文库管理更新失败!");
            }
            //更新文库实验样品
            result = wkmxService.updateList(wkmxList);
            if (!result) {
                throw new BusinessException("msg", "文库明细更新失败!");
            }


        } catch (IOException e) {
            flag = false;
            log.error(e.toString());
        } finally {
            try {
                //判断流是否关闭
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        resMap.put("sfdr", sfdr);
        resMap.put("flag", flag);
        return resMap;
    }

    @Override
    public WkglDto getWkglDtoBywkid(WkglDto wkglDto) {
        return wkglDao.getWkglDtoBywkid(wkglDto);
    }

    @Override
    public boolean insertWkgl(WkglDto wkglDto) {
        return wkglDao.insertWkgl(wkglDto) > 0;
    }
}
