package com.matridx.igams.dmp.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.InterfaceModel;
import com.matridx.igams.common.dao.entities.InterfaceReturnModel;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.dmp.dao.entities.ZyxxDto;
import com.matridx.igams.dmp.service.svcinterface.IZyxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.encrypt.Encrypt;

@Controller
@RequestMapping("/ws")
public class DataWsControl extends BaseController{

	@Autowired
	private IZyxxService zyxxService;
	
	@RequestMapping(value="/data/getInterface")
	@ResponseBody
	public String getInterface(BufferedReader br,HttpServletRequest request) {
		
		String inputLine;
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return null;
        }
        
        InterfaceModel inModel = JSONObject.parseObject(str, InterfaceModel.class);
		InterfaceReturnModel returnModel = new InterfaceReturnModel();
		//确认请求的内容
		if(StringUtil.isBlank(inModel.getResourceid())||StringUtil.isBlank(inModel.getFromsysid())) {
            return null;
        }
		
		ZyxxDto zyxxDto = new ZyxxDto();
		zyxxDto.setRzyhdm(inModel.getFromsysid());
		zyxxDto.setZydm(inModel.getResourceid());
		//确认请求的用户是否有权限
		List<ZyxxDto> zyxxDtos = zyxxService.getAuthList(zyxxDto);
		
		if(zyxxDtos==null || zyxxDtos.size() ==0)
		{
			returnModel.setStatus("-1");
			returnModel.setMsg("该用户认证未通过！");
			return JSONObject.toJSONString(returnModel);
		}
		
		zyxxDto = zyxxDtos.get(0);
		//确认请求的验证信息是否正确
		//密钥
		String key = new DBEncrypt().dCode(zyxxDto.getRzkey());
		//签名: xml文本+密钥取MD5摘要
		String sign = Encrypt.encrypt(inModel.getData() + key, "md5");
		//验证信息无法匹配
		if(!sign.equalsIgnoreCase(inModel.getSign())) {
			returnModel.setStatus("-2");
			returnModel.setMsg("信息验证未通过！");
			return JSONObject.toJSONString(returnModel);
		}
		//如果对接方式为本地的话，
		if("local".equals(zyxxDto.getDjfs())) {
			//执行类
	    	String classService = zyxxDto.getCs1();
	    	//执行方法
	    	String classMethod = zyxxDto.getCs2();
	    	//设置秘钥
	    	inModel.setKey(key);
			try {
	        	
	    		Object serviceInstance = ServiceFactory.getService(classService);//获取方法所在class
	    		
	    		//获取方法
				Method getCountMethod = serviceInstance.getClass().getMethod(classMethod,InterfaceModel.class);//获取计数方法
				//执行方法
				Object object = getCountMethod.invoke(serviceInstance,inModel);
				
				return object.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return null;
	}
}
