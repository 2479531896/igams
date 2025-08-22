package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.GrszModel;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.dao.post.IGrszDao;
import com.matridx.igams.common.enums.PersonalSettingEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IUserService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GrszServiceImpl extends BaseBasicServiceImpl<GrszDto, GrszModel, IGrszDao> implements IGrszService{
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private IUserService userService;
	@Value("${matridx.prefix.messageFlg:}")
	private String messageFlg;
	//是否发送rabbit标记     1：发送
	@Value("${matridx.rabbit.configflg:1}")
	private String configflg;
	@Autowired(required=false)
	private AmqpTemplate amqpTempl;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
    /**
     * 插入个人设置信息
     */
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertDto(GrszDto grszDto){
    	grszDto.setSzid(StringUtil.generateUUID());
    	int result = dao.insert(grszDto);
    	return result > 0;
    }
	/**
	 * 主页设置任务确认人
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean modSaveSettingConfirmer(GrszDto grszDto) {
		//判断是否存在
		grszDto.setSzlb(PersonalSettingEnum.SETTING_NEXT_CONFIRMER.getCode());
		GrszDto t_grszDto = dao.selectGrszDtoByYhidAndSzlb(grszDto);
		if(t_grszDto == null){
			return insertDto(grszDto);
		}else{
			if(StringUtil.isNotBlank(grszDto.getSzz())) {
				return dao.updateByYhidAndSzlb(grszDto);
			}else {
				return delete(grszDto);
			}
			
		}
	}
	
	/**
	 * 根据用户ID查询个人设置信息
	 */
	@Override
	public GrszDto selectByYhid(String yhid) {
		// TODO Auto-generated method stub
		GrszDto grszDto = new GrszDto();
		grszDto.setSzlb(PersonalSettingEnum.SETTING_NEXT_CONFIRMER.getCode());
		grszDto.setYhid(yhid);
		grszDto = dao.selectGrszDtoByYhidAndSzlb(grszDto);
		if(grszDto != null){
			Map<String, String> map=dao.getXtyhBYYhid(grszDto.getSzz());
			if(map.get("ddid")!=null&& !map.get("ddid").isEmpty()) {
				grszDto.setDdid(map.get("ddid"));
				grszDto.setZsxm(grszDto.getGlxx());
				grszDto.setYhm(map.get("yhm"));
			}
			
			/*
			 * String[] arrGlxx = grszDto.getGlxx().split("＆"); if(arrGlxx.length > 0){
			 * grszDto.setZsxm(arrGlxx[0]); if(arrGlxx.length > 1){
			 * grszDto.setDdid(arrGlxx[1]); } }
			 */
			 
		}
		return grszDto;
	}
	
	/**
	 * 根据条件查询个人设置信息
	 */
	public GrszDto selectGrszDtoByYhidAndSzlb(GrszDto grszDto) {
		return dao.selectGrszDtoByYhidAndSzlb(grszDto);
	}

	/**
	 * 根据条件修改个人设置信息
	 */
	public boolean updateByYhidAndSzlb(GrszDto grszDto) {
		return dao.updateByYhidAndSzlb(grszDto);
	}

	@Override
	public Map<String, GrszDto> selectGrszMapByYhidAndSzlb(GrszDto grszDto) {
		List<GrszDto> grszDtoList=dao.selectGrszListByYhidAndSzlb(grszDto);
		Map<String, GrszDto> map=new HashMap<>();
		if(!CollectionUtils.isEmpty(grszDtoList)){
			for(GrszDto dto:grszDtoList){
				map.put(dto.getSzlb(),dto);
			}
		}
		return map;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean saveGrsz(GrszDto grszDto) {
		List<GrszDto> grszDtoList = new ArrayList<>();
		if(grszDto.getIds()!=null && !grszDto.getIds().isEmpty()){
			for (String id:grszDto.getIds()){
				GrszDto grszDtoT = new GrszDto();
				grszDtoT.setSzid(StringUtil.generateUUID());
				grszDtoT.setSzz(id);
				grszDtoT.setYhid(grszDto.getYhid());
				grszDtoT.setLrry(grszDto.getYhid());
				grszDtoList.add(grszDtoT);
			}
		}
		return dao.saveGrsz(grszDtoList);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean delGrsz(GrszDto grszDto) {
		return dao.delGrsz(grszDto);
	}

	/**
	 * @Description: 获取默认订阅消息
	 * @param
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/21 9:39
	 */
	@Override
	public Map<String, Object> queryMrsz() {
		Map<String,Object> map = new HashMap<>();
		GrszDto grszDto = new GrszDto();
		grszDto.setCskz1("DYXX");
		grszDto.setYhid("MR");
		List<GrszDto> grszDtoList = dao.queryDyxx(grszDto);
		if(!CollectionUtils.isEmpty(grszDtoList)){
			Map<String, List<GrszDto>> groupMap = new HashMap<>();
			for (GrszDto dto : grszDtoList) {
				groupMap.computeIfAbsent(dto.getCsmc(), k -> new ArrayList<>()).add(dto);
			}
			map.putAll(groupMap);
		}
		return map;
	}

	/**
	 * @Description: 默认订阅消息保存
	 * @param grszDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/11/21 10:02
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean setDefaultSave(GrszDto grszDto) throws BusinessException {
		boolean result = false;
		if(StringUtil.isNotBlank(grszDto.getDyxxJson())){
			Map map = JSON.parseObject(grszDto.getDyxxJson(),Map.class);
			List<GrszDto> addList = new ArrayList<>();
			List<GrszDto> modList = new ArrayList<>();
			for (Object value : map.values()) {
				String objString = JSONObject.toJSONString(value);
				List<GrszDto> grszDtoList = JSON.parseArray(objString, GrszDto.class);
				for (GrszDto dto : grszDtoList){
                    if (StringUtil.isNotBlank(dto.getSzid())) {
                        dto.setXgry(grszDto.getLrry());
                        modList.add(dto);
                    } else {
						dto.setSzid(StringUtil.generateUUID());
                        dto.setLrry(grszDto.getLrry());
                        addList.add(dto);
                    }
                }
			}
			if(!CollectionUtils.isEmpty(addList)){
				result = insertList(addList);
				if(!result){
					throw new BusinessException("msg","默认订阅消息新增失败!");
				}
			}
			if(!CollectionUtils.isEmpty(modList)){
				result = updateList(modList);
				if(!result){
					throw new BusinessException("msg","默认订阅消息修改失败!");
				}
			}
			redisUtil.hdelByKey("Grsz:MR");
			if(!CollectionUtils.isEmpty(addList)){
				for (GrszDto dto : addList){
					redisUtil.hset("Grsz:MR",dto.getGlxx(),dto.getSzz(),-1);
				}
			}
			if(!CollectionUtils.isEmpty(modList)){
				for (GrszDto dto : modList){
					redisUtil.hset("Grsz:MR",dto.getGlxx(),dto.getSzz(),-1);
				}
			}
		}
		return result;
	}

	/**
	 * @Description: 批量新增
	 * @param grszDtoList
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/11/21 11:00
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertList(List<GrszDto> grszDtoList) {
		return dao.insertList(grszDtoList)>0;
	}

	/**
	 * @Description: 批量修改
	 * @param grszDtoList
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/11/21 11:00
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateList(List<GrszDto> grszDtoList) {
		return dao.updateList(grszDtoList)>0;
	}

	@Override
	public List<GrszDto> queryDyxx(GrszDto grszDto) {
		return dao.queryDyxx(grszDto);
	}

	@Override
	public List<GrszDto> queryByYhid(String yhid) {
		List<GrszDto> grszDtoList = dao.queryByYhid(yhid);
		if(!CollectionUtils.isEmpty(grszDtoList)){
			for (GrszDto grszDto:grszDtoList){
				if(StringUtil.isBlank(grszDto.getSzid())){
					Object object = redisUtil.hget("Grsz:MR",grszDto.getGlxx());
					if(object!=null){
						grszDto.setSzz(String.valueOf(object));
					}
				}
			}
		}
		return grszDtoList;
	}

	/**
	 * @Description: 获取订阅消息
	 * @param grszDto
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/21 16:58
	 */
	@Override
	public Map<String, Object> queryMessage(GrszDto grszDto) {
		Map<String, Object> map = new HashMap<>();
		String ids = "";
		GrszDto grsz = new GrszDto();
		UserDto userDto = userService.getDtoById(grszDto.getYhid());
		if(userDto!=null){
			map.put("zsxm",userDto.getZsxm());
			ids = grszDto.getYhid();
		}else{
			grsz.setFlag("1");
		}
		boolean hbflg = true;
		if(StringUtil.isNotBlank(messageFlg)){
			String[] list = messageFlg.split(",");
			for (String str:list){
				if ("HB".equals(str)) {
					hbflg = false;
					break;
				}
			}
		}
		String hbid="";
		List<GrszDto> sjhbxxDtos = new ArrayList<>();
		if(hbflg){
			sjhbxxDtos = dao.queryHbxxByYhid(grszDto);
			if(!CollectionUtils.isEmpty(sjhbxxDtos)){
				map.put("sjhbxxDtos",sjhbxxDtos);
				map.put("hbid",sjhbxxDtos.get(0).getHbid());
				hbid = sjhbxxDtos.get(0).getHbid();
				if(StringUtil.isNotBlank(ids)){
					ids = ids + "," + sjhbxxDtos.get(0).getHbid();
				}else {
					ids = sjhbxxDtos.get(0).getHbid();
				}
			}
		}
		grsz.setCskz1("DYXX");
		grsz.setIds(ids);
		List<GrszDto> grszDtoList = dao.queryDyxx(grsz);
		if(!CollectionUtils.isEmpty(grszDtoList)){
			Map<String, List<GrszDto>> groupMap = new HashMap<>();
			List<GrszDto> hbList = new ArrayList<>();
			List<GrszDto> yhList = new ArrayList<>();
			for (GrszDto dto:grszDtoList){
				dto.setXxnr(dto.getXxnr().replaceAll("[\\r\\n*#-]", ""));//这里对csmc去除markdown标识
				if(StringUtil.isBlank(dto.getSzid())){
					Object object = redisUtil.hget("Grsz:MR",dto.getGlxx());
					if(object!=null){
						dto.setSzz(String.valueOf(object));
					}
				}
				boolean mapFlg = true;
				if("HB".equals(dto.getSzlb())){
					if(hbflg && !sjhbxxDtos.isEmpty()){
						dto.setYhid(hbid);
						hbList.add(dto);
					}else {
						mapFlg = false;
					}
				}else{
					dto.setYhid(grszDto.getYhid());
					yhList.add(dto);
				}
				if(mapFlg){
					groupMap.computeIfAbsent(dto.getCsmc(), k -> new ArrayList<>()).add(dto);
				}
			}
			map.put("dyxxMap",groupMap);
			if(!CollectionUtils.isEmpty(hbList)){
				map.put("hbList",JSONObject.toJSONString(hbList));
			}
			if(!CollectionUtils.isEmpty(yhList)){
				map.put("yhList",JSONObject.toJSONString(yhList));
			}
		}
		return map;
	}

	/**
	 * @Description: 个人订阅消息设置
	 * @param grszDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/11/26 10:26
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean modSaveMessage(GrszDto grszDto) throws BusinessException {
		List<GrszDto> addGrszDtos = new ArrayList<>();
		List<GrszDto> modGrszDtos = new ArrayList<>();
		if(StringUtil.isNotBlank(grszDto.getHbJson())){
			List<GrszDto> hbGrszDtos = JSON.parseArray(grszDto.getHbJson(), GrszDto.class);
			if(!CollectionUtils.isEmpty(hbGrszDtos)){
				for (GrszDto dto:hbGrszDtos){
					if(StringUtil.isNotBlank(dto.getSzid())){
						dto.setXgry(grszDto.getYhid());
						modGrszDtos.add(dto);
					}else {
						dto.setLrry(grszDto.getYhid());
						dto.setSzid(StringUtil.generateUUID());
						addGrszDtos.add(dto);
					}
				}
			}
		}
		if(StringUtil.isNotBlank(grszDto.getYhJson())){
			List<GrszDto> yhGrszDtos = JSON.parseArray(grszDto.getYhJson(), GrszDto.class);
			if(!CollectionUtils.isEmpty(yhGrszDtos)){
				for (GrszDto dto:yhGrszDtos){
					if(StringUtil.isNotBlank(dto.getSzid())){
						dto.setXgry(grszDto.getYhid());
						modGrszDtos.add(dto);
					}else {
						dto.setLrry(grszDto.getYhid());
						dto.setSzid(StringUtil.generateUUID());
						addGrszDtos.add(dto);
					}
				}
			}
		}
		boolean result = true;
		if(!CollectionUtils.isEmpty(addGrszDtos)){
			result = dao.insertList(addGrszDtos)>0;
			if(!result){
				throw new BusinessException("msg","订阅消息新增失败!");
			}
		}
		if(!CollectionUtils.isEmpty(modGrszDtos)){
			result = dao.updateList(modGrszDtos)>0;
			if(!result){
				throw new BusinessException("msg","订阅消息修改失败!");
			}
		}
		List<String> ids = new ArrayList<>();
		if(StringUtil.isNotBlank(grszDto.getYhid())){
			ids.add(grszDto.getYhid());
		}
		if(!CollectionUtils.isEmpty(grszDto.getIds())){
			ids.addAll(grszDto.getIds());
		}
		if(!CollectionUtils.isEmpty(ids)){
			for (String string:ids){
				redisUtil.hdelByKey("Grsz:"+string);
			}
		}
		if(!CollectionUtils.isEmpty(addGrszDtos)){
			for (GrszDto dto : addGrszDtos){
				redisUtil.hset("Grsz:"+dto.getYhid(),dto.getGlxx(),dto.getSzz(),-1);
			}
		}
		if(!CollectionUtils.isEmpty(modGrszDtos)){
			for (GrszDto dto : modGrszDtos){
				redisUtil.hset("Grsz:"+dto.getYhid(),dto.getGlxx(),dto.getSzz(),-1);
			}
		}
		return result;
	}

	/**
	 * @Description: 发送rabbit
	 * @param grszDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/11/26 15:24
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean saveAndSendRabbit(GrszDto grszDto) throws BusinessException {
		boolean result = modSaveMessage(grszDto);
		if(result && StringUtil.isNotBlank(configflg) && "1".equals(configflg)){
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.grsz.save",JSONObject.toJSONString(grszDto));
		}
		return result;
	}

	@Override
	public List<GrszDto> getPagedDtoList(GrszDto grszDto) {
		boolean hbflg = true;
		if(StringUtil.isNotBlank(messageFlg)){
			String[] list = messageFlg.split(",");
			for (String str:list){
				if ("HB".equals(str)) {
					hbflg = false;
					break;
				}
			}
		}
		if(hbflg){
			grszDto.setFlag("1");
		}
		return dao.getPagedDtoList(grszDto);
	}

	@Override
	public GrszDto getDto(GrszDto grszDto){
		boolean hbflg = true;
		if(StringUtil.isNotBlank(messageFlg)){
			String[] list = messageFlg.split(",");
			for (String str:list){
				if ("HB".equals(str)) {
					hbflg = false;
					break;
				}
			}
		}
		if(hbflg){
			grszDto.setFlag("1");
		}
		return dao.getDto(grszDto);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertOrUpdate(GrszDto grszDto) {
		GrszDto grszDtoT = dao.selectGrszDtoByYhidAndSzlb(grszDto);
		if(grszDtoT!=null) {
			return updateByYhidAndSzlb(grszDto);
		}
		return insertDto(grszDto);
	}

	@Override
	public GrszDto getDtoByYhidAndGlxx(GrszDto grszDto) {
		return dao.getDtoByYhidAndGlxx(grszDto);
	}
}
