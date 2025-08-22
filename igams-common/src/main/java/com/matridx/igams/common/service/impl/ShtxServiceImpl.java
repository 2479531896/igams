package com.matridx.igams.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.matridx.igams.common.dao.entities.ShtxDto;
import com.matridx.igams.common.dao.entities.ShtxModel;
import com.matridx.igams.common.dao.post.IShtxDao;
import com.matridx.igams.common.service.svcinterface.IShtxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;

@Service
public class ShtxServiceImpl extends BaseBasicServiceImpl<ShtxDto, ShtxModel, IShtxDao> implements IShtxService{
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.systemflg.systemname:}")
	private String systemname;
	/**
	 * 插入一条审核提醒信息
	 */
	@Override
	public boolean insertshtx(ShtxDto shtxDto) {
		// TODO Auto-generated method stub
		return dao.insertshtx(shtxDto);
	}
	
	/**
	 * 定时提醒
	 */
	public boolean spyqRemind() {
		String ICOMM_SH00051 = xxglService.getMsg("ICOMM_SH00051");
		String ICOMM_SH00050 = xxglService.getMsg("ICOMM_SH00050");
		//ShtxDto shtxDto = new ShtxDto();
		//找到允许审核提醒的审核lb，即查找审核提醒列表所有数据组装一个类别的List
		List<ShtxDto> txlbList = dao.getShtxlb();
		//下面为发送钉钉cardMessage消息做准备
//		String token = talkUtil.getToken();
		//遍历所有的类别，查找需要发送钉钉消息的内容，审核类别，待审核延期的个数，部分审核要特殊处理		
		for(ShtxDto shtxdto:txlbList) {
			List<ShtxDto> msgxxnrList;
			if("AUDIT_REQUISITIONS".equals(shtxdto.getTxlb())) {//物料请购单独处理
				msgxxnrList = dao.getWlqgMgsToSend(shtxdto);				
			}else if("AUDIT_REQUISITIONS_CANCEL".equals(shtxdto.getTxlb())){//取消物料请购审核
				msgxxnrList = dao.getQxqgMgsnrToSend(shtxdto);
			}else if("AUDIT_CONTRACT".equals(shtxdto.getTxlb())){//合同审核
				msgxxnrList = dao.getHtshMgsnrToSend(shtxdto);
			}else if("AUDIT_RECHECK".equals(shtxdto.getTxlb()) || 
					"AUDIT_DING_RECHECK".equals(shtxdto.getTxlb())||
					"AUDIT_OUT_RECHECK".equals(shtxdto.getTxlb())){//复检审核，外部复检审核，钉钉复检审核
				msgxxnrList = dao.getFjshMgsnrToSend(shtxdto);
			}else if("AUDIT_VERIFICATION".equals(shtxdto.getTxlb())){//PCR送检验证
				msgxxnrList = dao.getYzshMgsnrToSend(shtxdto);
			}else if("AUDIT_GOODS_ARRIVAL".equals(shtxdto.getTxlb())){//货物新增审核
				msgxxnrList = dao.getHwxzshMgsnrToSend(shtxdto);
			}else if("AUDIT_GOODS_APPLY".equals(shtxdto.getTxlb()) ||
					"AUDIT_GOODS_APPLY_DING".equals(shtxdto.getTxlb())){//领料申请审核
				msgxxnrList = dao.getLlsqshMgsnrToSend(shtxdto);
			}
			else {
				msgxxnrList = dao.getMgsnrToSend(shtxdto);
			}
			for(ShtxDto shtxdtoM:msgxxnrList) {
				if(StringUtil.isNotBlank(shtxdtoM.getDdid())) {
//===================分别组装内外网的访问路径============
					//内网访问 &：%26        =：%3D  ?:%3F
					String internalbtn = applicationurl + urlPrefix + "/common/view/displayView?view_url=/ws/audit/spyqListdd%3Ftxlb%3D"+shtxdto.getTxlb()+"%26yhid%3D"+shtxdtoM.getYhid()+"%26dqshjs%3D"+shtxdtoM.getDqshjs();
					//外网访问
					//String external=externalurl + urlPrefix +  "/common/view/displayView?view_url=/ws/audit/spyqListdd%3Ftxlb%3D"+shtxdto.getTxlb()+"%26yhid%3D"+shtxdtoM.getYhid()+"%26dqshjs%3D"+shtxdtoM.getDqshjs();
					List<BtnJsonList> btnJsonLists = new ArrayList<>();
					BtnJsonList btnJsonList = new BtnJsonList();
					btnJsonList.setTitle("详细");
					btnJsonList.setActionUrl(internalbtn);
					btnJsonLists.add(btnJsonList);
					/*btnJsonList = new BtnJsonList();
					btnJsonList.setTitle("外网访问");
					btnJsonList.setActionUrl(external);
					btnJsonLists.add(btnJsonList);*/
//====================发送钉钉消息=================================
					talkUtil.sendCardMessage(shtxdtoM.getYhm(),
							shtxdtoM.getDdid(), 
							ICOMM_SH00051,
							StringUtil.replaceMsg(ICOMM_SH00050,
									systemname,
									shtxdtoM.getTxlbmc(),
									shtxdtoM.getTasknum(),
									DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH")),
							btnJsonLists, 
							"1");
				}
			}
		}
		return true;		
	}

	@Override
	public List<ShtxDto> getAllAuditings(ShtxDto shtxDto) {
		return dao.getAllAuditings(shtxDto);
	}


	/**
	 * 全部和个人审批列表数据显示
	 */
	@Override
	public Map<String,Object> getPagedSpyq(ShtxDto shtxDto) {
		Map<String,List<ShtxDto>> shlbmap = new HashMap<>();
		Map<String,Object> mapResult = new HashMap<>();
		List<ShtxDto> spyqList = dao.getPagedSpyq(shtxDto);//得到所有数据，但此时数据中业务名称为空
		spyqList = shlbYwmcDeal(shlbmap, spyqList);
		mapResult.put("rows",spyqList);
		return mapResult;
	}

	/**处理空缺业务名称的List，将List中的业务名称填补上，将所有延期数据按照审核类别分类存入map
	 */
	public List<ShtxDto> shlbYwmcDeal(Map<String, List<ShtxDto>> shlbmap, List<ShtxDto> spyqList) {
		List<ShtxDto> lb_list;
		//处理业务名称，按照类别存放到不同list，然后list再存入map保存
		shlbmap = shlbMapDeal(shlbmap, spyqList);
		//遍历map的key和value，该方法一次遍历拿出key和value
		for(Entry<String, List<ShtxDto>> item : shlbmap.entrySet()) {
			switch (item.getKey()) {
				case "AUDIT_REQUISITIONS": {//物料请购审核
					lb_list = item.getValue();
					List<ShtxDto> ywmcList = dao.WlqgshYwmcList(lb_list);//得到物料请购审核业务名称 （业务名称为djh

					for (ShtxDto shtxdtoYw : ywmcList) {
						spyqList.get(shtxdtoYw.getLsxh()).setYwmc(shtxdtoYw.getYwmc());
					}
					break;
				}
				case "AUDIT_REQUISITIONS_CANCEL": {//取消请购审核
					lb_list = item.getValue();
					List<ShtxDto> ywmcList = dao.QxqgshYwmcList(lb_list);//得到取消请购审核业务名称（业务名称为djh

					for (ShtxDto shtxdtoYw : ywmcList) {
						spyqList.get(shtxdtoYw.getLsxh()).setYwmc(shtxdtoYw.getYwmc());
					}
					break;
				}
				case "AUDIT_METRIEL": {//物料申请
					lb_list = item.getValue();
					List<ShtxDto> ywmcList = dao.WlsqYwmcList(lb_list);//得到物料申请业务名称（业务名称为wlmc

					for (ShtxDto shtxdtoYw : ywmcList) {
						spyqList.get(shtxdtoYw.getLsxh()).setYwmc(shtxdtoYw.getYwmc());
					}
					break;
				}
				case "AUDIT_METRIELMOD": {//物料修改审核
					lb_list = item.getValue();
					List<ShtxDto> ywmcList = dao.WlxgshYwmcList(lb_list);//得到物料修改审核业务名称（业务名称为wlmc

					for (ShtxDto shtxdtoYw : ywmcList) {
						spyqList.get(shtxdtoYw.getLsxh()).setYwmc(shtxdtoYw.getYwmc());
					}
					break;
				}
				case "QUALITY_FILE_ADD":
				case "QUALITY_FILE_MOD": {//文件申请    或者  文件修订申请
					lb_list = item.getValue();
					List<ShtxDto> ywmcList = dao.WjsqYwmcList(lb_list);//得到文件申请业务名称（业务名称为wjmc

					for (ShtxDto shtxdtoYw : ywmcList) {
						spyqList.get(shtxdtoYw.getLsxh()).setYwmc(shtxdtoYw.getYwmc());
					}
					break;
				}
				case "AUDIT_GOODS_ARRIVAL": {//货物到货审核（！！！这部分正在新做，目前我的数据被存到hwxx表中，没有dhdh）
					lb_list = item.getValue();
					List<ShtxDto> ywmcList = dao.HwdhshYwmcList(lb_list);//得到货物到货审核业务名称（业务名称为dhdh

					for (ShtxDto shtxdtoYw : ywmcList) {
						spyqList.get(shtxdtoYw.getLsxh()).setYwmc(shtxdtoYw.getYwmc());
					}
					break;
				}
				case "AUDIT_CONTRACT": {//合同审核  ...
					lb_list = item.getValue();
					List<ShtxDto> ywmcList = dao.HtshYwmcList(lb_list);//得到文件申请业务名称（业务名称为wjmc

					for (ShtxDto shtxdtoYw : ywmcList) {
						spyqList.get(shtxdtoYw.getLsxh()).setYwmc(shtxdtoYw.getYwmc());
					}
					break;
				}
				case "AUDIT_DEVICEINFO": {//设备审核  ...
					lb_list = item.getValue();
					List<ShtxDto> ywmcList = dao.SbshYwmcList(lb_list);//得到设备审核业务名称（业务名称为sbh

					for (ShtxDto shtxdtoYw : ywmcList) {
						spyqList.get(shtxdtoYw.getLsxh()).setYwmc(shtxdtoYw.getYwmc());
					}
					break;
				}
				case "AUDIT_GOODS_APPLY":
				case "AUDIT_GOODS_APPLY_DING": {//领料申请审核  ...
					lb_list = item.getValue();
					List<ShtxDto> ywmcList = dao.LlsqshYwmcList(lb_list);//得到领料申请审核业务名称（业务名称为lldh

					for (ShtxDto shtxdtoYw : ywmcList) {
						spyqList.get(shtxdtoYw.getLsxh()).setYwmc(shtxdtoYw.getYwmc());
					}
					break;
				}
				case "AUDIT_GOODS_REAGENT":
				case "AUDIT_GOODS_INSTRUMENT":
				case "AUDIT_GOODS_THORGH": {//试剂质检和仪器质检和退货  ...
					lb_list = item.getValue();
					List<ShtxDto> ywmcList = dao.DhjyYwmcList(lb_list);//得到到货检验业务名称（业务名称为jydh

					for (ShtxDto shtxdtoYw : ywmcList) {
						spyqList.get(shtxdtoYw.getLsxh()).setYwmc(shtxdtoYw.getYwmc());
					}
					break;
				}
				case "AUDIT_GOODS_STORAGE": {//入库审核  ...
					lb_list = item.getValue();
					List<ShtxDto> ywmcList = dao.RkshYwmcList(lb_list);//得到入库审核业务名称（业务名称为jydh

					for (ShtxDto shtxdtoYw : ywmcList) {
						spyqList.get(shtxdtoYw.getLsxh()).setYwmc(shtxdtoYw.getYwmc());
					}
					break;
				}
				case "AUDIT_VERIFICATION": {//送检验证审核  ...
					lb_list = item.getValue();
					List<ShtxDto> ywmcList = dao.SjyzshYwmcList(lb_list);//得到验证审核业务名称（业务名称为：样本编号--验证类别

					for (ShtxDto shtxdtoYw : ywmcList) {
						spyqList.get(shtxdtoYw.getLsxh()).setYwmc(shtxdtoYw.getYwmc());
					}
					break;
				}
				case "AUDIT_RECHECK":
				case "AUDIT_DING_RECHECK":
				case "AUDIT_OUT_RECHECK": {//复检审核，外部复检审核，钉钉复检审核
					lb_list = item.getValue();
					List<ShtxDto> ywmcList = dao.FjshYwmcList(lb_list);//得到验证审核业务名称（业务名称为：样本编号--验证类别

					for (ShtxDto shtxdtoYw : ywmcList) {
						spyqList.get(shtxdtoYw.getLsxh()).setYwmc(shtxdtoYw.getYwmc());
					}
					break;
				}
			}
		}
		return spyqList;
	}

	/**
	 * 处理业务名称，按照类别存放到不同list，然后list再存入map保存
	 */
	public Map<String, List<ShtxDto>> shlbMapDeal(Map<String, List<ShtxDto>> shlbmap, List<ShtxDto> spyqList) {
		int index =0;
		for(ShtxDto shtxdto: spyqList) {
			shtxdto.setLsxh(index++);
			String txlb = shtxdto.getTxlb();
			if(shlbmap.get(txlb)==null)
			{
				List<ShtxDto> t_list = new ArrayList<>();
				t_list.add(shtxdto);
				shlbmap.put(txlb, t_list);
			}else {
				shlbmap.get(txlb).add(shtxdto);
			}		
		}
		return shlbmap;
	}

	/**
	 * 得到审批延期中选中的一条数据信息
	 */
	@Override
	public ShtxDto getShyqxxByShid(ShtxDto shtxDto) {
		return dao.getShyqxxByShid(shtxDto);
	}

	/**
	 * 定时任务显示个人延期任务列表
	 */
	@Override
	public Map<String, Object> getSpyqDdviewList(ShtxDto shtxDto) {
		Map<String,List<ShtxDto>> shlbmap = new HashMap<>();
		Map<String,Object> mapResult = new HashMap<>();
		List<ShtxDto> spyqDDList = dao.getSpyqDdview(shtxDto);//得到所有数据，但此时数据中业务名称为空
		spyqDDList = shlbYwmcDeal(shlbmap, spyqDDList);
		mapResult.put("rows",spyqDDList);
		return mapResult;
	}

	/**
	 * 显示个人审批延期列表
	 */
	@Override
	public Map<String, Object> getPagedPersonSpyq(ShtxDto shtxDto,String txlbs) {
		Map<String,List<ShtxDto>> shlbmap = new HashMap<>();
		Map<String,Object> mapResult = new HashMap<>();
		shtxDto.setTxlb(txlbs);
		shtxDto.setDqsxlb(txlbs);
		//个人审批列表增加审批岗位单位限制，并且要根据业务过滤掉业务scbj=1的数据，所以需要对部分提醒类别特殊处理
		List<ShtxDto> spyqList;
//		if(StringUtil.isNotBlank(shtxDto.getDqsxlb())) {
//			spyqList = dao.getPagedPersonSpyq(shtxDto);
//			
//		}else {
			spyqList = dao.getPagedPersonSpyq(shtxDto);
//		}
		spyqList = shlbYwmcDeal(shlbmap, spyqList);
		mapResult.put("rows",spyqList);
		return mapResult;
	}

	/**
	 * 查找提醒类别
	 */
	@Override
	public List<ShtxDto> getTxlbList() {
		// TODO Auto-generated method stub
		return dao.getTxlbList();
	}

}
