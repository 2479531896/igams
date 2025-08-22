package com.matridx.igams.warehouse.service.impl;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.production.dao.entities.HtglDto;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.VendorDto;
import com.matridx.igams.production.dao.matridxsql.VendorDao;
import com.matridx.igams.production.service.impl.FileZipExport;
import com.matridx.igams.production.service.svcinterface.IHtglService;
import com.matridx.igams.production.service.svcinterface.IHtmxService;
import com.matridx.igams.production.util.FileZipThread;
import com.matridx.igams.warehouse.dao.entities.GysxxDto;
import com.matridx.igams.warehouse.dao.entities.GysxxModel;
import com.matridx.igams.warehouse.dao.post.IGysxxDao;
import com.matridx.igams.warehouse.service.svcinterface.IGysxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GysxxServiceImpl extends BaseBasicServiceImpl<GysxxDto, GysxxModel, IGysxxDao> implements IGysxxService{

	@Autowired
	VendorDao vendorDao;
	
	@Autowired
	IJcsjService jcsjService;

	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	RedisUtil redisUtil;
	@Value("${matridx.fileupload.prefix}")
	private String prefix;

	@Value("${matridx.fileupload.tempPath}")
	private String tempFilePath;

	@Autowired
	IHtmxService htmxService;
	@Lazy
	@Autowired
	IHtglService htglService;

	private final Logger log = LoggerFactory.getLogger(GysxxServiceImpl.class);

	/**
	 * 查找服务器
	 * @param server

	 */
	public JcsjDto getServer(String server){
		return dao.getServer(server);
	}
	/**
	 * 查询供应商信息名称
	 * @param gysxxDto

	 */
	public GysxxDto selectByGysmc(GysxxDto gysxxDto){
		return dao.selectByGysmc(gysxxDto);
	}

	@Override
	public Map<String, Object> downloadSupplier(GysxxDto gysxxDto) {
		Map<String, Object> map = new HashMap<>();
		try {
			gysxxDto.setYwlx(BusTypeEnum.IMP_SUBSUPPLIER.getCode());
			List<GysxxDto> gysxxDtos=dao.selectDownLoadFiles(gysxxDto);
			// TODO Auto-generated method stub
			//判断是否选中
			if(!CollectionUtils.isEmpty(gysxxDtos)){
				String key = String.valueOf(System.currentTimeMillis());
				redisUtil.hset("EXP_:_" + key, "Cnt", "0",3600);
				String folderName = "UP" + System.currentTimeMillis();
				String storePath = prefix + tempFilePath + BusTypeEnum.IMP_SUBSUPPLIERZIP.getCode() + "/" + folderName;
				mkDirs(storePath);
				map.put("count", gysxxDtos.size());
				map.put("redisKey", key);
				for (GysxxDto gysxxDto_t:gysxxDtos){
					String	 folderName_t = gysxxDto_t.getGysmc();
					String storePath_t =storePath + "/" + folderName_t;
					mkDirs(storePath_t);
					gysxxDto_t.setStorePath(storePath_t);
				}
				FileZipExport fileZipExport = new FileZipExport();
				fileZipExport.initGys(key,storePath,gysxxDtos, redisUtil,folderName);
				FileZipThread reportZipThread = new FileZipThread(fileZipExport,"Y");
				reportZipThread.start();
				map.put("status", "success");
				map.put("message", "下载成功!");
			}else {
				map.put("status", "fail");
				map.put("message","未查询到相关附件!");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage());
		}

		return map;
	}
	/**
	 * 根据路径创建文件
	 *
	 * @param storePath
	 */
	private void mkDirs(String storePath)
	{
		File file = new File(storePath);
		if (file.isDirectory()) {
			return;
		}
		file.mkdirs();
	}
	/**
	 * 插入供应商信息
	 * @param gysxxDto

	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertDto(GysxxDto gysxxDto) {
		gysxxDto.setGysid(StringUtil.generateUUID());
		if (StringUtil.isNotBlank(gysxxDto.getGysjc())){
			gysxxDto.setGysjc(gysxxDto.getGysjc().trim());
		}
		if (StringUtil.isNotBlank(gysxxDto.getGysmc())){
			gysxxDto.setGysmc(gysxxDto.getGysmc().trim());
		}
		//获取省份参数扩展1
		JcsjDto jcsjDto = jcsjService.getDtoById(gysxxDto.getSf());
		String sfkzcs = jcsjDto.getCskz1();
		//根据参数扩展1模糊查询长度为7的最大的供应商代码
		VendorDto vendorDto = new VendorDto();
		vendorDto.setcVenCode("0"+sfkzcs);
		Integer num = vendorDao.countMax(vendorDto);
		if(num==null) {//没有最大，初始化
			gysxxDto.setGysdm("0"+sfkzcs+"0001");
		}else {
			//有最大，最大+1
			int numnew = num+1;
			String string = Integer.toString(numnew);
			String newString = string.substring(string.length()-4); //截取后4位
			gysxxDto.setGysdm("0"+sfkzcs+newString); //0+省份+后四位
		}
		int result=dao.insert(gysxxDto);
		// 文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(gysxxDto.getFjids())){
			for (int i = 0; i < gysxxDto.getFjids().size(); i++) {
				fjcfbService.saveRealFileForPeo(gysxxDto.getFjids().get(i), gysxxDto.getGysid(),gysxxDto.getLrry());
			}
		}
		if(!CollectionUtils.isEmpty(gysxxDto.getMbfjids())){
			for (int i = 0; i < gysxxDto.getMbfjids().size(); i++) {
				fjcfbService.saveRealFileForPeo(gysxxDto.getMbfjids().get(i), gysxxDto.getGysid(),gysxxDto.getLrry());
			}
		}
		if(result==0)
			return false;
        vendorDto.setcVenCode(gysxxDto.getGysdm());
		VendorDto vendorDto_t=vendorDao.queryByCVenCode(vendorDto);
		if(vendorDto_t==null) {
			vendorDto.setcVenHeadCode(vendorDto.getcVenCode());
			vendorDto.setcVenName(gysxxDto.getGysmc());
			vendorDto.setcVenCode(gysxxDto.getGysdm());
			vendorDto.setcVenAbbName(gysxxDto.getGysjc());
			vendorDto.setcVCCode(jcsjDto.getCskz1());			 
			vendorDto.setcVenExch_name("人民币");
			vendorDto.setdVenDevDate(gysxxDto.getFzrq());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			vendorDto.setdVenCreateDatetime(df.format(new Date()));
			int numVen = vendorDao.insert(vendorDto);
			return numVen >= 1;
		}
		return true;
	}
	
	/**
	 * 插入供应商信息
	 * @param gysxxDto

	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateDto(GysxxDto gysxxDto) {
		VendorDto vendorDto = new VendorDto();
		vendorDto.setGysdm(gysxxDto.getGysdm());
		if (StringUtil.isNotBlank(gysxxDto.getGysjc())){
			gysxxDto.setGysjc(gysxxDto.getGysjc().trim());
		}
		if (StringUtil.isNotBlank(gysxxDto.getGysmc())){
			gysxxDto.setGysmc(gysxxDto.getGysmc().trim());
		}
		//判断供应商省份是否修改
		GysxxDto gysxxDto_t = dao.getDtoById(gysxxDto.getGysid());
		if(gysxxDto_t.getSf()!=null && !gysxxDto_t.getSf().equals(gysxxDto.getSf())) {
			//获取省份参数扩展1
			JcsjDto jcsjDto = jcsjService.getDtoById(gysxxDto.getSf());
			// String sfkzcs = jcsjDto.getCskz1();
			//根据参数扩展1模糊查询长度为7的最大的供应商代码			
			// vendorDto.setcVenCode("0"+sfkzcs);
			// Integer num = vendorDao.countMax(vendorDto);
			// if(num==null) {//没有最大，初始化
			// 	gysxxDto.setGysdm("0"+sfkzcs+"0001");
			// }else {
			// 	//有最大，最大+1
			// 	Integer numnew = num+1;
			// 	String string = numnew.toString();
			// 	String newString = string.substring(string.length()-4); //截取后4位
			// 	gysxxDto.setGysdm("0"+sfkzcs+newString); //0+省份+后四位
			// }
			//存入U8供应商省份
			vendorDto.setcVCCode(jcsjDto.getCskz1());
		}
		int result=dao.updateDto(gysxxDto);
		if(result==0)
			return false;
		if(!CollectionUtils.isEmpty(gysxxDto.getFjids())){
			for (int i = 0; i < gysxxDto.getFjids().size(); i++) {
				fjcfbService.saveRealFileForPeo(gysxxDto.getFjids().get(i), gysxxDto.getGysid(),gysxxDto.getXgry());
			}
		}
		if(!CollectionUtils.isEmpty(gysxxDto.getMbfjids())){
			for (int i = 0; i < gysxxDto.getMbfjids().size(); i++) {
				fjcfbService.saveRealFileForPeo(gysxxDto.getMbfjids().get(i), gysxxDto.getGysid(),gysxxDto.getXgry());
			}
		}
		// vendorDto.setcVenCode(gysxxDto.getGysdm());
		// vendorDto.setcVenHeadCode(vendorDto.getcVenCode());
		vendorDto.setcVenName(gysxxDto.getGysmc());
		vendorDto.setcVenAbbName(gysxxDto.getGysjc());
		vendorDto.setdVenDevDate(gysxxDto.getFzrq());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		vendorDto.setdModifyDate(df.format(new Date()));
		int numVen = vendorDao.update(vendorDto);
		return numVen >= 1;
	}
	/**
	 * 查询供应商列表
	 * @param gysxxDto

	 */
	@Override
	public List<GysxxDto> getPagedSupplierList(GysxxDto gysxxDto){
		return dao.getPagedSupplierList(gysxxDto);
	}
	
	/**
	 * 根据供应商id查询供应商信息
	 * @param gysxxDto

	 */
	@Override
	public GysxxDto selectGysxxByGysid(GysxxDto gysxxDto) {
		return dao.selectGysxxByGysid(gysxxDto);
	}
	
	/**
	 * 删除供应商信息
	 * @param gysxxDto

	 */
	@Override
	public boolean deleteByGysids(GysxxDto gysxxDto) {
		dao.deleteByGysids(gysxxDto);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwids(gysxxDto.getIds());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBSUPPLIER.getCode());
		int result = fjcfbService.deleteByYwidsAndYwlx(fjcfbDto);
		return result >= 0;
	}
	
	/**
	 * 模糊查询供应商
	 * @param gysxxDto

	 */
	@Override
	public List<GysxxDto> selectBygysmc(GysxxDto gysxxDto){
		// TODO Auto-generated method stub
		return dao.selectBygysmc(gysxxDto);
	}

	
	/**
	 * 供应商去重
	 * @param gysxxDto

	 */
	@Override
	public boolean getDtoByGysmcAndJc(GysxxDto gysxxDto) {
		List<GysxxDto> gysxxs = dao.getDtoByGysmcAndJc(gysxxDto);
		if(!CollectionUtils.isEmpty(gysxxs))
			return false;
		VendorDto vendorDto = new VendorDto();
		vendorDto.setcVenCode(gysxxDto.getGysdm());
		vendorDto.setcVenName(gysxxDto.getGysmc());
		vendorDto.setcVenAbbName(gysxxDto.getGysjc());
		List<VendorDto> vendorDtos = vendorDao.queryByMcAndJc(vendorDto);
		return vendorDtos.size() <= 0;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean disableSupplier(GysxxDto gysxxDto) throws BusinessException {
		if ("check".equals(gysxxDto.getDisableFlag())) {
			HtmxDto htmxDto = new HtmxDto();
			htmxDto.setGysid(gysxxDto.getGysid());
			List<HtmxDto> htmxDtos = htmxService.getNotAllArrival(htmxDto);
			if (!CollectionUtils.isEmpty(htmxDtos)) {
				String htnbbhs = StringUtil.join(htmxDtos.stream().map(HtmxDto::getHtnbbh).distinct().collect(Collectors.toList()), ",");
				throw new BusinessException("msg", "还有合同：" + htnbbhs + "未全部到货不允许停用");
			}
			HtglDto htglDto = new HtglDto();
			htglDto.setGys(gysxxDto.getGysid());
			List<HtglDto> invoiceHtglDtos = htglService.getNotAllInvoice(htglDto);
			if (!CollectionUtils.isEmpty(invoiceHtglDtos)) {
				String htnbbhs = StringUtil.join(invoiceHtglDtos.stream().map(HtglDto::getHtnbbh).distinct().collect(Collectors.toList()), ",");
				throw new BusinessException("msg", "还有合同：" + htnbbhs + "未全部发票维护不允许停用");
			}
			List<HtglDto> payHtglDtos = htglService.getNotAllPay(htglDto);
			if (!CollectionUtils.isEmpty(payHtglDtos)) {
				String htnbbhs = StringUtil.join(payHtglDtos.stream().map(HtglDto::getHtnbbh).distinct().collect(Collectors.toList()), ",");
				throw new BusinessException("msg", "还有合同：" + htnbbhs + "未全部付款维护不允许停用");
			}
			List<HtglDto> existStockHtglDtos = htglService.getExistStock(htglDto);
			if (!CollectionUtils.isEmpty(existStockHtglDtos)) {
				String htnbbhs = StringUtil.join(existStockHtglDtos.stream().map(HtglDto::getHtnbbh).distinct().collect(Collectors.toList()), ",");
				throw new BusinessException("msg", "还有合同：" + htnbbhs + "存在库存不允许停用");
			}
		}
		boolean isSuccess = dao.disableSupplier(gysxxDto);
		if (!isSuccess){
			throw new BusinessException("msg","停用供应商失败！");
		}
		GysxxDto dtoById = dao.getDtoById(gysxxDto.getGysid());
		VendorDto vendorDto = new VendorDto();
		vendorDto.setcVenCode(dtoById.getGysdm());
		vendorDto.setdEndDate(gysxxDto.getTysj());
		isSuccess = vendorDao.disableOrEnableVendor(vendorDto);
		if (!isSuccess){
			throw new BusinessException("msg","停用U8供应商失败！");
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean enableSupplier(GysxxDto gysxxDto) throws BusinessException  {
		boolean isSuccess = dao.enableSupplier(gysxxDto);
		if (!isSuccess){
			throw new BusinessException("msg","启用供应商失败！");
		}
		GysxxDto dtoById = dao.getDtoById(gysxxDto.getGysid());
		VendorDto vendorDto = new VendorDto();
		vendorDto.setcVenCode(dtoById.getGysdm());
		vendorDto.setdEndDate(null);
		isSuccess = vendorDao.disableOrEnableVendor(vendorDto);
		if (!isSuccess){
			throw new BusinessException("msg","启用U8供应商失败！");
		}
		return true;
	}

	@Override
	public boolean queryByGfbh(GysxxDto gysxxDto) {
		List<GysxxDto> gysxxDtoList = dao.queryByGfbh(gysxxDto);
		if(gysxxDtoList!=null && !gysxxDtoList.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
}
