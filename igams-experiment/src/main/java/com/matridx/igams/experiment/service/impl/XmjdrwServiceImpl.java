package com.matridx.igams.experiment.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.dao.post.IGzglDao;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.experiment.dao.entities.*;
import com.matridx.igams.experiment.dao.post.*;
import com.matridx.igams.experiment.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateTimeUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class XmjdrwServiceImpl extends BaseBasicServiceImpl<XmjdrwDto, XmjdrwModel, IXmjdrwDao> implements IXmjdrwService,ITaskService
{
	@Autowired
	private IGzglDao gzglDao;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	private IXxglService xxglService;
	@Autowired
	private IRwlyDao rwlydao;
	@Autowired
	private IXmglService xmglService;
	@Autowired
	IXmjdxxService xmjdxxService;
	@Autowired
	IXmcyService xmcyService;
	@Autowired
	IXmjdxxDao  xmjdxxDao;
	@Autowired
	IRwmbDao  rwmbDao;
	@Autowired
	IXmcyDao xmcyDao;
	@Autowired
	IRwrqService rwrqService;
	@Autowired
	IRwrqDao rwrqDao;
	@Autowired
	IFjcfbService fjcfbService;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	ICommonService commonService;
	@Autowired
	RedisUtil redisUtil;
	private final Logger logger = LoggerFactory.getLogger(XmjdrwServiceImpl.class);
	
	/**
	 * 插入项目阶段信息到数据库
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertDto(XmjdrwDto xmjdrwDto){
		//添加项目阶段任务
		int result = dao.insert(xmjdrwDto);
		if (result == 0)
			return false;
		//添加工作管理表
		GzglDto t_gzglDtos = new GzglDto();
		if (StringUtil.isNotBlank(xmjdrwDto.getFzr())){
			GzglDto gzglDto = new GzglDto();
			gzglDto.setYhid(xmjdrwDto.getFzr());
			GzglDto yhssjg = gzglDao.getYhssjg(gzglDto);
			if (null != yhssjg){
				t_gzglDtos.setJgid(yhssjg.getJgid());
			}
		}
		t_gzglDtos.setGzid(StringUtil.generateUUID());
		t_gzglDtos.setYwid(xmjdrwDto.getRwid());
		t_gzglDtos.setYwmc(xmjdrwDto.getRwmc());
		t_gzglDtos.setRwmc(TaskNameEnum.PROJECT_TASK.getCode());
		t_gzglDtos.setQwwcsj(xmjdrwDto.getJzrq());
		t_gzglDtos.setFzr(xmjdrwDto.getFzr());
		t_gzglDtos.setZt(xmjdrwDto.getZt());
		t_gzglDtos.setLrry(xmjdrwDto.getLrry());
		t_gzglDtos.setYwdz("/experiment/project/pagedataViewProjectTask?rwid="+xmjdrwDto.getRwid());
		if(Objects.equals(xmjdrwDto.getFrwid(), "") ||xmjdrwDto.getFrwid()==null) {
			t_gzglDtos.setRwid(xmjdrwDto.getRwid());
			t_gzglDtos.setGzlx(WorkTypeEnum.TASK_TYPE.getCode());
		}else {
			t_gzglDtos.setRwid(xmjdrwDto.getZwcrwid());
			t_gzglDtos.setGzlx(WorkTypeEnum.SUBTASK_TYPE.getCode());
		}
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setJclb(BasicDataTypeEnum.TASK_URGENT.getCode());
		JcsjDto jcsjDtos=jcsjService.getdefault(jcsjDto);
		if(jcsjDtos!=null)
			t_gzglDtos.setJjd(jcsjDtos.getCsid());
		t_gzglDtos.setYjgzl(xmjdrwDto.getYjgzl());
		t_gzglDtos.setSjgzl(xmjdrwDto.getSjgzl());
		boolean isSuccess = gzglDao.insertDtobyfzr(t_gzglDtos);
		if (!isSuccess)
			return false;
		//推送到钉钉
		try {
//			String token = talkUtil.getToken();
			XmjdrwDto xmjdrwDtos=dao.getDtoById(xmjdrwDto.getRwid());
			if(Objects.equals(xmjdrwDtos.getRwjbmc(), "") ||xmjdrwDtos.getRwjbmc()==null) {
				xmjdrwDtos.setRwjbmc("无");
			}
			XmjdrwDto xmjdrwDto_yhm=dao.getYhmByFzr(xmjdrwDto.getFzr());
			if(xmjdrwDto_yhm!=null && StringUtil.isNotBlank(xmjdrwDto_yhm.getYhm())){
				//talkUtil.sendWorkMessage(token, xmjdrwDto_ddid.getDdid(), xxglService.getMsg("ICOMM_SH00013"), xxglService.getMsg("ICOMM_SH00014", xmjdrwDtos.getXmmc(), xmjdrwDtos.getRwmc(), xmjdrwDtos.getRwjbmc(), xmjdrwDtos.getJsrq()));
				//组装访问路径
				//内网访问 &：%26        =：%3D  ?:%3F
				//String internalbtn = applicationurl + urlPrefix + "/common/view/displayView?view_url=/ws/projectdd/viewProjectTaskdd%3Frwid%3D"+xmjdrwDtos.getRwid();
				String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/tasknotification/tasktransfer/tasktransfer&applicationurl="+applicationurl+"&urlPrefix="+urlPrefix+"&rwlx=xm"+"&rwid="+xmjdrwDtos.getRwid(),StandardCharsets.UTF_8);
				//访问链接
				List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
				OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
				btnJsonList.setTitle("小程序");
				btnJsonList.setActionUrl(internalbtn);
				btnJsonLists.add(btnJsonList);
				//发送钉钉消息
				talkUtil.sendCardMessage(xmjdrwDto_yhm.getYhm(),
						xmjdrwDto_yhm.getDdid(),
						xxglService.getMsg("ICOMM_SH00013"),
						StringUtil.replaceMsg( xxglService.getMsg("ICOMM_SH00082"),
								xmjdrwDtos.getXmmc(),
								xmjdrwDtos.getRwmc(),
								xmjdrwDtos.getRwjbmc(),
								xmjdrwDtos.getJsrq()),
						btnJsonLists,
						"1");
			}
		}catch (Exception e){
			logger.info(e.getMessage());
		}
		//添加任务留言
		RwlyModel rwlyModel=new RwlyModel();
		rwlyModel.setLyid(StringUtil.generateUUID());
		rwlyModel.setRwid(xmjdrwDto.getRwid());
		rwlyModel.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
		rwlyModel.setLyxx("创建了任务");
		rwlyModel.setLrry(xmjdrwDto.getLrry());
		int succsss_flg=rwlydao.insert(rwlyModel);
		if(succsss_flg==0) {
			return false;
		}
		if(StringUtil.isNotBlank(xmjdrwDto.getFzr())){
			rwlyModel=new RwlyModel();
			rwlyModel.setLyid(StringUtil.generateUUID());
			rwlyModel.setRwid(xmjdrwDto.getRwid());
			rwlyModel.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
			User user=new User();
			user.setYhid(xmjdrwDto.getFzr());
			User userInfoById = commonService.getUserInfoById(user);
			rwlyModel.setLyxx("将任务分配给了  "+userInfoById.getZsxm());
			rwlyModel.setLrry(xmjdrwDto.getLrry());
			succsss_flg=rwlydao.insert(rwlyModel);
			if(succsss_flg==0) {
				return false;
			}
		}
		//新增任务日期表信息
		isSuccess = insertOrUpdateRwrq(xmjdrwDto);
		return isSuccess;
	}
	
	/**
	 * 获取项目全部阶段，新增任务日期表信息
	 */
	public boolean insertOrUpdateRwrq(XmjdrwDto xmjdrwDto){
		List<XmjdrwDto> t_xmjdrwDtos = dao.selectStageByRwid(xmjdrwDto);
		List<RwrqDto> rwrqDtos = new ArrayList<>();
		if(t_xmjdrwDtos != null && t_xmjdrwDtos.size() > 0){
			for (XmjdrwDto t_xmjdrwDto : t_xmjdrwDtos) {
				RwrqDto rwrqDto = new RwrqDto();
				rwrqDto.setRwid(xmjdrwDto.getRwid());
				rwrqDto.setLrry(xmjdrwDto.getLrry());
				rwrqDto.setRqid(StringUtil.generateUUID());
				rwrqDto.setXmjdid(t_xmjdrwDto.getJdxmjdid());
				if (t_xmjdrwDto.getJdxmjdid().equals(xmjdrwDto.getXmjdid())) {
					if (StringUtil.isBlank(xmjdrwDto.getJzrq())) {
						Object oncoxtsz = redisUtil.hget("matridx_xtsz", "task.stage.cycle");
						if (oncoxtsz != null) {
							XtszDto xtszDto = JSON.parseObject(String.valueOf(oncoxtsz), XtszDto.class);
							if (StringUtil.isNotBlank(xtszDto.getSzz())) {
								String szz = xtszDto.getSzz();
								Calendar calendar = Calendar.getInstance();
								calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(szz));
								String time = DateUtils.format(calendar.getTime(), "yyyy-MM-dd");
								rwrqDto.setJzrq(time);
							}
						}
					} else {
						rwrqDto.setJzrq(xmjdrwDto.getJzrq());
					}
					rwrqDto.setJhksrq(xmjdrwDto.getKsrq());
					rwrqDto.setSjksrq(DateTimeUtil.getCurrDateStr());
					rwrqDto.setJhjsrq(xmjdrwDto.getJsrq());
					rwrqDto.setClry(xmjdrwDto.getFzr());
				}
				if (StringUtil.isNotBlank(t_xmjdrwDto.getFsbl())&&StringUtil.isNotBlank(xmjdrwDto.getFs())){
					BigDecimal jdfs = new BigDecimal(t_xmjdrwDto.getFsbl()).multiply(new BigDecimal(xmjdrwDto.getFs()));
					rwrqDto.setJdfs(String.valueOf(jdfs));
				}
				rwrqDtos.add(rwrqDto);
			}
			return rwrqService.insertOrUpdateRwrq(rwrqDtos);
		}
		return true;
	}

	/**
	 * 根据模板ID查询任务模板信息
	 */
	@Override
	public List<RwmbDto> selectRwmbByMbid(XmglDto xmgldto)
	{
		return dao.selectRwmbByMbid(xmgldto);
	}

	/**
	 * 根据项目阶段ID查询项目阶段任务信息
	 */
	@Override
	public List<XmjdrwDto> selectTaskByXmjdid(String xmjdid)
	{
		// TODO Auto-generated method stub
		return dao.selectTaskByXmjdid(xmjdid);
	}

	/**
	 * 根据任务id查询任务信息
	 */
	@Override
	public XmjdrwDto getDtoById(String rwid){
		XmjdrwDto xmjdrwDto = dao.getDtoById(rwid);
		if(xmjdrwDto!=null  && StringUtil.isNotBlank(xmjdrwDto.getRwid())) {
			List<XmjdrwDto> zrwDto = dao.getDtoListById(xmjdrwDto.getRwid());
			xmjdrwDto.setXmjdzrwDto(zrwDto);
			RwlyDto rwlyDto=new RwlyDto();
			rwlyDto.setRwid(xmjdrwDto.getRwid());
			rwlyDto.setPageSize(10);
			List<RwlyDto> rwlylist=rwlydao.getDtoList(rwlyDto);
			for (RwlyDto dto : rwlylist) {
				if (("PERSON_MASSAGE").equals(dto.getLylx())) {
					dto.setColor("#72A17F");
				} else {
					dto.setColor("#A3A3A3");
				}
			}
			xmjdrwDto.setRwlyDtos(rwlylist);
		}
		return xmjdrwDto;
	}

	/**
	 *  删除任务以及该任务下的子任务，同时删除个人列表中的任务
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleterw(XmjdrwDto xmjdrwDto){
//		GzglDto gzglDto=new GzglDto();
//		gzglDto.setScry(xmjdrwDto.getScry());
//		gzglDto.setYwid(xmjdrwDto.getRwid());
		List<XmjdrwDto> rwDtolist=dao.getDqrwAndZrw(xmjdrwDto);
		List<String> rwlist= new ArrayList<>();
		for (XmjdrwDto dto : rwDtolist) {
			String rwid = dto.getRwid();
			rwlist.add(rwid);
		}
		xmjdrwDto.setRwids(rwlist);
		int deleterws=dao.deleterwByRwid(xmjdrwDto);
		if(deleterws>0) {
			return gzglDao.deleteByYwid(rwlist);
		}
		return false;
	}

	/**
	 * 完成任务
	 */
	@Override
	public boolean updateJdrwZt(XmjdrwDto xmjdrwDto){
		// TODO Auto-generated method stub
		xmjdrwDto.setZt(StatusEnum.CHECK_PASS.getCode());
		boolean isSuccess = dao.updateZt(xmjdrwDto);
		if(!isSuccess)
			return false;
		XmjdrwDto xmjdrwDtos=new XmjdrwDto();
		xmjdrwDtos.setRwid(xmjdrwDto.getRwid());
		xmjdrwDtos.setZt(StatusEnum.CHECK_PASS.getCode());
		xmjdrwDtos.setDqjd("100");
		xmjdrwDtos.setXgry(xmjdrwDto.getXgry());
		return dao.updateGzglZt(xmjdrwDtos);
	}

	/**
	 * 开始任务
	 */
	@Override public boolean StartTask(XmjdrwDto xmjdrwDto){ 
		// TODO Auto-generated method stub List<XmjdrwDto>
		xmjdrwDto.setZt(StatusEnum.CHECK_NO.getCode());
		boolean isSuccess = dao.updateZt(xmjdrwDto);
		if(!isSuccess)
			 return false;
		XmjdrwDto xmjdrwDtos=new XmjdrwDto();
		xmjdrwDtos.setRwid(xmjdrwDto.getRwid());
		xmjdrwDtos.setZt(StatusEnum.CHECK_NO.getCode());
		xmjdrwDtos.setDqjd("");
		xmjdrwDtos.setXgry(xmjdrwDto.getXgry());
		return dao.updateGzglZt(xmjdrwDtos);
	}
	/**
	 * 修改阶段time
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean modJdtime(XmjdrwDto xmjdrwDto){
		XmglDto xmglDto = new XmglDto();
		xmglDto.setXmid(xmjdrwDto.getXmid());
		List<XmjdxxDto> xmjdxxDtos = xmjdxxService.selectStageByXmid(xmglDto);
		XmjdrwDto dtoById = dao.getDtoById(xmjdrwDto.getRwid());
		XmjdrwDto xmjdrwDto1 = new XmjdrwDto();
		xmjdrwDto1.setRwid(xmjdrwDto.getRwid());
		xmjdrwDto1.setXgry(xmjdrwDto.getXgry());
		Integer start = null;
		Integer end = null;
		for (int i = 0; i <xmjdxxDtos.size() ; i++) {
			if (xmjdxxDtos.get(i).getXmjdid().equals(dtoById.getXmjdid())){
				start = i;
			}
			if (xmjdxxDtos.get(i).getXmjdid().equals(xmjdrwDto.getXmjdid())){
				end = i;
			}
		}
		if (StringUtil.isNotBlank(dtoById.getJdtime())){
			String[] strs = dtoById.getJdtime().split(",");
			if (strs.length>0){
				StringBuilder str = new StringBuilder();
				for (int i = 0; i < xmjdxxDtos.size(); i++) {
					if (i>strs.length){
						str.append(",-");
					}
				}
				dtoById.setJdtime(dtoById.getJdtime()+str);
				String[] strings = dtoById.getJdtime().split(",");
				String string = strings[start];
				strings[start] = strings[end];
				strings[end] = string;
				StringBuilder s = new StringBuilder();
				for (int i = 0; i <strings.length-1; i++) {
					s.append(strings[i]).append(",");
				}
				s.append(strings[strings.length - 1]);
				xmjdrwDto1.setJdtime(s.toString());
			}
		}else{
			if (StringUtil.isBlank(dtoById.getLrsj())){
				dtoById.setLrsj("-");
			}
			StringBuilder str= new StringBuilder("" + dtoById.getLrsj() + ",");
			for (int i = 0; i < xmjdxxDtos.size()-1; i++) {
				if (i==end){
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm");
					Date date = new Date();
					str.append(simpleDateFormat.format(date));
					if (i!= xmjdxxDtos.size()-2){
						str.append(",");
					}
				}else{
					if (i!= xmjdxxDtos.size()-2){
						str.append("-,");
					}else {
						str.append("-");
					}
				}
			}
			xmjdrwDto1.setJdtime(str.toString());
		}
		dao.updateDto(xmjdrwDto1);
		return true;
	}
	/**
	 * 项目任务重新排序
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean taskSort(XmjdrwDto xmjdrwDto){
		// TODO Auto-generated method stub
		if (StringUtil.isNotBlank(xmjdrwDto.getXmjdid()) && StringUtil.isNotBlank(xmjdrwDto.getRwid())){
			int result = dao.updateXmjdByRwid(xmjdrwDto);
			if (result == 0)
				return false;
			//判断阶段ID
			XmjdxxDto xmjdxxDto = new XmjdxxDto();
			xmjdxxDto.setXmjdid(xmjdrwDto.getXmjdid());
			xmjdxxDto = xmjdxxService.getDto(xmjdxxDto);
			//查询模板任务，新增子任务，增加到工作管理表
			List<RwmbDto> rwmbDtos = rwmbDao.getrwmbbyid(xmjdxxDto.getJdid());
			if(rwmbDtos != null && rwmbDtos.size() > 0){
				XmjdrwDto t_xmjdrwDto = dao.getSearchTaskRwDto(xmjdrwDto);
				//查询任务模板ID不为空的子任务
				List<String> rwmbids = dao.selectSubTaskByRwid(xmjdrwDto);
				for (RwmbDto rwmbDto : rwmbDtos) {
					if (rwmbids != null && rwmbids.contains(rwmbDto.getRwmbid())) {
						continue;
					}
					XmjdrwDto z_xmjdrwDto = new XmjdrwDto();
					z_xmjdrwDto.setRwid(StringUtil.generateUUID());
					z_xmjdrwDto.setFrwid(xmjdrwDto.getRwid());
					z_xmjdrwDto.setZt(StatusEnum.CHECK_NO.getCode());
					z_xmjdrwDto.setLrry(xmjdrwDto.getLrry());
					z_xmjdrwDto.setFzr(t_xmjdrwDto.getFzr());
					z_xmjdrwDto.setXmjdid(t_xmjdrwDto.getXmjdid());
					z_xmjdrwDto.setXmid(t_xmjdrwDto.getXmid());
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					z_xmjdrwDto.setKsrq(sdf.format(calendar.getTime()));
					if (StringUtil.isNotBlank(rwmbDto.getQx())) {
						calendar.add(Calendar.DATE, Integer.parseInt(rwmbDto.getQx()));
						z_xmjdrwDto.setJsrq(sdf.format(calendar.getTime()));
					}
					z_xmjdrwDto.setRwmbid(rwmbDto.getRwmbid());
					z_xmjdrwDto.setRwmc(rwmbDto.getRwmc());
					z_xmjdrwDto.setRwjb(rwmbDto.getRwjb());
					z_xmjdrwDto.setRwbq(rwmbDto.getRwbq());
					z_xmjdrwDto.setRwms(rwmbDto.getRwms());
					boolean isSuccess = insertDto(z_xmjdrwDto);
					if (!isSuccess)
						return false;
				}
			}
			//更新任务阶段日期
			boolean isSuccess = updateTaskTime(xmjdrwDto, xmjdxxDto, new XmjdxxDto());
			if(!isSuccess)
				return false;
		}
		int result = dao.taskSort(xmjdrwDto);
		return result != 0;
	}
	
	/**
	 * 任务阶段更改日期更新
	 */
	public boolean updateTaskTime(XmjdrwDto xmjdrwDto, XmjdxxDto xmjdxxDto, XmjdxxDto pre_xmjdxxDto){
		pre_xmjdxxDto.setXmjdid(xmjdrwDto.getPrejdid());
		pre_xmjdxxDto = xmjdxxService.getDto(pre_xmjdxxDto);
		List<RwrqDto> rwrqDtos = new ArrayList<>();
		RwrqDto s_rwrqDto = new RwrqDto();
		s_rwrqDto.setRwid(xmjdrwDto.getRwid());
		s_rwrqDto.setXmjdid(xmjdxxDto.getXmjdid());
		//移动后阶段
		RwrqDto t_rwrqDto = rwrqService.getDto(s_rwrqDto);
		if(t_rwrqDto == null){
			boolean result = insertOrUpdateRwrq(xmjdrwDto);
			if(!result)
				return false;
			t_rwrqDto = rwrqService.getDto(s_rwrqDto);
		}
		//移动前阶段
		s_rwrqDto.setXmjdid(pre_xmjdxxDto.getXmjdid());
		RwrqDto p_rwrqDto = rwrqService.getDto(s_rwrqDto);
		if(p_rwrqDto == null){
			boolean result = insertOrUpdateRwrq(xmjdrwDto);
			if(!result)
				return false;
			p_rwrqDto = rwrqService.getDto(s_rwrqDto);
		}
		RwrqDto rwrqDto = new RwrqDto();
		rwrqDto.setLrry(xmjdrwDto.getXgry());
		rwrqDto.setXgry(xmjdrwDto.getXgry());
		rwrqDto.setRwid(xmjdrwDto.getRwid());
		rwrqDto.setXmjdid(xmjdxxDto.getXmjdid());
		
		RwrqDto f_rwrqDto = new RwrqDto();
		f_rwrqDto.setLrry(xmjdrwDto.getXgry());
		f_rwrqDto.setXgry(xmjdrwDto.getXgry());
		f_rwrqDto.setRwid(xmjdrwDto.getRwid());
		f_rwrqDto.setXmjdid(pre_xmjdxxDto.getXmjdid());
		
		if(Integer.parseInt(pre_xmjdxxDto.getXh()) < Integer.parseInt(xmjdxxDto.getXh())){
			//移动后阶段
			rwrqDto.setSjksrq(DateTimeUtil.getCurrDateStr());
			if(t_rwrqDto != null){
				rwrqDto.setRqid(t_rwrqDto.getRqid());
			}else{
				rwrqDto.setRqid(StringUtil.generateUUID());
			}
			rwrqDtos.add(rwrqDto);
			//移动前阶段
			f_rwrqDto.setSjksrq(p_rwrqDto.getSjksrq());
			f_rwrqDto.setRqid(p_rwrqDto.getRqid());
			f_rwrqDto.setSjjsrq(DateTimeUtil.getCurrDateStr());
			rwrqDtos.add(f_rwrqDto);
			return rwrqService.insertOrUpdateRwrq(rwrqDtos);
		}else{
			//移动后阶段
			rwrqDto.setRqid(t_rwrqDto.getRqid());
			if(StringUtil.isNotBlank(t_rwrqDto.getSjksrq())){
				rwrqDto.setSjksrq(t_rwrqDto.getSjksrq());
			}else{
				rwrqDto.setSjksrq(DateTimeUtil.getCurrDateStr());
			}
			rwrqDtos.add(rwrqDto);
			//移动前阶段
			f_rwrqDto.setRqid(p_rwrqDto.getRqid());
			rwrqDtos.add(f_rwrqDto);
			return rwrqService.insertOrUpdateRwrq(rwrqDtos);
		}
	}

	/**
	 * 修改任务
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateGzglAndXmrw(XmjdrwDto xmjdrwDto){
		boolean result = updateDto(xmjdrwDto);
		if (!result)
			return false;
		if (!CollectionUtils.isEmpty(xmjdrwDto.getList())){
			boolean updateJdfss = rwrqService.updateJdfss(xmjdrwDto.getList());
			if (!updateJdfss){
				return false;
			}
		}
		//文件复制到正式文件夹，插入信息至正式表
		if(xmjdrwDto.getFjids()!=null && xmjdrwDto.getFjids().size() > 0){
			for (int i = 0; i < xmjdrwDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(xmjdrwDto.getFjids().get(i),xmjdrwDto.getRwid());
				if(!saveFile)
					return false;
			}
		}
		GzglDto gzglModel = new GzglDto();
		gzglModel.setYwmc(xmjdrwDto.getRwmc());
		gzglModel.setFzr(xmjdrwDto.getFzr());
		gzglModel.setQwwcsj(xmjdrwDto.getJsrq());
		gzglModel.setYwid(xmjdrwDto.getRwid());
		gzglModel.setYjgzl(xmjdrwDto.getYjgzl());
		gzglModel.setSjgzl(xmjdrwDto.getSjgzl());
		gzglModel.setXgry(xmjdrwDto.getXgry());
		int isSuccess = gzglDao.update(gzglModel);
		if (isSuccess == 0)
			return false;
		//修改时记录一下动态
		RwlyModel rwlyModel=new RwlyModel();
		rwlyModel.setLyid(StringUtil.generateUUID());
		rwlyModel.setRwid(xmjdrwDto.getRwid());
		rwlyModel.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
		rwlyModel.setLyxx("更新了任务");
		rwlyModel.setLrry(xmjdrwDto.getXgry());
		int succsss_flg=rwlydao.insert(rwlyModel);
		return succsss_flg != 0;
	}

	
	/**
	 * 项目研发列表查看项目详细信息
	 */
	public List<XmjdrwDto> selectjdrwxx(XmglDto xmglDto) {
		return dao.selectjdrwxx(xmglDto);
	}

	/**
	 * 处理个人任务提交时的信息,更新任务阶段信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateByTask(Map<String, Object> params) {
		// TODO Auto-generated method stub
		XmjdrwDto xmjdrwDto = (XmjdrwDto)params.get("dto");
		/*
		 * String qrry=(String) params.get("qrry"); xmjdrwDto.setFzr(qrry);
		 */
		//HttpServletRequest request = (HttpServletRequest)params.get("request");
		//如果阶段未改变，则不用处理，如果改变了阶段，则更新任务的阶段信息，同时序号为max(xh) + 1
		XmjdrwDto xmjdrwDtos=dao.getDtoById(xmjdrwDto.getRwid());
		XmjdxxDto xmjdxxDto = new XmjdxxDto();
		xmjdxxDto.setXmjdid(xmjdrwDto.getXmjdid());
		XmjdxxDto dto = xmjdxxService.getDto(xmjdxxDto);
		if(xmjdrwDtos!=null) {
			if(!xmjdrwDto.getXmjdid().equals(xmjdrwDtos.getXmjdid())) {
				if (StringUtil.isBlank(xmjdrwDtos.getJdtime())){
					if (StringUtil.isNotBlank(xmjdrwDtos.getXmid())){
						XmglDto xmglDto = new XmglDto();
						xmglDto.setXmid(xmjdrwDtos.getXmid());
						List<XmjdxxDto> xmjdxxDtos = xmjdxxService.selectStageByXmid(xmglDto);

						if (StringUtil.isBlank(xmjdrwDtos.getLrsj())){
							xmjdrwDtos.setLrsj("-");
						}
						StringBuilder str= new StringBuilder("" + xmjdrwDtos.getLrsj() + ",");
						for (int i = 0; i < xmjdxxDtos.size()-1; i++) {
							if (i!= xmjdxxDtos.size()-2){
								str.append("-,");
							}else {
								str.append("-");
							}
						}
						xmjdrwDtos.setJdtime(str.toString());
					}
				}
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm");
				Date date = new Date();
				String[] strings = xmjdrwDtos.getJdtime().split(",");
				strings[Integer.parseInt(dto.getXh())-1] = simpleDateFormat.format(date);
				StringBuilder str = new StringBuilder();
				for (int i = 0; i < strings.length; i++) {
					if (i>Integer.parseInt(dto.getXh())-1){
						str.append("-");
					}else{
						str.append(strings[i]);
					}
					if (i!=strings.length-1)
						str.append(",");
				}
				xmjdrwDto.setJdtime(str.toString());
				xmjdrwDto.setZt(null);
				return dao.updateJdid(xmjdrwDto);
			}
		}
		return true;
	}
	
	/**
	 * 查询个人项目研发列表
	 */
	public List<XmjdrwDto> getPagedSearchTaskList(XmjdrwDto xmjdrwDto){
		XmglDto xmglDto =new XmglDto();
		xmglDto.setXmid(xmjdrwDto.getXmid());
		List<XmcyDto> xmcyList=xmcyDao.selectSelMember(xmglDto);
		StringBuilder sb=new StringBuilder();
		if(xmcyList!=null) {
			sb.setLength(0);
			for (int i = 0; i < xmcyList.size(); i++){
				if(i!=0) 
					sb.append(",");
				sb.append(xmcyList.get(i).getZsxm());
				
			}
			xmjdrwDto.setXmcylist(sb.toString());
		}
		return dao.getPagedSearchTaskList(xmjdrwDto);
	}
	
	/**
	 * 查询子任务通过阶段
	 */
	public List<XmjdxxDto> getzrwByjd(XmjdrwDto xmjdrwDto){
		if(xmjdrwDto.getXmid()==null|| Objects.equals(xmjdrwDto.getXmid(), ""))
			return null;
		XmjdxxDto xmjdxxDto=new XmjdxxDto();
		xmjdxxDto.setXmid(xmjdrwDto.getXmid());
		List<XmjdxxDto> listjdxx= xmjdxxDao.getDtoList(xmjdxxDto);
		XmjdxxDto xmjdxxDtos=dao.getjdxxByrwid(xmjdrwDto);
		if(listjdxx!=null&&listjdxx.size()>0) {
			for (int i =listjdxx.size()-1; i > -1; i--){
				if(Integer.parseInt(listjdxx.get(i).getXh())>Integer.parseInt(xmjdxxDtos.getXh())) { 
					listjdxx.remove(i);
					}
			}
			 for (int i = 0; i <listjdxx.size(); i++){
				 xmjdrwDto.setXmjdid(listjdxx.get(i).getXmjdid());
				 List<XmjdrwDto> listdto=dao.getListzrwbyjd(xmjdrwDto);
				 for (XmjdrwDto dto : listdto) {
					 //根据任务ID查询附件表信息
					 List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(dto.getRwid());
					 if (fjcfbDtos != null && fjcfbDtos.size() > 0) {
						 for (FjcfbDto fjcfbDto : fjcfbDtos) {
							 String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
							 fjcfbDto.setWjmhz(wjmhz);
						 }
						 listdto.get(i).setFjcfbDtos(fjcfbDtos);
					 }
				 }
				 listjdxx.get(i).setXmjdrwDtos(listdto);
			 }
		}
		return listjdxx;
	}
	
	/**
	 * 新增保存项目研发信息
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean saveSearchProject(XmjdrwDto xmjdrwDto) {
		//新增项目信息
		XmglDto xmglDto=new XmglDto();
		xmglDto.setXmmc(xmjdrwDto.getXmmc());
		xmglDto.setLrry(xmjdrwDto.getLrry());
		xmglDto.setMbid(xmjdrwDto.getMbid());
		xmglDto.setXmlb(ProjectTypeEnum.PROJECT.getCode());
		List<OpennessTypeEnum> opennesslist = xmglService.getOpennessType();
		for (OpennessTypeEnum opennessTypeEnum : opennesslist) {
			if ((OpennessTypeEnum.PUBLIC_PROJECT.getValue()).equals(opennessTypeEnum.getValue())) {
				xmglDto.setXmgkx(opennessTypeEnum.getCode());
			}
		}
		boolean xmsave=xmglService.addSaveXm(xmglDto);
		if(!xmsave)
			return false;
		//清空默认添加的项目成员
		boolean deletexmcy=xmcyService.deleteXmcyByXmid(xmglDto);
		if(!deletexmcy)
			return false;
		//保存项目成员
		List<String> yhids = Arrays.asList(xmjdrwDto.getYhids().split(","));
		xmglDto.setIds(yhids);
		int result=xmcyService.insertXmcyByYhids(xmglDto);
		if(result==0) 
			return false;
		
		//新增项目信息成功则新增保存项目任务信息
		//将任务默认创建在序号为1的阶段下
		List<XmjdxxDto> xmjdlist=xmjdxxService.selectStageByXmid(xmglDto);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		String ksrq=sdf.format(date);
		for (XmjdxxDto xmjdxxDto : xmjdlist) {
			if (("1").equals(xmjdxxDto.getXh())) {
				xmjdrwDto.setXmjdid(xmjdxxDto.getXmjdid());
				List<RwmbDto> rwmblist = rwmbDao.getrwmbbyid(xmjdxxDto.getJdid());
				xmjdrwDto.setXmid(xmglDto.getXmid());
				xmjdrwDto.setZt(StatusEnum.CHECK_NO.getCode());
				xmjdrwDto.setRwid(StringUtil.generateUUID());
				boolean xmrwsave = insertDto(xmjdrwDto);
				if (rwmblist != null && rwmblist.size() > 0) {
					int zrwtjcs = 0;//子任务添加次数
					for (RwmbDto rwmbDto : rwmblist) {
						XmjdrwDto xmjdzrwDto = new XmjdrwDto();
						xmjdzrwDto.setXmjdid(xmjdxxDto.getXmjdid());
						xmjdzrwDto.setRwid(StringUtil.generateUUID());
						xmjdzrwDto.setXmid(xmglDto.getXmid());
						xmjdzrwDto.setRwmc(rwmbDto.getRwmc());
						xmjdzrwDto.setFzr(rwmbDto.getMrfzr());
						xmjdzrwDto.setRwjb(rwmbDto.getRwjb());
						xmjdzrwDto.setRwbq(rwmbDto.getRwbq());
						xmjdzrwDto.setRwms(rwmbDto.getRwms());
						xmjdzrwDto.setRwmbid(rwmbDto.getRwmbid());
						xmjdzrwDto.setKsrq(ksrq);
						if (rwmbDto.getQx() != null) {
							long qx = (long) Integer.parseInt(rwmbDto.getQx()) * 24 * 60 * 60 * 1000;
							try {
								long jsrqms = sdf.parse(sdf.format(date)).getTime() + qx;
								xmjdzrwDto.setJsrq(sdf.format(jsrqms));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						xmjdzrwDto.setLrry(xmglDto.getLrry());
						xmjdzrwDto.setZt(StatusEnum.CHECK_NO.getCode());
						xmjdzrwDto.setFrwid(xmjdrwDto.getRwid());
						xmjdzrwDto.setZwcrwid(xmjdrwDto.getRwid());
						if (!yhids.contains(rwmbDto.getMrfzr())) {
							xmjdrwDto.setMbzrwfzrxx("1");
							xmjdzrwDto.setFzr(xmjdrwDto.getFzr());
						}
						boolean mbzrwresult = insertDto(xmjdzrwDto);
						if (mbzrwresult)
							zrwtjcs = zrwtjcs + 1;
					}
					return rwmblist.size() == zrwtjcs;
				}
				return xmrwsave;
			}
		}
		return false;
	}

	/**
	 * 删除项目研发任务
	 */
	public boolean deleteXmyfrws(XmjdrwDto xmjdrwDto) {
		return dao.deleteXmyfrws(xmjdrwDto);
	}
	
	/**
	 * 根据xmids查询项目任务信息
	 */
	public List<XmjdrwDto> selectXmrwxxs(XmjdrwDto xmjdrwDto) {
		return dao.selectXmrwxxs(xmjdrwDto);
	}

	@Override
	public List<XmjdrwDto> getXmrwxxs(XmjdrwDto xmjdrwDto) {
		return dao.getXmrwxxs(xmjdrwDto);
	}

	/**
	 * 删除项目研发任务
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteSearchProjecttoHsz(XmjdrwDto xmjdrwDto) {
		List<String> xmids=xmjdrwDto.getXmids();
		List<String> ids=xmjdrwDto.getIds();
		List<XmjdrwDto> xmjdrwlist=dao.getXzrwsAndZrws(xmjdrwDto);
		List<String> rwlist=new ArrayList<>();
		for (XmjdrwDto dto : xmjdrwlist) {
			String rwid = dto.getRwid();
			rwlist.add(rwid);
		}
		xmjdrwDto.setRwids(rwlist);
		int deleteXmrws=dao.deleterwByRwid(xmjdrwDto);
		if(deleteXmrws>0) {
			//根据ids删除工作管理表中任务信息
			GzglDto gzglDto=new GzglDto();
			gzglDto.setIds(ids);
			gzglDto.setScry(xmjdrwDto.getScry());
			gzglDao.deletegzrwByrwids(gzglDto);
			//判断该项目下是否有其他任务，若没有则放入回收站
			for (String xmid : xmids) {
				xmjdrwDto.setXmid(xmid);
				List<XmjdrwDto> xmjdrwxxs = selectXmrwxxs(xmjdrwDto);
				if (xmjdrwxxs.size() <= 0) {
					XmglDto xmglDto = new XmglDto();
					xmglDto.setXmid(xmjdrwDto.getXmid());
					xmglService.deleteByXmid(xmglDto);
					return true;
				}
			}
			return true;
		}
		return false;
		
	}
	
	/**
	 * 修改项目研发任务信息
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean modXmyfrw(XmjdrwDto xmjdrwDto) {
		XmjdrwDto xmjdrwxx=getSearchTaskRwDto(xmjdrwDto);
		boolean updateXmjdrwAndGzgl=updateGzglAndXmrw(xmjdrwDto);
		if(updateXmjdrwAndGzgl) {
			XmglDto xmglDto=new XmglDto();
			xmglDto.setXgry(xmjdrwDto.getXgry());
			xmglDto.setXmid(xmjdrwDto.getXmid());
			xmglDto.setYhid(xmjdrwDto.getYhid());
			xmglDto.setYhids(xmjdrwDto.getYhids());
			boolean updateXmglxx=xmglService.modSaveProject(xmglDto);
			boolean updateXmcy=xmcyService.editSaveMember(xmglDto);
			RwrqDto rwrqDto=new RwrqDto();
			rwrqDto.setRwid(xmjdrwDto.getRwid());
			rwrqDto.setXmjdid(xmjdrwxx.getXmjdid());
			rwrqDto.setXgry(xmjdrwDto.getXgry());
			int updaterwrqxx=rwrqService.modRqxxByRwidAndXmjdid(rwrqDto);
			return updateXmglxx && updateXmcy && updaterwrqxx > 0;
		}
		return false;
	}
	
	/**
	 * 项目研发列表阶段转换以及项目任务排序
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean ResearchtaskSort(XmjdrwDto xmjdrwDto) {
		XmjdrwDto dqjdrwDto=dao.getSearchTaskRwDto(xmjdrwDto);
		if (StringUtil.isNotBlank(xmjdrwDto.getXmjdid()) && StringUtil.isNotBlank(xmjdrwDto.getRwid())){
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<XmjdrwDto> xmjdrws=dao.selectTaskByXmjdid(xmjdrwDto.getXmjdid());
			if(xmjdrws!=null && xmjdrws.size()>0) {
				String maxxh=xmjdrws.get(xmjdrws.size()-1).getXh();
				xmjdrwDto.setXh(maxxh);
			}else {
				String maxxh="1";
				xmjdrwDto.setXh(maxxh);
			}
			int result = dao.updateMaxxh(xmjdrwDto);
			if (result == 0)
				return false;
			XmjdrwDto f_xmjdrwDto = dao.getDtoById(xmjdrwDto.getRwid());
			RwrqDto exitrwrq=new RwrqDto();
			exitrwrq.setRwid(xmjdrwDto.getRwid());
			exitrwrq.setXmjdid(xmjdrwDto.getXmjdid());
			RwrqDto exitrwrqxx=rwrqDao.getRqxxByRwidAndXmjdid(exitrwrq);
			RwrqDto rwrqDto=new RwrqDto();
			rwrqDto.setRqid(StringUtil.generateUUID());
			rwrqDto.setSjksrq(sdf.format(calendar.getTime()));
			rwrqDto.setXmjdid(xmjdrwDto.getXmjdid());
			rwrqDto.setXmid(f_xmjdrwDto.getXmid());
			rwrqDto.setLrry(xmjdrwDto.getXgry());
			rwrqDto.setRwid(xmjdrwDto.getRwid());
			int saverwrq;
			if(exitrwrqxx!=null) {
				rwrqDto.setXgry(xmjdrwDto.getXgry());
				saverwrq=rwrqDao.modRqxxByRwidAndXmjdid(rwrqDto);
			}else {
				saverwrq=rwrqDao.insert(rwrqDto);
			}
			RwrqDto qrwrqDto=new RwrqDto();
			qrwrqDto.setRqid(StringUtil.generateUUID());
			qrwrqDto.setSjjsrq(sdf.format(calendar.getTime()));
			qrwrqDto.setXmjdid(dqjdrwDto.getXmjdid());
			qrwrqDto.setXmid(f_xmjdrwDto.getXmid());
			qrwrqDto.setLrry(xmjdrwDto.getXgry());
			qrwrqDto.setRwid(xmjdrwDto.getRwid());
			qrwrqDto.setXgry(xmjdrwDto.getXgry());
			int saveqrwrq=rwrqDao.modRqxxByRwidAndXmjdid(qrwrqDto);
			if(saverwrq>0 && saveqrwrq>0) {
				if(StringUtil.isBlank(f_xmjdrwDto.getFrwid())){
					//判断阶段ID
					XmjdxxDto xmjdxxDto = new XmjdxxDto();
					xmjdxxDto.setXmjdid(xmjdrwDto.getXmjdid());
					xmjdxxDto = xmjdxxService.getDto(xmjdxxDto);
					//查询模板任务，新增子任务，增加到工作管理表
					List<RwmbDto> rwmbDtos = rwmbDao.getrwmbbyid(xmjdxxDto.getJdid());
					if(rwmbDtos != null && rwmbDtos.size() > 0){
						XmjdrwDto t_xmjdrwDto = dao.getSearchTaskRwDto(xmjdrwDto);
						//查询任务模板ID不为空的子任务
						List<String> rwmbids = dao.selectSubTaskByRwid(xmjdrwDto);
						for (RwmbDto rwmbDto : rwmbDtos) {
							if (rwmbids != null && rwmbids.contains(rwmbDto.getRwmbid())) {
								continue;
							}
							XmjdrwDto z_xmjdrwDto = new XmjdrwDto();
							z_xmjdrwDto.setRwid(StringUtil.generateUUID());
							z_xmjdrwDto.setFrwid(xmjdrwDto.getRwid());
							z_xmjdrwDto.setZt(StatusEnum.CHECK_NO.getCode());
							z_xmjdrwDto.setLrry(xmjdrwDto.getLrry());
							z_xmjdrwDto.setFzr(t_xmjdrwDto.getFzr());
							z_xmjdrwDto.setXmjdid(t_xmjdrwDto.getXmjdid());
							z_xmjdrwDto.setXmid(t_xmjdrwDto.getXmid());
							z_xmjdrwDto.setKsrq(sdf.format(calendar.getTime()));
							if (StringUtil.isNotBlank(rwmbDto.getQx())) {
								calendar.add(Calendar.DATE, Integer.parseInt(rwmbDto.getQx()));
								z_xmjdrwDto.setJsrq(sdf.format(calendar.getTime()));
							}
							z_xmjdrwDto.setRwmbid(rwmbDto.getRwmbid());
							z_xmjdrwDto.setRwmc(rwmbDto.getRwmc());
							z_xmjdrwDto.setRwjb(rwmbDto.getRwjb());
							z_xmjdrwDto.setRwbq(rwmbDto.getRwbq());
							z_xmjdrwDto.setRwms(rwmbDto.getRwms());
							boolean isSuccess = insertDto(z_xmjdrwDto);
							if (!isSuccess)
								return false;
						}
					}
				}
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * 任务转交更新相应业务信息
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateByTaskTransmit(Map<String,Object> params) throws BusinessException{
		//获取fzrs和业务id
		String[] fzrs = (String[]) params.get("fzrs");
		String ywid = (String)params.get("ywid");
		//判断业务id不能为空，负责人只能为一
		if(StringUtil.isBlank(ywid)) 
			return false;
		if(fzrs==null) {
			throw new BusinessException("msg","负责人不能为空！");
		} 
		if(fzrs.length!=1) {
			throw new BusinessException("msg","研发任务负责人为唯一！");
		}
		//通过业务id查询当前项目的项目成员
		String fzr=Arrays.toString(fzrs).substring(Arrays.toString(fzrs).lastIndexOf("[")+1).replaceAll("\"", "").replaceAll("]", "");
		XmjdrwDto xmjdrwDto=new XmjdrwDto();
		xmjdrwDto.setRwid(ywid);
		xmjdrwDto.setFzr(fzr);
		List<XmcyDto> xmcylist =xmcyService.getxmcyByxmid(xmjdrwDto);
		//判断当前的负责人是不是所属项目的项目成员
		int flag=0;
		for (XmcyDto xmcyDto : xmcylist) {
			if ((fzr).equals(xmcyDto.getYhid())) {
				flag++;
			}
		}
		//如果flag为0，说明没有匹配到项目成员负责人
		if(flag==0){
			throw new BusinessException("msg","所选负责人不属于当前项目成员！");
		}	
			
		//执行修改项目阶段任务
		int result=dao.updateDto(xmjdrwDto);
		if(result==0) 
			return false;
		//执行修改任务列表的负责人
		GzglDto gzglDto=new GzglDto();
		gzglDto.setYwid(ywid);
		gzglDto.setFzr(xmjdrwDto.getFzr());
		int result2=gzglDao.update(gzglDto);
		if(result2==0) 
			return false;
		//发送钉钉
//		String token = talkUtil.getToken();
		XmjdrwDto xmjdrwDtos=dao.getDtoById(ywid);
		if(Objects.equals(xmjdrwDtos.getRwjbmc(), "") ||xmjdrwDtos.getRwjbmc()==null) {
			xmjdrwDtos.setRwjbmc("无"); 
			} 
		XmjdrwDto xmjdrwDto_yhm=dao.getYhmByFzr(xmjdrwDto.getFzr());
		if(xmjdrwDto_yhm!=null && StringUtil.isNotBlank(xmjdrwDto_yhm.getYhm())){
			talkUtil.sendWorkMessage(xmjdrwDto_yhm.getYhm(),xmjdrwDto_yhm.getDdid(),
					xxglService.getMsg("ICOMM_SH00008"),xxglService.getMsg("ICOMM_SH00014",xmjdrwDtos.getXmmc(),xmjdrwDtos.getRwmc(),xmjdrwDtos. getRwjbmc(),xmjdrwDtos.getJsrq())); }
		return true;
	}
	
	/**
	 * 任务确认更新相应业务信息
	 */
	public boolean updateByTaskConfirm(Map<String,Object> params) {
		XmjdrwDto xmjdrwDto = (XmjdrwDto)params.get("dto");

		//如果阶段未改变，则不用处理，如果改变了阶段，则更新任务的阶段信息，同时序号为max(xh) + 1
		XmjdrwDto t_xmjdrwDto=dao.getDtoById(xmjdrwDto.getRwid());
		XmjdxxDto xmjdxxDto = new XmjdxxDto();
		xmjdxxDto.setXmjdid(xmjdrwDto.getXmjdid());
		XmjdxxDto dto = xmjdxxService.getDto(xmjdxxDto);
		if(t_xmjdrwDto!=null) {
			if(!xmjdrwDto.getXmjdid().equals(t_xmjdrwDto.getXmjdid())) {
				if (StringUtil.isBlank(t_xmjdrwDto.getJdtime())){
					if (StringUtil.isNotBlank(t_xmjdrwDto.getXmid())){
						XmglDto xmglDto = new XmglDto();
						xmglDto.setXmid(t_xmjdrwDto.getXmid());
						List<XmjdxxDto> xmjdxxDtos = xmjdxxService.selectStageByXmid(xmglDto);

						if (StringUtil.isBlank(t_xmjdrwDto.getLrsj())){
							t_xmjdrwDto.setLrsj("-");
						}
						StringBuilder str= new StringBuilder("" + t_xmjdrwDto.getLrsj() + ",");
						for (int i = 0; i < xmjdxxDtos.size()-1; i++) {
							if (i!= xmjdxxDtos.size()-2){
								str.append("-,");
							}else {
								str.append("-");
							}
						}
						t_xmjdrwDto.setJdtime(str.toString());
					}
				}
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm");
				Date date = new Date();
				String[] strings = t_xmjdrwDto.getJdtime().split(",");
				strings[Integer.parseInt(dto.getXh())-1] = simpleDateFormat.format(date);
				StringBuilder str = new StringBuilder();
				for (int i = 0; i < strings.length; i++) {
					if (i>Integer.parseInt(dto.getXh())-1){
						str.append("-");
					}else{
						str.append(strings[i]);
					}
					if (i!=strings.length-1)
						str.append(",");
				}
				xmjdrwDto.setJdtime(str.toString());
				boolean result=dao.updateJdid(xmjdrwDto);
				if (!result)
					return false;
			}
		}
		XmjdrwDto xmjdrwDto1 = new XmjdrwDto();
		xmjdrwDto1.setRwid(xmjdrwDto.getRwid());
		if (null != params.get("zt")){
			xmjdrwDto1.setZt(params.get("zt").toString());
		}
		dao.updateDto(xmjdrwDto1);
		return true;
	}

	/**
	 * 任务转交更新相应业务信息
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateByClaimTaskTransmit(Map<String,Object> params) throws BusinessException{
		//获取fzrs和业务id
		String[] fzrs = (String[]) params.get("fzrs");
		String ywid = (String)params.get("ywid");
		//判断业务id不能为空，负责人只能为一
		if(StringUtil.isBlank(ywid))
			return false;
		if(fzrs==null) {
			throw new BusinessException("msg","负责人不能为空！");
		}
		if(fzrs.length!=1) {
			throw new BusinessException("msg","研发任务负责人为唯一！");
		}
		//通过业务id查询当前项目的项目成员
		String fzr=Arrays.toString(fzrs).substring(Arrays.toString(fzrs).lastIndexOf("[")+1).replaceAll("\"", "").replaceAll("]", "");
		XmjdrwDto xmjdrwDto=new XmjdrwDto();
		xmjdrwDto.setRwid(ywid);
		xmjdrwDto.setFzr(fzr);
		List<XmcyDto> xmcylist =xmcyService.getxmcyByxmid(xmjdrwDto);
		//判断当前的负责人是不是所属项目的项目成员
		int flag=0;
		for (XmcyDto xmcyDto : xmcylist) {
			if ((fzr).equals(xmcyDto.getYhid())) {
				flag++;
			}
		}
		//如果flag为0，说明没有匹配到项目成员负责人
		if(flag==0){
			throw new BusinessException("msg","所选负责人不属于当前项目成员！");
		}

		//执行修改项目阶段任务
		int result=dao.updateDto(xmjdrwDto);
		if(result==0)
			return false;
		//执行修改任务列表的负责人
		GzglDto gzglDto=new GzglDto();
		gzglDto.setYwid(ywid);
		gzglDto.setFzr(xmjdrwDto.getFzr());
		int result2=gzglDao.update(gzglDto);
		return result2!=0;
	}

	
	/**
	 * 通过项目查询当前任务所在阶段的序号是否为此项目中最后一个阶段
	 */
	public List<XmjdrwDto> getXhIsLastJdXh(List<XmjdrwDto> list){
		return dao.getXhIsLastJdXh(list);
	}
	
	/**
	 * 新建项目阶段任务
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addSaveProjectTask(XmjdrwDto xmjdrwDto) {
		// TODO Auto-generated method stub
		//新增项目任务
		xmjdrwDto.setZt(StatusEnum.CHECK_NO.getCode());
		xmjdrwDto.setRwid(StringUtil.generateUUID());
		if (StringUtil.isNotBlank(xmjdrwDto.getXmid())){
			XmglDto xmglDto = new XmglDto();
			xmglDto.setXmid(xmjdrwDto.getXmid());
			List<XmjdxxDto> xmjdxxDtos = xmjdxxService.selectStageByXmid(xmglDto);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm");
			Date date = new Date();
			StringBuilder str= new StringBuilder();
			for (int i = 0; i < xmjdxxDtos.size(); i++) {
				if (xmjdxxDtos.get(i).getXmjdid().equals(xmjdrwDto.getXmjdid())){
					str.append(simpleDateFormat.format(date)).append(",");
				}else{
					if (i!= xmjdxxDtos.size()-1){
						str.append("-,");
					}else {
						str.append("-");
					}

				}
			}
			xmjdrwDto.setJdtime(str.toString());
		}
		boolean result = insertDto(xmjdrwDto);
		if(!result)
			return false;
		//文件复制到正式文件夹，插入信息至正式表
		if(xmjdrwDto.getFjids()!=null && xmjdrwDto.getFjids().size() > 0){
			for (int i = 0; i < xmjdrwDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(xmjdrwDto.getFjids().get(i),xmjdrwDto.getRwid());
				if(!saveFile)
					return false;
			}
		}
		//判断父任务ID是否为空
		if(StringUtil.isBlank(xmjdrwDto.getFrwid())){
			//判断阶段ID
			XmjdxxDto xmjdxxDto = new XmjdxxDto();
			xmjdxxDto.setXmjdid(xmjdrwDto.getXmjdid());
			xmjdxxDto = xmjdxxService.getDto(xmjdxxDto);
			if(xmjdxxDto != null && StringUtil.isNotBlank(xmjdxxDto.getJdid())){
				//查询模板任务，新增子任务，增加到工作管理表
				List<RwmbDto> rwmbDtos = rwmbDao.getrwmbbyid(xmjdxxDto.getJdid());
				if(rwmbDtos != null && rwmbDtos.size() > 0){
					for (RwmbDto rwmbDto : rwmbDtos) {
						XmjdrwDto z_xmjdrwDto = new XmjdrwDto();
						z_xmjdrwDto.setRwid(StringUtil.generateUUID());
						z_xmjdrwDto.setFrwid(xmjdrwDto.getRwid());
						z_xmjdrwDto.setZt(StatusEnum.CHECK_NO.getCode());
						z_xmjdrwDto.setLrry(xmjdrwDto.getLrry());
						z_xmjdrwDto.setFzr(xmjdrwDto.getFzr());
						z_xmjdrwDto.setXmjdid(xmjdrwDto.getXmjdid());
						z_xmjdrwDto.setXmid(xmjdrwDto.getXmid());
						Calendar calendar = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						z_xmjdrwDto.setKsrq(sdf.format(calendar.getTime()));
						calendar.add(Calendar.DATE, Integer.parseInt(rwmbDto.getQx()));
						z_xmjdrwDto.setJsrq(sdf.format(calendar.getTime()));
						z_xmjdrwDto.setRwmbid(rwmbDto.getRwmbid());
						z_xmjdrwDto.setRwmc(rwmbDto.getRwmc());
						z_xmjdrwDto.setRwjb(rwmbDto.getRwjb());
						z_xmjdrwDto.setRwbq(rwmbDto.getRwbq());
						z_xmjdrwDto.setRwms(rwmbDto.getRwms());
						result = insertDto(z_xmjdrwDto);
						if (!result)
							return false;
					}
				}
			}
		}
		return true;
	}
	
    /**
     * 根据rwid查询项目研发列表任务信息
     */
	@Override
	public XmjdrwDto getSearchTaskRwDto(XmjdrwDto xmjdrwDto) {
		return dao.getSearchTaskRwDto(xmjdrwDto);
	}
	
	   /**
     * 根据frwid查询子任务信息
     */
	@Override
    public List<XmjdrwDto> getDtoListById(String frwid){
    	return dao.getDtoListById(frwid);
    }

	@Override
	public List<XmjdrwDto> selectStageByRwid(XmjdrwDto xmjdrwDto) {
		return dao.selectStageByRwid(xmjdrwDto);
	}
}
