package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.dao.entities.WkmxPcrModel;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjyzjgDto;
import com.matridx.igams.wechat.dao.entities.SjyzjgModel;

import java.util.List;


public interface ISjyzjgService extends BaseBasicService<SjyzjgDto, SjyzjgModel> {
    /**
     * 根据验证id删除
     * @param sjyzjgDto
     * @return
     */
    boolean deleteByYzid(SjyzjgDto sjyzjgDto);


    /**
     * 接受并保存pcr实验返回的消息
     * @param
     * @return
     */
    boolean getSjyzmxResult(WkmxPcrModel wkmxPcrModel);

    /**
     * 通过样本编号获取相关的信息
     * @return
     */
    List<SjyzjgDto> getInfoByYbbh(SjyzjgDto sjyzjgDto);

    /**
     * 通过ct值返回结论
     * @return
     */
    String judgmentConclusion(String ct);

    /**
     * 通过CT值得出结论
     * @param ctVaule
     * @return
     */
    String drawAconclusion(String ctVaule);

    /**
     * 根据yzid，yzlb，yzjg更新jl字段
     * @param sjyzjgDtoList
     * @return
     */
    boolean updateListJl(List<SjyzjgDto> sjyzjgDtoList);

    /**
     * 通过验证ID、类别、结果获取送检验证结果数据
     * @param sjyzjgDtoList
     * @return
     */
    List<SjyzjgDto> getByYzlbYzidYzjg(List<SjyzjgDto> sjyzjgDtoList);

    /**
     * 批量新增送检验证结果数据
     * @param updateSjyzjgList
     */
    void insertList(List<SjyzjgDto> updateSjyzjgList);
}
