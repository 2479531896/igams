package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;

import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.storehouse.dao.entities.XzdbcglDto;
import com.matridx.igams.storehouse.dao.entities.XzdbglDto;
import com.matridx.igams.storehouse.dao.entities.XzkcxxDto;
import com.matridx.igams.storehouse.dao.entities.XzkcxxModel;

import com.matridx.igams.storehouse.dao.post.IXzkcxxDao;
import com.matridx.igams.storehouse.service.svcinterface.IXzdbcglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzdbglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzkcxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.igams.common.util.DingTalkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class XzkcxxServiceImpl extends BaseBasicServiceImpl<XzkcxxDto, XzkcxxModel, IXzkcxxDao> implements IXzkcxxService {
    @Autowired
    private IXzdbglService xzdbglService;
    @Autowired
    private IXzdbcglService xzdbcglService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    /**
     * 批量新增
     */
    public boolean insertList(List<XzkcxxDto> list) {
        return dao.insertList(list);
    }

    /**
     * 批量修改
     */
    public boolean updateList(List<XzkcxxDto> list) {
        return dao.updateList(list);
    }

    @Override
    public List<XzkcxxDto> getDtoByXzkcids(XzdbcglDto xzdbcglDto) {
        return dao.getDtoByXzkcids(xzdbcglDto);
    }

    @Override
    public XzkcxxDto getDtoByHwmcAndHwbz(XzkcxxDto xzkcxxDto) {
        return dao.getDtoByHwmcAndHwbz(xzkcxxDto);
    }

    @Override
    public boolean updateListByIds(List<XzkcxxDto> list) {
        return dao.updateListByIds(list);
    }

    @Override
    public boolean updateListKcl(List<XzkcxxDto> list) {
        return dao.updateListKcl(list);
    }

    @Override
    public List<XzkcxxDto> getXzkcid(XzkcxxDto xzkcxxDto) {
        return dao.getXzkcid(xzkcxxDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean czDbc(XzdbglDto xzdbglDto, User user) throws BusinessException {
        List<XzkcxxDto> list = new ArrayList<>();
        List<XzkcxxDto> list1 = new ArrayList<>();
        List<XzkcxxDto> list2 = new ArrayList<>();
        List<XzdbcglDto> list4 = new ArrayList<>();
        List<XzdbglDto> xzdbglDtos = JSON.parseArray(xzdbglDto.getDbmx_json(), XzdbglDto.class);
        if (CollectionUtils.isEmpty(xzdbglDtos))
            throw new BusinessException("1","调拨单不能为空");
        for (XzdbglDto xzdbglDto1 : xzdbglDtos) {
            XzdbcglDto xzdbcglDto = new XzdbcglDto();
            XzkcxxDto xzkcxxDto = new XzkcxxDto();
            xzdbglDto1.setXzdbid(StringUtil.generateUUID());
            xzkcxxDto.setDrkw(xzdbglDto1.getDrkw());
            xzkcxxDto.setHwbz(xzdbglDto1.getHwbz());
            xzkcxxDto.setHwmc(xzdbglDto1.getHwmc());
            xzkcxxDto.setKcl(xzdbglDto1.getKcl());
            xzkcxxDto.setDbsl(xzdbglDto1.getDbsl());
            xzkcxxDto.setXzkcid(xzdbglDto1.getXzkcid());
            xzkcxxDto.setXgry(user.getYhid());
            xzkcxxDto.setYds("0");
            xzdbglDto1.setDbry(user.getYhid());
            list.add(xzkcxxDto);
            xzdbcglDto.setXzkcid(xzdbglDto1.getXzkcid());
            xzdbcglDto.setRyid(user.getYhid());
            list4.add(xzdbcglDto);
        }
        boolean iSsuccess = xzdbglService.insertList(xzdbglDtos);
        if (!iSsuccess) {
            throw new BusinessException("2","插入数据失败");
        }
        List<XzkcxxDto> list3 = new ArrayList<>();
        iSsuccess = updateListByIds(list);
        XzkcxxDto xzkcxxDto = new XzkcxxDto();
        List<XzkcxxDto> xzkcxxDtos = getXzkcid(xzkcxxDto);
        for (XzkcxxDto value : list) {
            boolean flag = false;
            for (XzkcxxDto dto : xzkcxxDtos) {
                if (value.getDrkw().equals(dto.getKwid()) && value.getHwbz().equals(dto.getHwbz()) && value.getHwmc().equals(dto.getHwmc())) {
                    list1.add(value);
                    flag = true;
                    break;
                }
            }
            if (!flag)
                list2.add(value);
        }
        if (list2.size() >= 2) {
            int num=list2.size();
            for (int i = 0; i < num; i++) {
                for (int j = i + 1; j < num; j++) {
                    if (!list2.get(i).getDrkw().equals(list2.get(j).getDrkw()) && list2.get(i).getHwbz().equals(list2.get(j).getHwbz()) && list2.get(i).getHwmc().equals(list2.get(j).getHwmc())) {
                        list1.add(list2.get(j));
                        list2.remove(list2.get(j));
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(list2)) {
            for (XzkcxxDto xzkcxxDto2 : list2) {
                XzkcxxDto xzkcxxDto_t = new XzkcxxDto();
                xzkcxxDto_t.setXzkcid(StringUtil.generateUUID());
                xzkcxxDto_t.setKwid(xzkcxxDto2.getDrkw());
                xzkcxxDto_t.setHwbz(xzkcxxDto2.getHwbz());
                xzkcxxDto_t.setHwmc(xzkcxxDto2.getHwmc());
                xzkcxxDto_t.setXgry(xzkcxxDto2.getXgry());
                xzkcxxDto_t.setKcl(xzkcxxDto2.getDbsl());
                xzkcxxDto_t.setYds(xzkcxxDto2.getYds());
                list3.add(xzkcxxDto_t);
            }
            iSsuccess = insertList(list3);
            if (!iSsuccess)
                throw new BusinessException("3","插入数据失败");
        }
         if (!CollectionUtils.isEmpty(list1))
            iSsuccess = updateListKcl(list1);
        if (!iSsuccess) {
            throw new BusinessException("4","更新数据失败");
        }
        iSsuccess = xzdbcglService.deleteList(list4);
        if (!iSsuccess)
            throw new BusinessException("5","删除数据失败");
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean Dbcz(XzdbglDto xzdbglDto, XzkcxxDto xzkcxxDto, User user) throws BusinessException {
        xzdbglDto.setXzdbid(StringUtil.generateUUID());
        xzdbglDto.setDbry(user.getYhid());
        boolean iSsuccess = xzdbglService.insertDto(xzdbglDto);
        if (!iSsuccess)
            throw new BusinessException("1","数据调拨失败");
        XzkcxxDto xzkcxxDto1 = getDto(xzkcxxDto);
        xzkcxxDto1.setDbsl(xzdbglDto.getDbsl());
        iSsuccess=updateDto(xzkcxxDto1);
        if (!iSsuccess)
            throw new BusinessException("2","库存量更新失败");
        XzkcxxDto xzkcxxDto_t = getDtoByHwmcAndHwbz(xzkcxxDto);
        if (xzkcxxDto_t != null) {
            xzkcxxDto_t.setDbsl(xzkcxxDto.getDbsl());
            iSsuccess = update(xzkcxxDto_t);
            if (!iSsuccess)
                throw new BusinessException("3","库存量更新失败");
        } else {
            xzkcxxDto1.setXzkcid(StringUtil.generateUUID());
            xzkcxxDto1.setKcl(xzdbglDto.getDbsl());
            xzkcxxDto1.setYds("0");
            xzkcxxDto1.setXgry(user.getYhid());
            xzkcxxDto1.setHwbz(xzkcxxDto.getHwbz());
            xzkcxxDto1.setHwmc(xzkcxxDto.getHwmc());
            xzkcxxDto1.setKwid(xzkcxxDto.getDrkw());
            iSsuccess = insert(xzkcxxDto1);
            if (!iSsuccess)
                throw new BusinessException("4","新增数据失败");
        }
        return true;
    }

    @Override
    public boolean updateAqkc(XzkcxxDto xzkcxxDto) {
        return dao.updateAqkc(xzkcxxDto);
    }

    @Override
    public List<XzkcxxDto> getXzkcxxs(List<XzkcxxDto> list) {
        return dao.getXzkcxxs(list);
    }

    @Override
    public void updateXzllDtos(List<XzkcxxDto> xzkcxxDtos) {
        dao.updateXzllDtos(xzkcxxDtos);
    }

    @Override
    public void updateListKclById(List<XzkcxxDto> list) {
        dao.updateListKclById(list);
    }

    /**
     * 导出
     *
     * @param xzkcxxDto
     * @return
     */
    public int getCountForSearchExp(XzkcxxDto xzkcxxDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(xzkcxxDto);
    }

    /**
     * 根据搜索条件获取导出信息
     *
     * @return
     */
    public List<XzkcxxDto> getListForSearchExp(Map<String, Object> params) {
        XzkcxxDto xzkcxxDto = (XzkcxxDto) params.get("entryData");
        queryJoinFlagExport(params, xzkcxxDto);
        return dao.getListForSearchExp(xzkcxxDto);
    }

    /**
     * 根据选择信息获取导出信息
     *
     * @return
     */
    public List<XzkcxxDto> getListForSelectExp(Map<String, Object> params) {
        XzkcxxDto xzkcxxDto = (XzkcxxDto) params.get("entryData");
        queryJoinFlagExport(params, xzkcxxDto);
        return dao.getListForSelectExp(xzkcxxDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, XzkcxxDto xzkcxxDto) {
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
        xzkcxxDto.setSqlParam(sqlcs);
    }
}
