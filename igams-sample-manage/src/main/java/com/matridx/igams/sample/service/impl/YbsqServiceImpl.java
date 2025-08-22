package com.matridx.igams.sample.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matridx.igams.sample.dao.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.sample.dao.post.IYbglDao;
import com.matridx.igams.sample.dao.post.IYbsqDao;
import com.matridx.igams.sample.service.svcinterface.ISbkxglService;
import com.matridx.igams.sample.service.svcinterface.IYbsqService;
import com.matridx.igams.sample.service.svcinterface.IYbsqssdwService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.springframework.util.CollectionUtils;

@Service
public class YbsqServiceImpl extends BaseBasicServiceImpl<YbsqDto, YbsqModel, IYbsqDao> implements IYbsqService,IAuditService{

	private final Logger logger = LoggerFactory.getLogger(YbsqServiceImpl.class);
	@Autowired
	IShgcService shgcService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ISbkxglService sbkxglService;
	@Autowired
	ICommonService commonservice;
	@Autowired
	IYbglDao ybglDao;
	@Autowired
	IXxglService xxglService;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IYbsqssdwService ybsqssdwService;
	@Autowired
	ICommonService commonService;
	
	/**
	 * 根据标本ID获取标本信息，用于标本申请用
	 */
	public YbsqDto getYbDto(YbsqModel ybsqModel){
		return dao.getYbDto(ybsqModel);
	}
	
	@Override
	public boolean insert(YbsqModel ybsqModel){

		//设置申请ID
		String sqid = StringUtil.generateUUID();
		
		ybsqModel.setSqid(sqid);
		
		ybsqModel.setZt(StatusEnum.CHECK_NO.getCode());
		
		int result = dao.insert(ybsqModel);
		
		return result > 0;
	}
	
	@Override 
	public List<YbsqDto> getPagedDtoList(YbsqDto ybsqDto){
		//获取审核信息
		try {
			List<YbsqDto> t_List = dao.getPagedDtoList(ybsqDto);
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_SAMPAPPLY.getCode(), "zt", "sqid", new String[]{
				StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()
			});
			jcsjService.handleCodeToValue(t_List,
					new BasicDataTypeEnum[] {
							BasicDataTypeEnum.SAMPLE_TYPE,
							BasicDataTypeEnum.SAMPLE_USE}, new String[] {
					"yblx:yblxmc", "yt:ytmc"});
			return t_List;
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 标本申请审核列表
	 */
	public List<YbsqDto> getPagedAuditList(YbsqDto ybsqDto){
		//获取人员ID和履历号
		List<YbsqDto> t_sqList = dao.getPagedAuditIdList(ybsqDto);
		
		if(t_sqList == null || t_sqList.isEmpty())
			return t_sqList;
		
		List<YbsqDto> sqList = dao.getAuditListByIds(t_sqList);
		
		commonservice.setSqrxm(sqList);
		
		jcsjService.handleCodeToValue(sqList,
				new BasicDataTypeEnum[] {
						BasicDataTypeEnum.SAMPLE_TYPE,
						BasicDataTypeEnum.SAMPLE_USE}, new String[] {
				"yblx:yblxmc", "yt:ytmc"});
		
		return sqList;
	}

	@Override
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}
		
//		String token = talkUtil.getToken();
		
		for(ShgcDto shgcDto : shgcList){
			YbsqDto ybsqDto = new YbsqDto();
			YbglDto ybglDto = new YbglDto();
			
			ybsqDto.setSqid(shgcDto.getYwid());
			//获取标本申请信息，主要获取标本ID和申请数，用于后面计算
			YbsqDto tYbsqDto = dao.getDto(ybsqDto);
			if(tYbsqDto == null)
				throw new BusinessException("","无法找到标本申请信息");
			
			ybsqDto.setXgry(operator.getYhid());
			//设置标本ID
			ybglDto.setYbid(tYbsqDto.getYbid());
			//审核退回
			if(AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())){
				ybglDto.setYds("-" + tYbsqDto.getSqs());
				ybglDao.updateYdsBySq(ybglDto);

				ybsqDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				//审核通过
			}else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())){
				//获取标本信息，用于计算相应的冰箱号 和位置
				YbglDto tYbglDto = ybglDao.getDto(ybglDto);
				//确认标本库存是否足够
				int i_kc = Integer.parseInt(tYbglDto.getSl());
				int i_sqs = Integer.parseInt(tYbsqDto.getSqs());
				if(i_kc < i_sqs)
					throw new BusinessException("","库存数不足。");
				else if(i_kc == i_sqs){
					//当库存数和申请数一样的情况，判断为该标本已使用完毕，则进行清除
					ybglDto.setScbj("2");
				}
				
				ybglDto.setYds("-" + tYbsqDto.getSqs());
				ybglDto.setSl(tYbsqDto.getSqs());
				ybglDao.updateYdsBySq(ybglDto);
				//组装相应 位置信息
				StringBuilder mString = new StringBuilder();
				List<String> wzList = calcWzList(tYbglDto,tYbsqDto.getSqs());
				
				mString.append("请从冰箱号：<span style='color:red'>");
				mString.append(tYbglDto.getBxh());
				mString.append("</span> <br/>&nbsp;&nbsp;&nbsp;&nbsp;抽屉号：<span style='color:red'>");
				mString.append(tYbglDto.getChth());
				mString.append("</span> <br/>&nbsp;&nbsp;&nbsp;&nbsp;盒子号：<span style='color:red'>");
				mString.append(tYbglDto.getHzh());
				mString.append("</span> <br/>&nbsp;&nbsp;&nbsp;&nbsp;的位置 : <span style='color:red'>");
				
				if(wzList.size() < 2)
				{
					mString.append(wzList.get(0));
				}else{
					mString.append(wzList.get(0));
					mString.append("--");
					mString.append(wzList.get(wzList.size()-1));
				}
				mString.append("</span><br/>取<span style='color:red'>");
				mString.append(wzList.size());
				mString.append("</span>件标本，谢谢！");
				if(auditParam.getSucMap() ==null){
					Map<String, String> map = new HashMap<>();
					map.put(shgcDto.getYwid(), mString.toString());
					auditParam.setSucMap(map);
				}else
					auditParam.getSucMap().put(shgcDto.getYwid(), mString.toString());
				
				ybsqDto.setZt(StatusEnum.CHECK_PASS.getCode());
				//设置为非空，用于map判断
				ybsqDto.setShsj(operator.getYhid());
				ybsqDto.setHzid(tYbglDto.getHzid());
				ybsqDto.setQswz(wzList.get(0));
				ybsqDto.setJswz(wzList.get(wzList.size()-1));
				
				//同时更新标本空闲表
				sbkxglService.recountSbkx(tYbglDto.getHzid());
				//审核中
			}else{
				ybsqDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				//提交的时候，进行更新标本管理表，增加预定数，其他审核中的时候不做任何处理
				if(GlobalString.AUDIT_STEP_ONE.equals(shgcDto.getXlcxh())){
					//更新预定数
					ybglDto.setYds(tYbsqDto.getSqs());
					ybglDao.updateYdsBySq(ybglDto);
					
					ybsqDto.setSqry(operator.getYhid());
					//设置为非空，用于map判断
					ybsqDto.setSqsj(operator.getYhid());
				}
				
				//发送钉钉消息--审核人员
				if(shgcDto.getSpgwcyDtos() != null && !shgcDto.getSpgwcyDtos().isEmpty()){
					try{
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
										shgcDto.getSpgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00003"),xxglService.getMsg("ICOMM_SH00001",operator.getZsxm(),shgcDto.getShlbmc() ,tYbsqDto.getYbbh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						logger.error(e.getMessage());
					}
				}
				//发送钉钉消息--取消审核人员
				if(shgcDto.getNo_spgwcyDtos() != null && !shgcDto.getNo_spgwcyDtos().isEmpty()){
					try{
						int size = shgcDto.getNo_spgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
										shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,tYbsqDto.getYbbh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						logger.error(e.getMessage());
					}
				}
			}
			//更新状态
			dao.updateZt(ybsqDto);
		}
		return true;
	}

	@Override
	public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
		// TODO Auto-generated method stub
		YbsqDto ybsqDto = (YbsqDto)baseModel;
		ybsqDto.setXgry(operator.getYhid());
        return update(ybsqDto);
	}
	
	/**
	 * 根据标本信息以及申请数，获取相应提取的位置信息
	 */
	private List<String> calcWzList(YbglDto ybglDto,String sqs){
		List<String> result = new ArrayList<>();
		//数量
		int i_sl = Integer.parseInt(ybglDto.getSl());
		//起始位置
		int i_qswz = Integer.parseInt(ybglDto.getQswz());
		//存放数
		int i_cfs = Integer.parseInt(ybglDto.getCfs());
		//申请数
		int i_sqs = Integer.parseInt(sqs);
		//每行个数
		double n =Math.sqrt(i_cfs);
		//根据申请数获取相应的位置
		for(int i = (i_qswz + i_sl - i_sqs);i<= (i_qswz + i_sl - 1);i++){
			result.add(calcWz(i,n));
		}
		
		return result;
	}
	
	/**
	 * 输入数字获取相应的字符位置
	 */
	private String calcWz(int i_start,double n){
		//计算当前位置的纵向偏移量
		double z_py = (i_start-1)/n+1;
		//计算当前位置的横向偏移量
		int h_py = (int)((i_start-1)%n+1);
		char c1=(char) (z_py+64);
		
		return String.valueOf(c1)+ h_py;
	}

	/**
	 * 标本申请保存
	 */
	@Override
	public boolean addSaveSampApply(YbsqDto ybsqDto) {
		// TODO Auto-generated method stub
		boolean isSuccess = insert(ybsqDto);
		if(!isSuccess)
			return false;
		YbsqssdwDto ybsqssdwDto = new YbsqssdwDto();
		ybsqssdwDto.setSqid(ybsqDto.getSqid());
		ybsqssdwDto.setXh("1");
		ybsqssdwDto.setJgid(ybsqDto.getJgid());
		isSuccess = ybsqssdwService.insert(ybsqssdwDto);
        return isSuccess;
    }

	/**
	 * 标本修改保存
	 */
	@Override
	public boolean modSaveSampApply(YbsqDto ybsqDto) {
		// TODO Auto-generated method stub
		boolean isSuccess = update(ybsqDto);
		if(!isSuccess)
			return false;
		YbsqssdwDto ybsqssdwDto = new YbsqssdwDto();
		ybsqssdwDto.setSqid(ybsqDto.getSqid());
		ybsqssdwService.delete(ybsqssdwDto);
		ybsqssdwDto.setXh("1");
		ybsqssdwDto.setJgid(ybsqDto.getJgid());
		isSuccess = ybsqssdwService.insert(ybsqssdwDto);
        return isSuccess;
    }

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
		return null;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
		Map<String, Object> map =new HashMap<>();
		List<String> ids = (List<String>)param.get("ywids");
		YbsqDto ybsqDto = new YbsqDto();
		ybsqDto.setIds(ids);
		List<YbsqDto> dtoList = dao.getDtoList(ybsqDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(YbsqDto dto:dtoList){
				list.add(dto.getSqid());
			}
		}
		map.put("list",list);
		return map;
	}
}
