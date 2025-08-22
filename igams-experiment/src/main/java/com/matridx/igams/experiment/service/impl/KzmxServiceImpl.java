package com.matridx.igams.experiment.service.impl;


import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.KzglDto;
import com.matridx.igams.experiment.dao.entities.KzmxDto;
import com.matridx.igams.experiment.dao.entities.KzmxModel;
import com.matridx.igams.experiment.dao.post.IKzglDao;
import com.matridx.igams.experiment.dao.post.IKzmxDao;
import com.matridx.igams.experiment.service.svcinterface.IKzmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class KzmxServiceImpl extends BaseBasicServiceImpl<KzmxDto, KzmxModel, IKzmxDao> implements IKzmxService {
    @Autowired
    private IKzglDao kzglDao;

    /**
     * 验证是否存在相同syglid
     */
    public KzmxDto verifySame(KzmxDto kzmxDto){
        return dao.verifySame(kzmxDto);
    }

    /**
     * 新增前校验内部编号是否存在
     */
    @Override
    public List<String> checkNbbms(KzmxDto kzmxDto) {
        List<String> notexitnbbhs = new ArrayList<>();
        List<String> exitnbbhlist = new ArrayList<>();
        String allnbbh;
        if (kzmxDto.getNbbhs() != null) {
            kzmxDto.getNbbhs();
            List<KzmxDto> sjxxList = dao.getSjxxByNbbhs(kzmxDto);
            for (KzmxDto dto : sjxxList) {
                if (dto.getSjid() != null && !Objects.equals(dto.getSjid(), "")) {
                    exitnbbhlist.add(dto.getNbbh());
                }
            }
            for (int j = 0; j < kzmxDto.getNbbhs().length; j++) {
                allnbbh = kzmxDto.getNbbhs()[j];
                boolean result = exitnbbhlist.contains(allnbbh);
                if (!result) {
                    notexitnbbhs.add(kzmxDto.getNbbhs()[j]);
                }
            }
        }
        return notexitnbbhs;
    }

    /**
     * 新增保存文库明细
     */
    @SuppressWarnings("unused")
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addSaveAmplification(KzmxDto kzmxDto) {
        KzglDto kzglDto = new KzglDto();
        kzglDto.setLrry(kzmxDto.getLrry());
        kzglDto.setZt(kzmxDto.getZt());
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String cjrq = sf.format(date);
        kzglDto.setLrsj(cjrq);
        kzglDto.setKzmc(kzmxDto.getKzmc());
        kzglDto.setJcdw(kzmxDto.getJcdw());
        kzglDto.setKzsj(kzmxDto.getKzsj());
        List<KzglDto> kzglDtos = kzglDao.getListByJcdwAndLrsj(kzglDto);
        if (kzglDtos != null && kzglDtos.size() > 0) {
            int xh = Integer.parseInt(kzglDtos.get(0).getXh());
            for (int i = 0; i < kzglDtos.size(); i++) {
                if (i < kzglDtos.size() - 1) {
                    if (Integer.parseInt(kzglDtos.get(i).getXh()) > Integer.parseInt(kzglDtos.get(i + 1).getXh())) {
                        xh = Integer.parseInt(kzglDtos.get(i).getXh()) + 1;
                    } else {
                        xh = Integer.parseInt(kzglDtos.get(i + 1).getXh()) + 1;
                    }
                }
            }
            kzglDto.setXh(String.valueOf(xh));
        } else {
            kzglDto.setXh("1");
        }
        kzglDto.setSjph1(kzmxDto.getSjph1());
        kzglDto.setSjph2(kzmxDto.getSjph2());
        kzglDto.setKzid(StringUtil.generateUUID());
        boolean result = kzglDao.insert(kzglDto) > 0;
        if (!result)
            return false;
        List<KzmxDto> kzmxDtos = dao.getSjxxByNbbhs(kzmxDto);
        if (kzmxDtos != null && kzmxDtos.size() > 0) {
            List<KzmxDto> insertlist = new ArrayList<>();
            for (int i = 0; i < kzmxDto.getNbbhs().length; i++) {
                KzmxDto kzmxDto_t = new KzmxDto();
                insertlist.add(kzmxDto_t);
                for (KzmxDto dto : kzmxDtos) {
                    if (dto.getNbbh().equals(kzmxDto.getNbbhs()[i])) {
                        insertlist.get(i).setSjid(dto.getSjid());
                        break;
                    }
                }
                insertlist.get(i).setNbbh(kzmxDto.getNbbhs()[i]);
                insertlist.get(i).setJcxmdm("F");
                insertlist.get(i).setXh(kzmxDto.getXhs()[i]);
                insertlist.get(i).setKzid(kzglDto.getKzid());
                insertlist.get(i).setLrry(kzmxDto.getLrry());
                insertlist.get(i).setJcdw(kzglDto.getJcdw());
                insertlist.get(i).setKzsj(kzglDto.getKzsj());

                if (kzmxDto.getTqms() != null && kzmxDto.getTqms().length>0){
                    insertlist.get(i).setTqm(kzmxDto.getTqms()[i]);
                }
                if (kzmxDto.getSyglids() != null && kzmxDto.getSyglids().length>0){
                    insertlist.get(i).setSyglid(kzmxDto.getSyglids()[i]);
                }
            }
            boolean isSuccess = dao.insertList(insertlist);
            if (!isSuccess)
                return false;
            List<String> ids= Arrays.asList(kzmxDto.getSyglids());
            KzmxDto kzmxDto_t=new KzmxDto();
            kzmxDto_t.setIds(ids);
            kzmxDto_t.setKzsj(kzglDto.getKzsj());
            isSuccess =dao.updateKzsjByIds(kzmxDto_t);
            return isSuccess;
        }
        return true;
    }

    /**
     * 新增保存文库明细
     */
    @SuppressWarnings("unused")
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean modSaveAmplification(KzmxDto kzmxDto) {
        KzglDto kzglDto = new KzglDto();
        kzglDto.setXgry(kzmxDto.getXgry());
        kzglDto.setZt(kzmxDto.getZt());
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String cjrq = sf.format(date);
        kzglDto.setLrsj(cjrq);
        kzglDto.setKzmc(kzmxDto.getKzmc());
        kzglDto.setJcdw(kzmxDto.getJcdw());
        kzglDto.setKzsj(kzmxDto.getKzsj());
        List<KzglDto> kzglDtos = kzglDao.getListByJcdwAndLrsj(kzglDto);
        if (kzglDtos != null && kzglDtos.size() > 0) {
            int xh = Integer.parseInt(kzglDtos.get(0).getXh());
            for (int i = 0; i < kzglDtos.size(); i++) {
                if (i < kzglDtos.size() - 1) {
                    if (Integer.parseInt(kzglDtos.get(i).getXh()) > Integer.parseInt(kzglDtos.get(i + 1).getXh())) {
                        xh = Integer.parseInt(kzglDtos.get(i).getXh()) + 1;
                    } else {
                        xh = Integer.parseInt(kzglDtos.get(i + 1).getXh()) + 1;
                    }
                }
            }
            kzglDto.setXh(String.valueOf(xh));
        } else {
            kzglDto.setXh("1");
        }
        kzglDto.setSjph1(kzmxDto.getSjph1());
        kzglDto.setSjph2(kzmxDto.getSjph2());
        kzglDto.setKzid(kzmxDto.getKzid());
        boolean result = kzglDao.update(kzglDto) > 0;
        if (!result)
            return false;
        List<KzmxDto> kzmxDtos = dao.getSjxxByNbbhs(kzmxDto);
        if (kzmxDtos != null && kzmxDtos.size() > 0) {
            List<KzmxDto> insertlist = new ArrayList<>();
            for (int i = 0; i < kzmxDto.getNbbhs().length; i++) {
                KzmxDto kzmxDto_t = new KzmxDto();
                insertlist.add(kzmxDto_t);
                for (KzmxDto dto : kzmxDtos) {
                    if (dto.getNbbh().equals(kzmxDto.getNbbhs()[i])) {
                        insertlist.get(i).setSjid(dto.getSjid());
                        break;
                    }
                }
                insertlist.get(i).setNbbh(kzmxDto.getNbbhs()[i]);
                insertlist.get(i).setJcxmdm("F");
                insertlist.get(i).setXh(kzmxDto.getXhs()[i]);
                insertlist.get(i).setKzid(kzglDto.getKzid());
                insertlist.get(i).setLrry(kzmxDto.getXgry());
                insertlist.get(i).setJcdw(kzglDto.getJcdw());
                insertlist.get(i).setKzsj(kzglDto.getKzsj());

                if (kzmxDto.getTqms() != null && kzmxDto.getTqms().length>0){
                    insertlist.get(i).setTqm(kzmxDto.getTqms()[i]);
                }
                if (kzmxDto.getSyglids() != null && kzmxDto.getSyglids().length>0){
                    insertlist.get(i).setSyglid(kzmxDto.getSyglids()[i]);
                }
            }

            KzmxDto kzmxDto_t=new KzmxDto();
            kzmxDto_t.setKzid(kzmxDto.getKzid());
            List<KzmxDto> dtoList = dao.getDtoList(kzmxDto_t);
            List<String> syglids=new ArrayList<>();
            if(dtoList!=null&&dtoList.size()>0){
                for(KzmxDto dto:dtoList){
                    if(StringUtil.isNotBlank(dto.getSyglid())){
                        syglids.add(dto.getSyglid());
                    }
                }
            }
            if(syglids.size() > 0){
                kzmxDto_t.setIds(syglids);
                dao.removeKzsjByIds(kzmxDto_t);
            }
            boolean deleteInfo = dao.deleteInfo(kzmxDto);// 先删除原有数据
            if (!deleteInfo)
                return false;
            boolean isSuccess = dao.insertList(insertlist);
            if (!isSuccess)
                return false;
            List<String> ids= Arrays.asList(kzmxDto.getSyglids());
            kzmxDto_t.setIds(ids);
            kzmxDto_t.setKzsj(kzglDto.getKzsj());
            isSuccess =dao.updateKzsjByIds(kzmxDto_t);
            return isSuccess;
        }
        return true;
    }

    /**
     * 根据ids移除扩增时间
     */
    public boolean removeKzsjByIds(KzmxDto kzmxDto){
        return dao.removeKzsjByIds(kzmxDto);
    }
}
