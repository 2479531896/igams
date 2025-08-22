package com.matridx.igams.wechat.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.enums.DetectionTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.crm.dao.entities.JxhsmxDto;
import com.matridx.igams.wechat.dao.entities.*;
import com.matridx.igams.wechat.dao.post.ISwyszkDao;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.igams.wechat.service.svcinterface.ISwskglService;
import com.matridx.igams.wechat.service.svcinterface.ISwyszkService;
import com.matridx.igams.wechat.service.svcinterface.IYszkmxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.email.EmailUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SwyszkServiceImpl extends BaseBasicServiceImpl<SwyszkDto, SwyszkModel, ISwyszkDao> implements ISwyszkService {


    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SjhbxxServiceImpl sjhbxxService;

    @Autowired
    IFjsqService fjsqService;

    @Autowired
    HbhzkhServiceImpl hbhzkhService;

    @Autowired
    SjjcxmServiceImpl sjjcxmService;

    @Autowired
    IYszkmxService yszkmxService;

    @Autowired
    SwkhglServiceImpl swkhglService;

    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ISwskglService swskglService;
    @Autowired
    ISjxxService sjxxService;

    private Logger log = LoggerFactory.getLogger(SwyszkServiceImpl.class);
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String uploadSaveFile(SwyszkDto swyszkDto) throws BusinessException, IOException {
        String message ="";
        String ids ="";
        for (int i = 0; i < swyszkDto.getFjids().size(); i++) {
            Map<Object, Object> mFile = redisUtil.hmget("IMP_:_" + swyszkDto.getFjids().get(i));
            //如果文件信息不存在，则返回错误
            if (mFile == null)
                return "false";
            //文件名
            String wjm = (String) mFile.get("wjm");
            //文件全路径
            String wjlj = (String) mFile.get("wjlj");
            String[] strings = wjm.split("-");
            String data = strings[0]+"-"+strings[1]+"-01";
            LocalDate lastDay = YearMonth.of(Integer.parseInt(strings[0]), Integer.parseInt(strings[1])).atEndOfMonth();
            String gsmc = strings[3];
            SwyszkDto dto = new SwyszkDto();
            dto.setYszkid(StringUtil.generateUUID());
            dto.setLrry(swyszkDto.getLrry());
            dto.setFlag(swyszkDto.getFlag());
            dto.setZdzq(data);
            dto.setLsdkrq(String.valueOf(lastDay));
            boolean success = false;
            if (wjm.contains(".xlsx")) {
                success = creatXSSFWorkbook(wjlj,dto,gsmc);
            } else if (wjm.contains(".xls")) {
                success = creatHSSFWorkbook(wjlj,dto,gsmc);
            }
            if (!success){
                message += ","+strings[0]+"-"+strings[1]+gsmc;
                ids += ","+swyszkDto.getFjids().get(i);
                break;
            }
            success = fjcfbService.save2RealFile(swyszkDto.getFjids().get(i),dto.getYszkid());
            if(!success)
                throw new BusinessException("msg","附件保存失败!");

        }
        if (StringUtil.isNotBlank(message)){
            message+="的对账单已上传，是否需要进行覆盖!"+"("+ids.substring(1)+")";
            return message.substring(1);
        }
        return "success";
    }

    public boolean creatXSSFWorkbook(String wjlj,SwyszkDto swyszkDto,String gsmc) throws IOException, BusinessException {
        @SuppressWarnings("resource")
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(wjlj));
        int numberOfSheets = workbook.getNumberOfSheets();
        Boolean success;
        List<SwyszkDto> swyszkDtos = new ArrayList<>();
        BigDecimal countJsje = new BigDecimal(0d);
        BigDecimal countSfje = new BigDecimal(0d);
        BigDecimal countKdfy = new BigDecimal(0d);
        List<SjjcxmDto> list = new ArrayList<>();
        List<FjsqDto> fjsqDtos = new ArrayList<>();
        List<YszkmxDto> yszkmxDtos = new ArrayList<>();
        boolean flag = true;
        String dcbj = null;
        String hbmc = null;
        for (int i = 0; i < numberOfSheets; i++) {
            XSSFSheet sheet =workbook.getSheetAt(i);
            int sjidInt = -1;
            int fjidInt = -1;
            int sjjcxmInt = -1;
            int sjjczxmInt = -1;
            int xmglidInt = -1;
            int hzxmInt = -1;
            int bzInt = -1;
            int sfjeInt = -1;
            int jsjeInt = -1;
            int kdfyInt = -1;
            boolean sheetflg = false;//用于判断该sheet是否需要存到数据库
            if (null != sheet.getRow(3)){
                for (int j = 0; j < sheet.getRow(3).getPhysicalNumberOfCells(); j++) {
                    if (null != sheet.getRow(3).getCell(j)){
                        String value = sheet.getRow(3).getCell(j).toString();
                        if("送检ID".equals(value)){
                            sheetflg=true;
                            sjidInt = j;
                        }else if ("复检ID".equals(value)){
                            fjidInt = j;
                        }else if ("检测项目ID".equals(value)){
                            sjjcxmInt = j;
                        }else if ("检测子项目ID".equals(value)){
                            sjjczxmInt = j;
                        }else if ("项目管理ID".equals(value)){
                            xmglidInt = j;
                        }else if ("患者姓名".equals(value)){
                            hzxmInt = j;
                        }else if ("备注".equals(value)){
                            bzInt = j;
                        }else if ("结算金额(元)".equals(value)){
                            jsjeInt = j;
                        }else if ("实付金额".equals(value)){
                            sfjeInt = j;
                        }else if ("快递费用".equals(value)){
                            kdfyInt = j;
                        }
                    }
                }
            }
            if(!sheetflg)//如果该sheet页没有读取到送检ID字段，择说明是自己添加的sheet，择不保存数据
                break;
            for (int j = 4; j < sheet.getPhysicalNumberOfRows(); j++) {
                if (null != sheet.getRow(j).getCell(0) && StringUtil.isNotBlank(sheet.getRow(j).getCell(0).toString()) && "合计".equals(sheet.getRow(j).getCell(0).toString())){
                    if (-1 != sfjeInt && null != sheet.getRow(j).getCell(sfjeInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(sfjeInt).toString())){
                        try {
                            countSfje = countSfje.add(new BigDecimal( Double.parseDouble(sheet.getRow(j).getCell(sfjeInt).toString())));
                        } catch (NumberFormatException e) {
                            try {
                                countSfje = countSfje.add(new BigDecimal( sheet.getRow(j).getCell(sfjeInt).getNumericCellValue()));
                            } catch (Exception ex) {
                                countSfje = countSfje.add(BigDecimal.ZERO);
                            }
                        }
                    }
                    if (-1 != jsjeInt && null != sheet.getRow(j).getCell(jsjeInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(jsjeInt).toString())){
                        try {
                            countJsje = countJsje.add(new BigDecimal( Double.parseDouble(sheet.getRow(j).getCell(jsjeInt).toString())));
                        } catch (NumberFormatException e) {
                            try {
                                countJsje = countJsje.add(new BigDecimal( sheet.getRow(j).getCell(jsjeInt).getNumericCellValue()));
                            } catch (Exception ex) {
                                countJsje = countJsje.add(BigDecimal.ZERO);
                            }
                        }
                    }
                    if (-1 != kdfyInt && null != sheet.getRow(j).getCell(kdfyInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(kdfyInt).toString())){
                        try {
                            countKdfy = countKdfy.add(new BigDecimal( Double.parseDouble(sheet.getRow(j).getCell(kdfyInt).toString())));
                        } catch (NumberFormatException e) {
                            try {
                                countKdfy = countKdfy.add(new BigDecimal( sheet.getRow(j).getCell(kdfyInt).getNumericCellValue()));
                            } catch (Exception ex) {
                                countKdfy = countKdfy.add(BigDecimal.ZERO);
                            }
                        }
                    }
                    break;
                }
                SjjcxmDto sjjcxmDto = new SjjcxmDto();
                YszkmxDto yszkmxDto = new YszkmxDto();
                yszkmxDto.setYszkid(swyszkDto.getYszkid());
                yszkmxDto.setYsmxid(StringUtil.generateUUID());
                yszkmxDto.setLrry(swyszkDto.getLrry());
                if (-1 != sfjeInt && null != sheet.getRow(j).getCell(sfjeInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(sfjeInt).toString()))
                    yszkmxDto.setSfje(sheet.getRow(j).getCell(sfjeInt).toString());
                if (-1 != kdfyInt && null != sheet.getRow(j).getCell(kdfyInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(kdfyInt).toString())){
                    try {
                        yszkmxDto.setKdfy(String.valueOf(Double.parseDouble(sheet.getRow(j).getCell(kdfyInt).toString())));
                    } catch (NumberFormatException e) {
                        try {
                            yszkmxDto.setKdfy(String.valueOf(sheet.getRow(j).getCell(kdfyInt).getNumericCellValue()));
                        } catch (Exception ex) {
                            yszkmxDto.setKdfy("0");
                        }
                    }
                }
                if (-1 != xmglidInt && null != sheet.getRow(j).getCell(xmglidInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(xmglidInt).toString()))
                    yszkmxDto.setXmglid(sheet.getRow(j).getCell(xmglidInt).toString());
                if (-1 != hzxmInt && null != sheet.getRow(j).getCell(hzxmInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(hzxmInt).toString()))
                    yszkmxDto.setMc(sheet.getRow(j).getCell(hzxmInt).toString());
                if (-1 != bzInt && null != sheet.getRow(j).getCell(bzInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(bzInt).toString()))
                    yszkmxDto.setBz(sheet.getRow(j).getCell(bzInt).toString());
                FjsqDto fjsqDto = new FjsqDto();
                if (-1 != sjidInt && null != sheet.getRow(j).getCell(sjidInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(sjidInt).toString())){
                    sjjcxmDto.setSjid(sheet.getRow(j).getCell(sjidInt).toString());
                    yszkmxDto.setYwid(sheet.getRow(j).getCell(sjidInt).toString());
                    yszkmxDto.setLx(DetectionTypeEnum.DETECT_SJ.getCode());
                }
                if (-1 != fjidInt && null != sheet.getRow(j).getCell(fjidInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(fjidInt).toString())){
                    fjsqDto.setFjid(sheet.getRow(j).getCell(fjidInt).toString());
                    yszkmxDto.setYwid(sheet.getRow(j).getCell(fjidInt).toString());
                    yszkmxDto.setLx(DetectionTypeEnum.DETECT_FJ.getCode());
                }
                if (-1 != sjjcxmInt && null != sheet.getRow(j).getCell(sjjcxmInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(sjjcxmInt).toString())) {
                    sjjcxmDto.setJcxmid(sheet.getRow(j).getCell(sjjcxmInt).toString());
                    fjsqDto.setJcxm(sheet.getRow(j).getCell(sjjcxmInt).toString());
                    yszkmxDto.setJcxmid(sheet.getRow(j).getCell(sjjcxmInt).toString());
                }
                if (-1 != sjjczxmInt && null != sheet.getRow(j).getCell(sjjczxmInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(sjjczxmInt).toString())) {
                    sjjcxmDto.setJczxmid(sheet.getRow(j).getCell(sjjczxmInt).toString());
                    fjsqDto.setJczxm(sheet.getRow(j).getCell(sjjczxmInt).toString());
                    yszkmxDto.setJczxmid(sheet.getRow(j).getCell(sjjczxmInt).toString());
                }
                if (-1 != jsjeInt && null != sheet.getRow(j).getCell(jsjeInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(jsjeInt).toString())){
                    sjjcxmDto.setDzje(sheet.getRow(j).getCell(jsjeInt).toString());
                    sjjcxmDto.setSfdz("1");
                    fjsqDto.setDzje(sheet.getRow(j).getCell(jsjeInt).toString());
                    fjsqDto.setSfdz("1");
                    yszkmxDto.setJsje(sheet.getRow(j).getCell(jsjeInt).toString());
                }else{
                    sjjcxmDto.setSfdz("0");
                    fjsqDto.setSfdz("0");
                }
                if(StringUtil.isBlank(yszkmxDto.getLx())){//若匹配不到送检或者复检，可能是手动添加，择设置类型为OTHER
                    yszkmxDto.setLx(DetectionTypeEnum.OTHER.getCode());
                }
                if (StringUtil.isNotBlank(sjjcxmDto.getSjid()) && StringUtil.isNotBlank(sjjcxmDto.getJcxmid()) && flag && StringUtil.isBlank(fjsqDto.getFjid()) && !"2".equals(swyszkDto.getFlag())){
                    flag = false;
                    SjjcxmDto dto = sjjcxmService.getDto(sjjcxmDto);
                    if (dto != null && StringUtil.isNotBlank(dto.getYszkid())){
                        if (!"1".equals(swyszkDto.getFlag()))
                            return false;
                        dcbj = dto.getDcbj();
                        SwyszkDto swyszkDto1 = new SwyszkDto();
                        swyszkDto1.setScry(swyszkDto.getLrry());
                        swyszkDto1.setYszkid(dto.getYszkid());
                        dao.delete(swyszkDto1);
                        dto.setXgry(swyszkDto.getLrry());
                        sjjcxmService.updateYszkInfoToNull(dto);
                    }
                }
                sjjcxmDto.setDcbj(dcbj);
                sjjcxmDto.setYszkid(swyszkDto.getYszkid());
                fjsqDto.setDcbj(dcbj);
                fjsqDto.setYszkid(swyszkDto.getYszkid());
                yszkmxDtos.add(yszkmxDto);
                if (StringUtil.isNotBlank(fjsqDto.getFjid())){
                    fjsqDtos.add(fjsqDto);
                }else {
                    list.add(sjjcxmDto);
                }
            }
        }

        if (StringUtil.isNotBlank(gsmc)){
            SwkhglDto swkhglDto = new SwkhglDto();
            swkhglDto.setGsmc(gsmc);
            SwkhglDto swkhglDto1 = swkhglService.getDto(swkhglDto);
            if (null != swkhglDto1){
                swyszkDto.setDzkh(swkhglDto1.getKhid());
                swyszkDto.setHkzq(swkhglDto1.getHkzq());
                swyszkDto.setHkzt(swkhglDto1.getKhid());
                HbhzkhDto hbhzkhDto = new HbhzkhDto();
                hbhzkhDto.setKhid(swkhglDto1.getKhid());
                //获取对账单中第一条数据的伙伴信息
                if(StringUtil.isNotBlank(yszkmxDtos.get(0).getYwid())){
                    SjxxDto sjxxDto=new SjxxDto();
                    sjxxDto.setSjid(yszkmxDtos.get(0).getYwid());
                    sjxxDto=sjxxService.getDto(sjxxDto);
                    hbhzkhDto.setHbid(sjxxDto.getHbid());
                }
                HbhzkhDto sfzykh=hbhzkhService.getDto(hbhzkhDto);
                if(sfzykh!=null && "1".equals(sfzykh.getZykh())){//若为主要客户，明细批量设置为1
                    yszkmxDtos.forEach(yszkmxDto -> yszkmxDto.setSfzykh("1"));
                }else{
                    yszkmxDtos.forEach(yszkmxDto -> yszkmxDto.setSfzykh("0"));
                }
                List<HbhzkhDto> dtoList = hbhzkhService.getDtoList(hbhzkhDto);
                if (!CollectionUtils.isEmpty(dtoList)){
                    swyszkDto.setQy(dtoList.get(0).getGwmc());
                    swyszkDto.setFl(dtoList.get(0).getFl());
                    swyszkDto.setDjxs(dtoList.get(0).getYhid());//用来存销售负责人
                    hbmc = dtoList.get(0).getHbmc();
                }
                swyszkDto.setSfje(countSfje.toString());
                swyszkDto.setJsje(countJsje.toString());
                swyszkDto.setKdfy(countKdfy.toString());
				swyszkDto.setSfyq("0");
                // 结算金额大于实付金额
                if (countJsje.add(countKdfy).compareTo(countSfje) > 0){
                    swyszkDto.setWhkje(countJsje.add(countKdfy).subtract(countSfje).toString());
                    swyszkDto.setWfkje("0");
                }else{
                    swyszkDto.setDkrq(swyszkDto.getLsdkrq());
                    swyszkDto.setWfkje(countSfje.subtract(countJsje).subtract(countKdfy).toString());
                    swyszkDto.setWhkje("0");
                }
                if(new BigDecimal(swyszkDto.getWhkje()).compareTo(BigDecimal.ZERO)==0 && new BigDecimal(swyszkDto.getWfkje()).compareTo(BigDecimal.ZERO)==0){
                    swyszkDto.setSfjq("1");
                }else{
                    swyszkDto.setSfjq("0");
                }
                swyszkDtos.add(swyszkDto);
            }
        }else{
            throw new BusinessException("msg","匹配客户失败，请检查文件名是否规范或者该客户是否存在！");
        }
        if (StringUtil.isNotBlank(dcbj) && StringUtil.isNotBlank(hbmc)){
            SjjcxmDto sjjcxmDto = new SjjcxmDto();
            sjjcxmDto.setDcbj(dcbj);
            sjjcxmDto.setHbmc(hbmc);
            success = sjjcxmService.updateDcEmpty(sjjcxmDto);
            if (!success)
                throw new BusinessException("msg","未找到数据！");
        }
        if (!CollectionUtils.isEmpty(list)){
            if (list.size() <= 100){
                sjjcxmService.updateListDzInfo(list);
            }else{
                List<SjjcxmDto> dtos = new ArrayList<>();
                for (SjjcxmDto sjjcxmDto : list) {
                    dtos.add(sjjcxmDto);
                    if (dtos.size() == 100){
                        sjjcxmService.updateListDzInfo(dtos);
                        dtos = new ArrayList<>();
                    }
                }

                if (!CollectionUtils.isEmpty(dtos)){
                    sjjcxmService.updateListDzInfo(dtos);
                }
            }
        }
        if (!CollectionUtils.isEmpty(fjsqDtos)){
            if (fjsqDtos.size() <= 100){
                fjsqService.updateListDzInfo(fjsqDtos);
            }else{
                List<FjsqDto> dtos = new ArrayList<>();
                for (FjsqDto fjsqDto : fjsqDtos) {
                    dtos.add(fjsqDto);
                    if (dtos.size() == 100){
                        fjsqService.updateListDzInfo(dtos);
                        dtos = new ArrayList<>();
                    }
                }

                if (!CollectionUtils.isEmpty(dtos)){
                    fjsqService.updateListDzInfo(dtos);
                }
            }
        }
        if (!CollectionUtils.isEmpty(swyszkDtos)){
            success = dao.insertList(swyszkDtos);
            if (!success)
                throw new BusinessException("msg","更新应收账款信息失败！");
        }
        if (!CollectionUtils.isEmpty(yszkmxDtos)){
            int num;
            if (yszkmxDtos.size() <= 100){
                num = yszkmxService.insertList(yszkmxDtos);
                if (yszkmxDtos.size() != num)
                    throw new BusinessException("msg","更新应收账款明细信息失败!");
            }else{
                List<YszkmxDto> dtos = new ArrayList<>();
                for (YszkmxDto yszkmxDto : yszkmxDtos) {
                    dtos.add(yszkmxDto);
                    if (dtos.size() == 100){
                        num = yszkmxService.insertList(dtos);
                        if (dtos.size() != num)
                            throw new BusinessException("msg","更新应收账款明细信息失败!");
                        dtos = new ArrayList<>();
                    }
                }

                if (!CollectionUtils.isEmpty(dtos)){
                    num = yszkmxService.insertList(dtos);
                    if (dtos.size() != num)
                        throw new BusinessException("msg","更新应收账款明细信息失败!");
                }
            }
        }
        return true;

    }

    public boolean creatHSSFWorkbook(String wjlj,SwyszkDto swyszkDto,String gsmc) throws IOException, BusinessException {
        @SuppressWarnings("resource")
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(wjlj));
        int numberOfSheets = workbook.getNumberOfSheets();
        Boolean success;
        List<SwyszkDto> swyszkDtos = new ArrayList<>();
        BigDecimal countJsje = new BigDecimal(0d);
        BigDecimal countSfje = new BigDecimal(0d);
        BigDecimal countKdfy = new BigDecimal(0d);
        List<SjjcxmDto> list = new ArrayList<>();
        List<FjsqDto> fjsqDtos = new ArrayList<>();
        List<YszkmxDto> yszkmxDtos = new ArrayList<>();
        boolean flag = true;
        String dcbj = null;
        String hbmc = null;
        for (int i = 0; i < numberOfSheets; i++) {
            HSSFSheet sheet =workbook.getSheetAt(i);
            int sjidInt = -1;
            int fjidInt = -1;
            int sjjcxmInt = -1;
            int sjjczxmInt = -1;
            int xmglidInt = -1;
            int hzxmInt = -1;
            int bzInt = -1;
            int sfjeInt = -1;
            int jsjeInt = -1;
            int kdfyInt = -1;
            boolean sheetflg=false;
            if (null != sheet.getRow(3)){
                for (int j = 0; j < sheet.getRow(3).getPhysicalNumberOfCells(); j++) {
                    if (null != sheet.getRow(3).getCell(j)){
                        String value = sheet.getRow(3).getCell(j).toString();
                        if("送检ID".equals(value)){
                            sjidInt = j;
                            sheetflg=true;
                        }else if ("复检ID".equals(value)){
                            fjidInt = j;
                        }else if ("检测项目ID".equals(value)){
                            sjjcxmInt = j;
                        }else if ("检测子项目ID".equals(value)){
                            sjjczxmInt = j;
                        }else if ("项目管理ID".equals(value)){
                            xmglidInt = j;
                        }else if ("患者姓名".equals(value)){
                            hzxmInt = j;
                        }else if ("备注".equals(value)){
                            bzInt = j;
                        }else if ("结算金额(元)".equals(value)){
                            jsjeInt = j;
                        }else if ("实付金额".equals(value)){
                            sfjeInt = j;
                        }else if ("快递费用".equals(value)){
                            kdfyInt = j;
                        }
                    }
                }
            }
            if(!sheetflg)//如果该sheet页没有读取到送检ID字段，择说明是自己添加的sheet，择不保存数据
                break;
            for (int j = 4; j < sheet.getPhysicalNumberOfRows(); j++) {
                if (null != sheet.getRow(j).getCell(0) && StringUtil.isNotBlank(sheet.getRow(j).getCell(0).toString()) && "合计".equals(sheet.getRow(j).getCell(0).toString())){
                    if (-1 != sfjeInt && null != sheet.getRow(j).getCell(sfjeInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(sfjeInt).toString())){
                        try {
                            countSfje = countSfje.add(new BigDecimal( Double.parseDouble(sheet.getRow(j).getCell(sfjeInt).toString())));
                        } catch (NumberFormatException e) {
                            try {
                                countSfje = countSfje.add(new BigDecimal( sheet.getRow(j).getCell(sfjeInt).getNumericCellValue()));
                            } catch (Exception ex) {
                                countSfje = countSfje.add(BigDecimal.ZERO);
                            }
                        }
                    }
                    if (-1 != jsjeInt && null != sheet.getRow(j).getCell(jsjeInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(jsjeInt).toString())){
                        try {
                            countJsje = countJsje.add(new BigDecimal( Double.parseDouble(sheet.getRow(j).getCell(jsjeInt).toString())));
                        } catch (NumberFormatException e) {
                            try {
                                countJsje = countJsje.add(new BigDecimal( sheet.getRow(j).getCell(jsjeInt).getNumericCellValue()));
                            } catch (Exception ex) {
                                countJsje = countJsje.add(BigDecimal.ZERO);
                            }
                        }
                    }
                    if (-1 != kdfyInt && null != sheet.getRow(j).getCell(kdfyInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(kdfyInt).toString())){
                        try {
                            countKdfy = countKdfy.add(new BigDecimal( Double.parseDouble(sheet.getRow(j).getCell(kdfyInt).toString())));
                        } catch (NumberFormatException e) {
                            try {
                                countKdfy = countKdfy.add(new BigDecimal( sheet.getRow(j).getCell(kdfyInt).getNumericCellValue()));
                            } catch (Exception ex) {
                                countKdfy = countKdfy.add(BigDecimal.ZERO);
                            }
                        }
                    }
                    break;
                }
                SjjcxmDto sjjcxmDto = new SjjcxmDto();
                YszkmxDto yszkmxDto = new YszkmxDto();
                yszkmxDto.setYszkid(swyszkDto.getYszkid());
                yszkmxDto.setYsmxid(StringUtil.generateUUID());
                yszkmxDto.setLrry(swyszkDto.getLrry());
                if (-1 != sfjeInt && null != sheet.getRow(j).getCell(sfjeInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(sfjeInt).toString()))
                    yszkmxDto.setSfje(sheet.getRow(j).getCell(sfjeInt).toString());
                if (-1 != kdfyInt && null != sheet.getRow(j).getCell(kdfyInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(kdfyInt).toString())){
                    try {
                        yszkmxDto.setKdfy(String.valueOf(Double.parseDouble(sheet.getRow(j).getCell(kdfyInt).toString())));
                    } catch (Exception e) {
                        try {
                            yszkmxDto.setKdfy(String.valueOf(sheet.getRow(j).getCell(kdfyInt).getNumericCellValue()));
                        } catch (Exception ex) {
                            yszkmxDto.setKdfy("0");
                        }
                    }
                }
                if (-1 != xmglidInt && null != sheet.getRow(j).getCell(xmglidInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(xmglidInt).toString()))
                    yszkmxDto.setXmglid(sheet.getRow(j).getCell(xmglidInt).toString());
                if (-1 != hzxmInt && null != sheet.getRow(j).getCell(hzxmInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(hzxmInt).toString()))
                    yszkmxDto.setMc(sheet.getRow(j).getCell(hzxmInt).toString());
                if (-1 != bzInt && null != sheet.getRow(j).getCell(bzInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(bzInt).toString()))
                    yszkmxDto.setBz(sheet.getRow(j).getCell(bzInt).toString());
                FjsqDto fjsqDto = new FjsqDto();
                if (-1 != sjidInt && null != sheet.getRow(j).getCell(sjidInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(sjidInt).toString())){
                    sjjcxmDto.setSjid(sheet.getRow(j).getCell(sjidInt).toString());
                    yszkmxDto.setYwid(sheet.getRow(j).getCell(sjidInt).toString());
                    yszkmxDto.setLx(DetectionTypeEnum.DETECT_SJ.getCode());
                }
                if (-1 != fjidInt && null != sheet.getRow(j).getCell(fjidInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(fjidInt).toString())){
                    fjsqDto.setFjid(sheet.getRow(j).getCell(fjidInt).toString());
                    yszkmxDto.setYwid(sheet.getRow(j).getCell(fjidInt).toString());
                    yszkmxDto.setLx(DetectionTypeEnum.DETECT_FJ.getCode());
                }
                if (-1 != sjjcxmInt && null != sheet.getRow(j).getCell(sjjcxmInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(sjjcxmInt).toString())) {
                    sjjcxmDto.setJcxmid(sheet.getRow(j).getCell(sjjcxmInt).toString());
                    fjsqDto.setJcxm(sheet.getRow(j).getCell(sjjcxmInt).toString());
                    yszkmxDto.setJcxmid(sheet.getRow(j).getCell(sjjcxmInt).toString());
                }
                if (-1 != sjjczxmInt && null != sheet.getRow(j).getCell(sjjczxmInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(sjjczxmInt).toString())) {
                    sjjcxmDto.setJczxmid(sheet.getRow(j).getCell(sjjczxmInt).toString());
                    fjsqDto.setJczxm(sheet.getRow(j).getCell(sjjczxmInt).toString());
                    yszkmxDto.setJczxmid(sheet.getRow(j).getCell(sjjczxmInt).toString());
                }
                if (-1 != jsjeInt && null != sheet.getRow(j).getCell(jsjeInt) && StringUtil.isNotBlank(sheet.getRow(j).getCell(jsjeInt).toString())){
                    sjjcxmDto.setDzje(sheet.getRow(j).getCell(jsjeInt).toString());
                    sjjcxmDto.setSfdz("1");
                    fjsqDto.setDzje(sheet.getRow(j).getCell(jsjeInt).toString());
                    fjsqDto.setSfdz("1");
                    yszkmxDto.setJsje(sheet.getRow(j).getCell(jsjeInt).toString());
                }else{
                    sjjcxmDto.setSfdz("0");
                    fjsqDto.setSfdz("0");
                }
                if(StringUtil.isBlank(yszkmxDto.getLx())){//若匹配不到送检或者复检，可能是手动添加，择设置类型为OTHER
                    yszkmxDto.setLx(DetectionTypeEnum.OTHER.getCode());
                }
                if (StringUtil.isNotBlank(sjjcxmDto.getSjid()) && StringUtil.isNotBlank(sjjcxmDto.getJcxmid()) && flag && StringUtil.isBlank(fjsqDto.getFjid()) && !"2".equals(swyszkDto.getFlag())){
                    flag = false;
                    SjjcxmDto dto = sjjcxmService.getDto(sjjcxmDto);
                    if (dto != null && StringUtil.isNotBlank(dto.getYszkid())){
                        if (!"1".equals(swyszkDto.getFlag()))
                            return false;
                        dcbj = dto.getDcbj();
                        SwyszkDto swyszkDto1 = new SwyszkDto();
                        swyszkDto1.setScry(swyszkDto.getLrry());
                        swyszkDto1.setYszkid(dto.getYszkid());
                        dao.delete(swyszkDto1);
                        dto.setXgry(swyszkDto.getLrry());
                        sjjcxmService.updateYszkInfoToNull(dto);
                    }
                }
                sjjcxmDto.setDcbj(dcbj);
                sjjcxmDto.setYszkid(swyszkDto.getYszkid());
                fjsqDto.setDcbj(dcbj);
                fjsqDto.setYszkid(swyszkDto.getYszkid());
                yszkmxDtos.add(yszkmxDto);
                if (StringUtil.isNotBlank(fjsqDto.getFjid())){
                    fjsqDtos.add(fjsqDto);
                }else {
                    list.add(sjjcxmDto);
                }
            }
        }
        if (StringUtil.isNotBlank(gsmc)){
            SwkhglDto swkhglDto = new SwkhglDto();
            swkhglDto.setGsmc(gsmc);
            SwkhglDto swkhglDto1 = swkhglService.getDto(swkhglDto);
            if (null != swkhglDto1){
                swyszkDto.setDzkh(swkhglDto1.getKhid());
                swyszkDto.setHkzq(swkhglDto1.getHkzq());
                swyszkDto.setHkzt(swkhglDto1.getKhid());
                HbhzkhDto hbhzkhDto = new HbhzkhDto();
                hbhzkhDto.setKhid(swkhglDto1.getKhid());
                //获取对账单中第一条数据的伙伴信息
                if(StringUtil.isNotBlank(yszkmxDtos.get(0).getYwid())){
                    SjxxDto sjxxDto=new SjxxDto();
                    sjxxDto.setSjid(yszkmxDtos.get(0).getYwid());
                    sjxxDto=sjxxService.getDto(sjxxDto);
                    hbhzkhDto.setHbid(sjxxDto.getHbid());
                }
                HbhzkhDto sfzykh=hbhzkhService.getDto(hbhzkhDto);
                if(sfzykh!=null && "1".equals(sfzykh.getZykh())){//若为主要客户，明细批量设置为1
                    yszkmxDtos.forEach(yszkmxDto -> yszkmxDto.setSfzykh("1"));
                }else{
                    yszkmxDtos.forEach(yszkmxDto -> yszkmxDto.setSfzykh("0"));
                }
                List<HbhzkhDto> dtoList = hbhzkhService.getDtoList(hbhzkhDto);
                if (!CollectionUtils.isEmpty(dtoList)){
                    swyszkDto.setQy(dtoList.get(0).getGwmc());
                    swyszkDto.setFl(dtoList.get(0).getFl());
                    swyszkDto.setDjxs(dtoList.get(0).getYhid());//用来存销售负责人
                    hbmc = dtoList.get(0).getHbmc();
                }
                swyszkDto.setSfje(countSfje.toString());
                swyszkDto.setKdfy(countKdfy.toString());
                swyszkDto.setJsje(countJsje.toString());
                swyszkDto.setSfyq("0");
                // 结算金额大于实付金额
                if (countJsje.add(countKdfy).compareTo(countSfje) > 0){
                    swyszkDto.setWhkje(countJsje.add(countKdfy).subtract(countSfje).toString());
                    swyszkDto.setWfkje("0");
                }else{
                    swyszkDto.setDkrq(swyszkDto.getLsdkrq());
                    swyszkDto.setWfkje(countSfje.subtract(countJsje).subtract(countKdfy).toString());
                    swyszkDto.setWhkje("0");
                }
                if(new BigDecimal(swyszkDto.getWhkje()).compareTo(BigDecimal.ZERO)==0 && new BigDecimal(swyszkDto.getWfkje()).compareTo(BigDecimal.ZERO)==0){
                    swyszkDto.setSfjq("1");
                }else{
                    swyszkDto.setSfjq("0");
                }
                swyszkDtos.add(swyszkDto);
            }else{
                throw new BusinessException("msg","匹配客户失败，请检查文件名是否规范或者该客户是否存在！");
            }
        }
        if (StringUtil.isNotBlank(dcbj) && StringUtil.isNotBlank(hbmc)){
            SjjcxmDto sjjcxmDto = new SjjcxmDto();
            sjjcxmDto.setDcbj(dcbj);
            sjjcxmDto.setHbmc(hbmc);
            success = sjjcxmService.updateDcEmpty(sjjcxmDto);
            if (!success)
                throw new BusinessException("msg","未找到数据！");
        }
        if (!CollectionUtils.isEmpty(list)){
            if (list.size() <= 100){
                sjjcxmService.updateListDzInfo(list);
            }else{
                List<SjjcxmDto> dtos = new ArrayList<>();
                for (SjjcxmDto sjjcxmDto : list) {
                    dtos.add(sjjcxmDto);
                    if (dtos.size() == 100){
                        sjjcxmService.updateListDzInfo(dtos);
                        dtos = new ArrayList<>();
                    }
                }

                if (!CollectionUtils.isEmpty(dtos)){
                    sjjcxmService.updateListDzInfo(dtos);
                }
            }
        }
        if (!CollectionUtils.isEmpty(fjsqDtos)){
            if (fjsqDtos.size() <= 100){
                fjsqService.updateListDzInfo(fjsqDtos);
            }else{
                List<FjsqDto> dtos = new ArrayList<>();
                for (FjsqDto fjsqDto : fjsqDtos) {
                    dtos.add(fjsqDto);
                    if (dtos.size() == 100){
                        fjsqService.updateListDzInfo(dtos);
                        dtos = new ArrayList<>();
                    }
                }

                if (!CollectionUtils.isEmpty(dtos)){
                    fjsqService.updateListDzInfo(dtos);
                }
            }
        }
        if (!CollectionUtils.isEmpty(swyszkDtos)){
            success = dao.insertList(swyszkDtos);
            if (!success)
                throw new BusinessException("msg","更新应收账款信息失败！");
        }
        if (!CollectionUtils.isEmpty(yszkmxDtos)){
            int num;
            if (yszkmxDtos.size() <= 100){
                num = yszkmxService.insertList(yszkmxDtos);
                if (yszkmxDtos.size() != num)
                    throw new BusinessException("msg","更新应收账款明细信息失败!");
            }else{
                List<YszkmxDto> dtos = new ArrayList<>();
                for (YszkmxDto yszkmxDto : yszkmxDtos) {
                    dtos.add(yszkmxDto);
                    if (dtos.size() == 100){
                        num = yszkmxService.insertList(dtos);
                        if (dtos.size() != num)
                            throw new BusinessException("msg","更新应收账款明细信息失败!");
                        dtos = new ArrayList<>();
                    }
                }

                if (!CollectionUtils.isEmpty(dtos)){
                    num = yszkmxService.insertList(dtos);
                    if (dtos.size() != num)
                        throw new BusinessException("msg","更新应收账款明细信息失败!");
                }
            }
        }
        return true;
    }

    /**
     * 修改
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateDto(SwyszkDto swyszkDto){
        int result = dao.update(swyszkDto);
        return result != 0;
    }

    /**
     * 获取未结清数据
     * @return
     */
    public List<SwyszkDto> getOutstandingData(){
        return dao.getOutstandingData();
    }

    /**
     * 修改是否逾期字段
     */
    public Boolean updateSfyq(SwyszkDto swyszkDto){
        return dao.updateSfyq(swyszkDto);
    }

    @Override
    public Boolean updateSfjs(SwyszkDto swyszkDto) {
        return dao.updateSfjs(swyszkDto);
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, SwyszkDto swyszkDto)
    {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuffer sqlParam = new StringBuffer();
        for (DcszDto dcszDto : choseList)
        {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;
            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd()))
            {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        swyszkDto.setSqlParam(sqlParam.toString());
    }


    /**
     * 选中导出
     * @param params
     * @return
     */
    public List<SwyszkDto> getYskListForSelectExp(Map<String, Object> params){
        SwyszkDto swyszkDto = (SwyszkDto) params.get("entryData");
        queryJoinFlagExport(params,swyszkDto);
        return dao.getYskListForSelectExp(swyszkDto);
    }

    /**
     * 根据搜索条件获取导出条数
     * @param swyszkDto
     * @return
     */
    public int getYskCountForSearchExp(SwyszkDto swyszkDto,Map<String,Object> params){
        Object xtsz=redisUtil.hget("matridx_xtsz","business.receipts.judge.time");
        JSONObject job=JSONObject.parseObject(String.valueOf(xtsz));
        swyszkDto.setSzz(String.valueOf(job.getString("szz")));
        return dao.getYskCountForSearchExp(swyszkDto);
    }

    /**
     * 根据搜索条件分页获取导出信息
     * @param params
     * @return
     */
    public List<SwyszkDto> getYskListForSearchExp(Map<String,Object> params){
        SwyszkDto swyszkDto = (SwyszkDto)params.get("entryData");
        queryJoinFlagExport(params,swyszkDto);
        Object xtsz=redisUtil.hget("matridx_xtsz","business.receipts.judge.time");
        JSONObject job=JSONObject.parseObject(String.valueOf(xtsz));
        swyszkDto.setSzz(String.valueOf(job.getString("szz")));
        return dao.getYskListForSearchExp(swyszkDto);
    }
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Boolean delInfo(SwyszkDto swyszkDto) throws BusinessException {
        boolean success = dao.delete(swyszkDto)!=0;
        if (!success)
            throw new BusinessException("msg","删除主表信息失败！");
        YszkmxDto yszkmxDto = new YszkmxDto();
        yszkmxDto.setYszkid(swyszkDto.getYszkid());
        yszkmxDto.setIds(swyszkDto.getIds());
        yszkmxDto.setScry(swyszkDto.getScry());
        success = yszkmxService.delete(yszkmxDto);
        if (!success)
            throw new BusinessException("msg","删除明细信息失败！");
        SjjcxmDto sjjcxmDto = new SjjcxmDto();
        sjjcxmDto.setXgry(swyszkDto.getScry());
        sjjcxmDto.setIds(swyszkDto.getIds());
        sjjcxmService.updateYszkInfoToNull(sjjcxmDto);
        return true;
    }

    /**
     * 修改数量字段
     */
    public boolean updateAmount(SwyszkDto swyszkDto){
        return dao.updateAmount(swyszkDto);
    }
    /**
     * 收款维护
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean collectionReceivableCredit(SwyszkDto swyszkDto){
        SwyszkDto swyszkDto_t = dao.getDto(swyszkDto);
        SwskglDto swskglDto=new SwskglDto();
        swskglDto.setSwskglid(StringUtil.generateUUID());
        swskglDto.setLrry(swyszkDto.getXgry());
        swskglDto.setYszkid(swyszkDto.getYszkid());
        swskglDto.setSkje(swyszkDto.getHkje());
        swskglDto.setSkrq(swyszkDto.getDkrq());
        swskglDto.setBz(swyszkDto.getBz());
        Object xtsz=redisUtil.hget("matridx_xtsz","business.receipts.judge.time");
        if(swyszkDto_t!=null&&StringUtil.isNotBlank(swyszkDto_t.getKprq())&&xtsz!=null){
            int hkzq=0;
            if(StringUtil.isNotBlank(swyszkDto_t.getKhhkzq())){
                hkzq=Integer.parseInt(swyszkDto_t.getKhhkzq());
            }
            JSONObject job=JSONObject.parseObject(String.valueOf(xtsz));
            String[] split = job.getString("szz").split(",");
            int first=Integer.parseInt(split[0]);
            int second=Integer.parseInt(split[1]);
            int third=Integer.parseInt(split[2]);
            DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
            Date dkrq = null;//到款日期
            Date kprq = null;//开票日期
            try {
                dkrq = dft.parse(swyszkDto.getDkrq());
                kprq=dft.parse(swyszkDto_t.getKprq());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Long starTime=kprq.getTime();
            Long endTime=dkrq.getTime();
            Long num=endTime-starTime;//时间戳相差的毫秒数
            int day = (int) (num / 24 / 60 / 60 / 1000);//相差天数
            if(day<=(first+hkzq)){
                swskglDto.setZt("00");
            }else if(day<=(second+hkzq)){
                swskglDto.setZt("10");
            }else if(day<=(third+hkzq)){
                swskglDto.setZt("20");
            }else{
                swskglDto.setZt("30");
            }
        }else{
            swskglDto.setZt("00");
        }
        swskglService.insert(swskglDto);
        String totalAmount = swskglService.getTotalAmount(swyszkDto.getYszkid());
        BigDecimal hkzje=new BigDecimal(totalAmount);
        BigDecimal jsje=new BigDecimal("0");
        BigDecimal kdfy=new BigDecimal("0");
        BigDecimal wfkje=new BigDecimal("0");
        BigDecimal sfje =new BigDecimal("0");
        if(StringUtil.isNotBlank(swyszkDto_t.getSfje())){
            sfje=new BigDecimal(swyszkDto_t.getSfje());
        }
        if(StringUtil.isNotBlank(swyszkDto_t.getJsje())){
            jsje=new BigDecimal(swyszkDto_t.getJsje());
        }
        if(StringUtil.isNotBlank(swyszkDto_t.getKdfy())){
            kdfy=new BigDecimal(swyszkDto_t.getKdfy());
        }
        if(StringUtil.isNotBlank(swyszkDto_t.getWfkje())){
            wfkje=new BigDecimal(swyszkDto_t.getWfkje());
        }
        BigDecimal whkje = jsje.add(kdfy).subtract(hkzje).subtract(sfje);
        if(whkje.compareTo(BigDecimal.ZERO) < 0)//若计算后未回款金额小于0，则当作0来进行保存
            whkje =new BigDecimal("0");
        swyszkDto.setWhkje(String.valueOf(whkje));
        swyszkDto.setHkje(String.valueOf(hkzje));
        if(wfkje.compareTo(BigDecimal.ZERO)==0&&whkje.compareTo(BigDecimal.ZERO)==0){
            swyszkDto.setSfjq("1");
        }
        int num = dao.update(swyszkDto);
        if(num==0){
            return false;
        }
        List<YszkmxDto> list = (List<YszkmxDto>) JSON.parseArray(swyszkDto.getXsmx_json(), YszkmxDto.class);
        if(!CollectionUtils.isEmpty(list)){
            for(YszkmxDto yszkmxDto:list){
                yszkmxDto.setYsmxid(StringUtil.generateUUID());
                yszkmxDto.setYszkid(swyszkDto.getYszkid());
                yszkmxDto.setYwid(yszkmxDto.getXsid());
                yszkmxDto.setMc(yszkmxDto.getOaxsdh());
                yszkmxDto.setSfje(yszkmxDto.getDkje());
                yszkmxDto.setLrry(swyszkDto.getXgry());
                yszkmxDto.setLx("SALE_ORDER");
            }
            num = yszkmxService.insertList(list);
            return num != 0;
        }
        return true;
    }

    /**
     * 逾期通知
     * @param swyszkDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean latenoticeSaveReceivableCredit(SwyszkDto swyszkDto){
        List<SwyszkDto> list = (List<SwyszkDto>) JSON.parseArray(swyszkDto.getYszk_json(), SwyszkDto.class);
        if(!CollectionUtils.isEmpty(list)){
            Map<String, List<SwyszkDto>> listMap = list.stream().collect(Collectors.groupingBy(SwyszkDto::getDzkh));
            if (null != listMap && listMap.size()>0){
                Iterator<Map.Entry<String, List<SwyszkDto>>> entries = listMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String,  List<SwyszkDto>> entry = entries.next();
                    List<SwyszkDto> resultModelList = entry.getValue();
                    if (!CollectionUtils.isEmpty(resultModelList)){
                        String dzzq="";
                        Map<String, List<SwyszkDto>> newMap = resultModelList.stream().collect(Collectors.groupingBy(SwyszkDto::getZdzq));
                        if (null != newMap && newMap.size()>0){
                            Iterator<Map.Entry<String, List<SwyszkDto>>> params = newMap.entrySet().iterator();
                            while (params.hasNext()) {
                                Map.Entry<String,  List<SwyszkDto>> param = params.next();
                                String key = param.getKey();
                                dzzq+=","+key;
                            }
                        }
                        if(StringUtil.isNotBlank(dzzq)){
                            dzzq=dzzq.substring(1);
                        }
                        //邮箱存在，发送邮箱消息
                        if (StringUtils.isNotBlank(resultModelList.get(0).getFzryx())) {
                            log.error("应收账款逾期通知邮件开始发送 -- 客户："+resultModelList.get(0).getDzkhmc()+" -- 邮箱为[ "+resultModelList.get(0).getFzryx()+" ]");
                            List<String> yxlist = new ArrayList<String>();
                            yxlist = StringUtil.splitMsg(yxlist, resultModelList.get(0).getFzryx(), "，|,");
                            try {
                                emailUtil.sendEmail(yxlist, xxglService.getMsg("ICOMM_SW00003"), xxglService.getMsg("ICOMM_SW00004", resultModelList.get(0).getDzkhmc()
                                        , dzzq));
                            } catch (Exception e) {
                                log.error("商务应收账款逾期通知邮件 -- 发送失败-- 客户："+resultModelList.get(0).getDzkhmc()+" -- 邮箱为[ "+resultModelList.get(0).getFzryx()+" ]");
                                return false;
                            }
                        }
                    }
                }
            }
            List<String> ids=new ArrayList<>();
            for(SwyszkDto dto:list){
                ids.add(dto.getYszkid());
            }
            swyszkDto.setIds(ids);
            Boolean updateYqtzsj = dao.updateYqtzsj(swyszkDto);
            if(!updateYqtzsj){
                return false;
            }
        }
        return true;
    }

    /**
     * 批量修改
     */
    public boolean updateInvoiceList(List<SwyszkDto> list){
        return dao.updateInvoiceList(list);
    }

    /**
     * 批量修改
     */
    public boolean updatePayList(List<SwyszkDto> list){
        return dao.updatePayList(list);
    }
    /**
     * 批量修改
     */
    public boolean updateJxhsmxs(List<JxhsmxDto> list) throws BusinessException {
        int i = dao.updateJxhsmxsAddJxje(list);
        boolean isSuccess = (i == list.size());
        if (isSuccess){
            int j = dao.updateJxhsmxsSfhs(list);
            return j == list.size();
        }else {
            throw new BusinessException("更新应收账款失败!");
        }
    }
    /**
     * 批量修改
     */
    public boolean updateJxhsmxsAddJxje(List<JxhsmxDto> list){
        int i = dao.updateJxhsmxsAddJxje(list);
        return i == list.size();
    }
    /**
     * 批量修改
     */
    public boolean updateJxhsmxsSfhs(List<JxhsmxDto> list){
        int i = dao.updateJxhsmxsSfhs(list);
        return i == list.size();
    }

    /**
     * @param swyszkDto
     * @return
     */
    @Override
    public List<SwyszkDto> getPullList(SwyszkDto swyszkDto) {
        return dao.getPullList(swyszkDto);
    }

    /**
     * 获取待核算绩效清单
     * @param swyszkDto
     * @return
     */
    public List<SwyszkDto> getAccountingList(SwyszkDto swyszkDto){
        return dao.getAccountingList(swyszkDto);
    }

     /**
     * 获取考核指标和绩效
     * @param map
     * @return
     */
    public Map<String,String> getCheckTarget(Map<String,String> map){
        return dao.getCheckTarget(map);
    }

     /**
     * 销售收款统计(个人)
     * @param map
     * @return
     */
     public List<Map<String,String>> getSaleReceiptsStatistics(Map<String,String> map){
         return dao.getSaleReceiptsStatistics(map);
     }
}
