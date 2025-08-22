package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.DataPermissionModel;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.AuditResult;
import com.matridx.igams.common.dao.entities.BaseBasicModel;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShgcModel;
import com.matridx.igams.common.dao.entities.ShlbModel;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.post.IShgcDao;
import com.matridx.igams.common.dao.post.IShlbDao;
import com.matridx.igams.common.dao.post.IShlcDao;
import com.matridx.igams.common.dao.post.IShxxDao;
import com.matridx.igams.common.dao.post.ISpgwcyDao;
import com.matridx.igams.common.dao.post.IWtxxDao;
import com.matridx.igams.common.dao.post.IXtshDao;
import com.matridx.igams.common.dao.post.IXtszDao;
import com.matridx.igams.common.dao.post.IXxglDao;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.ShlcKzcsEnum;
import com.matridx.igams.common.enums.YesNotEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.springboot.util.base.JsonUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShgcServiceImpl extends BaseBasicServiceImpl<ShgcDto, ShgcModel, IShgcDao> implements IShgcService{
	
	@Autowired
	private IXtshDao xtshDao;
	@Autowired
	private IXtszDao xtszDao;
	@Autowired
	private IShlcDao shlcDao;
	@Autowired
	private IXxglDao xxglDao;
	@Autowired
	private IShlbDao shlbDao;
	@Autowired
	private IShxxDao shxxDao;
	@Autowired
	private IWtxxDao wtxxDao;
	@Autowired
	private ISpgwcyDao spgwcyDao;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	@Autowired
	RedisUtil redisUtil;
	//是否发送rabbit标记     1：发送
  	@Value("${matridx.rabbit.configflg:}")
  	private String configflg;
  	@Value("${matridx.rabbit.preflg:}")
  	private String preRabbitFlg;
  	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
  	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
  	
	private final Logger logger = LoggerFactory.getLogger(ShgcServiceImpl.class);
	
	/**
	 * 根据业务id列表将当前审核过程信息放入列表中
	 * @param modelList 用于放检索结果的列表
	 * @param auditType 审核类别
	 * @param ztName	状态属性名
	 * @param ywIdName	业务id属性名
	 * @param queryZts 需要去查询审核信息的状态值（一般为：提交审核、审核退回状态）
	 */
	public void addShgcxxByYwid(List<? extends BaseBasicModel> modelList,String auditType, 
			String ztName, String ywIdName, String[] queryZts) throws BusinessException {
		addShgcxxByYwid(modelList, auditType, ztName, ywIdName, true, queryZts);
	}
	
	/**
	 * 根据业务id列表将当前审核过程信息放入列表中
	 * @param modelList 用于放检索结果的列表
	 * @param auditType 审核类别
	 * @param ztName	状态属性名
	 * @param ywIdName	业务id属性名
	 * @param isIncludeZt 判断queryZts时是否用包含还是排除法
	 * @param queryZts 状态数组参数
	 */
	public void addShgcxxByYwid(List<? extends BaseBasicModel> modelList,String auditType, 
			String ztName, String ywIdName, boolean isIncludeZt, String[] queryZts) throws BusinessException {
		if(modelList == null || modelList.isEmpty() || queryZts == null || queryZts.length == 0
				|| StringUtil.isBlank(ztName) || StringUtil.isBlank(ywIdName))
			return;
		List<String> queryZtList = Arrays.asList(queryZts);
		List<String> ywIdList = new ArrayList<>();
		try {
			for (BaseModel baseModel : modelList) {
				Class<?> entityClass = baseModel.getClass();
				Method ztMethod = entityClass.getMethod("get" + StringUtil.firstToUpper(ztName));
				Method ywIdMethod = entityClass.getMethod("get" + StringUtil.firstToUpper(ywIdName));
				String zt = (String) ztMethod.invoke(baseModel);
				String ywId = (String) ywIdMethod.invoke(baseModel);
				//判断该状态是否需要去查询审核信息
				if((isIncludeZt&&zt!=null&&queryZtList.contains(zt))
					||(!isIncludeZt&&zt!=null&&!queryZtList.contains(zt))){
					ywIdList.add(ywId);
				}else{
					ywIdList.add(null);
				}
			}
		} catch (Exception e) {
			throw new BusinessException(null, e.getMessage());
		}
		
		addShgcxxByYwid(modelList, ywIdList, auditType);
	}
	
	/**
	 * 根据业务id列表将当前审核过程信息放入列表中
	 * @param modelList 用于放检索结果的列表
	 * @param ywList 查询的业务ID列表
	 * @param auditType 审核类别
	 */
	public void addShgcxxByYwid(List<? extends BaseBasicModel> modelList,List<String> ywList,String auditType) {
		if(ywList == null || ywList.isEmpty())
			return;
		boolean containValue = false;
		for (String string : ywList) {
			if(StringUtil.isNotBlank(string)){
				containValue = true;
				break;
			}
		}
		if(!containValue) return;//全为null则不进行查询
		ShgcDto param = new ShgcDto();
		param.setYwids(ywList);
		param.setShlb(auditType);
		//ShlbModel shlbModel = shlbService.getModelById(auditType);
		//param.setFqmc(shlbModel.getFqlb());
		List<ShgcDto> dataList = dao.getCurrentShgcxxList(param);
		if(dataList==null|| dataList.isEmpty()){
			return;
		}
		for (ShgcDto shgc : dataList) {
			if (shgc == null) continue;
			(modelList.get(shgc.getOrdernum())).setShxx_dqgwid(shgc.getShxx_dqgwid());
			(modelList.get(shgc.getOrdernum())).setShxx_dqgwmc(shgc.getShxx_dqgwmc());
			(modelList.get(shgc.getOrdernum())).setShxx_gwmc(shgc.getShxx_gwmc());
			(modelList.get(shgc.getOrdernum())).setShxx_gwid(shgc.getShxx_gwid());
			(modelList.get(shgc.getOrdernum())).setShxx_shyj(shgc.getShxx_shyj());
			(modelList.get(shgc.getOrdernum())).setShxx_lcxh(shgc.getXlcxh());
			(modelList.get(shgc.getOrdernum())).setDqshr(shgc.getDqshr());
		}
	}
	
	/**
	 * 检查审核配置及不弹窗提交操作
	 * 1.获取设置值，判断是否弹窗显示
	 * 2.根据审核类别获取审核列表，大于1的显示弹窗，等于1的直接提交
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Map<String, Object> checkAndCommit(ShgcDto shgcDto,User user) throws BusinessException{
		Map<String, Object> data = new HashMap<>();// 返回信息
		
		if (StringUtil.isBlank(shgcDto.getShlb())) {
			throw new BusinessException("I99016", null);
		}
		if (StringUtil.isBlank(shgcDto.getExtend_1())) {
			throw new BusinessException("I99016", null);
		}
		XtszDto xtszDto = xtszDao.getDtoById(GlobalString.AUDIT_CONFIRM_TYPE);
		if (GlobalString.AUDIT_CONFIRM_JUDGE.equals(xtszDto.getSzz())) {
			// 根据流程数，判断是否需要提交
			XtshDto xtshDto = new XtshDto();
			xtshDto.setShlb(shgcDto.getShlb());
			xtshDto.setExtend_2(shgcDto.getExtend_2());
			xtshDto.setShid(shgcDto.getShid());
			xtshDto.setMrsz(shgcDto.getMrsz());
			List<XtshDto> xtshList = xtshDao.getDtoList(xtshDto);
			if (xtshList == null || xtshList.isEmpty()) {// 空则提示无法提交
				throw new BusinessException("I99029", "系统审核未配置，无法提交!");// 无系统审核，无法提交
			} else if (xtshList.size() > 1) {
				XtshDto t_xtshDto = new XtshDto();
				boolean bol = true;
				//判断是否存在默认的
				for (XtshDto xtshDtoT : xtshList){
					if("01".equals(xtshDtoT.getMrsz())){
						t_xtshDto = xtshDtoT;
						bol = false;
						break;
					}
				}
				if(!bol){
					shgcDto.setShid(t_xtshDto.getShid());// 放入审核id
					t_xtshDto.setExtend_1(shgcDto.getExtend_1());
					String result = doCommitAudit(t_xtshDto,user);
					if (StringUtil.isBlank(result)) {
						String code = "ICOM99015";
						data.put("status", "success");
						data.put("message", xxglDao.getModelById(code).getXxnr());
						data.put("msg", data.get("message"));
						data.put("result", true);
					} else {
						logger.error("checkAndCommit: Extend_1=" + shgcDto.getExtend_1() + " Extend_2=" + shgcDto.getExtend_2());
						data.put("status", "fail");
						data.put("message", result);
					}
				}else {
					data.put("popDialog", true); // 多个需要弹出提交
				}
			} else { // 1个则直接提交
				shgcDto.setShid(xtshList.get(0).getShid());// 放入审核id
				XtshDto t_xtshDto = xtshList.get(0);
				t_xtshDto.setExtend_1(shgcDto.getExtend_1());
				String result = doCommitAudit(t_xtshDto,user);
				if (StringUtil.isBlank(result)) {
					String code = "ICOM99015";
					data.put("status", "success");
					data.put("message", xxglDao.getModelById(code).getXxnr());
					data.put("msg", data.get("message"));
					data.put("result", true);
				} else {
					logger.error("checkAndCommit: Extend_1=" + shgcDto.getExtend_1() + " Extend_2=" + shgcDto.getExtend_2());
					data.put("status", "fail");
					data.put("message", result);
				}
			}
		} else{
			// 需要选择流程提交
			data.put("popDialog", true); // 需要弹出提交
		}
		
		return data;
	}

	/**
	 * 提交审核
	 * 1.切分业务ID列表
	 * 2.获取审核流程信息
	 * 3.如果该业务已有原有的审核信息，则直接修改原有审核信息
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public String doCommitAudit(XtshDto dto,User user) throws BusinessException {
		
		List<Object> ywids = JsonUtil.jsonArrToList(dto.getExtend_1());
		List<ShgcDto> shgcList = new ArrayList<>();
		int updateCount;
		//审核id
		if(StringUtil.isBlank(dto.getShid())){
			throw new BusinessException("I99016",null);
		}
		
		List<ShlcDto> shlcList = this.getShlcList(dto.getShid(), null, null);
		int maxLcxh = 0;
		if(shlcList!=null&&!shlcList.isEmpty()){
			maxLcxh = Integer.parseInt(shlcList.get(shlcList.size()-1).getLcxh());
		}
		
		//回调业务状态Service
		ShlbModel shlbModel = shlbDao.getDtoById(dto.getShlb());
		//确认系统设置，如果有钉钉审核提醒，则通知相应的人员 2018-12-27增加
		XtszDto xtszDto = xtszDao.getDtoById(GlobalString.AUDIT_DINGTALK_FLG);
		//end
		for (Object ywid : ywids) {
			//根据ywid获得已有审核过程
			ShgcDto oldShgc = dao.getDtoByYwid((String)ywid);
			ShgcDto shgc = new ShgcDto();

			//增加判断审核后是否进入最后一步的判断，需要发送消息
			if(maxLcxh  == 1)
				shgc.setEntryLastStep(true);
			
			shgc.setSqr(user.getYhid());
			shgc.setSqrxm(user.getZsxm());
			shgc.setYwid((String)ywid);
			shgc.setShlb(dto.getShlb());
			shgc.setAuditType(dto.getShlb());//放入审核类别
			shgc.setShlbmc(shlbModel.getShlbmc());
			shgc.setShid(dto.getShid());
			shgc.setYwdm(dto.getShlb());
			shgc.setYwmc(shlbModel.getShlbmc());//先随便放一个
			if(oldShgc!=null){//获取已有的过程进行判断
				//审核类型跟已有的相同
				if(shgc.getShlb().equals(oldShgc.getShlb())){
					//如果现流程序号已有，则表示已提交
					if(StringUtil.isNotBlank(oldShgc.getXlcxh())){
						throw new BusinessException("I99017","审批过程信息已经存在！");
					}
					shgc.setGcid(oldShgc.getGcid());
					//后续流程，暂不用
					shgc.setHxlcxh(null);
				}
//				else{//审核类型跟已有的不同
//					//判断需要检查不同的审核类型
//					/*if(false){
//						//当前流程序号不为空，则表示已经在走审核流程了，不允许同时走不同类型的审核流程
//						if(StringUtil.isNotBlank(oldShgc.getXlcxh())){
//							//抛出异常，表示正在审核中不允许提交
//							throw new BusinessException("I99020",null);
//						}else{//否则，表示退回到提交人了，可以提交，但是需要删除已有审核过程，插入新的
//							dao.deleteByYwid(oldShgc.getYwid());//直接删除，否则插入新的会报ywid唯一性错误
//							oldShgc = null;
//						}
//					}*/
//				}
			}
			out:if(xtszDto != null){
				if(GlobalString.EXTEND_ONE.equals(xtszDto.getSzz())){
					//获取下一步的审批岗位
					if(shlcList!=null&&!shlcList.isEmpty()) {
						ShlcDto shlcDto = shlcList.get(0);
						shgc.setShgwid(shlcDto.getGwid());
						shgc.setLclbcskz2(shlcDto.getLclbcskz2());
						shgc.setLclbcskz3(shlcDto.getLclbcskz3());
						shgc.setDdhdlxcskz1(shlcDto.getDdhdlxcskz1());
						SpgwcyDto spgwcyDto = new SpgwcyDto();
						if ("JX".equals(shlcDto.getWbgw())){
							spgwcyDto.setYhid(user.getYhid());
							SpgwcyDto spgwcyDto_ = spgwcyDao.getZgByDto(spgwcyDto);
							if (null != spgwcyDto_ && StringUtil.isNotBlank(spgwcyDto_.getYhid())){
								shgc.setSpgwcyDtos(Collections.singletonList(spgwcyDto_));
								shgc.setLxid(spgwcyDto_.getYhid());
								shgc.setLx("SJZG");//上级主管
								break out;
							}else{
								throw new BusinessException("I99016",null);
							}
						}
                        spgwcyDto.setGwid(shlcDto.getGwid());
                        List<SpgwcyDto> spgwcyDtos = spgwcyDao.getDtoList(spgwcyDto);
						shgc.setSpgwcyDtos(spgwcyDtos);
                        shgc.setShxx_gwid(shlcDto.getGwid());
                        shgc.setLxid(shlcDto.getGwid());
                        shgc.setLx("GW");//岗位
					}
				}
			}
			
			if(shlcList!=null&&!shlcList.isEmpty()) {
				shgc.setAuditState(AuditStateEnum.AUDITING);
			}else {
				shgc.setAuditState(AuditStateEnum.AUDITED);
			}
			
			shgc.setXlcxh(GlobalString.AUDIT_STEP_ONE);//现流程默认为1
			
			//提交成功后给特定的几个审核类型添加节点
			//insertIntoXmjdxx(shgc, ywid, user);
			
			shgcList.add(shgc);
		}
		
		//回调业务状态Service
		String serviceName = shlbModel.getHdl();
		boolean isUpdated;
		IAuditService auditService = (IAuditService)ServiceFactory.getService(serviceName);

		isUpdated = auditService.updateAuditTarget(shgcList,user,new AuditParam(shgcList.size()>1,true,false,false));
		//判断执行是否成功
		if(!isUpdated){
			throw new BusinessException("I99016",null);
		}
		
		// 2021-01-14 start
		// 为统一提交审核和审核中时的处理，全部改为先设置现流程序号，但不更新数据库，等 updateAuditTarget 执行完后才去更新数据库
		if(shlcList!=null&&!shgcList.isEmpty()) {
			for(ShgcDto t_shgc:shgcList) {
				if(maxLcxh==0){//没有配置流程,则直接通过
					t_shgc.setAuditState(AuditStateEnum.AUDITED);
					if(StringUtil.isNotBlank(t_shgc.getGcid())){
						dao.deleteByYwid(t_shgc.getGcid());
					}
				}else{
					t_shgc.setAuditState(AuditStateEnum.AUDITING);
					if(StringUtil.isBlank(t_shgc.getGcid())){
						//设置过程ID，同时插入到数据库
						String gcid = StringUtil.generateUUID();
						t_shgc.setGcid(gcid);
						updateCount = dao.insert(t_shgc);
					}else{
						updateCount = dao.updateShgc(t_shgc);
					}
					if(updateCount!=1){
						throw new BusinessException("I99016",null);
					}
				}
				//设置审核类别名称
				t_shgc.setShlbmc(shlbModel.getShlbmc());
				
				insertCommitShxx(t_shgc, t_shgc.getYwid(), user);//插入提交信息
			}
		}
		//2021-01-14 end
		
		return null;
	}
	
	/**
	 * 审核确定处理
	 * 1.循环审核ID列表，判断各种前置条件，保存审核信息
	 * 2.回调各个业务类的方法，执行后续操作，同时更新 审核表里的数据
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public AuditResult doAudit(ShxxDto dto, User user,HttpServletRequest request) throws Exception {
		if (StringUtil.isNotBlank(dto.getYwid()))
			dto.getYwids().add(dto.getYwid());
		return batchAudit(dto, user,request, false);
	}
	
	/**
	 * 审核回退到指定的审核流程，按照之前的审核通过实践允许自动多次回退(修改：钉钉撤回直接审核不通过)
	 * @param dto 本次审核信息
	 * @param user 本次用户信息
	 * @param request 请求信息
	 * @param lastlcxh 回退到的最后流程
	 */
	public AuditResult doManyBackAudit(ShxxDto dto, User user,HttpServletRequest request,String lastlcxh,JSONObject obj) throws Exception {
		String EventType=obj.getString("EventType");
		String content=obj.getString("content");
		//User t_user = new User();
		if (!dto.getYwids().contains(dto.getYwid())){
			dto.getYwids().add(dto.getYwid());
		}
		ShgcDto shgcDto=new ShgcDto();
		shgcDto.setShlb(dto.getShlb());
		shgcDto.setYwid(dto.getYwid());
		shgcDto.setGcid(dto.getGcid());
		ShgcDto shgcDto_t=dao.getSpgwCurrent(shgcDto);
		AuditResult result = null;
		//若当前岗位为钉钉特殊审批岗位时且事件未结束只添加审核信息，若事件结束，则审核过程往后走一步，并调用共通审核方法
		if(shgcDto_t!=null){
			if("TS".equals(shgcDto_t.getWbgw())){//若启用特殊岗位
				if("bpms_instance_change".equals(EventType)){//若事件结束,审核过程流程序号加1
					user.setYhid("");
					user.setZsxm("");
					user.setDqjs("");
					Date currentDate = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
					String dateString = dateFormat.format(currentDate);
					dto.setShsj(dateString);
					//更新完审核过程后，再次调用审核共通方法
					result = batchAudit(dto, user,request, false);
				}else{//task类型时保存审核信息
					//保存审核信息
					ShxxDto shxx = new ShxxDto();
					String shxxid =StringUtil.generateUUID();
					shxx.setShxxid(shxxid);
					shxx.setSqr(shgcDto_t.getSqr());
					shxx.setLcxh(null);
					shxx.setShid(shgcDto_t.getShid());
					shxx.setSftg(dto.getSftg());
					shxx.setGcid(shgcDto_t.getGcid());
					shxx.setYwid(dto.getYwid());
					if(StringUtils.isNotBlank(content)){
						shxx.setShyj(content.substring(0, Math.min(content.length(), 6000)));//这里写死，备注钉钉审批被提交人员撤销了
					}else{
						shxx.setShyj(dto.getShyj());
					}
					shxx.setShsj(dto.getShsj());
					shxx.setLrry(user.getYhid());
					shxxDao.insert(shxx);
				}
			}else{
				if(!"bpms_instance_change".equals(EventType)){
					result = batchAudit(dto, user,request, false);
				}else {
					throw new BusinessException("msg","10001");
				}
			}
		}
		return result;
	}

	/**
	 * 钉钉审批撤回操作回调审核
	 */
	public boolean terminateAudit(ShxxDto dto, User user,HttpServletRequest request,String lastlcxh,JSONObject obj)throws BusinessException{
		String type=obj.getString("type");
		String content=obj.getString("content");
		String remark=obj.getString("remark");
		//插入审核信息
		//1.1 获取审核过程，进行前置判断
		ShgcDto shgc = dao.getDtoByYwid(dto.getYwid());//获得已有审核过程
		//插入审核信息
		ShxxDto shxx = new ShxxDto();
		String shxxid =StringUtil.generateUUID();
		shxx.setShxxid(shxxid);
		shxx.setShlb(shgc.getShlb());
		shxx.setShlbs(shgc.getShlbs());
		shxx.setSqr(shgc.getSqr());
		shxx.setLcxh(dto.getThlcxh());
		shxx.setShid(shgc.getShid());
		shxx.setSftg(dto.getSftg());
		shxx.setGcid(shgc.getGcid());
		shxx.setYwid(dto.getYwid());
		if(("terminate").equals(type)){
			shxx.setShyj("提交人员撤销！");//这里写死，备注钉钉审批被提交人员撤销了
		}else{
			if(StringUtil.isNotBlank(content)){
				shxx.setShyj(content);//这里写死，备注钉钉审批被提交人员撤销了
			}else{
				shxx.setShyj(remark);
			}
		}
		shxx.setLrry(user.getYhid());
		shxx.setShsj(dto.getShsj());
		int addShxx = shxxDao.insert(shxx);
		if(addShxx<=0)
			throw new BusinessException("ICOM99019","审核失败！");
		//更新审核过程
		ShgcDto shgcDto=new ShgcDto();
		shgcDto.setYwid(dto.getYwid());
		shgcDto.setXlcxh(dto.getThlcxh());
		if(StringUtils.isBlank(dto.getThlcxh())){
			shgcDto.setAuditState(AuditStateEnum.AUDITBACK);
		}else{
			shgcDto.setAuditState(AuditStateEnum.AUDITING);
		}
		shgcDto.setGcid(shgc.getGcid());
		int updateShgc=dao.update(shgcDto);
		if(updateShgc<=0)
			throw new BusinessException("ICOM99019","审核失败！");
		shgcDto.setShxx(shxx);
		ShlbModel shlbModel = shlbDao.getDtoById(dto.getShlb());

		//获取回调类
		String serviceName = shlbModel.getHdl();
		IAuditService auditService = (IAuditService)ServiceFactory.getService(serviceName);

		List<ShgcDto> dataList = new ArrayList<>();
		dataList.add(shgcDto);
		boolean result=auditService.updateAuditTarget(dataList,user,null);

		if(!result)
			throw new BusinessException("ICOM99019","审核失败！");

		//获取审批人信息，发送钉钉消息
		shxx.setSftg(null);
		shxx.setCommit(false);
		List<ShxxDto> shxxlist = shxxDao.getSecShxxOrderByShsj(shxx);
		if(shxxlist!=null && !shxxlist.isEmpty()){
			List<ShxxDto> shxxlist_t=new ArrayList<>();
			for(int i=0;i<shxxlist.size();i++){
				if(i>0){
					if(StringUtils.isBlank(shxxlist.get(i).getSftg()))
						break;
					shxxlist_t.add(shxxlist.get(i));
				}
			}
//			String token = talkUtil.getToken();
			if(!shxxlist_t.isEmpty()){
				for(ShxxDto shxxDto : shxxlist_t){
					if(StringUtil.isNotBlank(shxxDto.getLrryddid())){
						talkUtil.sendWorkMessage(shxxDto.getLrry(), shxxDto.getLrryddid(),
								xxglService.getMsg("ICOMM_SH00026"),xxglService.getMsg("ICOMM_SH00016",shxxDto.getSqrmc(),shxxDto.getShmc() ,dto.getYwglmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
					}
				}
			}
		}
		return true;
	}

	/**
	 * 钉钉审批点击拒绝操作修改本地审核过程，添加审核信息（拒绝时用户为审核人员）
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public AuditResult DingtalkRecallAudit(ShxxDto dto, User user,HttpServletRequest request,String lastlcxh,JSONObject obj)  throws Exception{
		int i_last = 0;
		int i_xlcxh = 0;
		if(StringUtil.isNotBlank(lastlcxh))
			i_last = Integer.parseInt(lastlcxh);

		//1.1 获取审核过程，进行前置判断
		ShgcDto shgc = dao.getDtoByYwid(dto.getYwid());//获得已有审核过程
		String xlcxh = shgc.getXlcxh();

		if(StringUtil.isNotBlank(xlcxh))
			i_xlcxh = Integer.parseInt(xlcxh);

		User t_user = new User();
		t_user.setYhid(user.getYhid());
		t_user.setZsxm(user.getZsxm());
		t_user.setDqjs(user.getDqjs());
		dto.getYwids().add(dto.getYwid());

		//获取审批流程（步骤）
		List<ShlcDto> shlcList = this.getShlcList(shgc.getShid(), shgc.getGcid(),null);
		//获取审核信息
		ShxxDto t_shxxDto = new ShxxDto();
		t_shxxDto.setGcid(dto.getGcid());
		t_shxxDto.setSftg("1");
		t_shxxDto.setYwid(dto.getYwid());
		List<ShxxDto> shxxList = shxxDao.getShxxOrderByShsj(t_shxxDto);
		AuditResult result = null;
		if("0".equals(dto.getSftg())) {
			for(int i = i_xlcxh ;i>i_last;i--) {
				result = batchAudit(dto, t_user,request, false);
				if("0".equals(dto.getSftg()))
					dto.setLcxh(String.valueOf(Integer.parseInt(dto.getLcxh())-1));
				//获取前一个的审核岗位
				if(i>=2) {
					ShlcDto t_shlcDto = shlcList.get(i-2);
					//从以前的审核信息里获取最近通过的相同岗位的审核信息，从而得到审核人员信息
					boolean isFind = false;
					for (ShxxDto shxxDto : shxxList) {
						t_shxxDto = shxxDto;
						if (t_shxxDto.getGwid().equals(t_shlcDto.getGwid())) {
							isFind = true;
							t_user.setYhid(t_shxxDto.getLrry());
							t_user.setZsxm(t_shxxDto.getShrmc());
							//查询当前角色
							SpgwcyDto spgwcyDto = new SpgwcyDto();
							spgwcyDto.setYhid(t_shxxDto.getLrry());
							spgwcyDto.setGwid(t_shxxDto.getGwid());
							List<SpgwcyDto> list = spgwcyDao.getDtoList(spgwcyDto);
							if (list != null && !list.isEmpty()) {
								t_user.setDqjs(list.get(0).getJsid());
							}
							break;
						}
					}
					if(!isFind)
						return null;
				}
			}
		}else {
			result = batchAudit(dto, t_user,request, false);
		}
		return result;
	}
	
	/**
	 * 批量审核处理
	 */
	@Async
	public void doBatchAudit(ShxxDto dto, User user,HttpServletRequest request) throws Exception {
		try{
			List<String> ywids = dto.getYwids();
			if(ywids == null || ywids.size() <= 0)
				return;
			
			Map<String,Object> p_process = new HashMap<>();
			p_process.put("Finished", false);
			p_process.put("CurrentCnt", 0);
			p_process.put("Total", ywids.size());
			//失效时间1小时
			redisUtil.hmset("BATCH_AUDIT:_"+dto.getLoadYmCode(), p_process,3600);
			redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "Cancel",false);
			AuditResult auditResult = batchAudit(dto, user, request, true);	
			//如果成功
			if(auditResult.getType()==AuditResult.SUCCESS) {
				//完成审核逻辑后的回调处理，主要是为了跟审核的事务分开，确保数据都已经反馈到数据库中，则其他线程可以获取到数据
				// 比如发送rabbitmq 处理报告生成的时候，报告日期已经放入到数据库中
				doBatchAuditEnd(auditResult, user,dto, true);
				
				redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "status", "2");
				redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "message", auditResult.getMsg());
				redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "backInfo", auditResult.getBackMap());
			}else {
				redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "status", "-2");
				redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "message", auditResult.getMsg());
				redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "backInfo", auditResult.getBackMap());
			}
			//String msg = auditResult.getReturnMsg(xxglService.getModelById("ICOM99018").getXxnr(), xxglService.getModelById("ICOM99019").getXxnr());
			
			redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "Finished", true);
			redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "CurrentCnt", ywids.size());

		} catch (BusinessException bu){
			redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "Finished", true);
			redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "status", "-2");
			redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "message",bu.getMessage());
			throw bu;
		} catch (Exception e){
			// TODO: handle exception
			redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "Finished", true);
			redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "status", "-2");
			redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "message",e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 批量审核完成后的处理
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean doBatchAuditEnd(AuditResult result,User user,ShxxDto dto,boolean isBatchOpe) throws Exception
	{
		if(result == null)
			return false;
		
		Map<String, String> backMap = result.getBackMap();
		String serviceName = backMap.get("serviceName");
		
		IAuditService auditService = (IAuditService)ServiceFactory.getService(serviceName);
		
		//2.2 回调业务Service的最后处理，用于完成事务后调用
		return auditService.updateAuditEnd(dto, user, backMap);
	}
	
	/**
	 * 根据业务ID获取 审核过程信息
	 */
	public ShgcDto getDtoByYwid(String ywid){
		return dao.getDtoByYwid(ywid);
	}

	/**
	 * 根据业务ID获取 审核过程信息
	 */
	public List<ShgcDto> getDtoByYwids(List<String> ywids){
		return dao.getDtoByYwids(ywids);
	}

	/**
	 * 根据业务ID获取审批岗位成员信息
	 */
	public List<ShgcDto> getSpgwcyByYwids(List<String> ywids){
		return dao.getSpgwcyByYwids(ywids);
	}

	@Override
	public Boolean updateNext(String ywid) {
		return dao.updateNext(ywid)!=0;
	}

	/**
	 * 批量操作
	 * @param request 用于生成Dto
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	AuditResult batchAudit(ShxxDto dto, User user, HttpServletRequest request, boolean isBatchOpe) throws Exception {
		
		List<String> ywids = dto.getYwids();
		List<String> delList = new ArrayList<>();
		int updateCount;
		boolean isPass = YesNotEnum.YES.getCode().equals(dto.getSftg());
		Map<String,List<ShgcDto>> shgcMap = new HashMap<>();
		Map<String, String> backMap = null;
		Map<String,String> errMap;
		Map<String,String> sucMap;
		
		//确认系统设置，如果有钉钉审核提醒，则通知相应的人员 2018-12-27增加
		XtszDto xtszDto = xtszDao.getDtoById(GlobalString.AUDIT_DINGTALK_FLG);
		
		if(xtszDto == null)
			xtszDto = new XtszDto();
		
		//1.循环审核ID列表，判断各种前置条件，保存审核信息
		for (String ywid : ywids) {
			initShxxByYwid(ywid,isBatchOpe,dto,user,shgcMap,xtszDto.getSzz());
		}
		//2.回调各个业务类的方法，执行后续操作，同时更新 审核表里的数据
		boolean isUpdated = true;
		List<String> errmsg = new ArrayList<>();
		List<String> sucMsg = new ArrayList<>();
		for (String shlb : shgcMap.keySet()) {
			//如果客户端点击了取消按钮，则抛出异常，停止所有的更新处理
			if(isBatchOpe) {
				Object o_CancelObject = redisUtil.hget("BATCH_AUDIT:_"+dto.getLoadYmCode(), "Cancel");
				if(o_CancelObject != null) {
					boolean isCancel = (boolean)o_CancelObject;
					if(isCancel) {
						throw new BusinessException("","客户取消");
					}
				}
			}
			
			//2.1 回调业务Service 的前置处理，比如进行更新处理
			ShlbModel shlbModel = shlbDao.getDtoById(shlb);
			
			AuditParam param = new AuditParam(isBatchOpe,false,false,isPass);

			errMap = param.getErrMap();
			sucMap = param.getSucMap();
			backMap = param.getBackMap();
			
			//获取回调类
			String serviceName = shlbModel.getHdl();
			IAuditService auditService = (IAuditService)ServiceFactory.getService(serviceName);
			
			backMap.put("serviceName", serviceName);
			//如果为批量审核，则不需要对每个审核的业务内容进行更新
			if(!isBatchOpe) {
				String dtoName = shlbModel.getHdmx();
				Object baseModel = Class.forName(dtoName).newInstance();
				BeanInfo beanInfo = Introspector.getBeanInfo(baseModel.getClass());
				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
				
				for (PropertyDescriptor property : propertyDescriptors) {
					String key = property.getName();
					String value = request.getParameter(key);
					if (value != null){
						// 得到property对应的setter方法
						Method setter = property.getWriteMethod();
						switch (setter.getParameterTypes()[0].getName()) {
							case "java.lang.String":
								setter.invoke(baseModel, value);
								break;
							case "java.util.List": {
								String s_value = value.replace("[", "").replace("]", "").replace(" ", "");
								if (StringUtil.isBlank(s_value))
									continue;
								String[] ss_value = s_value.split(",");
								List<String> list = Arrays.asList(ss_value);
								setter.invoke(baseModel, list);
								break;
							}
							case "[Ljava.lang.String;": {
								String s_value = value.replace("[", "").replace("]", "");
								if (StringUtil.isBlank(s_value))
									continue;
								String[] ss_value = s_value.split(",");
								setter.invoke(baseModel, new Object[]{ss_value});
								break;
							}
						}
					}
					//根据审核信息，设置现流程序号，用于处理当前审核时的一些业务逻辑
					if("shxx_lcxh".equals(key)) {
						// 得到property对应的setter方法
						Method setter = property.getWriteMethod();
						setter.invoke(baseModel, dto.getLcxh());
					}
				}
				List<ShgcDto> dataList = shgcMap.get(shlb);
				
				param.setShgcDto(dataList.get(0));
				user.setBycsList(shgcMap.get(shlb));
				//2.2 回调业务Service的前期准备，进行各业务的保存等处理
				isUpdated = auditService.updatePreAuditTarget(baseModel, user, param);
			}
			
			//判断执行是否成功
			if(!isUpdated){
				throw new BusinessException("ICOM99019",null);
			}
			
			//2.3 回调业务Service，进行各业务的审核状态设置
			List<ShgcDto> dataList = shgcMap.get(shlb);
			//循环设置审核类别名称 2019-01-02
			if(dataList != null && !dataList.isEmpty()){
				for (ShgcDto shgcDto : dataList) {
					shgcDto.setShlbmc(shlbModel.getShlbmc());
					shgcDto.setJsids(dto.getJsids());
					shgcDto.setT_jsids(dto.getT_jsids());
					shgcDto.setD_jsids(dto.getD_jsids());
					shgcDto.setLastStep(dto.isLastStep());
					shgcDto.setShxx_shyj(dto.getShyj());
					shgcDto.setWjsh(dto.getWjsh());
					shgcDto.setQgsh(dto.getQgsh());
					shgcDto.setCsrs(dto.getCsrs());
					shgcDto.setDdspid(dto.getDdspid());
				}
			}
			isUpdated = auditService.updateAuditTarget(dataList,user,param);
			//判断执行是否成功
			if(!isUpdated){
				throw new BusinessException("ICOM99019",null);
			}

			//2.2 即使执行结果通过，也要判断其中是否有错误记录
			for(ShgcDto t_dto:dataList){
				//是否通过
				//2.3 更新审核过程表数据
				if(isPass){//通过
					//判断是否有错误信息，有则跳过处理
					if(errMap != null && !StringUtil.isBlank(errMap.get(t_dto.getYwid()))){
						errmsg.add(errMap.get(t_dto.getYwid()));
						errMap.remove(t_dto.getYwid());
						continue;
					}
					
					if(AuditStateEnum.AUDITED.equals(t_dto.getAuditState())){//审核最后一步
						delList.add(t_dto.getGcid());
					}else{//更新审核过程
						updateCount = dao.update(t_dto);
						if(updateCount!=1){
							throw new BusinessException("ICOM99019",null);
						}
					}
				}else{//不通过
					updateCount = dao.update(t_dto);
					if(updateCount != 1){
						throw new BusinessException("ICOM99019",null);
					}
				}
				//2.4 插入审核信息
				if(t_dto.getShxx()!=null) {
					t_dto.getShxx().setJsid(user.getDqjs());
					updateCount = shxxDao.insert(t_dto.getShxx());
					if(updateCount!=1){
						throw new BusinessException("ICOM99019",null);
					}
				}
				if(sucMap != null && StringUtil.isNotBlank(sucMap.get(t_dto.getYwid())) ){
					sucMsg.add(sucMap.get(t_dto.getYwid()));
				}
			}
			//2.5 删除通过的过程
			if(!delList.isEmpty()){
				dao.batchDelete(delList);
			}

			//如果为批量审核，则需要做一些进度控制
			if(isBatchOpe) {
				Object o_currentCnt = redisUtil.hget("BATCH_AUDIT:_"+dto.getLoadYmCode(), "CurrentCnt");
				if(o_currentCnt!=null) {
					int currentCnt = (int)o_currentCnt;
					currentCnt += dataList.size();
					redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "CurrentCnt", currentCnt);
				}else {
					redisUtil.hset("BATCH_AUDIT:_"+dto.getLoadYmCode(), "CurrentCnt", dataList.size());
				}
			}
		}
		return AuditResult.getInstance(ywids.size(), sucMsg, null, errmsg,backMap);
	}
	
	/**
	 * 获取审核流程列表并验证是否正常
	 */
	private List<ShlcDto> getShlcList(String shid, String gcid, String sqsj) throws BusinessException {
		//获取的审核流程列表
		ShlcDto shlcParam = new ShlcDto();
		shlcParam.setShid(shid);
		shlcParam.setSqsj(sqsj);
		shlcParam.setGcid(gcid);
		List<ShlcDto> shlcList = shlcDao.getDtoList(shlcParam);
		int xh = 0;
		for (ShlcDto shlcDto : shlcList) {
			xh++;
			if(StringUtil.isAnyBlank(shlcDto.getLcxh(),shlcDto.getGwid())
					||!shlcDto.getLcxh().equals(String.valueOf(xh))){
				throw new BusinessException("I99028",null);
			}
		}
		return shlcList;
	}
	
	/**
	 * 插入提交的审核信息
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	boolean insertCommitShxx(ShgcDto dto, String ywid, User user) throws BusinessException {
		//插入审核信息
		ShxxDto shxx = new ShxxDto();
		String shxxid =StringUtil.generateUUID();
		shxx.setShxxid(shxxid);
		shxx.setSqr(user.getYhid());
		shxx.setShid(dto.getShid());
		shxx.setGcid(dto.getGcid());
		shxx.setYwid(ywid);
		shxx.setLrry(user.getYhid());
		shxx.setCommit(true);
		// 判断是否委托
		if(StringUtil.isNotBlank(user.getWtrid())){
			shxx.setWtshry(user.getWtrid());
			shxx.setStshry(user.getYhid());
		}
		try{
			if(shxxDao.insert(shxx)!=1){
				throw new BusinessException("I99016",null);
			}			
			if("1".equals(configflg)) { 
				shxx.setPrefixFlg(prefixFlg);
				amqpTempl.convertAndSend("sys.igams",preRabbitFlg + "sys.igams.shxx.inert",JSONObject.toJSONString(shxx));
			}			 
		}catch(BusinessException be){
			throw be;
		}catch(Exception e){
			throw new BusinessException("",e.getMessage());
		}
		
		return true;
	}
	
	@Override
	public User getAuditUser(BaseBasicModel dto, User user) {
		if(dto.isWtOpe()){//委托的情况
			DataPermissionModel dp = dto.getDataPermissionModel();
			Map<String,Object> param = new HashMap<>();
			param.put("wtbh", dp.getWtWtbh());
			param.put("xtzy", dp.getWtZyid());
			param.put("str", user.getYhid());
			param.put("is_role_switch", GlobalString.IS_ROLE_SWITCH);
			//查询委托人
			User wtr = wtxxDao.getWtr(param);
			//加载委托人角色权限相关
			wtr = commonService.getAuthorByUser(wtr);
			
			return wtr;
		}else{//非委托
			return user;
		}
	}
	
	@Override
	public void importBusinessCommit(String ywid, String shlb, User operator) throws BusinessException {
		//根据流程数，判断是否需要提交
		XtshDto xtshDto = new XtshDto();
		xtshDto.setShlb(shlb);
		List<XtshDto> xtshList = xtshDao.getDtoList(xtshDto);
		
		if (xtshList==null || xtshList.isEmpty()){//空则提示无法提交
			throw new BusinessException("ICOMM_SH00002",null);//无系统审核，无法提交
		}else{ //用默认的审核流程直接提交
			XtshDto t_XtshDto = null;
			for (XtshDto xtsh : xtshList) {
				//取默认的审核流程
				if ("00".equals(xtsh.getMrsz())) {
					t_XtshDto = xtsh;
					break;
				}
			}
			if (t_XtshDto == null) {
				t_XtshDto = xtshList.get(0);
			}
			logger.error("importBusinessCommit : ywid=" + ywid);
			t_XtshDto.setExtend_1("[\"" + ywid + "\"]");
			this.doCommitAudit(t_XtshDto, operator);
		}
	}
	
	/**
	 * 验证传入的用户是否可以审核
	 */
	private boolean isUserAuditabled(ShgcDto dto, User user) {
		if(user==null) return false;
		Map<String,Object> param = new HashMap<>();
		param.put("shgcDto", dto);
		param.put("user", user);
		ShgcDto shgcDto=dao.getSpgwCurrent(dto);
		if(shgcDto!=null){
			if("TS".equals(shgcDto.getWbgw()) || "JX".equals(shgcDto.getWbgw()))//如果为特殊审批岗位，返回true
				return true;
		}
		return dao.isAuditabled(param);
	}

	
	/**
	 * 根据审核初始化审核信息
	 */
	private void initShxxByYwid(String ywid,boolean isBatchOpe,ShxxDto dto,User user,Map<String,List<ShgcDto>> shgcMap,String xtsz_value)
		throws BusinessException{
		
		//1.1 获取审核过程，进行前置判断
		ShgcDto shgc = dao.getDtoByYwid(ywid);//获得已有审核过程
		
		boolean isPass = YesNotEnum.YES.getCode().equals(dto.getSftg());

		User shrUser = getAuditUser(dto, user);//当前审核人（如委托审核，则是委托人，以委托人来判断是否可以审核）
		
		if(shgc==null){//数据不在审核中
			throw new BusinessException(isBatchOpe?xxglDao.getModelById("ICOM990410").getXxnr():xxglDao.getModelById("ICOM99041").getXxnr());
		}
		//非批量(批量无法判断)，单个审核的时候，判断审核页面显示的现审核岗位与审核过程表中的xlcxh是否一致
		if(!isBatchOpe&&!shgc.getXlcxh().equals(dto.getLcxh())){
			throw new BusinessException("","ICOM99042"+xxglDao.getModelById("ICOM99042").getXxnr());
		}
		if(!isUserAuditabled(shgc, shrUser)){//验证是否是当前审核岗位
			throw new BusinessException("",isBatchOpe?xxglDao.getModelById("ICOM990420").getXxnr():xxglDao.getModelById("ICOM99042").getXxnr());
		}
		
		int lcxh = Integer.parseInt(shgc.getXlcxh());
		//获取审批流程（步骤）
		//start 修改流程序号获取方式
		List<ShlcDto> shlcList = this.getShlcList(shgc.getShid(), shgc.getGcid(),null);
		int maxLcxh = Integer.parseInt(shlcList.get(shlcList.size()-1).getLcxh());
		shgc.setMaxlcxh(maxLcxh);
		//把当前审核流程设置到过程Dto中 2019-01-26
		shgc.setShxx_lcxh(dto.getLcxh());
		ShlcDto shlc = shlcList.get(lcxh-1);
		//end
		
		//1.2 是否通过，设置审核信息的流程情况
		if(isPass){
			//放入当前流程的流程扩展参数信息
			shgc.setShlcKzcs(ShlcKzcsEnum.getEnumByCode(shlc.getKzcs()));
			//非审核退回
			shgc.setBack(false);
			shgc.setXlcxh(String.valueOf(lcxh+1));
			if(maxLcxh<=lcxh){//审核最后一步
				shgc.setAuditState(AuditStateEnum.AUDITED);
			}else{//更新审核过程
				shgc.setAuditState(AuditStateEnum.AUDITING);
				//shgc.setXlcxh(String.valueOf(lcxh+1));
			}
			//增加判断审核后是否进入最后一步的判断，需要发送消息 2019-11-22
			if((maxLcxh -1) == lcxh)
				shgc.setEntryLastStep(true);
		}else{
			//不通过，退回
			shgc.setAuditState(AuditStateEnum.AUDITING);
			//审核退回
			shgc.setBack(true);
			//获取退回的流程序号
			String thlcxhStr = dto.getThlcxh();
			int thlcxh;
			if(StringUtil.isBlank(thlcxhStr)){
				thlcxh = lcxh-1;
			}else{
				thlcxh = Integer.parseInt(thlcxhStr);
			}
			if(thlcxh>lcxh){//序号异常判断
				throw new BusinessException("ICOM99019",null);
			}
			shgc.setXlcxh(thlcxh<=0?null:String.valueOf(thlcxh));//小于0表示退出到普通人员
			
			if(shgc.getXlcxh()==null){//退回给科研人员
				shgc.setAuditState(AuditStateEnum.AUDITBACK);
			}
		}

		//设置通知的人员
		//通知相应岗位的人员
		out:if(GlobalString.EXTEND_ONE.equals(xtsz_value) && (shgc.getXlcxh()==null ||  Integer.parseInt(shgc.getXlcxh())<= maxLcxh)){
			//退回到普通人员
			if(StringUtil.isBlank(shgc.getXlcxh())){
				if(StringUtil.isNotBlank(shgc.getSqrddid())){
					List<SpgwcyDto> spgwcyDtos = new ArrayList<>();
					SpgwcyDto t_tjry = new SpgwcyDto();
					t_tjry.setYhm(shgc.getYhm());
					spgwcyDtos.add(t_tjry);
					shgc.setSpgwcyDtos(spgwcyDtos);
				}
			}else{
				//获取下一步的审批岗位
				ShlcDto shlcDto = shlcList.get(Integer.parseInt(shgc.getXlcxh())-1);
				shgc.setShgwid(shlcDto.getGwid());
				shgc.setLclbcskz2(shlcDto.getLclbcskz2());
				shgc.setLclbcskz3(shlcDto.getLclbcskz3());
				shgc.setDdhdlxcskz1(shlcDto.getDdhdlxcskz1());
				SpgwcyDto spgwcyDto = new SpgwcyDto();
				if ("JX".equals(shlcDto.getWbgw())){
					if (isPass){
						spgwcyDto.setYhid(user.getYhid());
						SpgwcyDto spgwcyDto_ = spgwcyDao.getZgByDto(spgwcyDto);
						if (null != spgwcyDto_ && StringUtil.isNotBlank(spgwcyDto_.getYhid())){
							shgc.setSpgwcyDtos(Collections.singletonList(spgwcyDto_));
							shgc.setLxid(spgwcyDto_.getYhid());
							shgc.setLx("SJZG");//上级主管
							break out;
						}else{
							throw new BusinessException("ICOM99019",null);
						}
					}else{
						spgwcyDto.setYwid(ywid);
						spgwcyDto.setLcxh(shgc.getXlcxh());
						spgwcyDto.setGcid(shgc.getGcid());
						SpgwcyDto spgwcyDto_ = spgwcyDao.getThByDto(spgwcyDto);
						if (null != spgwcyDto_ && StringUtil.isNotBlank(spgwcyDto_.getYhid())){
							shgc.setSpgwcyDtos(Collections.singletonList(spgwcyDto_));
							shgc.setLxid(spgwcyDto_.getYhid());
							shgc.setLx("SJZG");//上级主管
							break out;
						}else{
							throw new BusinessException("ICOM99019",null);
						}
					}
				}
                spgwcyDto.setGwid(shlcDto.getGwid());
                shgc.setLxid(shlcDto.getGwid());
                List<SpgwcyDto> spgwcyDtos = spgwcyDao.getDtoList(spgwcyDto);
				shgc.setSpgwcyDtos(spgwcyDtos);
				shgc.setLx("GW");//上级主管
			}
		//通知相应申请人员
		}else if(GlobalString.EXTEND_ONE.equals(xtsz_value) && Integer.parseInt(shgc.getXlcxh()) > maxLcxh){
			if(StringUtil.isNotBlank(shgc.getSqrddid())){
				List<SpgwcyDto> spgwcyDtos = new ArrayList<>();
				SpgwcyDto t_tjry = new SpgwcyDto();
				t_tjry.setYhm(shgc.getYhm());
				spgwcyDtos.add(t_tjry);
				shgc.setSpgwcyDtos(spgwcyDtos);
			}
		}

		//插入审核信息
		ShxxDto shxx = new ShxxDto();
		String shxxid =StringUtil.generateUUID();
		shxx.setShxxid(shxxid);
		shxx.setSqr(shgc.getSqr());
		shxx.setLcxh(String.valueOf(lcxh));
		shxx.setShid(shgc.getShid());
		shxx.setSftg(dto.getSftg());
		shxx.setGcid(shgc.getGcid());
		shxx.setYwid(ywid);
		shxx.setShyj(dto.getShyj());
		if (StringUtil.isNotBlank(dto.getShsj())){
			shxx.setShsj(dto.getShsj());
		}
		//1.3 判断是否为委托，进行委托处理
		boolean wtOpe = dto.isWtOpe();
		
		if(wtOpe){//委托审核
			//若当前登录用户和委托用户不同
			//审核人放委托人
			shxx.setWtshry(shrUser.getYhid());
			shxx.setLrry(shrUser.getYhid());
			//受托审核人是当前用户
			shxx.setStshry(user.getYhid());
		}else{//非委托，审核人是当前用户
			shxx.setLrry(user.getYhid());
		}
		//存审核人用户角色
		if(GlobalString.IS_ROLE_SWITCH) {
			shxx.setJsid(shrUser.getDqjs());
		}
		shxx.setGwid(shlc.getGwid());
		shgc.setShxx(shxx);
		
		//按类别归类
		List<ShgcDto> shgcList =  shgcMap.get(shgc.getShlb());
		if(shgcList==null){
			shgcList = new ArrayList<>();
			shgcMap.put(shgc.getShlb(),shgcList);
		}
		shgc.setAuditType(shgc.getShlb());//放入审核类别
		shgc.setQwwcsj(dto.getQwwcsj());//放入期望完成时间
		shgc.setSignflg(dto.getSignflg());//放入文件转换标记
		shgc.setReplaceflg(dto.getReplaceflg());//放入文件替换标记
		//1.4 保存审核信息，但暂不插入数据库

		shgcList.add(shgc);
	}
	
	/**
	 * 取消审核
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean doCancleAudit(ShxxDto dto, User user) throws BusinessException{
		dto.getShxxids().clear();
		dto.getShxxids().add(dto.getShxxid());
		return batchCancelAudit(dto, user);
	}
	
	/**
	 * 批量取消审核
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	boolean batchCancelAudit(ShxxDto dto, User user) throws BusinessException {
		List<String> shxxids = dto.getShxxids();
		List<String> delList = new ArrayList<>();
		User shrUser = getAuditUser(dto, user);//当前审核人（如委托审核，则是委托人，以委托人来判断是否可以审核）
		boolean wtOpe = dto.isWtOpe();
		int updateCount;
		Map<String,List<ShgcDto>> shgcMap = new HashMap<>();
		
		//确认系统设置，如果有钉钉审核提醒，则通知相应的人员 2018-12-27增加
		XtszDto xtszDto = xtszDao.getDtoById(GlobalString.AUDIT_DINGTALK_FLG);
		
		for (String shxxid : shxxids) {
			//根据审核信息id，获得最新的审核信息
			ShxxDto shxxParam = new ShxxDto();
			shxxParam.setShxxid(shxxid);
			ShxxDto shxx = shxxDao.getLastShxx(shxxParam);
			//判断是否是最新的审批信息，不是则抛出异常
			if(shxx==null){//为空，则取消审批失败
				throw new BusinessException("ICOM99021","审核信息为空");
			}
			if(wtOpe){//委托下的操作
				//委托无效（委托人为空） 或 受托审核人不是当前用户 或 审核人不是委托人，则抛出异常
				if(shrUser==null
						||!user.getYhid().equals(shxx.getStshry())
						||!shrUser.getYhid().equals(shxx.getLrry())){
					throw new BusinessException("ICOM99021","委托无效（委托人为空） 或 受托审核人不是当前用户 或 审核人不是委托人");
				}
			}else{//非委托下的操作
				//最新的审核人不是当前用户，则抛出异常
				if(!user.getYhid().equals(shxx.getLrry())){
					throw new BusinessException("ICOM99021","最新的审核人不是当前用户，则抛出异常");
				}
			}
			//角色切换情况下，判断角色是否相同
			if(GlobalString.IS_ROLE_SWITCH//角色切换情况下
				&&StringUtil.isNotBlank(shxx.getJsid())//审核信息中角色id不为空（为空表示未记录审核角色，故不判断）
				&&!shxx.getJsid().equals(shrUser.getDqjs())) {//如果角色id不相等，则不是当前角色审核的
				throw new BusinessException("ICOM99021","当前角色并非是最后的审核人员,无法取消");
			}
			if(!shxxid.equals(shxx.getShxxid())){//该条信息不是最新的
				throw new BusinessException("ICOM99021","该条记录不是最后的审核记录,无法取消");
			}
			if(YesNotEnum.NOT.getCode().equals(shxx.getSftg())){//退回
				throw new BusinessException("ICOM99021","审核退回的记录无法取消");
			}
			//可以取消的情况下
			ShgcDto shgc = dao.getDtoById(shxx.getGcid());//获得审核过程对象
			//获取审核流程
			List<ShlcDto> shlcList = this.getShlcList(shxx.getShid(), null, shxx.getSqsj());
			int maxLcxh = Integer.parseInt(shlcList.get(shlcList.size()-1).getLcxh());
			//回调业务状态Service
			ShlbModel shlbModel = shlbDao.getDtoById(shxx.getShlb());
			//流程序号是最后一步或者审核过程为null，表示流程已走完
			SpgwcyDto spgwcyDto_;
			if(shgc==null){
				//判断是否能取消
				
				if(StringUtil.isBlank(shlbModel.getQxxk()) || GlobalString.SCBJ_ZERO.equals(shlbModel.getQxxk())){
					throw new BusinessException("ICOM99021","审核通过无法取消！");//不能取消抛出异常
				}
				ShgcDto yw_shgc = dao.getDtoByYwid(shxx.getYwid());//获得审核过程对象
				if(yw_shgc!=null){//过程表中是另一个业务的审核记录
					throw new BusinessException("ICOM99021","该条记录已经处于其他流程中无法取消！");//不能取消抛出异常
				}
				//恢复审核过程记录
				shgc = new ShgcDto();
				shgc.setGcid(shxx.getGcid());
				shgc.setYwid(shxx.getYwid());
				shgc.setShid(shxx.getShid());
				shgc.setSqsj(shxx.getSqsj());
				shgc.setXlcxh(shxx.getLcxh());
				shgc.setShlb(shxx.getShlb());
				SpgwcyDto spgwcyDto = new SpgwcyDto();
				spgwcyDto.setYwid(shxx.getYwid());
				spgwcyDto.setLcxh(shgc.getXlcxh());
				spgwcyDto.setGcid(shgc.getGcid());
				spgwcyDto_ = spgwcyDao.getThByDto(spgwcyDto);
				if (null == spgwcyDto_)
					throw new BusinessException("ICOM99021","审核信息丢失！");
				if ("JX".equals(shxx.getWbgw())){
					shgc.setLxid(spgwcyDto_.getYhid());
					shgc.setLx("SJZG");//上级主管
				}else{
					shgc.setLxid(spgwcyDto_.getGwid());
					shgc.setLx("GW");//上级主管
				}

				if(Integer.parseInt(shgc.getXlcxh())<=0||dao.insert(shgc)!=1){
					throw new BusinessException("ICOM99021","恢复记录的流程序号不正确！");
				}
			}else{//未走完
				//如果当前审核过程中的审核类别和审核信息的审核类别不一样，则抛出异常
				if(!shxx.getShlb().equals(shgc.getShlb())){
					throw new BusinessException("ICOM99021","当前审核过程中的审核类别和审核信息的审核类别不一样");
				}
				//可以取消
				int xlcxh = Integer.parseInt(shgc.getXlcxh())-1;
				shgc.setXlcxh(String.valueOf(xlcxh));//改变现流程序号
				SpgwcyDto spgwcyDto = new SpgwcyDto();
				spgwcyDto.setYwid(shxx.getYwid());
				spgwcyDto.setLcxh(shgc.getXlcxh());
				spgwcyDto.setGcid(shgc.getGcid());
				spgwcyDto_ = spgwcyDao.getThByDto(spgwcyDto);
				if (null == spgwcyDto_)
					throw new BusinessException("ICOM99021","审核信息丢失！");
				if ("JX".equals(shxx.getWbgw())){
					shgc.setLxid(spgwcyDto_.getYhid());
					shgc.setLx("SJZG");//上级主管
				}else{
					shgc.setLxid(spgwcyDto_.getGwid());
					shgc.setLx("GW");//上级主管
				}
				updateCount = dao.update(shgc);//更新审核过程
				if(updateCount!=1||xlcxh<=0){//现流程序号不可能小于0
					throw new BusinessException("ICOM99021","更新流程信息不正确或者现流程序号小于0");
				}
			}
			delList.add(shxx.getShxxid());//添加删除审批信息
			shgc.setAuditType(shxx.getShlb());//放入审核类别
			shgc.setAuditState(AuditStateEnum.AUDITING);//一定是变为审核中
			shgc.setShlbmc(shlbModel.getShlbmc());

			out:if(xtszDto!=null){
				//设置通知的人员
				if(GlobalString.EXTEND_ONE.equals(xtszDto.getSzz()) && (shgc.getXlcxh()==null ||  Integer.parseInt(shgc.getXlcxh())<= maxLcxh)){
					//退回到普通人员
					if(StringUtil.isBlank(shgc.getXlcxh())){
						if(StringUtil.isNotBlank(shgc.getSqrddid())){
							List<SpgwcyDto> spgwcyDtos = new ArrayList<>();
							SpgwcyDto t_tjry = new SpgwcyDto();
							t_tjry.setYhm(shgc.getYhm());
							spgwcyDtos.add(t_tjry);
							shgc.setSpgwcyDtos(spgwcyDtos);
						}
					}else{
						//获取获取当前的审批岗位
						ShlcDto shlcDto = shlcList.get(Integer.parseInt(shgc.getXlcxh())-1);
						SpgwcyDto spgwcyDto = new SpgwcyDto();
						if ("JX".equals(shxx.getWbgw())){
							shgc.setSpgwcyDtos(Collections.singletonList(spgwcyDto_));
							break out;
						}
						spgwcyDto.setGwid(shlcDto.getGwid());
						List<SpgwcyDto> spgwcyDtos = spgwcyDao.getDtoList(spgwcyDto);
						shgc.setSpgwcyDtos(spgwcyDtos);
						//同时通知原有人员不用审核,如果是审核通过的情况，则不用通知任何人
						if(Integer.parseInt(shgc.getXlcxh())+1 <= maxLcxh){
							ShlcDto ht_shlcDto = shlcList.get(Integer.parseInt(shgc.getXlcxh()));
							SpgwcyDto ht_spgwcyDto = new SpgwcyDto();
							ht_spgwcyDto.setGwid(ht_shlcDto.getGwid());
							List<SpgwcyDto> ht_spgwcyDtos = spgwcyDao.getDtoList(ht_spgwcyDto);
							shgc.setNo_spgwcyDtos(ht_spgwcyDtos);
						}
					}
				}
			}
			
			//按类别归类
			List<ShgcDto> shgcList =  shgcMap.get(shgc.getShlb());
			if(shgcList==null){
				shgcList = new ArrayList<>();
				shgcMap.put(shgc.getShlb(),shgcList);
			}
			shgcList.add(shgc);
		}
		
		//删除审核信息 2021-04-13 因为取消审核太多，每次都要查原因，所以要保留审核信息
		/*if(delList.size()>0){
			shxxDao.batchDelete(delList);
		}*/
		
		boolean isUpdated;
		for (String shlb : shgcMap.keySet()) {
			//回调业务状态Service
			ShlbModel shlbModel = shlbDao.getDtoById(shlb);
			
			String serviceName = shlbModel.getHdl();
			IAuditService auditService = (IAuditService)ServiceFactory.getService(serviceName);
			isUpdated = auditService.updateAuditRecall(shgcMap.get(shlb),user,new AuditParam(false,false,true,false));
			//判断执行是否成功
			if(!isUpdated){
				throw new BusinessException("I99031",null);
			}
		}
		
		return true;
	}
	
	/**
	 * 检查批量审核的进度
	 */
	@SuppressWarnings("unchecked")
	public AuditResult checkAuditThreadStatus(ShxxDto shxxDto) {
		//redisUtil.hmset("BATCH_AUDIT:_"+dto.getLoadYmCode(), p_process,3600);
		AuditResult result = new AuditResult();

		Object o_isFinished = redisUtil.hget("BATCH_AUDIT:_"+shxxDto.getLoadYmCode(), "Finished");
		if(o_isFinished!=null) {
			boolean isFinished = (Boolean)o_isFinished;
			result.setFinished(isFinished);
			//未完成，则返回已完成的个数
			if(!isFinished) {
				result.setCnt((int)redisUtil.hget("BATCH_AUDIT:_"+shxxDto.getLoadYmCode(), "CurrentCnt"));
				return result;
			}else {
				result.setType(Integer.parseInt((String)redisUtil.hget("BATCH_AUDIT:_"+shxxDto.getLoadYmCode(), "status")));
				result.setMsg((String)redisUtil.hget("BATCH_AUDIT:_"+shxxDto.getLoadYmCode(), "message"));
				result.setBackMap((Map<String,String>)redisUtil.hget("BATCH_AUDIT:_"+shxxDto.getLoadYmCode(), "backInfo"));
				return result;
			}
		}
		return result;
	}
	
	/**
	 * 审核中途取消
	 */
	public boolean cancelAuditProcess(ShxxDto shxxDto){
		redisUtil.hset("BATCH_AUDIT:_"+shxxDto.getLoadYmCode(), "Cancel",true);
		return true;
	}
	
	/**
	 * 根据审核类别，业务ID，删除审核过程表的数据，同时更新相应业务的状态
	 */
	public String updateAuditRecall(ShgcDto dto, User user) throws BusinessException {
		//审核类别
		if(StringUtil.isBlank(dto.getShlb())){
			throw new BusinessException("ICOM99022","审核类别不存在！");
		}
		//业务ID
		if (dto.getIds()==null || dto.getIds().size() <= 0) {
			throw new BusinessException("ICOM99022","业务信息不存在！");
		}
		List<ShgcDto> shgcList = new ArrayList<>();
		//现流程默认为1
		dto.setXlcxh(GlobalString.AUDIT_STEP_ONE);
		//确认是否可以撤回
		if(isRecallabled(dto)){
			//删除审核过程内的信息
			dao.deleteByYwids(dto.getIds());
			
			List<String> ywidList = dto.getIds();
			for (String s : ywidList) {

				ShgcDto t_shgDto = new ShgcDto();
				t_shgDto.setYwid(s);

				t_shgDto.setShlb(dto.getShlb());

				shgcList.add(t_shgDto);
			}
			
			//回调业务状态Service，更新业务状态
			ShlbModel shlbModel = shlbDao.getDtoById(dto.getShlb());
			
			String serviceName = shlbModel.getHdl();
			
			boolean isUpdated;
			IAuditService auditService = (IAuditService)ServiceFactory.getService(serviceName);
			
			isUpdated = auditService.updateAuditRecall(shgcList,user,new AuditParam(dto.getIds().size()>1,false,false,false));
			
			//判断执行是否成功
			if(!isUpdated){
				throw new BusinessException("ICOM99022","业务更新未成功！");
			}
		}else{
			throw new BusinessException("ICOM99022","信息无法撤回！");
		}
		
		return null;
	}
	
	/**
	 * 判断参数业务ID是否都允许撤回
	 */
	private boolean isRecallabled(ShgcDto shgcDto) {
		
		List<ShgcDto> shgcDtos = dao.getRecallabledList(shgcDto);

		return shgcDtos.size() == shgcDto.getIds().size();
	}

	/**
	 * 根据ywids删除审核过程信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteByYwids(List<String> ids) {
		// TODO Auto-generated method stub
		int result = dao.deleteByYwids(ids);
		return result != 0;
	}

	/**
	 * 获取审核历史信息审批岗位成员所需的机构信息，岗位id，审核类别等
	 */
	@Override
	public Map<String, Object> dealSpgwcy(ShgcDto shgcDto) {
		try {
			//获取回调类
			ShlbModel shlbModel = shlbDao.getDtoById(shgcDto.getShlb());
			String serviceName = shlbModel.getHdl();
			IAuditService auditService = (IAuditService)ServiceFactory.getService(serviceName);

			return auditService.requirePreAuditMember(shgcDto);
		} catch (BusinessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 批量新增审核过程信息
	 */
	public boolean insertDtoList(List<ShgcDto> shgcDtos){
		return dao.insertDtoList(shgcDtos);
	}

	/**
	 * 定时任务删除审核过程信息
	 */
	public boolean scheduledDeleteData(ShgcDto shgcDto){
		return dao.scheduledDeleteData(shgcDto);
	}

	/**
	 * 定时任务-获取申请时间超过三个月的审核过程信息
	 */
	public List<ShgcDto> getOverThreeMonthsData(ShgcDto shgcDto){
		return dao.getOverThreeMonthsData(shgcDto);
	}

	/**
	 * 定时任务清理数据
	 */
	public void scheduledDeleteData(){
		//1. 删除系统审核中该审核id 已经被删除的 记录
		//2. 申请日期超过半年以上，xlcxh 为空的删除记录
		//2. 申请日期超过一年以上，xlcxh 不为空的删除记录
		ShgcDto shgcDto=new ShgcDto();
		scheduledDeleteData(shgcDto);
		//4.获取申请日期超过三个月的数据 相应业务ID的删除标记为1 的记录进行删除
		List<ShgcDto> overThreeMonthsData = getOverThreeMonthsData(shgcDto);
		if(!CollectionUtils.isEmpty(overThreeMonthsData)){
			List<String> ids=new ArrayList<>();//用于存放验证过应该删除的ywid
			for(ShgcDto dto:overThreeMonthsData){
				if(StringUtil.isNotBlank(dto.getYwid())&&StringUtil.isNotBlank(dto.getHdl())){
					List<String> ywids = Arrays.asList(dto.getYwid().split(","));
					Map<String,Object> param=new HashMap<>();
					param.put("ywids",ywids);
					Object serviceInstance = ServiceFactory.getService(dto.getHdl());//获取方法所在class
					Method method;//获取执行方法
					try {
						method = serviceInstance.getClass().getMethod("returnAuditServiceInfo",Map.class);
						//执行方法
						@SuppressWarnings("unchecked")
						Map<String,Object> map = (Map<String,Object>)method.invoke(serviceInstance,param);
						//返回回来的都是scbj=0的数据，所以还需要执行循环验证
						@SuppressWarnings("unchecked")
						List<String> list = (List<String>)map.get("list");
						if(!CollectionUtils.isEmpty(list)){
							for(String ywid:ywids){
								boolean isFind=false;
								for(String id:list){
									if(ywid.equals(id)){
										isFind=true;
										break;
									}
								}
								if(!isFind){
									ids.add(ywid);
								}
							}
						}
					} catch (Exception e) {
						logger.error("定时任务删除审核过程数据出错："+e.getMessage());
					}
				}
			}
			if(!CollectionUtils.isEmpty(ids)){
				deleteByYwids(ids);
			}
		}
	}

	@Override
	public List<Map<String, Object>> getAuditTaskWaitingCount(ShgcDto shgcDto) {
		return dao.getAuditTaskWaitingCount(shgcDto);
	}

	@Override
	public List<ShgcDto> getPagedAuditTaskWaitingList(ShgcDto shgcDto) {
		return dao.getPagedAuditTaskWaitingList(shgcDto);
	}
}
