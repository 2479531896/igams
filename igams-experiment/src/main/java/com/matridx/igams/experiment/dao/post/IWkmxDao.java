package com.matridx.igams.experiment.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.WkglDto;
import com.matridx.igams.experiment.dao.entities.WkmxDto;
import com.matridx.igams.experiment.dao.entities.WkmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IWkmxDao extends BaseBasicDao<WkmxDto, WkmxModel>{

	/**
	 * 根据内部编号查询送检ID
	 */
	List<WkmxDto> getSjxxByNbbh(WkmxDto wkmxDto);
	
	/**
	 * 新增文库明细数据
	 */
	boolean insertWkxx(List<WkmxDto> list);
	
	/**
	 *根据文库ID查询文库明细数据
	 */
	List<WkmxDto> getWkmxByWkid(WkmxDto wkmxDto);
	
	
	/**
	 * 根据文库id删除文库明细数据(修改用)
	 */
	boolean deleteWkmxxx(WkmxDto wkmxDto);
	
	/**
	 * 从数据库分页获取导出送检信息数据
	 */
	public List<WkmxDto> getListForSelectExp(WkmxDto wkmxDto);
	
	/**
	 * 从数据库分页获取导出送检信息数据
	 */
	public List<WkmxDto> getListForSelectExpOld(WkmxDto wkmxDto);
	
	/**
	 * 根据文库id删除文库明细数据(删除文库信息用)
	 */
	boolean deleteWkmxxxlist(WkmxDto wkmxDto);
	
	/**
	 * 根据内部编号查询文库明细数据
	 */
	List<WkmxDto> getWkmxByNbbh(WkmxDto wkmxDto);
	
	/**
	 * 根据文库id和序号更新文库浓度
	 */
	boolean updateWknd(List<WkmxDto> list);
	
	/**
	 * 根据送检id获取文库明细数据
	 */
	List<WkmxDto> getWkmxBySjid(String sjid);
	
	/**
	 * 导入时修改原液浓度
	 */
	boolean updateQuantity(WkmxDto wkmxDto);
	
	/**
	 * 根据内部编号查找送检检测项目信息
	 */
	WkmxDto getSjDtoByNbbh(WkmxDto wkmxDto);

	/**
	 * 文库列表查找出用来对接PCR的字段
	 */
	List<WkmxDto> getDtoForPcrReady(WkmxDto wkmxDto);
	//获取需要发送给pcr的文库明细信息
	List<Map<String,String>> getWkmxTopcr(WkmxDto wkmxDto);

	/**
	 * 修改pcr结
	 */
	boolean updateWkmxResult(WkmxDto wkmxDto);
	WkmxDto getWkmxFristBynbbh(WkmxDto wkmxDto);

	/**
	 * 根据文库id删除文库明细数据（删除标记置为2）
	 */
    boolean delMergeWkmxlist(WkmxDto wkmxDto);

	/**
	 * 撤销合并的文库明细数据
	 */
    boolean cancelMergeWkmxlist(WkglDto wkgl);

	/**
	 * 根据IDs获取文库明细
	 */
    List<WkmxDto> getWkmxByIds(WkmxDto wkmxDto);

	/**
	 * 更新送检实验管理表接头数据
	 */
	boolean updateDetectJt(List<WkmxDto> list);
	/**
	 * 更新送检实验管理表接头数据
	 */
	boolean removeDetectJt(WkmxDto wkmxDto);
	/**
	 * 验证是否存在相同syglid
	 */
	List<WkmxDto> verifySame(WkmxDto wkmxDto);

	/**
	 * 文库上机数据
	 */
    List<WkmxDto> getWksjDtoList(WkmxDto wkmxDto);

	/**
	 * 根据接头获取文库明细信息
	 */
	List<WkmxDto> getWkmxByJt(WkmxDto wkmxDto);

	/**
	 * 根据syglids获取实验信息
	 */
	List<WkmxDto> getInfoBySyglids(WkmxDto wkmxDto);

	/**
	 * 根据sjsyid更新实验日期
	 */
	boolean updateSyglSyrq(List<WkmxDto> list);
	/**
	 * 根据扩展参数更新送检信息表
	 */
	boolean updateSjxxSyrq(List<WkmxDto> list);
	/**
	 * 根据扩展参数更新送检信息表
	 */
	boolean updateFjsqSyrq(List<WkmxDto> list);

	/**
	 *根据实验管理IDs查询实验数据
	 */
	List<WkmxDto> getDetectionInfo(WkmxDto wkmxDto);

	/**
	 * 根据送检ID更新送检接头信息
	 * @param list
	 * @return
	 */
	boolean updateJtxxBySjid(List<Map<String,String>> list);

	/**
	 * 获取送检信息和送检扩展信息
	 * 后期根据需求在增加其他字段
	 * @param map
	 * @return
	 */
	List<Map<String,String>>getSjxxInfo(Map<String,Object> map);


	boolean updateSjkzxx(List<Map<String,String>> list);

	/**
	 * 根据文库条码里的内部编码和后缀名称从sjsygl表获取项目代码
	 * @param params
	 * @return
	 */
	List<Map<String, String>> getXmdmFromSjsyByWk(Map<String, String> params);

	/**
	 * 根据文库条码里的内部编码和后缀名称从项目管理表获取项目代码
	 * @param params
	 * @return
	 */
	List<Map<String, String>> getXmdmFromSjxmByWk(Map<String, String> params);

	/**
	 * 根据文库条码里的内部编码和后缀名称从信息对应表获取项目代码
	 * @param params
	 * @return
	 */
	List<Map<String, String>> getXmdmFromXxdyByWk(Map<String, String> params);
}
