package com.matridx.crf.web.dao.post;

import com.matridx.crf.web.dao.entities.*;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NdzxxDao extends BaseBasicDao<NdzxxDto, NdzxxModel> {
    public Map<String,Object> getnryjbh(NdzxxDto ndzxxDto);
    /**
     * 患者信息
     * @param ndzxxDto
     * @return
     */
    public NdzxxDto getHzxx(NdzxxDto ndzxxDto);

    /**
     * 记录信息
     * @param ndzxxDto
     * @return
     */
    public NdzxxDto getJlxx(NdzxxDto ndzxxDto);




    /**
     * 新增患者信息
	 * @param ndzxxDto
	 * @return
             */
    public boolean insertHzxx(NdzxxDto ndzxxDto);
    /**
     * 判断插入的数据是否重复
     * @param ndzxxDto
     * @return
     */
    public NdzxxDto getReportRepeat(NdzxxDto ndzxxDto);

    /**
     * 新增报告信息
     * @param ndzxxDto
     * @return
     */
    public boolean insertReport(NdzxxDto ndzxxDto);

    /**
     * 判断插入的患者信息是否重复
     * @param ndzxxDto
     * @return
     */
    public NdzxxDto getHzxxRepeat(NdzxxDto ndzxxDto);

    /**
     * 判断插入的记录信息是否重复
     * @param ndzxxDto
     * @return
     */
    public NdzxxDto getJlxxRepeat(NdzxxDto ndzxxDto);

    /**
     * 获取医院的参数id
     * @param ndzxxDto
     * @return
     */
    public String getYyCsid(NdzxxDto ndzxxDto);
    /**
     * 获取就诊科室的参数id
     * @param ndzxxDto
     * @return
     */
    public String getJzksCsid(NdzxxDto ndzxxDto);

    /**
     * 获取病人类别的参数id
     * @param ndzxxDto
     * @return
     */
    public String getBrlbCsid(NdzxxDto ndzxxDto);

    /**
     * 获取既往合并症的参数id
     * @param jwhbz
     * @return
     */
    public String getJwhbzCsid(String jwhbz);
    /**
     * 获取感染部位的参数id
     * @param grbw
     * @return
     */
    public String getGrbwCsid(String grbw);

    /**
     * 获取出院状态的参数id
     * @param ndzxxDto
     * @return
     */
    public String getCyztCsid(NdzxxDto ndzxxDto);
    /**
     * 获取亚组的参数id
     * @param ndzxxDto
     * @return
     */
    public String getYzCsid(NdzxxDto ndzxxDto);

    /**
     * 获取出ICU状态的参数id
     * @param ndzxxDto
     * @return
     */
    public String getCicuztCsid(NdzxxDto ndzxxDto);
    /**
     * 所属医院统计
     * @param ndzxxDto
     * @return
     */
    public List<NdzxxDto> getPagedSsyy(NdzxxDto ndzxxDto);
    /**
     * 所有所属医院
     * @return
     */
    public List<NdzxxDto> getSsyy();
    /**
     * 所有亚组
     * @return
     */
    public List<NdzxxDto> getYz();
    /**
     * 新增转归情况
     * @param ndzxxDto
     * @return
     */
    public boolean insertZgqk(NdzxxDto ndzxxDto);
    /**
     * 动脉血气
     * @param ndzxxDto
     * @return
     */
    public List<NdzdmxqDto> getDmxq(NdzxxDto ndzxxDto);
    /**
     * 炎症指标
     * @param ndzxxDto
     * @return
     */
    public List<NdzyzzbDto> getYzzb(NdzxxDto ndzxxDto);
    /**
     * 生化
     * @param ndzxxDto
     * @return
     */
    public List<NdzshDto> getSh(NdzxxDto ndzxxDto);
    /**
     * 血常规
     * @param ndzxxDto
     * @return
     */
    public List<NdzxcgDto> getXcg(NdzxxDto ndzxxDto);
    /**
     * 动脉血气参数信息
     * @return
     */
    public List<JcsjDto> getDmxqJcsj();
    /**
     * 炎症指标参数信息
     * @return
     */
    public List<JcsjDto> getYzzbJcsj();
    /**
     * 生化参数信息
     * @return
     */
    public List<JcsjDto> getShJcsj();
    /**
     * 血常规参数信息
     * @return
     */
    public List<JcsjDto> getXcgJcsj();
    /**
     * 新增动脉血气
     * @param ndzdmxqDto
     * @return
     */
    public boolean insertDmxq(NdzdmxqDto ndzdmxqDto);
    /**
     * 新增炎症指标
     * @param ndzyzzbDto
     * @return
     */
    public boolean insertYzzb(NdzyzzbDto ndzyzzbDto);
    /**
     * 新增生化
     * @param ndzshDto
     * @return
     */
    public boolean insertSh(NdzshDto ndzshDto);
    /**
     * 新增血常规
     * @param ndzxcgDto
     * @return
     */
    public boolean insertXcg(NdzxcgDto ndzxcgDto);
    /**
     * 获取上传的文件信息
     * @param ndzxxDto
     * @return
     */
    public FjcfbDto getFjcfb(NdzxxDto ndzxxDto);
    /**
     * 从数据库分页获取导出数据
     * @param ndzxxDto
     * @return
     */
    public List<NdzxxDto> getListForSelectExp(NdzxxDto ndzxxDto);
    /**
     * 根据搜索条件获取导出条数
     * @param ndzxxDto
     * @return
     */
    public int getCountForSearchExp(NdzxxDto ndzxxDto);

    /**
     * 从数据库分页获取导出数据
     * @param ndzxxDto
     * @return
     */
    public List<NdzxxDto> getListForSearchExp(NdzxxDto ndzxxDto);
    /**
     * 既往合并症导出转义
     * @param jwhbz
     * @return
     */
    public String getJwhbzCsmc(String jwhbz);
    /**
     * 感染部位导出转义
     * @param grbw
     * @return
     */
    public String getGrbwCsmc(String grbw);
}
