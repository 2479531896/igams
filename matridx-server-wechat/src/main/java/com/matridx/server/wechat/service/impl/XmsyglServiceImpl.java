package com.matridx.server.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.XmsyglDto;
import com.matridx.server.wechat.dao.entities.XmsyglModel;
import com.matridx.server.wechat.dao.post.IXmsyglDao;
import com.matridx.server.wechat.service.svcinterface.IXmsyglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class XmsyglServiceImpl extends BaseBasicServiceImpl<XmsyglDto, XmsyglModel, IXmsyglDao> implements IXmsyglService {

    @Override
    public Boolean delInfo(XmsyglDto xmsyglDto) {
        return dao.delInfo(xmsyglDto)!=0;
    }

    @Override
    public Boolean modToNormal(XmsyglDto xmsyglDto) {
        return dao.modToNormal(xmsyglDto)!=0;
    }

    @Override
    public Boolean deleteInfo(XmsyglDto xmsyglDto) {
        return dao.deleteInfo(xmsyglDto)!=0;
    }

    @Override
    public Boolean insertList(List<XmsyglDto> list) {
        return dao.insertList(list) != 0;
    }

    /**
     * Rabbit同步
     * @param list
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void syncRabbitMsg(List<XmsyglDto> list){
        if(list!=null&&list.size()>0){
            XmsyglDto xmsyglDto=new XmsyglDto();
            xmsyglDto.setYwid(list.get(0).getYwid());
            List<XmsyglModel> insertInfo = dao.getModelList(xmsyglDto);
            dao.deleteInfo(xmsyglDto);
            xmsyglDto.setScry(StringUtil.isNotBlank(list.get(0).getXgry())?list.get(0).getXgry():list.get(0).getLrry());
            dao.delInfo(xmsyglDto);
            List<XmsyglDto> insertList =  new ArrayList<>();
            List<String> ids=new ArrayList<>();
            if(insertInfo!=null && insertInfo.size()>0){
                for (XmsyglDto dto : list) {
                    boolean flag = insertInfo.stream ().anyMatch (item->item.getXmsyglid().equals (dto.getXmsyglid()));
                    if (flag){
                        ids.add(dto.getXmsyglid());
                    }else{
                        insertList.add(dto);
                    }
                }
            }else {
                insertList.addAll(list);
            }
            if(!CollectionUtils.isEmpty(ids)){
                XmsyglDto xmsyglDto1 = new XmsyglDto();
                xmsyglDto1.setIds(ids);
                dao.modToNormal(xmsyglDto1);
            }
            if(!CollectionUtils.isEmpty(insertList)){
                dao.insertList(insertList);
            }
        }
    }

    /**
     * 批量更新
     * @param list
     * @return
     */
    public boolean updateList(List<XmsyglDto> list){
        return dao.updateList(list);
    }

}
