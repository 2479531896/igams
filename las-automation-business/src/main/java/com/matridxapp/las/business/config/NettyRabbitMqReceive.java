package com.matridxapp.las.business.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.SyxxDto;
import com.matridx.igams.common.dao.entities.XxdlcwglDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.file.AttachHelper;
import com.matridx.igams.common.service.svcinterface.ICommonRabbitService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.ISyxxService;
import com.matridx.igams.common.service.svcinterface.IXxdlcwglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Component
public class NettyRabbitMqReceive {

	/**
	 * FTP服务器地址
	 * 在SpringBoot中使用@Value只能给普通变量赋值，不能给静态变量赋值
	 */
	@Value("${matridx.ftp.url:}")
	private String FTP_URL = null;
	/**
	 * 文档转换完成OK
	 */
	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private String DOC_OK = null;
	@Autowired
	ICommonRabbitService rabbitService;
	@Autowired
	IXxdlcwglService xxdlcwglService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	AttachHelper attachHelper;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	
	@Autowired
	ISyxxService syxxService;
	private Logger log = LoggerFactory.getLogger(NettyRabbitMqReceive.class);
	
	/**
	 * 文件管理接收转换后的pdf,如果为送检信息的word文档，则新增一条pdf的附件信息，同时传递到阿里服务器上，并更新最后转换信息
	 * 否则只是更新最后转换信息
	 * @param str
	 */
	@RabbitListener(queues = ("${spring.rabbitmq.docok:mq.tran.basic.ok}"))
	public void DocChangePdf(String str) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>)JSON.parse(str);  
		DBEncrypt p = new DBEncrypt(); 
		String fwjlj=p.dCode((String) map.get("fwjlj"));
		
		mkDirs(fwjlj);
		String fileName=(String) map.get("pdfName");
		//连接服务器
		try{
			//docker无法连接FTP，因为
			//FTPClient ftp=FtpUtil.connect(FTPPDF_PATH, FTP_URL, FTP_PORT, FTP_USER, FTP_PD );
			//boolean result=FtpUtil.download(ftp, fileName, fwjlj+"/"+fileName);
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
			paramMap.add("fileName", fileName);
			
			boolean downflg = downloadPdfFile(paramMap,fwjlj+"/"+fileName);
			
			if(downflg){
				String zhwjxx=p.eCode(fwjlj+"/"+fileName);
				FjcfbModel fjcfbModel=new FjcfbModel();
				FjcfbModel t_fjcfbModel = null;
				if(StringUtil.isNotBlank((String)map.get("fjid"))){
					fjcfbModel.setFjid((String)map.get("fjid"));
					fjcfbModel.setYwlx((String)map.get("ywlx"));
					fjcfbModel.setZhwjxx(zhwjxx);
					fjcfbService.updateZhwjxx(fjcfbModel);
					t_fjcfbModel = fjcfbService.getModel(fjcfbModel);
				}
				if(t_fjcfbModel != null){
					String wjm = updateSuffix(t_fjcfbModel.getWjm());
					String wjlj = p.eCode(updateSuffix(p.dCode(t_fjcfbModel.getWjlj())));
					String fwjm = p.eCode(updateSuffix(p.dCode(t_fjcfbModel.getFwjm())));
					String newfwjlj = p.eCode(p.dCode(t_fjcfbModel.getFwjlj()));
					//判断业务类型
					if(BusTypeEnum.IMP_DOCUMENT.getCode().equals(t_fjcfbModel.getYwlx())){
						t_fjcfbModel.setYswjm(t_fjcfbModel.getWjm());
						t_fjcfbModel.setYswjlj(t_fjcfbModel.getWjlj());
						t_fjcfbModel.setYwlx(BusTypeEnum.IMP_DOCUMENT.getCode());
						t_fjcfbModel.setWjm(wjm);
						t_fjcfbModel.setWjlj(wjlj);
						t_fjcfbModel.setFwjm(fwjm);
						t_fjcfbModel.setFwjlj(newfwjlj);
						t_fjcfbModel.setShrs((String) map.get("shrList"));
						t_fjcfbModel.setShsjs((String) map.get("shsjList"));
						t_fjcfbModel.setShyhms((String) map.get("yhmList"));
						String signflg = (String) map.get("signflg");
						if("0".equals(signflg) || "1".equals(signflg)){
							SyxxDto syxxDto=syxxService.getDtoByWjlb(t_fjcfbModel.getYwlx());
							if(syxxDto!=null) {
								//0:加签名 1:加水印 
								attachHelper.addWatermark(t_fjcfbModel, signflg, (String) map.get("sxrq"),syxxDto);
							}else {
								log.error("水印信息不存在！");
							}
							
						}
						// replaceflg 为 1 表示转换，0 表示不转换
						String replaceflg = (String) map.get("replaceflg");
						if(StringUtil.isNotBlank(replaceflg) && replaceflg.equals("1")){
							fjcfbService.replaceFile(t_fjcfbModel);
						}
					}
				}
			}
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(DOC_OK);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e){
			log.error("Receive DocChangePdf:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(DOC_OK);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 下载pdf文件
	 * @param paramMap
	 * @param filePath
	 * @return
	 */
	private boolean downloadPdfFile(MultiValueMap<String, Object> paramMap,String filePath) {
		boolean downflg = false;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(paramMap,headers);
			RestTemplate t_restTemplate=new RestTemplate();
			ResponseEntity<byte[]> response = t_restTemplate.exchange("http://" + FTP_URL + ":8756/file/downloadPdfFile", HttpMethod.POST, httpEntity, byte[].class);
			byte[] result = response.getBody();
			inputStream = new ByteArrayInputStream(result);
			
			outputStream = new FileOutputStream(new File(filePath));
			
			int len = 0;
			byte[] buf = new byte[1024];
			while ((len = inputStream.read(buf, 0, 1024)) != -1) {
				outputStream.write(buf, 0, len);
			}
			outputStream.flush();
			downflg = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
        	try {
				if(inputStream != null) inputStream.close();
				if(outputStream != null) outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		
		return downflg;
	}
	
	/**
	 * 将后缀修改为pdf
	 * @param filename
	 * @return
	 */
	private String updateSuffix(String filename){
		if ((filename != null) && (filename.length() > 0)) { 
			int dot = filename.lastIndexOf('.'); 
			if ((dot >-1) && (dot < (filename.length()))) { 
				String substring = filename.substring(0, dot);
				filename = substring.concat(".pdf");
				return filename;
			}
		}
        return filename;
	}
	
	/**
	 * 根据路径创建文件
	 * @param storePath
	 * @return
	 */
	private boolean mkDirs(String storePath){
		File file = new File(storePath);
		if (file.isDirectory()) {
			return true;
		}
		return file.mkdirs();
	}
	
	
	
}
