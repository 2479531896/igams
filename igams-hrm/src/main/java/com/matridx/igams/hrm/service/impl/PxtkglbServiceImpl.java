package com.matridx.igams.hrm.service.impl;



import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.*;
import com.matridx.igams.hrm.dao.post.IPxtkglbDao;
import com.matridx.igams.hrm.service.svcinterface.IPxtkglbService;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PxtkglbServiceImpl extends BaseBasicServiceImpl<PxtkglbDto, PxtkglbModel, IPxtkglbDao> implements IPxtkglbService {

    /**
     * 通过pxid删除题库配置
     * @param pxtkglbDto
     */
    public void deleteByPxid(PxtkglbDto pxtkglbDto){
        dao.deleteByPxid(pxtkglbDto);
    }


    /**
     * 通过pxid获取全部信息
     * @param pxtkglbDto
     * @return
     */
    public List<PxtkglbDto> getListByPxid(PxtkglbDto pxtkglbDto){
        return dao.getListByPxid(pxtkglbDto);
    }

}
