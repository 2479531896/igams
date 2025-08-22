package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.dao.post.IGzglDao;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.enums.TaskMassageTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.RedisXmlConfig;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GzglServiceImpl extends BaseBasicServiceImpl<GzglDto, GzglModel, IGzglDao> implements IGzglService, IFileImport {
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IGzlcglService gzlcglService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	ICommonService commonService;
	@Autowired
	RedisXmlConfig redisXmlConfig;
	@Autowired
	DingTalkUtil talkUtil;

	@Autowired
	IShtxService shtxService;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	RedisUtil redisUtil;
	private final Logger logger = LoggerFactory.getLogger(GzglServiceImpl.class);
	/**
	 * 分页查询培训工作管理信息列表
	 */
	public List<GzglDto> getDtoTrainList(GzglDto gzglDto){return dao.getDtoTrainList(gzglDto);}
	/**
	 * 分页查询单个培训的已学习/未学习列表
	 */
	public List<GzglDto> getPagedTrainList(GzglDto gzglDto){
		return dao.getPagedTrainList(gzglDto);
	}
	/**
	 * 根据文件审核插入数据
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDtoByWjsh(List<GzglDto> gzglDtos) {
		// TODO Auto-generated method stub
		int result = dao.insertDtoByWjsh(gzglDtos);
		return result != 0;
	}

	@Override
	public List<GzglDto> getStatisDtoList(GzglDto gzglDto) {
		return dao.getStatisDtoList(gzglDto);
	}

	@Override
	public List<GzglDto> getTabInfo(GzglDto gzglDto) {
		return dao.getTabInfo(gzglDto);
	}

	@Override
	public List<GzglDto> getTaskStatistics(GzglDto gzglDto) {
		return dao.getTaskStatistics(gzglDto);
	}

	@Override
	public List<GzglDto> getDepartTaskStatistics(GzglDto gzglDto) {
		return dao.getDepartTaskStatistics(gzglDto);
	}

	/**
	 * 根据工作ID查询任务
	 */
	public GzglDto selectDtoBygzid(String gzid){
		return dao.selectDtoBygzid(gzid);
	}

	/**
	 * 进度提交保存
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean progressSaveTask(GzglDto gzglDto,HttpServletRequest req) {
		//是否已经保存附件
		boolean isSaveFile = false;
		if(StringUtil.isNotBlank(gzglDto.getRwid())){
			GzglDto gzglDto_rw=new GzglDto();
			gzglDto_rw.setLyid(StringUtil.generateUUID());
			gzglDto_rw.setRwid(gzglDto.getRwid());
			gzglDto_rw.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
			GzglDto xmjdxx = dao.getXmjdxx(gzglDto.getXmjdid());
			gzglDto_rw.setLyxx("将任务阶段更新为 "+xmjdxx.getJdmc());
			gzglDto_rw.setLrry(gzglDto.getXgry());
			dao.insertRwly(gzglDto_rw);
		}
		GzglDto t_gzglDto = dao.getDtoById(gzglDto.getGzid());
		// TODO Auto-generated method stub
		if(StatusEnum.CHECK_PASS.getCode().equals(gzglDto.getZt())){
			gzglDto.setDqjd("100");
			gzglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
			if(StringUtil.isBlank(t_gzglDto.getWcsj())){
				gzglDto.setWcsj("1");
			}
            gzglDto.setQwwcsj(gzglDto.getJzrq());
			//插入信息到工作流程表
			boolean insert = gzlcglService.insertBygzglDto(gzglDto);
			if(!insert)
				return false;
			//发送钉钉消息
//			String token = talkUtil.getToken();
			User user=new User();
			user.setYhid(gzglDto.getQrry());
			User userInfoById = commonService.getUserInfoById(user);
			if(userInfoById!=null&&StringUtil.isNotBlank(gzglDto.getDdid())){
				talkUtil.sendWorkMessage(userInfoById.getYhm(), userInfoById.getDdid(), xxglService.getMsg("ICOMM_SH00009"),xxglService.getMsg("ICOMM_SH00001",t_gzglDto.getZsxm(),t_gzglDto.getYwmc() ,t_gzglDto.getRwmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
			}

			if(StringUtil.isNotBlank(gzglDto.getRwid())){
				GzglDto gzglDto_t=new GzglDto();
				gzglDto_t.setLyid(StringUtil.generateUUID());
				gzglDto_t.setRwid(gzglDto.getRwid());
				gzglDto_t.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
				if (userInfoById != null) {
					gzglDto_t.setLyxx("将任务提交给 "+userInfoById.getZsxm());
				}
				gzglDto_t.setLrry(gzglDto.getXgry());
				dao.insertRwly(gzglDto_t);
			}
		}else if(StatusEnum.CHECK_SUBMIT.getCode().equals(gzglDto.getZt())){
			gzglDto.setDqjd("100");
			gzglDto.setSftg("1");
			if(StringUtil.isBlank(t_gzglDto.getWcsj())){
				gzglDto.setWcsj("1");
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			gzglDto.setGzjssj(simpleDateFormat.format(date));
			gzglDto.setZt(StatusEnum.CHECK_PASS.getCode());
			gzglDto.setLrry(gzglDto.getXgry());
			gzglDto.setSqr(gzglDto.getXgry());
			//插入信息到工作流程表
			boolean insert = gzlcglService.insertGzlcxxByQrrw(gzglDto);
			if(!insert)
				return false;
			isSaveFile = true;
		}

		if(StringUtil.isNotBlank(gzglDto.getRwid())){
			if(StringUtil.isNotBlank(gzglDto.getOld_xmjdid())&&(!gzglDto.getOld_xmjdid().equals(gzglDto.getXmjdid()))){
				GzglDto gzglDto_t=new GzglDto();
				gzglDto_t.setRwid(gzglDto.getRwid());
				gzglDto_t.setXmjdid(gzglDto.getOld_xmjdid());
				GzglDto rwrq = getRwrq(gzglDto_t,gzglDto.getXgry());
				if(rwrq!=null){
					if(StringUtil.isBlank(rwrq.getSjjsrq())){
						gzglDto_t.setSjjsrq(DateTimeUtil.getCurrDateStr());
						dao.modRwrqByRwidAndXmjdid(gzglDto_t);
					}
				}
			}
		}


		boolean updateDto = updateDto(gzglDto);
		if(!updateDto)
			return false;
		//insertGzlcxxByQrrw 方法里存在保存附件的情况，两边都执行会报重复插入错误
		if(!isSaveFile) {
			//附件复制到正式文件夹
			if(gzglDto.getFjids()!=null && !gzglDto.getFjids().isEmpty()){
				for (int i = 0; i < gzglDto.getFjids().size(); i++) {
					boolean saveFile = fjcfbService.save2RealFile(gzglDto.getFjids().get(i),gzglDto.getGzid());
					if(!saveFile)
						return false;
				}
			}
		}
		gzglDto.setZt(null);
		//处理提交时的扩展信息
		dealTaskExtend(gzglDto,req);
		if(StringUtil.isNotBlank(gzglDto.getRwid())){
			GzglDto rwrq = getRwrq(gzglDto,gzglDto.getXgry());
			if(rwrq!=null){
				if(StringUtil.isBlank(rwrq.getSjksrq())){
					gzglDto.setSjksrq(DateTimeUtil.getCurrDateStr());
				}
				if(StringUtil.isBlank(rwrq.getJzrq())){
					if(StringUtil.isBlank(gzglDto.getJzrq())){
						Object oncoxtsz=redisUtil.hget("matridx_xtsz","task.stage.cycle");
						if(oncoxtsz!=null) {
							XtszDto xtszDto = JSON.parseObject(String.valueOf(oncoxtsz), XtszDto.class);
							if(StringUtil.isNotBlank(xtszDto.getSzz())){
								String szz=xtszDto.getSzz();
								Calendar calendar = Calendar.getInstance();
								calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(szz));
								String time = DateUtils.format(calendar.getTime(), "yyyy-MM-dd");
								gzglDto.setJzrq(time);
							}
						}
					}
				}
			}
			if (StringUtil.isNotBlank(gzglDto.getQrry())){
				gzglDto.setClry(gzglDto.getQrry());
			}else {
				gzglDto.setClry(gzglDto.getXgry());
			}
			return dao.modRwrqByRwidAndXmjdid(gzglDto);
		}
		return true;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateRwrqList(List<GzglDto> list) {
		return dao.updateRwrqList(list)!=0;
	}

	/** 
	 * 修改任务信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateDto(GzglDto gzglDto){
		int result = dao.update(gzglDto);
		return result != 0;
	}

	/**
	 * 根据工作ID查询附件信息
	 */
	@Override
	public List<FjcfbDto> selectFjcfbDtoByGzid(String gzid) {
		// TODO Auto-generated method stub
		List<FjcfbDto> fjcfbDtos= dao.selectFjcfbDtoByGzid(gzid);
		for (FjcfbDto fjcfbDto : fjcfbDtos) {
			String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
			fjcfbDto.setWjmhz(wjmhz);
		}
		return fjcfbDtos;
	}

	/**
	 * 根据ids查询任务信息
	 */
	@Override
	public List<GzglDto> selectDtoByIds(List<String> ids) {
		// TODO Auto-generated method stub
		return dao.selectDtoByIds(ids);
	}

	/**
	 * 根据输入信息查询负责人
	 */
	@Override
	public List<GzglDto> selectTaskFzr(GzglDto gzglDto) {
		// TODO Auto-generated method stub
		return dao.selectTaskFzr(gzglDto);
	}

	/**
	 * 新增培训任务
	 */
	public boolean insertDtobyfzr(GzglDto gzglDto){
		return dao.insertDtobyfzr(gzglDto);
	}
	/**
	 * 根据ywid以及fzr查询任务
	 */
	public List<GzglDto> verification(GzglDto gzglDto){
		return dao.verification(gzglDto);
	}

	/**
	 * 多条添加任务
	 */
	public boolean insertList(List<GzglDto> list){
		return dao.insertList(list);
	}

	/**
	 * 确认转交
	 */
	public boolean updateConfirmPerson(GzglDto gzglDto,List<GzglDto> list,User user){
		List<GzglDto> gzglDtos=new ArrayList<>();
		for(GzglDto dto:list){
			GzglDto dto1 = new GzglDto();
			dto1.setClry(gzglDto.getQrry());
			dto1.setRwid(dto.getRwid());
			dto1.setXmjdid(dto.getXmjdid());
			gzglDtos.add(dto1);
			if(null != user && StringUtil.isNotBlank(user.getZsxm())){
				GzglDto gzglDto_t=new GzglDto();
				gzglDto_t.setLyid(StringUtil.generateUUID());
				gzglDto_t.setRwid(dto.getRwid());
				gzglDto_t.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
				gzglDto_t.setLyxx("将任务转交给 "+user.getZsxm()+" 由其接替确认");
				gzglDto_t.setLrry(gzglDto.getXgry());
				dao.insertRwly(gzglDto_t);
			}
		}
		dao.updateRwrqDtos(gzglDtos);
		return dao.updateQrry(gzglDto);
	}
	/**
	 * 根据业务ID删除工作任务
	 */
	public boolean deleteByYwids(GzglDto gzglDto){
		return dao.deleteByYwids(gzglDto);
	}

	@Override
	public List<GzglDto> getOverdueTasks(GzglDto gzglDto) {
		return dao.getOverdueTasks(gzglDto);
	}

	@Override
	public boolean insertGzgl(GzglDto gzglDto) {
		return dao.insertGzgl(gzglDto);
	}
	/**
	 * 查询相应的任务日期
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public GzglDto getRwrq(GzglDto gzglDto,String ryid){
	    GzglDto gzglDto_t=new GzglDto();
        GzglDto rwrq = dao.getRwrq(gzglDto);
        if(rwrq!=null){
            gzglDto_t=rwrq;
        }else{
            gzglDto_t.setRqid(StringUtil.generateUUID());
            gzglDto_t.setRwid(gzglDto.getRwid());
            gzglDto_t.setXmjdid(gzglDto.getXmjdid());
            gzglDto_t.setLrry(ryid);
            gzglDto_t.setJhksrq(gzglDto.getJhksrq());
            gzglDto_t.setJhjsrq(gzglDto.getJhjsrq());
            gzglDto_t.setSjksrq(gzglDto.getSjksrq());
            gzglDto_t.setSjjsrq(gzglDto.getSjjsrq());
            gzglDto_t.setJzrq(gzglDto.getJzrq());
            dao.insertRwrq(gzglDto_t);
        }
        return gzglDto_t;
    }

	/**
	 * 查询任务履历
	 */
	public List<GzglDto> getRwlyDtos(GzglDto gzglDto){
		return dao.getRwlyDtos(gzglDto);
	}

	/**
	 * 查询任务阶段日期
	 */
	public List<GzglDto> getRwrqDtos(GzglDto gzglDto){
		return dao.getRwrqDtos(gzglDto);
	}

	public boolean modRwrqByRwidAndXmjdid(GzglDto gzglDto){
		return dao.modRwrqByRwidAndXmjdid(gzglDto);
	}

	public GzglDto getXmjdxx(String xmjdid){
		return dao.getXmjdxx(xmjdid);
	}
	@Override
	public GzglDto getVerificationDto(GzglDto gzglDto) {
		return dao.getVerificationDto(gzglDto);
	}

	@Override
	public GzglDto getGzInfoByFzrAndYwid(GzglDto gzglDto) {
		return dao.getGzInfoByFzrAndYwid(gzglDto);
	}

	@Override
	public GzglDto getGzDtoByGzid(String gzid) {
		return dao.getGzDtoByGzid(gzid);
	}

	/**
	 * 任务转交保存
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean taskcareSaveTask(GzglDto gzglDto,HttpServletRequest req) throws BusinessException{
		if (StatusEnum.CHECK_SUBMIT.getCode().equals(gzglDto.getZt())){
			String[] gzids = gzglDto.getGzid().split(",");
			String[] qwwcsjs = gzglDto.getQwwcsj().split(",");
//			String[] bzs = gzglDto.getBz().split(",");
			List<GzglDto> gzglDtoList = new ArrayList<>();
			String xm="";
			String ddid="";
			//String yhid="";
			String yhm="";
			if(StringUtil.isNotBlank(gzglDto.getFzr())){
				User t_user=new User();
				t_user.setYhid(gzglDto.getFzr());
				User userInfoById = commonService.getUserInfoById(t_user);
				if(userInfoById!=null){
					xm=userInfoById.getZsxm();
					ddid=userInfoById.getDdid();
					//yhid=userInfoById.getYhid();
					yhm=userInfoById.getYhm();
				}
			}
			for (int i = 0; i < gzids.length; i++) {
				GzglDto dto = new GzglDto();
				dto.setGzid(gzids[i]);
				dto.setXgry(gzglDto.getXgry());
				boolean insert = gzlcglService.insertBygzglDto(dto);
				if(!insert)
					return false;
				dto.setQrry(gzglDto.getFzr());
				dto.setQwwcsj(qwwcsjs[i]);
//				dto.setBz(bzs.length==0?"":bzs[i]);
				//发送钉钉消息
//				String token = talkUtil.getToken();
				GzglDto t_gzglDto = dao.getDtoById(dto.getGzid());
				if(StringUtil.isNotBlank(ddid)){
					talkUtil.sendWorkMessage(yhm, ddid, xxglService.getMsg("ICOMM_SH00009"),xxglService.getMsg("ICOMM_SH00001",t_gzglDto.getZsxm(),t_gzglDto.getYwmc() ,t_gzglDto.getRwmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
				}
				gzglDtoList.add(dto);
				if(StringUtil.isNotBlank(t_gzglDto.getRwid())){
					GzglDto dto1 = new GzglDto();
					dto1.setClry(gzglDto.getFzr());
					dto1.setRwid(t_gzglDto.getRwid());
					dto1.setXmjdid(t_gzglDto.getXmjdid());
					dao.modRwrqByRwidAndXmjdid(dto1);
					if(StringUtil.isNotBlank(xm)){
						GzglDto gzglDto_t=new GzglDto();
						gzglDto_t.setLyid(StringUtil.generateUUID());
						gzglDto_t.setRwid(t_gzglDto.getRwid());
						gzglDto_t.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
						gzglDto_t.setLyxx("将任务转交给 "+xm+" 由其接替确认");
						gzglDto_t.setLrry(gzglDto.getXgry());
						dao.insertRwly(gzglDto_t);
					}
				}
			}
			if (!gzglDtoList.isEmpty()){
				return dao.updateGzglDtos(gzglDtoList);
			}

		}else{
			// TODO Auto-generated method stub
			//修改工作任务表信息
			String[] fzrs=gzglDto.getFzr().split(",");
			if(fzrs.length > 0){
				String[] gzids = gzglDto.getGzid().split(",");
				String[] qwwcsjs = gzglDto.getQwwcsj().split(",");
//				String[] bzs = gzglDto.getBz().split(",");
				if(fzrs.length==1){
					/* 负责人等于1个人的时候 ，直接修改*/
					GzglDto jg_gzglDto = dao.getJgidByFzr(gzglDto.getFzr());
					List<GzglDto> gzglDtos = new ArrayList<>();
					for (int i = 0; i < gzids.length; i++) {
						GzglDto t_gzglDto = new GzglDto();
						t_gzglDto.setGzid(gzids[i]);
						if(jg_gzglDto != null){
							t_gzglDto.setJgid(jg_gzglDto.getJgid());
						}
						t_gzglDto.setQwwcsj(qwwcsjs[i]);
						t_gzglDto.setFzr(gzglDto.getFzr());
						t_gzglDto.setXgry(gzglDto.getXgry());
//						t_gzglDto.setBz(bzs.length==0?"":bzs[i]);
						gzglDtos.add(t_gzglDto);
					}
					dao.updateByGzglDtoList(gzglDtos);
				}else {
					//第一步 修改第一负责人
					GzglDto jg_gzglDto = dao.getJgidByFzr(gzglDto.getFzr());
					List<GzglDto> gzglDtos = new ArrayList<>();
					for (int i = 0; i < gzids.length; i++) {
						GzglDto t_gzglDto = new GzglDto();
						t_gzglDto.setGzid(gzids[i]);
						if(jg_gzglDto != null){
							t_gzglDto.setJgid(jg_gzglDto.getJgid());
						}
						t_gzglDto.setQwwcsj(qwwcsjs[i]);
						t_gzglDto.setFzr(fzrs[0]);
						t_gzglDto.setXgry(gzglDto.getXgry());
//						t_gzglDto.setBz(bzs.length==0?"":bzs[i]);
						gzglDtos.add(t_gzglDto);
					}
					dao.updateByGzglDtoList(gzglDtos);
					//添加第一个后边的负责人
					for (int f = 0; f <gzids.length; f++){
						for (int y = 1; y < fzrs.length; y++){
							GzglDto t_gzglDtos=dao.selectDtoBygzid(gzids[f]);
							t_gzglDtos.setGzid(StringUtil.generateUUID());
							if(jg_gzglDto != null){
								t_gzglDtos.setJgid(jg_gzglDto.getJgid());
							}
							t_gzglDtos.setQwwcsj(qwwcsjs[f]);
							t_gzglDtos.setFzr(fzrs[y]);
							t_gzglDtos.setXgry(gzglDto.getXgry());
//							t_gzglDtos.setBz(bzs.length==0?"":bzs[f]);
							dao.insertDtobyfzr(t_gzglDtos);
						}
					}

				}
				User t_user=new User();
				t_user.setIds(gzglDto.getFzr());
				List<User> listByIds = commonService.getListByIds(t_user);
				StringBuilder xm= new StringBuilder();
				for(User dto:listByIds){
					xm.append(",").append(dto.getZsxm());
				}
				if(StringUtil.isNotBlank(xm.toString())){
					xm = new StringBuilder(xm.substring(1));
				}
				List<String> rwids=new ArrayList<>();
				List<GzglDto> gzglDtos=new ArrayList<>();
				for (String gzid : gzids) {
					GzglDto t_gzglDto = dao.getDtoById(gzid);
					gzglDto.setGzlx(t_gzglDto.getGzlx());
					gzglDto.setYwid(t_gzglDto.getYwid());
					//处理任务转交时的扩展信息
					boolean result = dealTaskCareExtend(gzglDto, req);
					if (!result)
						return false;
					//发送钉钉消息
					try {
//						String token = talkUtil.getToken();
						String ICOMM_SH00008 = xxglService.getMsg("ICOMM_SH00008");
						//String ICOMM_SH00010 = xxglService.getMsg("ICOMM_SH00010");
						String ICOMM_SH00081 = xxglService.getMsg("ICOMM_SH00081");
						//DBEncrypt p = new DBEncrypt();
						for (String fzr : fzrs) {
							GzglDto d_gzglDto = dao.getXtyhByYhid(fzr);
							//组装访问路径  &--> %26        =--> %3D          ?--> %3F
							//String internalbtn = applicationurl + urlPrefix + "/common/view/displayView?view_url=/ws/projectdd/viewProjectTaskdd%3Frwid%3D"+gzglDto.getYwid();
							String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/tasknotification/tasktransfer/tasktransfer&applicationurl=" + applicationurl + "&urlPrefix=" + urlPrefix + "&rwlx=xm" + "&rwid=" + gzglDto.getYwid(),StandardCharsets.UTF_8);

							//访问链接
							List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
							OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
							btnJsonList.setTitle("小程序");
							btnJsonList.setActionUrl(internalbtn);
							btnJsonLists.add(btnJsonList);
							//发送钉钉消息
							talkUtil.sendCardMessage(d_gzglDto.getYhm(),
									d_gzglDto.getDdid(),
									ICOMM_SH00008,
									StringUtil.replaceMsg(ICOMM_SH00081,
											gzglDto.getTszmc(),
											t_gzglDto.getRwmc(),
											t_gzglDto.getYwmc(),
											t_gzglDto.getFqwwcsj(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss"),
											""),
									btnJsonLists,
									"1");

						}
					} catch (Exception e) {
						logger.info(e.getMessage());
					}
					if (StringUtil.isNotBlank(t_gzglDto.getRwid())) {
						if (StringUtil.isNotBlank(xm.toString())) {
							GzglDto gzglDto_t = new GzglDto();
							gzglDto_t.setLyid(StringUtil.generateUUID());
							gzglDto_t.setRwid(t_gzglDto.getRwid());
							gzglDto_t.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
							gzglDto_t.setLyxx("将任务转交给 " + xm + " 由其接替完成");
							gzglDto_t.setLrry(gzglDto.getXgry());
							dao.insertRwly(gzglDto_t);
						}
						rwids.add(t_gzglDto.getRwid());
						GzglDto gzglDto_t = new GzglDto();
						gzglDto_t.setRwid(t_gzglDto.getRwid());
						gzglDto_t.setXmjdid(t_gzglDto.getXmjdid());
						gzglDto_t.setJhksrq(DateTimeUtil.getCurrDateStr());
						gzglDto_t.setSjksrq(DateTimeUtil.getCurrDateStr());
						gzglDto_t.setJhjsrq(t_gzglDto.getFqwwcsj());
						gzglDto_t.setJzrq(t_gzglDto.getFqwwcsj());
						gzglDto_t.setClry(gzglDto.getFzr());
						gzglDtos.add(gzglDto_t);
					}
				}

				if(!gzglDtos.isEmpty()){
					dao.updateRwrqDtos(gzglDtos);
				}
			}
		}

		return true;
	}

	/**
	 * 领取任务
	 * @param gzglDto
	 * @param isInProgress
	 * @param user
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean claimTask(GzglDto gzglDto,boolean isInProgress,User user,HttpServletRequest req)throws BusinessException{
		if (isInProgress){
			if (StatusEnum.CHECK_SUBMIT.getCode().equals(gzglDto.getZt())){
				gzglDto.setXgry(user.getYhid());
				boolean insert = gzlcglService.insertBygzglDto(gzglDto);
				if(!insert)
					return false;
				gzglDto.setQrry(user.getYhid());
				gzglDto.setClry(user.getYhid());
				dao.modRwrqByRwidAndXmjdid(gzglDto);

				GzglDto gzglDto_rwly=new GzglDto();
				gzglDto_rwly.setLyid(StringUtil.generateUUID());
				gzglDto_rwly.setRwid(gzglDto.getRwid());
				gzglDto_rwly.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
				gzglDto_rwly.setLyxx("领取了任务， 由其接替确认");
				gzglDto_rwly.setLrry(user.getYhid());
				dao.insertRwly(gzglDto_rwly);

				List<GzglDto> gzglDtoList = new ArrayList<>();
				gzglDtoList.add(gzglDto);
				return dao.updateGzglDtos(gzglDtoList);
			} else{
				//修改工作任务表信息
				List<GzglDto> gzglDtos = new ArrayList<>();
				gzglDto.setJgid(user.getJgid());
				gzglDto.setFzr(user.getYhid());
				gzglDto.setXgry(user.getYhid());
				gzglDto.setClry(user.getYhid());
				gzglDtos.add(gzglDto);
				dao.updateByGzglDtoList(gzglDtos);
				//处理任务转交时的扩展信息
				boolean result = dealClaimTaskCareExtend(gzglDto, req);
				if (!result)
					return false;
				if (StringUtil.isNotBlank(gzglDto.getRwid())) {
					GzglDto gzglDto_rwly = new GzglDto();
					gzglDto_rwly.setLyid(StringUtil.generateUUID());
					gzglDto_rwly.setRwid(gzglDto.getRwid());
					gzglDto_rwly.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
					gzglDto_rwly.setLyxx("领取了任务 由其接替完成");
					gzglDto_rwly.setLrry(user.getYhid());
					dao.insertRwly(gzglDto_rwly);
				}
				if(!gzglDtos.isEmpty()){
					dao.updateRwrqDtos(gzglDtos);
				}
			}
		}else {
			List<GzglDto> gzglDtos=new ArrayList<>();
			gzglDto.setClry(user.getYhid());
			gzglDto.setQrry(user.getYhid());
			gzglDtos.add(gzglDto);
			if(null != user && StringUtil.isNotBlank(user.getZsxm())){
				GzglDto gzglDto_rwly=new GzglDto();
				gzglDto_rwly.setLyid(StringUtil.generateUUID());
				gzglDto_rwly.setRwid(gzglDto.getRwid());
				gzglDto_rwly.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
				gzglDto_rwly.setLyxx("将任务转交给 "+user.getZsxm()+" 由其接替确认");
				gzglDto_rwly.setLrry(user.getYhid());
				dao.insertRwly(gzglDto_rwly);
			}
			dao.updateRwrqDtos(gzglDtos);
			return dao.updateQrry(gzglDto);
		}
		return true;
	}

	/**
	 * 查询负责人列表
	 */
	@Override
	public List<GzglDto> getPagedFzrList(GzglDto gzglDto) {
		// TODO Auto-generated method stub
		return dao.getPagedFzrList(gzglDto);
	}


	@Override
	public List<GzglDto> getPagedKhList(GzglDto gzglDto) {
		// TODO Auto-generated method stub
		return dao.getPagedKhList(gzglDto);
	}
	/**
	 * 查询任务确认列表
	 */
	@Override
	public List<GzglDto> getPagedConfirmList(GzglDto gzglDto) {
		// TODO Auto-generated method stub
		return dao.getPagedConfirmList(gzglDto);
	}

	/**
	 * 任务确认保存
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean confirmSaveTask(GzglDto gzglDto,HttpServletRequest req) {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(gzglDto.getRwid())){
			GzglDto gzglDto_jd=new GzglDto();
			gzglDto_jd.setLyid(StringUtil.generateUUID());
			gzglDto_jd.setRwid(gzglDto.getRwid());
			gzglDto_jd.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
			GzglDto xmjdxx = dao.getXmjdxx(gzglDto.getXmjdid());
			gzglDto_jd.setLyxx("将任务阶段更新为 "+xmjdxx.getJdmc());
			gzglDto_jd.setLrry(gzglDto.getXgry());
			dao.insertRwly(gzglDto_jd);
			GzglDto gzglDto_t=new GzglDto();
			gzglDto_t.setXgry(gzglDto.getXgry());
			gzglDto_t.setRwid(gzglDto.getRwid());
			gzglDto_t.setXmjdid(gzglDto.getOld_xmjdid());
			gzglDto_t.setJhksrq(gzglDto.getJhksrq());
			gzglDto_t.setJhjsrq(gzglDto.getJhjsrq());
			GzglDto rwrq = getRwrq(gzglDto_t,gzglDto.getXgry());
			if(rwrq!=null){
				if(StringUtil.isBlank(rwrq.getSjjsrq())){
					gzglDto_t.setSjjsrq(DateTimeUtil.getCurrDateStr());
				}
			}
			dao.modRwrqByRwidAndXmjdid(gzglDto_t);
		}
		//判断是否确认通过
		if("1".equals(gzglDto.getSftg())){
			//通过，判断是否有下阶段确认人
			if(StringUtil.isBlank(gzglDto.getQrry())){
				//没有，新增工作流程状态，修改原表数据
				gzglDto.setZt(StatusEnum.CHECK_PASS.getCode());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				gzglDto.setGzjssj(simpleDateFormat.format(date));
				gzglDto.setDqjd("100");
				boolean isSuccess = gzlcglService.insertGzlcxxByQrrw(gzglDto);
				if(!isSuccess)
					return false;
				if(StringUtil.isNotBlank(gzglDto.getZf())){
					String[] split = gzglDto.getYwdz().split("=");
					gzglDto.setGrksid(split[1]);
					gzglDto.setTgbj("1");
					dao.updateZf(gzglDto);
				}
				boolean result = dao.updateZt(gzglDto);
				if(!result)
					return false;
				//发送钉钉消息
//				String token = talkUtil.getToken();
				GzglDto t_gzglDto = dao.getDtoById(gzglDto.getGzid());
				gzglDto.setGzlx(t_gzglDto.getGzlx());
				//处理任务转交时的扩展信息
				result = dealTaskConfirmExtend(gzglDto,req);
				//通过，没有下一步确认人员并且更新没有问题
				if(result){
					if(!StringUtil.isBlank(gzglDto.getDdid())){
						talkUtil.sendWorkMessage(t_gzglDto.getQrryyhm(), t_gzglDto.getDdid(), xxglService.getMsg("ICOMM_SH00031"),xxglService.getMsg("ICOMM_SH00010",gzglDto.getTszmc(),t_gzglDto.getRwmc(),t_gzglDto.getYwmc(),t_gzglDto.getQwwcsj(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss"),gzglDto.getShyj()));
					}
				}
				if(StringUtil.isNotBlank(gzglDto.getRwid())){
					GzglDto gzglDto_rw=new GzglDto();
					gzglDto_rw.setLyid(StringUtil.generateUUID());
					gzglDto_rw.setRwid(gzglDto.getRwid());
					gzglDto_rw.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
					gzglDto_rw.setLyxx("进行了确认操作，结果为 通过 任务结束");
					gzglDto_rw.setLrry(gzglDto.getXgry());
					dao.insertRwly(gzglDto_rw);
					GzglDto gzglDto_t=new GzglDto();
					gzglDto_t.setXgry(gzglDto.getXgry());
					gzglDto_t.setRwid(gzglDto.getRwid());
					gzglDto_t.setXmjdid(gzglDto.getXmjdid());
					gzglDto_t.setJzrq(null);
					GzglDto rwrq = getRwrq(gzglDto_t,gzglDto.getXgry());
					if(rwrq!=null){
						if(StringUtil.isBlank(rwrq.getSjksrq())){
							gzglDto_t.setSjksrq(DateTimeUtil.getCurrDateStr());
						}
						if(StringUtil.isBlank(rwrq.getSjjsrq())){
							gzglDto_t.setSjjsrq(DateTimeUtil.getCurrDateStr());
						}
					}
					dao.modRwrqByRwidAndXmjdid(gzglDto_t);
				}
			}else{
				//有，修改原表数据，新增工作流程
				boolean isSuccess = gzlcglService.insertGzlcxxByQrrw(gzglDto);
				if(!isSuccess)
					return false;
				boolean result = dao.updateQrry(gzglDto);
				if(!result)
					return false;
				//发送钉钉消息
//				String token = talkUtil.getToken();
				GzglDto t_gzglDto = dao.getDtoById(gzglDto.getGzid());
				gzglDto.setGzlx(t_gzglDto.getGzlx());
				//处理任务转交时的扩展信息
				dealTaskConfirmExtend(gzglDto,req);
				if(!StringUtil.isBlank(gzglDto.getDdid())){
					talkUtil.sendWorkMessage(gzglDto.getQrryyhm(), gzglDto.getDdid(), xxglService.getMsg("ICOMM_SH00009"),xxglService.getMsg("ICOMM_SH00010",gzglDto.getTszmc(),t_gzglDto.getRwmc(),t_gzglDto.getYwmc(),t_gzglDto.getQwwcsj(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss"),gzglDto.getShyj()));
				}
				if(StringUtil.isNotBlank(gzglDto.getRwid())){
					GzglDto gzglDto_rw=new GzglDto();
					gzglDto_rw.setLyid(StringUtil.generateUUID());
					gzglDto_rw.setRwid(gzglDto.getRwid());
					gzglDto_rw.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
					User user=new User();
					user.setYhid(gzglDto.getQrry());
					User userInfoById = commonService.getUserInfoById(user);
					gzglDto_rw.setLyxx("进行了确认操作，结果为 通过 并转交给 "+userInfoById.getZsxm());
					gzglDto_rw.setLrry(gzglDto.getXgry());
					dao.insertRwly(gzglDto_rw);
					GzglDto gzglDto_t=new GzglDto();
					gzglDto_t.setXgry(gzglDto.getXgry());
					gzglDto_t.setRwid(gzglDto.getRwid());
					gzglDto_t.setXmjdid(gzglDto.getXmjdid());
					gzglDto_t.setJzrq(gzglDto.getJzrq());
					gzglDto_t.setClry(gzglDto.getQrry());
					GzglDto rwrq = getRwrq(gzglDto_t,gzglDto.getXgry());
					if(rwrq!=null){
						if(StringUtil.isBlank(rwrq.getSjksrq())){
							gzglDto_t.setSjksrq(DateTimeUtil.getCurrDateStr());
						}
					}
					dao.modRwrqByRwidAndXmjdid(gzglDto_t);
				}

			}
		}else{
			if(StringUtil.isNotBlank(gzglDto.getRwid())){
				GzglDto gzglDto_t=new GzglDto();
				gzglDto_t.setXgry(gzglDto.getXgry());
				gzglDto_t.setRwid(gzglDto.getRwid());
				gzglDto_t.setXmjdid(gzglDto.getXmjdid());
				gzglDto_t.setJzrq(gzglDto.getJzrq());
				gzglDto_t.setClry(gzglDto.getQrry());
				dao.modRwrqByRwidAndXmjdid(gzglDto_t);
			}
			//不通过,必须有退回人，判断退回人是否是申请人
			if(gzglDto.getFzr().equals(gzglDto.getQrry())){
				//是，新增工作流程状态，修改原表状态
				boolean isSuccess = gzlcglService.insertGzlcxxByQrrw(gzglDto);
				if(!isSuccess)
					return false;
				gzglDto.setZt(StatusEnum.CHECK_NO.getCode());
				boolean result = dao.updateInitZt(gzglDto);
				if(!result)
					return false;
				//发送钉钉消息
//				String token = talkUtil.getToken();
				GzglDto t_gzglDto = dao.getDtoById(gzglDto.getGzid());
				t_gzglDto.setXmjdid(gzglDto.getXmjdid());
                dao.updateXmjdByRwid(t_gzglDto);
				if(!StringUtil.isBlank(gzglDto.getDdid())){
					talkUtil.sendWorkMessage(gzglDto.getQrryyhm(), gzglDto.getDdid(), xxglService.getMsg("ICOMM_SH00015"),xxglService.getMsg("ICOMM_SH00010",gzglDto.getTszmc(),t_gzglDto.getRwmc(),t_gzglDto.getYwmc(),t_gzglDto.getFqwwcsj(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss"),gzglDto.getShyj()));
				}
				if(StringUtil.isNotBlank(gzglDto.getRwid())){
					GzglDto gzglDto_rw=new GzglDto();
					gzglDto_rw.setLyid(StringUtil.generateUUID());
					gzglDto_rw.setRwid(gzglDto.getRwid());
					gzglDto_rw.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
					User user=new User();
					user.setYhid(gzglDto.getQrry());
					User userInfoById = commonService.getUserInfoById(user);
					gzglDto_rw.setLyxx("进行了确认操作，结果为 不通过 并驳回给 "+userInfoById.getZsxm());
					gzglDto_rw.setLrry(gzglDto.getXgry());
					dao.insertRwly(gzglDto_rw);
				}
			}else{
				//不是，修改原表数据，新增工作流程
				boolean isSuccess = gzlcglService.insertGzlcxxByQrrw(gzglDto);
				if(!isSuccess)
					return false;
				boolean result = dao.updateQrry(gzglDto);
				if(!result)
					return false;
				//发送钉钉消息
//				String token = talkUtil.getToken();
				GzglDto t_gzglDto = dao.getDtoById(gzglDto.getGzid());
                t_gzglDto.setXmjdid(gzglDto.getXmjdid());
                dao.updateXmjdByRwid(t_gzglDto);
				if(!StringUtil.isBlank(gzglDto.getDdid())){
					talkUtil.sendWorkMessage(gzglDto.getQrryyhm(), gzglDto.getDdid(), xxglService.getMsg("ICOMM_SH00015"),xxglService.getMsg("ICOMM_SH00010",gzglDto.getTszmc(),t_gzglDto.getRwmc(),t_gzglDto.getYwmc(),t_gzglDto.getFqwwcsj(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss"),gzglDto.getShyj()));
				}
				if(StringUtil.isNotBlank(gzglDto.getRwid())){
					GzglDto gzglDto_rw=new GzglDto();
					gzglDto_rw.setLyid(StringUtil.generateUUID());
					gzglDto_rw.setRwid(gzglDto.getRwid());
					gzglDto_rw.setLylx(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode());
					User user=new User();
					user.setYhid(gzglDto.getQrry());
					User userInfoById = commonService.getUserInfoById(user);
					gzglDto_rw.setLyxx("进行了确认操作，结果为 不通过 并驳回给 "+userInfoById.getZsxm());
					gzglDto_rw.setLrry(gzglDto.getXgry());
					dao.insertRwly(gzglDto_rw);
				}
			}
		}
		return true;
	}
	
	/**
	 * 根据业务ID删除工作任务
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteByYwid(List<String> ywids) {
		// TODO Auto-generated method stub
		return dao.deleteByYwid(ywids);
	}
	
	/**
	 * 处理提交工作任务的扩展信息
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	boolean dealTaskExtend(GzglDto gzglDto, HttpServletRequest request) {
		//确认工作任务有没有设置工作类型，如果有则查询回调信息
		Map<String,Object> ResultMap = getTaskService(gzglDto,request);
		if(ResultMap.get("service") == null)
			return true;
		/* ResultMap.put("qrry",gzglDto.getQrry()); */
		ITaskService taskService = (ITaskService)ResultMap.get("service");
		ResultMap.put("xgry", gzglDto.getXgry());
		try {
			return taskService.updateByTask(ResultMap);
		}catch(Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		
	}
	
	/**
	 * 处理任务转交时的扩展信息
	 */
	private boolean dealTaskCareExtend(GzglDto gzglDto,HttpServletRequest request) throws BusinessException {
		//确认工作任务有没有设置工作类型，如果有则查询回调信息
		Map<String,Object> ResultMap = getTaskService(gzglDto,request);
		if(ResultMap.get("service") == null)
			return true;
		
		if(StringUtil.isNotBlank(gzglDto.getFzr()))
			ResultMap.put("fzrs", gzglDto.getFzr().split(","));
		else
			ResultMap.put("fzrs", null);
		
		ResultMap.put("ywid", gzglDto.getYwid());
		
		ITaskService taskService = (ITaskService)ResultMap.get("service");
		return taskService.updateByTaskTransmit(ResultMap);
	}
	
	/**
	 * 处理任务转交确认时的扩展信息
	 */
	private boolean dealTaskConfirmExtend(GzglDto gzglDto,HttpServletRequest request) {
		//确认工作任务有没有设置工作类型，如果有则查询回调信息
		Map<String,Object> ResultMap = getTaskService(gzglDto,request);
		if(ResultMap.get("service") == null)
			return true;
		ResultMap.put("ywid", gzglDto.getYwid());
		ResultMap.put("xgry", gzglDto.getXgry());
		if (StringUtil.isNotBlank(gzglDto.getZt())){
			ResultMap.put("zt", gzglDto.getZt());
		}
		ITaskService taskService = (ITaskService)ResultMap.get("service");
		
		try {
			return taskService.updateByTaskConfirm(ResultMap);
		}catch(Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		
	}


	/**
	 * 处理领取转交任务的扩展信息
	 */
	private boolean dealClaimTaskCareExtend(GzglDto gzglDto,HttpServletRequest request) throws BusinessException {
		//确认工作任务有没有设置工作类型，如果有则查询回调信息
		Map<String,Object> ResultMap = getTaskService(gzglDto,request);
		if(ResultMap.get("service") == null)
			return true;

		if(StringUtil.isNotBlank(gzglDto.getFzr()))
			ResultMap.put("fzrs", gzglDto.getFzr().split(","));
		else
			ResultMap.put("fzrs", null);

		ResultMap.put("ywid", gzglDto.getYwid());

		ITaskService taskService = (ITaskService)ResultMap.get("service");
		return taskService.updateByClaimTaskTransmit(ResultMap);
	}

	
	/**
	 * 获取回调信息（公用方法）
	 */
	private Map<String,Object> getTaskService(GzglDto gzglDto,HttpServletRequest request) {

		Map<String,Object> params = new HashMap<>();
		
		//确认工作任务有没有设置工作类型，如果有则查询回调信息
		if(StringUtil.isNotBlank(gzglDto.getGzlx())) {
			try {
				Map<String, String> taskConfig = redisXmlConfig.getTaskConfig(gzglDto.getGzlx());
				
				//回调信息如果存在，则
				if(taskConfig!=null) {
					//回调业务状态Service
					String serviceName = taskConfig.get("class-service");
					ITaskService taskService = (ITaskService)ServiceFactory.getService(serviceName);
					
					params.put("service", taskService);
					
					if(taskService!=null) {
						String dtoName = taskConfig.get("class-entity");
						
						if(StringUtil.isNotBlank(dtoName)) {
							Class<?> dtoClass = Class.forName(dtoName);//获取实体类class
							//设置查询条件信息
							Object dtoInstance = dtoClass.getDeclaredConstructor().newInstance();
							
							BeanInfo beanInfo = Introspector.getBeanInfo(dtoInstance.getClass());
							PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
							
							for (PropertyDescriptor property : propertyDescriptors) {
								String key = property.getName();
								String value = request.getParameter(key);
								if (value != null){
									// 得到property对应的setter方法
									Method setter = property.getWriteMethod();
									switch (setter.getParameterTypes()[0].getName()) {
										case "java.lang.String":
											setter.invoke(dtoInstance, value);
											break;
										case "java.util.List": {
											String s_value = value.replace("[", "").replace("]", "").replace(" ", "");
											if (StringUtil.isBlank(s_value))
												continue;
											String[] ss_value = s_value.split(",");
											List<String> list = Arrays.asList(ss_value);
											setter.invoke(dtoInstance, list);
											break;
										}
										case "[Ljava.lang.String;": {
											String s_value = value.replace("[", "").replace("]", "");
											if (StringUtil.isBlank(s_value))
												continue;
											String[] ss_value = s_value.split(",");
											setter.invoke(dtoInstance, new Object[]{ss_value});
											break;
										}
									}
								}
							}
							params.put("dto", dtoInstance);
						}
						
						params.put("request", request);
						params.put("qrry", gzglDto.getQrry());
						params.put("shyj", gzglDto.getShyj());
						params.put("sftg", gzglDto.getSftg());
						params.put("yhid", gzglDto.getYhid());
					}
					
				}
			}catch(Exception e) {
				logger.error(e.getMessage());
			}
		}
		return params;
	}

	/**
	 * 任务批量确认保存
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean batchconfirmSaveTask(GzglDto gzglDto) {
		// TODO Auto-generated method stub
		//判断是否确认通过
//		String token = talkUtil.getToken();
		if("1".equals(gzglDto.getSftg())){
			gzglDto.setZt(StatusEnum.CHECK_PASS.getCode());
			gzglDto.setDqjd("100");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			gzglDto.setGzjssj(simpleDateFormat.format(date));
			boolean isSuccess = gzlcglService.insertGzlcxxByQrrws(gzglDto);
			if(!isSuccess)
				return false;
			boolean result = dao.updateZts(gzglDto);
			if(!result)
				return false;
			//发送钉钉消息
			List<String> ids = gzglDto.getIds();
			if(ids != null && !ids.isEmpty()){
				String ICOMM_SH00009 = xxglService.getMsg("ICOMM_SH00009");
				for (String id : ids) {
					GzglDto t_gzglDto = dao.getDtoById(id);
					if (!StringUtil.isBlank(t_gzglDto.getDdid())) {
						talkUtil.sendWorkMessage(t_gzglDto.getQrry(), t_gzglDto.getDdid(), ICOMM_SH00009, xxglService.getMsg("ICOMM_SH00010", t_gzglDto.getZsxm(), t_gzglDto.getRwmc(), t_gzglDto.getYwmc(), t_gzglDto.getQwwcsj(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
					}
				}
			}
		}else{
			boolean isSuccess = gzlcglService.insertGzlcxxByQrrws(gzglDto);
			if(!isSuccess)
				return false;
			gzglDto.setZt(StatusEnum.CHECK_NO.getCode());
			boolean result = dao.updateInitZts(gzglDto);
			if(!result)
				return false;
			//发送钉钉消息
			List<String> ids = gzglDto.getIds();
			if(ids != null && !ids.isEmpty()){
				String ICOMM_SH00015 = xxglService.getMsg("ICOMM_SH00015");
				for (String id : ids) {
					GzglDto t_gzglDto = dao.getDtoById(id);
					if (!StringUtil.isBlank(t_gzglDto.getDdid())) {
						talkUtil.sendWorkMessage(t_gzglDto.getQrry(), t_gzglDto.getDdid(), ICOMM_SH00015, xxglService.getMsg("ICOMM_SH00010", t_gzglDto.getZsxm(), t_gzglDto.getRwmc(), t_gzglDto.getYwmc(), t_gzglDto.getFqwwcsj(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
					}
				}
			}
		}
		return true;
	}
	/*
	  查询任务到期时间
	  @param xmjdxxDto
	 * @return
	 */
//	public boolean timingTask(){
//
//		//查询出来状态为00，未完成的所有任务
//		List<GzglDto> gzglList=dao.selectGzglByzt();
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//		//推送到钉钉
//		if(gzglList.size()<0)
//			return true;
//
//		String comm002 = xxglService.getMsg("ICOMM_ZH00002");
//		String comm001 = xxglService.getMsg("ICOMM_ZH00001");
//		for (int i = 0; i <gzglList.size(); i++){
//			logger.error("timingTask: fzr "+gzglList.get(i).getFzr() + " gzid: "+gzglList.get(i).getGzid());
//			String token =talkUtil.getToken();
//			int yqsj=Integer.parseInt(gzglList.get(i).getYqsj());
//			if(yqsj<0) {
//				//逾期
//				if(gzglList.get(i).getDdid()!=null &&StringUtil.isNotBlank(gzglList.get(i).getDdid())){
//					talkUtil.sendWorkMessage("null",gzglList.get(i).getDdid(),null,StringUtil.replaceMsg(comm002,gzglList.get(i).getYwmc(),gzglList.get(i).getQwwcsj(),sdf.format(new Date())));
//				}
//			}else if(yqsj>=0&&yqsj<=2) {
//				//接近三天时间
//				String time=null;
//				if(yqsj==0) {
//					time="今天";
//				}else if(yqsj==1) {
//					time="明天";
//					}
//				else if(yqsj==2) {
//					time="后天";
//				}
//				if(gzglList.get(i).getDdid()!=null &&StringUtil.isNotBlank(gzglList.get(i).getDdid())){
//					talkUtil.sendWorkMessage("null",gzglList.get(i).getDdid(),null,
//							StringUtil.replaceMsg(comm001,gzglList.get(i).getYwmc(),gzglList.get(i).getQwwcsj(),time,sdf.format(new Date())));
//				}
//			}
//		}
//		//发送逾期任务
//		List<Map<String,String>> listMap=dao.getYqGzgl();
//		StringBuffer sb=new StringBuffer();
//		sb.setLength(0);
//		int count=0;
//		for (int i = 0; i < listMap.size(); i++){
//			if(i!=0)
//				sb.append(",");
//			count+=Integer.parseInt(String.valueOf(listMap.get(i).get("count")));
//			sb.append(listMap.get(i).get("rwmc"));
//			sb.append(String.valueOf(listMap.get(i).get("count"))+"条");
//
//		}
//		String nrxx=sb.toString();
//		JcsjDto jcsjDto = new JcsjDto();
//		jcsjDto.setJclb("DINGMESSAGETYPE");
//		jcsjDto.setCsdm("OVERDUE_TYPE");
//		List<DdxxglDto> ddxxList=ddxxDao.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
//		if(ddxxList.size()>0) {
//			for (int i = 0; i < ddxxList.size();i++){
//				String token =talkUtil.getToken();
//				if(ddxxList.get(i).getDdid()!=null &&StringUtil.isNotBlank(ddxxList.get(i).getDdid())) {
//					talkUtil.sendWorkMessage("null",ddxxList.get(i).getDdid(),null,xxglService.getMsg("ICOMM_ZH00007",count+"",nrxx,sdf.format(new Date())));
//				}
//			}
//		}
//		return true;
//	}

	/**
	 * 查询当前期望完成时间
	 */
	@Override
	public List<GzglDto> getGzglByfzr(GzglDto gzglDto){
		// TODO Auto-generated method stub
		return dao.getGzglByfzr(gzglDto);
	}
	
	/**
	 * 小程序分页查询用户列表
	 */
	public List<GzglDto> getMiniFzrList(GzglDto gzglDto){
		return dao.getMiniFzrList(gzglDto);
	}
	/**
	 * 定时查询未完成任务和未审核任务
	 */
	public void sendOverdueTasks() {
		GzglDto gzglDto=new GzglDto();
		List<GzglDto> gzglDtoList=getOverdueTasks(gzglDto);
		ShtxDto shtxDto=new ShtxDto();
		List<ShtxDto> shtxDtoList=shtxService.getAllAuditings(shtxDto);
//		String token = talkUtil.getToken();
		int zs=0;
		for (GzglDto gzglDto1:gzglDtoList) {
			StringBuilder str = new StringBuilder();
			int xh = 0;
			if (StringUtil.isNotBlank(gzglDto1.getZs())) {
				zs = Integer.parseInt(gzglDto1.getZs());
				String[] taskAll;
				if (gzglDto1.getString_agg() != null) {
					taskAll = gzglDto1.getString_agg().split(",");
				} else {
					continue;
				}
				for (String s : taskAll) {
					xh++;
					String[] task = s.split("@");
					for (int j = 0; j < task.length; j++) {
						if (j % 2 != 0) {
							str.append(task[j]).append("条\n").append("\n");
						} else
							str.append(xh).append(".").append(task[j]);
					}
				}
			}
			for (ShtxDto shtxDto1 : shtxDtoList)
				{
					String[] AllAuditings;
					if (shtxDto1.getString_agg() != null) {
						AllAuditings = shtxDto1.getString_agg().split(",");
					} else {
						continue;
					}
					if (shtxDto1.getDdid().equals(gzglDto1.getDdid())) {
						shtxDto1.setSfywwcdrw("1");
						if (StringUtil.isNotBlank(shtxDto1.getZs())) {
							zs += Integer.parseInt(shtxDto1.getZs());
							for (String allAuditing : AllAuditings) {
								xh++;
								String[] auditing = allAuditing.split("@");
								for (int j = 0; j < auditing.length; j++) {
									if (j % 2 != 0)
										str.append(auditing[j]).append("条\n").append("\n");
									else str.append(xh).append(".").append(auditing[j]);
								}
							}
						}
					}
				}
			String zs1 = String.valueOf((zs));
			talkUtil.sendWorkMessage(gzglDto1.getQrry(), gzglDto1.getDdid(), xxglService.getMsg("ICOMM_YQ00001", zs1), xxglService.getMsg("ICOMM_YQ00002", zs1, str.toString()));
		}

		for (ShtxDto shtxDto1:shtxDtoList){
			if (StringUtil.isNull(shtxDto1.getSfywwcdrw())) {
				if (StringUtil.isNotBlank(shtxDto1.getZs())) {
					StringBuilder str1 = new StringBuilder();
					int xh = 0;
					String[] AllAuditings;
					if (shtxDto1.getString_agg()!=null) {
						AllAuditings = shtxDto1.getString_agg().split(",");
					}else {
						break;
					}
					for (String allAuditing : AllAuditings) {
						xh++;
						String[] auditing = allAuditing.split("@");
						for (int j = 0; j < auditing.length; j++) {
							if (j % 2 != 0)
								str1.append(auditing[j]).append("条\n").append("\n");
							else str1.append(xh).append(".").append(auditing[j]);
						}
					}
					talkUtil.sendWorkMessage(shtxDto1.getYhm(), shtxDto1.getDdid(), xxglService.getMsg("ICOMM_YQ00001", shtxDto1.getZs()), xxglService.getMsg("ICOMM_YQ00002", shtxDto1.getZs(), str1.toString()));
				}
			}
		}
	}

	@Override
	public boolean updateGzglDtos(List<GzglDto> upGzglDtos) {
		return dao.updateGzglDtos(upGzglDtos);
	}

	/**
	 * 获取统计数据
	 */
	public Map<String,String> getStatistics(GzglDto gzglDto){
		return dao.getStatistics(gzglDto);
	}

	@Override
	public List<GzglDto> selectDistributedDtos(GzglDto gzglDto) {
		return dao.selectDistributedDtos(gzglDto);
	}

	@Override
	public List<GzglDto> selectTaskNames(GzglDto gzglDto) {
		return dao.selectTaskNames(gzglDto);
	}

	@Override
	public List<GzglDto> selectInstitution(GzglDto gzglDto) {
		return dao.selectInstitution(gzglDto);
	}
	/**
	 * 根据工作ID任务ids
	 */
	@Override
	public List<GzglDto> getRwidList(GzglDto gzglDto) {
		return dao.getRwidList(gzglDto);
	}

	@Override
	public GzglDto getYhssjgandjgxx(GzglDto gzglDto) {
		return dao.getYhssjgandjgxx(gzglDto);
	}
	/**
	 * 获取流水号
	 */
	public String getJlbhSerial(GzglDto gzglDto){
		return dao.getJlbhSerial(gzglDto);
	}

	@Override
	public boolean existCheck(String fieldName, String value) {
		return false;
	}

	@Override
	public boolean insertImportRec(BaseModel baseModel, User user,int rowindex,StringBuffer errorMessages) throws BusinessException {
		GzglDto gzglDto = (GzglDto) baseModel;
		User yh = commonService.getDtoByYhm(gzglDto.getYhm());
		if(yh!=null){
			gzglDto.setFzr(yh.getYhid());
		}
		List<JcsjDto> ssgsList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AFFILIATED_COMPANY.getCode());
		List<JcsjDto> pxfsList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINING_METHODS.getCode());
		if(ssgsList!=null&&!ssgsList.isEmpty()){
			for(JcsjDto dto:ssgsList){
				if(gzglDto.getSsgsmc().equals(dto.getCsmc())){
					gzglDto.setSsgs(dto.getCsid());
					break;
				}
			}
		}
		if(pxfsList!=null&&!pxfsList.isEmpty()){
			for(JcsjDto dto:pxfsList){
				if(gzglDto.getPxfsmc().equals(dto.getCsmc())){
					gzglDto.setPxfs(dto.getCsid());
					break;
				}
			}
		}
		if("通过".equals(gzglDto.getSftg())){
			gzglDto.setTgbj("1");
		}else{
			gzglDto.setTgbj("0");
		}
		if("是".equals(gzglDto.getSfnd())){
			gzglDto.setSfnd("1");
		}else{
			gzglDto.setSfnd("0");
		}
		gzglDto.setZt(StatusEnum.CHECK_PASS.getCode());
		gzglDto.setLrry(user.getYhid());
		gzglDto.setGzid(StringUtil.generateUUID());
		gzglDto.setGlbj("1");
        return dao.insertDtobyfzr(gzglDto);
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
	 * 获取培训信息
	 */
 	public List<GzglDto> getTrainInfo(GzglDto gzglDto){
		return dao.getTrainInfo(gzglDto);
	}
	
	public Map<String, Object> getHomePageTaskStatis(GzglDto gzglDto) {
		return dao.getHomePageTaskStatis(gzglDto);
	}
	
	/**
	 * 调用主站删除相应任务信息
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public boolean delete(GzglDto gzglDto,HttpServletRequest request){
		List<GzglDto> rwidList = getRwidList(gzglDto);
		if (!CollectionUtils.isEmpty(rwidList)){
			for (GzglDto dto : rwidList) {
				MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
				paramMap.add("rwid",dto.getRwid());
				paramMap.add("access_token",request.getParameter("access_token"));
				RestTemplate t_restTemplate = new RestTemplate();
				//logger.error(applicationurl + urlPrefix +"/experiment/project/pagedataSelectrw");
				String result = t_restTemplate.postForObject(applicationurl + urlPrefix +"/experiment/project/pagedataSelectrw", paramMap, String.class);
				JSONObject parse = (JSONObject) JSONObject.parse(result);
				if ("fail".equals(parse.getString("status"))){
					logger.error(StringUtil.isNotBlank(parse.getString("message"))?parse.getString("message"):" 任务调用pagedataSelectrw删除失败！");
					throw  new RuntimeException(StringUtil.isNotBlank(parse.getString("message"))?parse.getString("message"):"删除失败！");
				}
			}
		}
		return true;
	}

	@Override
	public List<GzglDto> getDtoTrainSignInPeo(GzglDto gzglDto) {
		return dao.getDtoTrainSignInPeo(gzglDto);
	}

	@Override
	public boolean deleteByTrainSignIn(GzglDto gzglDto) {
		return dao.deleteByTrainSignIn(gzglDto);
	}

	/**
	 * 获取未完成任务
	 * @param gzglDto
	 * @return
	 */
	public List<GzglDto> getConfirmList(GzglDto gzglDto){
		return dao.getConfirmList(gzglDto);
	}

	@Override
	public List<Map<String, Object>> getRemindTrainInfo(GzglDto gzglDto) {
		return dao.getRemindTrainInfo(gzglDto);
	}

	@Override
	public List<Map<String, Object>> getRemindTrainGroupPeo(GzglDto gzglDto) {
		return dao.getRemindTrainGroupPeo(gzglDto);
	}

	@Override
	public List<Map<String, Object>> getRemindTrainList(GzglDto gzglDto) {
		return dao.getRemindTrainList(gzglDto);
	}
	/**
	 * 删除
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteDto(GzglDto gzglDto) {
		// TODO Auto-generated method stub
		return dao.delete(gzglDto) != 0;
	}

	@Override
	public String getDtoTrainXh(GzglDto gzglDto) {
		return dao.getDtoTrainXh(gzglDto);
	}

	@Override
	public GzglDto getDtoTrainFjid(GzglDto gzglDto) {
		return dao.getDtoTrainFjid(gzglDto);
	}

	@Override
	public String getStringTrainFjid(GzglDto gzglDto) {
		return dao.getStringTrainFjid(gzglDto);
	}
}
