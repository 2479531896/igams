package com.matridx.igams.experiment.service.svcinterface;


import com.matridx.igams.common.dao.entities.WkmxPcrModel;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.WkglDto;
import com.matridx.igams.experiment.dao.entities.WkmxDto;
import com.matridx.igams.experiment.dao.entities.WkmxModel;

import java.util.List;
import java.util.Map;

public interface IWkmxService extends BaseBasicService<WkmxDto, WkmxModel>{

	
	/**
	 * 新增保存文库信息
	 */
	boolean addSaveWkxx(WkmxDto wkmxDto) throws BusinessException;
	
	/**
	 *根据文库ID查询文库明细数据
	 */
	List<WkmxDto> getWkmxByWkid(WkmxDto wkmxDto);
	
	/**
	 * 根据wkid和序号更新文库明细数据
	 */
	boolean updateWkmxByWkidAndXh(WkmxDto wkmxDto) throws BusinessException;
	
	/**
	 * 根据内部编号查询送检ID
	 */
	List<WkmxDto> getSjxxByNbbh(WkmxDto wkmxDto);
	
	/**
	 * 根据文库id删除文库明细数据(修改用)
	 */
	boolean deleteWkmxxx(WkmxDto wkmxDto);
	
	/**
	 * 新增前校验内部编号是否存在
	 */
	List<String> exitnbbh(WkmxDto wkmxDto) throws BusinessException;
	
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
	 * 根据内部编号查找送检检测项目信息
	 */
	WkmxDto getSjDtoByNbbh(WkmxDto wkmxDto);

	/**
	 * 文库列表查找用来对接PCR的字段
	 */
	List<WkmxDto> getDtoForPcrReady(WkmxDto wkmxDto);
	/**
	 * 发送开始实验消息
	 */
	boolean sendLibrary(WkmxDto wkmxDto);
	/**
	 * 接受并保存pcr实验返回的消息
	 */
	boolean getWkmxDtoFromPcr(WkmxPcrModel wkmxPcrModel);

	/**
	 * 文库明细数据删除标记置为2
	 */
	boolean delMergeWkmxlist(WkmxDto wkmxDto);

	/**
	 * 撤销合并的文库明细数据
	 */
	boolean cancelMergeWkmxlist(WkglDto wkgl);

	/**
	 * 根据Ids获取文库明细数据
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
	 * 新增文库明细数据
	 */
	boolean insertWkxx(List<WkmxDto> list);

	/**
	 * 根据接头获取文库明细信息
	 */
	List<WkmxDto> getWkmxByJt(WkmxDto wkmxDto);
	/**
	 * 根据syglids获取实验信息
	 */
	List<WkmxDto> getInfoBySyglids(WkmxDto wkmxDto);

	boolean insetSjjtxx(Map<String,Object> map);

	boolean updateDetectionDate(WkmxDto wkmxDto,List<WkmxDto> wkxxlist);

	/**
	 * 创建先声任务单
	 * @param sjids
	 * @return
	 */
	boolean createTaskReda(List<String> sjids);

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
