package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.dao.entities.WkmxPcrModel;
import com.matridx.igams.common.dao.entities.WkmxPcrResultModel;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IPcrsyjgService;
import com.matridx.igams.wechat.dao.entities.SjyzmxDto;
import com.matridx.igams.wechat.dao.entities.SjyzmxModel;
import com.matridx.igams.wechat.dao.post.ISjyzmxDao;
import com.matridx.igams.wechat.service.svcinterface.ISjyzmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SjyzmxServiceImpl extends BaseBasicServiceImpl<SjyzmxDto, SjyzmxModel, ISjyzmxDao>
		implements ISjyzmxService {
	private Logger log = LoggerFactory.getLogger(SjyzmxServiceImpl.class);
	@Autowired
	private IPcrsyjgService pcrsyjgService;

	/**
	 * 根据验证id查询最大序号
	 * 
	 * @param yzid
	 * @return
	 */
	@Override
	public int getMaxXh(String yzid) {
		// TODO Auto-generated method stub
		return dao.getMaxXh(yzid);
	}

	/**
	 * 接收处理从pcr返回的实验结果
	 */
	@Override
	public boolean getSjyzmxResult(WkmxPcrModel wkmxPcrModel) {
		if (StringUtil.isBlank(wkmxPcrModel.getWkid())) {
			pcrsyjgService.savePcrsyjgDto(wkmxPcrModel);
		} else {
			List<WkmxPcrResultModel> resultModels = wkmxPcrModel.getResult();
			if (resultModels != null && resultModels.size() > 0) {
				for (WkmxPcrResultModel pr : resultModels) {
					if (pr == null)
						continue;
					// 计算位置序号
					if (StringUtil.isBlank(pr.getWell())) {
						log.info(pr.getSampleUID() + ":空位为空 ");
						return false;
					}
					Map<String, String> map = new HashMap<>();
					// 计算位置序号
					if (StringUtil.isNotBlank(pr.getWellHIndex()) && StringUtil.isNotBlank(pr.getWellVIndex())) {
						int hnum = Integer.parseInt(pr.getWellHIndex());
						int znum = Integer.parseInt(pr.getWellVIndex());
						String xh = (znum * 8 + hnum + 1) + "";
						map.put("xh", xh);
					}
					map.put("nbbm", pr.getSampleName());

					SjyzmxDto sjyzmxDto = new SjyzmxDto();
					sjyzmxDto.setKw(pr.getWell());
					sjyzmxDto.setBh(pr.getSampleName());
					sjyzmxDto.setJcqf(pr.getReferenceDye());
					sjyzmxDto.setMbjy(pr.getGeneName());
					if (StringUtil.isNotBlank(pr.getCtVaule())) {
						if (!SjyzServiceImpl.isNumber(pr.getCtVaule())) {
							sjyzmxDto.setCt("0");
						} else {
							sjyzmxDto.setCt(pr.getCtVaule());
						}
					}
					SjyzmxDto sjyzmxDto2 = dao.getSjyzmxFristBynbbh(map);
					if (sjyzmxDto2 != null && StringUtil.isNotBlank(sjyzmxDto2.getYzmxid())) {
						sjyzmxDto.setYzmxid(sjyzmxDto2.getYzmxid());
                        return dao.updateBynbbm(sjyzmxDto);
					}
				}
				return true;
			} 
		}
		return true;
	}

}
