package com.matridx.igams.common.file;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.matridx.igams.common.enums.BusTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.SyxxDto;
import com.matridx.igams.common.dao.post.IFjcfbDao;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

/**
 * 附件处理辅助类
 * @author goofus
 *
 */
@Component
public class AttachHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(AttachHelper.class);
	
	/**
	 * 通用附件保存<p>
	 * if(StringUtils.isBlank(busiId) || null == busiTypes || busiTypes.length < 1) return;
	 * @param ywid 业务ID
	 * @param busiTypes 附件业务类型
	 * @param uploadInfos 附件们
	 * @author goofus
	 */
	public final static void addOrUpdate(String ywid, String busiType, List<FjcfbModel> uploadInfos){
		addOrUpdate(ywid, new String[]{busiType}, uploadInfos);
	}
	
	/**
	 * 通用附件保存<p>
	 * if(StringUtils.isBlank(busiId) || null == busiTypes || busiTypes.length < 1) return;
	 * @param ywid 业务ID
	 * @param busiTypes 附件业务类型们
	 * @param uploadInfos 附件们
	 * @author goofus
	 */
	public final static void addOrUpdate(String ywid, String[] busiTypes, List<FjcfbModel> uploadInfos){
		if(StringUtils.isBlank(ywid) || null == busiTypes || busiTypes.length < 1) return;
		
		IFjcfbService fileUploadService = ServiceFactory.getService(IFjcfbService.class);
		List<FjcfbModel> fjs = fileUploadService.update(ywid, "", Arrays.asList(busiTypes), uploadInfos);

        if(fjs != null && !fjs.isEmpty()){
        	for(FjcfbModel fj : fjs){
            	if(fj == null){
            		continue;
            	}
                fj.setYwid(ywid);
                String seqNum = fileUploadService.getDRNextSeqNum(fj);
                fj.setXh(seqNum);
                fileUploadService.insert(fj);
            }
        }
        if(uploadInfos!=null){
        	fileUploadService.updateSeqNum(ywid,Arrays.asList(busiTypes));
        }
	}
	
	public void addWatermark(String ywid, String ywlx,String barcodeStr,SyxxDto syxxDto){
		addWatermark(ywid,new String[] {ywlx},barcodeStr,syxxDto);
	}
	
	public void addWatermark(String ywid, String ywlx,SyxxDto syxxDto){
		addWatermark(ywid,new String[] {ywlx},null,syxxDto);
	}
	
	public void addWatermark(String ywid, List<String> ywlx,SyxxDto syxxDto){
		addWatermark(ywid,ywlx.toArray(new String[0]),null,syxxDto);
	}
	
	public void addWatermark(String ywid,SyxxDto syxxDto){
		addWatermark(ywid,new String[] {},null,syxxDto);
	}
	
	public void addWatermark(String ywid, List<String> ywlx,String shrList,String shsjList, String yhmList,SyxxDto syxxDto){
		if(StringUtils.isBlank(ywid) || null == ywlx || ywlx.isEmpty()) return;
		
		IFjcfbDao fileUploadDao = ServiceFactory.getService(IFjcfbDao.class);
		
		// 取得附件信息
		FjcfbDto fj = new FjcfbDto();
        fj.setYwid(ywid);
        if(!ywlx.isEmpty())
        	fj.setYwlxs(ywlx);
        List<FjcfbModel> fileList = fileUploadDao.getModelListByYwid(fj);
        for (FjcfbModel FjcfbModel : fileList) {
        	FjcfbModel.setShrs(shrList);
        	FjcfbModel.setShsjs(shsjList);
        	FjcfbModel.setShyhms(yhmList);
        	// 文件转换并加水印
        	new AdjunctTransByJacob(FjcfbModel,syxxDto).start();
        }
	}
	
	public void addWatermark(FjcfbModel fjcfbModel, String signflg, String sxrq,SyxxDto syxxDto){
		if(null == fjcfbModel || StringUtil.isBlank(signflg)) 
			return;
		// 文件添加水印和签名
    	new AdSignAndWatermark(fjcfbModel, signflg, sxrq, syxxDto).start();
	}

	public void addWatermarkBisync(FjcfbModel fjcfbModel, String signflg, String sxrq,SyxxDto syxxDto){
		logger.info(" ---------> "+"文件开始异步添加签名。");
		logger.error(" ---------> "+"只附加签名。");
		//判断是否给pdf添加审核人员信息
		if("0".equals(signflg)){
			//请从FjcfbModel获取相应信息，请设置auditInfoStr，auditDateStr，ImagePath
			String auditInfoStr = fjcfbModel.getShrs();
			String auditDateStr = fjcfbModel.getShsjs();
			String auditNameStr = fjcfbModel.getShyhms();
			DBEncrypt crypt = new DBEncrypt();
			String path = GlobalString.FILE_STOREPATH_PREFIX + crypt.dCode(fjcfbModel.getFwjlj());
			File file = new File(path);
			if(!file.exists()){
				file.mkdirs();
			}
			String fileName = crypt.dCode(fjcfbModel.getFwjm());
			//pdf文件完整路径
			String t_separ = "";
			if(!path.endsWith("/"))
				t_separ = "/";
			String pdfFile = path + t_separ + fileName.substring(0,fileName.lastIndexOf("."))+".pdf";
			if(auditInfoStr!=null&& !auditInfoStr.isEmpty()){
				BarCodeWatermarkUtil.pdfAddAuditReportInfo(new File(pdfFile), auditInfoStr,auditDateStr,auditNameStr, sxrq);
			}
		}
		logger.info(" ---------> "+"文件异步添加签名成功。");
	}

	/**
	 * 通用附件加水印操作<p>
	 * if(StringUtils.isBlank(busiId) || null == busiTypes || busiTypes.length < 1) return;
	 * @param ywid 业务ID
	 * @param busiTypes 附件业务类型们
	 * @author liuyj
	 */
	public void addWatermark(String ywid, String[] ywlxs,String barcodeStr,SyxxDto syxxDto){
		if(StringUtils.isBlank(ywid) || null == ywlxs || ywlxs.length < 1) return;
		
		IFjcfbDao fileUploadDao = ServiceFactory.getService(IFjcfbDao.class);
		
		// 取得附件信息
		FjcfbDto fj = new FjcfbDto();
        fj.setYwid(ywid);
        fj.setYwlxs(Arrays.asList(ywlxs));
        List<FjcfbModel> fileList = fileUploadDao.getModelListByYwid(fj);
        for (FjcfbModel FjcfbModel : fileList) {
        	if (StringUtils.isNotBlank(barcodeStr)) {
				FjcfbModel.setBarcodeStr(barcodeStr);
			}
        	// 文件转换并加水印
        	new AdjunctTransByJacob(FjcfbModel,syxxDto).start();
        }
	}
	
	/**
	 * 通用附件传到ftp<p>
	 * if(StringUtils.isBlank(busiId) || null == busiTypes || busiTypes.length < 1) return;
	 * @param ywid 业务ID
	 * @param busiTypes 附件业务类型们
	 * @param htbh 合同编号
	 * @author Kaijian Lee
	 * @param amqpTempl 
	 */
	public static void addDocToFtp(String ywid, String[] busiTypes, AmqpTemplate amqpTempl,String barcodeStr){
		if(StringUtils.isBlank(ywid) || null == busiTypes || busiTypes.length < 1) return;
		
		IFjcfbDao fileUploadDao = ServiceFactory.getService(IFjcfbDao.class);
		// 取得附件信息
		FjcfbDto fj = new FjcfbDto();
        fj.setYwid(ywid);
        fj.setYwlxs(Arrays.asList(busiTypes));
        List<FjcfbModel> fileList = fileUploadDao.getModelListByYwid(fj);
        for (FjcfbModel FjcfbModel : fileList) {
        	//FTP上传并且转换成pdf文件
        	if (StringUtils.isNotBlank(barcodeStr)) {
				FjcfbModel.setBarcodeStr(barcodeStr);
			}
        	new SendFTPAndChange(FjcfbModel,amqpTempl).start();
        }
	}
	
	/**
	 * 通用附件转换并且添加水印操作（window情况下jacob操作）<p>
	 * if(StringUtils.isBlank(busiId) || null == busiTypes || busiTypes.length < 1) return;
	 * @param ywid 业务ID
	 * @param busiTypes 附件业务类型们
	 * @author liuyj
	 */
	public void addWatermarkByJacob(String ywid, String[] busiTypes,String barcodeStr,SyxxDto syxxDto){
		if(StringUtils.isBlank(ywid) || null == busiTypes || busiTypes.length < 1) return;
		
		IFjcfbDao fileUploadDao = ServiceFactory.getService(IFjcfbDao.class);
		
		// 取得附件信息
		FjcfbDto fj = new FjcfbDto();
        fj.setYwid(ywid);
        //fj.setRelatedBusId(relatedBusId);
        fj.setYwlxs(Arrays.asList(busiTypes));
        List<FjcfbModel> fileList = fileUploadDao.getModelListByYwid(fj);
        for (FjcfbModel FjcfbModel : fileList) {
        	// 文件转换并加水印
        	if(StringUtils.isNotBlank(barcodeStr)){
        		FjcfbModel.setBarcodeStr(barcodeStr);
        	}
        	new AdjunctTransByJacob(FjcfbModel,syxxDto).start();
        }
	}
	
	/**
	 * 通用附件加水印成功后更新路径和加水印成功标记操作<p>
	 * if(StringUtils.isBlank(busiId) || null == busiTypes || busiTypes.length < 1) return;
	 * @param busiId 业务ID
	 * @param busiTypes 附件业务类型们
	 * @author liuyj
	 */
	public final static void updatePathAndSuccessFlg(FjcfbModel fjcfbModel){
		
		if(null == fjcfbModel) return;
		
		IFjcfbDao fjcfbDao = ServiceFactory.getService(IFjcfbDao.class);
    	// 更新附件信息
    	// 解密的附件路径
		//前提条件，所有文件信息还未加密
		DBEncrypt crypt = new DBEncrypt();
		if(!StringUtil.isBlank(fjcfbModel.getWjlj())){
			String wholePath = crypt.dCode(fjcfbModel.getWjlj());
	    	//pdf文件路径+名称
	    	wholePath = wholePath.substring(0, wholePath.lastIndexOf(".")) + ".pdf";
	    	// 加密附件路径
	    	wholePath = crypt.eCode(wholePath);
	    	fjcfbModel.setWjlj(wholePath);
	    	// 文件名
	    	String fileName = crypt.dCode(fjcfbModel.getFwjm());
	    	fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".pdf";// 加密文件名
	        String t_fileName = crypt.eCode(fileName);
	        fjcfbModel.setFwjm(t_fileName);
		}
    	
        fjcfbModel.setZhbj("1");
    	
    	// 更新路径和转换标记
    	fjcfbDao.update(fjcfbModel);
	}
	
	/**
	 * 通用附件加水印成功后加水印成功标记操作<p>
	 * @param busiId 业务ID
	 * @param busiTypes 附件业务类型们
	 * @author liuyj
	 */
	public final static void updateSuccessFlg(FjcfbModel fjcfbModel){
		if(null == fjcfbModel) return;
		IFjcfbDao fjcfbDao = ServiceFactory.getService(IFjcfbDao.class);
		FjcfbModel t_fjcfbModel = new FjcfbModel();
		t_fjcfbModel.setFjid(fjcfbModel.getFjid());
		t_fjcfbModel.setZhbj("1");
    	// 更新路径和转换标记
    	fjcfbDao.update(t_fjcfbModel);
	}
	
	/**
	 * 转换失败更新转换次数
	 * @param FjcfbModel
	 */
	public final static void updatePathAndFailFlg(FjcfbModel fjcfbModel){
		IFjcfbDao fileUploadDao = ServiceFactory.getService(IFjcfbDao.class);
		// 更新路径和转换标记
		fileUploadDao.updateFjljFail(fjcfbModel);
	}
	
	/**
	 * 转换失败更新转换次数
	 * @param FjcfbModel
	 */
	public final static void updateFailFlg(FjcfbModel fjcfbModel){
		IFjcfbDao fileUploadDao = ServiceFactory.getService(IFjcfbDao.class);
		FjcfbModel t_fjcfbModel = new FjcfbModel();
		t_fjcfbModel.setFjid(fjcfbModel.getFjid());
		// 更新路径和转换标记
		fileUploadDao.updateFjljFail(t_fjcfbModel);
	}
	
	/**
	 * 判断是否超出转换次数
	 * @param FjcfbModel
	 */
	public final static boolean isOutOfTimes(FjcfbModel FjcfbModel){
		boolean isOutOfTime = false;
		IFjcfbService fjcfbService = ServiceFactory.getService(IFjcfbService.class);
		FjcfbModel model = fjcfbService.getModel(FjcfbModel);
		if(model!=null&&StringUtil.isNotBlank(model.getZhcs())){
			if(fjcfbService.getFileTransLimit() <= Integer.parseInt(model.getZhcs())){
				isOutOfTime = true;
			}
		}
		return isOutOfTime;
	}
	
	public void main(String[] argsSyxx,SyxxDto syxxDto) {
		String[] ywlxs =new String[1];
		ywlxs[0] ="test";
		addWatermark("1", ywlxs, null,syxxDto);
	}

	/**
	 * 送检结果盖章
	 * @param t_fjcfbModel
	 */
	public static void addChapter(FjcfbModel fjcfbModel) {
		// TODO Auto-generated method stub
		addChapter(fjcfbModel, "chapter.png");
	}
	
	/**
	 * 送检结果盖章
	 * @param fjcfbModel
	 * @param chapter
	 */
	public static void addChapter(FjcfbModel fjcfbModel, String chapter) {
		// TODO Auto-generated method stub
		// 送检结果盖章
		if(fjcfbModel == null || StringUtil.isBlank(fjcfbModel.getWjlj()))
			return;
		logger.info(" ---------> 文件盖章判断开始。");
		DBEncrypt crypt = new DBEncrypt();
		String path = GlobalString.FILE_STOREPATH_PREFIX + crypt.dCode(fjcfbModel.getFwjlj());
		File t_file = new File(path);
		if(!t_file.exists()){
			t_file.mkdirs();
		}
		String file_Name = crypt.dCode(fjcfbModel.getFwjm());
		//pdf文件完整路径
		String t_separ = "";
		if(!path.endsWith("/"))
			t_separ = "/";
		String pdfFile = path + t_separ + file_Name.substring(0,file_Name.lastIndexOf("."))+".pdf";
		//word文件完整路径
		String docFile = path + t_separ + file_Name;
		if(docFile.endsWith(".pdf")){
			logger.info(" ---------> pdf文件盖章开始。");
			//进行文件盖章
			String fileName = docFile;
			File file = new File(fileName);
			if(!file.exists()){
				logger.error(" ---------> 文件异步转换失败。原因："+docFile+"文件不存在");
				return;
			}
			//文件直接盖章
			boolean addcodeResult;
			if(BusTypeEnum.IMP_REPORT_COVID.getCode().equals(fjcfbModel.getYwlx()) || BusTypeEnum.IMP_REPORT_HPV.getCode().equals(fjcfbModel.getYwlx())|| BusTypeEnum.IMP_REPORT_JHPCR.getCode().equals(fjcfbModel.getYwlx())){
				//HPV和COVID的报告PDF盖章
				addcodeResult= BarCodeWatermarkUtil.pdfAddChapterInfo(new File(pdfFile), chapter);
			}else if("IMP_REPORT_ONCO_QINDEX_TEMEPLATE_O".equals(fjcfbModel.getYwlx()) || "IMP_REPORT_SEQ_INDEX_TEMEPLATE_O".equals(fjcfbModel.getYwlx())){
				//Onco的报告取消PDF盖章
				return;
			}else if (BusTypeEnum.IMP_REPORT_RFS_TEMEPLATE_F.getCode().equals(fjcfbModel.getYwlx())) {
				//ResFirst项目PDF盖章
				addcodeResult= BarCodeWatermarkUtil.pdfAddChapterInfoWithPlace(new File(pdfFile), chapter,"210","750");
			}else if (fjcfbModel.getYwlx().contains("IMP_REPORT_ONCO_QINDEX_TEMEPLATE") || fjcfbModel.getYwlx().contains("IMP_REPORT_ONCO_QINDEX_TEMEPLATE_REM") || fjcfbModel.getYwlx().contains("IMP_REPORT_SEQ_INDEX_TEMEPLATE")) {
				//2.0,3.0项目才将盖章放在倒数第二页
				addcodeResult= BarCodeWatermarkUtil.pdfAddChapter(new File(pdfFile), chapter);
			}else {
				addcodeResult = true;
			}
			if(addcodeResult){
				logger.info(" ---------> "+docFile+"文件异步盖章成功。");
			}else{
				logger.error(" ---------> "+docFile+"文件异步盖章失败。原因：添加印章失败");
				//删除转化成的pdf文件
				if(new File(pdfFile).exists()){
					new File(pdfFile).delete();
				}
			}
		}
	}
}
