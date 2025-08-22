package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.dao.entities.XxglDto;
import com.matridx.igams.common.dao.entities.XxglModel;
import com.matridx.igams.common.dao.post.IXxglDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IShlbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class XxglServiceImpl extends BaseBasicServiceImpl<XxglDto, XxglModel, IXxglDao> implements IXxglService{
	@Autowired
	IShlbService shlbService;
	/**
	 * 根据消息ID获取消息，并根据参数进行替换
	 */
	public String getMsg(String msgid,String...params){
        XxglModel xxglModel = dao.getModelById(msgid);
        String xxnr = xxglModel.getXxnr();
        if(params != null && params.length > 0){
            for(int i=0;i<params.length;i++){
                if(params[i]!=null)
                    xxnr = xxnr.replace("#{" + i +"}", params[i]);
            }
        }
        return xxnr;
	}
	/**
	 * 根据消息ID获取消息，并根据参数进行替换
	 * @param msgid 需要替换的消息id
	 * @param map map中 key为消息内容中«key»，value为替换的内容
	 */
	public String getReplaceMsg(String msgid, Map<String,Object> map){
        XxglModel xxglModel = dao.getModelById(msgid);
        String xxnr = xxglModel.getXxnr();
        //遍历map，找出dto和Dto,将dto中有值的部分提取出来放入map
		for (int i = 0; i < map.keySet().size(); i++) {
			if (map.keySet().toArray()[i].toString().contains("Dto") || map.keySet().toArray()[1].toString().contains("dto")){
				Object ODto = map.get(map.keySet().toArray()[i]);
				Class cls = ODto.getClass();
				Field[] fields = cls.getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					try {
						if (field.get(ODto)!=null && field.get(ODto)!=""){
							map.put(field.getName(),field.get(ODto));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				for (;cls.getSuperclass()!=null;cls = cls.getSuperclass()){
					Field[] superfields = cls.getDeclaredFields();
					for (Field field : superfields) {
						field.setAccessible(true);
						try {
							if (field.get(ODto)!=null && field.get(ODto)!=""){
								map.put(field.getName(),field.get(ODto));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			}
		}
        //遍历消息内容中«»的值，设为map的key，获取map对应的value，并替换«»的内容
		while (xxnr.contains("«")) {
			int startIndex = xxnr.indexOf("«")+1;
			int endIndex = xxnr.indexOf("»");
			String lsythnr = xxnr.substring(startIndex,endIndex);
			Object o = map.get(lsythnr);
			String lsxthnr = "";
			if (o!=null && o!=""){
				lsxthnr = o.toString();
			}
			xxnr = xxnr.replace("«"+lsythnr+"»",lsxthnr);
		}
		return xxnr;
	}

	public String getReplaceMsgByObj(String msgid,Object object){
		XxglModel xxglModel = dao.getModelById(msgid);
		String xxnr = xxglModel.getXxnr();
		try {
			while (xxnr.contains("«")) {
				int start = xxnr.indexOf("«");
				if(start != -1){
					int end = xxnr.indexOf("»");
					String substring = xxnr.substring(start+1, end);
					//替换
					Method method = object.getClass().getMethod("get"+ StringUtil.firstToUpper(substring));
					String value = (String) method.invoke(object);
					xxnr = xxnr.replace("«"+substring+"»", value);
				}else{
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xxnr;
	}
}
