package com.matridx.las.frame.connect.channel.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.connect.dll.printDll.service.dao.entities.BasicElement;
import com.matridx.las.frame.connect.dll.printDll.service.dao.entities.DataConllection;
import com.matridx.las.frame.connect.dll.printDll.service.dao.entities.DataProject;
import com.matridx.las.frame.connect.dll.printDll.service.dao.entities.LabelContentParam;
import com.matridx.las.frame.connect.dll.printDll.service.dao.entities.LabelWidthOffset;
import com.matridx.las.frame.connect.dll.printDll.service.dao.entities.QrContentParam2D;
import com.matridx.las.frame.connect.dll.printDll.service.dao.entities.QrContentParamBar;
import com.matridx.las.frame.connect.dll.printDll.service.svcinterface.IPrintLibrary;
import com.matridx.las.frame.connect.util.ConnectUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.xml.XmlUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class PrintService extends HttpService {
	private final Logger log = LoggerFactory.getLogger(PrintService.class);
	private static int MaxShowLength;//最大字符长度
	private static int printTypeEdit;//打印机LabelWidthOffset下标

	private static DataConllection initDataCollection;
	@Override
	public Object getChannel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean init(Map<String, Object> map) {
		log.error("PrintService-init:{}",JSON.toJSONString(map));
		if(StringUtil.isNotObjectBank(map.get("MaxShowLength"))){
			MaxShowLength=Integer.parseInt(String.valueOf(map.get("MaxShowLength")));
		}
		if(StringUtil.isNotObjectBank(map.get("printTypeEdit"))){
			printTypeEdit=Integer.parseInt(String.valueOf(map.get("printTypeEdit")));
		}
		if(StringUtil.isNotObjectBank(map.get("initjson"))){
			initDataCollection = JSONObject.parseObject(map.get("initjson").toString(),DataConllection.class);
		}
		String ztqrdz = String.valueOf(map.get("ztqrdz"));
		if(StringUtil.isNotObjectBank(map.get("confirmflg"))&&Boolean.parseBoolean(String.valueOf(map.get("confirmflg")))){

			if(ztqrdz.equals("null")||StringUtil.isBlank(ztqrdz)){
				return super.init(map);
			}else{
				return ConnectUtil.confirmConnect(ztqrdz);
			}

		}

		return true;
    }

	@Override
	public boolean sendMessage(FrameModel frameModel,String info) {
		if(StringUtil.isBlank(info)){
			return false;
		}
		//参数转Map
		Type type = new TypeToken<Map<String,String>>() {}.getType();
		Map<String,String> infoMap = JSON.parseObject(frameModel.getMsgInfo(), type);
		DataProject dataProject = new DataProject();
		if ("1".equals(infoMap.get("flg"))) {
			//条码
			dataProject = initPrintByJson("PRINT_MACHINE");

		} else if ("2".equals(infoMap.get("flg"))) {
			//二维码
			dataProject = initPrintByJson("PRINT_XGQY");
		}
		//获取一行最多打印多少个标签(若未设置，默认为一个)
		int columnNum = Integer.parseInt(StringUtil.isNotBlank(dataProject.getCodePrintColumn().getValue())?dataProject.getCodePrintColumn().getValue():"1");
		//根据宽度和打印次数计算X轴间隔，整个打印宽度/打印次数，结果取整数
		int num = StringUtil.isNotBlank(infoMap.get("num")) ? Integer.valueOf(infoMap.get("num")) : 1;
		int times = 1;
		if (num % columnNum == 0) {
			times = num / columnNum;
		} else {
			if (num < columnNum) {
				times = 0;
			} else {
				times = (num / columnNum) + 1;
			}
		}
		if (times == 0) {
			CreatePrint(num, infoMap, dataProject);
		} else {
			for (int i = 0; i < times; i++) {
				CreatePrint(columnNum, infoMap,dataProject);
			}
		}
		//执行打印操作
		int printLabelResult = IPrintLibrary.PRINT_SERVICE.PTK_PrintLabel(1,1);
		if(printLabelResult !=0){
			log.error("执行打印错误，错误码："+ printLabelResult);
			//return false;
		}
		return super.recvBackCmdByModel(frameModel);
	}

	/**
	 * 打印机位置设置
	 * @param columnNum
	 * @param infoMap
	 * @param dataProject
	 */
	public boolean CreatePrint(int columnNum, Map<String,String> infoMap, DataProject dataProject) {
		if (columnNum == 0){
			return false;
		}
		//设置条码内容
		String barCode = infoMap.get("code");
		if (StringUtil.isNotBlank(infoMap.get("qrcode"))) {
			barCode = infoMap.get("qrcode");
		}
		String project = infoMap.get("project");
		String word = infoMap.get("word");
		String widthOffsets = dataProject.getLabelWidthOffsets().get(printTypeEdit).getWidthOffset().getValue();
		if ("1".equals(infoMap.get("flg"))) {
			//条形码
			for (int i = 0; i < columnNum; i++) {
				int widthOffset = 0;
				if (columnNum > 0){
					String[] split = widthOffsets.split(",");
					if (i>=1 && split.length>=i){
						widthOffset = Integer.parseInt(widthOffsets.split(",")[i-1]);
					}
				}
				for (LabelContentParam labelContentParam : dataProject.getLabelContentParams()) {
					if ("tmh".equals(labelContentParam.getNumber().getValue())){
						//条码底部
						if(infoMap.containsKey("code")){
							PrintText("code_"+columnNum+"_"+i,infoMap.get("code"),".",widthOffset,labelContentParam);

						}
					} else if ("nbbh".equals(labelContentParam.getNumber().getValue())){
						//第一行 project
						if(infoMap.containsKey("project")){
							PrintText("project_"+columnNum+"_"+i,project,"..",widthOffset,labelContentParam);
						}
					} else if ("wkh".equals(labelContentParam.getNumber().getValue())){
						//第二行
						if(infoMap.containsKey("word")){
							PrintText("word_"+columnNum+"_"+i,word,"..",widthOffset,labelContentParam);
						}
					}
				}
				for (QrContentParamBar qrContentParamBar : dataProject.getQrContentParamBars()) {
					if ("nbbh".equals(qrContentParamBar.getNumber().getValue())){
						//条码底部
						PrintBarCode(barCode,widthOffset,qrContentParamBar);
					}
				}
			}
		} else if ("2".equals(infoMap.get("flg"))) {
			//二维码
			for (int i = 0; i < columnNum; i++) {
				int widthOffset = 0;
				if (columnNum > 0){
					String[] split = widthOffsets.split(",");
					if (split.length > columnNum){
						widthOffset = Integer.parseInt(widthOffsets.split(",")[columnNum-1]);
					}
				}
				for (LabelContentParam labelContentParam : dataProject.getLabelContentParams()) {
					if ("ybbh".equals(labelContentParam.getNumber().getValue())){
						//第一行
						if(infoMap.containsKey("project")){
							PrintText("project",project,"..",widthOffset,labelContentParam);
						}
					} else if ("syh".equals(labelContentParam.getNumber().getValue())){
						//第二行 project
						if(infoMap.containsKey("word")){
							PrintText("word",word,"..",widthOffset,labelContentParam);
						}
					} else if ("nbbh".equals(labelContentParam.getNumber().getValue())){
						//第三行
						if(infoMap.containsKey("code")){
							PrintText("code",infoMap.get("code"),"..",widthOffset,labelContentParam);
						}
					}
				}
				for (QrContentParam2D qrContentParam2D : dataProject.getQrContentParam2Ds()) {
					if ("qr".equals(qrContentParam2D.getNumber().getValue())){
						//条码底部
						PrintQrCode(barCode,widthOffset,qrContentParam2D);
					}
				}
			}
		}
		return true;
	}

	/**
	 * 字体设置
	 * @param key 字体关键词
	 * @param value 字体文字
	 * @param replaceText 超过长度替换符
	 * @param widthOffset 字体位移量
	 * @param labelContentParam
	 */
	private void PrintText(String key, String value, String replaceText, int widthOffset, LabelContentParam labelContentParam){
		String trueValue = value.length()>MaxShowLength?value.substring(0,MaxShowLength)+replaceText:value;
		int drawTextTrueResult = IPrintLibrary.PRINT_SERVICE.PTK_DrawTextTrueTypeW(
				Integer.parseInt(labelContentParam.getFontXPosEdit().getValue()) + widthOffset,//x轴位置
				Integer.parseInt(labelContentParam.getFontYPosEdit().getValue()),//y轴位置
				Integer.parseInt(labelContentParam.getFontHeight().getValue()),//字体高度
				Integer.parseInt(labelContentParam.getFontWidth().getValue()),//字体宽度
				labelContentParam.getFontTypeCombox().getValue(),//字型名称
				Integer.parseInt(labelContentParam.getFontComboBoxRotate().getValue()),//字体旋转角度
				700,//字体粗细
				false,//斜体：0false,1true
				false,//下划线：0false,1true
				false,//删除线：0false,1true
				key,
				trueValue);
		if(drawTextTrueResult != 0){
			log.error("打印设置错误，错误码："+ drawTextTrueResult);
		}
	}
	/**
	 *  条码设置
	 * @param value 条码内容
	 * @param widthOffset 条码内容位移量
	 * @param qrContentParamBar
	 */
	private void PrintBarCode(String value, int widthOffset, QrContentParamBar qrContentParamBar){
		int drawBarCodeTrueResult = IPrintLibrary.PRINT_SERVICE.PTK_DrawBarcodeEx(
                Integer.parseInt(qrContentParamBar.getQrBarCodeXPosEdit().getValue()) + widthOffset,
                Integer.parseInt(qrContentParamBar.getQrBarCodeYPosEdit().getValue()),
				Integer.parseInt(qrContentParamBar.getQrBarComboxRotate().getValue()),
				qrContentParamBar.getQrBarCodeType().getValue(),
                Integer.parseInt(qrContentParamBar.getQrBarCodeNarrowWidth().getValue()),
                Integer.parseInt(qrContentParamBar.getQrBarCodePHorizontal().getValue()),
                Integer.parseInt(qrContentParamBar.getQrBarCodePVertical().getValue()),
				qrContentParamBar.getQrBarCodePtext().getValue().charAt(0),
				value,
				false);
		if(drawBarCodeTrueResult != 0){
			log.error("打印设置错误，错误码："+ drawBarCodeTrueResult);
		}
	}
	/**
	 *  条码设置
	 * @param value 条码内容
	 * @param widthOffset 条码内容位移量
	 * @param qrContentParam2D
	 */
	private void PrintQrCode(String value, int widthOffset, QrContentParam2D qrContentParam2D){
		int drawBarResult = IPrintLibrary.PRINT_SERVICE.PTK_DrawBar2D_QR(
				Integer.parseInt(qrContentParam2D.getQrCodeXPosEdit().getValue()) + widthOffset,
				Integer.parseInt(qrContentParam2D.getQrCodeYPosEdit().getValue()),
				Integer.parseInt(qrContentParam2D.getQrCodeMaxWidthEdit().getValue()),
				Integer.parseInt(qrContentParam2D.getQrCodeMaxHeightEdit().getValue()),
				Integer.parseInt(qrContentParam2D.getQrComboxRotate().getValue()),
				Integer.parseInt(qrContentParam2D.getQrCodeEnlargeEdit().getValue()),
				4,0,8,
				value);
		if(drawBarResult != 0){
			log.error("二维码信息设置错误，错误码："+ drawBarResult);
			//return false;
		}
	}
	/**
	 * 根据配置文件初始化打印机配置,并返回文字和条码\二维码配置
	 * @param DataNumber
	 */
	public DataProject initPrintByJson(String DataNumber){
		initPrintBase();
		List<DataProject> dataProjects = initDataCollection.getDataProjects().stream().filter(dataProject -> DataNumber.equals(dataProject.getDataNumber().getValue())).collect(Collectors.toList());
		DataProject dataProject =  dataProjects.get(0);
		int setLabelWidthResult = IPrintLibrary.PRINT_SERVICE.PTK_SetLabelWidth(Integer.parseInt(dataProject.getLabelWidthEdit().getValue()));      //设置标签的宽度
		if (setLabelWidthResult != 0) {
			log.error("设置标签的宽度错误，错误码："+setLabelWidthResult);
		}
		int setLabelHeight = IPrintLibrary.PRINT_SERVICE.PTK_SetLabelHeight(Integer.parseInt(dataProject.getLabelHeightEdit().getValue()), 16, 0, false);     //设置标签的高度和定位间隙\黑线\穿孔的高度
		if (setLabelHeight != 0) {
			log.error("设置标签的高度和定位间隙错误，错误码："+ setLabelHeight);
		}
		return dataProject;
	}
	/**
	 * 根据配置文件初始化打印机配置,并返回文字和条码\二维码配置
	 * @param DataNumber
	 * @param relativePath
	 */
	public DataProject initPrintByXml(String DataNumber, String relativePath){
		List<Map<String, Object>> list = new ArrayList<>();
		initPrintBase();
		DataConllection dataConllection = readXmlToDataConllectionList(relativePath);
		List<DataProject> dataProjects = dataConllection.getDataProjects().stream().filter(dataProject -> DataNumber.equals(dataProject.getDataNumber().getValue())).collect(Collectors.toList());
		DataProject dataProject =  dataProjects.get(0);
		int setLabelWidthResult = IPrintLibrary.PRINT_SERVICE.PTK_SetLabelWidth(Integer.parseInt(dataProject.getLabelWidthEdit().getValue()));      //设置标签的宽度
		if (setLabelWidthResult != 0) {
			log.error("设置标签的宽度错误，错误码："+setLabelWidthResult);
		}
		int setLabelHeight = IPrintLibrary.PRINT_SERVICE.PTK_SetLabelHeight(Integer.parseInt(dataProject.getLabelHeightEdit().getValue()), 16, 0, false);     //设置标签的高度和定位间隙\黑线\穿孔的高度
		if (setLabelHeight != 0) {
			log.error("设置标签的高度和定位间隙错误，错误码："+ setLabelHeight);
		}
		return dataProject;
	}

	/**
	 * 初始化打印机(通用设置)
	 */
	private void initPrintBase(){
		//初始化打印机设置
		int openPortResult = IPrintLibrary.PRINT_SERVICE.OpenPort(255);
		if (openPortResult != 0) {
			log.error("设置端口号错误，错误码："+ openPortResult);
		}
		int comPortResult = IPrintLibrary.PRINT_SERVICE.SetPCComPort(1,  true);
		if (comPortResult != 0)
		{
			log.error("设置波特率错误，错误码："+comPortResult);
		}
		int clearBufferResult = IPrintLibrary.PRINT_SERVICE.PTK_ClearBuffer();           //清空缓冲区
		if (clearBufferResult != 0) {
			log.error("清空缓冲区错误，错误码："+ clearBufferResult);
		}
		int setPrintSpeedResult = IPrintLibrary.PRINT_SERVICE.PTK_SetPrintSpeed(4);        //设置打印速度
		if (setPrintSpeedResult != 0) {
			log.error("设置打印速度错误，错误码："+ setPrintSpeedResult);
		}
		int setDarknessResult = IPrintLibrary.PRINT_SERVICE.PTK_SetDarkness(10);         //设置打印黑度
		if (setDarknessResult != 0) {
			log.error("设置打印黑度错误，错误码："+ setDarknessResult);
		}
		int setDirectionResult = IPrintLibrary.PRINT_SERVICE.PTK_SetDirection('T');      //设置标签打印方向
		if (setDirectionResult != 0) {
			log.error("设置标签打印方向："+ setDirectionResult);
		}
	}

	/**
	 * 读取xml配置文件并解析
	 * @param relativePath
	 * @return
	 */
	public DataConllection readXmlToDataConllectionList(String relativePath){
		DataConllection dataConllection = new DataConllection();
		try {
			Document doc = XmlUtil.getInstance().read( Thread.currentThread().getContextClassLoader().getResourceAsStream(relativePath));
			Element root = doc.getRootElement();
			dealElementsObject(root.elements(),dataConllection);
		} catch (DocumentException e) {
			log.error(relativePath+"文件读取失败！",e);
		}
		return dataConllection;
	}

	/**
	 * 解析Elements
	 * @param elements
	 * @param object
	 * @return
	 */
	public void dealElementsObject(List<Element> elements,Object object) {
		List<String> keyList = new ArrayList<>();
		keyList.add("DataProject");
		keyList.add("LabelWidthOffsets");
		keyList.add("LabelContentParams");
		keyList.add("QrContentParam2Ds");
		keyList.add("QrContentParamBars");
		for (Element element : elements) {
			String elementName = element.getName();
			try {
				String finalElementName = elementName;
				if (keyList.stream().anyMatch(item -> item.equals(finalElementName))){
					if ("DataProject".equals(finalElementName)) {
						elementName = elementName + "s";
					}
					Method getMethod = object.getClass().getMethod("get" + elementName.substring(0, 1).toUpperCase() + elementName.substring(1));
					Object invoke = getMethod.invoke(object);
					List<Object> childObjects = new ArrayList<>();
					if (invoke!=null){
						childObjects = (List<Object>) invoke;
					}
					if ("DataProject".equals(finalElementName)) {
						DataProject childObject = new DataProject();
						if (element.attribute("Name") != null){
							Method setMethod = childObject.getClass().getMethod("setName",String.class);
							setMethod.invoke(childObject, element.attribute("Name").getStringValue());
						}
						dealElementsObject(element.elements(),childObject);
						childObjects.add(childObject);
					} else{
						if (element.elements()!= null && element.elements().size()>0) {
							for (Element childElement : element.elements()) {
								Object childObject = null;
								if ("LabelWidthOffsets".equals(elementName)) {
									childObject = new LabelWidthOffset();
								} else if ("LabelContentParams".equals(elementName)) {
									childObject = new LabelContentParam();
								} else if ("QrContentParam2Ds".equals(elementName)) {
									childObject = new QrContentParam2D();
								} else if ("QrContentParamBars".equals(elementName)) {
									childObject = new QrContentParamBar();
								}
								dealElementsObject(childElement.elements(),childObject);
								childObjects.add(childObject);
							}
						}
					}
					Method setMethod = object.getClass().getMethod("set" + elementName.substring(0, 1).toUpperCase() + elementName.substring(1),List.class);
					setMethod.invoke(object, childObjects);
				} else {
					BasicElement basicElement = new BasicElement();
					if (element.attribute("Name") != null){
						basicElement.setName(element.attribute("Name").getStringValue());
					}
					basicElement.setValue(element.getTextTrim());
					Method method = object.getClass().getMethod("set" + elementName.substring(0, 1).toUpperCase() + elementName.substring(1), BasicElement.class);
					method.invoke(object, basicElement);
				}
			} catch (NoSuchMethodException e) {
				log.error("找不到属性" + object.getClass() + "." + elementName, e);
			} catch (InvocationTargetException e) {
				log.error("属性" + object.getClass() + "." + elementName + "赋值失败", e);
			} catch (IllegalAccessException e) {
				log.error("属性" + object.getClass() + "." + elementName + "赋值失败", e);
			}
		}
	}

}
