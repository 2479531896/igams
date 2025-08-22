
package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.XszbDto;
import com.matridx.igams.wechat.dao.entities.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISjxxTwoDao extends BaseBasicDao<SjxxDto, SjxxModel>{



	/**
	 * 获取ResFirst列表数据
	 * @param resFirstDto
	 * @return
	 */
    List<ResFirstDto> getPagedResFirstList(ResFirstDto resFirstDto);
	/**
	 * 获取审核列表
	 * @param
	 * @return
	 */
	List<Map<String,String>> getPagedAuditDevice(SjxxDto sjxxDto);

	/**
	 * 审核列表
	 * @param
	 * @return
	 */
	List<SjxxDto> getAuditListDevice(List<Map<String,String>> sjxxDtos);

	/**
	 * 获取实验列表数据
	 * @param
	 * @return
	 */
    List<SjxxDto> getExperimentList(SjxxDto sjxxDto);

	/**
	 * 实验统计数量
	 * @return
	 */
	Map<String, Object> queryExperimentNums(SjxxDto sjxxDto);
	/**
	 * 从数据库分页获取导出实验数据
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListForSearchExpSy(SjxxDto sjxxDto);

	/**
	 * 批量更新
	 * @return
	 */
    Boolean updateList(List<SjxxDto> list);
	/**
	 * 根据搜索条件获取导出实验列表条数
	 * @param sjxxDto
	 * @return
	 */
    int getCountForSearchExpSy(SjxxDto sjxxDto);

	/**
	 * 选中导出送检信息数据实验列表
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListForSelectExpSy(SjxxDto sjxxDto);

    List<SjxxDto> getPagedSalesResFirstList(SjxxDto sjxxDto);
	/**
	 * 全国趋势
	 */
	List<Map<String,String>> getAllCountryChanges(SjxxDto sjxxDto);
	/**
	 * 产品趋势
	 */
	List<Map<String,Object>> getProductionChanges(SjxxDto sjxxDto);
		/**
	 * @description 根据伙伴分类和平台获取第三方Top20伙伴
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getTopDsf20(SjxxDto sjxxDto);
	/**
	 * @description 根据伙伴分类和平台获取直销Top20伙伴
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getTopZx20(SjxxDto sjxxDto);
	/**
	 * @description 根据伙伴分类和平台获取CSOTop20伙伴
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getTopCSO20(SjxxDto sjxxDto);
	/**
	 * @description 获取入院信息
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getTopRY20(SjxxDto sjxxDto);
		/**
	 * 平台趋势
	 */
	List<Map<String,Object>> getPlatformChanges(SjxxDto sjxxDto);

	/**
	 * 模糊查询内部编码
	 * @param nbbm
	 * @return
	 */
    List<String> getSjxxByLikeNbbm(String nbbm);
	/**
	 * 平台业务占比
	 */
	List<Map<String,Object>> getPlatformProportion(XszbDto xszbDto);
	/**
	 * 平台业务占比
	 */
	List<Map<String,Object>> getProductionProportion(XszbDto xszbDto);
	/**
	 * 收费测试数 科室分类(直销+代理)
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, Object>> getChargesDivideByKs(SjxxDto sjxxDto);
	/**
	 * 收费测试数 样本分类(直销+代理)
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, Object>> getChargesDivideByYblx(SjxxDto sjxxDto);

	/**
	 * 核心医院的收费检测数(先展示测试数前10的）
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, Object>> getHxyyTopList(SjxxDto sjxxDto);
	/**
	 * @description 销售端平台趋势
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getAllCountryChangesForSale(SjxxDto sjxxDto);


	/**
	 * 根据searchMap查询送检信息数据
	 * @param searchMap
	 * @return
	 */
	List<Map<String,Object>> getEntrustDtoList(Map<String,Object> searchMap);

	/**
	 * 根据barcode和检测项目代码查询送检信息数据
	 * @param searchMap
	 * @return
	 */
	List<Map<String,Object>> getSampleInfoByBarcode(Map<String,Object> searchMap);

	/**
	 * 更新报告信息
	 * @param
	 * @return
	 */
	int updateReportInfo(Map<String,Object> reportUpdateMap);

	List<Map<String,Object>> getDateSjxxList(String dateStr);
}
