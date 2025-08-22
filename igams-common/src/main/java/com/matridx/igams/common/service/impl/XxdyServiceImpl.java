package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.Role;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.dao.entities.XxdyModel;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.dao.post.IXxdyDao;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 说明:第一列编码说明，一共6位，第一位代表项目分类，比如Q2.0用Q，3.0用A，Q2.5用X，Q-mNGS™3.0 DNA+Onco类似这种暂时用V，TNGS用T
 * 第二位代表项目需要做哪些文库，比如2.0D用D，2.0D+R用C，2.0R用R，ONCO用O，ResFirst™用F,TNGS用ABC
 * 第三位代表要做的文库比如DNA代表D，RNA代表R，ONCO代表O，DNA+RNA、tNGS代表C
 * 第四位预留(TNGS用来区分A/B/C1/C2/C3/REM，对应关系是 C1=1, C2=2, C3=3, REM=E)
 * 第五位IT内部定义
 * 第6位会替换成具体的样本类型代码，
 * @author linwu
 *
 */
@Service
public class XxdyServiceImpl extends BaseBasicServiceImpl<XxdyDto, XxdyModel, IXxdyDao> implements IXxdyService, IFileImport {

	@Autowired
	private ICommonDao commonDao;
	@Autowired
	RedisUtil redisUtil;
	/**
	 * 取到对应表中岗位名称的角色ID
	 */
	@Override
	public XxdyDto getGwJsid(String gwmc) {
		// TODO Auto-generated method stub
		return dao.getGwJsid(gwmc);
	}

	@Override
	public XxdyDto getInfo(XxdyDto t) {
		return dao.getInfo(t);
	}

	/**
	 * 找到角色数据
	 */
	@Override
	public List<Role> getRoleList() {
		// TODO Auto-generated method stub
		return commonDao.getRoleList();
	}

	@Override
	public List<User> getUserList() {
		// TODO Auto-generated method stub
		return commonDao.getUserList();
	}

	@Override
	public XxdyDto getCskz(XxdyDto xxdyDto) {
		// TODO Auto-generated method stub
		return dao.getCskz(xxdyDto);
	}

	/**
	 * 通过dyxx拿到不同连接表的dyxxmc(人员)
	 */
	@Override
	public String getRyXxdymc(String dyxx) {
		// TODO Auto-generated method stub
		return dao.getRyXxdymc(dyxx);
	}

	/**
	 * 通过dyxx拿到不同连接表的dyxxmc(角色)
	 */
	@Override
	public String getJsXxdymc(String dyxx) {
		// TODO Auto-generated method stub
		return dao.getJsXxdymc(dyxx);
	}

	/**
	 * 通过dyxx拿到不同连接表的dyxxmc(基础数据)
	 */
	@Override
	public String getJcsjXxdymc(String dyxx) {
		// TODO Auto-generated method stub
		return dao.getJcsjXxdymc(dyxx);
	}

	/**
	 * 通过用户的岗位名称查找对应表中的角色Id
	 */
	@Override
	public String getJsidByGwmc(String gwmc) {
		// TODO Auto-generated method stub
		return dao.getJsidByGwmc(gwmc);
	}


	@Override
	public List<XxdyDto> getPagedInfoDtoList(XxdyDto xxdyDto){
		return dao.getPagedInfoDtoList(xxdyDto);
	}
	/**
	 * 更新全部字段
	 */
	public boolean updateAll(XxdyDto xxdyDto){
		return dao.updateAll(xxdyDto);
	}


	@Override
	public boolean existCheck(String fieldName, String value) {
		return false;
	}

	@Override
	public boolean insertImportRec(BaseModel baseModel, User user,int rowindex,StringBuffer errorMessages) {
		XxdyDto xxdyDto=(XxdyDto)baseModel;
		List<JcsjDto> jcxmDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());//检测项目
		List<JcsjDto> jczxmDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());//检测子项目
		List<JcsjDto> dylxDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.XXDY_TYPE.getCode());//对应类型
		List<JcsjDto> jclxDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_PROJECT_TYPE.getCode());//检测类型
		if(dylxDtos!= null && !dylxDtos.isEmpty()){
			if(StringUtil.isNotBlank(xxdyDto.getDylxmc())){
				for(JcsjDto dto:dylxDtos){
					if(xxdyDto.getDylxmc().equals(dto.getCsmc())){
						xxdyDto.setDylx(dto.getCsid());
						break;
					}
				}
			}
		}
		if(jcxmDtos!= null && !jcxmDtos.isEmpty()){
			if(StringUtil.isNotBlank(xxdyDto.getYxx())){
				for(JcsjDto dto:jcxmDtos){
					if(xxdyDto.getYxx().equals(dto.getCsmc())){
						xxdyDto.setYxxid(dto.getCsid());
						break;
					}
				}
			}
		}
		if(jczxmDtos!= null && !jczxmDtos.isEmpty()){
			if(StringUtil.isNotBlank(xxdyDto.getZxx())){
				for(JcsjDto dto:jczxmDtos){
					if(xxdyDto.getZxx().equals(dto.getCsmc())){
						xxdyDto.setZid(dto.getCsid());
						break;
					}
				}
			}
		}
		if(jclxDtos!= null && !jclxDtos.isEmpty()){
			if(StringUtil.isNotBlank(xxdyDto.getDyxxmc())){
				for(JcsjDto dto:jclxDtos){
					if(xxdyDto.getDyxxmc().equals(dto.getCsmc())){
						xxdyDto.setDyxx(dto.getCsid());
						break;
					}
				}
			}
		}
		if(StringUtil.isNotBlank(xxdyDto.getDyid())){
			dao.updateAll(xxdyDto);
		}else{
			xxdyDto.setDyid(StringUtil.generateUUID());
			dao.insertDto(xxdyDto);
		}
		return true;
	}

	@Override
	public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
		return null;
	}

	@Override
	public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
		return false;
	}

	@Override
	public boolean checkDefined(List<Map<String, String>> defined) {
		return true;
	}

	/**
	 * 导出
	 */
	public int getCountForSearchExp(XxdyDto xxdyDto) {
		return dao.getCountForSearchExp(xxdyDto);
	}

	/**
	 * 根据搜索条件获取导出信息
	 */
	public List<XxdyDto> getListForSearchExp(Map<String,Object> params){
		XxdyDto xxdyDto = (XxdyDto)params.get("entryData");
		queryJoinFlagExport(params,xxdyDto);
		return dao.getListForSearchExp(xxdyDto);
	}

	/**
	 * 根据选择信息获取导出信息
	 */
	public List<XxdyDto> getListForSelectExp(Map<String,Object> params){
		XxdyDto xxdyDto = (XxdyDto)params.get("entryData");
		queryJoinFlagExport(params,xxdyDto);
		return dao.getListForSelectExp(xxdyDto);
	}
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String,Object> params,XxdyDto xxdyDto){
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
		xxdyDto.setSqlParam(sqlcs);
	}
	/**
	 * 根据对应代码获取对应信息
	 */
	public List<XxdyDto> getDtoByDydm(XxdyDto xxdyDto){
		return dao.getDtoByDydm(xxdyDto);
	}

	/**
	 * 根据原信息获取数据
	 * @param xxdyDto
	 * @return
	 */
	@Override
	public List<XxdyDto> getDtoMsgByYxx(XxdyDto xxdyDto){
		return dao.getDtoMsgByYxx(xxdyDto);
	}
	/**
	 * 获取原信息
	 */
	public List<XxdyDto> getYxxMsg(XxdyDto xxdyDto){
		return dao.getYxxMsg(xxdyDto);
	}

	/**
	 * 根据原信息分组显示
	 */
	public List<XxdyDto> getListGroupByYxx(XxdyDto xxdyDto){
		return dao.getListGroupByYxx(xxdyDto);
	}



	/**
	 * 根据原信息获取基础数据List
	 * @param xxdyDto
	 * @return
	 */
	public List<JcsjDto> getJcsjListByXxdy(XxdyDto xxdyDto){
		return dao.getJcsjListByXxdy(xxdyDto);
	}

	/**
	 * 根据原信息获取基础数据Lis(模糊匹配)
	 * @param xxdyDto
	 * @return
	 */
	public List<JcsjDto> getJcsjListByLikeXxdy(XxdyDto xxdyDto){
		return dao.getJcsjListByLikeXxdy(xxdyDto);
	}

	@Override
	public List<Map<String, Object>> getSjwzxxList() {
		return dao.getSjwzxxList();
	}

	@Override
	public List<XxdyDto> queryHolidays(XxdyDto xxdyDto) {
		return dao.queryHolidays(xxdyDto);
	}

	@Override
	public List<Map<String, Object>> getOriginList(XxdyDto xxdyDto) {
		return dao.getOriginList(xxdyDto);
	}
	
	@Override
	public List<XxdyDto> getListOrder(XxdyDto xxdyDto){
		return dao.getListOrder(xxdyDto);
	}

	/**
	 * 根据list顺序查找信息对应
	 *
	 * @param list
	 * @return
	 */
	@Override
	public List<JcsjDto> getCompareListSortByList(List<Map<String, Object>> list) {
		return dao.getCompareListSortByList(list);
	}

	/**
	 * 批量新增
	 *
	 * @param list
	 * @return
	 */
	@Override
	public boolean batchInsertDtos(List<Map<String, Object>> list) {
		int i = dao.batchInsertDtos(list);
		return i == list.size();
	}

	/**
	 * 批量修改
	 *
	 * @param list
	 * @return
	 */
	@Override
	public boolean batchUpdateDtos(List<Map<String, Object>> list) {
		int i = dao.batchUpdateDtos(list);
		return i == list.size();
	}

	/**
	 * 批量删除
	 *
	 * @param list
	 * @return
	 */
	@Override
	public boolean batchDeleteDtos(List<Map<String, Object>> list) {
		int i = dao.batchDeleteDtos(list);
		return i == list.size();
	}

	public String getByDylxcsdmAndDyxxAndYxx(XxdyDto xxdyDto){
		return dao.getByDylxcsdmAndDyxxAndYxx(xxdyDto);
	}
	
	@Override
	public List<XxdyDto> getJcxmByKzcs7(XxdyDto xxdyDto) {
		return dao.getJcxmByKzcs7(xxdyDto);
	}
	
	@Override
	public List<Map<String, String>> getPcdyrq(XxdyDto xxdyDto) {
		return dao.getPcdyrq(xxdyDto);
	}

	@Override
	public List<Map<String, String>> getStddevAndVariance(XxdyDto xxdyDto) {
		return dao.getStddevAndVariance(xxdyDto);
	}

	@Override
	public List<XxdyDto> getDyxxByYxx(XxdyDto xxdyDto) {
		return dao.getDyxxByYxx(xxdyDto);
	}
}
