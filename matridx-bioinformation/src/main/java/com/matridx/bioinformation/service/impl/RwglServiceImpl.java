package com.matridx.bioinformation.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.bioinformation.dao.entities.JxcmdDto;
import com.matridx.bioinformation.dao.entities.JxcmdModel;
import com.matridx.bioinformation.dao.entities.RwglDto;
import com.matridx.bioinformation.dao.entities.RwglModel;
import com.matridx.bioinformation.dao.post.IRwglDao;
import com.matridx.bioinformation.service.svcinterface.IJxcmdService;
import com.matridx.bioinformation.service.svcinterface.IRwglService;
import com.matridx.bioinformation.util.DockerUtil;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
public class RwglServiceImpl extends BaseBasicServiceImpl<RwglDto, RwglModel, IRwglDao> implements IRwglService {

    @Autowired
    private DockerUtil dockerUtil;
    @Autowired
    IJxcmdService jxcmdService;
    private Logger log = LoggerFactory.getLogger(RwglServiceImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean addTaskInfo(RwglDto rwglDto) throws Exception {
        boolean success;
        RwglDto rwglDto1 = new RwglDto();
        rwglDto1.setRwmc(rwglDto.getRwmc());
        rwglDto1 = dao.getDto(rwglDto1);
        if (rwglDto1!=null){
            throw new BusinessException("msg", "任务名称重复！");
        }
        success = dao.insertDto(rwglDto) != 0;
        if (!success) {
            throw new BusinessException("msg", "新增任务失败！");
        }
        List<JxcmdModel> jxcmdModels =  (List<JxcmdModel>) JSONObject.parseArray(rwglDto.getArgs(), JxcmdModel.class);
        if (null != jxcmdModels && jxcmdModels.size() >0){
            for (JxcmdModel jxcmdModel:jxcmdModels) {
                jxcmdModel.setJxcmdid(StringUtil.generateUUID());
                jxcmdModel.setYwid(rwglDto.getRwid());
                jxcmdModel.setLrry(rwglDto.getLrry());
            }
            success = jxcmdService.batchInsert(jxcmdModels);
            if (!success){
                throw new BusinessException("mes","明细修改失败！");
            }
        }
        return true;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean modTaskInfo(RwglDto rwglDto) throws BusinessException {
        boolean success;
        RwglDto rwglDto1 = dao.getDtoInfo(rwglDto);
        if (rwglDto1!=null){
            throw new BusinessException("msg", "任务名称重复！");
        }
        success = dao.update(rwglDto) != 0;
        if (!success) {
            throw new BusinessException("msg", "修改任务失败！");
        }
        JxcmdDto jxcmdDto = new JxcmdDto();
        jxcmdDto.setYwid(rwglDto.getRwid());
        jxcmdDto.setScry(rwglDto.getXgry());
        jxcmdService.updateDto(jxcmdDto);
        List<JxcmdModel> jxcmdModels =  (List<JxcmdModel>) JSONObject.parseArray(rwglDto.getArgs(), JxcmdModel.class);
        if (null != jxcmdModels && jxcmdModels.size() >0){
            for (JxcmdModel jxcmdModel:jxcmdModels) {
                jxcmdModel.setJxcmdid(StringUtil.generateUUID());
                jxcmdModel.setYwid(rwglDto.getRwid());
                jxcmdModel.setLrry(rwglDto.getXgry());
            }
            success = jxcmdService.batchInsert(jxcmdModels);
            if (!success){
                throw new BusinessException("mes","明细修改失败！");
            }
        }
        return true;
    }

    @Override
    public List<RwglDto> getInfoList(RwglDto rwglDto) {
        return dao.getInfoList(rwglDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean operateTaskInfo(RwglDto rwglDto) throws Exception {
        if (StringUtil.isNotBlank(rwglDto.getRwid())) {
            RwglDto dto = dao.getDto(rwglDto);
            if (dto ==null) {
                throw new BusinessException("msg", "该任务不存在！");
            }
            rwglDto.setDocker(dto.getDocker());
            rwglDto.setImage(dto.getImage());
            rwglDto.setSj(dto.getSj());
            dockerUtil.operateContainer(rwglDto);
        } else {
            throw new BusinessException("msg", "未获取到任务id！");
        }
        return true;
    }

    @Override
    public Boolean updateList(List<RwglDto> rwglDtoList) {
        return dao.updateList(rwglDtoList)!=0;
    }

    /**
     * 获取容器
     * @param strings
     * @return
     */
    @Override
    public List<RwglDto> getEndDtoList(List<String> strings){
        return dao.getEndDtoList(strings);
    }


    /**
     * 定期删除事件超过七天的容器
     */
    public void delOverdueContainer(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR)-7);
        Date endTime = calendar.getTime();
        RwglDto rwglDto = new RwglDto();
        rwglDto.setLrsj( simpleDateFormat.format(endTime));
        List<RwglDto> dtos = dao.getOverdueList(rwglDto);
        if (null != dtos && dtos.size()>0){
            for (RwglDto dto : dtos) {
//                try {
//                    dto.setOperate("remove");
                    dto.setScbj("2");
//                    dockerUtil.operateContainer(dto);
//                } catch (Exception e) {
//                    log.error("OverdueError:"+e.getMessage());
//                }
            }
            dao.updateList(dtos);
        }
    }

}