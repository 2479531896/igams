package com.matridx.igams.production.service.impl;



import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.HtfktxDto;
import com.matridx.igams.production.dao.entities.HtfktxModel;
import com.matridx.igams.production.dao.post.IHtfktxDao;
import com.matridx.igams.production.service.svcinterface.IHtfktxService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class HtfktxServiceImpl extends BaseBasicServiceImpl<HtfktxDto, HtfktxModel, IHtfktxDao> implements IHtfktxService{

    @Autowired
    IShgcService shgcService;

    private final Logger log = LoggerFactory.getLogger(HtfktxServiceImpl.class);
    /**
     * 合同列表（查询审核状态）
     */
    @Override
    public List<HtfktxDto> getPagedDtoList(HtfktxDto htfktxDto) {
        List<HtfktxDto> list = dao.getPagedDtoList(htfktxDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_CONTRACT.getCode(), "zt", "htid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    /**
     * 获取付款提醒信息
     */
    public List<HtfktxDto> getListByHtid(HtfktxDto htfktxDto){
        return dao.getListByHtid(htfktxDto);
    }

    /**
     * 保存合同付款提醒信息
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean saveContractPayRemind(HtfktxDto htfktxDto,List<HtfktxDto> htfkDtos){
        List<HtfktxDto> yy_htfkDtos=dao.getListByHtid(htfktxDto);
        List<HtfktxDto> addList=new ArrayList<>();
        List<HtfktxDto> modList=new ArrayList<>();
        List<String> delids=new ArrayList<>();
        if(!CollectionUtils.isEmpty(htfkDtos)){
            log.error("1");
            for(HtfktxDto t_htfktxDto : htfkDtos){
                log.error("2");
                if(StringUtils.isBlank(t_htfktxDto.getFktxid())){
                    log.error("3");
                    t_htfktxDto.setFktxid(StringUtil.generateUUID());
                    t_htfktxDto.setLrry(htfktxDto.getLrry());
                    addList.add(t_htfktxDto);
                }else{
                    log.error("mod");
                    t_htfktxDto.setXgry(htfktxDto.getLrry());
                    modList.add(t_htfktxDto);
                }
            }
            //获取删除提醒数据
            if(!CollectionUtils.isEmpty(yy_htfkDtos)){
                for(HtfktxDto yy_htfkDto : yy_htfkDtos){
                    boolean sfsc=true;
                    for(HtfktxDto t_htfktxDto : htfkDtos){
                        if (yy_htfkDto.getFktxid().equals(t_htfktxDto.getFktxid())) {
                            sfsc = false;
                            break;
                        }
                    }
                    if(sfsc)
                        delids.add(yy_htfkDto.getFktxid());
                }
            }
            htfktxDto.setIds(delids);
        }else{
            //获取删除提醒数据
            if(!CollectionUtils.isEmpty(yy_htfkDtos)){
                for(HtfktxDto yy_htfkDto : yy_htfkDtos){
                    delids.add(yy_htfkDto.getFktxid());
                }
            }
            htfktxDto.setIds(delids);
        }
        if(!CollectionUtils.isEmpty(addList)){
            boolean addresult=dao.insertDtoList(addList);
            if(!addresult)
                return false;
        }
        if(!CollectionUtils.isEmpty(modList)){
            log.error("modstart");
            boolean modresult=dao.updateDtoList(modList);
            if(!modresult)
                return false;
        }
        if(!CollectionUtils.isEmpty(delids)){
            htfktxDto.setScry(htfktxDto.getLrry());
            return dao.delDtoList(htfktxDto);
        }
        return true;
    }

    /**
     * 删除合同付款提醒数据
     */
    public boolean delPendingPayment(HtfktxDto htfktxDto){
        int result=dao.delete(htfktxDto);
        return result > 0;
    }

    /**
     * 更新付款提醒信息
     */
    public boolean updateHtfkid(HtfktxDto htfktxDto){
        return dao.updateHtfkid(htfktxDto);
    }

    /**
     * 新增合同付款提醒信息
     */
    public void insertDtoList(List<HtfktxDto> htfktxDtos){
        dao.insertDtoList(htfktxDtos);
    }

    /**
     * 更新htfkid为null
     */
    public boolean updateHtfkidToNull(HtfktxDto htfktxDto){
        return dao.updateHtfkidToNull(htfktxDto);
    }

    /**
     * 根据htid删除
     */
    public void deleteByHtid(HtfktxDto htfktxDto){
        dao.deleteByHtid(htfktxDto);
    }

}
