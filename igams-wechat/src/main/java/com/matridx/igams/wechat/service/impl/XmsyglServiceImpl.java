package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.XmsyglDto;
import com.matridx.igams.wechat.dao.entities.XmsyglModel;
import com.matridx.igams.wechat.dao.post.IXmsyglDao;
import com.matridx.igams.wechat.service.svcinterface.IXmsyglService;
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
    public Boolean modToNormal(List<XmsyglDto> xmsyglDtos) {
        return dao.modToNormal(xmsyglDtos)!=0;
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
     * 批量更新
     * @param list
     * @return
     */
    public boolean updateList(List<XmsyglDto> list){
        return dao.updateList(list);
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
            List<XmsyglDto> updateXmsyDtos=new ArrayList<>();
            if(insertInfo!=null && insertInfo.size()>0){
                for (XmsyglDto dto : list) {
                    boolean flag = insertInfo.stream ().anyMatch (item->item.getXmsyglid().equals (dto.getXmsyglid()));
                    if (flag){
                    	XmsyglDto xmsyglDto1 = new XmsyglDto();
                        xmsyglDto1.setXmsyglid(dto.getXmsyglid());
                        xmsyglDto1.setYwid(dto.getYwid());
                        updateXmsyDtos.add(xmsyglDto1);
                    }else{
                        insertList.add(dto);
                    }
                }
            }else {
                insertList.addAll(list);
            }
            if(!CollectionUtils.isEmpty(updateXmsyDtos)){
                dao.modToNormal(updateXmsyDtos);
            }
            if(!CollectionUtils.isEmpty(insertList)){
                dao.insertList(insertList);
            }
        }
    }
    
    /**
     * 根据业务IDs删除数据
     * @param sjsyglDto
     * @return
     */
    public int deleteByYwids(XmsyglDto xmsyglDto){
        return dao.deleteByYwids(xmsyglDto);
    }

    /**
     * 根据实验管理ids获取数据
     * @param xmsyglDto
     * @return
     */
    public List<XmsyglDto> getDtoListBySyglids(XmsyglDto xmsyglDto){
        return dao.getDtoListBySyglids(xmsyglDto);
    }

    /**
     * 根据ywids获取数据
     * @param xmsyglDto
     * @return
     */
    public List<XmsyglDto> getDtoListByYwids(XmsyglDto xmsyglDto){
        return dao.getDtoListByYwids(xmsyglDto);
    }

    /**
     * 获取样本的信息对应信息
     * @param xmsyglDto
     * @return
     */
    public List<XmsyglDto> getXmsyXXdyYwids(XmsyglDto xmsyglDto){
        return dao.getXmsyXXdyYwids(xmsyglDto);
    }
}
