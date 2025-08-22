package com.matridx.igams.storehouse.service.impl;


import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IGzglService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.storehouse.dao.entities.ZjszDto;
import com.matridx.igams.storehouse.dao.entities.ZjszModel;
import com.matridx.igams.storehouse.dao.post.IZjszDao;
import com.matridx.igams.storehouse.service.svcinterface.IZjszService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ZjszServiceImpl extends BaseBasicServiceImpl<ZjszDto, ZjszModel, IZjszDao> implements IZjszService {
    @Autowired
    IGzglService gzglService;
    @Autowired
    ICommonService commonService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Map<String,Object> insertQualitySetting(ZjszDto zjszDto) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        zjszDto.setGzid(StringUtil.generateUUID());
        zjszDto.setZjszid(StringUtil.generateUUID());
        int insert = dao.insert(zjszDto);
        if (insert<1){
            throw new BusinessException("保存失败!");
        }
        User user = new User();
        user.setYhid(zjszDto.getZjry());
        User userInfoById = commonService.getUserInfoById(user);
        zjszDto.setZjrymc(userInfoById.getZsxm());
        map.put("zjszDto",zjszDto);
        map.put("status","success");
        return map;
    }
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delQualitySetting(ZjszDto zjszDto) throws BusinessException {
        int insert = dao.delete(zjszDto);
        if (insert<1){
            throw new BusinessException("删除失败!");
        }
        return true;
    }
    /**
     * 定时发布任务
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void zjszTask() throws BusinessException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        Date date = new Date();
        String newdate = sdf.format(date);
        List<ZjszDto> zjszDtoList = dao.getZjszDtos();
        List<GzglDto> gzglDtos = new ArrayList<>();
        List<ZjszDto> zjszDtos = new ArrayList<>();
//        String token = talkUtil.getToken();
        if (!CollectionUtils.isEmpty(zjszDtoList)){
            for (ZjszDto zjszDto : zjszDtoList) {
                if (newdate.equals(zjszDto.getKssj())){
                    GzglDto gzglDto = new GzglDto();
                    gzglDto.setGzid(StringUtil.generateUUID());
                    gzglDto.setRwmc(zjszDto.getWlmc()+zjszDto.getScph());
                    gzglDto.setFzr(zjszDto.getZjry());
                    gzglDto.setQwwcsj(zjszDto.getJssj());
                    gzglDto.setZt(StatusEnum.CHECK_NO.getCode());
                    gzglDtos.add(gzglDto);
                    ZjszDto zjszDto_u = new ZjszDto();
                    zjszDto_u.setGzid(gzglDto.getGzid());
                    zjszDto_u.setZjszid(zjszDto.getZjszid());
                    zjszDtos.add(zjszDto_u);
                }
            }
            if (!CollectionUtils.isEmpty(zjszDtos)){
                boolean updateList = dao.updateList(zjszDtos);
                if (!updateList){
                    throw new BusinessException("更新失败!");
                }
            }
            if (!CollectionUtils.isEmpty(gzglDtos)){
                boolean insertList = gzglService.insertList(gzglDtos);
                if (!insertList){
                    throw new BusinessException("保存失败!");
                }
                String ICOMM_SH00009 = xxglService.getMsg("ICOMM_SH00009");
                for (GzglDto gzglDto : gzglDtos) {
                    User user = new User();
                    user.setYhid(gzglDto.getFzr());
                    User userInfoById = commonService.getUserInfoById(user);
                    if(!StringUtil.isBlank(userInfoById.getDdid())){
                        talkUtil.sendWorkMessage(userInfoById.getYhm(), userInfoById.getDdid(), ICOMM_SH00009,xxglService.getMsg("ICOMM_ZJ00100",gzglDto.getRwmc(),gzglDto.getQwwcsj(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                    }
                }
            }

        }
    }
}
