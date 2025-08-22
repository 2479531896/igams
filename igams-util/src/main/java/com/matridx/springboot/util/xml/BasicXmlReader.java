package com.matridx.springboot.util.xml;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;


/**
 * 用于读取XML配置文件
 * @author xinyf
 * {@code @date} 2015-5-20
 */
public class BasicXmlReader {
	private static final Logger log = LoggerFactory.getLogger(BasicXmlReader.class);

	/**
	 * 读取&lt;root>&lt;element attr="value">&lt;/element>&lt;/root>
	 * 此类格式的xml文件转换为List:[{attr:value},...]
	 * @param relativePath	文件相对路径
	 * @param attributes	指定的属性名List
	 */
	public static List<Map<String,String>> readXmlToList(String relativePath, List<String> attributes){
		List<Map<String,String>> list = new ArrayList<>();
		try {
			Document doc = XmlUtil.getInstance().read(
					Thread.currentThread().getContextClassLoader().getResourceAsStream(relativePath)
				);
			Element root = doc.getRootElement();
			List<?> elementList = root.elements();
			for (Object obj : elementList) {
				Element element = (Element)obj;
				list.add(parseElementToMap(element, attributes));
			}
		} catch (DocumentException e) {
			log.error(relativePath+"文件读取失败！",e);
		}
		return list;
	}
	
	/**
	 * 读取&lt;root>&lt;element attr="value">&lt;/element>&lt;/root>
	 * 此类格式的xml文件转换为List:[{attr:value},...]
	 * @param relativePath	文件相对路径
	 * @param attributes	指定的属性名List
	 */
	public static List<Map<Object,Object>> readXmlToObjectList(String relativePath, List<String> attributes){
		List<Map<Object,Object>> list = new ArrayList<>();
		try {
			Document doc = XmlUtil.getInstance().read(
					Thread.currentThread().getContextClassLoader().getResourceAsStream(relativePath)
				);
			Element root = doc.getRootElement();
			List<?> elementList = root.elements();
			for (Object obj : elementList) {
				Element element = (Element)obj;
				list.add(parseElementToObjectMap(element, attributes));
			}
		} catch (DocumentException e) {
			log.error(relativePath+"文件读取失败！",e);
		}
		return list;
	}
	
	/**
	 * 解析xml内容，并保存到hashMap中
	 * 不适用有重复内容的，如多条记录
	 * @param inpStream 需要解析的内容
	 * @return hashMap
	 */
	public static Map<String,Object> readXmlToMap(InputStream inpStream){
		return readXmlToMap(inpStream,"data");
	}
	
	/**
	 * 解析xml内容，并保存到hashMap中
	 * 不适用有重复内容的，如多条记录
	 * @param inpStream 需要解析的内容
	 * @param echoNodeName 重复节点名称
	 * @return hashMap
	 */
	public static Map<String,Object> readXmlToMap(InputStream inpStream,String echoNodeName){
		Map<String,Object> result = null;
		try {
			result = new HashMap<>();
			Document doc = XmlUtil.getInstance().read(inpStream);
			Element root = doc.getRootElement();
			List<?> elementList = root.elements();
			for (Object obj : elementList) {
				Element element = (Element)obj;
				result.put(element.getName(), element.getText());
				parseChildElement(element,result,echoNodeName);
			}
		} catch (DocumentException e) {
			log.error("内容解析失败！",e);
		}
		
		return result;
	}
	
	/**
	 * 解析xml内容，并保存到hashMap中
	 * @param xmlString 需要解析的内容
	 * @return hashMap
	 */
	public static Map<String,Object> readXmlToMap(String xmlString){
		return readXmlToMap(xmlString,"data");
	}
	
	/**
	 * 解析xml内容，并保存到hashMap中
	 * @param xmlString 需要解析的内容
	 * @param echoNodeName 重复节点名称
	 * @return hashMap
	 */
	public static Map<String,Object> readXmlToMap(String xmlString,String echoNodeName){
		Map<String,Object> result = null;
		try {
			result = new HashMap<>();
			Document doc = XmlUtil.getInstance().parse(xmlString);
			Element root = doc.getRootElement();
			result.put(root.getName(), root.getText());
			parseChildElement(root,result,echoNodeName);
		} catch (DocumentException e) {
			log.error("内容解析失败！",e);
		}
		
		return result;
	}
	
	/**
	 * 解析xml内容，并保存到hashMap中
	 * @param xmlString 需要解析的内容
	 * @param echoNodeName 重复节点名称
	 * @return hashMap
	 */
	public static Map<String,Object> readXmlToMapExtend(String xmlString,String echoNodeName){
		return readXmlToMap(xmlString,echoNodeName);
	}
	
	@SuppressWarnings("unchecked")
	private static void parseChildElement(Element parentElement,Map<String,Object> result,String echoNodeName){
		if(parentElement == null)
			return;
		List<Element> list = parentElement.elements();
		List<Map<String,String>> datalist = null;
		if(list == null || list.isEmpty()){
			return;
		}
		for (Element obj : list) {
            if(echoNodeName != null && echoNodeName.equals(obj.getName())){
				if(datalist == null)
					datalist = new ArrayList<>();
				Map<String,String> datamap = parseChildElementToList(obj);
				datalist.add(datamap);
			}else{
				result.put(obj.getName(), obj.getText());
				parseChildElement(obj,result,echoNodeName);
			}
		}
		if(datalist != null){
			result.put(echoNodeName, datalist);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String,String> parseChildElementToList(Element parentElement){
		if(parentElement == null)
			return null;
		Map<String,String> datamap = new HashMap<>();
		List<Element> list = parentElement.elements();
		for (Element obj : list) {
            datamap.put(obj.getName(), obj.getText());
		}
		return datamap;
	}
	
	/**
	 * 解析Element为Map<String,String>
	 */
	public static Map<String,String> parseElementToMap(Element element,List<String> attributes){
		if(null==attributes) return parseElementToMap(element);
		Map<String,String> map = new HashMap<>();
		for (String attr : attributes) {
			String attrValue = element.attributeValue(attr);
			if(attrValue!=null){
				map.put(attr,attrValue);
			}
		}
		return map;
	}
	
	/**
	 * 解析Element为Map<String,String>
	 */
	public static Map<Object,Object> parseElementToObjectMap(Element element,List<String> attributes){
		if(null==attributes) return parseElementToObjectMap(element);
		Map<Object,Object> map = new HashMap<>();
		for (String attr : attributes) {
			String attrValue = element.attributeValue(attr);
			if(attrValue!=null){
				map.put(attr,attrValue);
			}
		}
		return map;
	}
	
	/**
	 * 解析Element为Map<String,String>
	 */
	public static Map<String,String> parseElementToMap(Element element){
		List<String> attributes = new ArrayList<>();
		for (Attribute obj : element.attributes()) {
            attributes.add(obj.getName());
		}
		return parseElementToMap(element,attributes);
	}	
	
	
	/**
	 * 解析Element为Map<String,String>
	 */
	public static Map<Object,Object> parseElementToObjectMap(Element element){
		List<String> attributes = new ArrayList<>();
		for (Attribute obj : element.attributes()) {
            attributes.add(obj.getName());
		}
		return parseElementToObjectMap(element,attributes);
	}	
	
	/**
	 * 重载readXmlToList便于传参
	 */
	public static List<Map<String,String>> readXmlToList(String relativePath, String[] attributes){
		return readXmlToList(relativePath, Arrays.asList(attributes));
	}
	
	/**
	 * 读取&lt;root>&lt;element attr="value">&lt;/element>&lt;/root>
	 * 此类格式的xml文件转换为List:[{attr:value},...]
	 * @param relativePath	文件相对路径
	 */
	public static List<Map<String,String>> readXmlToList(String relativePath){
		return readXmlToList(relativePath, ( List<String>)null);
	}
	
	/**
	 * 读取&lt;root>&lt;element attr="value">&lt;/element>&lt;/root>
	 * 此类格式的xml文件转换为List:[{attr:value},...]
	 * @param relativePath	文件相对路径
	 */
	public static List<Map<Object,Object>> readXmlToObjectList(String relativePath){
		return readXmlToObjectList(relativePath, null);
	}

	/**
	 * 读取xml文件转换为Document对象
	 * @param relativePath	文件相对路径
	 * @return Document
	 */
	public static Document readXmlToDocument(String relativePath){
		Document document = null;
		try {
			document = XmlUtil.getInstance().read(Thread.currentThread().
					getContextClassLoader().getResourceAsStream(relativePath));
		} catch (DocumentException e) {
			log.error(relativePath + "文件读取失败！",e);
		}
		return document;
	}
	
	/**
	 * 读取xml文件转换为复旦天翼财务接口对象
	 * @param relativePath 文件相对路径
	 * @param version 接口版本号
	 * @param userId 接口访问用户
	 * @param funcId 业务类型ID
	 * @return Document
	 */
	public static Document readXmlToDocumentWingsoft(String relativePath, String version,
			String userId, String funcId){
		Document document = readXmlToDocument(relativePath);
		if(document != null){
			Element rootElement = document.getRootElement();
			//设置接口版本号
			Node versionNode = rootElement.selectSingleNode("head/version");
			if(StringUtil.isNotBlank(version)){
				versionNode.setText(version);
			}
			//设置接口访问用户
			Node user_id = rootElement.selectSingleNode("head/user_id");
			if(StringUtil.isNotBlank(userId)){
				user_id.setText(userId);
			}
			//设置业务类型参数
			Node func_id = rootElement.selectSingleNode("head/func_id");
			if(StringUtil.isNotBlank(funcId)){
				func_id.setText(funcId);
			}
			//设置业务流水号
			/*Node seq_id = rootElement.selectSingleNode("head/seq_id");
			if(StringUtil.isNotBlank(seqId)){
				seq_id.setText(seqId);
			}*/
			//设置报文发起时间：格式yyyy-MM-dd HH:mm:ss
			Node seq_datetime = rootElement.selectSingleNode("head/seq_datetime");
			seq_datetime.setText(DateUtils.getCustomFomratCurrentDate(null));
		}
		return document;
	}
	
	/**
     * URL访问WebService接口
     * @param path 访问路径
     * @param params 拼接好的参数：name=zhangsan&age=21
     * @return byte[] 字节数组
     */
	public static String urlConnWebService(String path, String params) throws Exception{
		URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");// 提交模式
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.connect();
        OutputStream outputStream = conn.getOutputStream();
        DataOutputStream out = new DataOutputStream(outputStream);
        out.writeBytes(params);
        out.flush();
        out.close();
        InputStream inputStream = conn.getInputStream();
        String result = inputStreamToString(inputStream, null);
        conn.disconnect();
        inputStream.close();
		return result;
    }
	
	/**
     * URL访问WebService接口
     * @param path 访问路径
     * @param params 拼接好的参数：name=zhangsan&age=21
     * @return byte[] 字节数组
     */
	public static String urlConnWebServiceByGet(String path, String params) throws Exception{
		URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");// 提交模式
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.connect();
        if(StringUtil.isNotBlank(params)){
	        OutputStream outputStream = conn.getOutputStream();
	        DataOutputStream out = new DataOutputStream(outputStream);
	        out.writeBytes(params);
	        out.flush();
	        out.close();
        }
        InputStream inputStream = conn.getInputStream();
        String result = inputStreamToString(inputStream, null);
        conn.disconnect();
        inputStream.close();
		return result;
    }
	
    
    /**
     * 读取输入流中数据
     * @param inputStream 输入流
     * @return byte[] 字节数组
     */
    public static byte[] readInputStream(InputStream inputStream) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while((len = inputStream.read(buffer)) != -1){
        	outputStream.write(buffer, 0, len);
        }
        byte[] data = outputStream.toByteArray();
        outputStream.close();
        inputStream.close();
        return data;
    }
    
    /**
     * 读取输入流中数据，转换为字符串
     * @param inputStream 输入流
     * @param encode 编码方式：默认为utf-8
     * @return String
     */
    public static String inputStreamToString(InputStream inputStream, String encode) throws Exception{
    	StringBuilder stringBuffer = new StringBuilder();
    	byte[] bytes = new byte[1024];
    	int len;
    	if(StringUtil.isBlank(encode)){
    		encode = "utf-8";
    	}
    	while((len = inputStream.read(bytes)) != -1){
    		stringBuffer.append(new String(bytes, 0, len ,encode));
    	}
    	return stringBuffer.toString();
    }
    
	public static void main(String[] args) {
    	//Map<String,Object> map= BasicXmlReader.readXmlToMap("<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'><soap:Body><ns2:executeResponse xmlns:ns2='http://service.matridx.com/'><return><zdsjzx><success>success-1002</success><data><GRJSDJPRRQ>null</GRJSDJPRRQ><CZLX>I</CZLX><RN>1</RN><DWDM>518052</DWDM><XRZW>null</XRZW><XWDM>3</XWDM><GWLB>null</GWLB><GJDQM>156</GJDQM><ZHXLBYXX>浙江工业大学</ZHXLBYXX><XXM>null</XXM><XLSYRQ>201206</XLSYRQ><BZLBM>null</BZLBM><ZJHM>330104198607244428</ZJHM><GATQWM>null</GATQWM><ZGH>2513006</ZGH><XBM>2</XBM><ZZMMM>01</ZZMMM><CSDM>null</CSDM><XWSYRQ>20120606</XWSYRQ><JG>330100</JG><YKT>null</YKT><GUID>08313C64D6D0FD12E050470AC1137020</GUID><RXNY>201303</RXNY><YWXM>null</YWXM><PXM>null</PXM><CSRQ>1986-07-24</CSRQ><XM>施水娟</XM><MZM>01</MZM><ZYJSZWDM>000</ZYJSZWDM><JZGLBDM>7030</JZGLBDM><ZWJBDM>null</ZWJBDM><WHCDM>1a</WHCDM><DQZTM>11</DQZTM><RZGWM>null</RZGWM><RZDWDM>518000</RZDWDM><CYM>null</CYM><ZYJSZWPRRQ>null</ZYJSZWPRRQ><RYFL>1</RYFL><XMPY>null</XMPY><XKDM>null</XKDM><CJGZNY>201303</CJGZNY><ZJLXM>1</ZJLXM><SSMC>医务部</SSMC><CLRQ>2014-11-19</CLRQ><XZJBJSRQ>null</XZJBJSRQ><ZYJSZWJB>null</ZYJSZWJB><GWDJM>null</GWDJM><GRJSDJDM>null</GRJSDJDM><HYZKM>null</HYZKM><SJLY>RS</SJLY></data></zdsjzx></return></ns2:executeResponse></soap:Body></soap:Envelope>");

    }
}
