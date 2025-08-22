package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjkdxxDto;
import com.matridx.igams.wechat.dao.entities.SjkdxxModel;
import com.matridx.igams.wechat.dao.post.ISjkdxxDao;
import com.matridx.igams.wechat.service.svcinterface.ISjkdxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SjkdxxServiceImpl extends BaseBasicServiceImpl<SjkdxxDto, SjkdxxModel, ISjkdxxDao> implements ISjkdxxService{

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 批量插入
	 */
	@Override
	public void inserDtoList(List<SjkdxxDto> resultList) {
		dao.inserDtoList(resultList);
	}

	/**
	 * 查询出所有未签收的送检快递信息
	 */
	@Override
	public List<SjkdxxDto> getAllDto() {
		return dao.getAllDto();
	}

	/**
	 * 通过送检ID和快递号查找一条数据
	 */
	@Override
	public int getSjkdDto(SjkdxxDto sjkd) {
		return dao.getSjkdDto(sjkd);
	}

	/**
	 * 根据快递类型和快递状态0查找快递信息
	 */
	@Override
	public List<String> getKdhListByKdlx(SjkdxxDto sjkdxxDto) {
		return dao.getKdhListByKdlx(sjkdxxDto);
	}

	/**
	 * 根据快递类型和快递状态0查找快递信息，返回sjkdxx的list
	 * @param sjkdxxDto
	 * @return
	 */
	@Override
	public List<SjkdxxDto> getSjkdDtosNotAccept(SjkdxxDto sjkdxxDto) {
		return dao.getSjkdDtosNotAccept(sjkdxxDto);
	}

	//根据快递号更新送检快递表的快递开始时间
	@Override
	public boolean updateStarttimeByMailno(SjkdxxDto sjkdxxDto1) {
		return dao.updateStarttimeByMailno(sjkdxxDto1);
	}

	//根据快递号更新送检快递表的快递结束时间和签收状态
	@Override
	public boolean updateEndtimeByMailno(SjkdxxDto sjkdxxDto) {
		return dao.updateEndtimeByMailno(sjkdxxDto);
	}

	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
	public int updateList(List<SjkdxxDto> list){return dao.updateList(list);}
	
	@Override
	public void updateByMailno(SjkdxxDto sjkdxxDto) {
		dao.updateByMailno(sjkdxxDto);
	}

	/**
	 * 自动生成快递单号
	 * @return
	 */
	public String generateMailno() {
		String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		String prefix = "JY" + "-" + date + "-";
		// 查询流水号
		String serial = dao.getMailnoSerial(prefix);
		return prefix + serial;
	}
	/**
	 * 将旧的快递单号更改为新的快递单号
	 */
	@Override
	public void updateMailnoByOld(Map<String, String> map) {
		dao.updateMailnoByOld(map);
	}

	/**
	 * 通过快递单号获取业务id
	 * @param mailno
	 * @return
	 */
	@Override
	public List<String> getDtoListByMailno(String mailno) {
		return dao.getDtoListByMailno(mailno);
	}


	/**
	 * 插入送检派单快递信息
	 * @param
	 * @return
	 */
	@Override
	public Boolean insertSjkdxxInfo(String ywid,String gldh,String jsfsmc,String pdh,String ywlx) {
		if (StringUtil.isNotBlank(ywid) && StringUtil.isNotBlank(gldh) && StringUtil.isNotBlank(jsfsmc) && StringUtil.isNotBlank(ywlx)){
			SjkdxxDto sjkdxxDto = new SjkdxxDto();
			sjkdxxDto.setYwlx(ywlx);
			sjkdxxDto.setYwid(ywid);
			sjkdxxDto.setMailno(gldh);
			SjkdxxDto dto = dao.getDto(sjkdxxDto);
			List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE.getCode());
			if(null == dto){
				sjkdxxDto.setSjkdid(StringUtil.generateUUID());
				Date date = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
				sjkdxxDto.setStarttime(simpleDateFormat.format(date));
				sjkdxxDto.setKdzt("0");
				for (JcsjDto jcsjDto : list) {
					if ((jcsjDto.getCsmc().contains("京东") && jsfsmc.contains("京东")) ||
							(jcsjDto.getCsmc().contains("顺丰") && jsfsmc.contains("顺丰"))){
						sjkdxxDto.setKdlx(jcsjDto.getCsid());
						break;
					}
				}
				sjkdxxDto.setSjmailno(pdh);
				dao.insert(sjkdxxDto);
			}
		}
		return true;
	}

	/**
	 * 批量更新或插入
	 * @param list
	 * @return
	 */
	public boolean insertOrUpdateList(List<SjkdxxDto> list){
		return dao.insertOrUpdateList(list);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean modSjkdxxByList(List<SjkdxxDto> list) {
		return dao.modSjkdxxByList(list);
	}
}
