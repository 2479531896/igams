package com.matridx.igams.experiment.service.impl;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.experiment.dao.entities.SysjglDto;
import com.matridx.igams.experiment.dao.entities.TqglDto;
import com.matridx.igams.experiment.dao.entities.TqglModel;
import com.matridx.igams.experiment.dao.entities.TqmxDto;
import com.matridx.igams.experiment.dao.post.ITqglDao;
import com.matridx.igams.experiment.service.svcinterface.ISysjglService;
import com.matridx.igams.experiment.service.svcinterface.ITqglService;
import com.matridx.igams.experiment.service.svcinterface.ITqmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
public class TqglServiceImpl extends BaseBasicServiceImpl<TqglDto, TqglModel, ITqglDao> implements ITqglService{

	@Autowired
	ITqmxService tqmxService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	private ISysjglService sysjglService;

	private final Logger log = LoggerFactory.getLogger(TqglServiceImpl.class);
	/**
	 * 获取提取管理列表
	 */
	public List<TqglDto> getPagedTqglDtoList(TqglDto tqglDto){
		return dao.getPagedTqglDtoList(tqglDto);
	}
	
	/**
	 * 新增提取管理信息
	 */
	public boolean insertDto(TqglDto tqglDto) {
		if(StringUtils.isBlank(tqglDto.getTqid())) {
			tqglDto.setTqid(StringUtil.generateUUID());
		}
		int result=dao.insert(tqglDto);
		return result > 0;
	}
	
	/**
	 * 更新提取管理信息
	 */
	public boolean updateTqgl(TqglDto tqglDto) {
		return dao.updateTqgl(tqglDto);
	}
	
	/**
	 * 删除提取信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteTqxx(TqglDto tqglDto) {
		boolean deltqgl=dao.deleteTqgl(tqglDto);
		TqmxDto tqmxDto =new TqmxDto();
		if(deltqgl) {
			tqmxDto.setIds(tqglDto.getIds());
			tqmxDto.setScry(tqglDto.getScry());
			tqmxService.delByTqid(tqmxDto);
			//删除相应的提取试剂信息
			SysjglDto sysjglDto = new SysjglDto();
			sysjglDto.setIds(tqglDto.getIds());
			sysjglDto.setScry(tqglDto.getScry());
			sysjglService.deleteByIds(sysjglDto);
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 根据名称获取提取管理信息
	 */
	public List<TqglDto> getTqglByMc(TqglDto tqglDto){
		return dao.getTqglByMc(tqglDto);
	}

	@Override
	public List<Map<String, String>> getJsjcdwByjsid(String jsid) {
		// TODO Auto-generated method stub
		return dao.getJsjcdwByjsid(jsid);
	}
	
	/**
	 * 删除临时保存的提取管理信息
	 */
	public boolean deleteLs(String tqid) {
		//删除相应的提取试剂信息
		SysjglDto sysjglDto = new SysjglDto();
		sysjglDto.setYwid(tqid);
		sysjglDto.setScry("tqgl");
		sysjglService.delete(sysjglDto);

		return dao.deleteLs(tqid);
	}

	/**
	 * @Description: 获取操作人和审核人签名
	 * @param tqmxDto
	 * @return com.matridx.igams.experiment.dao.entities.TqglDto
	 * @Author: 郭祥杰
	 * @Date: 2025/5/7 15:01
	 */
	@Override
	public TqglDto getCzrAndShrPic(TqmxDto tqmxDto) {
		TqglDto tqglDto = dao.getDtoById(tqmxDto.getTqid());
		TqglDto tqglDtoT = new TqglDto();
		if(tqglDto!=null){
			if(StringUtils.isNotBlank(tqglDto.getCzryhm())) {
				FjcfbDto czrPic = new FjcfbDto();
				czrPic.setYwid(tqglDto.getCzryhm());
				czrPic.setYwlx(BusTypeEnum.IMP_SIGNATURE.getCode());
				List<FjcfbDto> czrPicList = fjcfbService.selectFjcfbDtoByYwidAndYwlx(czrPic);
				if(!CollectionUtils.isEmpty(czrPicList)){
					DBEncrypt dbEncrypt = new DBEncrypt();
	                try{
						String filePath = dbEncrypt.dCode(czrPicList.get(0).getWjlj());
						byte[] imageBytes = Files.readAllBytes(
								Paths.get(filePath)
						);
						String base64 = Base64Utils.encodeToString(imageBytes);
						File file = new File(filePath);
						String fileName = file.getName();
						String mimeType = determineMimeType(fileName);
						tqglDtoT.setCzr("data:" + mimeType + ";base64," + base64);
					}catch (Exception e){
						log.error("获取操作人签名失败",e);
					}
				}
			}
			if(StringUtils.isNotBlank(tqglDto.getHdryhm())) {
				FjcfbDto hdrPic = new FjcfbDto();
				hdrPic.setYwid(tqglDto.getHdryhm());
				hdrPic.setYwlx(BusTypeEnum.IMP_SIGNATURE.getCode());
				List<FjcfbDto> hdrPicList = fjcfbService.selectFjcfbDtoByYwidAndYwlx(hdrPic);
				if(!CollectionUtils.isEmpty(hdrPicList)){
					DBEncrypt dbEncrypt = new DBEncrypt();
					try{
						String filePath = dbEncrypt.dCode(hdrPicList.get(0).getWjlj());
						byte[] imageBytes = Files.readAllBytes(
								Paths.get(filePath)
						);
						String base64 = Base64Utils.encodeToString(imageBytes);
						File file = new File(filePath);
						String fileName = file.getName();
						String mimeType = determineMimeType(fileName);
						tqglDtoT.setHdr("data:" + mimeType + ";base64," + base64);
					}catch (Exception e){
						log.error("获取核对人签名失败",e);
					}
				}
			}
		}
		return tqglDtoT;
	}

	/**
	 * @Description: 根据文件名判断MIME类型
	 * @param filename
	 * @return java.lang.String
	 * @Author: 郭祥杰
	 * @Date: 2025/5/7 15:27
	 */
	private String determineMimeType(String filename) {
		if (filename.endsWith(".png")){
			return "image/png";
		}
		if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
			return "image/jpeg";
		}
		if (filename.endsWith(".gif")) {
			return "image/gif";
		}
		return "application/octet-stream";
	}
}
