package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.FfjlDto;
import com.matridx.igams.production.dao.entities.FfjlModel;
import com.matridx.igams.production.dao.entities.WjglDto;
import com.matridx.igams.production.dao.post.IFfjlDao;
import com.matridx.igams.production.service.svcinterface.IFfjlService;
import com.matridx.igams.production.service.svcinterface.IWjglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class FfjlServiceImpl extends BaseBasicServiceImpl<FfjlDto, FfjlModel, IFfjlDao> implements IFfjlService {
    @Lazy
    @Autowired
    IWjglService wjglService;

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean printgrantSaveDocument(FfjlDto ffjlDto) throws BusinessException {
        List<FfjlDto> ffjlDtos = JSON.parseArray(ffjlDto.getFfmx_json(), FfjlDto.class);
        if (!CollectionUtils.isEmpty(ffjlDtos)){
            //修改前发放记录
            List<FfjlDto> yffjlDtos = dao.getDtoList(ffjlDto);
            List<FfjlDto> addFfjlDtos = new ArrayList<>();
            List<FfjlDto> modFfjlDtos = new ArrayList<>();
            StringBuilder sfff = new StringBuilder();
            String ffzt;
            for (FfjlDto dto : ffjlDtos) {
                sfff.append(",").append(dto.getSfff());
                if (StringUtil.isBlank(dto.getFfjlid())){
                    dto.setWjid(ffjlDto.getWjid());
                    dto.setFfjlid(StringUtil.generateUUID());
                    addFfjlDtos.add(dto);
                }else {
                    modFfjlDtos.add(dto);
                }
                //剩下的是删除的
                yffjlDtos.removeIf(next -> next.getFfjlid().equals(dto.getFfjlid()));
            }
            if (sfff.length()>0){
                sfff = new StringBuilder(sfff.substring(1));
            }
            //全是是没有否
            if (sfff.indexOf("1")!=-1&&sfff.indexOf("0")==-1){
                ffzt = "1";
                //有是 也有否
            }else if (sfff.indexOf("1")!=-1&&sfff.indexOf("0")!=-1){
                ffzt = "2";
            }else {
                ffzt = "0";
            }

            if (!CollectionUtils.isEmpty(addFfjlDtos)){
                boolean isSuccess = dao.insertFfjlDtos(addFfjlDtos);
                if (!isSuccess){
                    throw new BusinessException("msg","新增发放记录失败！");
                }
            }
            if (!CollectionUtils.isEmpty(modFfjlDtos)){
                boolean isSuccess = dao.updateFfjlDtos(modFfjlDtos);
                if (!isSuccess){
                    throw new BusinessException("msg","修改发放记录失败！");
                }
            }
            if (!CollectionUtils.isEmpty(yffjlDtos)){
                FfjlDto ffjlDto_del = new FfjlDto();
                List<String> ids = new ArrayList<>();
                for (FfjlDto yffjlDto : yffjlDtos) {
                    ids.add(yffjlDto.getFfjlid());
                }
                ffjlDto_del.setIds(ids);
                boolean isSuccess = dao.deleteByIds(ffjlDto_del);
                if (!isSuccess){
                    throw new BusinessException("msg","删除发放记录失败！");
                }
            }
            WjglDto wjglDto = new WjglDto();
            wjglDto.setWjid(ffjlDto.getWjid());
            wjglDto.setFfzt(ffzt);
            boolean isSuccess = wjglService.updateFfzt(wjglDto);
            if (!isSuccess){
                throw new BusinessException("msg","修改文件发放状态失败！");
            }
        }else {
            delete(ffjlDto);
            WjglDto wjglDto = new WjglDto();
            wjglDto.setWjid(ffjlDto.getWjid());
            wjglDto.setFfzt(null);
            boolean isSuccess = wjglService.updateFfzt(wjglDto);
            if (!isSuccess){
                throw new BusinessException("msg","修改文件发放状态失败！");
            }
        }
        return true;
    }

    @Override
    public void insertFfjlDtos(List<FfjlDto> ffjlDtos) {
        dao.insertFfjlDtos(ffjlDtos);
    }
}
