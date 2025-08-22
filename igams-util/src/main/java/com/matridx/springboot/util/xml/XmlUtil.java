package com.matridx.springboot.util.xml;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * XML解析实用工具类
 * 
 * @author zhangqy
 * @version v1.0.0
 */
@SuppressWarnings("rawtypes")
public class XmlUtil {

	private static final Logger log = LoggerFactory.getLogger(XmlUtil.class);

	/**
	 * @return Returns the instance.
	 */
	public static XmlUtil getInstance() {
		return instance;
	}

	private final static XmlUtil instance = new XmlUtil();

	private final SAXReader reader;

	private XmlUtil() {
		reader = new SAXReader();
	}

	/**
	 * 把传进来的Xml字符串转换成Dom4j的Document对象
	 */
	public Document parse(String xmlString) throws DocumentException {
		Document document;
		document = DocumentHelper.parseText(xmlString);
		return document;
	}

	/**
	 * 从文件读取XML，输入文件名，返回XML文档
	 */
	public Document read(String fileName) throws DocumentException {
		Document document;
		document = reader.read(new File(fileName));
		return document;
	}

	/**
	 * 从一个URL读取XML
	 * 
	 * @param url
	 *            URL
	 * @return Document
	 */
	public Document read(URL url) throws DocumentException {
		Document document;
		document = reader.read(url);

		return document;
	}

	/**
	 * 从一个InputStream读入XML
	 * 
	 * @param is
	 *            InpustStream
	 * @return Document
	 */
	public Document read(InputStream is) throws DocumentException {
		Document document;
		document = reader.read(is);
		return document;
	}

	/**
	 * 从一个InputStream读入XML
	 *            InpustStream
	 * @return Document
	 */
	public Document read(Reader r) throws DocumentException {
		Document document;
		document = reader.read(r);
		return document;
	}

	/**
	 * 获得一个XML文档的Root Element
	 * 
	 * @param doc
	 *            Document文档
	 * @return Element
	 */
	public Element getRootElement(Document doc) {
		return doc.getRootElement();
	}

	/**
	 * 获得一个XML文档的所有Element
	 * 
	 * @param doc
	 *            Document文档
	 * @return Iterator
	 */
	public Iterator getAllElement(Document doc) {
		return doc.getRootElement().elementIterator();
	}

	/**
	 * 获得一个XML文档所有指定Element Name的Element
	 * 
	 * @param doc
	 *            Document文档
	 * @param elementName
	 *            要获得的Element Name
	 * @return Iterator
	 */
	public Iterator getNamedElement(Document doc, String elementName) {
		return doc.getRootElement().elementIterator(elementName);
	}

	/**
	 * 获得一个XML文档的所有属性
	 * 
	 * @param doc
	 *            Document文档
	 * @return Iterator
	 */
	public Iterator getAllAttribute(Document doc) {
		return doc.getRootElement().attributeIterator();
	}

	/**
	 * 获得一个XPath表达式的所有Node
	 * 
	 * @param doc
	 *            Document文档
	 * @param xpathString
	 *            XPath表达式
	 * @return List
	 */
	public List getNodes(Document doc, String xpathString) {
		return doc.selectNodes(xpathString);

	}

	/**
	 * 通过XPath表达式只获得一个Node
	 * 
	 * @param doc
	 *            Document文档
	 * @param xpathString
	 *            XPath表达式
	 * @return Node
	 */
	public Node getNode(Document doc, String xpathString) {
		return doc.selectSingleNode(xpathString);
	}

	/**
	 * 通过XPath表达式获得一个Node的值
	 * 
	 * @param node
	 *            Node
	 * @param xpathString
	 *            XPath表达式
	 * @return String
	 */
	public String getNodeValue(Node node, String xpathString) {
		return node.valueOf(xpathString);
	}

	/**
	 * 通过XPath表达式获得一个值
	 * 
	 * @param doc
	 *            Document文档
	 * @param xpathString
	 *            XPath表达式
	 * @return String
	 */
	public String getValue(Document doc, String xpathString) {
		return doc.valueOf(xpathString);
	}

	/**
	 * 计算某一个Element在XML文档的个数
	 * 
	 * @param doc
	 *            Document文档
	 * @param elementName
	 *            Element名字
	 * @return int
	 */
	public int count(Document doc, String elementName) {
		return Integer.parseInt(doc.valueOf("count(//" + elementName + ")"));
	}

	/**
	 * 将XML文件的内容转化为String
	 */
	public String doc2String(Document doc) {
		String str = "";
		// 使用输出流来进行转化
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		// 使用GBK编码
		OutputFormat format = new OutputFormat("  ", false, "UTF-8");
		format.setNewlines(false);
		XMLWriter writer;
		try {
			writer = new XMLWriter(out, format);
			writer.write(doc);
			str = out.toString(StandardCharsets.UTF_8);
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return str;
	}

	/**
	 * 格式化XML文档,并解决中文问题
	 */
	public int doc2XMLFile(Document doc, String filename) {
		int returnValue = 0;
		try {
            /* 格式化输出,类型IE浏览一样 */
			OutputFormat format = OutputFormat.createPrettyPrint();

            /* 指定XML编码 */
			format.setEncoding("UTF-8");

			XMLWriter writer = new XMLWriter(
					new FileWriter(filename), format);
			writer.write(doc);
			writer.close();

            /* 执行成功,需返回1 */
			returnValue = 1;
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
		return returnValue;
	}
	
	public String textMsg(String fromId,String toId,String content){
		try{
			String text = "<xml></xml>";
			Document document = DocumentHelper.parseText(text);
			Element rootElement = document.getRootElement();
			//接收用户
			Element toUserName = rootElement.addElement("ToUserName");
			toUserName.setText(toId);
			//发送用户
			Element fromUserName = rootElement.addElement("FromUserName");
			fromUserName.setText(fromId);
			//发送时间
			Element createTime = rootElement.addElement("CreateTime");
			createTime.setText(String.valueOf(new Date().getTime()));
			//消息类型
			Element msgType = rootElement.addElement("MsgType");
			msgType.setText("text");
			//消息内容
			Element msg = rootElement.addElement("Content");
			msg.setText(content);
			
			return XmlUtil.getInstance().doc2String(document);
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return null;
	}
}