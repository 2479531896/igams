package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ExceptionSSEUtil;
import com.matridx.springboot.common.speech.service.svcinterface.IAudioService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.RandomUtil;
import com.matridx.springboot.util.pdf.PdfUtil;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 公用功能对外接口
 * @author linwu
 *
 */
@Controller
@RequestMapping("/ws")
public class CommonWSController  extends BaseController {
	
	@Autowired
	IAudioService audioService;
	@Autowired
	ICommonService commonService;
	@Autowired
	IShgcService shgcService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	IShxxService shxxService;
	@Autowired
	IShlcService shlcService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IGzglService gzglService;
	@Autowired
	IShtxService shtxService;
	@Autowired
	ILscxszService lscxszService;
	@Autowired
	DingTalkUtil talkUtil;

	@Autowired
	ISjycService sjycService;
	@Autowired
	ISjycfkService sjycfkService;
	@Autowired
	ISjyctzService sjyctzService;
	@Autowired(required=false)
	private AmqpTemplate amqpTempl;
	@Autowired
	ISjyczdService sjyczdService;
	@Autowired
	private ExceptionSSEUtil exceptionSSEUtil;
	@Autowired
	IDdxxglService ddxxglService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;

	@Value("${matridx.cookie.cookiekey:}")
	private String cookiekey;
	
	private final Logger log = LoggerFactory.getLogger(CommonWSController.class);
	
	@Override
	public String getPrefix(){
		return urlPrefix;
	}

	/**
	 * 上传临时文件
	 */
	@RequestMapping(value="/saveImportFile")
	@ResponseBody
	public Map<String, Object> saveImportFile(FjcfbDto fjcfbDto,HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		try{
			List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(((MultipartHttpServletRequest) request).getFile("file").getName());
			MultipartFile[] imp_file = new MultipartFile[files.size()];
			files.toArray(imp_file);
			if(imp_file.length>0){
				fjcfbService.save2TempFile(imp_file, fjcfbDto,null);
				result.put("status", "success");
				result.put("fjcfbDto", fjcfbDto);
				result.put("filePath", request.getParameter("filePath"));
			}else{
				result.put("status", "fail");
				result.put("msg", "未获取文件");
			}
		} catch (Exception e) {
			result.put("status", "fail");
			result.put("msg", e.getMessage());
		}
		return result;
	}

	/**
	 * 通用手机删除附件
	 */
	@RequestMapping(value="/phone/delPhoneImportFile")
	@ResponseBody
	public Map<String, Object> delPhoneImportFile(FjcfbDto fjcfbDto){
		Map<String, Object> result = new HashMap<>();
		Map<Object,Object> redisMap=redisUtil.hmget("COM_FILE_UPLOAD:"+fjcfbDto.getYwid()+"_"+fjcfbDto.getYwlx());
		List<FjcfbDto> fjcfbDtoList=new ArrayList<>();
		Map<String,Object>newMap=new HashMap<>();
		if(redisMap.get("fjcfbDtoList")!=null){
			fjcfbDtoList= JSONObject.parseArray(redisMap.get("fjcfbDtoList").toString(),FjcfbDto.class);
		}
		for(Object key:redisMap.keySet()){
			newMap.put(key.toString(),redisMap.get(key));
		}
		newMap.put("uploadFlg","true");
		if(!fjcfbDtoList.isEmpty()){
			int index=0;
			for(int i=0;i<fjcfbDtoList.size();i++){
				if(Objects.equals(fjcfbDtoList.get(i).getFjid(), fjcfbDto.getFjid())){
					index=i;
					break;
				}
			}
			fjcfbDtoList.remove(index);
		}
		newMap.put("fjcfbDtoList",JSONObject.toJSONString(fjcfbDtoList));
		redisUtil.hmset("COM_FILE_UPLOAD:"+fjcfbDto.getYwid()+"_"+fjcfbDto.getYwlx(), newMap,1800);
		return result;
	}

	/**
	 * 通用手机上传附件
	 */
	@RequestMapping(value="/phone/saveImportFile")
	@ResponseBody
	public Map<String, Object> savePhoneImportFile(FjcfbDto fjcfbDto,HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		try{
			List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(((MultipartHttpServletRequest) request).getFile("file").getName());
			MultipartFile[] imp_file = new MultipartFile[files.size()];
			files.toArray(imp_file);
			if(imp_file.length>0){
				fjcfbService.save2TempFile(imp_file, fjcfbDto,null);
				result.put("status", "success");
				result.put("fjcfbDto", fjcfbDto);
				Map<Object,Object> redisMap=redisUtil.hmget("COM_FILE_UPLOAD:"+fjcfbDto.getYwid()+"_"+fjcfbDto.getYwlx());
				List<FjcfbDto> fjcfbDtoList=new ArrayList<>();
				Map<String,Object>newMap=new HashMap<>();
				if(redisMap.get("fjcfbDtoList")!=null){
					fjcfbDtoList= JSONObject.parseArray(redisMap.get("fjcfbDtoList").toString(),FjcfbDto.class);
				}
				for(Object key:redisMap.keySet()){
					newMap.put(key.toString(),redisMap.get(key));
				}
				newMap.put("uploadFlg","true");
				fjcfbDtoList.add(fjcfbDto);
				newMap.put("fjcfbDtoList",JSONObject.toJSONString(fjcfbDtoList));
				redisUtil.hmset("COM_FILE_UPLOAD:"+fjcfbDto.getYwid()+"_"+fjcfbDto.getYwlx(), newMap,1800);
				result.put("filePath", request.getParameter("filePath"));
			}else{
				result.put("status", "fail");
				result.put("msg", "未获取文件");
			}
		} catch (Exception e) {
			result.put("status", "fail");
			result.put("msg", e.getMessage());
		}
		return result;
	}
	/**
	 * 由客户端上传语音文件
	 */
	@RequestMapping(value="/audio/trans",consumes= {MediaType.MULTIPART_FORM_DATA_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public Map<String, Object> trans(HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		try {
			List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("wavData"); 
			MultipartFile[] imp_file = new MultipartFile[files.size()];
			files.toArray(imp_file);
			if(imp_file.length>0){
				result = audioService.getStringFromAudio(imp_file[0].getInputStream());
			}else{
				result.put("status", "fail");
				result.put("msg", "未获取文件");
			}
		} catch (Exception e) {
			result.put("status", "fail");
			result.put("msg", e.getMessage());
		}
		return result;
	}
	


	/**
	 *图片在线预览(统计)
	 */
	@RequestMapping(value="/pripreview")
	public  ModelAndView pripreview(FjcfbDto fjcfbDto) {
		ModelAndView mav= new ModelAndView("common/file/imagepreview");
		mav.addObject("fjcfbDto", fjcfbDto);
		return mav;
	}
	
	/**
	 * 获取服务器文件接口
	 */
	@RequestMapping(value="/getImportFile")
	@ResponseBody
    public void sendUploadVoice(HttpServletResponse response, HttpServletRequest request) {
		String wjlj = request.getParameter("wjlj");
		DBEncrypt dbEncrypt = new DBEncrypt();
		String pathString = dbEncrypt.dCode(wjlj);
		File file = new File(pathString);
		//文件不存在不做任何操作
		if(!file.exists())
			return;
		byte[] buffer = new byte[1024];
		BufferedInputStream bis = null;
        InputStream iStream;
        OutputStream os = null; //输出流
		//获取服务器文件
		try {
			iStream = new FileInputStream(pathString);
			os = response.getOutputStream();
            bis = new BufferedInputStream(iStream);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                os.flush();
                i = bis.read(buffer);
            }
			
		}catch(Exception e) {
			log.error(e.getMessage());
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
			log.error(e.getMessage());
        }
    }
	
	/**
	 * 手机端显示审核页面
	 */
	@RequestMapping(value ="/auditProcess/auditPhone")
	public ModelAndView auditPhone(ShgcDto shgcDto,HttpServletRequest req){
		//检测签名，如果不正确，则抛出异常
		if(!checkSign(req)) {
			return commonService.jumpAuditError();
		}
		ModelAndView mav = new ModelAndView("globalweb/systemcheck/audit_view_phone");
		mav.addObject("urlPrefix", urlPrefix);
		
		ShxxDto shxxParam = new ShxxDto();
		shxxParam.setShlb(shgcDto.getShlb());
		shxxParam.setYwid(shgcDto.getYwid());
		List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
		ShlcDto shlcParam = new ShlcDto();
		if (shxxList != null && !shxxList.isEmpty()) {
			shlcParam.setShid(shxxList.get(0).getShid());
			shlcParam.setSqsj(shxxList.get(0).getSqsj());
		}
		// 获取当前审核过程
		ShgcDto t_shgcDto = shgcService.getDtoByYwid(shgcDto.getYwid());
		if(t_shgcDto == null){
			mav.addObject("shxxList", shxxList);
			List<ShlcDto> shlcList = new ArrayList<>();
			if(StringUtil.isNotBlank(shlcParam.getShid()) ){
				shlcList = shlcService.getDtoList(shlcParam);
				if(shlcList != null && !shlcList.isEmpty()){
					for (ShlcDto shlc : shlcList) {
						shlc.setAudited(true);
					}
				}
			}
			shgcDto.setXlcxh("");
			mav.addObject("shlcList", shlcList);
			mav.addObject("shgcDto", shgcDto);
			return mav;
		}
		//审核业务页面
		t_shgcDto.setShxx_shry(shgcDto.getShxx_shry());
		t_shgcDto.setBusiness_url(shgcDto.getBusiness_url());
		t_shgcDto.setYwzd(shgcDto.getYwzd());
		t_shgcDto.setShlbmc(shgcDto.getShlbmc());
		t_shgcDto.setShxx_shjs(shgcDto.getShxx_shjs());// 链接传过来的角色
		// 获取的审核流程列表
		shlcParam.setShid(t_shgcDto.getShid());
		shlcParam.setGcid(t_shgcDto.getGcid());// 处理旧流程判断用
		List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
		// 退回列表
		List<ShlcDto> backShlcList = new ArrayList<>();
		ShlcDto startLc = new ShlcDto();
		startLc.setGwmc("用户");
		startLc.setLcxh("-1");
		backShlcList.add(startLc);
		for (ShlcDto shlc : shlcList) {
			// 当流程序号小于现流程序号则放入退回列表
			if (t_shgcDto.getXlcxh() != null
					&& Integer.parseInt(shlc.getLcxh()) < Integer.parseInt(t_shgcDto.getXlcxh())) {
				shlc.setAudited(true);
				backShlcList.add(shlc);
			} else if (shlc.getLcxh().equals(t_shgcDto.getXlcxh())) {// 相等则是当前流程
				// 当前流程做标记
				shlc.setCurrent(true);// 当前流程
				shlc.setAudited(true); //已审核流程
				// 最后一步标志
				if (t_shgcDto.getXlcxh() != null
						&& shlcList.get(shlcList.size() - 1).getLcxh().equals(t_shgcDto.getXlcxh())) {
					t_shgcDto.setLastStep(true);
				}
				//判断如果流程类别不为空，则代表需要显示特殊页面
				if(StringUtil.isNotBlank(shlc.getLclb())) {
					JcsjDto jcsjDto = jcsjService.getDtoById(shlc.getLclb());
					//审核业务页面
					t_shgcDto.setBusiness_url(jcsjDto.getCskz1());
				}
				break;// 跳出for循环
			}
		}
		//如果当前审核已经通过，则直接显示查看页面，并返回
		if(StringUtil.isNotBlank(shgcDto.getXlcxh()) && !shgcDto.getXlcxh().equals(t_shgcDto.getXlcxh())){
			mav.addObject("shxxList", shxxList);
			mav.addObject("shlcList", shlcList);
			mav.addObject("shgcDto", shgcDto);
			return mav;
		}
		//如果还未审核通过，则显示审核中页面
		mav = new ModelAndView("globalweb/systemcheck/audit_process_phone");
		mav.addObject("urlPrefix", urlPrefix);

		//退回节点最后一个
		ShlcDto backlastlist=backShlcList.get(backShlcList.size()-1);
		mav.addObject("shgcDto", t_shgcDto);
		mav.addObject("backShlcList", backShlcList);
		mav.addObject("shlcList", shlcList);
		mav.addObject("shxxList", shxxList);
		mav.addObject("ShlcDto", backlastlist);
		return mav;
	}
	
	/**
	 * 审核保存
	 */
	@RequestMapping(value ="/auditProcess/auditSave")
	@ResponseBody
	public Map<String, Object> auditSave(ShxxDto shxxDto,HttpServletRequest request) {
		Map<String, Object> data = new HashMap<>();
		Boolean result = false;
		try {
			ShgcDto shgcDto=shgcService.getDtoByYwid(shxxDto.getYwid());
			if(Integer.parseInt(shgcDto.getXlcxh())>Integer.parseInt(shxxDto.getLcxh())) {
				data.put("status", "fail");
				data.put("message", "审核信息不存在，可能已经被审核，请重新确认！");
				data.put("result",false);
				return data;
			}
			// 传入委托相关参数
			DataPermission.addWtParam(shxxDto);
			Map<String,Object> userinfo=commonService.getUserInfo(null, shxxDto.getShxx_shry(), request);
			User user = (User)userinfo.get("user");
			user.setDqjs(shxxDto.getShxx_shjs());
			AuditResult auditResult = shgcService.doAudit(shxxDto, user,request);
			result = auditResult.getReturnResult();
			String msg = auditResult.getReturnMsg(xxglService.getModelById("ICOM99018").getXxnr(), xxglService.getModelById("ICOM99019").getXxnr());
			data.put("status", "success");
			data.put("message", msg);
			if(auditResult.getBackMap()!=null)
				data.put("backInfo", auditResult.getBackMap());
		} catch (BusinessException e) {
			String mess = e.getMsg();
			XxglModel xxglModel = xxglService.getModelById(e.getMsgId());
			String idMsg = xxglModel==null?"":xxglModel.getXxnr();
			data.put("status", "fail");
			data.put("message", idMsg + (StringUtil.isNotBlank(idMsg) && StringUtil.isNotBlank(mess) ? "<br/>" : "")
					+ (StringUtil.isNotBlank(mess) ? mess : ""));

		} catch (Exception e) {
			log.error(e.toString());
			data.put("status", "fail");
			data.put("message", xxglService.getModelById("ICOM99019").getXxnr());
		}
		data.put("result", result);
		return data;
	}
	
	/**
	 * 对传递的安全信息进行检查
	 */
	private boolean checkSign(HttpServletRequest req) {
		String sign =req.getParameter("sign");
		try{
			sign=URLDecoder.decode(sign);
		}catch (Exception e){
			log.error(e.getMessage());
		}
		sign = sign.replace(" ", "+");
		String ywid =req.getParameter("ywid");
		boolean checksign=commonService.checkSign(sign,ywid, req);
		log.error("checksign="+ checksign);
		return checksign;
	}
	
	
	/**
	 * 异步获取基础数据子数据
	 */
	@RequestMapping("/data/ansyGetJcsjList")
	@ResponseBody
	public List<JcsjDto> ansyGetJcsjList(JcsjDto jcsjDto){
		return jcsjService.getJcsjDtoList(jcsjDto);
	}
	
	/**
	 *	送检图片在线预览（内部）
	 */
	@RequestMapping(value="/sjxxpripreview")
	public ModelAndView sjxxpripreview(HttpServletRequest request, FjcfbDto fjcfbDto) {
		String pageFlg = request.getParameter("pageflg");
		
		ModelAndView mav= new ModelAndView("common/view/pripreview");
		if(StringUtil.isNotBlank(pageFlg))
			mav= new ModelAndView("common/view/priurlpreview");
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	@RequestMapping(value="/videoView")
	public ModelAndView videoView(HttpServletRequest request, FjcfbDto fjcfbDto) {
		ModelAndView mav= new ModelAndView("common/view/videoView");
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 *	送检图片在线打印（内部）
	 */
	@RequestMapping(value="/printView")
	public ModelAndView printView(FjcfbDto fjcfbDto) {
		ModelAndView mav= new ModelAndView("common/view/printView");
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 公开的查看文件方法
	 */
	@RequestMapping(value="/file/getFileByPath")
	@ResponseBody
	public void getFileByPath(FjcfbDto fjcfbDto, HttpServletResponse response){
		if (StringUtil.isNotBlank(fjcfbDto.getWjlj()) && !"null".equals(fjcfbDto.getWjlj())){
			DBEncrypt encrypt = new DBEncrypt();
			String filePath = encrypt.dCode(fjcfbDto.getWjlj());
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
				log.error(e.toString());
			}finally {
				try {
					if(bis!=null)
						bis.close();
					if(os!=null) {
						os.flush();
						os.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.error(e.toString());
				}
			}
		}

	}

	/**
	 * @Description: 查看视频
	 * @param fjcfbDto
	 * @param request
	 * @param response
	 * @return void
	 * @Author: 郭祥杰
	 * @Date: 2024/7/11 16:05
	 */
	@RequestMapping(value="/file/getVideoFileInfo")
	@ResponseBody
	public void getVideoFileInfo(FjcfbDto fjcfbDto, HttpServletRequest request, HttpServletResponse response){
		if (StringUtil.isNotBlank(fjcfbDto.getFjid()) && !"null".equals(fjcfbDto.getFjid())){
			String filePath;
			FjcfbModel fjcfbModel = fjcfbService.getModel(fjcfbDto);
			if (fjcfbModel!=null){
				DBEncrypt encrypt = new DBEncrypt();
				String wjm  = fjcfbModel.getWjm();
				filePath = encrypt.dCode(fjcfbModel.getWjlj());
				response.reset();
				String rangeString = request.getHeader("Range");
				try {
					OutputStream outputStream = response.getOutputStream();
					File file = new File(filePath);
					if(file.exists()){
						RandomAccessFile targetFile = new RandomAccessFile(file, "r");
						long fileLength = targetFile.length();
						long range = Long.parseLong(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
						response.setHeader("Content-Type", "video/mov");
						response.setHeader("Content-Length", String.valueOf(fileLength - range));
						response.setHeader("Content-Range", "bytes "+range+"-"+(fileLength-1)+"/"+fileLength);
						response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
						targetFile.seek(range);
						byte[] cache = new byte[1024 * 300];
						int flag;
						while ((flag = targetFile.read(cache))!=-1){
							outputStream.write(cache, 0, flag);
						}
					}else {
						String message = "file:"+filePath+" not exists";
						//解决编码问题
						response.setHeader("Content-Type","application/json");
						outputStream.write(message.getBytes(StandardCharsets.UTF_8));
					}
					outputStream.flush();
					outputStream.close();
				} catch (FileNotFoundException e) {

				} catch (IOException e) {

				}

			}

		}
	}
	/**
	 * 公开的查看文件方法
	 */
	@RequestMapping(value="/file/getPubFileInfo")
	@ResponseBody
	public void getPubFileInfo(FjcfbDto fjcfbDto, HttpServletResponse response){
		if (StringUtil.isNotBlank(fjcfbDto.getFjid()) && !"null".equals(fjcfbDto.getFjid())){
			String filePath;
			if (StringUtil.isNotBlank(fjcfbDto.getTemporary()) && !"null".equals(fjcfbDto.getTemporary())){
				Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+fjcfbDto.getFjid());
				filePath = (String)mFile.get("wjlj");
			}else{
				FjcfbModel fjcfbModel = fjcfbService.getModel(fjcfbDto);
				if (fjcfbModel!=null){
					DBEncrypt encrypt = new DBEncrypt();
					String wjm  = fjcfbModel.getWjm();
					if(wjm.endsWith(".doc") || wjm.endsWith(".docx") || wjm.endsWith(".xls")||  wjm.endsWith(".xlsx")) {
						if(StringUtil.isBlank(fjcfbModel.getZhwjxx()))
							return;
						filePath = encrypt.dCode(fjcfbModel.getZhwjxx());
					}else {
						filePath = encrypt.dCode(fjcfbModel.getWjlj());
					}
				}else{
					Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+fjcfbDto.getFjid());
					if(mFile!=null&&mFile.size()>0){
						filePath = (String)mFile.get("wjlj");
					}else{
						return;
					}
				}
			}


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
				log.error(e.toString());
			}finally {
				try {
					if(bis!=null)
						bis.close();
					if(os!=null) {
						os.flush();
						os.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.error(e.toString());
				}
			}
		}

	}
	
	/**
	 * 内部图片预览
	 */
	@RequestMapping(value="/file/getwechatFileInfo")
	@ResponseBody
	public void getwechatFileInfo(FjcfbDto fjcfbDto,HttpServletResponse response){
		FjcfbModel fjcfbModel = fjcfbService.getModel(fjcfbDto);
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
			log.error(e.getMessage());
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
			log.error(e.getMessage());
        }
	}
	
	/**
	 * 小程序异常录入界面获取用户列表
	 */
	@RequestMapping("/mini/getXtyhList")
	@ResponseBody
	public Map<String,Object> getExceptionQrrList(GzglDto gzglDto){
		if(StringUtils.isNotBlank(gzglDto.getYhid())) {
			gzglDto.setYhid(gzglDto.getYhid());
			gzglDto.setQf(HabitsTypeEnum.USER_HABITS.getCode());
		}
		List<GzglDto> t_List = gzglService.getMiniFzrList(gzglDto);
		Map<String,Object> result = new HashMap<>();
		result.put("list", t_List);
		return result;
	}
	
	/**
	 * 获取机构列表(小程序)
	 */
	@RequestMapping("/mini/DepartmentList")
	@ResponseBody
	public Map<String,Object> DepartmentList(DepartmentDto departmentDto){
		Map<String,Object> map= new HashMap<>();
		List<DepartmentDto> jglist=commonService.getMiniDepartmentList(departmentDto);
		map.put("list", jglist);
		return map;
	}
	
	/**
	 * 异步获取基础数据子数据
	 */
	@RequestMapping("/data/GetMiniJcsjList")
	@ResponseBody
	public Map<String,Object> GetMiniJcsjList(JcsjDto jcsjDto){
		Map<String,Object> map= new HashMap<>();
		map.put("zsjList", jcsjService.getJcsjDtoList(jcsjDto));
		return map;
	}
	
	/**
	 * pdf游览路径
	 */
	@RequestMapping(value="/file/getFileInfo")
	@ResponseBody
	public void getFileInfo(FjcfbDto fjcfbDto,HttpServletResponse response){
		
		FjcfbModel fjcfbModel = fjcfbService.getModel(fjcfbDto);
		if(fjcfbModel == null)
			return;
		
		DBEncrypt encrypt = new DBEncrypt();
		
		String wjm  = fjcfbModel.getWjm();
		if(StringUtil.isBlank(wjm))
			return;
		String filePath;
		if(wjm.endsWith(".doc") || wjm.endsWith(".docx") || wjm.endsWith(".xls")||  wjm.endsWith(".xlsx")) {
			filePath = encrypt.dCode(fjcfbModel.getZhwjxx());
		}else {
			filePath = encrypt.dCode(fjcfbModel.getWjlj());
		}
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
			log.error(e.getMessage());
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
			log.error(e.getMessage());
        }
	}
	
	/**
	 * 点击延期任务消息跳转页面
	 */
	@RequestMapping("/audit/spyqListdd")
	public ModelAndView spyqDdview(HttpServletRequest httpServletRequest) {
		ModelAndView mav = new ModelAndView("systemmain/audit/spyq_ddview");
		String txlb = httpServletRequest.getParameter("txlb");
//		String txlb = "QUALITY_FILE_ADD";
		String yhid = httpServletRequest.getParameter("yhid");
//		String yhid = "0613C0923C5240E1905E9E057E7D1040";
		String dqshjs = httpServletRequest.getParameter("dqshjs");
//		String dqshjs = "FA2E98B26C4F4F07BE5ADE74BB7F4DFA";
		mav.addObject("txlb",txlb);
		mav.addObject("yhid",yhid);
		mav.addObject("dqshjs",dqshjs);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	
	@RequestMapping("/audit/getSpyqListddData")
	@ResponseBody
	public Map<String,Object> getSpyqddList(ShtxDto shtxDto,HttpServletRequest httpServletRequest) {
		Map<String,Object> map;
		//获取用户信息
		String txlb = httpServletRequest.getParameter("txlb");
		String yhid = httpServletRequest.getParameter("yhid");
		String dqshjs = httpServletRequest.getParameter("dqshjs");
		shtxDto.setTxlb(txlb);
		shtxDto.setDqshyh(yhid);
		shtxDto.setDqshjs(dqshjs);
		map = shtxService.getSpyqDdviewList(shtxDto);
//		map.put("total",shtxDto.getTotalNumber());
		return map;		
	}
	
	@RequestMapping(value ="/query/getStaticPageByType")
	public ModelAndView getStaticPageByType(LscxszDto lscxszDto,HttpServletRequest httpServletRequest){
		ModelAndView mav=new ModelAndView("systemmain/statistics/statistics_common");
		try{
			lscxszDto.setYhid(httpServletRequest.getParameter("yhid"));
			
			List<String> cxqfdms = new ArrayList<>();
			cxqfdms.add("STATISTICS");
			lscxszDto.setCxqfdms(cxqfdms);
			lscxszDto.setSortName("px");
			lscxszDto.setSortOrder("asc");
			lscxszDto.setPageSize(100);
			lscxszDto.setPageNumber(1);
			lscxszDto.setPageStart(0);
			List<LscxszDto> lscxszlist=lscxszService.getSimpDtoListByCode(lscxszDto);
			
			mav.addObject("lscxszlist",lscxszlist);
			mav.addObject("urlPrefix", urlPrefix);
			mav.addObject("pageflag", "wspage");
			mav.addObject("yhid",httpServletRequest.getParameter("yhid"));
			mav.addObject("lbqf",httpServletRequest.getParameter("lbqf"));
		}catch(Exception e){
			log.error(e.getLocalizedMessage());
		}
		return mav;
	}
	
	/**
	 * 根据类别区分查询临时查询信息的数据
	 */
	@RequestMapping(value ="/query/getStaticDataByType")
	@ResponseBody
	public Map<String,Object> getStaticDataByType(LscxszDto lscxszDto,HttpServletRequest httpServletRequest){
		Map<String,Object> result = new HashMap<>();
		try{
			String yhid = httpServletRequest.getParameter("yhid");
			lscxszDto.setYhid(yhid);
			
			List<String> cxqfdms = new ArrayList<>();
			cxqfdms.add("STATISTICS");
			lscxszDto.setCxqfdms(cxqfdms);
			lscxszDto.setSortName("px");
			lscxszDto.setSortOrder("asc");
			lscxszDto.setPageSize(100);
			lscxszDto.setPageNumber(1);
			lscxszDto.setPageStart(0);
			
			List<LscxszDto> lscxszlist=lscxszService.getDtoListByCode(lscxszDto);
			
			if(lscxszlist != null && !lscxszlist.isEmpty()) {
				for (LscxszDto t_lscxszDto : lscxszlist) {
					// 处理查询sql
					t_lscxszDto.setCxtjs(lscxszDto.getCxtjs());
					String csdmone = lscxszService.dealStatisticsQuerySql(t_lscxszDto, yhid);
					t_lscxszDto.setCxdm(csdmone);
					String cxdm = lscxszService.dealQuerySql(t_lscxszDto, yhid);
					t_lscxszDto.setCxdm(cxdm);

					List<HashMap<String, Object>> listResult = lscxszService.getQueryResult(t_lscxszDto);
					//为了页面使用，删除大容量字段
					//t_lscxszDto.setCxdm("");
					t_lscxszDto.setBz("");
					t_lscxszDto.setCxmc("");
					// 判断类型处理
					if ("Table".equals(t_lscxszDto.getXsfsdm())) { // 表格
						List<Map<String, Object>> listmap = new ArrayList<>();
						List<String> headList = new ArrayList<>();
						// 遍历list
						for (HashMap<String, Object> stringObjectHashMap : listResult) {
							// 遍历hashmap，取key和value
							Map<String, Object> t_map = new HashMap<>(stringObjectHashMap);
							listmap.add(t_map);
						}

						Statement stmt = CCJSqlParserUtil.parse(t_lscxszDto.getCxdm());
						Select select = (Select) stmt;
						SelectBody selectBody = select.getSelectBody();
						if (selectBody instanceof PlainSelect) {
							PlainSelect plainSelect = (PlainSelect) selectBody;
							List<SelectItem> selectitems = plainSelect.getSelectItems();
							for (SelectItem selectitem : selectitems) {
								String itemString = selectitem.toString();
								if (StringUtil.isNotBlank(itemString)) {
									String[] s_item = itemString.split(" ");
									String t_item = s_item[s_item.length - 1];
									headList.add(t_item.substring(!t_item.contains(".") ? 0 : (t_item.indexOf(".") + 1)));
								}
							}
						}
						Map<String, Object> subresult = new HashMap<>();
						subresult.put("resultList", listmap);
						subresult.put("headList", headList);
						result.put(t_lscxszDto.getCxbm(), subresult);
					} else if ("Pie".equals(t_lscxszDto.getXsfsdm())) { // 饼状图
						Map<String, Object> subresult = new HashMap<>();
						subresult.put("resultList", listResult);
						result.put(t_lscxszDto.getCxbm(), subresult);
					} else if ("Bar".equals(t_lscxszDto.getXsfsdm())) { // 柱状图
						Map<String, Object> subresult = new HashMap<>();
						subresult.put("resultList", listResult);
						result.put(t_lscxszDto.getCxbm(), subresult);
					}
				}
			}
			result.put("lscxszlist", lscxszlist);
			result.put("urlPrefix", urlPrefix);
		}catch(Exception e){
			log.error(e.getLocalizedMessage());
		}
		return result;
	}
	
	/**
	 * 预览PDF，预览转换后的PDF
	 */
	@RequestMapping(value="/file/pdfPreview")
	public ModelAndView pdfPreview(FjcfbDto fjcfbDto) {
		try {
            String filepath;
            if("1".equals(fjcfbDto.getPdf_flg())) {
                filepath = urlPrefix + "/ws/file/getFileInfoFlg?fjid=" + fjcfbDto.getFjid();
            }else {
                filepath = urlPrefix + "/ws/file/getFileInfo?fjid=" + fjcfbDto.getFjid();
            }
            ModelAndView mv = new ModelAndView("common/file/viewer");
            mv.addObject("file",filepath);
            return mv;
        }catch(Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@RequestMapping("/eureka/pagedataDelRedis")
	@ResponseBody
	public  Map<String, Object> delRedisEureka(String ipAddr){
		log.error("启动完成--自动删除Redis里暂停的记录：" + ipAddr);
		Map<String, Object> map= new HashMap<>();
		List<Object> list=redisUtil.lGet("eurekaList");
		List<Object> removeList=new ArrayList<>();
		Object remObj;
		if(list!=null){
			for(Object j:list){
				JSONObject jsonObject=JSONObject.parseObject(j.toString());
				if(jsonObject.get("ipAddr").equals(ipAddr)){
					remObj=j;
					removeList.add(remObj);
				}
			}
		}
		if(!removeList.isEmpty()){
			for(Object j:removeList){
				redisUtil.lRemove("eurekaList",1,j);
			}

		}
		return map;
	}

	@RequestMapping("/verify/pagedataGetOnlyKey")
	@ResponseBody
	public  Map<String, Object> getOnlyKey(){
		Map<String, Object> map= new HashMap<>();
		map.put("onlykey",cookiekey);
		return map;
	}


	/**
	 * 线路发送钉钉消息接口
	 * @param type  网络类型
	 * @param mark	发送标记 0线路正常，1线路故障
	 */
	@RequestMapping("/verify/pagedataContentMsg")
	@ResponseBody
	public void sendContentMsg(String type,String mark){
		List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
		OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
		//            btnJsonList.setTitle(xxglService.getMsg("ICOMM_SJ00004"));
		//            btnJsonList.setActionUrl(viewurl);
		//            btnJsonLists.add(btnJsonList);
		//            btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
		btnJsonList.setTitle("");
		btnJsonList.setActionUrl("download");
		btnJsonLists.add(btnJsonList);
		List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.CONTENT_BATCH.getCode());
		String msg="网络出现问题";
		if("0".equals(mark)){
			msg="网络恢复正常";
		}
		String format = LocalDateTime.now().format(
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
		);

		if (ddxxglDtolist != null && !ddxxglDtolist.isEmpty()) {
			for (DdxxglDto ddxxglDto : ddxxglDtolist) {
				if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
					talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), msg, StringUtil.replaceMsg(xxglService.getMsg("ICOMM_WLQK00001"), type + msg, format)
					);
				}
			}
		}
	}

	/**
	 * 小程序跳转异常新增界面
	 */
	@RequestMapping("/dingtalk/getAddExceptionPage")
	@ResponseBody
	public Map<String,Object> getAddExceptionPage(SjycDto sjycDto){
		Map<String,Object> t_map= new HashMap<>();
		sjycDto.setYwlx(BusTypeEnum.IMP_EXCEPTION.getCode());
		// 通知类型
		List<Map<String, String>> tzlxList = new ArrayList<>();
		for (DingNotificationTypeEnum tzlxType : DingNotificationTypeEnum.values())
		{
			Map<String, String> map = new HashMap<>();
			map.put("code", tzlxType.getCode());
			map.put("value", tzlxType.getValue());
			tzlxList.add(map);
		}
		//Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.EXCEPTION_TYPE,BasicDataTypeEnum.SUBCLASSEXCEPTION_TYPE});
		t_map.put("yclblist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_TYPE.getCode()));
		t_map.put("yczlblist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SUBCLASSEXCEPTION_TYPE.getCode()));
		t_map.put("ycqflist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_DISTINGUISH.getCode()));
		t_map.put("yczqflist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_SUBDISTINGUISH.getCode()));
		t_map.put("jcdwlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		t_map.put("yctjlxList", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_STATISTICS.getCode()));
		t_map.put("tzlxList", tzlxList);
		t_map.put("sjycDto", sjycDto);
		return t_map;
	}

	/**
	 * 查看异常任务
	 */
	@RequestMapping("/dingtalk/viewException")
	@ResponseBody
	public Map<String,Object> getExceptionView(SjycDto sjycDto) {
		SjycDto sjyc=sjycService.getDto(sjycDto);
		Map<String,Object> map= new HashMap<>();
		//根据异常ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(sjycDto.getYcid());
		if(fjcfbDtos != null){
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
				fjcfbDto.setWjmhz(wjmhz);
			}
		}
		SjyctzDto sjyctzDto=new SjyctzDto();
		sjyctzDto.setYcid(sjycDto.getYcid());
		List<SjyctzDto> sjyctzDtos = sjyctzService.getDtoList(sjyctzDto);
		StringBuilder tzrys= new StringBuilder();
		if(sjyctzDtos!=null&& !sjyctzDtos.isEmpty()){
			for(SjyctzDto dto:sjyctzDtos){
				tzrys.append(",").append(dto.getMc());
			}
			if(StringUtil.isNotBlank(tzrys.toString())){
				tzrys = new StringBuilder(tzrys.substring(1));
			}
		}
		sjyc.setTzry(tzrys.toString());
		map.put("fjcfbDtos",fjcfbDtos);
		map.put("sjycDto", sjyc);
		map.put("urlPrefix", urlPrefix);
		return map;
	}

	/**
	 * 查看异常任务
	 */
	@RequestMapping("/dingtalk/addException")
	@ResponseBody
	public Map<String,Object> addException() {
		Map<String,Object> map= new HashMap<>();
		map.put("jcdwlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		map.put("ycqflist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_DISTINGUISH.getCode()));
		map.put("yczqflist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_SUBDISTINGUISH.getCode()));
		return map;
	}

	/**
	 * 小程序保存异常信息
	 */
	@RequestMapping("/dingtalk/addSaveException")
	@ResponseBody
	public Map<String,Object> addSaveException(SjycDto sjycDto){
		Map<String,Object> map= new HashMap<>();
		try {
			sjycDto.setTwrlx(TwrTypeEnum.WEB.getCode());
			if (StringUtil.isBlank(sjycDto.getFjbcbj())){
				sjycDto.setFjbcbj("dingding");
			}
			boolean isSuccess=sjycService.addSaveException(sjycDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (Exception e) {
			// TODO: handle exception
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 小程序获取送检异常全部清单
	 */
	@RequestMapping("/dingtalk/getExceptionList")
	@ResponseBody
	public Map<String,Object> getExceptionList(SjycDto sjycDto){
		Map<String,Object> map= new HashMap<>();
		List<SjycDto> sjyclist=sjycService.getMiniDtoList(sjycDto);
		List<String> ids=new ArrayList<>();
		if(sjyclist!=null && !sjyclist.isEmpty()) {
			for (SjycDto dto : sjyclist) {
				ids.add(dto.getYcid());
			}
			SjycfkDto sjycfkDto=new SjycfkDto();
			sjycfkDto.setIds(ids);
			List<SjycfkDto> pls=sjycfkService.getExceptionPls(sjycfkDto);
			if(pls!=null && !pls.isEmpty()) {
				for (SjycfkDto pl : pls) {
					for (SjycDto dto : sjyclist) {
						if (pl.getYcid().equals(dto.getYcid())) {
							dto.setPls(pl.getPls());
						}
					}
				}
			}

		}
		map.put("jcdwlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		map.put("ycqflist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_DISTINGUISH.getCode()));
		map.put("yczqflist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_SUBDISTINGUISH.getCode()));
		map.put("sjycList", sjyclist);
		return map;
	}
	/**
	 * 附件下载（用于普通的文件下载使用）
	 */
	@RequestMapping(value="/common/downloadDocFile")
	public String downloadDocFile(FjcfbDto fjcfbDto,HttpServletResponse response,HttpServletRequest request){
		FjcfbDto t_fjcfbDto = fjcfbService.getDto(fjcfbDto);
		if(t_fjcfbDto==null)
		{
			return "对不起，系统未找到相应文件 downloadDocFile";
		}
		String wjlj = t_fjcfbDto.getWjlj();
		String wjm = t_fjcfbDto.getWjm();
		DBEncrypt crypt = new DBEncrypt();
		String filePath = crypt.dCode(wjlj);
		String agent = request.getHeader("user-agent");
		if(wjm != null){
			log.error("----------agent:"+agent+"----------");
			if (agent.contains("iPhone") || agent.contains("Trident")){
				wjm = URLEncoder.encode(wjm, StandardCharsets.UTF_8);
				response.setHeader("Content-Disposition","attachment;filename*=UTF-8''" + wjm);
			}else {
				byte[] bytes = agent.contains("MSIE") ? wjm.getBytes() : wjm.getBytes(StandardCharsets.UTF_8);
				wjm = new String(bytes, StandardCharsets.ISO_8859_1);
				response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", wjm));// 文件名外的双引号处理firefox的空格截断问题
			}
		}
		//指明为下载
		response.setHeader("content-type", "application/octet-stream");

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
			log.error("downloadDocFile1 "+e.getMessage());
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
			log.error("downloadDocFile2 "+e.getMessage());
		}
		return null;
	}
	/**
	 * 附件下载（用于普通的文件下载使用）
	 */
	@RequestMapping(value="/common/downloadDocFilePro")
	public String downloadDocFilePro(FjcfbDto fjcfbDto,HttpServletResponse response,HttpServletRequest request){
		FjcfbDto report_fjcfbDto = fjcfbService.getDto(fjcfbDto);
		if(report_fjcfbDto==null){
			return "对不起，系统未找到相应文件 downloadDocFilePro";
		}
		//报告合并下载处理（若链接传递isNeedMerge=false，则不合并）
		boolean isNeedMerge = true;
		if (StringUtil.isNotBlank(request.getParameter("isNeedMerge"))){
			isNeedMerge = !"false".equals(request.getParameter("isNeedMerge"));
		}
		DBEncrypt p = new DBEncrypt();
		String pdfWjm = report_fjcfbDto.getWjm();;
		String pdfWjlj = p.dCode(report_fjcfbDto.getWjlj());
		boolean isMergeSuccess = true;
		String ywlx = report_fjcfbDto.getYwlx();
		if (isNeedMerge && StringUtil.isNotBlank(ywlx) && StringUtil.isNotBlank(fjcfbDto.getYwid())
				&& (ywlx.indexOf("IMP_REPORT_ONCO_QINDEX_TEMEPLATE") >-1 || ywlx.indexOf("IMP_REPORT_SEQ_INDEX_TEMEPLATE") >-1)
				&& ("_D".equals(ywlx.substring(ywlx.length()-2)) || "_C".equals(ywlx.substring(ywlx.length()-2)) || "_O".equals(ywlx.substring(ywlx.length()-2)))){
			//3.0/2.0的D/C/O才需要合并
			List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(fjcfbDto.getYwid());
			List<FjcfbDto> fjcfbDtos = new ArrayList<>();
			fjcfbDtos.add(report_fjcfbDto);
			if (t_fjcfbDtos.size()>0){
				for (FjcfbDto t_fjcfbDto : t_fjcfbDtos) {
					if ((t_fjcfbDto.getYwlx().indexOf("IMP_REPORT_ONCO_QINDEX_TEMEPLATE")>-1 || t_fjcfbDto.getYwlx().indexOf("IMP_REPORT_SEQ_INDEX_TEMEPLATE")>-1 ) && t_fjcfbDto.getYwlx().indexOf("WORD")==-1 ){
						//筛选3.0/2.0的D/C/O
						if ("_D".equals(ywlx.substring(ywlx.length()-2)) && ("_O".equals(t_fjcfbDto.getYwlx().substring(t_fjcfbDto.getYwlx().length()-2)))){
							//若所出报告为D，则筛选O报告合并
							fjcfbDtos.add(t_fjcfbDto);
						}else if ("_C".equals(ywlx.substring(ywlx.length()-2)) && ("_O".equals(t_fjcfbDto.getYwlx().substring(t_fjcfbDto.getYwlx().length()-2)))){
							//若所出报告为C，则筛选O报告合并
							fjcfbDtos.add(t_fjcfbDto);
						}else if ("_O".equals(ywlx.substring(ywlx.length()-2)) && ("_D".equals(t_fjcfbDto.getYwlx().substring(t_fjcfbDto.getYwlx().length()-2)) || "_C".equals(t_fjcfbDto.getYwlx().substring(t_fjcfbDto.getYwlx().length()-2)))){
							//若所出报告为O，则筛选最新的D/C报告合并
							fjcfbDtos.add(t_fjcfbDto);
							break;
						}
					}
				}
				if (fjcfbDtos.size()>1){
					try {
						Object o = redisUtil.get("IMP_REPORT_MREGE:" + fjcfbDto.getYwid());
						if (o != null){
								Map<String,Object> json = (Map<String,Object>) JSON.parse(o.toString());
								List<String> parseids = (List<String>) JSON.parse(json.get("fjids").toString());
								if (parseids.size() == fjcfbDtos.size()){
									int compareNum = 0;
									for (String parseid : parseids) {
										for (FjcfbDto dto : fjcfbDtos) {
											if (parseid.equals(dto.getFjid())){
												compareNum++;
											}
										}
									}
									if (compareNum==parseids.size()){
										pdfWjm = json.get("pdfWjm").toString();
										pdfWjlj = json.get("pdfWjlj").toString();
										isNeedMerge = false;
									}
								}
						}
						if (isNeedMerge){
							pdfWjm = "MREGE_"+System.currentTimeMillis() + RandomUtil.getRandomString() + ".pdf";
							String mergeFilePath = mkDirs("IMP_REPORT_MREGE","/matridx/fileupload/temp/");
							pdfWjlj = mergeFilePath +"/"+ pdfWjm;
							PdfUtil pdfUtil = new PdfUtil();
							DBEncrypt dbEncrypt = new DBEncrypt();
							List<String> fileStrings = new ArrayList<>();
							List<String> fjids = new ArrayList<>();
							for (FjcfbDto fj : fjcfbDtos) {
								String fileString = dbEncrypt.dCode(fj.getWjlj());
								fileStrings.add(fileString);
								fjids.add(fj.getFjid());
							}
							isMergeSuccess = pdfUtil.mergePdfFiles(fileStrings, pdfWjlj);
							Map<String,Object> redisJson = new HashMap<>();
							redisJson.put("pdfWjm", pdfWjm);
							redisJson.put("pdfWjlj", pdfWjlj);
							redisJson.put("fjids", fjids);
							redisUtil.set("IMP_REPORT_MREGE:"+fjcfbDto.getYwid(),JSON.toJSONString(redisJson),24*60*60);
						}
					} catch (Exception e) {
						log.error("downloadDocFilePro "+e.getMessage());
					}
				}
				else{
					pdfWjm = fjcfbDtos.get(0).getWjm();
					pdfWjlj = p.dCode(fjcfbDtos.get(0).getWjlj());;
				}
			}
		}
		if (isMergeSuccess){
			String agent = request.getHeader("user-agent");
			if(pdfWjm != null){
				log.error("----------agent:"+agent+"----------");
				if (agent.contains("iPhone") || agent.contains("Trident")){
					pdfWjm = URLEncoder.encode(pdfWjm, StandardCharsets.UTF_8);
					response.setHeader("Content-Disposition","attachment;filename*=UTF-8''" + pdfWjm);
				}else {
					byte[] bytes = agent.contains("MSIE") ? pdfWjm.getBytes() : pdfWjm.getBytes(StandardCharsets.UTF_8);
					pdfWjm = new String(bytes, StandardCharsets.ISO_8859_1);
					response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", pdfWjm));// 文件名外的双引号处理firefox的空格截断问题
				}
			}
			//指明为下载
			response.setHeader("content-type", "application/octet-stream");

			byte[] buffer = new byte[1024];
			BufferedInputStream bis = null;
			InputStream iStream;
			OutputStream os = null; //输出流
			try {
				iStream = new FileInputStream(pdfWjlj);
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
				log.error("downloadDocFilePro1 "+e.getMessage() + " " + pdfWjlj);
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
				log.error("downloadDocFilePro2 "+e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 根据路径创建文件
	 * @param ywlx
	 * @param realFilePath
	 * @return
	 */
	private String mkDirs(String ywlx,String realFilePath)
	{
		String storePath;
		if(ywlx!=null) {
			//根据日期创建文件夹
			storePath =realFilePath + ywlx +"/"+ "UP"+
					DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
					DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
					DateUtils.getCustomFomratCurrentDate("yyyyMMdd");

		}else {
			storePath=realFilePath;
		}

		File file = new File(storePath);
		if (file.isDirectory()) {
			return storePath;
		}
		if(file.mkdirs())
		{
			return storePath;
		}
		return null;
	}

	/**
	 * 小程序获取个人异常清单
	 */
	@RequestMapping("/dingtalk/getPersonalExceptionList")
	@ResponseBody
	public Map<String,Object> getMiniPersonalList(SjycDto sjycDto){
		Map<String,Object> map= new HashMap<>();
		SjyctzDto sjyctzDto=new SjyctzDto();
		sjyctzDto.setYcid(sjycDto.getYcid());
		List<SjycDto> yhjsList = sjycService.getYhjsList(sjycDto);
		if (yhjsList!=null && !yhjsList.isEmpty()){
			List<String> yhjss = new ArrayList<>();
			for (SjycDto dto : yhjsList) {
				yhjss.add(dto.getJsid());
			}
			sjycDto.setYhjss(yhjss);
		}
		List<SjyctzDto> yctzlist=sjyctzService.getDtoList(sjyctzDto);
		if(yctzlist!=null && !yctzlist.isEmpty()) {
			List<String> yctzrylist=new ArrayList<>();
			for (SjyctzDto dto : yctzlist) {
				yctzrylist.add(dto.getRyid());
			}
			sjycDto.setTzrys(yctzrylist);
		}
		List<SjycDto> sjyclist=sjycService.getMiniPersonalList(sjycDto);
		List<String> ids=new ArrayList<>();
		if(sjyclist!=null && !sjyclist.isEmpty()) {
			for (SjycDto dto : sjyclist) {
				ids.add(dto.getYcid());
			}
			SjycfkDto sjycfkDto=new SjycfkDto();
			sjycfkDto.setIds(ids);
			List<SjycfkDto> pls=sjycfkService.getExceptionPls(sjycfkDto);
			if(pls!=null && !pls.isEmpty()) {
				for (SjycfkDto pl : pls) {
					for (SjycDto dto : sjyclist) {
						if (pl.getYcid().equals(dto.getYcid())) {
							dto.setPls(pl.getPls());
						}
					}
				}
			}

		}
		map.put("jcdwlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		map.put("ycqflist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_DISTINGUISH.getCode()));
		map.put("yczqflist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_SUBDISTINGUISH.getCode()));
		map.put("sjycList", sjyclist);
		return map;
	}



	/**
	 * 小程序异常评论界面
	 */
	@RequestMapping("/exception/discussException")
	@ResponseBody
	public Map<String,Object> getreplyExceptionPage(SjycDto sjycDto) {
		SjycDto sjyc;
		if (StringUtil.isBlank(sjycDto.getYhid())){
			User user = new User();
			user.setYhid(sjycDto.getYhid());
			List<User> usersByDdid = commonService.getUserByDdid(user);
			sjycDto.setYhid(usersByDdid.get(0).getYhid());
		}
		sjyc=sjycService.getDto(sjycDto);
		sjyc.setYhid(sjycDto.getYhid());
		sjyc.setYwlx(BusTypeEnum.IMP_EXCEPTION.getCode());
		SjycfkDto sjycfkDto=new SjycfkDto();
		List<String> ids=new ArrayList<>();
		ids.add(sjyc.getYcid());
		sjycfkDto.setIds(ids);
		sjycfkDto.setYcid(sjyc.getYcid());
		List<SjycfkDto> pls=sjycfkService.getExceptionPls(sjycfkDto);
		sjyc.setPls(String.valueOf(pls.size()));

		//根据异常ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(sjycDto.getYcid());
		if(fjcfbDtos != null){
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
				fjcfbDto.setWjmhz(wjmhz);
			}
		}
		SjyctzDto sjyctzDto=new SjyctzDto();
		sjyctzDto.setYcid(sjycDto.getYcid());
		List<SjyctzDto> dtoList = sjyctzService.getDtoList(sjyctzDto);
		List<String> jsids=new ArrayList<>();
		List<SjyctzDto> ryList = new ArrayList<>();
		if(dtoList!=null&& !dtoList.isEmpty()){
			if("ROLE_TYPE".equals(dtoList.get(0).getLx())){
				for(SjyctzDto dto:dtoList){
					jsids.add(dto.getId());
				}
				SjyctzDto sjyctzDto_t=new SjyctzDto();
				sjyctzDto_t.setIds(jsids);
				ryList = sjyctzService.getYhjsList(sjyctzDto_t);
			}else{
				ryList=dtoList;
			}
		}

		Map<String,Object> map= new HashMap<>();
		map.put("exceptioncopylist",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_COPY_TYPE.getCode()) );//异常复制类型
		map.put("ryList",ryList);
		map.put("fjcfbDtos",fjcfbDtos);
		map.put("sjycDto", sjyc);
		map.put("fkywlx", BusTypeEnum.IMP_EXCEPTION_FEEDBACK.getCode());
		SjycDto _sjycDto= sjycService.getDto(sjycDto);
		if("1".equals(_sjycDto.getSfjs())){
			exceptionSSEUtil.viewFinishException(sjycDto.getYhid(),_sjycDto.getYcid());
		}else{
			exceptionSSEUtil.viewExceptionMessage(sjycDto.getYhid(),_sjycDto.getYcid());
		}
		return map;
	}

	/**
	 * 小程序异常评论界面
	 */
	@RequestMapping("/exception/getMoreDiscuss")
	@ResponseBody
	public Map<String,Object> getMoreDiscuss(SjycfkDto sjycfkDto) {
		Map<String,Object> map= new HashMap<>();
		List<SjycfkDto> SjycfkDtos=sjycfkService.getMiniDtoByYcid(sjycfkDto);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
		String hfsjc;
		try {
			for (SjycfkDto dto : SjycfkDtos) {
				Date date = format.parse(dto.getHfsj());
				long l = new Date().getTime() - date.getTime();
				long t = 60 * 5 - l;
				if (t >= 0) {
					dto.setSfyxch("1");
				} else {
					dto.setSfyxch("0");
				}
				long hour = l / (60 * 60 * 1000);
				if (hour > 0) {
					hfsjc = hour + "小时前";
					if (hour > 24) {
						hfsjc = dto.getHfsj();
					}
				} else {
					long min = l / (60 * 1000);
					if (min > 0) {
						hfsjc = min + "分钟前";
					} else {
						hfsjc = "刚刚";
					}
				}
				dto.setHfsjc(hfsjc);
				if ("WECHAT_FEEDBACK".equals(dto.getFkqfdm())) {
					dto.setFkrymc(dto.getLrrymc());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		map.put("sjycfklist", SjycfkDtos);
		return map;
	}
	/**
	 * 小程序查异常反馈界面看更多回复内容
	 */
	@RequestMapping("/exception/viewMoreDiscuss")
	@ResponseBody
	public Map<String,Object> viewMoreDiscuss(SjycfkDto sjycfkDto){
		Map<String,Object> map= new HashMap<>();
		List<SjycfkDto> list=sjycfkService.getDtosByGid(sjycfkDto);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
		try {
			if(list!=null && !list.isEmpty()) {
				for (SjycfkDto dto : list) {
					Date date = format.parse(dto.getHfsj());
					long l = new Date().getTime() - date.getTime();
					long t = 60 * 5 * 1000 - l;
					if (t >= 0) {
						dto.setSfyxch("1");
					} else {
						dto.setSfyxch("0");
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		map.put("t_sjycfklist", list);
		return map;
	}

	/**
	 * 小程序保存异常反馈信息
	 */
	@RequestMapping("/exception/addSaveExceptionFk")
	@ResponseBody
	public Map<String,Object> addSaveExceptionFeedback(SjycfkDto sjycfkDto){
		log.error("开始保存反馈信息");
		sjycfkDto.setLrrymc(sjycfkDto.getFkrymc());
		SjycDto sjycDto=new SjycDto();
		sjycDto.setYcid(sjycfkDto.getYcid());
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sjycDto.setZhhfsj(dateFm.format(new Date()));
		sjycDto.setZhhfnr(sjycfkDto.getFkrymc()+":"+sjycfkDto.getFkxx());
		sjycService.updateDto(sjycDto);
		Map<String,Object> map= new HashMap<>();
		String tzrys=sjycfkDto.getTzrys();
		if(StringUtil.isBlank(sjycfkDto.getFjbcbj())){
			sjycfkDto.setFjbcbj("dingding");
		}
		boolean isSuccess=sjycfkService.insertDto(sjycfkDto);
		List<JcsjDto> fkqfList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.FEEDBACK_DISTINGUISH.getCode());
		if (fkqfList!=null && !fkqfList.isEmpty()) {
			for (JcsjDto jc_fkqf : fkqfList) {
				if ("NORMAL_FEEDBACK".equals(jc_fkqf.getCsdm())){
					sjycfkDto.setFkqf(jc_fkqf.getCsid());
					break;
				}
			}
		}
		sjycfkDto.setSfyxch("1");
		List<SjycfkDto> list=sjycfkService.getDtosByGid(sjycfkDto);
		List<SjycfkDto> SjycfkDtos=sjycfkService.getMiniDtoByYcid(sjycfkDto);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
		String hfsjc;
		try {
			if(list!=null && !list.isEmpty()) {
				for (SjycfkDto dto : list) {
					Date date = format.parse(dto.getHfsj());
					long l = new Date().getTime() - date.getTime();
					long t = 60 * 5 * 1000 - l;
					if (t >= 0) {
						dto.setSfyxch("1");
					} else {
						dto.setSfyxch("0");
					}
				}
			}
			if(SjycfkDtos!=null && !SjycfkDtos.isEmpty()) {
				for (SjycfkDto dto : SjycfkDtos) {
					Date date = format.parse(dto.getHfsj());
					long l = new Date().getTime() - date.getTime();
					long t = 60 * 5 * 1000 - l;
					if (t >= 0) {
						dto.setSfyxch("1");
					} else {
						dto.setSfyxch("0");
					}
					long hour = l / (60 * 60 * 1000);
					if (hour > 0) {
						hfsjc = hour + "小时前";
						if (hour > 24) {
//				    		long day=l/(24*60*60*1000);
//				    		long hour_t=(l/(60*60*1000)-24*day);
							hfsjc = dto.getHfsj();
						}
					} else {
						long min = l / (60 * 1000);
						if (min > 0) {
							hfsjc = min + "分钟前";
						} else {
							hfsjc = "刚刚";
						}
					}
					dto.setHfsjc(hfsjc);
					if ("WECHAT_FEEDBACK".equals(dto.getFkqfdm())) {
						dto.setFkrymc(dto.getLrrymc());
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 获取送检附件IDs
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjycfkDto.getFkid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_EXCEPTION_FEEDBACK.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);


		map.put("fjcfbDtos", fjcfbDtos);
		map.put("t_sjycfklist", list);
		map.put("sjycfklist",SjycfkDtos);
		map.put("status", isSuccess);
		map.put("sjycfkDto", sjycfkDto);
		SjycDto dtoById = sjycService.getDtoById(sjycfkDto.getYcid());

		List<SjycDto> sjycDtos=new ArrayList<>();
		List<String> yhids=new ArrayList<>();
		List<String> jsids=new ArrayList<>();
		List<String> no_yhids=new ArrayList<>();
		SjyctzDto sjyctzDto=new SjyctzDto();
		sjyctzDto.setYcid(sjycfkDto.getYcid());
		List<SjyctzDto> dtoList = sjyctzService.getDtoList(sjyctzDto);
		if(StringUtil.isNotBlank(tzrys)){
			User user_t=new User();
			user_t.setIds(tzrys);
			List<User> users=commonService.getUsersByYhms(user_t);
			if(users!=null&& !users.isEmpty()){
				List<SjyctzDto> sjyctzDtos = new ArrayList<>();
				for(User t_user:users){
					no_yhids.add(t_user.getYhid());
					boolean findSame=false;
					for(SjyctzDto sjyctzDto_t:dtoList){
						if(t_user.getYhid().equals(sjyctzDto_t.getRyid())){
							findSame=true;
							break;
						}
					}
					if(!findSame){
						SjyctzDto sjyctzDto_t = new SjyctzDto();
						sjyctzDto_t.setRyid(t_user.getYhid());
						sjyctzDto_t.setLx(DingNotificationTypeEnum.USER_TYPE.getCode());
						sjyctzDto_t.setYctzid(StringUtil.generateUUID());
						sjyctzDto_t.setYcid(sjycfkDto.getYcid());
						sjyctzDtos.add(sjyctzDto_t);
					}
				}
				if(!sjyctzDtos.isEmpty()){
					sjyctzService.insertList(sjyctzDtos);
				}
				sjycfkDto.setUsers(users);
				sjycfkDto.setYcbt(dtoById.getYcbt());
				sjycfkService.sendMessageFromOA(sjycfkDto);
			}
		}
		if("ROLE_TYPE".equals(dtoById.getQrlx())){
			jsids.add(dtoById.getQrr());
		}else{
			yhids.add(dtoById.getQrr());
		}
		for(SjyctzDto dto:dtoList){
			if("ROLE_TYPE".equals(dto.getLx())){
				if(!jsids.contains(dto.getId())){
					jsids.add(dto.getId());
				}
			}else{
				if(!yhids.contains(dto.getId())){
					yhids.add(dto.getId());
				}
			}
		}
		if(!jsids.isEmpty()){
			SjyctzDto sjyctzDto_t=new SjyctzDto();
			sjyctzDto_t.setIds(jsids);
			List<SjyctzDto> yhjsList = sjyctzService.getYhjsList(sjyctzDto_t);
			for(SjyctzDto dto:yhjsList){
				if(!dto.getRyid().equals(sjycfkDto.getLrry())&&!yhids.contains(dto.getRyid())&&!no_yhids.contains(dto.getRyid())){
					SjycDto sjycDto_t=new SjycDto();
					sjycDto_t.setRyid(dto.getRyid());
					sjycDtos.add(sjycDto_t);
				}
			}
		}

		if(!yhids.isEmpty()){
			for(String yhid:yhids){
				if(!yhid.equals(sjycfkDto.getLrry())&&!no_yhids.contains(yhid)){
					SjycDto sjycDto_t=new SjycDto();
					sjycDto_t.setRyid(yhid);
					sjycDtos.add(sjycDto_t);
				}
			}
		}
		exceptionSSEUtil.addExceptionMessage(sjycDtos,sjycDto.getYcid());
		SjycDto ycdto = sjycService.getDtoById(sjycfkDto.getYcid());
		if("WECHAT_EXCEPTION".equals(dtoById.getYcqfdm())){
			Map<String,Object> rabbitMap=new HashMap<>();
			sjycfkDto.setQrr(ycdto.getTwr());
			rabbitMap.put("type","hf");
			rabbitMap.put("yhid",dtoById.getTwr());
			rabbitMap.put("sjycDto",ycdto);
			rabbitMap.put("ycid",sjycfkDto.getYcid());
			rabbitMap.put("sjycfkDto",sjycfkDto);
			amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.SSE_SENDMSG_EXCEPRION_WECHART.getCode(), JSON.toJSONString(rabbitMap));
		}
		sjycfkDto.setYcbt(ycdto.getYcbt());
		sjycfkDto.setLrrymc(sjycfkDto.getFkrymc());
		sjycService.sendFkMessage(sjycDtos,sjycfkDto,false);
		return map;
	}

	/**
	 * 结束异常信息
	 */
	@RequestMapping("/exception/finishException")
	@ResponseBody
	public Map<String,Object> finishException(SjycDto sjycDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=sjycService.finishYc(sjycDto);


		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());

		Map<String,Object> rabbitMap=new HashMap<>();
		rabbitMap.put("type","finish");
		rabbitMap.put("sjycDto",sjycDto);
		SjycDto dtoById = sjycService.getDtoById(sjycDto.getIds().get(0));
		List<String> ycids=sjycDto.getIds();
		ycids.stream().forEach(s -> exceptionSSEUtil.finishException(s));
		amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.SSE_SENDMSG_EXCEPRION_WECHART.getCode(), JSON.toJSONString(rabbitMap));
		List<SjycDto> sjycDtos=new ArrayList<>();
		List<String> yhids=new ArrayList<>();
		List<String> jsids=new ArrayList<>();

		if("ROLE_TYPE".equals(dtoById.getQrlx())){
			jsids.add(dtoById.getQrr());
		}else{
			if(!dtoById.getQrr().equals(sjycDto.getRyid())){
				yhids.add(dtoById.getQrr());
			}
		}
		SjyctzDto sjyctzDto=new SjyctzDto();
		sjyctzDto.setYcid(sjycDto.getIds().get(0));
		List<SjyctzDto> dtoList = sjyctzService.getDtoList(sjyctzDto);
		if(dtoList!=null&& !dtoList.isEmpty()){
			if("ROLE_TYPE".equals(dtoList.get(0).getLx())){
				for(SjyctzDto dto:dtoList){
					if(!jsids.contains(dto.getId())){
						jsids.add(dto.getId());
					}
				}
			}else{
				for(SjyctzDto dto:dtoList){
					if(!dto.getId().equals(sjycDto.getYhid())&&!yhids.contains(dto.getId())){
						yhids.add(dto.getId());
					}
				}
			}
		}

		if(!jsids.isEmpty()){
			SjyctzDto sjyctzDto_t=new SjyctzDto();
			sjyctzDto_t.setIds(jsids);
			List<SjyctzDto> yhjsList = sjyctzService.getYhjsList(sjyctzDto_t);
			for(SjyctzDto dto:yhjsList){
				if(!dto.getRyid().equals(sjycDto.getYhid())&&!yhids.contains(dto.getRyid())){
					SjycDto sjycDto_t=new SjycDto();
					sjycDto_t.setRyid(dto.getRyid());
					sjycDtos.add(sjycDto_t);
				}
			}
		}

		if(!yhids.isEmpty()){
			for(String yhid:yhids){
				SjycDto sjycDto_t=new SjycDto();
				sjycDto_t.setRyid(yhid);
				sjycDtos.add(sjycDto_t);
			}
		}
		SjycfkDto sjycfkDto=new SjycfkDto();
		//保存人员操作习惯
		sjycfkDto.setYcid(sjycDto.getIds().get(0));
		sjycfkDto.setLrrymc(sjycDto.getLrrymc());
		sjycService.sendFkMessage(sjycDtos,sjycfkDto,true);
		return map;
	}

	/**
	 * 删除异常信息
	 */
	@RequestMapping("/exception/delException")
	@ResponseBody
	public Map<String,Object> delException(SjycDto sjycDto) {
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=sjycService.delException(sjycDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 异常置顶/取消置顶
	 */
	@RequestMapping("/exception/pagedataTopException")
	@ResponseBody
	public Map<String,Object> pagedataTopException(SjyczdDto sjyczdDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess;
		//如果是置顶则添加，否则删除
		if("1".equals(sjyczdDto.getSfzd())) {
			isSuccess=sjyczdService.insert(sjyczdDto);
		}else {
			isSuccess=sjyczdService.delete(sjyczdDto);
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 跳转转发页面
	 */
	@RequestMapping("/exception/choseRepeatObject")
	@ResponseBody
	public Map<String,Object> choseRepeatObject(SjycDto sjycDto) {
		// 通知类型
		Map<String,Object> map= new HashMap<>();
		List<Map<String, String>> tzlxList = new ArrayList<>();
		for (DingNotificationTypeEnum tzlxType : DingNotificationTypeEnum.values())
		{
			Map<String, String> t_map = new HashMap<>();
			t_map.put("code", tzlxType.getCode());
			t_map.put("value", tzlxType.getValue());
			tzlxList.add(t_map);
		}
		sjycDto=sjycService.getDto(sjycDto);
		SjyctzDto sjyctzDto=new SjyctzDto();
		sjyctzDto.setYcid(sjycDto.getYcid());
		List<SjyctzDto> list=sjyctzService.getDtoList(sjyctzDto);
		map.put("yctzList", list);
		map.put("sjycDto", sjycDto);
		map.put("tzlxList", tzlxList);
		return map;
	}

	/**
	 * 异常转发
	 */
	@RequestMapping("/exception/repeatObject")
	@ResponseBody
	public Map<String,Object> repeatObject(SjycDto sjycDto){
		Map<String,Object> map= new HashMap<>();
		SjycDto dto = sjycService.getDto(sjycDto);
		if(StringUtil.isNotBlank(dto.getZfsj())){
			if(!dto.getZfsj().equals(sjycDto.getZfsj())){
				map.put("status", "fail");
				map.put("message", "该异常已被别人转发!");
				return map;
			}
		}
		if (StringUtil.isNotBlank(sjycDto.getYhid())){
			sjycDto.setXgry(sjycDto.getYhid());
		}
		boolean isSuccess=sjycService.exceptionRepeat(sjycDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM99026").getXxnr():xxglService.getModelById("ICOM99027").getXxnr());
		return map;
	}

	/**
	 * 异常权限更改
	 */
	@RequestMapping("/exception/updateTzry")
	@ResponseBody
	public Map<String,Object> updateTzry(SjycDto sjycDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=sjycService.updatePower(sjycDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?"保存成功！":"保存失败");
		return map;
	}

	/**
	 * 投诉评价保存
	 */
	@RequestMapping("/dingtalk/pagedataEvaluationSave")
	@ResponseBody
	public Map<String, Object> pagedataEvaluationSave(SjycDto sjycDto) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess = sjycService.evaluation(sjycDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	@RequestMapping("/mini/getOaXtjsList")
	@ResponseBody
	public Map<String,Object> MiniXtjsList(User user){
		Map<String,Object> map= new HashMap<>();
		List<User> jslist=commonService.getOaXtjsList(user);
		map.put("list", jslist);
		return map;
	}
	/**
	 * 图片/视频展示
	 */
	@RequestMapping(value="/file/pictureShowRedis")
	@ResponseBody
	public void pictureShowRedis(FjcfbDto fjcfbDto,HttpServletResponse response,HttpServletRequest request){
		Object fjcfbDtoObj = redisUtil.hget(RedisCommonKeyEnum.REDIS_FILE.getKey() ,fjcfbDto.getFjid());
		if (fjcfbDtoObj!=null){
			response.setContentType("image/png");
			OutputStream os = null; //输出流
			try {
				response.setContentType("image/png;charset=utf-8");
				os = response.getOutputStream();
				// 将base64编码的图片数据解码成二进制数据
				byte[] imageBytes = Base64.getDecoder().decode(String.valueOf(fjcfbDtoObj));
				// 将二进制数据写入到输出流中
				os.write(imageBytes);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
			try {
				if (os!=null){
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}else {
			pictureShow(fjcfbDto,response,request);
		}
	}
	/**
	 * 图片/视频展示
	 */
	@SuppressWarnings("resource")
	@RequestMapping(value="/file/fileShow")
	@ResponseBody
	public void pictureShow(FjcfbDto fjcfbDto,HttpServletResponse response,HttpServletRequest request){
		FjcfbDto t_fjcfbDto = fjcfbService.getDto(fjcfbDto);
		String wjlj = t_fjcfbDto.getWjlj();
		DBEncrypt crypt = new DBEncrypt();
		String filePath = crypt.dCode(wjlj);
		String fileName=crypt.dCode(t_fjcfbDto.getFwjm());
		//File downloadFile = new File(filePath);
		String[] array = fileName.split("[.]");
		String fileType = array[array.length-1].toLowerCase();
		//设置文件ContentType类型
		if("jpg,jepg,gif,png".contains(fileType)){//图片类型
			response.setContentType("image/"+fileType);
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
				log.error(e.getMessage());
			}
			try {
				if (bis!=null){
					bis.close();
				}
				if (os!=null){
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}else if("mp4".contains(fileType)){
			response.reset();
			//获取从那个字节开始读取文件
			String rangeString = request.getHeader("Range");
			try {
				//获取响应的输出流
				OutputStream outputStream = response.getOutputStream();
				File file = new File(filePath);
				if(file.exists()){
					RandomAccessFile targetFile = new RandomAccessFile(file, "r");
					long fileLength = targetFile.length();
					//播放
					if(rangeString != null){
						long range = Long.parseLong(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
						response.setHeader("Accept-Ranges","byte");
						//设置内容类型
						response.setHeader("Content-Type", "video/mp4");
						//设置此次相应返回的数据长度
						response.setHeader("Content-Length", String.valueOf(fileLength - range));
						//设置此次相应返回的数据范围
						response.setHeader("Content-Range", "bytes "+range+"-"+(fileLength-1)+"/"+fileLength);
						//返回码需要为206，而不是200
						response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
						//设定文件读取开始位置（以字节为单位）
						targetFile.seek(range);
					}else {//下载
						//设置响应头，把文件名字设置好
						response.setHeader("Content-Disposition", "attachment; filename="+fileName);
						//设置文件长度
						response.setHeader("Content-Length", String.valueOf(fileLength));
						//解决编码问题
						response.setHeader("Content-Type","application/octet-stream");
					}
					byte[] cache = new byte[1024 * 300];
					int flag;
					while ((flag = targetFile.read(cache))!=-1){
						outputStream.write(cache, 0, flag);
					}
				}else {
					String message = "file:"+fileName+" not exists";
					//解决编码问题
					response.setHeader("Content-Type","application/json");
					outputStream.write(message.getBytes(StandardCharsets.UTF_8));
				}

				outputStream.flush();
				outputStream.close();

			} catch (IOException ignored) {

			}
		}
	}

	@RequestMapping(value="/common/queryFjcfbDto")
	@ResponseBody
	public Map<String,Object> queryFjcfbDto(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setFjid(request.getParameter("fjid"));
		fjcfbDto = fjcfbService.getDto(fjcfbDto);
		map = JSONObject.parseObject(JSONObject.toJSONString(fjcfbDto));
		return map;
	}

}
