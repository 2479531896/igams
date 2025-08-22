package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.wechat.dao.entities.BfdxlxrglDto;
import com.matridx.igams.wechat.dao.entities.BfglDto;
import com.matridx.igams.wechat.dao.entities.BfglModel;
import com.matridx.igams.wechat.dao.entities.BfplszDto;
import com.matridx.igams.wechat.dao.post.IBfglDao;
import com.matridx.igams.wechat.service.svcinterface.IBfglService;
import com.matridx.igams.wechat.service.svcinterface.IBfplszService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.List;
import java.util.Map;

@Service
public class BfglServiceImpl extends BaseBasicServiceImpl<BfglDto, BfglModel, IBfglDao> implements IBfglService{

	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	private IBfplszService bfplszService;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;

	/**
	 * 根据录入人员查询拜访登记信息(根据单位,客户姓名,科室分组)
	 * @param bfglDto
	 * @return
	 */
	public List<BfglDto> getDtoByLrry(BfglDto bfglDto){
		return dao.getDtoByLrry(bfglDto);
	}
	
	
	
	/** 
	 * 插入错误信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(BfglDto bfglDto){
		if(StringUtil.isBlank(bfglDto.getBfid())){
			bfglDto.setBfid(StringUtil.generateUUID());
		}
		int result = dao.insert(bfglDto);
        return result != 0;
    }
	
	/**
	 * 钉钉小程序获取个人拜访清单
	 * @param bfglDto
	 * @return
	 */
	public List<BfglDto> getPersonalListByLrry(BfglDto bfglDto){
		return dao.getPersonalListByLrry(bfglDto);
	}
	
	/**
	 * 获取最新的医生数,床位数,分组数,科室合作公司信息
	 * @param bfglDto
	 * @return
	 */
	public BfglDto getNewKsxx(BfglDto bfglDto) {
		return dao.getNewKsxx(bfglDto);
	}
	
	/**
	 * 选中导出
	 * 
	 * @param params
	 * @return
	 */
	public List<BfglDto> getListForSelectExp(Map<String, Object> params)
	{
		BfglDto bfglDto = (BfglDto) params.get("entryData");
		queryJoinFlagExport(params, bfglDto);
        return dao.getListForSelectExp(bfglDto);
	}
	
	/**
	 * 根据搜索条件获取导出条数
	 * 
	 * @param params
	 * @return
	 */
	public int getCountForSearchExp(BfglDto bfglDto,Map<String, Object> params) {
        return dao.getCountForSearchExp(bfglDto);
	}
	
	/**
	 * 根据搜索条件分页获取导出信息
	 * @param params
	 * @return
	 */
	public List<BfglDto> getListForSearchExp(Map<String,Object> params){
		BfglDto bfglDto = (BfglDto)params.get("entryData");
		queryJoinFlagExport(params,bfglDto);
		return dao.getListForSearchExp(bfglDto);
	}

	
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String, Object> params, BfglDto bfglDto)
	{
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuffer sqlParam = new StringBuffer();
		for (DcszDto dcszDto : choseList)
		{
			
			if (dcszDto == null || dcszDto.getDczd() == null)
				 continue; 
		 	if(dcszDto.getDczd().equalsIgnoreCase("BFRMC")) { 
		 		bfglDto.setBfr_flg("Y"); 
		 	}else if(dcszDto.getDczd().equalsIgnoreCase("DWFLMC")) {
		 		bfglDto.setDwfl_flg("Y"); 
		 	}else if(dcszDto.getDczd().equalsIgnoreCase("DWDJMC")) {
		 		bfglDto.setDwdj_flg("Y"); 
		 	}else if(dcszDto.getDczd().equalsIgnoreCase("DWLBMC")) {
		 		bfglDto.setDwlb_flg("Y"); 
		 	}else if(dcszDto.getDczd().equalsIgnoreCase("SFMC")) {
		 		bfglDto.setSf_flg("Y"); 
		 	}
			 
			sqlParam.append(",");
			if (StringUtil.isNotBlank(dcszDto.getSqlzd()))
			{
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		bfglDto.setSqlparam(sqlParam.toString());
	}

	
	/**
	 * 钉钉小程序统计本周的拜访次数，拜访单位个数，拜访时长
	 * @param bfglDto
	 * @return
	 */
	public BfglDto getStatictisByWeek(BfglDto bfglDto){
		return dao.getStatictisByWeek(bfglDto);
	}
	
	/**
	 * 钉钉小程序统计本月的拜访次数，拜访单位个数，拜访时长
	 * @param bfglDto
	 * @return
	 */
	public BfglDto getStatictisByMonth(BfglDto bfglDto) {
		return dao.getStatictisByMonth(bfglDto);
	}
	
	/**
	 * 钉钉小程序统计全部的拜访次数，拜访单位个数，拜访时长
	 * @param bfglDto
	 * @return
	 */
	public BfglDto getStatictisByAll(BfglDto bfglDto) {
		return dao.getStatictisByAll(bfglDto);
	}
	
	/**
	 * 钉钉小程序统计本日的拜访次数，拜访单位个数，拜访时长
	 * @param bfglDto
	 * @return
	 */
	public BfglDto getStatictisByDAY(BfglDto bfglDto) {
		return dao.getStatictisByDAY(bfglDto);
	}
	
	/**
	 * 钉钉小程序全部拜访次数统计图
	 * @param bfglDto
	 * @return
	 */
	public List<BfglDto> getStatictisCountByDwid(BfglDto bfglDto) {
		return dao.getStatictisCountByDwid(bfglDto);
	}
	
	/**
	 * 钉钉小程序查询拜访单位个数统计图(根据拜访时间起始区分)
	 * @param bfglDto
	 * @return
	 */
	public List<BfglDto> getStatictisDwgsByBfsjqs(BfglDto bfglDto){
		return dao.getStatictisDwgsByBfsjqs(bfglDto);
	}
	
	/**
	 * 钉钉小程序查询拜访时长统计图(根据拜访单位区分)
	 * @param bfglDto
	 * @return
	 */
	public List<BfglDto> getStatictisBfscByDwid(BfglDto bfglDto){
		return dao.getStatictisBfscByDwid(bfglDto);
	}
	
	/**
	 * 获取统计周期内拜访人信息
	 * @param bfglDto
	 * @return
	 */
	public List<BfglDto> getBfrByTjrq(BfglDto bfglDto) {
		return dao.getBfrByTjrq(bfglDto);
	}
	
	/**
	 * 钉钉小程序查询拜访次数统计图(根据人员区分)
	 * @param bfglDto
	 * @return
	 */
	public List<BfglDto> getStatictisCountByBfr(BfglDto bfglDto){
		return dao.getStatictisCountByBfr(bfglDto);
	}
	
	/**
	 * 钉钉小程序查询拜访单位个数统计图(根据拜访人区分)
	 * @param bfglDto
	 * @return
	 */
	public List<BfglDto> getStatictisDwgsByBfr(BfglDto bfglDto){
		return dao.getStatictisDwgsByBfr(bfglDto);
	}
	
	/**
	 * 钉钉小程序查询拜访时长统计图(根据拜访人区分) 
	 * @param bfglDto
	 * @return
	 */
	public List<BfglDto> getStatictisBfscByBfr(BfglDto bfglDto){
		return dao.getStatictisBfscByBfr(bfglDto);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean saveVisitRecord(BfglDto bfglDto) {
		try {
			if(StringUtil.isNotBlank(bfglDto.getBfid())){
				BfglDto dto = dao.getDto(bfglDto);
				bfglDto.setXgry(bfglDto.getBfr());
				int updated = dao.update(bfglDto);
				if(updated==0){
					return false;
				}
				BfplszDto bfplszDto=new BfplszDto();
				bfplszDto.setDwid(dto.getDwid());
				bfplszDto.setLxrid(dto.getLxrid());
				bfplszDto.setYhid(dto.getBfr());
				bfplszService.determineAndModVisitMarker(bfplszDto);
				bfplszDto.setDwid(bfglDto.getDwid());
				bfplszDto.setLxrid(bfglDto.getLxrid());
				bfplszDto.setYhid(bfglDto.getBfr());
				bfplszService.determineAndModVisitMarker(bfplszDto);
			}else{
				bfglDto.setBfid(StringUtil.generateUUID());
				bfglDto.setLrry(bfglDto.getBfr());
				int inserted = dao.insert(bfglDto);
				if(inserted==0){
					return false;
				}
				BfplszDto bfplszDto=new BfplszDto();
				bfplszDto.setDwid(bfglDto.getDwid());
				bfplszDto.setLxrid(bfglDto.getLxrid());
				bfplszDto.setYhid(bfglDto.getBfr());
				bfplszService.determineAndModVisitMarker(bfplszDto);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if(bfglDto.getFjids()!=null && !bfglDto.getFjids().isEmpty()){
			if(StringUtil.isNotBlank(bfglDto.getFjbcbj())&&"DINGDING".equals(bfglDto.getFjbcbj())){
				MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
				String fjids = StringUtils.join(bfglDto.getFjids(), ",");
				paramMap.add("fjids", fjids);
				paramMap.add("ywid", bfglDto.getBfid());
				RestTemplate restTemplate = new RestTemplate();
				String param=restTemplate.postForObject(menuurl +"/wechat/getFileAddress", paramMap, String.class);
				if(param!=null) {
					JSONArray parseArray = JSONObject.parseArray(param);
					for (Object o : parseArray) {
						FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) o, FjcfbModel.class);
						fjcfbModel.setYwid(bfglDto.getBfid());
						// 下载服务器文件到指定文件夹
						boolean isSuccess = fjcfbService.insert(fjcfbModel);
						downloadFile(fjcfbModel);
						if (!isSuccess)
							return false;
					}
				}
			}else{
				for (int i = 0; i < bfglDto.getFjids().size(); i++) {
					fjcfbService.save2RealFile(bfglDto.getFjids().get(i),bfglDto.getBfid());
				}
			}
		}
		return true;
	}

	/**
	 * 根据路径创建文件
	 */
	private boolean mkDirs(String storePath)
	{
		File file = new File(storePath);
		if (file.isDirectory())
		{
			return true;
		}
		return file.mkdirs();
	}

	/**
	 * 文件下载
	 */
	public boolean downloadFile(FjcfbModel fjcfbModel)
	{
		String wjlj = fjcfbModel.getWjlj();
		String fwjlj = fjcfbModel.getFwjlj();
		DBEncrypt crypt = new DBEncrypt();
		String filePath = crypt.dCode(wjlj);
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("wjlj", wjlj);
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try
		{
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
			RestTemplate t_restTemplate = new RestTemplate();
			ResponseEntity<byte[]> response = t_restTemplate.exchange(menuurl + "/wechat/getImportFile", HttpMethod.POST, httpEntity, byte[].class);
			// 校验文件夹目录是否存在，不存在就创建一个目录
			mkDirs(crypt.dCode(fwjlj));
			byte[] result = response.getBody();
			inputStream = new ByteArrayInputStream(result);

			outputStream = new FileOutputStream(filePath);

			int len;
			byte[] buf = new byte[1024];
			while ((len = inputStream.read(buf, 0, 1024)) != -1)
			{
				outputStream.write(buf, 0, len);
			}
			outputStream.flush();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			try
			{
				if (inputStream != null)
					inputStream.close();
				if (outputStream != null)
					outputStream.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean delVisitRecord(BfglDto bfglDto) {
		BfglDto dto = dao.getDto(bfglDto);
		int deleted = dao.delete(bfglDto);
		if(deleted==0){
			return false;
		}
		BfplszDto bfplszDto=new BfplszDto();
		bfplszDto.setDwid(dto.getDwid());
		bfplszDto.setLxrid(dto.getLxrid());
		bfplszDto.setYhid(dto.getBfr());
        try {
            bfplszService.determineAndModVisitMarker(bfplszDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean batchDelete(BfglDto bfglDto) {
		List<BfglDto> dtoList = dao.getDtoList(bfglDto);
		int deleted = dao.delete(bfglDto);
		if(deleted==0){
			return false;
		}
		for(BfglDto dto:dtoList){
			BfplszDto bfplszDto=new BfplszDto();
			bfplszDto.setDwid(dto.getDwid());
			bfplszDto.setLxrid(dto.getLxrid());
			bfplszDto.setYhid(dto.getBfr());
			try {
				bfplszService.determineAndModVisitMarker(bfplszDto);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return true;
	}

	@Override
	public boolean merge(BfglDto bfglDto) {
		return dao.merge(bfglDto);
	}

	@Override
	public boolean mergeLxrid(List<BfdxlxrglDto> bfdxlxrglDtos_sc) {
		return dao.mergeLxrid(bfdxlxrglDtos_sc);
	}

	@Override
	public List<BfglDto> getStatisticsListByBfr(BfglDto bfglDto) {
		return dao.getStatisticsListByBfr(bfglDto);
	}

	@Override
	public List<BfglDto> getStatisticsListByDw(BfglDto bfglDto) {
		return dao.getStatisticsListByDw(bfglDto);
	}

	@Override
	public List<BfglDto> viewStatisticList(BfglDto bfglDto) {
		return dao.viewStatisticList(bfglDto);
	}

}
