package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.MQTypeEnum_pub;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.production.service.svcinterface.IWjglService;
import com.matridx.igams.storehouse.dao.entities.KhglDto;
import com.matridx.igams.storehouse.dao.entities.KhglModel;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.storehouse.dao.post.IKhglxxDao;
import com.matridx.igams.storehouse.service.svcinterface.IKhglxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class KhglxxServiceImpl extends BaseBasicServiceImpl<KhglDto, KhglModel, IKhglxxDao> implements IKhglxxService {
	@Autowired
	IRdRecordService iRdRecordService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IWjglService wjglService;
	@Autowired(required=false)
	private AmqpTemplate amqpTempl;
	/**
	 * 文档转换完成OK
	 */
	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private final String DOC_OK = null;
    /**
	 * 插入客户管理信息
	 * @param khglDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertKhglxx(KhglDto khglDto) throws BusinessException {
        khglDto.setKhid(StringUtil.generateUUID());
		if(!CollectionUtils.isEmpty(khglDto.getFjids())) {
			for (int i = 0;i<khglDto.getFjids().size();i++) {

				boolean saveFile = fjcfbService.save2RealFile(khglDto.getFjids().get(i), khglDto.getKhid());
				if(!saveFile)
					throw new BusinessException("msg","附件保存失败!");
				FjcfbDto fjcfbDto=new FjcfbDto();
				fjcfbDto.setFjid(khglDto.getFjids().get(i));
				FjcfbDto t_fjcfbDto=fjcfbService.getDto(fjcfbDto);
				int begin=t_fjcfbDto.getWjm().lastIndexOf(".");
				String wjmkzm=t_fjcfbDto.getWjm().substring(begin);
				DBEncrypt p = new DBEncrypt();
				if((wjmkzm.equalsIgnoreCase(".doc"))||(wjmkzm.equalsIgnoreCase(".docx"))||(wjmkzm.equalsIgnoreCase(".xls"))||(wjmkzm.equalsIgnoreCase(".xlsx"))) {
					String wjljjm=p.dCode(t_fjcfbDto.getWjlj());
					//连接服务器
					boolean sendFlg=wjglService.sendWordFile(wjljjm);
					if(sendFlg) {
						Map<String,String> t_map=new HashMap<>();
						String fwjm=p.dCode(t_fjcfbDto.getFwjm());
						t_map.put("wordName", fwjm);
						t_map.put("fwjlj",t_fjcfbDto.getFwjlj());
						t_map.put("fjid",t_fjcfbDto.getFjid());
						t_map.put("ywlx",t_fjcfbDto.getYwlx());
						t_map.put("MQDocOkType",DOC_OK);
						//发送Rabbit消息转换pdf
						amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(t_map));
					}
				}

			}
		}
		int result=dao.insert(khglDto);

		boolean b =  iRdRecordService.addCustomerAndCustomer_extradefineAndSa_invoicecustomersAndTc_tmp_duplication(khglDto);
			if(result==0|| !b)
				throw new BusinessException("msg","新增客户管理信息失败");
		return true;
	}
    /**
     * 查询客户编码最大值
     * @param khglDto
     * @return
     */
    @Override
    public Integer countMax(KhglDto khglDto) {
        return dao.countMax(khglDto);
    }


	/**
	 * 删除客户管理信息
	 * @param khglDto
	 * @return
	 */
	@Override
	public boolean deleteByKhglids(KhglDto khglDto) {
		Integer result = dao.deleteByIds(khglDto);
		return result >= 0;
	}

	/**
	 * 修改客户管理信息
	 * @param khglDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateKhglxx(KhglDto khglDto) throws BusinessException {
		if(!CollectionUtils.isEmpty(khglDto.getFjids())) {
			for (int i = 0;i<khglDto.getFjids().size();i++) {

				boolean saveFile = fjcfbService.save2RealFile(khglDto.getFjids().get(i), khglDto.getKhid());
				if(!saveFile)
					throw new BusinessException("msg","附件保存失败!");
				FjcfbDto fjcfbDto=new FjcfbDto();
				fjcfbDto.setFjid(khglDto.getFjids().get(i));
				FjcfbDto t_fjcfbDto=fjcfbService.getDto(fjcfbDto);
				int begin=t_fjcfbDto.getWjm().lastIndexOf(".");
				String wjmkzm=t_fjcfbDto.getWjm().substring(begin);
				DBEncrypt p = new DBEncrypt();
				if((wjmkzm.equalsIgnoreCase(".doc"))||(wjmkzm.equalsIgnoreCase(".docx"))||(wjmkzm.equalsIgnoreCase(".xls"))||(wjmkzm.equalsIgnoreCase(".xlsx"))) {
					String wjljjm=p.dCode(t_fjcfbDto.getWjlj());
					//连接服务器
					boolean sendFlg=wjglService.sendWordFile(wjljjm);
					if(sendFlg) {
						Map<String,String> t_map=new HashMap<>();
						String fwjm=p.dCode(t_fjcfbDto.getFwjm());
						t_map.put("wordName", fwjm);
						t_map.put("fwjlj",t_fjcfbDto.getFwjlj());
						t_map.put("fjid",t_fjcfbDto.getFjid());
						t_map.put("ywlx",t_fjcfbDto.getYwlx());
						t_map.put("MQDocOkType",DOC_OK);
						//发送Rabbit消息转换pdf
						amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(t_map));
					}
				}
			}
		}
		int result=dao.update(khglDto);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		khglDto.setXgsj(df.format(new Date()));
		boolean b = iRdRecordService.updateCustomerAndCustomer_extradefineAndSa_invoicecustomersAndTc_tmp_duplication(khglDto);
			if(result==0|| !b)
				throw new BusinessException("msg","修改客户管理信息失败");
		return true;
	}

	@Override
	public KhglDto getKhglDtoByKhjc(String khjc) {
		return dao.getKhglDtoByKhjc(khjc);
	}

	@Override
	public boolean updateList(KhglDto khglDto) {
		return dao.updateList(khglDto);
	}

	@Override
	public KhglDto getKhglDtoByKhdm(String khdm) {
		return dao.getKhglDtoByKhdm(khdm);
	}

	/**
	 * 导出
	 *
	 * @param params
	 * @return
	 */
	@Override
	public int getCountForSearchExp(KhglDto khglDto, Map<String, Object> params) {
		return dao.getCountForSearchExp(khglDto);
	}

	/**
	 * 根据搜索条件获取导出信息
	 *
	 * @param params
	 * @return
	 */
	public List<KhglDto> getListForSearchExp(Map<String, Object> params) {
		KhglDto khglDto = (KhglDto) params.get("entryData");
		queryJoinFlagExport(params, khglDto);
		return dao.getListForSearchExp(khglDto);
	}
	/**
	 * 根据选择信息获取导出信息
	 *
	 * @param params
	 * @return
	 */
	public List<KhglDto> getListForSelectExp(Map<String, Object> params) {
		KhglDto khglDto = (KhglDto) params.get("entryData");
		queryJoinFlagExport(params, khglDto);
		return dao.getListForSelectExp(khglDto);
	}

	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String, Object> params, KhglDto khglDto) {
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuilder sqlParam = new StringBuilder();
		for (DcszDto dcszDto : choseList) {
			if (dcszDto == null || dcszDto.getDczd() == null)
				continue;

			sqlParam.append(",");
			if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		String sqlcs = sqlParam.toString();
		khglDto.setSqlParam(sqlcs);
	}


}
