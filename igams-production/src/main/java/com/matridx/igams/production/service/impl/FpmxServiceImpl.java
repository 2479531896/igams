package com.matridx.igams.production.service.impl;


import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.FpmxDto;
import com.matridx.igams.production.dao.entities.FpmxModel;
import com.matridx.igams.production.dao.post.IFpmxDao;
import com.matridx.igams.production.service.svcinterface.IFpmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FpmxServiceImpl extends BaseBasicServiceImpl<FpmxDto, FpmxModel, IFpmxDao> implements IFpmxService {

    /**
     * 多条添加发票明细
     */
    public boolean insertList(List<FpmxDto> list){
        return dao.insertList(list);
    }

    /**
     * 根据发票ID删除记录
     */
    public void deleteByFpid(FpmxDto fpmxDto){
        dao.deleteByFpid(fpmxDto);
    }

    /**
     * 根据合同明细ID查找总数量
     */
    public String getSumById(String htmxid){
        return dao.getSumById(htmxid);
    }
    /**
     * 查找入库信息
     */
    public List<FpmxDto> getListForRk(FpmxDto fpmxDto){
        return dao.getListForRk(fpmxDto);
    }

    @Override
    public List<FpmxDto> getRkListByFp(FpmxDto fpmxDto) {
        return dao.getRkListByFp(fpmxDto);
    }

    /**
     * 通过htfkid查找信息
     */
    public List<FpmxDto> getListByHtfkid(FpmxDto fpmxDto){
        return dao.getListByHtfkid(fpmxDto);
    }

    /**
     * 通过htmxid查找信息
     */
    public List<FpmxDto> getListByHtmxid(FpmxDto fpmxDto){
        return dao.getListByHtmxid(fpmxDto);
    }

    /**
     * 查找货物信息
     */
    public List<FpmxDto> getListForHw(FpmxDto fpmxDto){
        return dao.getListForHw(fpmxDto);
    }

    /**
     * 通过htid分组查询总金额
     */
    public List<FpmxDto> getSumGroupByHtid(FpmxDto fpmxDto){
        return dao.getSumGroupByHtid(fpmxDto);
    }

    /**
     * 获取明细
     */
    public List<FpmxDto> getInvoiceList(FpmxDto fpmxDto){
        return dao.getInvoiceList(fpmxDto);
    }

    /**
     * 根据搜索条件获取导出信息
     */
    public List<FpmxDto> getListForSearchExp(Map<String, Object> params) {
        FpmxDto fpmxDto = (FpmxDto) params.get("entryData");
        queryJoinFlagExport(params, fpmxDto);
        List<FpmxDto> list=dao.getListForSearchExp(fpmxDto);
        List<FpmxDto> mxlist=new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            if(!CollectionUtils.isEmpty(list)){
                FpmxDto fpmxDto_t=list.get(0);
                StringBuilder u8ids= new StringBuilder();
                for(int i=0;i<list.size();i++){
                    if (!fpmxDto_t.getFpmxid().equals(list.get(i).getFpmxid())) {
                        fpmxDto_t.setU8rkdh(u8ids.toString());
                        mxlist.add(fpmxDto_t);
                        fpmxDto_t = list.get(i);
                        u8ids = new StringBuilder();
                        FpmxDto fpmxDto1 = new FpmxDto();
                        fpmxDto1.setRkid(list.get(i).getRkid());
                        fpmxDto1.setDhid(list.get(i).getDhid());
                        fpmxDto1.setU8rkdh(list.get(i).getU8rkdh());
                        fpmxDto1.setLbcskz1(list.get(i).getLbcskz1());
                    }
                    u8ids.append("/").append(list.get(i).getU8rkdh());
                    if(i==list.size()-1){
                        fpmxDto_t.setU8rkdh(u8ids.toString());
                        mxlist.add(fpmxDto_t);
                    }
                }
            }
        }
        return mxlist;
    }

    /**
     * 根据选择信息获取导出信息
     *
     * @param params
     
     */
    public List<FpmxDto> getListForSelectExp(Map<String, Object> params) {
        FpmxDto fpmxDto = (FpmxDto) params.get("entryData");
        queryJoinFlagExport(params, fpmxDto);
        List<FpmxDto> list=dao.getListForSelectExp(fpmxDto);
        List<FpmxDto> mxlist=new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            if(!CollectionUtils.isEmpty(list)){
                FpmxDto fpmxDto_t=list.get(0);
               StringBuilder u8ids= new StringBuilder();
                for(int i=0;i<list.size();i++){
                    if (!fpmxDto_t.getFpmxid().equals(list.get(i).getFpmxid())) {
                        fpmxDto_t.setU8rkdh(u8ids.toString());
                        mxlist.add(fpmxDto_t);
                        fpmxDto_t = list.get(i);
                        u8ids = new StringBuilder();
                        FpmxDto fpmxDto1 = new FpmxDto();
                        fpmxDto1.setRkid(list.get(i).getRkid());
                        fpmxDto1.setDhid(list.get(i).getDhid());
                        fpmxDto1.setU8rkdh(list.get(i).getU8rkdh());
                        fpmxDto1.setLbcskz1(list.get(i).getLbcskz1());
                    }
                    u8ids.append("/").append(list.get(i).getU8rkdh());
                    if(i==list.size()-1){
                        fpmxDto_t.setU8rkdh(u8ids.toString());
                        mxlist.add(fpmxDto_t);
                    }
                }
            }
        }
        return mxlist;
    }

    /**
     * 导出
     *
     * @param fpmxDto
     
     */
    public int getCountForSearchExp(FpmxDto fpmxDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(fpmxDto);
    }


    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, FpmxDto fpmxDto) {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for (DcszDto dcszDto : choseList) {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs = sqlParam.toString();
        fpmxDto.setSqlParam(sqlcs);
    }


}
