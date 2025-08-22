package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.production.dao.entities.BomglDto;
import com.matridx.igams.production.dao.entities.BomglModel;
import com.matridx.igams.production.dao.entities.BommxDto;
import com.matridx.igams.production.dao.entities.WlglDto;
import com.matridx.igams.production.dao.post.IScbomDao;
import com.matridx.igams.production.service.svcinterface.IScbomService;
import com.matridx.igams.production.service.svcinterface.IScbommxService;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ScbomServiceImpl extends BaseBasicServiceImpl<BomglDto, BomglModel, IScbomDao> implements IScbomService, IFileImport {
    @Autowired
    IWlglService wlglService;
    @Autowired
    IScbommxService scbommxService;
    @Autowired
    IJcsjService jcsjService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addSaveProduceBom(BomglDto bomglDto) throws BusinessException {
        String bomid = StringUtil.generateUUID();
        bomglDto.setBomid(bomid);
        insert(bomglDto);
        List<BommxDto> bommxDtos = JSON.parseArray(bomglDto.getBommx_json(), BommxDto.class);
        if (!CollectionUtils.isEmpty(bommxDtos)) {
            for (BommxDto bommxDto : bommxDtos) {
                bommxDto.setBomid(bomid);
                bommxDto.setBommxid(StringUtil.generateUUID());
                bomglDto.setLrry(bomglDto.getLrry());
            }
            boolean isSuccess = scbommxService.insertBommxDtos(bommxDtos);
            if (!isSuccess) {
                throw new BusinessException("msg", "新增BOM明细失败！");
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean modSaveProduceBom(BomglDto bomglDto) throws BusinessException {
        update(bomglDto);
        BommxDto bommxDto = new BommxDto();
        bommxDto.setBomid(bomglDto.getBomid());
        List<BommxDto> bommxDtos = JSON.parseArray(bomglDto.getBommx_json(), BommxDto.class);
        List<BommxDto> modBommxDtos = new ArrayList<>();
        List<BommxDto> addBommxDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(bommxDtos)) {
            List<BommxDto> dtoList = scbommxService.getDtoList(bommxDto);
            for (BommxDto dto : bommxDtos) {
                //剩下的是删除的
                dtoList.removeIf(next -> next.getBommxid().equals(dto.getBommxid()));
                if (StringUtil.isBlank(dto.getBommxid())) {
                    dto.setBomid(bomglDto.getBomid());
                    dto.setBommxid(StringUtil.generateUUID());
                    dto.setLrry(bomglDto.getXgry());
                    addBommxDtos.add(dto);
                } else {
                    dto.setXgry(bomglDto.getXgry());
                    modBommxDtos.add(dto);
                }
            }
            if (!CollectionUtils.isEmpty(addBommxDtos)) {
                boolean isSuccess = scbommxService.insertBommxDtos(addBommxDtos);
                if (!isSuccess) {
                    throw new BusinessException("msg", "新增BOM明细失败！");
                }
            }
            if (!CollectionUtils.isEmpty(modBommxDtos)) {
                boolean isSuccess = scbommxService.updateBommxDtos(modBommxDtos);
                if (!isSuccess) {
                    throw new BusinessException("msg", "修改BOM明细失败！");
                }
            }
            if (!CollectionUtils.isEmpty(dtoList)) {
                BommxDto bommxDto_del = new BommxDto();
                bommxDto_del.setScry(bomglDto.getXgry());
                List<String> ids = new ArrayList<>();
                for (BommxDto dto : dtoList) {
                    ids.add(dto.getBommxid());
                }
                bommxDto_del.setIds(ids);
                boolean delete = scbommxService.delete(bommxDto_del);
                if (!delete) {
                    throw new BusinessException("msg", "删除BOM明细失败！");
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delProduceBom(BomglDto bomglDto) throws BusinessException {
        boolean delete = delete(bomglDto);
        if (!delete) {
            throw new BusinessException("msg", "删除BOM信息失败！");
        }
        BommxDto bommxDto = new BommxDto();
        bommxDto.setIds(bomglDto.getIds());
        bommxDto.setScry(bomglDto.getScry());
        boolean isSuccess = scbommxService.deleteByBomIds(bommxDto);
        if (!isSuccess) {
            throw new BusinessException("msg", "删除BOM明细失败！");
        }
        return true;
    }

    @Override
    public BomglDto selectDtoByMjwlid(String mjwlid) {
        return dao.selectDtoByMjwlid(mjwlid);
    }

    @Override
    public boolean existCheck(String fieldName, String value) {
        return false;
    }

    @Override
    public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages) throws BusinessException {
        BommxDto bommxDto = (BommxDto) baseModel;
        bommxDto.setScbj("0");
        if (StringUtil.isNotBlank(bommxDto.getBomid())) {
            BomglDto bomglDto = new BomglDto();
            BomglDto bomglDto2 = getDtoById(bommxDto.getBomid());
            if (bomglDto2 == null) {
                WlglDto wlglDto = wlglService.getWlidByWlbm(bommxDto.getMjwlbm());
                if (null == wlglDto) {
                    throw new BusinessException("msg", "母件物料编码" + bommxDto.getMjwlbm() + "有误" + "没有此物料,请填写正确的母件物料编码!");
                }
                BomglDto bomglDto1 = dao.selectDtoByMjwlid(wlglDto.getWlid());
                if (null != bomglDto1) {
                    throw new BusinessException("msg", "母件物料编码" + bommxDto.getMjwlbm() + "和数据库中重复!");
                }
                bomglDto.setBomid(bommxDto.getBomid());
                bomglDto.setLrry(user.getYhid());
                bomglDto.setMjwlid(wlglDto.getWlid());
                bomglDto.setBbh(bommxDto.getBbh());
                bomglDto.setBbrq(bommxDto.getBbrq());
                bomglDto.setBbsm(bommxDto.getBbsm());
                JcsjDto jcsjDto = new JcsjDto();
                jcsjDto.setCsmc(bommxDto.getBomlbmc());
                jcsjDto.setJclb("SCBOM_TYPE");
                jcsjDto = jcsjService.getDtoByCsmcAndJclb(jcsjDto);
                if (null == jcsjDto) {
                    throw new BusinessException("msg", "没有" + bommxDto.getBomlbmc() + "此bom类别");
                }
                bomglDto.setBomlb(jcsjDto.getCsid());
                if (!insert(bomglDto)) {
                    throw new BusinessException("msg", "新增bom管理失败!");
                }
            }
        }
        bommxDto.setBomid(bommxDto.getBomid());
        bommxDto.setBommxid(StringUtil.generateUUID());
        WlglDto wlglDto_t = wlglService.getWlidByWlbm(bommxDto.getZwlbm());
        if (null == wlglDto_t) {
            dao.deleteById(bommxDto.getBomid());
            throw new BusinessException("msg", "子件物料编码" + bommxDto.getZwlbm() + "有误" + "没有此物料,请填写正确的子件物料编码!");
        }
        bommxDto.setZjwlid(wlglDto_t.getWlid());
        if (StringUtil.isNotBlank(bommxDto.getBzyl())) {
            Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|(0{1}))(\\.\\d{1,2})?$");
            Matcher matcher = pattern.matcher(bommxDto.getBzyl());
            if (!matcher.matches()) {
                dao.deleteById(bommxDto.getBomid());
                throw new BusinessException("msg", "请填写正确的标准用量，可以保留两位小数!");
            }
            if (!scbommxService.insert(bommxDto)) {
                dao.deleteById(bommxDto.getBomid());
                throw new BusinessException("msg", "插入bom明细失败!");
            }
        }
        return true;
    }

    @Override
    public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
        return null;
    }

    @Override
    public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
        return false;
    }

    @Override
    public boolean checkDefined(List<Map<String, String>> defined) {
        return true;
    }
}
