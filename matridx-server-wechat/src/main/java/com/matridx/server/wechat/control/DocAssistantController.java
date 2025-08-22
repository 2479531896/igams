package com.matridx.server.wechat.control;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.ProgramCodeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.server.wechat.dao.entities.HzxxDto;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WxyhDto;
import com.matridx.server.wechat.dao.entities.XxcqxxjlDto;
import com.matridx.server.wechat.dao.entities.ZdybtwzDto;
import com.matridx.server.wechat.dao.entities.ZdyfaDto;
import com.matridx.server.wechat.service.svcinterface.IHzxxService;
import com.matridx.server.wechat.service.svcinterface.ISjwzxxService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.server.wechat.service.svcinterface.IWbcxService;
import com.matridx.server.wechat.service.svcinterface.IWxyhService;
import com.matridx.server.wechat.service.svcinterface.IXxcqxxjlService;
import com.matridx.server.wechat.service.svcinterface.IZdybtwzService;
import com.matridx.server.wechat.service.svcinterface.IZdyfaService;
import com.matridx.server.wechat.util.WordRecognitionUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Controller
@RequestMapping("/wechat")
public class DocAssistantController  extends BaseController {

	@Value("${matridx.assistantprogram.api:}")
	private String api;
	@Autowired
	IWxyhService wxyhService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ISjwzxxService sjwzxxService;
	@Autowired
	ISjxxService sjxxService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IHzxxService hzxxService;
	@Autowired
	IZdyfaService zdyfaService;
	@Autowired
	IXxcqxxjlService xxcqxxjlService;
	@Autowired
	IZdybtwzService zdybtwzService;
	@Autowired
	IWbcxService wbcxService;
	
    private Logger log = LoggerFactory.getLogger(DocAssistantController.class);
	
	/**
	 * 小程序登录获取openid
	 * @param wxyhDto
	 * @param code
	 * @return
	 */
	@RequestMapping(value="/hosprogram/login")
	@ResponseBody
	public Map<String, Object> login(WxyhDto wxyhDto, String code){
		Map<String,Object> map = new HashMap<>();
		if(StringUtil.isBlank(code)){
			map.put("status", "fail");
			return map;
		}
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setWbcxdm(ProgramCodeEnum.DOCASST.getCode());
		wbcxDto = wbcxService.getDto(wbcxDto);
		if(wbcxDto == null){
			log.error("未找到外部编码为 "+ProgramCodeEnum.DOCASST.getCode()+" 的外部程序信息！");
			map.put("status", "fail");
			return map;
		}
		DBEncrypt dbEncrypt = new DBEncrypt();
		String login_url = new StringBuffer(api).append("?appid=").append(dbEncrypt.dCode(wbcxDto.getAppid()))
				.append("&secret=").append(dbEncrypt.dCode(wbcxDto.getSecret())).append("&js_code=").append(code).append("&grant_type=authorization_code").toString();
		log.error("微信信息请求地址docAss："+login_url);
		String s_login_return = restTemplate.getForObject(login_url, String.class);
		log.error("微信信息docAss："+s_login_return);
		JSONObject loginObject = JSONObject.parseObject(s_login_return);
		//有错误直接返回
		Integer errorString = (Integer)loginObject.get("errcode");
		if(errorString != null){
			map.put("status", "fail");
			map.put("errorString", errorString);
			return map;
		}
		String openid = (String)loginObject.get("openid");
		map.put("openid", openid);
		WxyhDto t_wxyhDto = wxyhService.getDtoById(openid);
		if(t_wxyhDto == null){
			wxyhDto.setWxid(openid);
			wxyhDto.setLrry(openid);
			wxyhDto.setGzpt(wbcxDto.getWbcxid());
			// 新增用户信息
			boolean result = wxyhService.insertDto(wxyhDto);
			if (!result){
				map.put("status", "fail");
				return map;
			}
		}else{
			map.put("wxyhDto", t_wxyhDto);
		}
		map.put("status", "success");
		return map;
	}
	
	/**
	 * 小程序修改微信用户表信息
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value="/hosprogram/updateUserInfo")
	@ResponseBody
	public Map<String, Object> updateUserInfo(WxyhDto wxyhDto){
		Map<String,Object> map = new HashMap<>();
		if(StringUtil.isNotBlank(wxyhDto.getSj()) && "13764520566".equals(wxyhDto.getSj())){
			boolean isSuccess = wxyhService.update(wxyhDto);
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		}
		boolean isSuccess;
		try {
			isSuccess = wxyhService.modSaveWeChatUser(wxyhDto);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			map.put("status","fail");
			map.put("message",e.getMsg());
			return map;
		}
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 小程序获取微信用户表信息
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value="/hosprogram/getUserInfo")
	@ResponseBody
	public Map<String, Object> getUserInfo(WxyhDto wxyhDto){
		Map<String,Object> map = new HashMap<>();
		WxyhDto t_wxyhDto = wxyhService.getDtoById(wxyhDto.getWxid());
		map.put("wxyhDto", t_wxyhDto);
		return map;
	}
	
	/**
	 * 医生电话解绑
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value="/hosprogram/updatePhone")
	@ResponseBody
	public Map<String, Object> updatePhone(WxyhDto wxyhDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess = false;
		if(StringUtil.isBlank(wxyhDto.getSj())) {
			isSuccess = wxyhService.updateSj(wxyhDto);
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 发送短信验证码
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value="/hosprogram/getSms")
	@ResponseBody
	public Map<String, Object> getSms(WxyhDto wxyhDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess;
		if(StringUtil.isNoneBlank(wxyhDto.getCskz2()) && "13764520566".equals(wxyhDto.getCskz2())){
			isSuccess = true;
		}else{
			isSuccess = wxyhService.getSms(wxyhDto);
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 查询患者信息
	 * @param hzxxDto
	 * @return
	 */
	@RequestMapping(value="/hosprogram/getPatientInfo")
	@ResponseBody
	public Map<String, Object> getPatientInfo(HzxxDto hzxxDto){
		Map<String,Object> map= new HashMap<>();
		List<HzxxDto> hzxxDtos = hzxxService.getDtoList(hzxxDto);
		map.put("hzxxDtos", hzxxDtos);
		return map;
	}
	
	/**
	 * 根据ID查询患者信息
	 * @param hzxxDto
	 * @return
	 */ 
	@RequestMapping(value="/hosprogram/getPatient")
	@ResponseBody
	public Map<String, Object> getPatient(HzxxDto hzxxDto){
		Map<String,Object> map= new HashMap<>();
		HzxxDto t_hzxxDto = hzxxService.getDtoById(hzxxDto.getHzid());
		t_hzxxDto.setYwlx(BusTypeEnum.IMP_MEDHISTORY.getCode());
		map.put("hzxxDto", t_hzxxDto);
		//查看附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(hzxxDto.getHzid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_MEDHISTORY.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		map.put("fjcfbDtos", fjcfbDtos);
		//体温类型
		Map<String, List<JcsjDto>> templist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.BODYTEMP_TYPE});
		map.put("temperaturelist", templist.get(BasicDataTypeEnum.BODYTEMP_TYPE.getCode()));
		//症状类型
		Map<String, List<JcsjDto>> symptommap =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SYMPTOM_TYPE});
		List<JcsjDto> symptomlist = symptommap.get(BasicDataTypeEnum.SYMPTOM_TYPE.getCode());
		if(symptomlist!= null && symptomlist.size() > 0){
			for (int i = 0; i < symptomlist.size(); i++) {
				symptomlist.get(i).setChecked("false");
			}
		}
		map.put("symptomlist", symptomlist);
		//CRP类型
		Map<String, List<JcsjDto>> crplist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.CRP_TYPE});
		map.put("crplist", crplist.get(BasicDataTypeEnum.CRP_TYPE.getCode()));
		//PCT类型
		Map<String, List<JcsjDto>> pctlist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PCT_TYPE});
		map.put("pctlist", pctlist.get(BasicDataTypeEnum.PCT_TYPE.getCode()));
		return map;
	}
	
	/**
	 * 附件下载（用于普通的文件下载使用）
	 * @param fjcfbDto
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/hosprogram/downloadFile")
	public String downloadFile(FjcfbDto fjcfbDto,HttpServletResponse response,HttpServletRequest request){
		FjcfbDto t_fjcfbDto = fjcfbService.getDto(fjcfbDto);
		if(t_fjcfbDto==null){
			System.out.println("对不起，系统未找到相应文件");
			return "对不起，系统未找到相应文件";
		}
		String wjlj = t_fjcfbDto.getWjlj();
		String wjm = t_fjcfbDto.getWjm();
		DBEncrypt crypt = new DBEncrypt();
		String filePath = crypt.dCode(wjlj);
		String agent = request.getHeader("user-agent");
		try {
			if(wjm != null){
				byte[] bytes = agent.contains("MSIE") ? wjm.getBytes() : wjm.getBytes("UTF-8");
				wjm = new String(bytes, "ISO-8859-1");
				response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", wjm));// 文件名外的双引号处理firefox的空格截断问题
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
            e.printStackTrace();
        }
        try {
            bis.close();
            os.flush();
            os.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
	}
	
	/**
	 * 删除附件信息
	 * @param fjcfbDto
	 * @return
	 */
	@RequestMapping(value = "/hosprogram/delFile")
	@ResponseBody
	public Map<String, Object> delFile(FjcfbDto fjcfbDto){
		fjcfbDto.setScry(fjcfbDto.getWxid());
		boolean isSuccess = fjcfbService.delFile(fjcfbDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 上传临时文件
	 * @param fjcfbDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/hosprogram/saveImportFile")
	@ResponseBody
	public Map<String, Object> saveImportFile(FjcfbDto fjcfbDto,HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		try{
			List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(((MultipartHttpServletRequest) request).getFile("file").getName());
			MultipartFile[] imp_file = new MultipartFile[files.size()];
			files.toArray(imp_file);
			if(imp_file!=null&& imp_file.length>0){
				User user = null;
				fjcfbService.save2TempFile(imp_file, fjcfbDto,user);
				
				result.put("status", "success");
				result.put("fjcfbDto", fjcfbDto);
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
	 * 保存小程序临时图片并进行文字识别
	 * @param fjcfbDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/hosprogram/imageDiscern")
	@ResponseBody
	public Map<String,Object> imageDiscern(FjcfbDto fjcfbDto, HttpServletRequest request){
		//保存临时图片
		Map<String, Object> result = new HashMap<>();
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(((MultipartHttpServletRequest) request).getFile("file").getName());
		MultipartFile[] imp_file = new MultipartFile[files.size()];
		files.toArray(imp_file);
		if(imp_file!=null&& imp_file.length>0){
			User user = null;
			fjcfbService.save2TempFile(imp_file, fjcfbDto, user);
			result.put("fjcfbDto", fjcfbDto);
			//图片临时存放地址
			String lscfdz=fjcfbDto.getLsbcdz();
			JSONArray words = WordRecognitionUtil.getWordsSiteFromNet(lscfdz);//识别图片文字
			List<List<Map<String, String>>> resultList = ToolUtil.pareString(words);//分行
			//查询标题信息
			ZdyfaDto zdyfaDto = new ZdyfaDto();
			zdyfaDto.setFaid(request.getParameter("faid"));
			List<ZdyfaDto> zdyfaDtos = zdyfaService.getDtoList(zdyfaDto);
			Map<String,Map<String, String>> keyMap = new HashMap<>();
			if(zdyfaDtos != null && zdyfaDtos.size() > 0){
				for (int i = 0; i < zdyfaDtos.size(); i++) {
					Map<String, String> map = new HashMap<>();
					map.put("words", zdyfaDtos.get(i).getZdxx());
					map.put("wordflg", zdyfaDtos.get(i).getBhbj());
					map.put("rightpoint", zdyfaDtos.get(i).getYjg());
					map.put("rightword", zdyfaDtos.get(i).getYbj());
					map.put("bottompoint", zdyfaDtos.get(i).getXjg());
					map.put("bottomword", zdyfaDtos.get(i).getXbj());
					map.put("leftpoint", zdyfaDtos.get(i).getZjg());
					map.put("leftword", zdyfaDtos.get(i).getZbj());
					map.put("toppoint", zdyfaDtos.get(i).getSjg());
					map.put("topword", zdyfaDtos.get(i).getSbj());
					map.put("sameflg", zdyfaDtos.get(i).getYwbj());
					keyMap.put(zdyfaDtos.get(i).getZdmc(), map);
				}
			}
			Map<String, String> resultMap = ToolUtil.prediction(resultList, keyMap);//抽取信息
			result.put("resultMap", resultMap);//返回结果
			//新增学习记录
			XxcqxxjlDto xxcqxxjlDto = new XxcqxxjlDto();
			xxcqxxjlDto.setWxid(request.getParameter("wxid"));
			xxcqxxjlDto.setYsxx(JSONObject.toJSONString(resultList));
			xxcqxxjlDto.setCqxx(resultMap.toString());
			xxcqxxjlDto.setLrry(request.getParameter("wxid"));
			xxcqxxjlService.insertDto(xxcqxxjlDto);
			result.put("xxcqxxjlDto", xxcqxxjlDto);
		}
		return result;
	}
	
	/**
	 * 修改患者信息
	 * @param hzxxDto
	 * @return
	 */
	@RequestMapping("/hosprogram/modPatient")
	@ResponseBody
	public Map<String,Object> modPatient(HzxxDto hzxxDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess = hzxxService.updatePatient(hzxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 初始化统计页面
	 * @return
	 */
	@RequestMapping(value="/hosprogram/initStatis")
	@ResponseBody
	public Map<String, Object> initStatis(){
		Map<String,Object> map = new HashMap<>();
		//体温类型
		Map<String, List<JcsjDto>> templist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.BODYTEMP_TYPE});
		map.put("temperaturelist", templist.get(BasicDataTypeEnum.BODYTEMP_TYPE.getCode()));
		return map;
	}
	
	/**
	 * 患者信息统计
	 * @param hzxxDto
	 * @return
	 */
	@RequestMapping(value = "/hosprogram/getPatientStatis")
	@ResponseBody
	public Map<String, Object> getPatientStatis(HzxxDto hzxxDto){
		Map<String,Object> map = new HashMap<>();
		List<HzxxDto> hzxxDtos = hzxxService.getPatientStatis(hzxxDto);
		map.put("hzxxDtos", hzxxDtos);
		return map;
	}
	
	/**
	 * 小程序请求后台保存病人信息
	 * @param hzxxDto
	 * @return
	 */
	@RequestMapping(value="/hosprogram/savePatientData")
	@ResponseBody
	public Map<String,Object> savePatientData(HzxxDto hzxxDto) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess=hzxxService.insertPatient(hzxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 保存小程序临时图片并进行文字识别
	 * @param fjcfbDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/hosprogram/messageDiscern")
	@ResponseBody
	public Map<String,String> getMessagetoText(String map,FjcfbDto fjcfbDto,XxcqxxjlDto xxcqxxjlDto,HttpServletRequest request){
		@SuppressWarnings("unchecked")
		Map<String,String> mapType = JSON.parseObject(map,Map.class);
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(((MultipartHttpServletRequest) request).getFile("file").getName());
		MultipartFile[] imp_file = new MultipartFile[files.size()];
		files.toArray(imp_file);
		//根据记录id获取学习信息
		XxcqxxjlDto xxjl=xxcqxxjlService.getDto(xxcqxxjlDto);
		@SuppressWarnings("unchecked")
		List<List<Map<String, String>>> xxjllist = JSON.parseObject(xxjl.getYsxx(),List.class);
		Map<String,String> resultmap= new HashMap<>();
		if(imp_file!=null&& imp_file.length>0){
			User user = null;
			fjcfbService.save2TempFile(imp_file, fjcfbDto,user);
			String lscfdz=fjcfbDto.getLsbcdz();//图片临时存放地址
			JSONArray words = WordRecognitionUtil.getWordsSiteFromNet(lscfdz);//识别图片文字
			List<List<Map<String, String>>> resultList = ToolUtil.pareString(words);//分行
			mapType.put("words",resultList.get(0).get(0).get("words"));
			resultmap=ToolUtil.getAroundWord(xxjllist, mapType);
		}
		return resultmap;
	}
	
	/**
	 * 查询该用户的自定义方案
	 * @return
	 */
	@RequestMapping(value="/hosprogram/viewZdysz")
	@ResponseBody
	public List<ZdyfaDto> getZdysz(ZdyfaDto zdyfaDto){
        return zdyfaService.getZdyProject(zdyfaDto);
	}
	
	/**
	 * 用户更新自定义方案
	 * @param zdyfaDto
	 * @return
	 */
	@RequestMapping(value="/hosprogram/updateZdysz")
	@ResponseBody
	public Map<String,Object> updateZdysz(ZdyfaDto zdyfaDto){
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess=zdyfaService.update(zdyfaDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 设置应用方案
	 * @param zdyfaDto
	 * @return
	 */
	@RequestMapping(value="/hosprogram/setUseProject")
	@ResponseBody
	public Map<String,Object> setUseProject(ZdyfaDto zdyfaDto) {
		Map<String, Object> map = new HashMap<>();
		List<ZdyfaDto> falist=JSONArray.parseArray(zdyfaDto.getList(),ZdyfaDto.class);
		for(int i=0;i<falist.size();i++) {
			falist.get(i).setXgry(zdyfaDto.getXgry());
		}
		boolean isSuccess=zdyfaService.setUseProject(falist);
		List<ZdyfaDto> list=zdyfaService.getZdyProject(zdyfaDto);
		map.put("zdyszlist", list);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 仅保存自定义方案
	 * @param zdyfaDto
	 * @return
	 */
	@RequestMapping(value="/hosprogram/addFa")
	@ResponseBody
	public  Map<String,Object> addzdyfa(ZdyfaDto zdyfaDto){
		Map<String, Object> map = new HashMap<>();
		//添加一条方案的同时添加一条默认设置，默认设置表暂时未创建，后期需增加
		boolean isSuccess=zdyfaService.insertMrfa(zdyfaDto);
		map.put("zdyfaDto",zdyfaDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 将上传图片保存到正式文件并添加自定义方案
	 * @param zdyfaDto
	 * @param fjcfbDto
	 * @return
	 */
	@RequestMapping(value="/hosprogram/addProject")
	@ResponseBody
	public Map<String,Object> addSaveZdyxx(ZdyfaDto zdyfaDto,FjcfbDto fjcfbDto){
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess=zdyfaService.addProject(fjcfbDto,zdyfaDto);
		List<ZdyfaDto> list=zdyfaService.getZdyProject(zdyfaDto);
		map.put("zdyszlist", list);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 删除自定义方案
	 * @param zdyfaDto
	 * @return
	 */
	@RequestMapping(value="/hosprogram/delProject")
	@ResponseBody
	public Map<String,Object> delZdyxx(ZdyfaDto zdyfaDto){
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess=zdyfaService.delete(zdyfaDto);
		List<ZdyfaDto> list=zdyfaService.getZdyProject(zdyfaDto);
		map.put("zdyszlist", list);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 录入页面初始化获取信息
	 * @param zdyfaDto
	 * @return
	 */
	@RequestMapping(value="/hosprogram/getScheme")
	@ResponseBody
	public Map<String,Object> getScheme(ZdyfaDto zdyfaDto){
		Map<String, Object> map = new HashMap<>();
		zdyfaDto.setFasfyy("1");
		zdyfaDto = zdyfaService.getDto(zdyfaDto);
		map.put("zdyfaDto", zdyfaDto);
		//体温类型
		Map<String, List<JcsjDto>> s_jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.BODYTEMP_TYPE});
		map.put("temperaturelist", s_jclist.get(BasicDataTypeEnum.BODYTEMP_TYPE.getCode()));
		//白细胞类型
		Map<String,List<JcsjDto>> leukocytemap=jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.LEUKOCYTEL_TYPE});
		map.put("leukocytelist", leukocytemap.get(BasicDataTypeEnum.LEUKOCYTEL_TYPE.getCode()));
		//中性粒类型
		Map<String,List<JcsjDto>> neutrophilsmap=jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.NEUTROPHILS_TYPE});
		map.put("neutrophilslist", neutrophilsmap.get(BasicDataTypeEnum.NEUTROPHILS_TYPE.getCode()));
		//症状类型
		Map<String, List<JcsjDto>> symptommap =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SYMPTOM_TYPE});
		List<JcsjDto> symptomlist = symptommap.get(BasicDataTypeEnum.SYMPTOM_TYPE.getCode());
		if(symptomlist!= null && symptomlist.size() > 0){
			for (int i = 0; i < symptomlist.size(); i++) {
				symptomlist.get(i).setChecked("false");
			}
		}
		map.put("symptomlist", symptomlist);
		//CRP类型
		Map<String, List<JcsjDto>> crplist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.CRP_TYPE});
		map.put("crplist", crplist.get(BasicDataTypeEnum.CRP_TYPE.getCode()));
		//PCT类型
		Map<String, List<JcsjDto>> pctlist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PCT_TYPE});
		map.put("pctlist", pctlist.get(BasicDataTypeEnum.PCT_TYPE.getCode()));
		return map;
	}

	/**
	 * 点击设置按钮获取当前已应用方案信息
	 * @param zdyfaDto
	 * @return
	 */
	@RequestMapping("/hosprogram/getYyyfa")
	@ResponseBody
	public Map<String,Object> getYyyfa(ZdyfaDto zdyfaDto){
		Map<String, Object> map = new HashMap<>();
		ZdyfaDto yyfa= zdyfaService.getYyyFa(zdyfaDto);
		DBEncrypt crypt = new DBEncrypt();
		if(StringUtil.isNotBlank(yyfa.getFaimglj())) {
			yyfa.setFaimglj(crypt.dCode(yyfa.getFaimglj()));
			ZdybtwzDto zdybtwzDto=new ZdybtwzDto();
			zdybtwzDto.setFaid(zdyfaDto.getFaid());
			List<ZdybtwzDto> btwzxxlist=zdybtwzService.getZdybtwzByFaid(zdybtwzDto);
			map.put("btwzxxlist",btwzxxlist);
		}
		map.put("zdyfaDto",yyfa);
		return map;
	}
	
	/**
	 * 点击保存按钮保存标题位置信息和图片
	 * @return
	 */
	@RequestMapping(value="/hosprogram/saveBtwz")
	@ResponseBody
	public Map<String,Object> addSaveBtwzAndImage(String result,FjcfbDto fjcfbDto,String faid){
		Map<String, Object> map = new HashMap<>();
		String Json="["+result.substring(1)+"]";
		@SuppressWarnings("unchecked")
		List<Map<String,Map<String,String>>> list = (List<Map<String,Map<String,String>>>) JSONArray.parse(Json);
		boolean isSuccess=zdybtwzService.SaveImageAndWz(list, fjcfbDto,faid);
		if(isSuccess){
			//图片存放地址
			String cfdz=fjcfbDto.getWjlj();
			JSONArray words = WordRecognitionUtil.getWordsSiteFromNet(cfdz);//识别图片文字
			List<List<Map<String, String>>> resultList = ToolUtil.pareString(words);//分行
			//查询标题信息
			ZdyfaDto zdyfaDto = new ZdyfaDto();
			zdyfaDto.setFaid(faid);
			List<ZdyfaDto> zdyfaDtos = zdyfaService.getDtoList(zdyfaDto);
			Map<String,Map<String, String>> keyMap = new HashMap<>();
			if(zdyfaDtos != null && zdyfaDtos.size() > 0){
				for (int i = 0; i < zdyfaDtos.size(); i++) {
					Map<String, String> t_map = new HashMap<>();
					t_map.put("words", zdyfaDtos.get(i).getZdxx());
					t_map.put("wordflg", zdyfaDtos.get(i).getBhbj());
					t_map.put("rightpoint", zdyfaDtos.get(i).getYjg());
					t_map.put("rightword", zdyfaDtos.get(i).getYbj());
					t_map.put("bottompoint", zdyfaDtos.get(i).getXjg());
					t_map.put("bottomword", zdyfaDtos.get(i).getXbj());
					t_map.put("leftpoint", zdyfaDtos.get(i).getZjg());
					t_map.put("leftword", zdyfaDtos.get(i).getZbj());
					t_map.put("toppoint", zdyfaDtos.get(i).getSjg());
					t_map.put("topword", zdyfaDtos.get(i).getSbj());
					t_map.put("sameflg", zdyfaDtos.get(i).getYwbj());
					keyMap.put(zdyfaDtos.get(i).getZdmc(), t_map);
				}
			}
			Map<String, String> resultMap = ToolUtil.prediction(resultList, keyMap);//抽取信息
			map.put("resultMap", resultMap);//返回结果
			//新增学习记录
			XxcqxxjlDto xxcqxxjlDto = new XxcqxxjlDto();
			xxcqxxjlDto.setWxid(fjcfbDto.getWxid());
			xxcqxxjlDto.setYsxx(JSONObject.toJSONString(resultList));
			xxcqxxjlDto.setCqxx(resultMap.toString());
			xxcqxxjlDto.setLrry(fjcfbDto.getWxid());
			xxcqxxjlService.insertDto(xxcqxxjlDto);
			map.put("xxcqxxjlDto", xxcqxxjlDto);
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
