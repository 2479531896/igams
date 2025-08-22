package com.matridx.igams.sample.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.sample.dao.entities.*;
import com.matridx.igams.sample.dao.post.IYbkcxxDao;
import com.matridx.igams.sample.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 *@date 2022年06月06日12:32
 *
 */
@Service
public class YbkcxxServiceImpl extends BaseBasicServiceImpl<YbkcxxDto, YbkcxxModel, IYbkcxxDao> implements IYbkcxxService {

    @Autowired
    ISbglService sbglService;
    @Autowired
    IYbllcService ybllcService;
    @Autowired
    IYbllglService ybllglService;
    @Autowired
    IYbllmxService ybllmxService;
    @Autowired
    RedisUtil redisUtil;


    /*
    * 样本库存列表
    * */
    @Override
    public List<YbkcxxDto> getPagedDtoList(YbkcxxDto ybkcxxDto) {
//        sql优化，将sql中jcsj的部分改为java循环放入
//        select jc_yblr.csmc yblrlxmc , jcsj.csmc yblxmc
//        left join matridx_jcsj jc_yblr on jc_yblr.csid = ybkcxx.yblrlx
//        left join matridx_jcsj jcsj on jcsj.csid = sjxx.yblx
        List<YbkcxxDto> ybkcxxDtoList = dao.getPagedDtoList(ybkcxxDto);
        List<JcsjDto> yblrList = redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.SAMPLE_ENTRY_TYPE.getCode());
        List<JcsjDto> yblxList = redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
        List<JcsjDto> jcdwList = redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
        for (YbkcxxDto ybkcxx : ybkcxxDtoList){
            for (JcsjDto yblr_jc : yblrList){
                if (yblr_jc.getCsid().equals(ybkcxx.getYblrlx())){
                    ybkcxx.setYblrlxmc(yblr_jc.getCsmc());
                    break;
                }
            }
            for (JcsjDto yblx_jc : yblxList){
                if (yblx_jc.getCsid().equals(ybkcxx.getYblx())){
                    ybkcxx.setYblxmc(yblx_jc.getCsmc());
                    break;
                }
            }
            for (JcsjDto jcdw_jc : jcdwList){
                if (jcdw_jc.getCsid().equals(ybkcxx.getJcdw())){
                    ybkcxx.setJcdwmc(jcdw_jc.getCsmc());
                    break;
                }
            }
        }
        return ybkcxxDtoList;
    }

    //通过盒子id查询位置list
    @Override
    public List<YbkcxxDto> getWzListByHzid(String hzid) {
        return dao.getWzListByHzid(hzid);
    }

    /*
    * 批量插入样本库存信息
    * */
    @Override
    public boolean insertYbkcxxDtos(List<YbkcxxDto> ybkcxxDtos) {
        return dao.insertYbkcxxDtos(ybkcxxDtos);
    }

    /**
     * 通过样本库存id获取样本明细
     */
    @Override
    public YbkcxxDto queryYbmxByYbkcid(YbkcxxDto ybkcxxDto) {
        return dao.queryYbmxByYbkcid(ybkcxxDto);
    }
    //样本编号模糊查询库存
    @Override
    public List<YbkcxxDto> getYbkcxxByNbbm(String nbbm) {
        return dao.getYbkcxxByNbbm(nbbm);
    }
    /*
    * 修改预定标记
    * */
    @Override
    public boolean updateYdbj(YbkcxxDto ybkcxxDto) {
        return dao.updateYdbj(ybkcxxDto);
    }
    /*
    * 出库时批量更改删除标记
    * */
    @Override
    public boolean deleteListForCK(YbkcxxDto ybkcxxDto) {
        return dao.deleteListForCK(ybkcxxDto);
    }
    /**
     * 样本录入保存
     * @return boolean
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addsaveYbkcxx(YbkcxxDto ybkcxxDto, User user) throws BusinessException{
        SbglDto sbglDto_xgsj = new SbglDto();
        sbglDto_xgsj.setSbid(ybkcxxDto.getHzid());
        sbglDto_xgsj.setXgsj(ybkcxxDto.getHzxgsj());
        sbglDto_xgsj.setXgry(user.getYhid());
        sbglDto_xgsj.setSbh(ybkcxxDto.getHh());
        boolean xgsjSuccess = sbglService.updateForXgsj(sbglDto_xgsj);
        if (!xgsjSuccess){
            throw new BusinessException("msg", "该盒子已被其他人使用，请关闭后重新录入!");
        }
        List<YbkcxxDto> ybkcxxDtos = JSON.parseArray(ybkcxxDto.getYbmx_json(), YbkcxxDto.class);
        int sum = 0;
        for (YbkcxxDto dto : ybkcxxDtos) {
            dto.setYbkcid(StringUtil.generateUUID());
            dto.setLrry(user.getYhid());
            dto.setBxid(ybkcxxDto.getBxid());
            dto.setHzid(ybkcxxDto.getHzid());
            dto.setChtid(ybkcxxDto.getChtid());
            sum++;
        }
        SbglDto sbglDto = new SbglDto();
        sbglDto.setSbid(ybkcxxDto.getHzid());
        sbglDto.setYcfs(sum+"");
        boolean isSuccess = dao.insertYbkcxxDtos(ybkcxxDtos);
        if (!isSuccess){
            throw new BusinessException("msg", "样本录入保存失败!");
        }
        boolean isYcfsSuccess = sbglService.updateYcfs(sbglDto);
        if (!isYcfsSuccess){
            throw new BusinessException("msg", "样本录入保存失败!");
        }
        return true;
    }
    /**
     * 样本领料车保存或样本领料新增
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveYbPickingCar(YbllglDto ybllglDto, User user) throws BusinessException {
        //样本领料管理
        List<YbllglDto> ybllglDtos = JSON.parseArray(ybllglDto.getYbllmx_json(), YbllglDto.class);
        String llid = StringUtil.generateUUID();
        ybllglDto.setLlid(llid);
        //样本库存
        YbkcxxDto ybkcxxDto = new YbkcxxDto();
        List<String> ybkcidlist = new ArrayList<>();
        //样本领料明细
        List<YbllmxDto> ybllmxDtos = new ArrayList<>();
        for (YbllglDto dto : ybllglDtos) {
            YbllmxDto ybllmxDto = new YbllmxDto();
            if (!"".equals(dto.getYbkcid())&&dto.getYbkcid()!=null){
                ybllmxDto.setLlmxid(StringUtil.generateUUID());
                ybllmxDto.setYbkcid(dto.getYbkcid());
                ybllmxDto.setLlid(llid);
                ybllmxDto.setLrry(user.getYhid());
                ybllmxDtos.add(ybllmxDto);
                ybkcidlist.add(ybllmxDto.getYbkcid());
            }
        }
        ybkcxxDto.setIds(ybkcidlist);
        ybllglDto.setZt(StatusEnum.CHECK_NO.getCode());
        //清空领料车
        ybllcService.deleteById(user.getYhid());
        //插入领料管理信息
        boolean isSuccess = ybllglService.insert(ybllglDto);
        if (!isSuccess){
            throw new BusinessException("msg", "保存失败!");
        }
        //插入明细信息
        if (!ybllmxDtos.isEmpty()){
            boolean isSuccesstwo = ybllmxService.insertYbllmxDtos(ybllmxDtos);
            if (!isSuccesstwo){
                throw new BusinessException("msg", "保存失败!");
            }
        }
        //更改预定标记
        if (ybkcxxDto.getIds()!=null&& !ybkcxxDto.getIds().isEmpty()){
            boolean isSuccessThree = this.updateYdbj(ybkcxxDto);
            if (!isSuccessThree){
                throw new BusinessException("msg", "保存失败!");
            }
        }
        return true;
    }
    /*
     * 扫描枪扫描后通过内部编码获取送检信息
     * */

    @Override
    public YbkcxxDto getSjxxByNbbm(String nbbm) {
        return dao.getSjxxByNbbm(nbbm);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveYbkcxx(YbkcxxDto ybkcxxDto) throws BusinessException{
        List<YbkcxxDto> ybkcxxDtos = JSON.parseArray(ybkcxxDto.getYbmx_json(), YbkcxxDto.class);
        ybkcxxDtos.get(0).setYbkcid(ybkcxxDto.getYbkcid());
        ybkcxxDtos.get(0).setXgry(ybkcxxDto.getXgry());
        ybkcxxDtos.get(0).setBxid(ybkcxxDto.getBxid());
        ybkcxxDtos.get(0).setHzid(ybkcxxDto.getHzid());
        ybkcxxDtos.get(0).setChtid(ybkcxxDto.getChtid());
        if (!(ybkcxxDto.getXgqhzid().equals(ybkcxxDto.getHzid())&&ybkcxxDto.getXgqwz().equals(ybkcxxDtos.get(0).getWz()))){
            List<YbkcxxDto> wzListByHzid = dao.getWzListByHzid(ybkcxxDto.getHzid());
            if (!CollectionUtils.isEmpty(wzListByHzid)){
                if (wzListByHzid.size()>80){
                    throw new BusinessException("msg", "修改失败，盒子已满!");
                }
                for (YbkcxxDto dto : wzListByHzid) {
                    if (ybkcxxDtos.get(0).getWz().equals(dto.getWz())){
                        throw new BusinessException("msg", "修改失败，位置冲突!");
                    }
                }
            }
            if (!ybkcxxDto.getXgqhzid().equals(ybkcxxDto.getHzid())){
                SbglDto sbglDto = new SbglDto();
                sbglDto.setYcfs("1");
                sbglDto.setSbid(ybkcxxDto.getHzid());
                sbglDto.setYcfsbj("1");
                boolean isYcfsSuccess = sbglService.updateYcfsForMod(sbglDto);
                if (!isYcfsSuccess){
                    throw new BusinessException("msg", "修改失败,更新已存放数失败!");
                }
                sbglDto.setYcfsbj("0");
                sbglDto.setSbid(ybkcxxDto.getXgqhzid());
                boolean isYcfsSuccess_t = sbglService.updateYcfsForMod(sbglDto);
                if (!isYcfsSuccess_t){
                    throw new BusinessException("msg", "修改失败,更新已存放数失败!");
                }
            }
        }
        int update = dao.update(ybkcxxDtos.get(0));
        if (update<1){
            throw new BusinessException("msg", "修改样本库存信息失败!");
        }
        SbglDto sbglDto = new SbglDto();
        sbglDto.setSbh(ybkcxxDto.getHh());
        sbglDto.setSbid(ybkcxxDto.getHzid());
        sbglDto.setXgry(ybkcxxDto.getXgry());
        boolean updateSb = sbglService.updateSb(sbglDto);
        if (!updateSb){
            throw new BusinessException("msg", "修改设备失败!");
        }
        return true;
    }

    /**
     * 导出条数
     */
    @Override
    public int getCountForSearchExp(YbkcxxDto ybkcxxDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(ybkcxxDto);
    }
	
	
	@Override
    public List<YbkcxxDto> getPagedDtoYds(YbkcxxDto ybkcxxDto) {
        return dao.getPagedDtoYds(ybkcxxDto);
    }

    /**
     * 根据搜索条件获取导出信息
     */
    public List<YbkcxxDto> getListForSearchExp(Map<String, Object> params) {
        YbkcxxDto ybkcxxDto = (YbkcxxDto) params.get("entryData");
        queryJoinFlagExport(params, ybkcxxDto);
        return dao.getListForSearchExp(ybkcxxDto);
    }
    /**
     * 根据选择信息获取导出信息
     */
    public List<YbkcxxDto> getListForSelectExp(Map<String, Object> params) {
        YbkcxxDto ybkcxxDto = (YbkcxxDto) params.get("entryData");
        queryJoinFlagExport(params, ybkcxxDto);
        return dao.getListForSelectExp(ybkcxxDto);
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, YbkcxxDto ybkcxxDto) {
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
        ybkcxxDto.setSqlParam(sqlcs);
    }

    @Override
    public List<Map<String, String>> getJsjcdwByjsid(String dqjs) {
        return dao.getJsjcdwByjsid(dqjs);
    }


    /**
     * 根据盒子id修改预定标记
     * @param ybkcxxDto
     * @return
     */
    @Override
    public boolean updateYdbjByHzids(YbkcxxDto ybkcxxDto){
        return dao.updateYdbjByHzids(ybkcxxDto);
    }
    /**
     * 根据盒子id恢复预定标记
     * @param ybkcxxDto
     * @return
     */
    @Override
    public boolean restoreYdbjByHzids(YbkcxxDto ybkcxxDto){
        return dao.restoreYdbjByHzids(ybkcxxDto);
    }

    /**
     * 根据盒子id获取样本信息
     * @param ybkcxxDto
     * @return
     */
    @Override
     public List<YbkcxxDto> getSampleInfoByHzid(YbkcxxDto ybkcxxDto){
         return dao.getSampleInfoByHzid(ybkcxxDto);
     }

     @Override
     public Map<String,Object> checkHzCanAllot(YbkcxxDto ybkcxxDto){
        return dao.checkHzCanAllot(ybkcxxDto);
     }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modboxSaveYbkcxx(YbkcxxDto ybkcxxDto) throws BusinessException{
        boolean isSuccess = false;
        //①.根据调出盒子id修改样本库存信息表的冰箱id,抽屉id,盒子id.
        //冰箱id=调入冰箱id,抽屉id=调入抽屉id,盒子id=调入盒子id,修改人员=当前登陆用户，修改时间=当前时间。
        isSuccess = dao.updateBox(ybkcxxDto);
        if (!isSuccess){
            throw new BusinessException("msg", "修改盒子失败!");
        }
        //②.根据调出盒子修改设备管理表的已存放数，已存放数=0，
        SbglDto sbglDto_dc = new SbglDto();
        sbglDto_dc.setSbid(ybkcxxDto.getDchzid());
        sbglDto_dc.setYcfs("0");
        sbglDto_dc.setYcfsbj("2");
        isSuccess = sbglService.updateYcfsForMod(sbglDto_dc);
        if (!isSuccess){
            throw new BusinessException("msg", "修改调出盒子已存放数失败!");
        }
        //③.根据调入盒子修改设备管理表的已存放数，已存放数=调出盒子的已存放数。
        SbglDto sbglDto_dr = new SbglDto();
        sbglDto_dr.setSbid(ybkcxxDto.getDrhzid());
        sbglDto_dr.setYcfs(ybkcxxDto.getDcycfs());
        sbglDto_dr.setYcfsbj("2");
        isSuccess = sbglService.updateYcfsForMod(sbglDto_dr);
        if (!isSuccess){
            throw new BusinessException("msg", "修改调入盒子已存放数失败!");
        }
        return true;
    }

    @Override
    public boolean updateYdbjForDelete(YbkcxxDto ybkcxxDto) {
        return dao.updateYdbjForDelete(ybkcxxDto);
    }

    @Override
    public boolean updateScbjForDelete(YbkcxxDto ybkcxxDto) {
        return dao.updateScbjForDelete(ybkcxxDto);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateByDbmx(YbkcxxDto ybkcxxDto) {
        return dao.updateByDbmx(ybkcxxDto);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean deleteBydbmx(YbdbDto ybdbDto) {
        return dao.deleteBydbmx(ybdbDto);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateYbkcxx(YbdbDto ybdbDto) {
        return dao.updateYbkcxx(ybdbDto);
    }
}
