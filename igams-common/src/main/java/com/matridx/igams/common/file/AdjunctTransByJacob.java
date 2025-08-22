package com.matridx.igams.common.file;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.SyxxDto;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

public class AdjunctTransByJacob extends Thread {
	
	private final Logger logger = LoggerFactory.getLogger(AdjunctTransByJacob.class);
	
	private String pdfFile;
	
	private String docFile;
	
	private FjcfbModel fjModel;
	
	private final SyxxDto syxxDto;
	
	public AdjunctTransByJacob(FjcfbModel fjModel,SyxxDto syxxDto){
		
		this.fjModel = fjModel;
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
		this.docFile = path + t_separ + fileName;
	}

	@Override
	public void run() {
		
		if(fjModel == null || StringUtil.isBlank(fjModel.getWjlj()))
			return;
		if(getDocFile().endsWith(".doc")||getDocFile().endsWith(".docx")||getDocFile().endsWith(".pdf")){
			//进行文件转换并添加条形码
			boolean result = transform();
			if(result){
				logger.info(" ---------> "+getDocFile()+"文件转换并添加水印成功！");
				AttachHelper.updatePathAndSuccessFlg(fjModel);
            }else{
				logger.info(" ---------> "+getDocFile()+"文件转换并添加水印失败！");
				AttachHelper.updatePathAndFailFlg(fjModel);
            }
		}else{
			logger.info(" ---------> "+getDocFile()+"文件非doc、docx、pdf后缀文件，直接算做转换成功");
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
		
		if(fileName.endsWith(".doc")||fileName.endsWith(".docx")){
			//文件进行转换（word->pdf）
			boolean result = Jacob.word2pdf(fileName, getPdfFile());
			if(result){
				logger.info(" ---------> "+getDocFile()+"文件异步转换成功。下一步添加水印");
				boolean isSuccess = WatermarkUtil.addWatermark(new File(getPdfFile()),syxxDto);
				if(isSuccess){
					//判断是否添加条形码
					if(StringUtil.isNotBlank(getFjcfbModel().getBarcodeStr())){
						boolean addcodeResult = BarCodeWatermarkUtil.pdfAddControlCode(new File(getPdfFile()), getFjcfbModel().getBarcodeStr());
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
					return true;
				}else{
					logger.error(" ---------> "+getDocFile()+"文件异步添加水印失败。原因：添加水印失败");
					//删除转化成的pdf文件
					if(new File(getPdfFile()).exists()){
						new File(getPdfFile()).delete();
					}
					return false;
				}
			}else{
				logger.error(" ---------> "+getDocFile()+"文件异步转换失败。原因：文件转化异常");
				return false;
			}
		}else{
			//pdf文件直接添加水印，无需转换
			boolean isSuccess = WatermarkUtil.addWatermark(new File(getPdfFile()),syxxDto);
			if(isSuccess){
				//判断是否添加条形码
				if(StringUtil.isNotBlank(getFjcfbModel().getBarcodeStr())){
					//添加条形码
					boolean addcodeResult = BarCodeWatermarkUtil.pdfAddControlCode(new File(getPdfFile()), getFjcfbModel().getBarcodeStr());
					if(!addcodeResult){
						logger.error(" ---------> "+getDocFile()+"文件异步添加水印失败。原因：添加条形码失败");
						//删除转化成的pdf文件
						if(new File(getPdfFile()).exists()){
							new File(getPdfFile()).delete();
						}
						return false;
					}
				}
				
				//给pdf添加审核人员信息
				//请从FjcfbModel获取相应信息，请设置auditInfoStr，auditDateStr，ImagePath
				/*String auditInfoStr = fjModel.getShrs();
				String auditDateStr = fjModel.getShsjs();
				String auditNameStr = fjModel.getShyhms();
				if(auditInfoStr!=null&&!auditInfoStr.equals("")){
					BarCodeWatermarkUtil.pdfAddAuditInfo(new File(getPdfFile()), auditInfoStr,auditDateStr,auditNameStr);
				}*/
				logger.info(" ---------> "+getDocFile()+"文件异步添加水印成功。");
				return true;
			}else{
				logger.error(" ---------> 文件异步添加水印失败。原因：添加水印失败");
				return false;
			}
		}
		
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

	public FjcfbModel getFjcfbModel() {
		return fjModel;
	}

	public void setFjcfbModel(FjcfbModel fjModel) {
		this.fjModel = fjModel;
	}
	
}
