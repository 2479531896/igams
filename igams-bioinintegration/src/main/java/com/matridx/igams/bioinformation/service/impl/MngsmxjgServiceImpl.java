package com.matridx.igams.bioinformation.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.bioinformation.dao.entities.*;
import com.matridx.igams.bioinformation.dao.post.IMngsmxjgDao;
import com.matridx.igams.bioinformation.service.svcinterface.*;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.OperateEnum;


import com.matridx.igams.common.enums.PersonalSettingEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MngsmxjgServiceImpl extends BaseBasicServiceImpl<MngsmxjgDto, MngsmxjgModel, IMngsmxjgDao> implements IMngsmxjgService {

    @Autowired
    private IWkcxbbService wkcxbbService;
    @Autowired
    private IWkcxService wkcxService;
    @Autowired
    private INyfxjgService nyfxjgService;
    @Autowired
    private IActionrecordService actionrecordService;
    @Autowired
    private IWklcznService wklcznService;
    @Autowired
    private IWkysmxService wkysmxService;
    @Autowired
    private IWknyfxService wknyfxService;
    @Autowired
    private IWkdlfxService wkdlfxService;
    @Autowired
    IGrszService grszService;

    @Override
    public Integer existsTable(String tableName) {
        return dao.existsTable(tableName);
    }

    @Override
    public Integer updateWzxx(MngsmxjgDto mngsmxjgDto) {
        return dao.updateWzxx(mngsmxjgDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean reviewAudit(List<MngsmxjgDto> mngsmxjgList, WkcxDto wkcxDto, User user) throws BusinessException {
        //保存个人设置的审核人
        if(StringUtil.isNotBlank(wkcxDto.getShryid())){
            GrszDto grszDto=new GrszDto();
            grszDto.setYhid(user.getYhid());
            grszDto.setSzlb(PersonalSettingEnum.BIO_AUDIT.getCode());
            grszService.delete(grszDto);
            grszDto.setSzid(StringUtil.generateUUID());
            grszDto.setSzz(wkcxDto.getShryid());
            grszService.insert(grszDto);
        }
        //保存个人设置的检验人
        if(StringUtil.isNotBlank(wkcxDto.getJyryid())){
            GrszDto grszDto=new GrszDto();
            grszDto.setYhid(user.getYhid());
            grszDto.setSzlb(PersonalSettingEnum.BIO_INSPECT.getCode());
            grszService.delete(grszDto);
            grszDto.setSzid(StringUtil.generateUUID());
            grszDto.setSzz(wkcxDto.getJyryid());
            grszService.insert(grszDto);
        }
        if (StringUtil.isBlank(wkcxDto.getShbbbh()) || StringUtil.isBlank(wkcxDto.getSffsbg()))
            throw new BusinessException("msg","未获取到文库信息！");
        wkcxDto.setXgry(user.getYhid());
        wkcxDto.setDqshry(user.getYhid());
        //更新文库测序信息
        boolean success = wkcxService.update(wkcxDto);
        if (!success)
            throw new BusinessException("msg","更新文库失败！");
        //获取文库测序版本
        WkcxbbDto wkcxbbDto = new WkcxbbDto();
        wkcxbbDto.setBbh(wkcxDto.getShbbbh());
        wkcxbbDto.setWkcxid(wkcxDto.getWkcxid());
        WkcxbbDto dto = wkcxbbService.getDto(wkcxbbDto);
        //判断版本是否存在
        if (null == dto)
            throw new BusinessException("msg","未获取到当前版本！");
        
        MngsmxjgDto updateDto =new MngsmxjgDto();
        updateDto.setBbh(wkcxDto.getShbbbh());
        updateDto.setWkbh(wkcxDto.getWkbh());
        updateDto.setWkcxid(wkcxDto.getWkcxid());
        //获取分表后缀
        updateDto.setLrsj(dto.getLrsj().substring(0,4)+dto.getLrsj().substring(5,7));

        for (MngsmxjgDto mngsmxjgDto : mngsmxjgList) {
            mngsmxjgDto.setLrsj(dto.getLrsj().substring(0,4)+dto.getLrsj().substring(5,7));
            mngsmxjgDto.setXgry(user.getYhid());
            //判断是否是微生物共生并set属性
            if("3".equals(mngsmxjgDto.getGzd())&&"0".equals(mngsmxjgDto.getFl())&&"S".equals(mngsmxjgDto.getFldj())){
                mngsmxjgDto.setSfwswgs("1");
            }
        }
        //更新明细结果信息
        dao.updateList(mngsmxjgList);
        //更新成疑似
        dao.updateGzdToFour(updateDto);
        NyfxjgDto nyfxjgDto = new NyfxjgDto();
        nyfxjgDto.setWkcxid(wkcxDto.getWkcxid());
        nyfxjgDto.setBbh(wkcxDto.getShbbbh());
        nyfxjgDto.setSfbg("1");
        nyfxjgDto.setXgry(user.getYhid());
        //更新耐药分析结果表的是否报告字段
         nyfxjgService.update(nyfxjgDto);
         //新增操作记录到操作记录表
        ActionrecordDto actionrecordDto = new ActionrecordDto();
        actionrecordDto.setCzid(StringUtil.generateUUID());
        actionrecordDto.setWkbh(dto.getWkbh());
        actionrecordDto.setWkcxid(dto.getWkcxid());
        actionrecordDto.setBbh(dto.getBbh());
        actionrecordDto.setLx("mNGS结果");
        actionrecordDto.setJtcz(OperateEnum.SUBMIT.getCode());
        actionrecordDto.setCzyh(user.getYhid());
        success = actionrecordService.insert(actionrecordDto);
        if (!success)
            throw new BusinessException("msg","操作记录表更新失败！");
        //删除原本审核的临床指南数据
        success = wklcznService.deleteById(wkcxDto.getWkcxid());
        if(StringUtil.isNotBlank(wkcxDto.getLczn())){
            if (!success)
                throw new BusinessException("msg","文库临床指南表删除失败！");
            List<WklcznDto> wklcznDtos=new ArrayList<>();
            String[] split = wkcxDto.getLczn().split(",");
            for(String s:split){
                WklcznDto wklcznDto=new WklcznDto();
                wklcznDto.setWkcxid(wkcxDto.getWkcxid());
                wklcznDto.setZnid(s);
                wklcznDtos.add(wklcznDto);
            }
            //新增审核的临床指南数据
            success = wklcznService.insertList(wklcznDtos);
            if (!success)
                throw new BusinessException("msg","文库临床指南表新增失败！");
        }
        //删除原本审核的文库疑似数据
        wkysmxService.deleteById(wkcxDto.getWkcxid());
        if(wkcxDto.getYsmx()!=null&&wkcxDto.getYsmx().size()>0){
            if (!success)
                throw new BusinessException("msg","文库疑似明细表删除失败！");
            List<WkysmxDto> wkysmxDtos=new ArrayList<>();
            for(String s:wkcxDto.getYsmx()){
                WkysmxDto wkysmxDto=new WkysmxDto();
                wkysmxDto.setWkcxid(wkcxDto.getWkcxid());
                wkysmxDto.setMxjgid(s);
                wkysmxDtos.add(wkysmxDto);
            }
            //新增审核的文库疑似数据
            success = wkysmxService.insertList(wkysmxDtos);
            if (!success)
                throw new BusinessException("msg","文库疑似明细表新增失败！");
        }
        //删除原本审核的文库耐药分析数据
        wknyfxService.deleteById(wkcxDto.getWkcxid());
        if(wkcxDto.getNyfx()!=null&&wkcxDto.getNyfx().size()>0){
            if (!success)
                throw new BusinessException("msg","文库疑似明细表删除失败！");
            List<WknyfxDto> wknyfxDtos=new ArrayList<>();
            for(String s:wkcxDto.getNyfx()){
                WknyfxDto wknyfxDto=new WknyfxDto();
                wknyfxDto.setWkcxid(wkcxDto.getWkcxid());
                wknyfxDto.setNyjgid(s);
                wknyfxDtos.add(wknyfxDto);
            }
            //新增审核的文库耐药分析数据
            success = wknyfxService.insertList(wknyfxDtos);
            if (!success)
                throw new BusinessException("msg","文库耐药分析表新增失败！");
        }
        //删除原本审核的文库毒力因子数据
        wkdlfxService.deleteById(wkcxDto.getWkcxid());
        if(wkcxDto.getDlfx()!=null&&wkcxDto.getDlfx().size()>0){
            if (!success)
                throw new BusinessException("msg","文库疑似明细表删除失败！");
            List<WkdlfxDto> wkdlfxDtos=new ArrayList<>();
            for(String s:wkcxDto.getDlfx()){
                WkdlfxDto wkdlfxDto=new WkdlfxDto();
                wkdlfxDto.setWkcxid(wkcxDto.getWkcxid());
                wkdlfxDto.setDlfxid(s);
                wkdlfxDtos.add(wkdlfxDto);
            }
            //新增审核的文库毒力因子数据
            success = wkdlfxService.insertList(wkdlfxDtos);
            if (!success)
                throw new BusinessException("msg","文库毒力分析表新增失败！");
        }
        return true;
    }

    @Override
    public Integer deleteByNull(MngsmxjgDto mngsmxjgDto) {
        return dao.deleteByNull(mngsmxjgDto);
    }

    @Override
    public List<MngsmxjgDto> getDtoListGroupBy(MngsmxjgDto mngsmxjgDto) {
        return dao.getDtoListGroupBy(mngsmxjgDto);
    }

    @Override
    public MngsmxjgDto getDtoNull(MngsmxjgDto mngsmxjgDto) {
        return dao.getDtoNull(mngsmxjgDto);
    }

    @Override
    public Boolean updateListLs(List<MngsmxjgDto> mngsmxjgList) {
        return dao.updateListLs(mngsmxjgList) != 0;
    }

    @Override
    public List<MngsmxjgDto> getDtoListLs(MngsmxjgDto mngsmxjgDto) {
        return dao.getDtoListLs(mngsmxjgDto);
    }

    @Override
    public List<MngsmxjgDto> getDtoListLsCs(MngsmxjgDto mngsmxjgDto) {
        return dao.getDtoListLsCs(mngsmxjgDto);
    }


    public List<MngsmxjgDto> findBottom (MngsmxjgDto mngsmxjgDto){
        List<MngsmxjgDto> dtoListLsCs = dao.getDtoListLsCs(mngsmxjgDto);
        List<MngsmxjgDto> strings = new ArrayList<>();
        List<String> stringList = new ArrayList<>();
        if (null != dtoListLsCs && dtoListLsCs.size()>0){
            for (MngsmxjgDto dtoListLsC : dtoListLsCs) {

                if (StringUtil.isNotBlank(dtoListLsC.getScid())) {
                    stringList.add(dtoListLsC.getScid());
                }

            }
            for (MngsmxjgDto dtoListLsC : dtoListLsCs) {
                if (!stringList.contains(dtoListLsC.getTaxid())) {
                    if ("S".equals(dtoListLsC.getFldj())) {
                        strings.add(dtoListLsC);
                    } else {
                        if (Double.parseDouble(dtoListLsC.getReadscount()) > 0 || "pathogenic".equals(dtoListLsC.getZbx()) || "1".equals(dtoListLsC.getAijg()) || "2".equals(dtoListLsC.getAijg()) || "3".equals(dtoListLsC.getAijg())) {
                            strings.add(dtoListLsC);
                        }
                    }
                } else {

                    if (Double.parseDouble(dtoListLsC.getReadscount()) > 0 || "pathogenic".equals(dtoListLsC.getZbx()) || "1".equals(dtoListLsC.getAijg()) || "2".equals(dtoListLsC.getAijg()) || "3".equals(dtoListLsC.getAijg())) {

                        strings.add(dtoListLsC);
                    }
                }
            }
        }
        return strings;
    }

    public List<MngsmxjgDto> findFilteredBottom (String jgid,MngsmxjgDto mngsmxjgDto,WkcxDto dto,String type){

        List<MngsmxjgDto> list_f = findBottom(mngsmxjgDto);
        mngsmxjgDto.setJgid(dto.getJgid());
        if(type.equals("EF")){
            mngsmxjgDto.setList(new ArrayList<>());
        }

        List<MngsmxjgDto> list_db = findBottom(mngsmxjgDto);
        mngsmxjgDto.setList(mngsmxjgDto.getList1());
        List<MngsmxjgDto> list_remove = new ArrayList<>();
        if (null!= list_f && list_f.size()>0 && null!= list_db && list_db.size()>0){
            for (MngsmxjgDto h : list_db) {
                for (int i = list_f.size()-1; i >=0; i--) {
                    if (list_f.get(i).getTaxid().equals(h.getTaxid())){
                        if ((Double.parseDouble(h.getRpq())*5 > Double.parseDouble(list_f.get(i).getRpq()))){
                            if (Double.parseDouble(list_f.get(i).getReadscount()) > 0&&list_f.get(i).getTaxid().equals(list_f.get(i).getFtaxid())){
                                list_remove.add(list_f.get(i));
                            }
                            if((list_f.get(i).getFldj().equals("S")&&(Double.parseDouble(h.getRpq())*5 > Double.parseDouble(list_f.get(i).getRpq())))){
                                list_f.remove(i);
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (null!= list_f && list_f.size()==0){
            MngsmxjgDto dto1 = new MngsmxjgDto();
            dto1.setMxjgid("1");
            list_f.add(dto1);
        }
        mngsmxjgDto.setJgid(jgid);
        mngsmxjgDto.setList(list_f);
        List<MngsmxjgDto> list = dao.getDtoListLsCs(mngsmxjgDto);
        mngsmxjgDto.setList(list_remove);
        if(type.equals("EF")){
            mngsmxjgDto.setFl(null);
        }
        List<MngsmxjgDto> high_Concern_new = dao.getDtoListLsCs(mngsmxjgDto);
        for(MngsmxjgDto dto1:list){
            for (int i = high_Concern_new.size()-1; i >=0; i--) {
                if(dto1.getTaxid().equals(high_Concern_new.get(i).getTaxid())){
                    high_Concern_new.remove(i);
                }
            }
        }
        list.addAll(high_Concern_new);
        return list;
    }



    @Override
    public List<MngsmxjgDto> getListInfo( MngsmxjgDto mngsmxjgDto,WkcxDto wkcxDto) {
        List<MngsmxjgDto> mngsmxjgDtos;
        MngsmxjgDto clonDto=new MngsmxjgDto();
        try {
            clonDto=mngsmxjgDto.clone();
        }catch (Exception e){
            e.printStackTrace();
        }


        WkcxDto wDto = null;
        String jgid = null;
        if ("Filter".equals(wkcxDto.getCz())){

            mngsmxjgDtos = findBottom(clonDto);
            clonDto.setList(mngsmxjgDtos);
            jgid = mngsmxjgDto.getJgid();
            if (StringUtil.isNotBlank(wkcxDto.getWkbh())){
                WkcxDto wkcxDto1 = new WkcxDto();
                wkcxDto1.setXpid(wkcxDto.getXpid());
                if (wkcxDto.getWkbh().contains("-REM")){
                    wkcxDto1.setWkbhlike("NC-6-REM");
                    wDto = wkcxService.getDtoNew(wkcxDto1);
                }else{
                    if (StringUtil.isNotBlank(wkcxDto.getLibccon())){
                        if (Double.parseDouble(wkcxDto.getLibccon()) < 100){
                            if (wkcxDto.getWkbh().contains("DNA")){
                                wkcxDto1.setWkbhlike("NC-DNA-4");
                            }else{
                                wkcxDto1.setWkbhlike("NC-RNA-4");
                            }
                            wDto = wkcxService.getDtoNew(wkcxDto1);
                        }else{
                            if (wkcxDto.getWkbh().contains("DNA")){
                                wkcxDto1.setWkbhlike("NC-DNA-6");
                            }else{
                                wkcxDto1.setWkbhlike("NC-RNA-6");
                            }
                            wDto = wkcxService.getDtoNew(wkcxDto1);
                        }
                    }
                }
            }
        }else{
//            if("01".equals(wkcxDto.getZt())){
//                List<MngsmxjgDto> dtoListLsCs = dao.getDtoListLsCs(mngsmxjgDto);
//                List<MngsmxjgDto> strings = new ArrayList<>();
//                if (null != dtoListLsCs && dtoListLsCs.size()>0){
//                    for (int i = 0; i < dtoListLsCs.size(); i++) {
//                        if ("S".equals(dtoListLsCs.get(i).getFldj())){
//                            strings.add(dtoListLsCs.get(i));
//                        }else {
//                            if (Double.parseDouble(dtoListLsCs.get(i).getReadscount()) >0 || "pathogenic".equals(dtoListLsCs.get(i).getZbx()) || "1".equals(dtoListLsCs.get(i).getAijg())  || "2".equals(dtoListLsCs.get(i).getAijg())  || "3".equals(dtoListLsCs.get(i).getAijg())){
//                                strings.add(dtoListLsCs.get(i));
//                            }
//                        }
//                    }
//                }
//                mngsmxjgDto.setList(strings);
//            }else{
                mngsmxjgDtos = findBottom(mngsmxjgDto);

                mngsmxjgDto.setList(mngsmxjgDtos);
                clonDto.setList(mngsmxjgDtos);
//            }

        }
        if("01".equals(wkcxDto.getZt())){
            mngsmxjgDto.setAijg("1");
            clonDto.setAijg("1");
            clonDto.setAijg1("4");
            clonDto.setAijg2("2");
            mngsmxjgDto.setAijg1("4");
            mngsmxjgDto.setAijg2("2");
            //mngsmxjgDto.setZbx("likely pathogenic");
            //mngsmxjgDto.setZbx1("pathogenic");
        }else{
            mngsmxjgDto.setAijg("1");
            mngsmxjgDto.setAijg1("2");
            clonDto.setAijg("1");
            clonDto.setAijg1("2");
        }

        List<MngsmxjgDto> high_Concern;
        if ("Filter".equals(wkcxDto.getCz()) && null != wDto){

            high_Concern = findFilteredBottom(jgid, mngsmxjgDto, wDto,"Filter");
        }else{
            high_Concern = dao.getDtoListLsCs(mngsmxjgDto);
        }

        mngsmxjgDto.setSfgz(null);
        mngsmxjgDto.setNoai("1");
        mngsmxjgDto.setAijg(null);
        mngsmxjgDto.setAijg2(null);
        mngsmxjgDto.setZbx(null);
        mngsmxjgDto.setZbx1(null);
        mngsmxjgDto.setAijg1(null);
        mngsmxjgDto.setFl("0");
        clonDto.setSfgz(null);
        clonDto.setNoai("1");
        clonDto.setAijg(null);
        clonDto.setAijg2(null);
        clonDto.setZbx(null);
        clonDto.setZbx1(null);
        clonDto.setAijg1(null);
        clonDto.setFl("0");
        if(!"01".equals(wkcxDto.getZt())){
            mngsmxjgDto.setNoai1("2");
            clonDto.setNoai1("2");
        }else{
            mngsmxjgDto.setNoai1("4");
            clonDto.setNoai1("4");
        }

        List<MngsmxjgDto> core;
        List<MngsmxjgDto> remove=new ArrayList<>();
        if (mngsmxjgDto.getList()!=null){
            for(MngsmxjgDto _dto:mngsmxjgDto.getList()){
                for(MngsmxjgDto __dto:high_Concern){
                    if(__dto.getScid()!=null&&__dto.getScid().equals(_dto.getTaxid())){
                        remove.add(_dto);
                    }
                }
            }
        }
        if(remove.size()>0){
            for(MngsmxjgDto _dto:remove){
                mngsmxjgDto.getList().remove(_dto);
                clonDto.getList().remove(_dto);
            }
        }


        if ("Filter".equals(wkcxDto.getCz()) && null != wDto){
            core = findFilteredBottom(jgid, mngsmxjgDto, wDto,"Filter");
        }else{
            core = dao.getDtoListLsCs(mngsmxjgDto);
        }
        mngsmxjgDto.setNoai("1");
        mngsmxjgDto.setAijg(null);
        mngsmxjgDto.setAijg1(null);
        mngsmxjgDto.setFl("1");
        clonDto.setNoai("1");
        clonDto.setAijg(null);
        clonDto.setAijg1(null);
        clonDto.setFl("1");
        List<MngsmxjgDto> extension;
        remove=new ArrayList<>();
        if (mngsmxjgDto.getList()!=null){
            for(MngsmxjgDto _dto:mngsmxjgDto.getList()){
                for(MngsmxjgDto __dto:core){
                    if(__dto.getScid()!=null&&__dto.getScid().equals(_dto.getTaxid())){
                        remove.add(_dto);
                    }
                }
            }
        }
        if(remove.size()>0){
            for(MngsmxjgDto _dto:remove){
                mngsmxjgDto.getList().remove(_dto);
                clonDto.getList().remove(_dto);
            }
        }
        if ("Filter".equals(wkcxDto.getCz()) && null != wDto){
            clonDto.setList1(clonDto.getList());
            extension = findFilteredBottom(jgid, clonDto, wDto,"EF");
        }else{
            extension = dao.getDtoListLsCs(mngsmxjgDto);
        }
        List<MngsmxjgDto> infoList = new ArrayList<>();
        if (null != high_Concern && high_Concern.size()>0){
            findCOV(high_Concern);
            MngsmxjgDto dto = new MngsmxjgDto();
            dto.setDl("high_Concern");
            dto.setChildren(listDispose(high_Concern));
            infoList.add(dto);
        }
        if (null != core && core.size()>0){
            findCOV(core);
            MngsmxjgDto dto = new MngsmxjgDto();
            dto.setDl("core");
            dto.setChildren(listDispose(core));
            infoList.add(dto);
        }
        if (null != extension && extension.size()>0){
            findCOV(extension);
            MngsmxjgDto dto = new MngsmxjgDto();
            dto.setDl("extension");
            dto.setChildren(listDispose(extension));
            infoList.add(dto);
        }

        return infoList;
    }

    @Override
    public List<MngsmxjgDto> getReviewInfo(List<MngsmxjgDto> reviewInfo) {
        List<MngsmxjgDto> infoList = new ArrayList<>();
        findCOV(reviewInfo);
        Map<String, List<MngsmxjgDto>> listMap = reviewInfo.stream().filter(item-> StringUtil.isNotBlank(item.getDl())).collect(Collectors.groupingBy(MngsmxjgDto::getDl));
        if (null != listMap && listMap.size()>0){
            Iterator<Map.Entry<String, List<MngsmxjgDto>>> entries = listMap.entrySet().iterator();
            List<MngsmxjgDto> high_Concern = new ArrayList<>();
            List<MngsmxjgDto> core = new ArrayList<>();
            List<MngsmxjgDto> extension = new ArrayList<>();
            while (entries.hasNext()) {
                Map.Entry<String,  List<MngsmxjgDto>> entry = entries.next();
                MngsmxjgDto dto = new MngsmxjgDto();
                dto.setDl(entry.getKey());
                List<MngsmxjgDto> resultModelList = entry.getValue();
                List<MngsmxjgDto> list = new ArrayList<>();
                Map<String, List<MngsmxjgDto>> map = resultModelList.stream().filter(item-> StringUtil.isNotBlank(item.getWzdl())).collect(Collectors.groupingBy(MngsmxjgDto::getWzdl));
                TreeMap<String, List<MngsmxjgDto>> treemap=map.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey))
                        .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(k1,k2)->k1,TreeMap::new));
                if (treemap.size()>0){
                    Iterator<Map.Entry<String, List<MngsmxjgDto>>> iterator = treemap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String,  List<MngsmxjgDto>> next = iterator.next();
                        MngsmxjgDto model = new MngsmxjgDto();
                        model.setXl(next.getKey());
                        model.setChildren(next.getValue());
                        list.add(model);
                    }
                }
//                dto.setChildren(list);
//                infoList.add(dto);
                if ("high_Concern".equals(entry.getKey())){
                    high_Concern = list;
                }else if ("core".equals(entry.getKey())){
                    core = list;
                }else {
                    extension = list;
                }

            }
            if (high_Concern.size()>0){
                MngsmxjgDto dto = new MngsmxjgDto();
                dto.setDl("high_Concern");
                dto.setChildren(high_Concern);
                for (MngsmxjgDto model : high_Concern) {
                    int size = 0;
                    for (MngsmxjgDto child : model.getChildren()) {
                        if (StringUtil.isBlank(child.getScid())){
                            size++;
                        }
                    }
                    model.setNum(String.valueOf(size));
                }
                infoList.add(dto);
            }
            if (core.size()>0){
                MngsmxjgDto dto = new MngsmxjgDto();
                dto.setDl("core");
                dto.setChildren(core);
                for (MngsmxjgDto model : core) {
                    int size = 0;
                    for (MngsmxjgDto child : model.getChildren()) {
                        if (StringUtil.isBlank(child.getScid())){
                            size++;
                        }
                    }
                    model.setNum(String.valueOf(size));
                }
                infoList.add(dto);
            }
            if (extension.size()>0){
                MngsmxjgDto dto = new MngsmxjgDto();
                dto.setDl("extension");
                dto.setChildren(extension);
                for (MngsmxjgDto model : extension) {
                    int size = 0;
                    for (MngsmxjgDto child : model.getChildren()) {
                        if (StringUtil.isBlank(child.getScid())){
                            size++;
                        }
                    }
                    model.setNum(String.valueOf(size));
                }
                infoList.add(dto);
            }
        }

        return infoList;
    }

    @Override
    public List<MngsmxjgDto> getReviewFilterInfo(MngsmxjgDto mngsmxjgDto, WkcxDto wkcxDto) {
        //String jgid = mngsmxjgDto.getJgid();
        if (StringUtil.isNotBlank(wkcxDto.getWkbh())){
            mngsmxjgDto.setXpid(wkcxDto.getXpid());
            if (wkcxDto.getWkbh().contains("-REM")){
                mngsmxjgDto.setWkbhlike("NC-6-REM");
            }else{
                if (StringUtil.isNotBlank(wkcxDto.getLibccon())){
                    if (Double.parseDouble(wkcxDto.getLibccon()) < 100){
                        mngsmxjgDto.setWkbhlike("NC-DNA-4");
                    }else{
                        mngsmxjgDto.setWkbhlike("NC-DNA-6");
                    }
                }
            }
        }
        if("01".equals(wkcxDto.getZt())){
            mngsmxjgDto.setNoai("4");
        }
        List<MngsmxjgDto> reviewInfo = dao.getReviewInfo(mngsmxjgDto);
        return getReviewInfo(reviewInfo);
    }

    @Override
    public List<MngsmxjgDto> getInfo(MngsmxjgDto mngsmxjgDto) {
        return dao.getReviewInfo(mngsmxjgDto);
    }

    public void findCOV(List<MngsmxjgDto> dtoList){
        for(MngsmxjgDto dto:dtoList){
            if (StringUtil.isNotBlank(dto.getFgdxx())){
                JSONObject jsonObject = JSONObject.parseObject(dto.getFgdxx());
                BigDecimal b2 = new BigDecimal("10000");
                BigDecimal value = new BigDecimal(jsonObject.get("coverage").toString()).multiply(b2).setScale(2, RoundingMode.HALF_UP);
                if (0 ==value.doubleValue()){
                    dto.setCov("NA");
                }else {
                    if (value.doubleValue()<1){
                        dto.setCov("<0.01");
                    }else{
                        dto.setCov(value.divide(new BigDecimal(100),3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).toString());
                    }
                }

            }
        }
    }

    /**
     * 批量插入明细结果临时表
     */
    @Override
    public boolean batchInsertLs(List<MngsmxjgDto> mngsmxjgList) {
        int index = 0;
        List<MngsmxjgDto> insertDtos = new ArrayList<>();
        for (MngsmxjgDto dto : mngsmxjgList) {
            index ++ ;
            insertDtos.add(dto);
            if (index == 300){
                index = 0;
                dao.batchInsertLs(insertDtos);
                insertDtos = new ArrayList<>();
            }
        }
        if (insertDtos.size()>0) {
            return dao.batchInsertLs(insertDtos);
        }
        return true;
    }

    /**
     * 批量修改
     */
    @Override
    public  Boolean updateListFz(MngsmxjgDto mngsmxjgDto){
        return dao.updateListFz(mngsmxjgDto)!=0;
    }

    @Override
    public List<MngsmxjgDto> getGidAndsfdc(MngsmxjgDto mngsmxjgDto) {
        return dao.getGidAndsfdc(mngsmxjgDto);
    }

    @Override
    public Boolean updateGidAndsfdc(List<MngsmxjgDto> list) {
        return dao.updateGidAndsfdc( list);
    }

    @Override
    public Boolean updateBjtppath(List<MngsmxjgDto> list) {
        return dao.updateBjtppath(list);
    }


    @Override
    public boolean updateGzdSetMr(List<MngsmxjgDto> mngsmxjgList) {
        return dao.updateGzdSetMr(mngsmxjgList);
    }

    /**
     * 批量修改明细结果临时表
     */
    @Override
    public boolean batchUpdateLs(List<MngsmxjgDto> mngsmxjgList) {
        return dao.batchUpdateLs(mngsmxjgList);
    }

    @Override
    public boolean updateGzdToAijgList(List<MngsmxjgDto> mngsmxjgList) {
        return dao.updateGzdToAijgList(mngsmxjgList);
    }

    /**
     * 查找出文库结果明细临时表中所有的数据
     */
    @Override
    public List<MngsmxjgDto> getLsAll(MngsmxjgDto mngsmxjgDto) {
        return dao.getLsAll(mngsmxjgDto);
    }

    /**
     * 全部字段的list数据插入到文库明细结果表中
     */
    @Override
    public boolean insertLsAllToWkjgmx(List<MngsmxjgDto> mngsmxjgDtoList) {
        int index = 0;
        List<MngsmxjgDto> insertDtos = new ArrayList<>();
        for (MngsmxjgDto dto : mngsmxjgDtoList) {
            index ++ ;
            insertDtos.add(dto);
            if (index == 300){
                index = 0;
                dao.insertLsAllToWkjgmx(insertDtos);
                insertDtos = new ArrayList<>();
            }
        }
        if (insertDtos.size()>0){
            return dao.insertLsAllToWkjgmx(insertDtos);
        }
        return true;
    }

    /**
     * 清空文库结果明细临时表中所有的数据
     */
    @Override
    public boolean delLsAll(MngsmxjgDto mngsmxjgDto) {
        return dao.delLsAll(mngsmxjgDto);
    }

    @Override
    public List<MngsmxjgDto> getMxjgByTjAndNotREM(MngsmxjgDto mngsmxjgDto) {
        return dao.getMxjgByTjAndNotREM(mngsmxjgDto);
    }


    private List<MngsmxjgDto> listDispose(List<MngsmxjgDto> mngsmxjgDtos){

        List<MngsmxjgDto> zmx = new ArrayList<>();
        List<MngsmxjgDto> mx = new ArrayList<>();
        //List<String> noFidList=new ArrayList<>();
        //String fidStr="";

        for (MngsmxjgDto dto : mngsmxjgDtos) {
            if(dto.getScid()==null){

                mx.add(dto);
            }else{
                zmx.add(dto);
            }
        }


        List<MngsmxjgDto> mngsmxjgDtoList = new ArrayList<>();
        if (mx.size()>0){
            Map<String, List<MngsmxjgDto>> listMap = mx.stream().collect(Collectors.groupingBy(MngsmxjgDto::getWzdl));
            if (null != listMap && listMap.size()>0){
                Iterator<Map.Entry<String, List<MngsmxjgDto>>> entries = listMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String,  List<MngsmxjgDto>> entry = entries.next();
                    List<MngsmxjgDto> resultModelList = entry.getValue();
                    MngsmxjgDto dto = new MngsmxjgDto();
                    dto.setXl(entry.getKey());
                    dto.setNum(String.valueOf(resultModelList.size()));
                    if (resultModelList.size()>0){
                        for(int i=0;i<resultModelList.size();i++){
                            recursionlist(resultModelList.get(i),zmx,resultModelList);
                        }
                    }
                    dto.setChildren(resultModelList);
                    mngsmxjgDtoList.add(dto);
                }
            }
        }
        return mngsmxjgDtoList;
    }


    private void recursionlist(MngsmxjgDto mngsmxjgDto,List<MngsmxjgDto> zmx, List<MngsmxjgDto> resultModelList){
        if (null == zmx || zmx.size() ==0)
             return ;
        boolean success = false;
        for (int i = zmx.size()-1; i >= 0; i--) {
            if (mngsmxjgDto.getTaxid().equals(zmx.get(i).getScid())){
                success = true;
                break;
            }
        }
        if (!success)
            return ;
        for (int i = zmx.size()-1; i >= 0; i--) {
            if (mngsmxjgDto.getTaxid().equals(zmx.get(i).getScid())){
                if (null ==  mngsmxjgDto.getChildren())
                    mngsmxjgDto.setChildren(new ArrayList<>());

                mngsmxjgDto.getChildren().add(zmx.get(i));
                resultModelList.add(zmx.get(i));
                zmx.remove(i);
//                i++;
                recursionlist( mngsmxjgDto.getChildren().get( mngsmxjgDto.getChildren().size()-1),zmx,resultModelList);
            }
        }
    }
    /**
     *  得到微生物列表
     */
    @Override
    public List<MngsmxjgDto> getListbyTaxids (MngsmxjgDto mngsmxjgDto){
        List<MngsmxjgDto> mngsmxjgDtos=dao.getListbyTaxids (mngsmxjgDto);
        for (MngsmxjgDto dto : mngsmxjgDtos) {//计算q_position
            if(StringUtil.isNoneBlank(dto.getZdz())&&StringUtil.isNoneBlank(dto.getZxz())&&StringUtil.isNoneBlank(dto.getQz())){
                BigDecimal qz=new BigDecimal(dto.getQz());
                BigDecimal zxz=new BigDecimal(dto.getZxz());
                BigDecimal zdz=new BigDecimal(dto.getZdz());
                qz=(qz.subtract(zxz)).divide(zdz.subtract(zxz),2, RoundingMode.HALF_UP);
                if(qz.compareTo(BigDecimal.ONE) > 0 ||qz.compareTo(BigDecimal.ONE)==0)
                    dto.setQ_position("1");
                else if(qz.compareTo(BigDecimal.ZERO) > 0 && qz.compareTo(BigDecimal.ONE) < 0){
                    dto.setQ_position(qz.toPlainString());
                }
                else
                    dto.setQ_position("0");
            }
            if(StringUtil.isNoneBlank(dto.getFgdxx())){
                MngsmxjgDto mngsmxjgDtoi= JSON.parseObject(dto.getFgdxx(),MngsmxjgDto.class);
                if(StringUtil.isNoneBlank(mngsmxjgDtoi.getComment())){
                    if("no ref dir".equals(mngsmxjgDtoi.getComment())){
                        dto.setCoverage(null);
                    }
                    else dto.setCoverage(mngsmxjgDtoi.getCoverage());
                }
                dto.setRef_length(mngsmxjgDtoi.getRef_length());
                dto.setHighlight(mngsmxjgDtoi.getHighlight());
                dto.setCoverlength(mngsmxjgDtoi.getCoverlength());
            }
            dto.setQ_index(dto.getQz());
            if(StringUtil.isNotBlank(dto.getQzpw())){
                BigDecimal bg = new BigDecimal(dto.getQzpw());
                dto.setQ_percentile(String.valueOf(bg.setScale(2, RoundingMode.HALF_UP).doubleValue()));
            }
            dto.setLibrary_type(mngsmxjgDto.getLibrary_type());
        }
        return mngsmxjgDtos;
    }

    /**
     * 根据关注度查找文库编号并对文库编号去重
     */
    @Override
    public Set<String> getWkbhByGzh(MngsmxjgDto mngsmxjgDto) {
        return dao.getWkbhByGzh(mngsmxjgDto);
    }

    /**
     * 根据物种ID查找文库编号并对文库编号去重
     */
    @Override
    public Set<String> getWkbhByTaxid(MngsmxjgDto mngsmxjg) {
        return dao.getWkbhByTaxid(mngsmxjg);
    }

    /**
     * 将表中关注度为2，fl为核心库0的数据gzd改为4（疑似）
     */
    @Override
    public boolean updateGzdToFour(MngsmxjgDto dto) {
        return dao.updateGzdToFour(dto);
    }

    @Override
    public boolean updateGzdToAijg(MngsmxjgDto dto) {
        return dao.updateGzdToAijg(dto);
    }

    @Override
    public List<MngsmxjgDto> getSampleTreeList(MngsmxjgDto dto) {
        return dao.getSampleTreeList(dto);
    }


    public List<List<MngsmxjgDto>> dealMngsmxInfo(List<MngsmxjgDto> list) {
        List<List<MngsmxjgDto>> infoList=new ArrayList<>();
        List<MngsmxjgDto> mngsmxjgDtos=new ArrayList<>();
        for(MngsmxjgDto mngsmxjgDto:list){
            if(mngsmxjgDto.getTaxid().equals(mngsmxjgDto.getFtaxid())){
                mngsmxjgDtos.add(mngsmxjgDto);
            }
        }

       for(MngsmxjgDto mngsmxjgDto:mngsmxjgDtos){
           if(!"5".equals(mngsmxjgDto.getGzd())){
               List<MngsmxjgDto> taxids=new ArrayList<>();
               taxids.add(mngsmxjgDto);
               List<MngsmxjgDto> mngsmxjgDtoList=new ArrayList<>();
               mngsmxjgDtoList.add(mngsmxjgDto);
               recursionInfo(taxids,list,mngsmxjgDtoList);
               infoList.add(mngsmxjgDtoList);
           }
       }
        return infoList;
    }

    @Override
    public boolean updateListLsFl(MngsmxjgDto dto) {
        return dao.updateListLsFl(dto)>0;
    }

    public void recursionInfo(List<MngsmxjgDto> taxids,List<MngsmxjgDto> list,List<MngsmxjgDto> mngsmxjgDtos){

        if(taxids!=null&&taxids.size()>0){
            List<MngsmxjgDto> strings=new ArrayList<>();
            for(MngsmxjgDto mngsmxjgDto:taxids){
                boolean flag=true;
                for(MngsmxjgDto dto:list){
                    if(!dto.getTaxid().equals(dto.getFtaxid())){
                        if(dto.getFtaxid().equals(mngsmxjgDto.getTaxid())){
                            strings.add(dto);
                            flag=false;
                        }
                    }
                }
                if(flag){
                    boolean insert=true;
                    for(MngsmxjgDto dto:mngsmxjgDtos){
                        if(dto.getMxjgid().equals(mngsmxjgDto.getMxjgid())){
                            insert=false;
                            break;
                        }
                    }
                    if(insert){
                        if(!"5".equals(mngsmxjgDto.getGzd())&&!"2".equals(mngsmxjgDto.getGzd())&&StringUtil.isNotBlank(mngsmxjgDto.getGzd())){
                            mngsmxjgDtos.add(mngsmxjgDto);
                        }
                    }
                }
            }
            recursionInfo(strings,list,mngsmxjgDtos);
        }
    }


    public boolean updateGzdByIds(MngsmxjgDto dto){
        return dao.updateGzdByIds(dto);
    }
    /**
     * 获取合并数据
     */
    public List<MngsmxjgDto> getMngsInfo(MngsmxjgDto dto){
        return dao.getMngsInfo(dto);
    }

    /**
     * AI结果导出
     */
    public List<MngsmxjgDto> getListForExp(Map<String, Object> params) {
        MngsmxjgDto mngsmxjgDto = (MngsmxjgDto) params.get("entryData");
        WkcxbbDto wkcxbbDto=new WkcxbbDto();
        wkcxbbDto.setBbh(mngsmxjgDto.getIds().get(1));
        wkcxbbDto.setWkcxid(mngsmxjgDto.getIds().get(0));
        WkcxbbDto dto = wkcxbbService.getDto(wkcxbbDto);
        List<MngsmxjgDto> list =new ArrayList<>();
        if (dto!=null){
            mngsmxjgDto.setLrsj(dto.getLrsj().substring(0,4)+dto.getLrsj().substring(5,7));
            mngsmxjgDto.setJgid(dto.getJgid());
            list = dao.getListForExp(mngsmxjgDto);
        }
        return list;
    }

    /**
     * 审核结果导出
     */
    public List<MngsmxjgDto> getListForAuditExp(Map<String, Object> params) {
        MngsmxjgDto mngsmxjgDto = (MngsmxjgDto) params.get("entryData");
        WkcxbbDto wkcxbbDto=new WkcxbbDto();
        wkcxbbDto.setBbh(mngsmxjgDto.getIds().get(1));
        wkcxbbDto.setWkcxid(mngsmxjgDto.getIds().get(0));
        WkcxbbDto wkcxbbDto_t = wkcxbbService.getDto(wkcxbbDto);
        List<MngsmxjgDto> list;
        List<MngsmxjgDto> newList =new ArrayList<>();
        if (wkcxbbDto_t!=null){
            mngsmxjgDto.setLrsj(wkcxbbDto_t.getLrsj().substring(0,4)+wkcxbbDto_t.getLrsj().substring(5,7));
            mngsmxjgDto.setJgid(wkcxbbDto_t.getJgid());
            String px="Bacteria,Fungi,Viruses,Parasite";
            String[] split = px.split(",");
            list = dao.getListForAuditExp(mngsmxjgDto);
            for(MngsmxjgDto dto:list){
                if (StringUtil.isNotBlank(dto.getFgdxx())){
                    JSONObject jsonObject = JSONObject.parseObject(dto.getFgdxx());
                    BigDecimal b2 = new BigDecimal("10000");
                    BigDecimal value = new BigDecimal(jsonObject.get("coverage").toString()).multiply(b2).setScale(2, RoundingMode.HALF_UP);
                    if (0 ==value.doubleValue()){
                        dto.setCov("NA");
                    }else {
                        if (value.doubleValue()<1){
                            dto.setCov("<0.01%");
                        }else{
                            dto.setCov(value.divide(new BigDecimal(100),3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP) +"%");
                        }
                    }

                }
                BigDecimal readsaccum=new BigDecimal(dto.getReadsaccum());
                BigDecimal bigDecimal=new BigDecimal(100);
                if(StringUtil.isNotBlank(wkcxbbDto_t.getTotalreads())){
                    BigDecimal totalReads=new BigDecimal(wkcxbbDto_t.getTotalreads());
                    dto.setRatio((readsaccum.multiply(bigDecimal)).divide(totalReads, 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP) +"%");
                }
            }
            for(String s:split){
                for(MngsmxjgDto dto:list){
                    if(s.equals(dto.getWzfl())){
                        newList.add(dto);
                    }
                }
            }
            for(MngsmxjgDto dto:list){
                if(!px.contains(dto.getWzfl())){
                    newList.add(dto);
                }
            }
        }
        return newList;
    }

    /**
     * 创建mngsmxjg
     */
    public void createMngsmxjg(){
        //获取日期和时间
        LocalDateTime dateTime = LocalDateTime.now();
        //格式化
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = formatDate.format(dateTime);
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,Integer.parseInt(dateStr.substring(0,4)));
        //设置月份
        cal.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(4,6)));
        String thismonth=String.valueOf(cal.get(Calendar.MONTH)+1);
        if(existsTable("igams_mngsmxjg_"+dateStr.substring(0,4)+thismonth)==0){
            MngsmxjgDto mngsmxjgDto=new MngsmxjgDto();
            formatDate = DateTimeFormatter.ofPattern("HH:mm:ss");
            String dateStr1 = formatDate.format(dateTime);
            mngsmxjgDto.setLrsj(dateStr.substring(0,4)+"-"+thismonth+"-01"+" "+dateStr1);
            mngsmxjgDto.setMxjgid(StringUtil.generateUUID());
            mngsmxjgDto.setTaxid("0");
            mngsmxjgDto.setWkbh("cs");
            List<MngsmxjgDto>list=new ArrayList<>();
            list.add(mngsmxjgDto);
            batchInsertLs(list);
        }

    }
    /**
     * 获取高关注数据
     */
    public List<MngsmxjgDto> getHighConcernList(MngsmxjgDto mngsmxjgDto){
        return dao.getHighConcernList(mngsmxjgDto);
    }
    /**
     * 获取疑似数据
     */
    public List<MngsmxjgDto> getSuspectedList(MngsmxjgDto mngsmxjgDto){
        return dao.getSuspectedList(mngsmxjgDto);
    }
    /**
     * 获取导出数据
     */
    public List<MngsmxjgDto> getExportList(MngsmxjgDto mngsmxjgDto){
        return dao.getExportList(mngsmxjgDto);
    }

    /**
     * 背景数据库导出数据
     */
    public List<Map<String,String>> backgroundDataBase(MngsmxjgDto mngsmxjgDto){
        return dao.backgroundDataBase(mngsmxjgDto);
    }
}
