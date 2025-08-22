package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.PayTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.dao.entities.PayinfoDto;
import com.matridx.igams.wechat.dao.entities.PayinfoModel;
import com.matridx.igams.wechat.dao.post.IPayinfoDao;
import com.matridx.igams.wechat.service.svcinterface.IPayinfoService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class PayinfoServiceImpl extends BaseBasicServiceImpl<PayinfoDto, PayinfoModel, IPayinfoDao> implements IPayinfoService{

	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	private DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IJcsjService jcsjService;
	
	/**
	 * 新增送检信息
	 * @param payinfoDto
	 * @throws BusinessException 
	 */
	@Override
	public void addPayinfo(PayinfoDto payinfoDto) throws BusinessException {
		int insert = dao.insert(payinfoDto);
		if(insert == 0)
			throw new BusinessException("","新增送检信息未成功！");
	}

	/**
	 * 同步送检信息
	 * @param payinfoDto
	 * @throws BusinessException 
	 */
	@Override
	public void editPayinfo(PayinfoDto payinfoDto) throws BusinessException {
		PayinfoDto t_payinfoDto = dao.getDto(payinfoDto);
		if(t_payinfoDto == null) {
			addPayinfo(payinfoDto);
		}else {
			//若原有结果已是1，且新传入支付结果为0(表明rabbit顺序表错误)
			if ("1".equals(t_payinfoDto.getJg()) && "0".equals(payinfoDto.getJg())){
				payinfoDto.setJg("1");
			}
			updatePayinfo(payinfoDto);
		}
	}

	/**
	 * 修改送检信息
	 * @param payinfoDto
	 * @throws BusinessException 
	 */
	private void updatePayinfo(PayinfoDto payinfoDto) throws BusinessException {
		int result = dao.update(payinfoDto);
		if(result == 0)
			throw new BusinessException("","修改送检信息未成功！");
	}

	/**
	 * 根据业务信息查询支付信息
	 * @param ywid
	 * @param ywlx
	 * @return
	 */
	@Override
	public List<PayinfoDto> selectByYwxx(String ywid, String ywlx) {
		// TODO Auto-generated method stub
		PayinfoDto payinfoDto = new PayinfoDto();
		payinfoDto.setYwid(ywid);
		payinfoDto.setYwlx(ywlx);
		payinfoDto.setDylx(PayTypeEnum.PAY.getCode());
        return dao.selectByYwxx(payinfoDto);
	}

	/**
	 * 支付成功通知
	 * @param payinfoDto
	 */
	@Override
	public void paySuccessMessage(PayinfoDto payinfoDto) {
		// TODO Auto-generated method stub
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb("DINGMESSAGETYPE");
		jcsjDto.setCsdm("PAY_SUCCESS_TYPE");
		List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());

		if(ddxxglDtos != null && ddxxglDtos.size() > 0) {
	    	// 判断支付方式
	    	String zffs = null;
	    	if("ZF".equals(payinfoDto.getZffs()))
	    		zffs = "支付宝";
	    	if("WX".equals(payinfoDto.getZffs()))
	    		zffs = "微信";
	    	if("YL".equals(payinfoDto.getZffs()))
	    		zffs = "银联";
			
	    	// 支付金额
			String zfje = String.valueOf(new BigDecimal(payinfoDto.getJyje()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
			
//	    	String token = talkUtil.getToken();
	    	String title = xxglService.getMsg("ICOMM_ZF00002");
			String message = xxglService.getMsg("ICOMM_ZF00001", payinfoDto.getYbbh(), payinfoDto.getHzxm(), payinfoDto.getSjhb(), zffs, zfje+"(元)", payinfoDto.getJysj());
	    	for (int i = 0; i < ddxxglDtos.size(); i++) {
	    		talkUtil.sendWorkMessage(ddxxglDtos.get(i).getYhm(), ddxxglDtos.get(i).getDdid(), title, message);
			}
		}
	}

	/**
	 * 支付列表查询某条支付信息
	 */
	@Override
	public PayinfoDto getDtoPayment(PayinfoDto payinfoDto) {
		// TODO Auto-generated method stub
		return dao.getDtoPayment(payinfoDto);
	}

	@Override
	public PayinfoDto selectPayYuan(PayinfoDto payinfoDto) {
		return dao.selectPayYuan(payinfoDto);
	}

	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String, Object> params, PayinfoDto payinfoDto)
	{
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuffer sqlParam = new StringBuffer();
		for (DcszDto dcszDto : choseList)
		{
			if (dcszDto == null || dcszDto.getDczd() == null)
				continue;
			if(dcszDto.getDczd().equalsIgnoreCase("YBBH") || dcszDto.getDczd().equalsIgnoreCase("NBBM")|| dcszDto.getDczd().equalsIgnoreCase("HZXM")) {
				payinfoDto.setSjxx_flg("Y");
			}
			
			
			sqlParam.append(",");
			if (StringUtil.isNotBlank(dcszDto.getSqlzd()))
			{
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		payinfoDto.setSqlParam(sqlParam.toString());
	}


	/**
	 * 选中导出
	 * @param params
	 * @return
	 */
	public List<PayinfoDto> getListForSelectExp(Map<String, Object> params){
		PayinfoDto payinfoDto = (PayinfoDto) params.get("entryData");
		queryJoinFlagExport(params,payinfoDto);
        return dao.getListForSelectExp(payinfoDto);
	}
	
	/**
	 * 根据搜索条件获取导出条数
	 * @param params
	 * @return
	 */
	public int getCountForSearchExp(PayinfoDto payinfoDto,Map<String,Object> params){
		return dao.getCountForSearchExp(payinfoDto);
	}
	
	/**
	 * 根据搜索条件分页获取导出信息
	 * @param params
	 * @return
	 */
	public List<PayinfoDto> getListForSearchExp(Map<String,Object> params){
		PayinfoDto payinfoDto = (PayinfoDto)params.get("entryData");
		queryJoinFlagExport(params,payinfoDto);
		return dao.getListForSearchExp(payinfoDto);
	}
}
