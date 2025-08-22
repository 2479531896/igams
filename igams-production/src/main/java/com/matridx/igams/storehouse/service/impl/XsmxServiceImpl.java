package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.XsmxDto;
import com.matridx.igams.storehouse.dao.entities.XsmxModel;
import com.matridx.igams.storehouse.dao.post.IXsmxDao;
import com.matridx.igams.storehouse.service.svcinterface.IXsmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class XsmxServiceImpl extends BaseBasicServiceImpl<XsmxDto, XsmxModel, IXsmxDao> implements IXsmxService {

    /**
     * 废弃按钮
     *
     * @param xsmxDto
     */
    public void discard(XsmxDto xsmxDto){
        dao.discard(xsmxDto);
    }

    /**
     * 批量新增
     * @param list
     * @return
     */
    public boolean insertList(List<XsmxDto> list){
        return dao.insertList(list);
    }

    /**
     * 根据Id查询信息
     * @param xsmxDto
     * @return
     */
    public List<XsmxDto> getListById(XsmxDto xsmxDto){
        return dao.getListById(xsmxDto);
    }

    @Override
    public Boolean updateXsmxList(List<XsmxDto> xsmxDtos) {
        return dao.updateXsmxList(xsmxDtos) != 0;
    }

    /**
     * 批量更新
     * @param list
     * @return
     */
    public int updateList(List<XsmxDto> list){
        return dao.updateList(list);
    }

    @Override
    public List<XsmxDto> getPagedDtoListDetails(XsmxDto xsmxDto) {
        return dao.getPagedDtoListDetails(xsmxDto);
    }

    /**
     * 根据选择信息获取导出信息
     * @return
     */
    public List<XsmxDto> getListForSelectExp(Map<String,Object> params){
        XsmxDto xsmxDto = (XsmxDto)params.get("entryData");
        queryJoinFlagExport(params,xsmxDto);
        return dao.getListForSelectExp(xsmxDto);
    }


    /**
     * 根据搜索条件获取导出条数
     *
     * @param xsmxDto
     * @return
     */
    public int getCountForSearchExp(XsmxDto xsmxDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(xsmxDto);
    }
    /**
     * 根据搜索条件分页获取导出信息
     *
     * @return
     */

    public List<XsmxDto> getListForSearchExp(Map<String, Object> params)
    {
        XsmxDto xsmxDto = (XsmxDto)params.get("entryData");
        queryJoinFlagExport(params,xsmxDto);

        return dao.getListForSearchExp(xsmxDto);
    }
    /**
     * 销售报表导出
     * @return
     */
    public List<XsmxDto> getListForReportSearchExp(Map<String, Object> params)
    {
        XsmxDto xsmxDto = (XsmxDto)params.get("entryData");
        queryJoinFlagExport(params,xsmxDto);

        return dao.getListForReportSearchExp(xsmxDto);
    }
    /**
     * 根据搜索条件获取导出条数
     *
     * @param xsmxDto
     * @return
     */
    public int getCountForReportSearchExp(XsmxDto xsmxDto, Map<String, Object> params) {
        return dao.getCountForReportSearchExp(xsmxDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String,Object> params,XsmxDto xsmxDto){
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
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
        xsmxDto.setSqlParam(sqlcs);
    }
	@Override
    public String escapeWlbm(XsmxDto xsmxDto) {
        return dao.escapeWlbm(xsmxDto);
    }

    @Override
    public String escapecplx(XsmxDto xsmxDto) {
        return dao.escapecplx(xsmxDto);
    }

    @Override
    public List<XsmxDto> getDkxxByXsid(XsmxDto xsmxDto) {
        return dao.getDkxxByXsid(xsmxDto);
    }

    @Override
    public List<XsmxDto> getXsmxByXs(XsmxDto xsmxDto) {
        return dao.getXsmxByXs(xsmxDto);
    }

    @Override
    public boolean updateDkxx(XsmxDto xsmxDto) {
        return dao.updateDkxx(xsmxDto);
    }

    @Override
    public List<XsmxDto> getDkxxGroup(XsmxDto xsmxDto) {
        return dao.getDkxxGroup(xsmxDto);
    }

    @Override
    public List<XsmxDto> getAllDkjeGroupXs(XsmxDto xsmxDto) {
        return dao.getAllDkjeGroupXs(xsmxDto);
    }
}
