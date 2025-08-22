package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SwhtglDto;
import com.matridx.igams.wechat.dao.entities.SwhtglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 * 商务合同管理(IgamsSwhtgl)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-04 13:52:16
 */
@Mapper
public interface ISwhtglDao extends BaseBasicDao<SwhtglDto, SwhtglModel> {


    /**
     * 查询是否重复数据
     */
    List<SwhtglDto> getRepeatDtoList(SwhtglDto swhtglDto);
    /**
     * 根据前缀生成合同编号
     */
    String getContractNumber(String cskz);

    /**
     * 审核列表
     * @param
     * @return
     */
    List<SwhtglDto> getPagedAuditList(SwhtglDto swhtglDto);



    /**
     * 客户合同管理查询合同信息
     * @param
     * @return
     */
    List<SwhtglDto> getKhhtglInfo(SwhtglDto swhtglDto);

    /**
     * 审核信息
     * @param
     * @return
     */
    List<SwhtglDto> getAuditListRecheck(List<SwhtglDto> list);

    /**
     * @description 批量修改商务合同
     * @param swhtglDtos
     * @return
     */
    boolean updateSwhtglDtos(List<SwhtglDto> swhtglDtos);

    /**
     * 根据钉钉实例ID获取特殊申请信息
     * @param
     * @return
     */
    SwhtglDto getDtoByDdslid(SwhtglDto swhtglDto);


    /**
     * 根据审批人ID获取审批人角色信息
     * @param sprid
     * @return
     */
    List<SwhtglDto> getSprjsBySprid(String sprid);

    /**
     * 选中导出
     * @param Map
     * @return
     */
    List<Map<String,Object>> getListForSelectExp(Map<String,Object> map);

    /**
     * 搜索条件分页获取导出信息
     * @param Map
     * @return
     */
    List<Map<String,Object>> getListForSearchExp(Map<String,Object> map);

    /**
     * 获取导出条数
     * @param swhtglDto
     * @return
     */
    int getCountForSearchExp(Map<String,Object> map);

    /**
     * @description 批量修改处理标记
     * @param swhtglDto
     * @return
     */
    boolean updateSfcl(SwhtglDto swhtglDto);
    /**
     * 修改状态
     */
    boolean updateZt(SwhtglDto swhtglDto);

    /**
     * 更新收费标准
     * @param swhtglDto
     * @return
     */
    boolean updateSfbz(SwhtglDto swhtglDto);

    List<SjjcxmDto> getUpListBbHt(SwhtglDto swhtglDto);

    List<SjxxDto> getSjUpListByHt(SwhtglDto swhtglDto);
}

