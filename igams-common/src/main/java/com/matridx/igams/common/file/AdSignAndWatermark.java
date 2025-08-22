package com.matridx.igams.common.file;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.SyxxDto;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

public class AdSignAndWatermark  extends Thread {

private final Logger logger = LoggerFactory.getLogger(AdSignAndWatermark.class);
	
	private String signflg;
	
	private String sxrq;
	
	private FjcfbModel fjModel;
	
	private String pdfFile;
	
	private String docFile;
	//水印信息
	private final SyxxDto syxxDto;
	
	public AdSignAndWatermark(FjcfbModel fjModel, String signflg, String sxrq,SyxxDto syxxDto){
		this.fjModel = fjModel;
		this.signflg = signflg;
		this.sxrq = sxrq;
		this.syxxDto=syxxDto;
		if(fjModel == null || StringUtil.isBlank(fjModel.getWjlj()))
			return;
		
		DBEncrypt crypt = new DBEncrypt();
		String path = GlobalString.FILE_STOREPATH_PREFIX + crypt.dCode(fjModel.getFwjlj());
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		String fileName = crypt.dCode(fjModel.getFwjm());
		//pdf文件完整路径
		String t_separ = "";
		if(!path.endsWith("/"))
			t_separ = "/";
		this.pdfFile = path + t_separ + fileName.substring(0,fileName.lastIndexOf("."))+".pdf";
		//word文件完整路径
		docFile = path + t_separ + fileName;
	}
	
	@Override
	public void run() {
		
		if(fjModel == null || StringUtil.isBlank(fjModel.getWjlj()))
			return;
		if(getDocFile().endsWith(".pdf") || getDocFile().endsWith(".PDF")){
			//进行文件转换并添加条形码
			boolean result = transform();
			if(result){
				logger.info(" ---------> "+getDocFile()+"文件转换并添加水印成功！");
				AttachHelper.updateSuccessFlg(fjModel);
            }else{
				logger.info(" ---------> "+getDocFile()+"文件转换并添加水印失败！");
				AttachHelper.updatePathAndFailFlg(fjModel);
            }
		}else{
			logger.info(" ---------> "+getDocFile()+"pdf后缀文件，直接算做转换成功");
			FjcfbModel updateFj = new FjcfbModel();
			updateFj.setFjid(fjModel.getFjid());
			AttachHelper.updatePathAndSuccessFlg(updateFj);
        }
		
	}
	
	/**
	 * 转换成PDF文件并加水印，将文件保存在相同路径(pdf/doc/docx)
	 * @return
	 */
	public boolean transform(){
		
		String fileName = getDocFile();
		File file = new File(fileName);
		if(!file.exists()){
			logger.error(" ---------> 文件异步转换失败。原因："+getDocFile()+"文件不存在");
			return false;
		}
		
		if(fileName.endsWith(".pdf") || getDocFile().endsWith(".PDF")){
			//pdf文件直接添加水印
			if(syxxDto!=null){
				boolean isSuccess= WatermarkUtil.addWatermark(new File(getPdfFile()),syxxDto);

				if(isSuccess){
					//判断是否添加条形码
					if(StringUtil.isNotBlank(getFjModel().getBarcodeStr())){
						//添加条形码
						boolean addcodeResult = BarCodeWatermarkUtil.pdfAddControlCode(new File(getPdfFile()), getFjModel().getBarcodeStr());
						if(!addcodeResult){
							logger.error(" ---------> "+getDocFile()+"文件异步添加水印失败。原因：添加条形码失败");
							//删除转化成的pdf文件
							if(new File(getPdfFile()).exists()){
								new File(getPdfFile()).delete();
							}
							return false;
						}
					}
					logger.info(" ---------> "+getDocFile()+"文件异步添加水印成功。");
					//判断是否给pdf添加审核人员信息
					if("0".equals(signflg)){
						//请从FjcfbModel获取相应信息，请设置auditInfoStr，auditDateStr，ImagePath
						String auditInfoStr = fjModel.getShrs();
						String auditDateStr = fjModel.getShsjs();
						String auditNameStr = fjModel.getShyhms();
						if(auditInfoStr!=null&& !auditInfoStr.isEmpty()){
							BarCodeWatermarkUtil.pdfAddAuditInfo(new File(getPdfFile()), auditInfoStr,auditDateStr,auditNameStr, sxrq);
						}
					}
					logger.info(" ---------> "+getDocFile()+"文件异步添加签名成功。");
					return true;
				}else{
					logger.error(" ---------> 文件异步添加水印失败。原因：添加水印失败");
					return false;
				}
			}else{
				logger.info(" ---------> "+getDocFile()+"文件开始异步添加签名。");
				logger.error(" ---------> "+getDocFile()+"只附加签名。");
				//判断是否给pdf添加审核人员信息
				if("0".equals(signflg)){
					//请从FjcfbModel获取相应信息，请设置auditInfoStr，auditDateStr，ImagePath
					String auditInfoStr = fjModel.getShrs();
					String auditDateStr = fjModel.getShsjs();
					String auditNameStr = fjModel.getShyhms();
					if(auditInfoStr!=null&& !auditInfoStr.isEmpty()){
						BarCodeWatermarkUtil.pdfAddAuditReportInfo(new File(getPdfFile()), auditInfoStr,auditDateStr,auditNameStr, sxrq);
					}
				}
				logger.info(" ---------> "+getDocFile()+"文件异步添加签名成功。");
				return true;
			}
		}
		return false;
	}
	
	
	public String getSxrq() {
		return sxrq;
	}

	public void setSxrq(String sxrq) {
		this.sxrq = sxrq;
	}

	public String getSignflg() {
		return signflg;
	}

	public void setSignflg(String signflg) {
		this.signflg = signflg;
	}

	public FjcfbModel getFjModel() {
		return fjModel;
	}

	public void setFjModel(FjcfbModel fjModel) {
		this.fjModel = fjModel;
	}

	public String getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(String pdfFile) {
		this.pdfFile = pdfFile;
	}

	public String getDocFile() {
		return docFile;
	}

	public void setDocFile(String docFile) {
		this.docFile = docFile;
	}
	
}
