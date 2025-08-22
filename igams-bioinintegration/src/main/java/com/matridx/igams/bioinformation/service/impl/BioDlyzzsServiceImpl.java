package com.matridx.igams.bioinformation.service.impl;

import com.matridx.igams.bioinformation.dao.entities.BioDlyzzsDto;
import com.matridx.igams.bioinformation.dao.entities.BioDlyzzsModel;
import com.matridx.igams.bioinformation.dao.post.IBioDlyzzsDao;
import com.matridx.igams.bioinformation.service.svcinterface.IBioDlyzzsService;
import com.matridx.igams.bioinformation.util.JDBCUtils;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BioDlyzzsServiceImpl extends BaseBasicServiceImpl<BioDlyzzsDto, BioDlyzzsModel, IBioDlyzzsDao> implements IBioDlyzzsService {

    /**
     * 插入
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertDto(BioDlyzzsDto bioDlyzzsDto){
        bioDlyzzsDto.setDljyid(StringUtil.generateUUID());
        int result = dao.insert(bioDlyzzsDto);
        return result != 0;
    }

    /**
     * 修改
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateDto(BioDlyzzsDto bioDlyzzsDto){
        int result = dao.update(bioDlyzzsDto);
        return result != 0;
    }

    /**
     * 删除
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean deleteDto(BioDlyzzsDto bioDlyzzsDto){
        int result = dao.delete(bioDlyzzsDto);
        return result != 0;
    }

    /**
     * 更新毒力因子注释表
     */
    public void updateDlyzzs(){
        JDBCUtils jdbcUtils=new JDBCUtils();
        List<BioDlyzzsDto> list=jdbcUtils.queryDlyzzsData();
        List<BioDlyzzsDto> newList= new ArrayList<>();
        if( list != null && list.size()>0 ){
            if(list.size()>100){
                for(int a=1;a<=list.size();a++){
                    newList.add(list.get(a-1));
                    if(a!=0&&a%100==0){
                        dao.updateList(newList);
                        newList.clear();
                    }
                }
                if(newList.size()>0){
                    dao.updateList(newList);
                }
            }else{
                dao.updateList(list);
            }
        }
    }
}
