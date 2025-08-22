package com.matridx.igams.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.matridx.springboot.util.base.StringUtil;


public class ToolUtil {
	/**
	 * 筛选指定机构的审批用户信息，暂时采用CommonService的siftJgList进行代替
	 * @param preList
	 * @param jgid
	 * @return
	 */
	/*public static List<SpgwcyDto> preparList(List<SpgwcyDto> preList,String jgid){
		if(StringUtil.isBlank(jgid)) {
			return preList;
		}
		List<SpgwcyDto> resultList = new ArrayList<>();
		if(preList != null && preList.size() > 0){
			for(int i =0;i<preList.size();i++) {
				if("1".equals(preList.get(i).getDwxz())) {
					if(jgid.equals(preList.get(i).getJgid())) {
						resultList.add(preList.get(i));
					}
				}else {
					resultList.add(preList.get(i));
				}
			}
		}
		
		return resultList;
	}*/
	

	/**
	 * 处理图片识别信息，分行
	 * @param words
	 * @return
	 */
	public static List<List<Map<String, String>>> pareString(JSONArray words){
		if(words == null)
			return null;
		List<List<Map<String, String>>> resultList = new ArrayList<>();
		for(int i=0;i<words.size();i++) {
			String word = words.getJSONObject(i).getString("words");
			JSONObject locations = JSONObject.parseObject(words.getJSONObject(i).getString("location"));
			Map<String, String> wordMap = new HashMap<>();
			wordMap.put("words", word);
			wordMap.put("top", locations.getString("top"));
			wordMap.put("left", locations.getString("left"));
			wordMap.put("width", locations.getString("width"));
			wordMap.put("height", locations.getString("height"));
			dealResult(resultList,wordMap);
		}
		return resultList;
	}
	
	/**
	 * 根据现有信息，处理文字应属于哪一行
	 * @param resultList
	 * @param wordMap
	 */
	private static BigDecimal dealResult(List<List<Map<String, String>>> resultList,Map<String, String> wordMap) {
		boolean isFind = false;
		boolean isCalSlope = false;
		BigDecimal slope = new BigDecimal("0");
		List<BigDecimal> slopes = new ArrayList<>();
		//判断list里每个元素有无存在两条以上的
        for (List<Map<String, String>> maps : resultList) {
            if (maps.size() > 1) {

                BigDecimal t_top = new BigDecimal(maps.get(0).get("top"));
                BigDecimal t_left = new BigDecimal(maps.get(0).get("left"));
                BigDecimal top = new BigDecimal(maps.get(1).get("top"));
                BigDecimal left = new BigDecimal(maps.get(1).get("left"));

                BigDecimal sub_left = left.subtract(t_left);

                if (sub_left.compareTo(new BigDecimal("0")) == 0)
                    continue;

                isCalSlope = true;
                BigDecimal t_slope = top.subtract(t_top).divide(left.subtract(t_left), RoundingMode.FLOOR);
                slope.add(t_slope);
                slopes.add(t_slope);
            }
        }
		
		if(isCalSlope)
			slope = slope.divide(new BigDecimal(slopes.size()), RoundingMode.FLOOR);

		BigDecimal top = new BigDecimal(wordMap.get("top"));
		BigDecimal height = new BigDecimal(wordMap.get("height"));
		BigDecimal left = new BigDecimal(wordMap.get("left"));
		BigDecimal bottom = top.add(height);
		
		//没有斜率，则计算第一个个
		if(!isCalSlope) {
            for (List<Map<String, String>> rowList : resultList) {
                //如果该行里没有数据，则直接另起一行
                if (rowList==null||rowList.isEmpty())
                    break;
                Map<String, String> t_wordMap = rowList.get(0);
                BigDecimal t_top = new BigDecimal(t_wordMap.get("top"));
                BigDecimal t_height = new BigDecimal(t_wordMap.get("height"));
                BigDecimal t_left = new BigDecimal(t_wordMap.get("left"));
                BigDecimal t_width = new BigDecimal(t_wordMap.get("width"));
                BigDecimal t_bottom = t_top.add(t_height);

                if (top.compareTo(t_bottom) <= 0 && bottom.compareTo(t_top) >= 0 && t_left.add(t_width).compareTo(left) <= 0) {
                    isFind = true;
                    if (t_left.compareTo(left) <= 0)
                        rowList.add(wordMap);
                    else {
                        rowList.add(0, wordMap);
                    }
                }
            }
		}else {
            for (List<Map<String, String>> rowList : resultList) {
                //如果该行里没有数据，则直接另起一行
                if ( rowList==null||rowList.isEmpty())
                    break;
                Map<String, String> t_wordMap = rowList.get(0);
                //循环已有标本计算截距，同时计算在标本区应该有的高度  b= y-kx
                BigDecimal intercept = new BigDecimal(t_wordMap.get("top")).subtract(new BigDecimal(t_wordMap.get("left")).multiply(slope));

                //计算每一行在现文件区域应该所处的
                BigDecimal x_top = slope.multiply(new BigDecimal(wordMap.get("left"))).add(intercept);
                BigDecimal t_height = new BigDecimal(t_wordMap.get("height"));

                if (new BigDecimal(wordMap.get("top")).compareTo(x_top.add(t_height)) <= 0 &&
                        new BigDecimal(wordMap.get("top")).add(t_height).compareTo(x_top) >= 0) {
                    isFind = true;

                    for (int y = rowList.size() - 1; y >= 0; y--) {
                        if (new BigDecimal(rowList.get(y).get("left")).compareTo(new BigDecimal(wordMap.get("left"))) <= 0) {
                            rowList.add(y + 1, wordMap);
                            break;
                        }
                    }
                    break;
                }
            }
		}
		
		//如果没有找到，则另起一行
		if(!isFind) {
			List<Map<String, String>> resList = new ArrayList<>();
			resList.add(wordMap);
			resultList.add(resList);
		}
		
		return slope;
	}
	
	/**
	 * 根据现有文字信息确定相应的左右上下信息
	 * @param resultList
	 * @param wordMap
	 * @return
	 */
	public static Map<String, String> getAroundWord(List<List<Map<String, String>>> resultList,Map<String, String> wordMap){
		
		Map<String, String> result = new HashMap<>();
		
		BigDecimal top = new BigDecimal(wordMap.get("top"));
		BigDecimal height = new BigDecimal(wordMap.get("height"));
		BigDecimal left = new BigDecimal(wordMap.get("left"));
		BigDecimal width = new BigDecimal(wordMap.get("width"));
		BigDecimal bottom = top.add(height);
		BigDecimal right = left.add(width);
		String word = wordMap.get("words");
		
		boolean isFind = false;
		//判断list里
		for(int y = 0 ;y < resultList.size();y++) {
			
			List<Map<String, String>> rowList = resultList.get(y);
			
			for(int x = 0 ;x < rowList.size();x++) {
				Map<String, String> t_wordMap = rowList.get(x);
				BigDecimal t_top = new BigDecimal(t_wordMap.get("top"));
				
				if(t_top.compareTo(top) > 200 || t_top.compareTo(top) < -200)
					break;
				
				BigDecimal t_height = new BigDecimal(t_wordMap.get("height"));
				BigDecimal t_left = new BigDecimal(t_wordMap.get("left"));
				BigDecimal t_width = new BigDecimal(t_wordMap.get("width"));
				BigDecimal t_bottom = t_top.add(t_height);
				BigDecimal t_right = t_left.add(t_width);
				
				if(t_top.compareTo(bottom) <= 0 && t_bottom.compareTo(top) >=0 && t_left.compareTo(right) <=0 && t_right.compareTo(left) >=0) {
					String t_word = t_wordMap.get("words");
					result.put("words", word);
					//包含该文字
					if(t_word.contains(word)) {
						//完全想等，则取后面的信息
						if(t_word.equals(word)) {
							result.put("wordflg", "equals");
							//左边关键字
							if( x > 1) {
								result.put("leftword", rowList.get(x-2).get("words").replace(":", ""));
								result.put("leftpoint", "2");
							}else if( x == 1){
								result.put("leftword", rowList.get(0).get("words").replace(":", ""));
								result.put("leftpoint", "1");
							}
							//右边关键字
							if( (x + 2) < rowList.size()) {
								result.put("rightword", rowList.get(x+2).get("words").replace(":", ""));
								result.put("rightpoint", "2");
							}else if( (x + 1) < rowList.size()){
								result.put("rightword", rowList.get(x+1).get("words").replace(":", ""));
								result.put("rightpoint", "1");
							}
							//上边关键字
							if( (y - 2) >= 0) {
								List<Map<String, String>> t_rowList = resultList.get(y-2);
								if(t_rowList.size() >= x+1) {
									result.put("topword", resultList.get(y-2).get(x).get("words").replace(":", ""));
									result.put("toppoint", "2");
								}
							}else if((y - 1) >= 0){
								List<Map<String, String>> t_rowList = resultList.get(0);
								if(t_rowList.size() >= x+1) {
									result.put("topword", resultList.get(y-12).get(x).get("words")).replace(":", "");
									result.put("toppoint", "1");
								}
							}
							//下边关键字
							if( (y + 2) < resultList.size()) {
								List<Map<String, String>> t_rowList = resultList.get(y+2);
								if(t_rowList.size() >= x+1) {
									result.put("bottomword", resultList.get(y+2).get(x).get("words").replace(":", ""));
									result.put("bottompoint", "2");
								}
							}else if((y + 1) < resultList.size()){
								List<Map<String, String>> t_rowList = resultList.get(y+1);
								if(t_rowList.size() >= x+1) {
									result.put("bottomword", resultList.get(y+1).get(x).get("words").replace(":", ""));
									result.put("bottompoint", "1");
								}
							}
						}else {
							//部分相等，则进行截取，开始部分符合，则截取后一部分为输入信息
							if(t_word.contains(word)) {
								result.put("wordflg", "contains");
								//左边关键字
								if( x >= 1) {
									String tmpString = rowList.get(x-1).get("words");
									result.put("leftword", tmpString.length() > 2? tmpString.substring(0,2):tmpString.replace(":", ""));
									result.put("leftpoint", "1");
								}
								//右边关键字
								if( (x + 1) < rowList.size()) {
									String tmpString = rowList.get(x+1).get("words");
									result.put("rightword", tmpString.length() > 2? tmpString.substring(0,2):tmpString.replace(":", ""));
									result.put("rightpoint", "1");
								}
								//上边关键字
								if( (y - 1) >= 0) {
									List<Map<String, String>> t_rowList = resultList.get(y-1);
									if(t_rowList.size() >= (x+1)) {
										String tmpString = t_rowList.get(x).get("words");
										result.put("topword", tmpString.length() > 2? tmpString.substring(0,2):tmpString.replace(":", ""));
										result.put("toppoint", "1");
									}
								}
								//下边关键字
								if( (y + 1) < resultList.size()) {
									List<Map<String, String>> t_rowList = resultList.get(y + 1);
									if(t_rowList.size() >= x+1) {
										String tmpString = t_rowList.get(x).get("words");
										result.put("bottomword", tmpString.length() > 2? tmpString.substring(0,2):tmpString.replace(":", ""));
										result.put("bottompoint", "1");
									}
								}
							}
						}
						
						isFind = true;
						//已经找到
						break;
					}
				}
			}
			if(isFind)
				break;
		}
		
		return result;
	}
	
	/**
	 * 文字预测，相应信息抽取方法
	 * @param resultList 图片识别后的文字信息
	 * @param keyMap 关键词信息
	 * @return
	 */
	public static Map<String, String> prediction(List<List<Map<String, String>>> resultList,Map<String,Map<String, String>> keyMap){
		Map<String, String> result = new HashMap<>();
		
		for(String key:keyMap.keySet()){
			Map<String, String> wordMap = keyMap.get(key);
			String word = wordMap.get("words");
			String wordflg = wordMap.get("wordflg");
			String leftword = wordMap.get("leftword");
			String leftpoint = wordMap.get("leftpoint");
			String rightword = wordMap.get("rightword");
			String rightpoint = wordMap.get("rightpoint");
			String topword = wordMap.get("topword");
			String toppoint = wordMap.get("toppoint");
			String bottomword = wordMap.get("bottomword");
			String bottompoint = wordMap.get("bottompoint");
			String sameflg = wordMap.get("sameflg");
			
			//循环list里已经找到的
			for(int y = 0 ;y < resultList.size();y++) {
				List<Map<String, String>> rowList = resultList.get(y);
				
				boolean isFind = false;
				for(int x = 0 ;x < rowList.size();x++) {
					Map<String, String> t_wordMap = rowList.get(x);
					String t_word = t_wordMap.get("words");
					//如果内部不包含相应关键字，则直接查找下一个信息
					if(StringUtil.isBlank(word) || !t_word.contains(word)) {
						continue;
					}
					//简单计算置信度
					int confidence = 0;
					//计算左边置信情况
					if(StringUtil.isNotBlank(leftword)&&StringUtil.isNotBlank(leftpoint)) {
						//横坐标大于计算位置
						if( x >= Integer.parseInt(leftpoint)) {
							String leftwordinfo = rowList.get(x-Integer.parseInt(leftpoint)).get("words");
							if(leftwordinfo!=null && leftwordinfo.contains(leftword)) {
								confidence ++;
							}
						}
					}else {
						confidence ++;
					}
					//计算右边置信情况
					if(StringUtil.isNotBlank(rightword)&&StringUtil.isNotBlank(rightpoint)) {
						//横坐标小于该行最大值
						if( x+Integer.parseInt(rightpoint) < rowList.size()) {
							String rightwordinfo = rowList.get(x + Integer.parseInt(rightpoint)).get("words");
							if(rightwordinfo!=null && rightwordinfo.contains(rightword)) {
								confidence ++;
							}
						}
					}else {
						confidence ++;
					}
					//计算上边置信情况
					if(StringUtil.isNotBlank(topword)&&StringUtil.isNotBlank(toppoint)) {
						//纵坐标大于计算位置
						if( y >= Integer.parseInt(toppoint) && resultList.get(y - Integer.parseInt(toppoint)).size() >= x) {
							String topwordinfo = resultList.get(y - Integer.parseInt(toppoint)).get(x).get("words");
							if(topwordinfo!=null && topwordinfo.contains(topword)) {
								confidence ++;
							}
						}
					}else {
						confidence ++;
					}
					//计算下边置信情况
					if(StringUtil.isNotBlank(bottomword)&&StringUtil.isNotBlank(bottompoint)) {
						//纵坐标大于计算位置
						if( y + Integer.parseInt(bottompoint) < resultList.size() && resultList.get(y + Integer.parseInt(bottompoint)).size() > x ) {
							String bottomwordinfo = resultList.get(y + Integer.parseInt(bottompoint)).get(x).get("words");
							if(bottomwordinfo!=null && bottomwordinfo.contains(bottomword)) {
								confidence ++;
							}
						}
					}else {
						confidence ++;
					}
					//置信情况大于等于3认为可信任
					if(confidence >= 3) {
						isFind = true;
						
						//如果为相等操作
						if("equals".equals(wordflg)) {
							if("1".equals(sameflg)) {
								String wordInfo = rowList.get(x+1).get("words").substring(word.length()).replace(":", "");
								result.put(key, wordInfo);
							}else {
								if(x < resultList.get(y+1).size()){
									String wordInfo = resultList.get(y+1).get(x).get("words").replace(":", "");
									result.put(key, wordInfo);
								}
							}
						}else if("contains".equals(wordflg)) {
							//如果为包含操作
							String wordInfo = t_word.substring(word.length()).replace(":", "");
							result.put(key, wordInfo);
						}else {
							//如果为包含操作
							int index = t_word.indexOf(word);
							if("0".equals(rightpoint)) {
								int endpoint = t_word.indexOf(rightword);
								int startpoint = index+word.length();
								String wordInfo = t_word.substring(startpoint,endpoint).replace(":", "");
								result.put(key, wordInfo);
							}else if("0".equals(sameflg)){
								List<Map<String, String>> y_rowList = resultList.get(y + 1);
								if(y_rowList.size() > x) {
									Map<String, String> y_wordMap = y_rowList.get(x);
									String y_word = y_wordMap.get("words").replace(":", "");
									result.put(key, y_word);
								}
							}else {
								String wordInfo = t_word.substring(index+word.length()).replace(":", "");
								result.put(key, wordInfo);
							}
						}
						break;
					}
				}
				
				if(isFind)
					break;
			}
		}
		
		return result;
	}
	
	/**
	 * 保存时根据结果重新进行学习
	 * @param resultList 辨认后的文字信息
	 * @param keyMap 关键词信息
	 * @param infoMap 更正后的信息
	 * @param recoderList 学习记录
	 * @return
	 */
	public static Map<String,Map<String, String>> study(List<List<Map<String, String>>> resultList,Map<String,Map<String, String>> keyMap,Map<String,String> infoMap,List<Map<String,String>> recoderList){
		Map<String,Map<String, String>> result = new HashMap<>();
		//循环获取每一个
		for(String key:infoMap.keySet()){
			
			String inforword = infoMap.get(key);
			Map<String, String> keyresult = new HashMap<>();
			//存放包含相同内容的备选项列表，如果标题相同，则直接采用标题，如果标题不同，那么把相应信息放到该列表中，到最后如果还未找到相同的标题，则从该列表里获取第一个信息
			List<Map<String, String>> sameList = new ArrayList<>();
			//循环文字识别结果，针对实际输入项进行调整关键字
			for(int y = 0 ;y < resultList.size();y++) {
				List<Map<String, String>> rowList = resultList.get(y);
				//标题是否找到标记
				boolean isFind = false;
				for(int x = 0 ;x < rowList.size();x++) {
					Map<String, String> t_wordMap = rowList.get(x);
					String t_word = t_wordMap.get("words");
					int index = t_word.indexOf(inforword);
					//如果内部不包含相应输入内容，则直接查找下一个信息
					if(index == -1) {
						continue;
					}
					
					Map<String, String> wordMap = keyMap.get(key);
					
					String word = wordMap ==null? null:wordMap.get("words");
					String wordflg = wordMap ==null? null:wordMap.get("wordflg");
					String leftword = wordMap ==null? null:wordMap.get("leftword");
					String leftpoint = wordMap ==null? null:wordMap.get("leftpoint");
					String rightword = wordMap ==null? null:wordMap.get("rightword");
					String rightpoint = wordMap ==null? null:wordMap.get("rightpoint");
					String topword = wordMap ==null? null:wordMap.get("topword");
					String toppoint = wordMap ==null? null:wordMap.get("toppoint");
					String bottomword = wordMap ==null? null:wordMap.get("bottomword");
					String bottompoint = wordMap ==null? null:wordMap.get("bottompoint");
					String sameflg = "1"; //键值对同一行标记
					
					//确认内容的标题位置，然后查找前后上下的内容
					int i_title_index = -1;
					int i_title_index_y = -1;
					//如果是起始点
					if(index == 0) {
						if(x!=0) {
							//确认标题
							Map<String, String> title_wordMap = rowList.get(x-1);
							
							if(title_wordMap != null)
							{
								String title_word = title_wordMap.get("words");
								//前一个内容里包含标题
								if(StringUtil.isBlank(word)) {
									Map<String, String> sameMap = new HashMap<>();
									sameMap.put("x", String.valueOf(x-1));
									sameMap.put("y", String.valueOf(y));
									sameMap.put("wordflg", "contains");
									sameMap.put("words", title_word);
									sameMap.put("sameflg", "1");
									sameList.add(sameMap);
								}else if(title_word.contains(word)) {
									i_title_index = x-1;
									i_title_index_y = y;
									wordflg = "equals";
									isFind = true;
								}
							}
						}
						if(!isFind && y >= 1) {
							//确认标题
							Map<String, String> title_wordMap = resultList.get(y-1).get(x);
							if(title_wordMap != null)
							{
								String title_word = title_wordMap.get("words");
								//前一个内容里包含标题
								if(StringUtil.isNotBlank(word) && title_word.contains(word)) {
									i_title_index = x;
									i_title_index_y = y-1;
									wordflg = "equals";
									sameflg = "0";
									isFind = true;
								}else {
									Map<String, String> sameMap = new HashMap<>();
									sameMap.put("x", String.valueOf(x));
									sameMap.put("y", String.valueOf(y-1));
									sameMap.put("wordflg", "contains");
									sameMap.put("words", title_word);
									sameMap.put("sameflg", "0");
									sameList.add(sameMap);
								}
							}
						}
						
					}else {
						//如果为中途开始,则取前一部分的信息
						String title_word = t_word.substring(0,index);
						//内容里包含标题
						if(StringUtil.isNotBlank(word) && title_word.contains(word)) {
							i_title_index = x;
							i_title_index_y = y;
							sameflg = "1";
							wordflg = "contains";
							isFind = true;
						}else if(StringUtil.isNotBlank(word) && y >0 && resultList.get(y-1).get(x).get("words").contains(word)) {
							//如果前面没有找到标题，则获取上面的记录
							i_title_index = x;
							i_title_index_y = y-1;
							sameflg = "0";
							wordflg = "contains";
							isFind = true;
						}else{
							//如果内容不包含标题，则作为备选项，放到备选项列表
							Map<String, String> sameMap = new HashMap<>();
							sameMap.put("x", String.valueOf(x));
							sameMap.put("y", String.valueOf(y));
							sameMap.put("wordflg", "contains");
							sameMap.put("sameflg", "1");
							sameMap.put("words", title_word);
							sameList.add(sameMap);
						}
					}
					
					//如果没有找到相应的标题，则认为原有信息有问题，则重新定义标记
					if( sameList!=null&&!sameList.isEmpty()) {
						isFind = true;
						Map<String, String> sameMap = sameList.get(0);
						//如果前面没有找到标题，则获取上面的记录
						i_title_index = Integer.parseInt(sameMap.get("x"));
						i_title_index_y = Integer.parseInt(sameMap.get("y"));
						sameflg = sameMap.get("sameflg");
						wordflg = sameMap.get("wordflg");
						word = sameMap.get("words");
					}
					
					keyresult.put("words", word);
					keyresult.put("wordflg", wordflg);
					keyresult.put("sameflg", sameflg);
					if(i_title_index == -1)
						continue;
					
					List<Map<String, String>> currentRow = resultList.get(i_title_index_y);
					//重新计算左边设置
					if(StringUtil.isNotBlank(leftpoint)) {
						//如果左边间隔小于2，并且标题和内容不再同一行，则缩小间隔
						if("2".equals(leftpoint) && i_title_index < Integer.parseInt(leftpoint)&& i_title_index_y != y) {
							leftpoint ="1";
						}
						//横坐标大于计算位置
						if( i_title_index >= Integer.parseInt(leftpoint)) {
							String leftwordinfo = currentRow.get(i_title_index-Integer.parseInt(leftpoint)).get("words");
							if(leftwordinfo!=null && !leftwordinfo.contains(leftword)) {
								//按照之前的坐标无法获取相应的数据，则需要更新坐标和内容
								//不在同一个地方 并且 前面还有内容
								if(i_title_index-2 >= 0 && i_title_index != x) {
									//因为不在同一个地方，前面有两个以上元素，那么表明标题是单独的，不跟内容在一起，所以无需截取
									keyresult.put("leftword", currentRow.get(i_title_index-2).get("words"));
									keyresult.put("leftpoint", "2");
								}else if(i_title_index-1 >= 0 && i_title_index == x){
									//不在同一个地方，前面没有两个元素，这种情况不考虑
									//在同一个地方，前面还有一个以上的元素，这个时候需要进行截取
									keyresult.put("leftword", currentRow.get(i_title_index-1).get("words").substring(0,2));
									keyresult.put("leftpoint", "1");
								}else {
									keyresult.put("leftword", null);
									keyresult.put("leftpoint", null);
								}
							}else {
								//如果跟原有一样，则保留信息
								keyresult.put("leftword", leftword);
								keyresult.put("leftpoint", leftpoint);
							}
						}
					}else {
						//不在同一个地方 并且 前面还有内容
						if(i_title_index-2 >= 0 && i_title_index != x) {
							//因为不在同一个地方，前面有两个以上元素，那么表明标题是单独的，不跟内容在一起，所以无需截取
							keyresult.put("leftword", currentRow.get(i_title_index-2).get("words"));
							keyresult.put("leftpoint", "2");
						}else if(i_title_index-1 >= 0 && i_title_index == x){
							//不在同一个地方，前面没有两个元素，这种情况不考虑
							//在同一个地方，前面还有一个以上的元素，这个时候需要进行截取
							keyresult.put("leftword", currentRow.get(i_title_index-1).get("words").substring(0,2));
							keyresult.put("leftpoint", "1");
						}else {
							keyresult.put("leftword", null);
							keyresult.put("leftpoint", null);
						}
					}
					//重新计算右边设置
					if(StringUtil.isNotBlank(rightpoint)) {
						//如果右边间隔小于2，并且标题和内容不再同一行，则缩小间隔
						if("2".equals(rightpoint) && i_title_index_y != y && i_title_index + Integer.parseInt(rightpoint) >= currentRow.size()) {
							rightpoint ="1";
						}
						//横坐标大于计算位置
						if( i_title_index + Integer.parseInt(rightpoint) < currentRow.size()) {
							String rightwordinfo = currentRow.get(i_title_index+Integer.parseInt(rightpoint)).get("words");
							if(rightwordinfo!=null && !rightwordinfo.contains(rightword)) {
								//按照之前的坐标无法获取相应的数据，则需要更新坐标和内容
								//不在同一个地方 并且 后面还有内容
								if(i_title_index + 2 < currentRow.size() && i_title_index != x) {
									//因为不在同一个地方，前面有两个以上元素，那么表明标题是单独的，不跟内容在一起，所以无需截取
									keyresult.put("rightword", currentRow.get(i_title_index+2).get("words"));
									keyresult.put("rightpoint", "2");
								}else if(i_title_index + 1 < currentRow.size() && i_title_index == x){
									//不在同一个地方，后面没有两个元素，这种情况不考虑
									//在同一个地方，后面还有一个以上的元素，这个时候需要进行截取
									keyresult.put("rightword", currentRow.get(i_title_index+1).get("words").substring(0,2));
									keyresult.put("rightpoint", "1");
								}else if(i_title_index == x && currentRow.get(i_title_index).get("words").substring(index).length() > (inforword.length() + 2)){
									//不在同一个地方，后面没有两个元素，这种情况不考虑
									//在同一个地方，后面还有一个以上的元素，这个时候需要进行截取
									keyresult.put("rightword", currentRow.get(i_title_index).get("words").substring(index + inforword.length()).substring(0,2));
									keyresult.put("rightpoint", "0");
								}else {
									keyresult.put("rightword", null);
									keyresult.put("rightpoint", null);
								}
							}else {
								//如果跟原有一样，则保留信息
								keyresult.put("rightword", rightword);
								keyresult.put("rightpoint", rightpoint);
							}
						}
					}else {
						//按照之前的坐标无法获取相应的数据，则需要更新坐标和内容
						//不在同一个地方 并且 后面还有内容
						if(i_title_index + 2 < currentRow.size() && i_title_index != x) {
							//因为不在同一个地方，前面有两个以上元素，那么表明标题是单独的，不跟内容在一起，所以无需截取
							keyresult.put("rightword", currentRow.get(i_title_index+2).get("words"));
							keyresult.put("rightpoint", "2");
						}else if(i_title_index + 1 < currentRow.size() && i_title_index == x){
							//不在同一个地方，后面没有两个元素，这种情况不考虑
							//在同一个地方，后面还有一个以上的元素，这个时候需要进行截取
							keyresult.put("rightword", currentRow.get(i_title_index+1).get("words").substring(0,2));
							keyresult.put("rightpoint", "1");
						}else if(i_title_index == x && currentRow.get(i_title_index).get("words").substring(index).length() > (inforword.length() + 2)){
							//不在同一个地方，后面没有两个元素，这种情况不考虑
							//在同一个地方，后面还有一个以上的元素，这个时候需要进行截取
							keyresult.put("rightword", currentRow.get(i_title_index).get("words").substring(index + inforword.length()).substring(0,2));
							keyresult.put("rightpoint", "0");
						}else{
							keyresult.put("rightword", null);
							keyresult.put("rightpoint", null);
						}
					}
					//重新计算上边设置
					if(StringUtil.isNotBlank(toppoint)) {
						//纵坐标大于计算位置
						if( i_title_index_y >= Integer.parseInt(toppoint) && i_title_index < resultList.get(i_title_index_y - Integer.parseInt(toppoint)).size()) {
							String topwordinfo = resultList.get(i_title_index_y - Integer.parseInt(toppoint)).get(i_title_index).get("words");
							if(topwordinfo!=null && !topwordinfo.contains(topword)) {
								//按照之前的纵，横坐标无法获取相应的数据，则需要更新坐标和内容
								//不在同一个地方 并且 后面还有内容
								if(i_title_index_y - 2  >= 0 && i_title_index < resultList.get(i_title_index_y - 2).size() &&  i_title_index_y != y) {
									//因为纵坐标不在同一个地方，上面有两个以上元素，那么表明标题是单独的，不跟内容在一起，所以无需截取
									keyresult.put("topword", resultList.get(i_title_index_y - 2).get(i_title_index).get("words"));
									keyresult.put("toppoint", "2");
								}else if(i_title_index_y - 1 >= 0 && i_title_index < resultList.get(i_title_index_y - 1).size() && i_title_index_y == y && i_title_index != x){
									//纵坐标不在同一个地方，上面没有两个元素，这种情况不考虑
									//纵坐标在同一个地方，上面还有一个以上的元素，如果横坐标不一样，这个时候无需要截取
									keyresult.put("topword", resultList.get(i_title_index_y - 1).get(i_title_index).get("words"));
									keyresult.put("toppoint", "1");
								}else if(i_title_index_y - 1 >= 0 && i_title_index < resultList.get(i_title_index_y - 1).size() && i_title_index_y == y && i_title_index == x){
									//纵坐标不在同一个地方，上面没有两个元素，这种情况不考虑
									//纵坐标在同一个地方，上面还有一个以上的元素，如果横坐标不一样，这个时候需要进行截取
									keyresult.put("topword", resultList.get(i_title_index_y - 1).get(i_title_index).get("words").substring(0,2));
									keyresult.put("toppoint", "1");
								}else {
									keyresult.put("topword", null);
									keyresult.put("toppoint", null);
								}
							}else {
								//如果跟原有一样，则保留信息
								keyresult.put("topword", topword);
								keyresult.put("toppoint", toppoint);
							}
						}else {
							keyresult.put("topword", null);
							keyresult.put("toppoint", null);
						}
					}else {
						//按照之前的纵，横坐标无法获取相应的数据，则需要更新坐标和内容
						//不在同一个地方 并且 后面还有内容
						if(i_title_index_y - 2  >= 0 && i_title_index < resultList.get(i_title_index_y - 2).size() &&  i_title_index_y != y) {
							//因为纵坐标不在同一个地方，上面有两个以上元素，那么表明标题是单独的，不跟内容在一起，所以无需截取
							keyresult.put("topword", resultList.get(i_title_index_y - 2).get(i_title_index).get("words"));
							keyresult.put("toppoint", "2");
						}else if(i_title_index_y - 1 >= 0 && i_title_index < resultList.get(i_title_index_y - 1).size() && i_title_index_y == y && i_title_index != x){
							//纵坐标不在同一个地方，上面没有两个元素，这种情况不考虑
							//纵坐标在同一个地方，上面还有一个以上的元素，如果横坐标不一样，这个时候无需要截取
							keyresult.put("topword", resultList.get(i_title_index_y - 1).get(i_title_index).get("words"));
							keyresult.put("toppoint", "1");
						}else if(i_title_index_y - 1 >= 0 && i_title_index < resultList.get(i_title_index_y - 1).size() && i_title_index_y == y && i_title_index == x){
							//纵坐标不在同一个地方，上面没有两个元素，这种情况不考虑
							//纵坐标在同一个地方，上面还有一个以上的元素，如果横坐标不一样，这个时候需要进行截取
							keyresult.put("topword", resultList.get(i_title_index_y - 1).get(i_title_index).get("words").substring(0,2));
							keyresult.put("toppoint", "1");
						}else {
							keyresult.put("topword", null);
							keyresult.put("toppoint", null);
						}
					}
					//重新计算下边设置
					if(StringUtil.isNotBlank(bottompoint)) {
						int resultSize = resultList.size();
						//纵坐标大于计算位置
						if( i_title_index_y + Integer.parseInt(bottompoint) < resultSize && i_title_index < resultList.get(i_title_index_y + Integer.parseInt(bottompoint)).size()) {
							String bottomwordinfo = resultList.get(i_title_index_y + Integer.parseInt(bottompoint)).get(i_title_index).get("words");
							if(bottomwordinfo!=null && !bottomwordinfo.contains(bottomword)) {
								//按照之前的纵，横坐标无法获取相应的数据，则需要更新坐标和内容
								//不在同一个地方 并且 后面还有内容
								if(i_title_index_y + 2 < resultSize && i_title_index < resultList.get(i_title_index_y + 2).size() &&  i_title_index_y != y) {
									//因为纵坐标不在同一个地方，下面有两个以上元素，那么表明标题是单独的，不跟内容在一起，所以无需截取
									keyresult.put("bottomword", resultList.get(i_title_index_y + 2).get(i_title_index).get("words"));
									keyresult.put("bottompoint", "2");
								}else if(i_title_index_y + 1 > 0 && i_title_index < resultList.get(i_title_index_y + 1).size() && i_title_index_y == y && i_title_index != x){
									//纵坐标不在同一个地方，下面没有两个元素，这种情况不考虑
									//纵坐标在同一个地方，下面还有一个以上的元素，如果横坐标不一样，这个时候无需要截取
									keyresult.put("bottomword", resultList.get(i_title_index_y + 1).get(i_title_index).get("words"));
									keyresult.put("bottompoint", "1");
								}else if(i_title_index_y + 1 > 0 && i_title_index < resultList.get(i_title_index_y + 1).size() && i_title_index_y == y && i_title_index == x){
									//纵坐标不在同一个地方，上面没有两个元素，这种情况不考虑
									//纵坐标在同一个地方，上面后面还有一个以上的元素，如果横坐标不一样，这个时候需要进行截取
									keyresult.put("bottomword", resultList.get(i_title_index_y + 1).get(i_title_index).get("words").substring(0,2));
									keyresult.put("bottompoint", "1");
								}else {
									keyresult.put("bottomword", null);
									keyresult.put("bottompoint", null);
								}
							}else {
								//如果跟原有一样，则保留信息
								keyresult.put("bottomword", bottomword);
								keyresult.put("bottompoint", bottompoint);
							}
						}else {
							keyresult.put("bottomword", null);
							keyresult.put("bottompoint", null);
						}
					}else {
						int resultSize = resultList.size();
						//按照之前的纵，横坐标无法获取相应的数据，则需要更新坐标和内容
						//不在同一个地方 并且 后面还有内容
						if(i_title_index_y + 2 < resultSize && i_title_index < resultList.get(i_title_index_y + 2).size() &&  i_title_index_y != y) {
							//因为纵坐标不在同一个地方，下面有两个以上元素，那么表明标题是单独的，不跟内容在一起，所以无需截取
							keyresult.put("bottomword", resultList.get(i_title_index_y + 2).get(i_title_index).get("words"));
							keyresult.put("bottompoint", "2");
						}else if(i_title_index_y + 1 < resultSize && i_title_index < resultList.get(i_title_index_y + 1).size() && i_title_index_y == y && i_title_index != x){
							//纵坐标不在同一个地方，下面没有两个元素，这种情况不考虑
							//纵坐标在同一个地方，下面还有一个以上的元素，如果横坐标不一样，这个时候无需要截取
							keyresult.put("bottomword", resultList.get(i_title_index_y + 1).get(i_title_index).get("words"));
							keyresult.put("bottompoint", "1");
						}else if(i_title_index_y + 1 < resultSize && i_title_index < resultList.get(i_title_index_y + 1).size() && i_title_index_y == y && i_title_index == x){
							//纵坐标不在同一个地方，上面没有两个元素，这种情况不考虑
							//纵坐标在同一个地方，上面后面还有一个以上的元素，如果横坐标不一样，这个时候需要进行截取
							keyresult.put("bottomword", resultList.get(i_title_index_y + 1).get(i_title_index).get("words").substring(0,2));
							keyresult.put("bottompoint", "1");
						}else {
							keyresult.put("bottomword", null);
							keyresult.put("bottompoint", null);
						}
					}
					if(isFind)
						break;
				}
				if(isFind)
					break;
			}
			result.put(key, keyresult);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,List<String>> reformRequest(HttpServletRequest request) {
		
		Map<String,List<String>> map = null;
		try {
			Object wrapperRequest;
			if(request.getClass().getName().equals("org.springframework.web.multipart.support.StandardMultipartHttpServletRequest")) {
				Class t_clazz = request.getClass().getSuperclass().getSuperclass().getSuperclass();
				Field t_RequestField = t_clazz.getDeclaredField("request");
				t_RequestField.setAccessible(true);
				Object wrapper2Request = t_RequestField.get(request);//获取到request对象
				
				if(!wrapper2Request.getClass().getName().equals("org.springframework.security.web.firewall.StrictHttpFirewall$1"))
				{
					Class clazz1=wrapper2Request.getClass().getSuperclass().getSuperclass().getSuperclass();
					Field wrapperRequestField;
					try {
						wrapperRequestField = clazz1.getDeclaredField("request");
					} catch (Exception e) {
						wrapperRequestField = wrapper2Request.getClass().getSuperclass().getSuperclass().getDeclaredField("request");
					}
					wrapperRequestField.setAccessible(true);
					wrapperRequest = wrapperRequestField.get(wrapper2Request);//获取到request对象
				}else {
					wrapperRequest = wrapper2Request;
				}

			}
			else {
				Class clazz = request.getClass().getSuperclass().getSuperclass();
				Field wrapperRequestField = clazz.getDeclaredField("request");
				wrapperRequestField.setAccessible(true);
				wrapperRequest = wrapperRequestField.get(request);//获取到request对象
			}
			
			Object innerRequest = null;
            switch (wrapperRequest.getClass().getName()) {
                case "org.springframework.security.web.firewall.StrictHttpFirewall$1":
                case "org.springframework.security.web.firewall.StrictHttpFirewall$StrictFirewalledRequest": {
                    Class wrapperClazz = wrapperRequest.getClass().getSuperclass().getSuperclass().getSuperclass();
                    Field wrapper2RequestField = wrapperClazz.getDeclaredField("request");
                    wrapper2RequestField.setAccessible(true);
                    Object wrapper2Request = wrapper2RequestField.get(wrapperRequest);//获取到request对象


                    Class clazz3 = wrapper2Request.getClass();
                    Field requestField3 = clazz3.getDeclaredField("request");
                    requestField3.setAccessible(true);
                    innerRequest = requestField3.get(wrapper2Request);//获取到request对象

                    break;
                }
                case "org.springframework.security.web.servletapi.HttpServlet3RequestFactory$Servlet3SecurityContextHolderAwareRequestWrapper": {
                    Class wrapperClazz = wrapperRequest.getClass().getSuperclass().getSuperclass().getSuperclass();
                    Field wrapper2RequestField = wrapperClazz.getDeclaredField("request");
                    wrapper2RequestField.setAccessible(true);
                    Object wrapper2Request = wrapper2RequestField.get(wrapperRequest);//获取到request对象


                    Class clazz1 = wrapper2Request.getClass().getSuperclass().getSuperclass();
                    Field requestField1 = clazz1.getDeclaredField("request");
                    requestField1.setAccessible(true);
                    Object innerRequest1 = requestField1.get(wrapper2Request);//获取到request对象


                    Class clazz2 = innerRequest1.getClass().getSuperclass().getSuperclass().getSuperclass();
                    Field requestField2 = clazz2.getDeclaredField("request");
                    requestField2.setAccessible(true);
                    Object innerRequest2 = requestField2.get(innerRequest1);//获取到request对象


                    Class clazz3 = innerRequest2.getClass();
                    Field requestField3 = clazz3.getDeclaredField("request");
                    requestField3.setAccessible(true);
                    innerRequest = requestField3.get(innerRequest2);//获取到request对象

                    break;
                }
                case "com.matridx.igams.web.config.security.XssHttpServletRequestWrapper": {
                    Class wrapperClazz = wrapperRequest.getClass().getSuperclass().getSuperclass();
                    Field wrapper2RequestField = wrapperClazz.getDeclaredField("request");
                    wrapper2RequestField.setAccessible(true);
                    Object wrapper2Request = wrapper2RequestField.get(wrapperRequest);//获取到request对象


                    Class clazz1 = wrapper2Request.getClass().getSuperclass().getSuperclass();
                    Field requestField1 = clazz1.getDeclaredField("request");
                    requestField1.setAccessible(true);
                    Object innerRequest1 = requestField1.get(wrapper2Request);//获取到request对象

                    break;
                }
                case "org.apache.catalina.connector.RequestFacade": {
                    Class wrapperClazz = wrapperRequest.getClass();
                    Field wrapper2RequestField = wrapperClazz.getDeclaredField("request");
                    wrapper2RequestField.setAccessible(true);
                    innerRequest = wrapper2RequestField.get(wrapperRequest);//获取到request对象

                    break;
                }
            }
			
			
/*
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Class<?> classOfClassLoader = classLoader.getClass();
			System.out.print(classOfClassLoader.getClasses());
			Class innerClazz = classLoader.loadClass("org.apache.catalina.connector.RequestFacade");
			//设置尚未初始化 (否则在获取一些参数的时候，可能会导致不一致)
			Field t_requestField = innerClazz.getDeclaredField("request");
			t_requestField.setAccessible(true);
			Object t_innerRequest = t_requestField.get(innerRequest);//获取到request对象
			Field field = t_innerRequest.getClass().getDeclaredField("parametersParsed");
			field.setAccessible(true);
			field.setBoolean(innerRequest , false);
*/
			Field coyoteRequestField = innerRequest.getClass().getDeclaredField("coyoteRequest");
			coyoteRequestField.setAccessible(true);
			Object coyoteRequestObject = coyoteRequestField.get(innerRequest);//获取到coyoteRequest对象

			Field parametersField = coyoteRequestObject.getClass().getDeclaredField("parameters");
			parametersField.setAccessible(true);
			Object parameterObject = parametersField.get(coyoteRequestObject);//获取到parameter的对象

			//获取hashtable来完成对参数变量的修改
			Field hashTabArrField = parameterObject.getClass().getDeclaredField("paramHashValues");
			hashTabArrField.setAccessible(true);
			map = (Map<String,List<String>>)hashTabArrField.get(parameterObject);
			//也可以通过下面的方法，不过下面的方法只能添加参数，如果有相同的key，会追加参数，即，同一个key的结果集会有多个
			//Method method = parameterObject.getClass().getDeclaredMethod("addParameterValues" , String.class , String[].class);
			//method.invoke(parameterObject , "11" , new String[] {"1 you!" , "1"});
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		
		return map;
	}
	
	public static void main(String[] args){
		//String s = "{\"log_id\": 5631749734365636938, \"words_result_num\": 7, \"words_result\": [{\"location\": {\"width\": 129, \"top\": 67, \"left\": 59, \"height\": 34}, \"words\": \"姓名:张三\"}, {\"location\": {\"width\": 122, \"top\": 69, \"left\": 318, \"height\": 32}, \"words\": \"年龄:24\"}, {\"location\": {\"width\": 98, \"top\": 69, \"left\": 530, \"height\": 32}, \"words\": \"性别:男\"}, {\"location\": {\"width\": 615, \"top\": 109, \"left\": 60, \"height\": 33}, \"words\": \"电话:17855856564身高:175cm居住地:杭州\"}, {\"location\": {\"width\": 147, \"top\": 152, \"left\": 60, \"height\": 33}, \"words\": \"体重:70kg\"}, {\"location\": {\"width\": 127, \"top\": 151, \"left\": 325, \"height\": 32}, \"words\": \"爱好:篮球\"}, {\"location\": {\"width\": 151, \"top\": 151, \"left\": 527, \"height\": 33}, \"words\": \"工作地:杭州\"}]}";
		//String s = "{\"log_id\": 7848046802621478468, \"words_result_num\": 43, \"words_result\": [{\"location\": {\"width\": 279, \"top\": 529, \"left\": 2050, \"height\": 72}, \"words\": \"MARD人\"}, {\"location\": {\"width\": 310, \"top\": 798, \"left\": 230, \"height\": 80}, \"words\": \"会议日程\"}, {\"location\": {\"width\": 1144, \"top\": 771, \"left\": 828, \"height\": 169}, \"words\": \"病原宏基因组测序在危重感染诊断中的应用研讨会\"}, {\"location\": {\"width\": 123, \"top\": 833, \"left\": 2216, \"height\": 60}, \"words\": \"讲着\"}, {\"location\": {\"width\": 115, \"top\": 927, \"left\": 1333, \"height\": 52}, \"words\": \"内容\"}, {\"location\": {\"width\": 212, \"top\": 935, \"left\": 2208, \"height\": 58}, \"words\": \"陈主任\"}, {\"location\": {\"width\": 83, \"top\": 1005, \"left\": 404, \"height\": 44}, \"words\": \"时\"}, {\"location\": {\"width\": 345, \"top\": 1007, \"left\": 1244, \"height\": 95}, \"words\": \"大会主席开场\"}, {\"location\": {\"width\": 152, \"top\": 1023, \"left\": 2347, \"height\": 56}, \"words\": \"公攀授\"}, {\"location\": {\"width\": 273, \"top\": 1097, \"left\": 335, \"height\": 68}, \"words\": \"8:30-8:40\"}, {\"location\": {\"width\": 625, \"top\": 1097, \"left\": 1132, \"height\": 121}, \"words\": \"发热情查的临床诊的\"}, {\"location\": {\"width\": 283, \"top\": 1121, \"left\": 2273, \"height\": 76}, \"words\": \"霍决平主任\"}, {\"location\": {\"width\": 279, \"top\": 1208, \"left\": 341, \"height\": 74}, \"words\": \"8:40-9:20\"}, {\"location\": {\"width\": 806, \"top\": 1197, \"left\": 1068, \"height\": 136}, \"words\": \"功低下想钠部溶爷\"}, {\"location\": {\"width\": 474, \"top\": 1205, \"left\": 2224, \"height\": 123}, \"words\": \"王(杰毅生物CEO)\"}, {\"location\": {\"width\": 324, \"top\": 1324, \"left\": 326, \"height\": 76}, \"words\": \"9:20-10:00\"}, {\"location\": {\"width\": 869, \"top\": 1298, \"left\": 1072, \"height\": 164}, \"words\": \"宏基因组测序背景菌的挑战和方案\"}, {\"location\": {\"width\": 255, \"top\": 1328, \"left\": 2390, \"height\": 93}, \"words\": \"周华主任\"}, {\"location\": {\"width\": 335, \"top\": 1443, \"left\": 347, \"height\": 78}, \"words\": \"0:00-10:20\"}, {\"location\": {\"width\": 1351, \"top\": 1376, \"left\": 845, \"height\": 250}, \"words\": \"mNGS在肺部病毒、真菌混合感染中的临床案例解杄\"}, {\"location\": {\"width\": 179, \"top\": 1464, \"left\": 2445, \"height\": 62}, \"words\": \"陈俭主\"}, {\"location\": {\"width\": 78, \"top\": 1451, \"left\": 2621, \"height\": 52}, \"words\": \"任\"}, {\"location\": {\"width\": 373, \"top\": 1570, \"left\": 322, \"height\": 91}, \"words\": \"10:20-11:00\"}, {\"location\": {\"width\": 1039, \"top\": 1521, \"left\": 1044, \"height\": 205}, \"words\": \"mNGs在危重感染诊断中的应用和前景\"}, {\"location\": {\"width\": 101, \"top\": 1582, \"left\": 2644, \"height\": 42}, \"words\": \"ina\"}, {\"location\": {\"width\": 119, \"top\": 1562, \"left\": 2730, \"height\": 50}, \"words\": \"中国\"}, {\"location\": {\"width\": 128, \"top\": 1605, \"left\": 2382, \"height\": 60}, \"words\": \"刘然\"}, {\"location\": {\"width\": 378, \"top\": 1701, \"left\": 332, \"height\": 95}, \"words\": \"11:00-11:40\"}, {\"location\": {\"width\": 146, \"top\": 1732, \"left\": 1453, \"height\": 66}, \"words\": \"检测\"}, {\"location\": {\"width\": 380, \"top\": 1691, \"left\": 1574, \"height\": 83}, \"words\": \"只有 shote\"}, {\"location\": {\"width\": 289, \"top\": 1750, \"left\": 1173, \"height\": 87}, \"words\": \"病原NG5\"}, {\"location\": {\"width\": 388, \"top\": 1839, \"left\": 337, \"height\": 101}, \"words\": \"1:40-1200\"}, {\"location\": {\"width\": 160, \"top\": 1855, \"left\": 1552, \"height\": 62}, \"words\": \"结、\"}, {\"location\": {\"width\": 140, \"top\": 1832, \"left\": 1728, \"height\": 66}, \"words\": \"午餐\"}, {\"location\": {\"width\": 82, \"top\": 1882, \"left\": 1421, \"height\": 64}, \"words\": \"大\"}, {\"location\": {\"width\": 195, \"top\": 1998, \"left\": 443, \"height\": 76}, \"words\": \"12:00\"}, {\"location\": {\"width\": 2484, \"top\": 2107, \"left\": 253, \"height\": 523}, \"words\": \"会议时间:2019年12月22日(周日)上午8:30-12:00\"}, {\"location\": {\"width\": 2755, \"top\": 2210, \"left\": 242, \"height\": 642}, \"words\": \"会议地点:杭州天元大厦5楼天元厅(中国浙江杭州市江干区钱江新\"}, {\"location\": {\"width\": 443, \"top\": 2291, \"left\": 257, \"height\": 144}, \"words\": \"会议主办\"}, {\"location\": {\"width\": 406, \"top\": 2224, \"left\": 763, \"height\": 130}, \"words\": \"杰毅生物\"}, {\"location\": {\"width\": 2568, \"top\": 2429, \"left\": 248, \"height\": 621}, \"words\": \"路2号),杭州东站地铁B口乘4号线至钱江路站D3口出。\"}, {\"location\": {\"width\": 1654, \"top\": 2791, \"left\": 246, \"height\": 480}, \"words\": \"会议联系:严女士15168233654\"}, {\"location\": {\"width\": 710, \"top\": 3769, \"left\": 2150, \"height\": 228}, \"words\": \"码法册参\"}]}";
		/* 2020-12-31
		String s = "{\"log_id\": 5760196122227551339, \"words_result_num\": 18, \"words_result\": [{\"location\": {\"width\": 65, \"top\": 36, \"left\": 44, \"height\": 35}, \"words\": \"姓1名\"}, {\"location\": {\"width\": 66, \"top\": 38, \"left\": 298, \"height\": 32}, \"words\": \"年龄\"}, {\"location\": {\"width\": 66, \"top\": 37, \"left\": 591, \"height\": 35}, \"words\": \"性别\"}, {\"location\": {\"width\": 40, \"top\": 77, \"left\": 43, \"height\": 38}, \"words\": \"张三\"}, {\"location\": {\"width\": 67, \"top\": 78, \"left\": 300, \"height\": 33}, \"words\": \"17岁\"}, {\"location\": {\"width\": 33, \"top\": 79, \"left\": 600, \"height\": 33}, \"words\": \"男\"}, {\"location\": {\"width\": 64, \"top\": 122, \"left\": 46, \"height\": 32}, \"words\": \"身高\"}, {\"location\": {\"width\": 65, \"top\": 121, \"left\": 298, \"height\": 33}, \"words\": \"体11重\"}, {\"location\": {\"width\": 69, \"top\": 121, \"left\": 592, \"height\": 33}, \"words\": \"爱好\"}, {\"location\": {\"width\": 83, \"top\": 165, \"left\": 46, \"height\": 29}, \"words\": \"175cm\"}, {\"location\": {\"width\": 62, \"top\": 168, \"left\": 305, \"height\": 29}, \"words\": \"60kg\"}, {\"location\": {\"width\": 66, \"top\": 163, \"left\": 597, \"height\": 34}, \"words\": \"下棋\"}, {\"location\": {\"width\": 94, \"top\": 205, \"left\": 45, \"height\": 32}, \"words\": \"居住地\"}, {\"location\": {\"width\": 65, \"top\": 204, \"left\": 298, \"height\": 33}, \"words\": \"电话\"}, {\"location\": {\"width\": 120, \"top\": 205, \"left\": 592, \"height\": 32}, \"words\": \"政治面貌\"}, {\"location\": {\"width\": 62, \"top\": 247, \"left\": 48, \"height\": 31}, \"words\": \"宁波\"}, {\"location\": {\"width\": 127, \"top\": 252, \"left\": 305, \"height\": 23}, \"words\": \"123456789\"}, {\"location\": {\"width\": 62, \"top\": 246, \"left\": 608, \"height\": 33}, \"words\": \"团员\"}]}";
		
		JSONObject jsonObject = JSONObject.parseObject(s);
		JSONArray words = jsonObject.getJSONArray("words_result");
		List<List<Map<String, String>>> resultList = ToolUtil.pareString(words);
		Map<String,Map<String, String>> keyMap = new HashMap<String,Map<String, String>>();
		*/
		/*
		Map<String, String> xm = new HashMap<>(); xm.put("words", "姓1名");
		xm.put("rightpoint", "1"); xm.put("rightword", "年龄"); xm.put("bottompoint",
		"2"); xm.put("bottomword", "身高");xm.put("sameflg", "0");
		  
		Map<String, String> tz = new HashMap<>(); tz.put("words", "体重");
		tz.put("toppoint", "2"); tz.put("topword", "年龄"); tz.put("rightpoint", "1");
		tz.put("rightword", "爱好");tz.put("leftpoint", "1");
		tz.put("leftword", "身高");tz.put("sameflg", "0");
		  
		Map<String, String> sg = new HashMap<>(); sg.put("words", "身高");
		sg.put("toppoint", "2"); sg.put("topword", "姓1名"); sg.put("bottompoint", "2");
		sg.put("bottomword", "居住"); 
		sg.put("rightpoint", "1"); sg.put("rightword", "体重");sg.put("sameflg", "0");
		*/
		/* 2020-12-31
		Map<String, String> xm = new HashMap<>(); xm.put("words", "姓1名");
		xm.put("rightpoint", "1"); xm.put("rightword", "年龄"); xm.put("bottompoint",
		"2"); xm.put("bottomword", "身高");xm.put("sameflg", "0");xm.put("wordflg", "contains");
		  
		Map<String, String> tz = new HashMap<>(); tz.put("words", "122cm");
		tz.put("toppoint", "1"); tz.put("topword", "身高"); tz.put("rightpoint", "2");
		tz.put("rightword", "下2棋");tz.put("leftpoint", null);
		tz.put("leftword", null);tz.put("bottompoint", "1");
		tz.put("bottomword", "居住地");tz.put("sameflg", "1");tz.put("wordflg", "contains");
		  
		Map<String, String> sg = new HashMap<>(); sg.put("words", "身高");
		sg.put("toppoint", "2"); sg.put("topword", "姓1名"); sg.put("bottompoint", "2");
		sg.put("bottomword", "居住地"); sg.put("wordflg", "contains");
		sg.put("rightpoint", "1"); sg.put("rightword", "体1");sg.put("sameflg", "0");
		
		keyMap.put("体重", tz); 
		keyMap.put("姓名", xm);
		keyMap.put("身高", sg);
		 
		
		
		Map<String,String> infoMap = new HashMap<String,String>(); infoMap.put("姓名",
		"张三"); infoMap.put("身高", "175cm"); infoMap.put("体重", "60kg");
		List<Map<String,String>> recoderList = new ArrayList<Map<String,String>>();
		ToolUtil.study(resultList, keyMap, infoMap, recoderList);
		  
		Map<String,String> wordMap = new HashMap<>();
		*/
		/*
		 * wordMap.put("words", "体重"); wordMap.put("top", "152"); wordMap.put("left",
		 * "60"); wordMap.put("width", "147"); wordMap.put("height", "33");
		 */
		  
		/*
		 * wordMap.put("words", "姓名"); wordMap.put("top", "67"); wordMap.put("left",
		 * "59"); wordMap.put("width", "129"); wordMap.put("height", "34");
		 */
		
		/*
		 * wordMap.put("words", "体重"); wordMap.put("top", "121"); wordMap.put("left",
		 * "298"); wordMap.put("width", "65"); wordMap.put("height", "33");
		 */
		  
		//ToolUtil.getAroundWord(resultList, wordMap);
		 
		//ToolUtil.prediction(resultList, keyMap);
		/* 2020-12-31
		System.out.println(wordMap.size());
		*/
		
		ToolUtil.pdfToImagePdf("E:/Logs/海南省人民医院_赵雅芳之子(脑脊液)_20C235263.pdf", "E:/Logs/122.pdf");
	}
	
	/**
	 * PDFBOX转图片
	 * 
	 * @param pdfUrl
	 *			pdf的路径
	 * @param imgTempUrl
	 *			图片输出路径
	 */
	/*public static void pdfToImage(String pdfUrl, String imgTempUrl)
	{
		try
		{
			// 读入PDF
			PdfReader pdfReader = new PdfReader(pdfUrl);
			// 计算PDF页码数
			int pageCount = pdfReader.getNumberOfPages();
			// 循环每个页码
			for (int i = pageCount; i >= pdfReader.getNumberOfPages(); i--)
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				PdfStamper pdfStamper = null;
				PDDocument pdDocument = null;

				pdfReader = new PdfReader(pdfUrl);
				pdfReader.selectPages(String.valueOf(i));
				pdfStamper = new PdfStamper(pdfReader, out);
				pdfStamper.close();
				// 利用PdfBox生成图像
				pdDocument = PDDocument.load(new ByteArrayInputStream(out
						.toByteArray()));
				OutputStream outputStream = new FileOutputStream(imgTempUrl
						+ "ImgName" + "-" + i + ".bmp");

				ImageOutputStream output = ImageIO
						.createImageOutputStream(outputStream);
				PDPage page = (PDPage) pdDocument.getDocumentCatalog().getPages().get(0);
				BufferedImage image = page.convertToImage(
						BufferedImage.TYPE_INT_RGB, 96);
				ImageIOUtil.writeImage(image, "bmp", outputStream, 96);
				if (output != null)
				{
					output.flush();
					output.close();
				}
				pdDocument.close();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}*/
	
	/**
	 * pdf转图片
	 *
	 * @param pdfPath PDF路径
	 * @return 图片路径
	 */
	public static void pdfToImage(String pdfPath, String imgPath) {
		try {
					   
			//System.setProperty("sun.java2d.cmm","sun.java2d.cmm.kcms.KcmsServiceProvider");
			//图像合并使用参数
			// 总宽度
			int width = 0;
			// 保存一张图片中的RGB数据
			int[] singleImgRGB;
			int shiftHeight = 0;
			//保存每张图片的像素值
			BufferedImage imageResult = null;
			//利用PdfBox生成图像
			PDDocument pdDocument = PDDocument.load(new File(pdfPath));
			PDFRenderer renderer = new PDFRenderer(pdDocument);
			
			//循环每个页码
			for (int i = 0, len = pdDocument.getNumberOfPages(); i < len; i++) {
				BufferedImage image = renderer.renderImageWithDPI(i, 105, ImageType.RGB);
				int imageHeight = image.getHeight();
				int imageWidth = image.getWidth();
				//计算高度和偏移量
				if (i == 0) {
					//使用第一张图片宽度;
					width = imageWidth;
					//保存每页图片的像素值
					imageResult = new BufferedImage(width, imageHeight * len, BufferedImage.TYPE_INT_RGB);
				} else {
					// 计算偏移高度
					shiftHeight += imageHeight;
				}
				singleImgRGB = image.getRGB(0, 0, width, imageHeight, null, 0, width);
				
				// 写入流中
				imageResult.setRGB(0, shiftHeight, width, imageHeight, singleImgRGB, 0, width);
				// 第四步：在文档中增加图片。
			}
			pdDocument.close();
			// 写图片
			ImageIO.write(imageResult, "jpg", new File(imgPath));
		} catch (Exception e) {
			//logger.error("PDF转图片失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * pdf转图片
	 *
	 * @param pdfPath PDF路径
	 * @return 图片路径
	 */
	public static void pdfToImagePdf(String pdfPath, String imgPdfPath) {
		try {
					   
			//System.setProperty("sun.java2d.cmm","sun.java2d.cmm.kcms.KcmsServiceProvider");
			//利用PdfBox生成图像
			PDDocument pdDocument = PDDocument.load(new File(pdfPath));
			PDFRenderer renderer = new PDFRenderer(pdDocument);
			// 第一步：创建一个document对象。
			Document document = new Document();
			document.setMargins(0, 0, 0, 0);
			// 第二步：
			// 创建一个PdfWriter实例，
			PdfWriter.getInstance(document, new FileOutputStream(imgPdfPath));
			// 第三步：打开文档。
			document.open();
			
			
			//循环每个页码
			for (int i = 0, len = pdDocument.getNumberOfPages(); i < len; i++) {
				BufferedImage image = renderer.renderImageWithDPI(i, 122, ImageType.RGB);
				int imageHeight = image.getHeight();
				int imageWidth = image.getWidth();
				ImageIO.write(image, "jpg", new File("E:/logs/temp.jpg"));
				// 第四步：在文档中增加图片。
				// 根据图片大小设置页面，一定要先设置页面，再newPage（），否则无效
				document.setPageSize(new Rectangle(imageWidth, imageHeight));
				document.newPage();
				document.add(Image.getInstance("E:/logs/temp.jpg"));
			}
			pdDocument.close();
			document.close();
			// 写图片
			//ImageIO.write(imageResult, "jpg", new File(imgPath));
		} catch (Exception e) {
			//logger.error("PDF转图片失败");
			e.printStackTrace();
		}
	}
}
