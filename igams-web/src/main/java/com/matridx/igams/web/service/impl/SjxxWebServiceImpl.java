package com.matridx.igams.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.detection.molecule.dao.entities.FzjybgDto;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxxService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjybgService;
import com.matridx.igams.web.service.svcinterface.ISjxxWebService;
import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.dao.entities.SjnyxDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.service.svcinterface.ISjnyxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxWsService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.RandomUtil;
import com.matridx.springboot.util.file.upload.ZipUtil;
import com.matridx.springboot.util.xml.XmlUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.jws.WebService;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebService(serviceName = "ISjxxWebService", 
endpointInterface = "com.matridx.igams.web.service.svcinterface.ISjxxWebService",
targetNamespace = "http://service.matridx.com")
@Service
public class SjxxWebServiceImpl implements ISjxxWebService{

	@Autowired
	ISjxxWsService sjxxWsService;
	@Autowired
	private ISjxxService sjxxService;
	@Autowired
	private ISjnyxService sjnyxService; 
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	IXxglService xxglService;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	@Value("${matridx.fileupload.prefix}")
	private String prefix;
	@Value("${matridx.fileupload.releasePath}")
	private String releaseFilePath;
	@Value("${matridx.fileupload.tempPath}")
	private String tempFilePath;
	@Autowired
	private DingTalkUtil talkUtil;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IFzjcxxService fzjcxxService;

	@Autowired
	IFzjybgService fzjybgService;
	@Value("${matridx.reportSubmit.health_organ:}")
	private String health_organ;
	@Value("${matridx.reportSubmit.health_domain:}")
	private String health_domain;
	@Value("${matridx.reportSubmit.receive_code:}")
	private String receive_code;
	
	private final static Logger log = LoggerFactory.getLogger(SjxxWebServiceImpl.class);
	
	@Override
	public String downloadByCode(String organ, String code, String lastcode, String sign, String type) {
		// TODO Auto-generated method stub
		// 创建document对象
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");// 创建根节点root
		Element statusElement = root.addElement("status");// 创建根节点status
		statusElement.setText("fail");
		Element errorCodeElement = root.addElement("errorCode");// 创建根节点errorCode
		Element fileElement = root.addElement("file");// 创建根节点file
		Element fileNameElement = root.addElement("fileName");// 创建根节点fileName
		Element infosElement = root.addElement("infos");// 创建根节点infos
		
		// 安全验证
		DBEncrypt crypt = new DBEncrypt();
		Map<String, Object> map = sjxxWsService.checkSecurity(organ, type, code, lastcode, sign, crypt);
		if(!"0".equals(map.get("errorCode"))){
			errorCodeElement.setText((String)map.get("errorCode"));
			log.error((String)map.get("errorCode"));
			return transformToString(document);
		}
		//查询合作伙伴
		List<String> dbs = sjxxService.getSjhbByCode(organ);
		if(CollectionUtils.isEmpty(dbs)){
			errorCodeElement.setText("未查询到伙伴权限！");
			log.error("未查询到伙伴权限！");
			return transformToString(document);
		}
		SjxxDto sjxxDto = new SjxxDto();
		sjxxDto.setDbs(dbs);
		if(StringUtil.isNotBlank(code)){
			//返回报告文件
			sjxxDto.setWbbm(code);
			List<SjxxDto> sjxxDtos = sjxxService.getListByWbbm(sjxxDto);
			if(!CollectionUtils.isEmpty(sjxxDtos)){
				Element infoElement = infosElement.addElement("info");// 创建根节点info
				Element codeElement = infoElement.addElement("code");
				codeElement.setText(sjxxDtos.get(0).getWbbm());				
				List<SjwzxxDto> sjwzxxDtos = sjxxService.selectWzxxBySjid(sjxxDtos.get(0));
				Element sjwzxxsElement = infoElement.addElement("sjwzxxs");// 创建根节点sjwzxxs
				if(!CollectionUtils.isEmpty(sjwzxxDtos)){
                    for (SjwzxxDto sjwzxxDto : sjwzxxDtos) {
                        Element sjwzxxElement = sjwzxxsElement.addElement("sjwzxx");// 创建根节点sjwzxx
                        Element wzlxElement = sjwzxxElement.addElement("wzlx");
                        wzlxElement.setText(sjwzxxDto.getWzlx() == null ? "" : sjwzxxDto.getWzlx());
                        Element wzfllxElement = sjwzxxElement.addElement("wzfllx");
                        wzfllxElement.setText(sjwzxxDto.getWzfllx() == null ? "" : sjwzxxDto.getWzfllx());
                        Element wzflElement = sjwzxxElement.addElement("wzfl");
                        wzflElement.setText(sjwzxxDto.getWzfl() == null ? "" : sjwzxxDto.getWzfl());
                        Element wzzwmElement = sjwzxxElement.addElement("wzzwm");
                        wzzwmElement.setText(sjwzxxDto.getWzzwm() == null ? "" : sjwzxxDto.getWzzwm());
                        Element wzywmElement = sjwzxxElement.addElement("wzywm");
                        wzywmElement.setText(sjwzxxDto.getWzywm() == null ? "" : sjwzxxDto.getWzywm());
                        Element dqsElement = sjwzxxElement.addElement("dqs");
                        dqsElement.setText(sjwzxxDto.getDqs() == null ? "" : sjwzxxDto.getDqs());
                        Element xdfdElement = sjwzxxElement.addElement("xdfd");
                        xdfdElement.setText(sjwzxxDto.getXdfd() == null ? "" : sjwzxxDto.getXdfd());
                        Element smElement = sjwzxxElement.addElement("sm");
                        smElement.setText(sjwzxxDto.getSm() == null ? "" : sjwzxxDto.getSm());
                        Element szwmElement = sjwzxxElement.addElement("szwm");
                        szwmElement.setText(sjwzxxDto.getSzwm() == null ? "" : sjwzxxDto.getSzwm());
                        Element sddsElement = sjwzxxElement.addElement("sdds");
                        sddsElement.setText(sjwzxxDto.getSdds() == null ? "" : sjwzxxDto.getSdds());
                        Element sfdElement = sjwzxxElement.addElement("sfd");
                        sfdElement.setText(sjwzxxDto.getSfd() == null ? "" : sjwzxxDto.getSfd());
                    }
				}
				
				List<SjnyxDto> sjnyxDtos = sjnyxService.getNyxBySjid(sjxxDtos.get(0));
				Element sjnyxsElement = infoElement.addElement("sjnyxs");// 创建根节点sjnyxs
				if(!CollectionUtils.isEmpty(sjnyxDtos)){
                    for (SjnyxDto sjnyxDto : sjnyxDtos) {
                        Element sjnyxElement = sjnyxsElement.addElement("sjnyx");// 创建根节点sjnyx
                        Element jyElement = sjnyxElement.addElement("jy");
                        jyElement.setText(sjnyxDto.getJy() == null ? "" : sjnyxDto.getJy());
                        Element ypElement = sjnyxElement.addElement("yp");
                        ypElement.setText(sjnyxDto.getYp() == null ? "" : sjnyxDto.getYp());
                        Element jlElement = sjnyxElement.addElement("jl");
                        jlElement.setText(sjnyxDto.getJl() == null ? "" : sjnyxDto.getJl());
                        Element xgwzElement = sjnyxElement.addElement("xgwz");
                        xgwzElement.setText(sjnyxDto.getXgwz() == null ? "" : sjnyxDto.getXgwz());
                    }
				}
				
				String jclx = sjxxDtos.get(0).getJclx();
				sjxxDtos.get(0).setDbs(dbs);
				if(StringUtil.isBlank(type) || "pdf".equals(type)){
					sjxxDtos.get(0).setYwlx(sjxxDtos.get(0).getCskz3().replace("_ONCO", "") + "_" + jclx);
				}else{
					sjxxDtos.get(0).setYwlx(sjxxDtos.get(0).getCskz3().replace("_ONCO", "") + "_" + jclx + "_WORD");
				}
				List<SjxxDto> sjxxList = sjxxService.selectDownloadReportBySjxxDtos(sjxxDtos);
				if(!CollectionUtils.isEmpty(sjxxList)){
					String wjlj = crypt.dCode(sjxxList.get(0).getWjlj());
					String wjm = sjxxList.get(0).getWjm();
					File file = new File(wjlj);
					if (file.exists()) {
						FileInputStream inputFile = null;
						try {
							inputFile = new FileInputStream(file);
							byte[] buffer = new byte[(int) file.length()];  
					        inputFile.read(buffer);
					        String fileString = Base64.getEncoder().encodeToString(buffer);
					        fileNameElement.setText(wjm);
					        fileElement.setText(fileString);
							statusElement.setText("success");
							errorCodeElement.setText("0");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							log.error("读取文件异常："+e);
							errorCodeElement.setText("读取文件异常!");
							return transformToString(document);
						} finally {
							try {
                                if (inputFile != null) {
                                    inputFile.close();
                                }
                            } catch (IOException e) {
								log.error(e.toString());
							}
						}
					}else{
						log.info("-----文件未找到:" + wjlj);
					}
				}else{
					errorCodeElement.setText("未获取到相关文件信息!");
					log.error("未获取到相关文件信息!");
				}
			}else{
				errorCodeElement.setText("未获取到相关送检信息!");
				log.error("未获取到相关送检信息!");
			}
		}else{
			//返回文件压缩包
			sjxxDto.setLastwbbm(lastcode);
			List<SjxxDto> sjxxDtos = sjxxService.getListByLastWbbm(sjxxDto);
			if(!CollectionUtils.isEmpty(sjxxDtos)){
                for (SjxxDto dto : sjxxDtos) {
                    Element infoElement = infosElement.addElement("info");// 创建根节点info
                    Element codeElement = infoElement.addElement("code");
                    codeElement.setText(dto.getWbbm());
                    List<SjwzxxDto> sjwzxxDtos = sjxxService.selectWzxxBySjid(sjxxDtos.get(0));
                    Element sjwzxxsElement = infoElement.addElement("sjwzxxs");// 创建根节点sjwzxxs
                    if (!CollectionUtils.isEmpty(sjwzxxDtos)) {
                        for (SjwzxxDto sjwzxxDto : sjwzxxDtos) {
                            Element sjwzxxElement = sjwzxxsElement.addElement("sjwzxx");// 创建根节点sjwzxx
                            Element wzlxElement = sjwzxxElement.addElement("wzlx");
                            wzlxElement.setText(sjwzxxDto.getWzlx());
                            Element wzfllxElement = sjwzxxElement.addElement("wzfllx");
                            wzfllxElement.setText(sjwzxxDto.getWzfllx());
                            Element wzflElement = sjwzxxElement.addElement("wzfl");
                            wzflElement.setText(sjwzxxDto.getWzfl());
                            Element wzzwmElement = sjwzxxElement.addElement("wzzwm");
                            wzzwmElement.setText(sjwzxxDto.getWzzwm());
                            Element wzywmElement = sjwzxxElement.addElement("wzywm");
                            wzywmElement.setText(sjwzxxDto.getWzywm());
                            Element dqsElement = sjwzxxElement.addElement("dqs");
                            dqsElement.setText(sjwzxxDto.getDqs());
                            Element xdfdElement = sjwzxxElement.addElement("xdfd");
                            xdfdElement.setText(sjwzxxDto.getXdfd());
                            Element smElement = sjwzxxElement.addElement("sm");
                            smElement.setText(sjwzxxDto.getSm());
                            Element szwmElement = sjwzxxElement.addElement("szwm");
                            szwmElement.setText(sjwzxxDto.getSzwm());
                            Element sddsElement = sjwzxxElement.addElement("sdds");
                            sddsElement.setText(sjwzxxDto.getSdds());
                            Element sfdElement = sjwzxxElement.addElement("sfd");
                            sfdElement.setText(sjwzxxDto.getSfd());
                        }
                    }

                    List<SjnyxDto> sjnyxDtos = sjnyxService.getNyxBySjid(sjxxDtos.get(0));
                    Element sjnyxsElement = infoElement.addElement("sjnyxs");// 创建根节点sjnyxs
                    if (!CollectionUtils.isEmpty(sjnyxDtos)) {
                        for (SjnyxDto sjnyxDto : sjnyxDtos) {
                            Element sjnyxElement = sjnyxsElement.addElement("sjnyx");// 创建根节点sjnyx
                            Element jyElement = sjnyxElement.addElement("jy");
                            jyElement.setText(sjnyxDto.getJy());
                            Element ypElement = sjnyxElement.addElement("yp");
                            ypElement.setText(sjnyxDto.getYp());
                            Element jlElement = sjnyxElement.addElement("jl");
                            jlElement.setText(sjnyxDto.getJl());
                            Element xgwzElement = sjnyxElement.addElement("xgwz");
                            xgwzElement.setText(sjnyxDto.getXgwz());
                        }
                    }

                    String jclx = dto.getJclx();
                    dto.setDbs(dbs);
                    if (StringUtil.isBlank(type) || "pdf".equals(type)) {
                        dto.setYwlx(dto.getCskz3().replace("_ONCO", "") + "_" + jclx);
                    } else {
                        dto.setYwlx(dto.getCskz3().replace("_ONCO", "") + "_" + jclx + "_WORD");
                    }
                }
				
				List<SjxxDto> sjxxList = sjxxService.selectDownloadReportBySjxxDtos(sjxxDtos);
				if(!CollectionUtils.isEmpty(sjxxList)){
					String folderName = "UP" + System.currentTimeMillis();
					String storePath = prefix + tempFilePath + BusTypeEnum.IMP_REPORTZIP.getCode() + "/" + folderName;
					mkDirs(storePath);
                    for (SjxxDto dto : sjxxList) {
                        String wjlj = crypt.dCode(dto.getWjlj());
                        String wjm = dto.getWjm();
                        String newWjlj = storePath + "/" + wjm;
                        File file = new File(wjlj);
                        if (file.exists()) {
                            copyFile(wjlj, newWjlj);
                        } else {
                            errorCodeElement.setText("未找到文件!");
                            log.info("-----文件未找到:" + wjlj);
                        }
                    }
					//调用公共方法压缩文件
					String srcZip = storePath+".zip";
					ZipUtil.toZip(storePath, srcZip, true);
					String fileName = folderName+".zip";
					File file = new File(srcZip);
					if (file.exists()) {
						FileInputStream inputFile = null;
						try {
							inputFile = new FileInputStream(file);
							byte[] buffer = new byte[(int) file.length()];  
					        inputFile.read(buffer);
					        String fileString = Base64.getEncoder().encodeToString(buffer);
					        fileNameElement.setText(fileName);
					        fileElement.setText(fileString);
							statusElement.setText("success");
							errorCodeElement.setText("0");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							log.error("读取文件异常："+e);
							errorCodeElement.setText("读取文件异常!");
							return transformToString(document);
						} finally {
							try {
                                if (inputFile != null) {
                                    inputFile.close();
                                }
                            } catch (IOException e) {
								log.error(e.toString());
							}
						}
					}else{
						errorCodeElement.setText("未找到压缩文件!");
						log.info("-----压缩文件未找到:" + srcZip);
					}
				}else{
					errorCodeElement.setText("未获取到相关文件信息!");
					log.error("未获取到相关文件信息!");
				}
			}else{
				errorCodeElement.setText("未获取到相关送检信息!");
				log.error("未获取到相关送检信息!");
			}
		}
		return transformToString(document);
	}
	
	/**
	 * xml转换为字符串格式
	 * @param document
	 * @return
	 */
	private String transformToString(Document document) {
		// TODO Auto-generated method stub
		StringWriter sw = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		try {
			XMLWriter xmlWriter = new XMLWriter(sw, format);
			xmlWriter.write(document);
		} catch (IOException e) {
			log.error(e.toString());
		}finally{
			try {
				sw.close();
			} catch (IOException e) {
				log.error(e.toString());
			}
		}
		return sw.toString();
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public String receiveInspectInfo(String organ, String sign, String param) {
		// TODO Auto-generated method stub
		//先判断安全验证
		RestTemplate restTemplate = new RestTemplate();
		DBEncrypt crypt = new DBEncrypt();
		Map<String, Object> map = sjxxWsService.checkSecurityReceive(organ, param, sign, crypt,false);
		if(!"0".equals(map.get("errorCode"))){
			map.put("status", "fail");
			map.put("errorCode", map.get("errorCode"));
			log.error((String)map.get("errorCode"));
			return map.toString();
		}
		List<SjxxDto> sjxxDtos = readerInspectionXml(param);
        for (SjxxDto sjxxDto : sjxxDtos) {
            //保存数据，默认为80状态，设置附件类型
            sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
            sjxxDto.setYbbh(sjxxDto.getWbbm());
            sjxxDto.setLrry(sjxxDto.getWbbm());
            sjxxDto.setZt(StatusEnum.CHECK_PASS.getCode());
            boolean result = sjxxService.insertDto(sjxxDto);
            if (!result) {
                log.error("本地保存失败！");
            }
            result = sjxxService.insertAll(sjxxDto, sjxxDto.getLrry());
            if (!result) {
                log.error("关联表保存失败！");
            }
            RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_ADD.getCode() + JSONObject.toJSONString(sjxxDto));
            List<String> wjxxs = sjxxDto.getWjxxs();
            List<String> wjms = sjxxDto.getWjms();
            if (!CollectionUtils.isEmpty(wjxxs)&& !CollectionUtils.isEmpty(wjms) && wjxxs.size() == wjms.size()) {
                List<FjcfbDto> fjcfbDtos = new ArrayList<>();
                for (int j = 0; j < wjxxs.size(); j++) {
                    String wjm = wjms.get(j);
                    byte[] decode = Base64.getDecoder().decode(wjxxs.get(j));
                    // 根据日期创建文件夹
                    String fwjlj = prefix + releaseFilePath + sjxxDto.getYwlx() + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/"
                            + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
                    int index = (wjm.lastIndexOf(".") > 0) ? wjm.lastIndexOf(".") : wjm.length() - 1;
                    String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
                    String fwjm = t_name + wjm.substring(index);
                    String wjlj = fwjlj + "/" + fwjm;
                    mkDirs(fwjlj);
                    FjcfbDto fjcfbDto = new FjcfbDto();
                    fjcfbDto.setFjid(StringUtil.generateUUID());
                    fjcfbDto.setYwid(sjxxDto.getSjid());
                    fjcfbDto.setZywid("");
                    fjcfbDto.setXh("1");
                    fjcfbDto.setYwlx(sjxxDto.getYwlx());
                    fjcfbDto.setWjm(wjm);
                    fjcfbDto.setWjlj(crypt.eCode(wjlj));
                    fjcfbDto.setFwjlj(crypt.eCode(fwjlj));
                    fjcfbDto.setFwjm(crypt.eCode(fwjm));
                    fjcfbDto.setZhbj("0");
                    result = fjcfbService.insert(fjcfbDto);
                    if (!result) {
                        log.error("文件名：" + wjm + "附件保存失败！");
                    }
                    FileOutputStream fos = null;
                    BufferedOutputStream output = null;
                    try {
                        mkDirs(fwjlj);
                        fos = new FileOutputStream(wjlj);
                        output = new BufferedOutputStream(fos);
                        output.write(decode);
                    } catch (Exception e) {
                        log.error(e.toString());
                        map.put("status", "fail");
                        map.put("errorCode", e.toString());
                        return map.toString();
                    } finally {
                        closeStream(new Closeable[]{output, fos});
                    }
                    fjcfbDtos.add(fjcfbDto);
                }
                if (!CollectionUtils.isEmpty(fjcfbDtos)) {
                    // 附件排序
                    FjcfbDto fjcfbDto = new FjcfbDto();
                    fjcfbDto.setYwid(sjxxDto.getSjid());
                    fjcfbDto.setYwlx(sjxxDto.getYwlx());
                    result = fjcfbService.fileSort(fjcfbDto);
                    if (!result)
                        log.error("外部编码：" + sjxxDto.getWbbm() + " 附件排序失败！");
                    // 拷贝文件到微信服务器
                    for (FjcfbModel fjcfbModel : fjcfbDtos) {
                        if (fjcfbModel != null) {
                            String wjlj = fjcfbModel.getWjlj();
                            String pathString = crypt.dCode(wjlj);
                            File file = new File(pathString);
                            // 文件不存在不做任何操作
                            if (file.exists()) {
                                byte[] bytesArray = new byte[(int) file.length()];
                                FileInputStream fis;
                                try {
                                    fis = new FileInputStream(file);
                                    fis.read(bytesArray); // read file into bytes[]
                                    fis.close();
                                } catch (FileNotFoundException e) {
                                    // TODO Auto-generated catch block
                                    log.error("未找到文件：" + e);
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    log.error("读写文件异常：" + e);
                                }
                                // 需要给文件的名称
                                ByteArrayResource contentsAsResource = new ByteArrayResource(bytesArray) {
                                    @Override
                                    public String getFilename() {
                                        return file.getName();
                                    }
                                };
                                MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                                paramMap.add("file", contentsAsResource);
                                paramMap.add("fjcfbModel", fjcfbModel);
                                // 发送文件到服务器
                                String reString = restTemplate.postForObject(menuurl + "/wechat/upSaveInspReport", paramMap, String.class);
                                if ("OK".equals(reString)) {
                                    // 更新文件的转换标记为true
                                    result = fjcfbService.updateZhbj(fjcfbModel);
                                    if (!result) {
                                        log.error("附件ID：" + fjcfbModel.getFjid() + " 转换标记更新失败！");
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                log.error("外部编码：" + sjxxDto.getWbbm() + " 附件未上传或信息不一致！");
            }
            // 发送钉钉消息
            String url = menuurl + "/common/view/displayView?view_url=/common/view/inspectionView?ybbh=" + sjxxDto.getYbbh() + "&hzxm=" + sjxxDto.getHzxm();
            JcsjDto jcsjDto = new JcsjDto();
            jcsjDto.setJclb("DINGMESSAGETYPE");
            jcsjDto.setCsdm("INSPECTION_TYPE");
            List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
            if (!CollectionUtils.isEmpty(ddxxglDtos)) {
                String ICOMM_SJ00002 = xxglService.getMsg("ICOMM_SJ00002");
                String ICOMM_SJ00001 = xxglService.getMsg("ICOMM_SJ00001");
                for (DdxxglDto ddxxglDto : ddxxglDtos) {
                    if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
                        talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), ICOMM_SJ00002, StringUtil.replaceMsg(ICOMM_SJ00001,
                                sjxxDto.getYbbh(), sjxxDto.getDb(), sjxxDto.getSjdwmc(), sjxxDto.getYblxmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), url);
                    }
                }
            }
        }
		map.put("status", "success");
		return map.toString();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public String receiveDetectionInfo(String param) {
		try {
			log.error("进入采集结果接收方法，请求xml为================================="+param);
			Map<String,Object> map = new HashMap<>();
			String taskidcode = "";
			String standardcode = "";
			int addNum = 0;

			Document document = XmlUtil.getInstance().parse(param);
			FzjybgDto fzjybgDto = new FzjybgDto();
			String stringValue = "";

			Element root = document.getRootElement();
			Element heartbeat = root.element("heartbeat");
			if ("1".equals(heartbeat.getStringValue())){
				//心跳服务
				log.error("请求为心跳监测=================================");
				String res = generateReceiveResults("1","写入成功");
				log.error("结果采集接收服务响应xml=================="+res);
				return res;
			}else {
				log.error("请求为正常业务=================================");
				Element business = root.element("business");
				Element businessdata = business.element("businessdata");
				Element result = businessdata.element("result");
				List<Element> elements = result.elements();
                for (Element element : elements) {
                    if ("taskid".equals(element.getName())) {
                        taskidcode = element.getText();//任务号
                        fzjybgDto.setTaskid(element.getText());
                    } else if ("resourcecode".equals(element.getName())) {
                        standardcode = element.getText();//取标准任务码
                        fzjybgDto.setResourcecode(element.getText());
                    } else if ("resourcename".equals(element.getName())) {
                        fzjybgDto.setResourcename(element.getText());
                    } else if ("uploadtime".equals(element.getName())) {
                        fzjybgDto.setUploadtime(element.getText());
                    } else if ("resultcode".equals(element.getName())) {
                        fzjybgDto.setResultcode(element.getText());
                    } else if ("resultdesc".equals(element.getName())) {
                        fzjybgDto.setResultdesc(element.getText());
                    } else if ("report".equals(element.getName())) {
                        stringValue = element.getStringValue();
                    }
                }

				String storePath = releaseFilePath + "CONVID_JYBG" +"/"+ "UP"+
						DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
						DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
						DateUtils.getCustomFomratCurrentDate("yyyyMMdd") +"/" ;

				File file = new File(storePath);
				//如果路径不存在，新建
				if(!file.exists()&&!file.isDirectory()) {
					file.mkdirs();
				}
				String filePath = storePath  + fzjybgDto.getTaskid()+".html";
				fzjybgDto.setWjm(fzjybgDto.getTaskid()+".html");//文件名为任务号加html文件后缀
				fzjybgDto.setWjlj(filePath);
				fzjybgDto.setFzjybgid(StringUtil.generateUUID());
				writeBytesToFile(stringValue,filePath);//生成后缀为html的文件
				fzjybgService.insert(fzjybgDto);

				//判断标准码,根据标准码申请+1，检验+2，常规+4
				if ("REQ.C0101.0303.02".equals(standardcode)){ //检验+2
					taskidcode = taskidcode+"jy";
					addNum = 2;
				}else if ("REQ.C0101.0303.01".equals(standardcode)){ //申请+1
					taskidcode = taskidcode+"sq";
					addNum = 1;
				}else if ("REQ.C0101.0303.0201".equals(standardcode)){ //常规+4
					taskidcode = taskidcode+"cg";
					addNum = 4;
				}
				map.put("taskidcode",taskidcode); //S0029rH7rFDs8aj3lqYPOYmsq
				map.put("addNum",addNum);
				log.error("=================taskidcode:"+taskidcode + "     =================addnum:"+addNum);

				//取返回的结果
				if (    "1".equals(fzjybgDto.getResultcode())    ){
					//  1表示上传成功,则更新分子检测信息表中对于单次任务号数据的结果值； -1表示上传失败，不更新表结果
					fzjcxxService.updateScjg(map);
				}
				String res = generateReceiveResults("1","接收成功");
				log.error("结果采集接收服务响应xml=================="+res);
				return res;
			}
		} catch (Exception e) {
			String res = generateReceiveResults("-1",e.getMessage());
			log.error("发生异常，结果采集接收服务响应xml=================="+res);
			return res;
		}
	}

	//取到请求xml中报告report节点里面的CDATA数据写入文件
	public void writeBytesToFile(String content, String filePath) throws IOException {
		log.error("开始生成HTML文件 =================================");
		log.error("文件内容为(即CDATA): ================================="+content);
		log.error("文件路径为: ================================="+filePath);
		byte[] bs= content.getBytes();
		OutputStream out = new FileOutputStream(filePath);
		InputStream is = new ByteArrayInputStream(bs);
		byte[] buff = new byte[1024];
		int len;
		while((len=is.read(buff))!=-1){
			out.write(buff, 0, len);
		}
		is.close();
		out.flush();
		out.close();
		log.error("生成HTML文件结束: =================================");
	}

	//生成采集结果接收服务的xm
	private String generateReceiveResults(String retvalue, String retText) {
		try {
			log.error("开始生成采集结果响应xml =================================");
			XmlUtil xmlUtil = XmlUtil.getInstance();
			Document document = xmlUtil.parse("<messages></messages>");
			Element root = document.getRootElement();

			Element switchset = root.addElement("switchset");
			Element business = root.addElement("business");
			Element extendset = root.addElement("extendset");

			Element visitor = switchset.addElement("visitor");
			Element serviceinf = switchset.addElement("serviceinf");
			Element provider = switchset.addElement("provider");
			switchset.addElement("route");
			switchset.addElement("process");
			switchset.addElement("switchmessage");

			business.addElement("standardcode");
			Element returnmessage = business.addElement("returnmessage");
			Element returnset = business.addElement("returnset");
			business.addElement("datacompress");
			business.addElement("businessdata");

			extendset.addElement("token");
			Element sourceorgan = visitor.addElement("sourceorgan");
			sourceorgan.setText(health_organ);
			Element sourcedomain = visitor.addElement("sourcedomain");
			sourcedomain.setText(health_domain);
			Element servicecode = serviceinf.addElement("servicecode");
			servicecode.setText(receive_code);
			provider.addElement("targetorgan");
			provider.addElement("targetdomain");

			Element retcode = returnmessage.addElement("retcode");
			Element rettext = returnmessage.addElement("rettext");
			retcode.setText(retvalue);
			if (StringUtil.isBlank(retText)){
				retText = "";
			}
			rettext.setText(retText);

			returnset.addElement("rettotal");
			returnset.addElement("retpaging");
			returnset.addElement("retpageindex");
			returnset.addElement("retpageset");

			log.error("结束生成采集结果响应xml =================================");
			return XmlUtil.getInstance().doc2String(document);

		} catch (DocumentException e) {
			log.error("generateReceiveResults出错=================",e.getMessage());
			return null;
		}
	}


	/**
	 * 解析XML参数
	 * @param param
	 * @return
	 */
	public List<SjxxDto> readerInspectionXml(String param){
		//查询基础数据
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.DETECT_TYPE,
						BasicDataTypeEnum.PATHOGENY_TYPE,BasicDataTypeEnum.SD_TYPE,BasicDataTypeEnum.DETECTION_UNIT});
		List<SjdwxxDto> sjdwxxList= sjxxService.getSjdw();//科室
		List<JcsjDto> pathogenylist = jclist.get(BasicDataTypeEnum.PATHOGENY_TYPE.getCode());//关注病原
		List<JcsjDto> samplelist = jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());//标本类型
		List<JcsjDto> datectlist = jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());//检测项目
		List<JcsjDto> expressage = jclist.get(BasicDataTypeEnum.SD_TYPE.getCode());//快递类型
		List<JcsjDto> decetionlist = jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode());//检测单位
		//组装sjxxDtos
		List<SjxxDto> sjxxDtos = new ArrayList<>();
		try {
			Document doc = XmlUtil.getInstance().parse(param);
			Element root = doc.getRootElement();
			List<?> elements = root.elements();
            for (Object o : elements) {
                Element element = (Element) o;
                if ("infos".equals(element.getName())) {
                    List<?> infos = element.elements();
                    for (Object object : infos) {
                        Element info = (Element) object;
                        SjxxDto sjxxDto = new SjxxDto();
                        List<?> c_infos = info.elements();
                        for (Object cInfo : c_infos) {
                            Element c_info = (Element) cInfo;
                            if ("wbbm".equals(c_info.getName())) {
                                sjxxDto.setWbbm(c_info.getText());
                                sjxxDto.setYbbh(c_info.getText());
                            } else if ("hzxm".equals(c_info.getName())) {
                                sjxxDto.setHzxm(c_info.getText());
                            } else if ("bys".equals(c_info.getName())) {
                                List<?> bys = c_info.elements();
                                List<String> list = new ArrayList<>();
                                for (int l = 0; l < bys.size(); l++) {
                                    Element by = (Element) bys.get(l);
                                    list.add(by.getText());
                                    for (JcsjDto jcsjDto : pathogenylist) {
                                        if (by.getText().equals(jcsjDto.getCsmc())) {
                                            list.set(l, jcsjDto.getCsid());
                                            break;
                                        }
                                    }
                                }
                                sjxxDto.setBys(list);
                            } else if ("wjs".equals(c_info.getName())) {
                                List<?> wjs = c_info.elements();
                                List<String> wjms = new ArrayList<>();
                                List<String> wjxxs = new ArrayList<>();
                                for (Object value : wjs) {
                                    Element wj = (Element) value;
                                    List<?> wjxx = wj.elements();
                                    for (Object item : wjxx) {
                                        Element c_wjxx = (Element) item;
                                        if ("wjm".equals(c_wjxx.getName())) {
                                            wjms.add(c_wjxx.getText());
                                        } else if ("wjxx".equals(c_wjxx.getName())) {
                                            wjxxs.add(c_wjxx.getText());
                                        }
                                    }
                                }
                                sjxxDto.setWjms(wjms);
                                sjxxDto.setWjxxs(wjxxs);
                            } else if ("xb".equals(c_info.getName())) {
                                sjxxDto.setXb(c_info.getText());
                            } else if ("nl".equals(c_info.getName())) {
                                sjxxDto.setNl(c_info.getText());
                            } else if ("nldw".equals(c_info.getName())) {
                                sjxxDto.setNldw(c_info.getText());
                            } else if ("dh".equals(c_info.getName())) {
                                sjxxDto.setDh(c_info.getText());
                            } else if ("bgrq".equals(c_info.getName())) {
                                sjxxDto.setBgrq(c_info.getText());
                            } else if ("cyrq".equals(c_info.getName())) {
                                sjxxDto.setCyrq(c_info.getText());
                            } else if ("db".equals(c_info.getName())) {
                                sjxxDto.setDb(c_info.getText());
                            } else if ("sjys".equals(c_info.getName())) {
                                sjxxDto.setSjys(c_info.getText());
                            } else if ("ysdh".equals(c_info.getName())) {
                                sjxxDto.setYsdh(c_info.getText());
                            } else if ("sjdw".equals(c_info.getName())) {
                                sjxxDto.setSjdw(c_info.getText());
                            } else if ("ks".equals(c_info.getName())) {
                                String ks = null;
                                String qtks = null;
                                for (SjdwxxDto sjdwxxDto : sjdwxxList) {
                                    if (sjdwxxDto.getDwmc().equals(c_info.getText())) {
                                        ks = sjdwxxDto.getDwid();
                                        qtks = null;
                                        break;
                                    }
                                    if ("1".equals(sjdwxxDto.getKzcs())) {
                                        ks = sjdwxxDto.getDwid();
                                        qtks = c_info.getText();
                                    }
                                }
                                sjxxDto.setKs(ks);
                                sjxxDto.setQtks(qtks);
                            } else if ("jcdw".equals(c_info.getName())) {
                                sjxxDto.setJcdw(c_info.getText());
                                for (JcsjDto jcsjDto : decetionlist) {
                                    if (jcsjDto.getCsdm().equals(c_info.getText())) {
                                        sjxxDto.setJcdw(jcsjDto.getCsid());
                                        break;
                                    }
                                }
                            } else if ("zyh".equals(c_info.getName())) {
                                sjxxDto.setZyh(c_info.getText());
                            } else if ("cwh".equals(c_info.getName())) {
                                sjxxDto.setCwh(c_info.getText());
                            } else if ("kdlx".equals(c_info.getName())) {
                                sjxxDto.setKdlx(c_info.getText());
                                for (JcsjDto jcsjDto : expressage) {
                                    if (jcsjDto.getCsdm().equals(c_info.getText())) {
                                        sjxxDto.setKdlx(jcsjDto.getCsid());
                                        break;
                                    }
                                }
                            } else if ("jcxm".equals(c_info.getName())) {
                                List<String> jcxmids = new ArrayList<>();
                                for (JcsjDto jcsjDto : datectlist) {
                                    if (jcsjDto.getCskz1().equals(c_info.getText())) {
                                        jcxmids.add(jcsjDto.getCsid());
                                        sjxxDto.setJcxmids(jcxmids);
                                        break;
                                    }
                                }
                            } else if ("yblx".equals(c_info.getName())) {
                                String yblx = null;
                                String yblxmc = null;
                                for (JcsjDto jcsjDto : samplelist) {
                                    if (jcsjDto.getCsmc().equals(c_info.getText())) {
                                        yblx = jcsjDto.getCsid();
                                        yblxmc = null;
                                        break;
                                    }
                                    if ("1".equals(jcsjDto.getCskz1())) {
                                        yblx = jcsjDto.getCsid();
                                        yblxmc = c_info.getText();
                                    }
                                }
                                sjxxDto.setYblx(yblx);
                                sjxxDto.setYblxmc(yblxmc);
                            } else if ("ybtj".equals(c_info.getName())) {
                                sjxxDto.setYbtj(c_info.getText());
                            } else if ("qqzd".equals(c_info.getName())) {
                                sjxxDto.setQqzd(c_info.getText());
                            } else if ("lczz".equals(c_info.getName())) {
                                sjxxDto.setLczz(c_info.getText());
                            } else if ("jqyy".equals(c_info.getName())) {
                                sjxxDto.setJqyy(c_info.getText());
                            } else if ("bz".equals(c_info.getName())) {
                                sjxxDto.setBz(c_info.getText());
                            }
                        }
                        sjxxDtos.add(sjxxDto);
                    }
                }
            }
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		}
		return sjxxDtos;
	}
	
	/**
	 * 根据路径创建文件
	 * 
	 * @param storePath
	 * @return
	 */
	private boolean mkDirs(String storePath){
		File file = new File(storePath);
		if (file.isDirectory())
		{
			return true;
		}
		return file.mkdirs();
	}
	
	/**
	 * 关闭流
	 * 
	 * @param streams
	 */
	private static void closeStream(Closeable[] streams){
		if (streams == null || streams.length < 1)
			return;
        for (Closeable closeable : streams) {
            try {
                if (null != closeable) {
                    closeable.close();
                }
            } catch (IOException e) {
                log.error(e.toString());
            }
        }
	}
	
	/**
	 * 复制文件
	 * @param src
	 * @param dest
	 * @return
	 */
    public boolean copyFile(String src, String dest){
    	boolean flag = false;
    	FileInputStream in = null;
    	FileOutputStream out = null;
		try {
			in = new FileInputStream(src);
			File file = new File(dest);
	        if(!file.exists())
	            file.createNewFile();
	        out = new FileOutputStream(file);
	        int c;
	        byte[] buffer = new byte[1024];
	        while((c = in.read(buffer)) != -1){
	            for(int i = 0; i < c; i++)
	                out.write(buffer[i]);        
	        }
	        flag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		} finally {
	        try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
				// TODO Auto-generated catch block
                log.error(e.toString());
			}
	    }
		return flag;
    }
}
