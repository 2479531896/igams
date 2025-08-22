package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.YyxxDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.service.svcinterface.IYyxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/common")
public class CommonFileController extends BaseController{
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IYyxxService yyxxService;
	@Autowired
	private RedisUtil redisUtil;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;

	@Value("${matridx.operate.filePath:}")
	private String FilePath;

	@Value("${matridx.wechat.serverapplicationurl:}")
	private String serverapplicationurl;
	private final Logger log = LoggerFactory.getLogger(CommonFileController.class);
	
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;

	/**
	 * C#端文件上传
	 */
	@RequestMapping(value="/file/saveAndDealImportFile")
	@ResponseBody
	public Map<String, Object> saveAndDealImportFile(FjcfbDto fjcfbDto,HttpServletRequest request){
		log.error("进入C#文件上传接口");
		Map<String, Object> result = new HashMap<>();
		try{
			if(StringUtil.isNotBlank(fjcfbDto.getYwlx())) {
				String file_tag = "";
				for (Part part : request.getParts()) {
					String str = part.getName();
					if (str.endsWith("_file") || str.endsWith("_file_D") || str.endsWith("_file_R") || str.endsWith("_file_C") || str.endsWith("_file_z") || str.endsWith("_file_q")
							||str.endsWith("_file_1")||str.endsWith("_file_2")||str.endsWith("_file_3")) {
						file_tag = str;
						break;
					}
				}
				if(StringUtil.isBlank(file_tag)) {
					result.put("status", "fail");
					result.put("msg", "未获取文件");
					return result;
				}

				List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(file_tag);
				MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
				paramMap.add("db",request.getParameter("db"));
				paramMap.add("flag",request.getParameter("flag"));
				paramMap.add("dwmc",request.getParameter("dwmc"));//是否考虑配置在C#的配置文件
				MultipartFile[] imp_file = new MultipartFile[files.size()];
				files.toArray(imp_file);
				if(imp_file.length>0){
					User user = getLoginInfo(request);
					fjcfbDto.setLrry(user.getYhid());
					fjcfbDto.setCheckFlg("0"); //存入检查标记，0不做检查
					boolean isSuccess = fjcfbService.save2TempFile(imp_file, fjcfbDto,user);
					if(!isSuccess){
						result.put("status", "fail");
						result.put("msg", xxglService.getModelById("ICOM00002").getXxnr());
						return result;
					}
					paramMap.add("filePath",fjcfbDto.getLsbcdz());
					//判断服务器url是否为空，如果不为空发送rabbit消息，返回success,如果为空，继续执行。
					if(StringUtil.isNotBlank(applicationurl)){
						//localization保存成功标本信息后post接口，将文件信息转换成sjxxDto
						log.error("判断是否将文件标本信息转换为sjxxDto并发送给服务器,db:"+request.getParameter("db")+",flag:"+request.getParameter("flag")+",jcdwdm:"+request.getParameter("jcdwdm"));
						if(paramMap.get("db")!=null && StringUtil.isNotBlank(String.valueOf(paramMap.get("db")))){
							log.error("开始将文件标本信息转换为sjxxDto并发送给服务器,db:"+request.getParameter("db")+",flag:"+request.getParameter("flag")+",jcdwdm:"+request.getParameter("jcdwdm"));
							RestTemplate restTemplate=new RestTemplate();
							restTemplate.postForObject(applicationurl + "/ws/localization/receiveData", paramMap, Map.class);
						}
						result.put("status", "success");
						result.put("msg", xxglService.getModelById("ICOM00001").getXxnr());

					}else{
						log.error("location接收");
						if (StringUtil.isNotBlank(request.getParameter("hospitalName"))){
							YyxxDto yyxxDto = new YyxxDto();
							yyxxDto.setDwmc(request.getParameter("hospitalName"));
							List<YyxxDto> yyxxDtos = yyxxService.queryByDwmc(yyxxDto);
							if (!CollectionUtils.isEmpty(yyxxDtos)){
								Map<String,String> jsonmap = new HashMap<>();
								jsonmap.put("sjdw",yyxxDtos.get(0).getDwid());
								jsonmap.put("sjdwmc",yyxxDtos.get(0).getDwmc());
								jsonmap.put("jcxmpp",yyxxDtos.get(0).getJcxmpp());
								jsonmap.put("txtSet",yyxxDtos.get(0).getCskz4());
								jsonmap.put("db",request.getParameter("db"));
								jsonmap.put("jcdwdm",request.getParameter("jcdwdm"));
								redisUtil.hset("IMP_:_"+fjcfbDto.getFjid(),"settings", JSONObject.toJSONString(jsonmap));

							} else {
								result.put("status", "fail");
								result.put("msg", "医院信息匹配失败！");
								return result;
							}
						}
						isSuccess = fjcfbService.save2Db(fjcfbDto,user);
						result.put("status", isSuccess?"success":"fail");
						result.put("msg", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());

					}
				}else{
					result.put("status", "fail");
					result.put("msg", "未获取文件");
				}
			}
		} catch (Exception e) {
			result.put("status", "fail");
			result.put("msg", e.getMessage());
			log.error("saveImportFile error:"+e.getMessage());
		}
		return result;
	}

	
	/**
	 * 客户端选择文件后自动上传处理，对文件的格式进行检查，同时把检查结果返回给页面
	 */
	@RequestMapping(value="/file/saveImportFile")
	@ResponseBody
	public Map<String, Object> saveImportFile(FjcfbDto fjcfbDto,HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		try{
			if(StringUtil.isNotBlank(fjcfbDto.getYwlx())) {
				//获取基础数据，文件类型权限设置
				List<JcsjDto> wjlxqxlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.FILETYPE_PERMISSION_SETTINGS.getCode());
				//判断文件类型权限设置是否为空，为空提示设置上传文件类型权限
				if(wjlxqxlist!=null && !wjlxqxlist.isEmpty()) {
					String suffix = "";
					String defau = "";
					for (JcsjDto jcsjDto : wjlxqxlist) {
						if(jcsjDto.getCsdm().equals(fjcfbDto.getYwlx())) { //通过文件业务类型与基础数据代码比较
							suffix = jcsjDto.getCskz1(); //参数扩展1为可上传文件后缀
						}
						if("1".equals(jcsjDto.getSfmr())) {
							defau = jcsjDto.getCskz1(); //获取默认的文件类型权限
						}
					}
					//判断是否设置默认
					if(StringUtil.isBlank(defau)) {
						result.put("status", "fail");
						result.put("msg", "请设置默认文件类型权限！");
						return result;
					}
					//判断这个文件类型是否设置文件权限
					if(StringUtil.isBlank(suffix)) {
						//如果没有设置文件权限，使用默认的文件权限设置
						suffix = defau;
					}
					List<String> list = new ArrayList<>();
					if(StringUtil.isNotBlank(suffix)) { //判断是否为空
						//转为小写
						suffix = suffix.toLowerCase();
						//转为list
						String[] str = suffix.split(",");
						list = Arrays.asList(str);
					}
					
					String file_tag = "";
					for (Part part : request.getParts()) {
						String str = part.getName();
						if (str.endsWith("_file") || str.endsWith("_file_D") || str.endsWith("_file_R") || str.endsWith("_file_C") || str.endsWith("_file_z") || str.endsWith("_file_q")
							||str.endsWith("_file_1")||str.endsWith("_file_2")||str.endsWith("_file_3")) {
							file_tag = str;
							break;
						}
					}
					if(StringUtil.isBlank(file_tag)) {
						result.put("status", "fail");
						result.put("msg", "未获取文件");
						return result;
					}
					
					List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(file_tag); 
					MultipartFile[] imp_file = new MultipartFile[files.size()];
					files.toArray(imp_file);
					
					if(!files.isEmpty()) {
						//判断是否增加文件后缀限制
						for (MultipartFile file : files) {
							if (!list.isEmpty()) {
								boolean resu = false; //标记，判断是否符合限制
								for (String s : list) {
									if (file.getOriginalFilename().toLowerCase().endsWith(s)) {
										resu = true;
										break;
									}
								}
								if (!resu) {
									log.error("=================================文件上传格式不对,文件名为" + file.getOriginalFilename());
									result.put("status", "fail");
									result.put("msg", "文件上传格式不对,文件名为" + file.getOriginalFilename() + ",请重新上传!(只允许上传后缀为" + suffix + "的文件)");
									return result;
								}
							}
						}
					}
					
					if(imp_file.length>0){
						User user = getLoginInfo(request);
						
						fjcfbDto.setLrry(user.getYhid());
						boolean isSuccess = fjcfbService.save2TempFile(imp_file, fjcfbDto,user);
						if (StringUtil.isNotBlank(request.getParameter("hospitalName"))){
							YyxxDto yyxxDto = new YyxxDto();
							yyxxDto.setDwmc(request.getParameter("hospitalName"));
							List<YyxxDto> yyxxDtos = yyxxService.queryByDwmc(yyxxDto);
							if (!CollectionUtils.isEmpty(yyxxDtos)){
								Map<String,String> jsonmap = new HashMap<>();
								jsonmap.put("sjdw",yyxxDtos.get(0).getDwid());
								jsonmap.put("sjdwmc",yyxxDtos.get(0).getDwmc());
								jsonmap.put("jcxmpp",yyxxDtos.get(0).getJcxmpp());
								jsonmap.put("txtSet",yyxxDtos.get(0).getCskz4());
								if (StringUtil.isNotBlank(yyxxDtos.get(0).getCskz3())) {
									String[] split = yyxxDtos.get(0).getCskz3().split(",");
									jsonmap.put("db",split[1]);
									jsonmap.put("jcdwdm",split[3]);
								}
								redisUtil.hset("IMP_:_"+fjcfbDto.getFjid(),"settings", JSONObject.toJSONString(jsonmap));
							} else {
								result.put("status", "fail");
								result.put("msg", "医院信息匹配失败！");
								return result;
							}
						}
						result.put("fjcfbDto", fjcfbDto);
						result.put("fjid", fjcfbDto.getFjid());
						result.put("status", isSuccess?"success":"fail");
						result.put("msg", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
					}else{
						result.put("status", "fail");
						result.put("msg", "未获取文件");
					}
				}else {
					result.put("status", "fail");
					result.put("msg", "请设置上传文件类型权限!");
					return result;
				}			
			}
		} catch (Exception e) {
			e.printStackTrace();//用于错误日志打印
			result.put("status", "fail");
			result.put("msg", e.getMessage());
		}
		return result;
	}

	/**
	 * 文件删除
	 */
	@RequestMapping(value="/file/deleteFileDir")
	@ResponseBody
	public Map<String, Object> deleteFileDir(String filePath){
		Map<String,Object> result = new HashMap<>();
		Path path = Paths.get(FilePath+filePath,"");
		log.error("path:"+ path);
		try {
			boolean exists = Files.deleteIfExists(path);//返回值void
			if (!exists){
				result.put("status", "fail");
				result.put("msg", "删除失败！");
				return result;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		result.put("status","success");
		result.put("message","删除成功！");
		return result;
	}

	/**
	 * 文件上传
	 */
	@RequestMapping(value="/file/fileUpload")
	@ResponseBody
	public Map<String, Object> fileUpload(MultipartFile file,String path){
		Map<String, Object> result = new HashMap<>();
		File toFile;
		if (file==null || file.getSize() <= 0) {
			result.put("status","fail");
			result.put("message","为获取到文件！");
			return result;
		} else {
			InputStream ins = null;
			OutputStream os = null;
			try {
				/*获取文件原名称*/
				String originalFilename = file.getOriginalFilename();
				toFile = new File(FilePath+path + File.separator + originalFilename);
				ins = file.getInputStream();
				os = new FileOutputStream(toFile);
				int bytesRead;
				byte[] buffer = new byte[8192];
				while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				result.put("status","success");
				result.put("message","上传成功！");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (os != null) {
						os.close();
					}
					if (ins != null) {
						ins.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		return result;
	}

	/**
	 * 文件infoList
	 */
	@RequestMapping(value="/file/getFileList")
	@ResponseBody
	public Map<String, Object> getFileList(FjcfbDto fjcfbDto){
		Map<String, Object> result = new HashMap<>();
		List<FjcfbDto> pagedDtoList = fjcfbService.getPagedDtoList(fjcfbDto);
		result.put("fileList",pagedDtoList);
		return result;
	}
	/**
	 * 文件正式保存
	 */
	@RequestMapping(value="/file/saveFile")
	@ResponseBody
	public Map<String, Object> saveFile(FjcfbDto fjcfbDto){
		Map<String, Object> result = new HashMap<>();
		//文件复制到正式文件夹，插入信息至正式表
		if(!fjcfbDto.getFjids().isEmpty()){
			for (int i = 0; i < fjcfbDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(fjcfbDto.getFjids().get(i),BusTypeEnum.IMP_DOCKER.getCode());
				if(!saveFile){
					result.put("status","fail");
					result.put("message","保存失败！");
					return result;
				}

			}
		}
		result.put("status","success");
		result.put("message","保存成功");
		return result;
	}
	/**
	 * 显示检查结果后，用户点击继续，则进行实际的导入操作
	 */
	@RequestMapping(value="/file/saveImportRecord")
	@ResponseBody
	public Map<String, Object> saveImportRecord(FjcfbDto fjcfbDto,HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		try{
			User user = getLoginInfo(request);
			fjcfbDto.setLrry(user.getYhid());
			boolean isSuccess;
			if(BusTypeEnum.IMPORT_INSPECTION.getCode().equals(fjcfbDto.getYwlx())){
				isSuccess = fjcfbService.save2Inspection(fjcfbDto,user);
			}else{
				isSuccess = fjcfbService.save2Db(fjcfbDto,user);
			}

			result.put("status", isSuccess?"success":"fail");
			result.put("msg", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			result.put("fjcfbDto", fjcfbDto);
		} catch (Exception e) {
			result.put("status", "fail");
			result.put("msg", e.getMessage());
		}
		return result;
	}
	
	/**
	 * 文件下载处理（用于下载导入模板用的一个方法）
	 */
	@RequestMapping(value="/file/downFile")
	public String downFile(HttpServletRequest request,HttpServletResponse response){
		
		String wjlx = request.getParameter("wjlx");
		wjlx = wjlx.replace("/", "").replace("\\", "").replace(" ", "");
		
		String path = "templates/imp/" + wjlx;
		InputStream iStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);

        response.setHeader("content-type", "application/octet-stream");
        //指明为下载
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + wjlx);
        
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        
        OutputStream os = null; //输出流
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(iStream);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                os.flush();
                i = bis.read(buffer);
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
        	log.error(e.toString());
        }
        try {
			if (bis != null) {
				bis.close();
			}
			if (os != null) {
				os.flush();
				os.close();
			}
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	log.error(e.toString());
        }
        return null;
	}
	
	/**
	 * 文件下载（用于普通的文件下载使用）
	 */
	@RequestMapping(value="/file/downloadFile")
	public String downloadFile(FjcfbDto fjcfbDto,HttpServletResponse response,HttpServletRequest request){
		String filePath;
		String wjm;
		if (StringUtil.isNotBlank(fjcfbDto.getTemporary()) && !"null".equals(fjcfbDto.getTemporary())){
			filePath = fjcfbDto.getWjlj();
			wjm = fjcfbDto.getWjm();
		}else {
			FjcfbDto t_fjcfbDto = fjcfbService.getDtoWithScbjNotOne(fjcfbDto);
			if(t_fjcfbDto!=null) {
				String wjlj = t_fjcfbDto.getWjlj();
				wjm = t_fjcfbDto.getWjm();
				DBEncrypt crypt = new DBEncrypt();
				filePath = crypt.dCode(wjlj);
			}else{
				Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+fjcfbDto.getFjid());
				if(mFile!=null&&mFile.size()>0){
					filePath = (String)mFile.get("wjlj");
					wjm = (String)mFile.get("wjm");
				}else{
					log.error("对不起，系统未找到相应文件! downloadFile");
					return null;
				}
			}

		}
		File downloadFile = new File(filePath);
		response.setContentLength((int) downloadFile.length());
		String agent = request.getHeader("user-agent");
		//log.error("文件下载 agent=" + agent);
		//指明为下载
		response.setHeader("content-type", "application/octet-stream");
		/*if(wjm != null){
			wjm = URLEncoder.encode(wjm, "utf-8");
		}*/
		if(wjm != null){
			if (agent.contains("iPhone") || agent.contains("Trident")){
				//iphone手机 微信内置浏览器 下载
				if (agent.contains("MicroMessenger")|| agent.contains("micromessenger")){
//						byte[] bytes = agent.contains("MSIE") ? wjm.getBytes() : wjm.getBytes("UTF-8");
//						wjm = new String(bytes, "ISO-8859-1");
					wjm = URLEncoder.encode(wjm, StandardCharsets.UTF_8);
					response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", wjm));// 文件名外的双引号处理firefox的空格截断问题
				}else {
					//iphone手机 非微信内置浏览器 下载 或 ie浏览器 下载
					wjm = URLEncoder.encode(wjm, StandardCharsets.UTF_8);
					response.setHeader("Content-Disposition","attachment;filename*=UTF-8''" + wjm);
				}
			}else {
				wjm = URLEncoder.encode(wjm, StandardCharsets.UTF_8);
				response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", wjm));// 文件名外的双引号处理firefox的空格截断问题
			}
		}
		if (wjm != null) {
			log.error("文件下载 准备完成 wjm=" + URLDecoder.decode(wjm, StandardCharsets.UTF_8));
		}

		byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        InputStream iStream;
        OutputStream os = null; //输出流
        try {
        	iStream = new FileInputStream(filePath);
            os = response.getOutputStream();
            bis = new BufferedInputStream(iStream);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                os.flush();
                i = bis.read(buffer);
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
			log.error("文件下载 写文件异常：" + e);
        }
        try {
        	if(bis!=null)
        		bis.close();
        	if(os!=null) {
        		os.flush();
            	os.close();
        	}
        } catch (IOException e) {
            // TODO Auto-generated catch block
			log.error("文件下载 关闭文件流异常：" + e);
			return null;
        }
        return null;
	}
	
	/**
	 * 原文件下载（用于普通的原文件下载使用）
	 */
	@RequestMapping(value="/file/predownloadFile")
	public String predownloadFile(FjcfbDto fjcfbDto,HttpServletResponse response){
		FjcfbDto t_fjcfbDto = fjcfbService.getDto(fjcfbDto);
		String wjlj = t_fjcfbDto.getYswjlj();
		String wjm = t_fjcfbDto.getYswjm();
		DBEncrypt crypt = new DBEncrypt();
		String filePath = crypt.dCode(wjlj);
		File downloadFile = new File(filePath);
		if(wjm != null){
			wjm = URLEncoder.encode(wjm, StandardCharsets.UTF_8);
		}
		response.setHeader("content-type", "application/octet-stream");
		response.setContentLength((int) downloadFile.length());
        //指明为下载
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + wjm);
        
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        InputStream iStream;
        OutputStream os = null; //输出流
        try {
        	iStream = new FileInputStream(filePath);
            os = response.getOutputStream();
            bis = new BufferedInputStream(iStream);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                os.flush();
                i = bis.read(buffer);
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            if(bis!=null)
                bis.close();
            if(os != null)
            {
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
	}
	
	/**
	 * 文件删除(包括删除服务器的文件)
	 */
	@RequestMapping(value="/file/delFile")
	@ResponseBody
	public Map<String, Object> delFile(FjcfbDto fjcfbDto, HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		//获取用户信息
		User user = getLoginInfo(request);
		fjcfbDto.setScry(user.getYhid());
		
		boolean isSuccess = fjcfbService.delFile(fjcfbDto);
		result.put("status", isSuccess?"success":"fail");
		result.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        return result;
	}

	/**
	 * 文件删除（删除附件存放表，不删除服务器文件）
	 */
	@RequestMapping(value="/file/delFileOnlyFjcfb")
	@ResponseBody
	public Map<String, Object> delFileOnlyFjcfb(FjcfbDto fjcfbDto, HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		//获取用户信息
		User user = getLoginInfo(request);
		fjcfbDto.setScry(user.getYhid());

		boolean isSuccess = fjcfbService.delFileOnlyFjcfb(fjcfbDto);
		result.put("status", isSuccess?"success":"fail");
		result.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return result;
	}
	
	/**
	 * 导入页面显示转换后的数据列表
	 */
	@RequestMapping(value ="/file/listImpInfo")
	@ResponseBody
	public Map<String,Object> pageGetImportInfo(FjcfbDto fjcfbDto,HttpServletRequest request){
		User user=getLoginInfo(request);
		List<Map<String, String>> t_List = fjcfbService.getImpList(fjcfbDto,request,user);

		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", t_List== null?0:t_List.size());
		result.put("rows", t_List);
		return result;
	}


	/**
	 * 检查导入进度
	 */
	@RequestMapping(value ="/file/checkImpInfo")
	@ResponseBody
	public Map<String,Object> checkImpInfo(FjcfbDto fjcfbDto){
		
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递

		return fjcfbService.checkImpFileProcess(fjcfbDto.getFjid());
	}
	
	/**
	 * 检查插入数据库的进度
	 */
	@RequestMapping(value ="/file/checkImpSave")
	@ResponseBody
	public Map<String,Object> checkImpSave(FjcfbDto fjcfbDto){
		
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递

		return fjcfbService.checkImpRecordProcess(fjcfbDto.getFjid());
	}
	
	@RequestMapping(value="/file/pdfPreview")
	public ModelAndView pdfPreview(FjcfbDto fjcfbDto,HttpServletRequest request) {
		try {

			if("1".equals(fjcfbDto.getPdf_flg())) {
				String filepath = urlPrefix+"/common/file/getFileInfoFlg?fjid=" + fjcfbDto.getFjid() + "&access_token=" + fjcfbDto.getAccess_token();
				if (StringUtil.isNotBlank(fjcfbDto.getYwid())){
					filepath+="&ywid="+fjcfbDto.getYwid();
				}
				ModelAndView mv = new ModelAndView("common/file/viewer");
				//mv.addObject("file",URLEncoder.encode(filepath, "UTF-8"));
				mv.addObject("file",filepath);
				if(StringUtil.isNotBlank(request.getParameter("syFlag"))){
					User user = getLoginInfo(request);
					//水印信息
					mv.addObject("syText",user.getYhm()+user.getZsxm());
					mv.addObject("syFlag",request.getParameter("syFlag"));
				}
				return mv;
			}else {
				String filepath = urlPrefix+"/common/file/getFileInfo?fjid=" + fjcfbDto.getFjid() + "&access_token=" + fjcfbDto.getAccess_token();
				if (StringUtil.isNotBlank(fjcfbDto.getYwid())){
					filepath+="&ywid="+fjcfbDto.getYwid();
				}
				ModelAndView mv = new ModelAndView("common/file/viewer");
				//mv.addObject("file",URLEncoder.encode(filepath, "UTF-8"));
				if(StringUtil.isNotBlank(request.getParameter("syFlag"))){
					User user = getLoginInfo(request);
					//水印信息
					mv.addObject("syText",user.getYhm()+user.getZsxm());
					mv.addObject("syFlag",request.getParameter("syFlag"));
				}
				mv.addObject("file",filepath);
				return mv;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping(value="/file/pdfPreviewTrain")
	public ModelAndView pdfPreviewTrain(FjcfbDto fjcfbDto,HttpServletRequest request) {
		try {
			if("1".equals(fjcfbDto.getPdf_flg())) {
				String filepath = urlPrefix+"/common/file/getFileInfoFlg?fjid=" + fjcfbDto.getFjid() + "&access_token=" + fjcfbDto.getAccess_token();
				if (StringUtil.isNotBlank(fjcfbDto.getYwid())){
					filepath+="&ywid="+fjcfbDto.getYwid();
				}
				ModelAndView mv = new ModelAndView("common/file/viewerTrain");
				//mv.addObject("file",URLEncoder.encode(filepath, "UTF-8"));
				mv.addObject("file",filepath);
				mv.addObject("xh",request.getParameter("xh"));
				mv.addObject("ddid",request.getParameter("ddid"));
				mv.addObject("zlbdm",request.getParameter("zlbdm"));
				mv.addObject("gqsjbj",request.getParameter("gqsjbj"));
				mv.addObject("access_token",fjcfbDto.getAccess_token());
				String urlString = "/common/file/pdfPreviewTrain?fjid="+fjcfbDto.getFjid()+"&access_token="+fjcfbDto.getAccess_token()+"&syFlag=1&xh="+request.getParameter("xh")
						+"&ddid="+request.getParameter("ddid")+"&zlbdm="+request.getParameter("zlbdm")+"&gqsjbj="+request.getParameter("gqsjbj");
				mv.addObject("urlString",urlString);
				if(StringUtil.isNotBlank(request.getParameter("syFlag"))){
					User user = getLoginInfo(request);
					//水印信息
					mv.addObject("syText",user.getYhm()+user.getZsxm());
					mv.addObject("syFlag",request.getParameter("syFlag"));
				}
				return mv;
			}else {
				String filepath = urlPrefix+"/common/file/getFileInfo?fjid=" + fjcfbDto.getFjid() + "&access_token=" + fjcfbDto.getAccess_token();
				if (StringUtil.isNotBlank(fjcfbDto.getYwid())){
					filepath+="&ywid="+fjcfbDto.getYwid();
				}
				ModelAndView mv = new ModelAndView("common/file/viewerTrain");
				mv.addObject("xh",request.getParameter("xh"));
				mv.addObject("ddid",request.getParameter("ddid"));
				mv.addObject("zlbdm",request.getParameter("zlbdm"));
				mv.addObject("gqsjbj",request.getParameter("gqsjbj"));
				mv.addObject("access_token",fjcfbDto.getAccess_token());
				String urlString = "/common/file/pdfPreviewTrain?fjid="+fjcfbDto.getFjid()+"&access_token="+fjcfbDto.getAccess_token()+"&syFlag=1&xh="+request.getParameter("xh")
						+"&ddid="+request.getParameter("ddid")+"&zlbdm="+request.getParameter("zlbdm")+"&gqsjbj="+request.getParameter("gqsjbj");
				mv.addObject("urlString",urlString);
				//mv.addObject("file",URLEncoder.encode(filepath, "UTF-8"));
				if(StringUtil.isNotBlank(request.getParameter("syFlag"))){
					User user = getLoginInfo(request);
					//水印信息
					mv.addObject("syText",user.getYhm()+user.getZsxm());
					mv.addObject("syFlag",request.getParameter("syFlag"));
				}
				mv.addObject("file",filepath);
				return mv;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * word在线预览还未完成，普通做法是要转换成pdf然后才可以，但公司内不太适合，应该是暂时废弃的
	 */
	@RequestMapping(value="/file/wordPreview")
	public ModelAndView wordPreview(FjcfbDto fjcfbDto) {
		try {
			
			ModelAndView mv = new ModelAndView("common/file/filepreview");
			
			mv.addObject("fjcfbDto",fjcfbDto);
			
			return mv;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 预览普通pdf文件
	 */
	@RequestMapping(value="/file/getFileInfo")
	public void getFileInfo(FjcfbDto fjcfbDto,HttpServletResponse response){
		
		FjcfbModel fjcfbModel = fjcfbService.getModel(fjcfbDto);

		if(fjcfbModel == null) {
			return;
		}
		
		DBEncrypt encrypt = new DBEncrypt();
		String filePath = encrypt.dCode(fjcfbModel.getWjlj());
		if(StringUtil.isBlank(filePath))
			return;
        
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        
        OutputStream os = null; //输出流
        try {
    		InputStream iStream = new FileInputStream(filePath);

    		//设置Content-Disposition  
            //response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fjcfbModel.getWjm(), "UTF-8"));
            //指明为流
            //response.setContentType("application/octet-stream; charset=utf-8");
            
            os = response.getOutputStream();
            bis = new BufferedInputStream(iStream);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                os.flush();
                i = bis.read(buffer);
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
			if (bis != null) {
				bis.close();
			}
			if (os != null) {
				os.flush();
				os.close();
			}
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	/**
	 * 预览word转换的pdf
	 */
	@RequestMapping(value="/file/getFileInfoFlg")
	public void getFileInfoFlg(FjcfbDto fjcfbDto,HttpServletResponse response){
		DBEncrypt encrypt = new DBEncrypt();
		FjcfbModel fjcfbModel = fjcfbService.getModel(fjcfbDto);
		String filePath = null;
		if(StringUtil.isNotBlank(fjcfbModel.getZhwjxx())) {
			filePath = encrypt.dCode(fjcfbModel.getZhwjxx());
		}else {
			String wjm = fjcfbModel.getWjm();
			if(StringUtil.isNotBlank(wjm) && (wjm.substring(wjm.lastIndexOf(".")).equalsIgnoreCase(".pdf") || wjm.substring(wjm.lastIndexOf(".")).equalsIgnoreCase(".PDF") )) {
				filePath = encrypt.dCode(fjcfbModel.getWjlj());
			}
		}
        try{
        	byte[] buffer = new byte[1024];
            BufferedInputStream bis;
            OutputStream os; //输出流
            try {
        		InputStream iStream = new FileInputStream(filePath);

        		//设置Content-Disposition  
                //response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fjcfbModel.getWjm(), "UTF-8"));
                //指明为流
                //response.setContentType("application/octet-stream; charset=utf-8");
                
                os = response.getOutputStream();
                bis = new BufferedInputStream(iStream);
                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    os.flush();
                    i = bis.read(buffer);
                }
                try {
                    bis.close();
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		} catch (Exception e){
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * word转pdf
	 */
	@RequestMapping(value="/file/wordtopdf")
	@ResponseBody
	public String WordToPdf() {
		fjcfbService.selectWord();
		return "OK";
	}

	/**
	 * 根据ywid ， ywlx 查找redis中得附件
	 */
	@RequestMapping("/file/pagedataPhoneUploadStateCheck")
	@ResponseBody
	public Map<String,Object> phoneUploadStateCheck(FjcfbDto fjcfbDto){
		Map<String,Object>map=new HashMap<>();
		Map<Object,Object> redisMap=redisUtil.hmget("COM_FILE_UPLOAD:"+fjcfbDto.getYwid()+"_"+fjcfbDto.getYwlx());
		List<FjcfbDto> fjcfbDtoList=new ArrayList<>();
		Map<String,Object>newMap=new HashMap<>();
		map.put("update","false");
		if(redisMap!=null && !redisMap.isEmpty() && "true".equals(redisMap.get("uploadFlg").toString())){
			if(redisMap.get("fjcfbDtoList")!=null){
				fjcfbDtoList= JSONObject.parseArray(redisMap.get("fjcfbDtoList").toString(),FjcfbDto.class);
			}
			for(Object key:redisMap.keySet()){
				newMap.put(key.toString(),redisMap.get(key));
			}
			newMap.put("uploadFlg","false");
			map.put("update","true");
			redisUtil.hmset("COM_FILE_UPLOAD:"+fjcfbDto.getYwid()+"_"+fjcfbDto.getYwlx(), newMap,1800);
		}
		if(!fjcfbDtoList.isEmpty()){
			map.put("fjcfbDtoList",fjcfbDtoList);
			String fjids="";
			for (FjcfbDto dto : fjcfbDtoList) {
				Map<Object, Object> mFile = redisUtil.hmget("IMP_:_" + dto.getFjid());
				dto.setWjlj((String) mFile.get("wjlj"));
				if (StringUtil.isBlank(fjids)) {
					fjids += dto.getFjid();
				} else {
					fjids += "," + dto.getFjid();
				}
			}
			map.put("fjids",fjids);
		}
		return map;
	}


	/**
	 * 通用上传中间页面
	 */
	@RequestMapping("/file/pagedataPhoneUpload")
	public ModelAndView phoneUpload(FjcfbDto fjcfbDto){
		ModelAndView mv = new ModelAndView("common/file/phonefile");
		if(StringUtil.isBlank(fjcfbDto.getYwid())){
			fjcfbDto.setYwid(StringUtil.generateUUID());
		}

		mv.addObject("urlPrefix",urlPrefix);
		mv.addObject("switchoverurl", applicationurl+"/ws/pathogen/pagedataPhoneUploadFile");
		mv.addObject("fjcfbDto", fjcfbDto);
		return mv;
	}

	@RequestMapping("/file/pagedataDelPhoneUpload")
	@ResponseBody
	public Map<String,Object> pagedataDelPhoneUpload(FjcfbDto fjcfbDto){
		Map<String,Object>map=new HashMap<>();
		redisUtil.del("COM_FILE_UPLOAD:"+fjcfbDto.getYwid()+"_"+fjcfbDto.getYwlx());
		return map;
	}


	/**
	 * 文件下载（用于普通的文件下载使用）
	 */

	@RequestMapping(value="/file/downloadFileByFilePath")
	public String downloadFileByFilePath(String filePath, HttpServletResponse response, HttpServletRequest request){
		File downloadFile = new File(filePath);
		response.setContentLength((int) downloadFile.length());
		String agent = request.getHeader("user-agent");
		//log.error("文件下载 agent=" + agent);
		//指明为下载
		response.setHeader("content-type", "application/octet-stream");
		/*if(wjm != null){
			wjm = URLEncoder.encode(wjm, "utf-8");
		}*/
		String wjm = filePath.substring(filePath.lastIndexOf("/")+1);
		if (agent.contains("iPhone") || agent.contains("Trident")){
			//iphone手机 微信内置浏览器 下载
			if (agent.contains("MicroMessenger")|| agent.contains("micromessenger")){
//						byte[] bytes = agent.contains("MSIE") ? wjm.getBytes() : wjm.getBytes("UTF-8");
//						wjm = new String(bytes, "ISO-8859-1");
				wjm = URLEncoder.encode(wjm, StandardCharsets.UTF_8);
				response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", wjm));// 文件名外的双引号处理firefox的空格截断问题
			}else {
				//iphone手机 非微信内置浏览器 下载 或 ie浏览器 下载
				wjm = URLEncoder.encode(wjm, StandardCharsets.UTF_8);
				response.setHeader("Content-Disposition","attachment;filename*=UTF-8''" + wjm);
			}
		}else {
			wjm = URLEncoder.encode(wjm, StandardCharsets.UTF_8);
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", wjm));// 文件名外的双引号处理firefox的空格截断问题
		}
		byte[] buffer = new byte[1024];
		BufferedInputStream bis = null;
		InputStream iStream;
		OutputStream os = null; //输出流
		try {
			iStream = new FileInputStream(filePath);
			os = response.getOutputStream();
			bis = new BufferedInputStream(iStream);
			int i = bis.read(buffer);
			while(i != -1){
				os.write(buffer);
				os.flush();
				i = bis.read(buffer);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("文件下载 写文件异常：" + e);
		}
		try {
			if(bis!=null)
				bis.close();
			if(os!=null) {
				os.flush();
				os.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("文件下载 关闭文件流异常：" + e);
			return null;
		}
		return null;
	}
}
