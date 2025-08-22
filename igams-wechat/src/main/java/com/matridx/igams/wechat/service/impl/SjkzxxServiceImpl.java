package com.matridx.igams.wechat.service.impl;


import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjkzxxDto;
import com.matridx.igams.wechat.dao.entities.SjkzxxModel;
import com.matridx.igams.wechat.dao.post.ISjkzxxDao;
import com.matridx.igams.wechat.service.svcinterface.ISjkzxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SjkzxxServiceImpl extends BaseBasicServiceImpl<SjkzxxDto, SjkzxxModel, ISjkzxxDao> implements ISjkzxxService {


    /**
     * 查询对账明细
     * @param params
     * @return
     */
    public List<Map<String,Object>> getDtoListOptimize(Map<String, Object> params){
        return dao.getDtoListOptimize(params);
    }

    /**
     * 标本实验列表分页
     *
     * @return
     */
    @Override
    public List<SjkzxxDto> getPagedSpecimenExperimentalList(SjkzxxDto sjkzxxDto){
        return dao.getPagedSpecimenExperimentalList(sjkzxxDto);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void copySjkzxx(SjkzxxDto copy1, SjkzxxDto copy2, User user) {
        SjkzxxDto copySjkzxxDto=new SjkzxxDto();
        copySjkzxxDto.setQtxx(copy2.getQtxx());
        copySjkzxxDto.setSjid(copy1.getSjid());
        copySjkzxxDto.setXgry(user.getYhid());
        dao.update(copySjkzxxDto);
        SjkzxxDto copySjkzxxDto1=new SjkzxxDto();
        copySjkzxxDto1.setQtxx(copy1.getQtxx());
        copySjkzxxDto1.setSjid(copy2.getSjid());
        copySjkzxxDto1.setXgry(user.getYhid());
        dao.update(copySjkzxxDto1);
    }

    @Override
    public SjkzxxDto getSjkzxxBySjid(SjkzxxDto sjkzxxDto) {
        return dao.getSjkzxxBySjid(sjkzxxDto);
    }

    @Override
    public List<Map<String, String>> getTsWzlx() {
        return dao.getTsWzlx();
    }

    /**
     * 根据选择信息获取导出信息
     * @param params
     * @return
     */
    public List<SjkzxxDto> getListForSelectExp(Map<String,Object> params){
        SjkzxxDto sjkzxxDto = (SjkzxxDto)params.get("entryData");
        queryJoinFlagExport(params,sjkzxxDto);
        return dao.getListForSelectExp(sjkzxxDto);
    }


    /**
     * 根据搜索条件获取导出条数
     *
     * @param sjkzxxDto
     * @return
     */
    public int getCountForSearchExp(SjkzxxDto sjkzxxDto,Map<String, Object> params) {
        return dao.getCountForSearchExp(sjkzxxDto);
    }
    /**
     * 根据搜索条件分页获取导出信息
     *
     * @param params
     * @return
     */

    public List<SjkzxxDto> getListForSearchExp(Map<String, Object> params)
    {
        SjkzxxDto sjkzxxDto = (SjkzxxDto)params.get("entryData");
        queryJoinFlagExport(params,sjkzxxDto);

        return dao.getListForSearchExp(sjkzxxDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String,Object> params,SjkzxxDto sjkzxxDto){
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuffer sqlParam = new StringBuffer();
        for(DcszDto dcszDto:choseList){
            if(dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs=sqlParam.toString();
        sjkzxxDto.setSqlParam(sqlcs);
    }
}
