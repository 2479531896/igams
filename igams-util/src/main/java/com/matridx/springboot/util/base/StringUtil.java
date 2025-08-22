package com.matridx.springboot.util.base;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StringUtil extends StringUtils {

	private static final Logger log = LoggerFactory.getLogger(StringUtil.class);
	private static final int[] allChineseScope = { 1601, 1637, 1833, 2078,
			2274, 2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730,
			3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249, 5600,
			Integer.MAX_VALUE };
	public static final char unknowChar = '*';
	private static final char[] allEnglishLetter = { 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'W', 'X', 'Y', 'Z', unknowChar };

	private StringUtil() {

	}

	/**
	 * 字串是否为空
	 */
	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		} else if (str.isEmpty()) {
			return true;
		} else return "NULL".equalsIgnoreCase(str);
    }

	/**
	 * 判断 Null 或 空字符串
	 */
	public static boolean isNull(String str) {

		return str == null || str.trim().isEmpty();
	}

	/**
	 * 生成查询字串Map
	 */
	public static Map<String, String> getMapFromQueryParamString(String str) {
		Map<String, String> param = new HashMap<>();
		String[] keyValues = str.split("`");
		for (int i = 0; i < keyValues.length; i++) {

		}
		return param;
	}

	/**
	 * 全替换
	 * 
	 * @param src
	 *            替换字串
	 * @param tar
	 *            替换目标
	 * @param str
	 *            主字串
	 */
	public static String replaceAll(String src, String tar, String str) {
		StringBuilder sb = new StringBuilder();
		byte[] bytesSrc = src.getBytes();

		byte[] bytes = str.getBytes();
		int point = 0;
		for (int i = 0; i < bytes.length; i++) {

			if (isStartWith(bytes, i, bytesSrc)) {

				sb.append(new String(bytes, point, i));
				sb.append(tar);
				i += bytesSrc.length;
				point = i;
			}

		}
		sb.append(new String(bytes, point, bytes.length));
		return sb.toString();
	}

	private static boolean isStartWith(byte[] bytesSrc, int startSrc,
                                       byte[] bytesTar) {
		for (int j = 0; j < bytesTar.length; j++) {
			if (bytesSrc[startSrc + j] != bytesTar[j]) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 取中文拼音首字符
	 */
	public static char getFirstLetterFromChinessWord(String str) {
		char result = '*';
		String temp = str.toUpperCase();
		try {
			byte[] bytes = temp.getBytes("gbk");
			if (bytes[0] > 0) {
				return (char) bytes[0];
			}

			int gbkIndex;

			for (int i = 0; i < bytes.length; i++) {
				bytes[i] -= (byte) 160;
			}
			gbkIndex = bytes[0] * 100 + bytes[1];
			for (int i = 0; i < allEnglishLetter.length; i++) {
				if (gbkIndex >= allChineseScope[i]
						&& gbkIndex < allChineseScope[i + 1]) {
					result = allEnglishLetter[i];
					break;
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 字串分割
	 */
	public static String[] split(String src, char letter) {
		if (src == null) {
			return new String[0];
		}
		List<String> ret = new ArrayList<>();
		byte[] bytes = src.getBytes();
		int curPoint = 0;
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] == letter) {
				String s = new String(bytes, curPoint, i - curPoint);
				ret.add(s);
				curPoint = i + 1;
			}
		}
		if (ret.isEmpty()) {
			return new String[] { src };
		}
		// ret.add(new String(bytes, curPoint, src.length() - curPoint));
		String[] retStr = new String[ret.size()];
		for (int i = 0; i < ret.size(); i++) {
			retStr[i] = ret.get(i);
		}
		return retStr;
	}

	public static String[] split(String src, String letter) {
		String[] retStr = new String[0];
		try {
			retStr = StringUtils.split(src, letter);

		} catch (Exception e) {
			return retStr;
		}
		return retStr;
	}

	/**
	 * 去除最后一个字符
	 */
	public static String removeLast(String str) {
		if (str == null) {
			return null;
		}
		return str.substring(0, str.length() - 1);
	}

	/**
	 * 为字符串的每个元素增加单引号，供sql语句调用 如字符串"123,567"变成"'123','567'"
	 */
	public static String addQuotation(String str) {
		if (str == null) {
			return null;
		}
		StringBuilder newStr = new StringBuilder();
		String[] strs = split(str, ",");
		for (int i = 0; i < strs.length; i++) {
			if (i > 0) {
				newStr.append(",");
			}
			newStr.append("'").append(strs[i]).append("'");
		}
		return newStr.toString();
	} 
	
	/**
	 * list转string数组
	 * @return String[]
	 */
	public static String[] listToArray(List<String> list){
	      String[] strs = new String[list.size()]; 
	      return list.toArray(strs);
	}
	
	/**
	 * list转string字符串,以符号分隔
	 * @return String
	 */
	public static String listToString(List<String> list,String separator){
	   return  StringUtil.join(listToArray(list), separator);
	}

	/**
	 * 逗号分割的字符串转换为Collection
	 * @return Collection
	 */
	public static Collection<String> stringToCollection(String str) {
		if (isNotBlank(str)) {
            String[] strArray = str.split(",");
			if (strArray.length > 0) {
                return new HashSet<>(Arrays.asList(strArray));
			}
		}
		return new HashSet<>();
	}
	
	public static boolean isEmptyArray(String[] array){
		if (null != array){
			for (String str : array){
				if (!StringUtils.isEmpty(str)){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 获取字符串的长度，中文占两个字符,英文数字占一个字符
	 * @return int
	 */
	public static int stringLength(String value) {
		int valueLength = 0;
		if (isNotBlank(value)){
			String chinese = "[\u4e00-\u9fa5]";
			for (int i = 0; i < value.length(); i++) {
				String temp = value.substring(i, i + 1);
				if (temp.matches(chinese)) {
					valueLength += 2;
				} else {
					valueLength += 1;
				}
			}
		}
		return valueLength;
	}
	
	public static void main(String[] argv) {
		
	}
	
	public static String firstToUpper(String s){
		if(StringUtils.isBlank(s)) return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
	public static String generateUUID(){
		return generateUUID(null);
	}
	
	public static String generateUUID(String s){
		UUID uuid;
		if(StringUtils.isNotBlank(s)){
			uuid = UUID.nameUUIDFromBytes(s.getBytes());
		}else{
			uuid = UUID.randomUUID();
		}
		return uuid.toString().toUpperCase().replaceAll("-", "");
	}
	
	/**
	 * 若为空或为"null"返回指定的默认值
	 * @author goofus
	 */
	public static String getStr(String str, String defStr){
		if(StringUtils.isBlank(str) || "null".equalsIgnoreCase(str) || "undefined".equalsIgnoreCase(str)){
			str = defStr;
		}
		return str;
	}
	
	/**
	 * 按指定分隔符拼接字符串(忽略空的字符)
	 * @param separator 分隔符
	 * @param strs	字符串数组
	 */
	public static String joinIgnoreEmpty(String separator, String... strs){
		String result = null;
		if(strs!=null){
			List<String> _temp = new ArrayList<>();
			for (String str : strs) {
				if(StringUtil.isNotBlank(str)){
					_temp.add(str);
				}
			}
			result = StringUtils.join(_temp.toArray(), separator);
		}
		return StringUtil.defaultString(result);
	}
	
	/**
	 * 判断多个字符串中是否存在空的
	 * @return 存在空，返回true，不存在则false
	 */
	public static boolean isAnyBlank(String... strs){
		if(strs==null||strs.length==0) return true;
		for (String str : strs) {
			if(StringUtils.isBlank(str)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 按字节长度截取字符串
	 * @return 小于两位字节返回空
	 */
	public static String subStringByByte(String str, int len) {
        String result = null;
        if (str != null) {
            byte[] a = str.getBytes();
            if (a.length <= len) {
                result = str;
            } else if (len > 0) {
                result = new String(a, 0, len);
                int length = result.length();
                if (str.charAt(length - 1) != result.charAt(length - 1)) {
                    if (length < 2) {
                        result = null;
                    } else {
                        result = result.substring(0, length - 1);
                    }
                }
            }
        }
        return result;
    }
	
	/**
	 * 字符串为null或为0，置为空串
	 */
	public static String zeroToEmpty(String s){
		if(StringUtils.isBlank(s) || "0".equals(s)){
			return "";
		}
		return s;
	}
	
	/**
	 * 字符串太长则截取前面几位
	 * @param lang 超出给定长度从开始位置截取给定的这个长度
	 */
	public static String cutOfLangStr(String s, int lang){
		if(StringUtils.isNotBlank(s) && s.length() > lang){
			return s.substring(0, lang) + "...";
		}
		return s;
	}
	
	/**
	 * 根据正则表达式替换全部匹配的字符为指定的字符
	 * @param str 原字符串
	 * @param destStr 要替换的字符串，传null将默认替换为空串
	 * @param regex 正则表达式，传null默认匹配TAB，换行，空格
	 * @author goofus
	 */
	public static String replaceAllByRegex(String str, String destStr, String regex){
	        if(StringUtils.isNotBlank(str)) {
	        	if(StringUtils.isBlank(regex)) regex = "\\s|\t|\r|\n";
	        	if(null == destStr) destStr = "";
	            Pattern p = Pattern.compile(regex);
	            Matcher m = p.matcher(str);
	            str = m.replaceAll(destStr);
	        }
	        return str;
	}
	
	/*
     * 将小写的人民币转化成大写
     */
    public static String convertToChineseNumber(BigDecimal number)
    {
        StringBuilder chineseNumber = new StringBuilder();
        String [] num={"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
        String [] unit = {"分","角","圆","拾","佰","仟","万","拾","佰","仟","亿","拾","佰","仟","万"};
        String tempNumber = String.valueOf(number.multiply(new BigDecimal("100")).setScale(0).toString());
        int tempNumberLength = tempNumber.length();
        if ("0".equals(tempNumber))
        {
            return "零圆整";
        }
        if (tempNumberLength > 15)
        {
        	return "超出转化范围.";
        }
        boolean preReadZero = true;    //前面的字符是否读零
        for (int i=tempNumberLength; i>0; i--)
        {
            if ((tempNumberLength - i + 2) % 4 == 0)
            {
                //如果在（圆，万，亿，万）位上的四个数都为零,如果标志为未读零，则只读零，如果标志为已读零，则略过这四位
                if (i - 4 >= 0 && "0000".equals(tempNumber.substring(i - 4, i)))
                {
                    if (!preReadZero)
                    {
                        chineseNumber.insert(0, "零");
                        preReadZero = true;
                    }
                    i -= 3;    //下面还有一个i--
                    continue;
                }
                //如果当前位在（圆，万，亿，万）位上，则设置标志为已读零（即重置读零标志）
                preReadZero = true;
            }
            int digit = Integer.parseInt(tempNumber.substring(i - 1, i));
            if (digit == 0)
            {
                //如果当前位是零并且标志为未读零，则读零，并设置标志为已读零
                if (!preReadZero)
                {
                    chineseNumber.insert(0, "零");
                    preReadZero = true;
                }
                //如果当前位是零并且在（圆，万，亿，万）位上，则读出（圆，万，亿，万）
                if ((tempNumberLength - i + 2) % 4 == 0)
                {
                    chineseNumber.insert(0, unit[tempNumberLength - i]);
                }
            }
            //如果当前位不为零，则读出此位，并且设置标志为未读零
            else
            {
                chineseNumber.insert(0, num[digit] + unit[tempNumberLength - i]);
                preReadZero = false;
            }
        }
        chineseNumber.append("圆");
        //如果分角两位上的值都为零，则添加一个“整”字
        if (tempNumberLength - 2 >= 0 && "00".equals(tempNumber.substring(tempNumberLength - 2, tempNumberLength)))
        {
            chineseNumber.append("整");
        }
        return chineseNumber.toString();
    }
    
    /**
	* 人民币大写转换小写
	*/
	public static long convertUpperToNum(String money) {
		long result = 0L;
		/*
		 先分割出各级别(亿、万、个)
		*/
		String yi = money.contains("亿") ? money.substring(0, money.indexOf("亿") + 1) : ""; // 亿
		if (!yi.isEmpty()) {
			result = convertUpperToNum(yi.substring(0, yi.length()-1)) * 100000000; // 计算亿前面的值
		}
		money = money.contains("亿") ? money.substring(money.indexOf("亿") + 1) : money;
		String wan = money.contains("万") ? money.substring(0, money.indexOf("万") + 1) : ""; // 万
		money = money.contains("万") ? money.substring(money.indexOf("万") + 1) : money; // 个
		long wanNum = !wan.isEmpty() ? calcNum(wan, 2) : 0;
		long geNum = !money.isEmpty() ? calcNum(money, 1) : 0;
		result = result + wanNum + geNum;
		return result;
	}
	/**
	* 计算各级别的数值
	*/
	public static long calcNum(String money, int index) {
		long num = 0L;
		if (index != 1) {
		// System.out.println("money:" + money);
			money = money.substring(0, money.length()-1);
		}
		/*
		 分割千、百、十、个
		*/
		String qian = money.contains("仟") ? money.substring(0, money.indexOf("仟")) : ""; // 仟
		if (!qian.isEmpty()) {
			num += upperToNum(qian.charAt(qian.length()-1)) * 1000L;
		}
		money = money.contains("仟") ? money.substring(money.indexOf("仟") + 1) : money;
		String bai = money.contains("佰") ? money.substring(0, money.indexOf("佰")) : ""; // 佰
		if (!bai.isEmpty()) {
			num += upperToNum(bai.charAt(bai.length()-1)) * 100L;
		}
		money = money.contains("佰") ? money.substring(money.indexOf("佰") + 1) : money;
		String shi = money.contains("拾") ? money.substring(0, money.indexOf("拾")) : ""; // 拾
		if (!shi.isEmpty()) {
			num += upperToNum(shi.charAt(shi.length()-1)) * 10L;
		}
		if (!money.isEmpty()) {
			money = money.contains("拾") ? money.substring(money.indexOf("拾") + 1) : money.substring(money.length()-1); // 个
			if (!money.isEmpty()) {
				num += upperToNum(money.charAt(money.length()-1));
			}
		}
		switch (index) {
			case 1: // 个
				break;
			case 2: // 万
				num = num * 10000;
				break;
		}
		return num;
	}
	/**
	* 小写转大写
	*/
	public static int upperToNum(char value) {
		int num = 0;
		switch (value) {
			case '零':
				break;
			case '壹':
				num = 1;
				break;
			case '贰':
				num = 2;
				break;
			case '叁':
				num = 3;
				break;
			case '肆':
				num = 4;
				break;
			case '伍':
				num = 5;
				break;
			case '陆':
				num = 6;
				break;
			case '柒':
				num = 7;
				break;
			case '捌':
				num = 8;
				break;
			case '玖':
				num = 9;
			break;
		}
		return num;
	}
    
    /**
     * str2在str1中出现的个数
     */
    public static int countStr(String str1, String str2) {   
        int counter=0;  
        if (!str1.contains(str2)) {
            return 0;  
        }   
            while(str1.contains(str2)){
                counter++;  
                str1=str1.substring(str1.indexOf(str2)+str2.length());  
            }  
            return counter;    
    }
    
    public static String urlEncodeToString(String enString) {
    	byte[] bytes = enString.getBytes(StandardCharsets.ISO_8859_1);
		StringBuilder bf = new StringBuilder();
        for (byte aByte : bytes) {
            String s_byte = Integer.toHexString(aByte);
            s_byte = "%" + s_byte.substring(s_byte.length() - 2);
            bf.append(s_byte);
        }

        return URLDecoder.decode(bf.toString(), StandardCharsets.UTF_8);
    }
    
	/**
	 * 切割前台拼接的字符串
	 * */
	public static List<String> splitString(String str){
		List<String> items = new ArrayList<>();
		if(isNotBlank(str)){
			String[] item = str.split(",");
			for (String string : item) {
				string = string.replace("'", "");
				items.add(string.trim());
			}
			return items;
		}
		return items;
	}
	
	/**
	 * 从数字转换成盒子的位置（数字）
	 */
	public static String changeToPos(int total,int pos){
		double n =Math.sqrt(total);
		int d_row = (pos-1)/(int)n+1;
		int d_col = (pos-1)%(int)n+1;
		
		String s_row=String.valueOf((char) (d_row+64));
		
		return s_row+ d_col;
	}
	
	/**
	 * 从数字转换成盒子的位置（字符）
	 */
	public static String changeToPos(String total,String pos){
		return changeToPos(Integer.parseInt(total),Integer.parseInt(pos));
	}
	

	
	/**
	 * 流水号自动加1，当超过最大流水号后第一位变成A，当周末的再次超过最大数，则第一位变成B，当第一位到达Z后，更改第二位从A开始
	 */
	public static String serialPlus(String nownum,int length){
		Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(nownum);
        //如果存在非数字情况
        if( !isNum.matches() ){
        	boolean isFind = false;
        	String t_num;
        	for(int i =0 ;i< length;i++){
        		char oneChar = nownum.charAt(i);
        		//忽略非数字，直接采用剩余数字进行增加
        		if(oneChar > 57){
        			continue;
        		}
				t_num = nownum.substring(i);
        		int i_num = Integer.parseInt(t_num) + 1;
        		//当剩余数字大于最大数时
        		if(i_num >= Math.pow(10, length-i)){
        			//前面如果小于Z，则前面的字母加1
            		if(nownum.charAt(i-1) < 90){
            			return nownum.substring(0,i-1) + (char) (nownum.charAt(i - 1) + 1) + String.format("%0"+ (length -i) + "d", 1);
            		}else{
            			//如果前面的字母已经到Z了，那么后面的采用A
            			return nownum.substring(0,i) + "A" + String.format("%0"+ (length -i - 1) + "d", 1);
            		}
            	}else{
            		//当剩余数字没超过最大数时，直接加1
            		return nownum.substring(0,i) + String.format("%0"+ (length -i) + "d", i_num);
            	}
        	}
        	if(!isFind){
        		char oneChar = nownum.charAt(nownum.length() -1);
        		oneChar += 1;
        		//如果小于Z
        		if(oneChar <= 90)
        			return nownum.substring(0,nownum.length() -2) + oneChar;
			}
        }else{
        	//如果全部为数字。则进行加一
        	int i_num = Integer.parseInt(nownum) + 1;
        	if(i_num >= Math.pow(10, length)){
        		//超过最大数，则采用A0001
        		return "A" + String.format("%0"+ (length - 1) + "d", 1);
        	}else{
        		return String.format("%0"+ length + "d", i_num);
        	}
        }
        return "";
	}
	
	/**
	 * 防止XSS攻击，对于用户输入的参数值展现在HTML正文中或者属性值中的情况，需要将用户输入做如下的转码(即将`< > ' " &` 转成HTML实体)：
	 */
	public static String changeXSSInfo(String info){
		/*String result = null;
		if(isEmpty(info))
			return info;
		
		result = info.replaceAll("<", "&lt;").replaceAll(">", "&gt;").
				replaceAll("'", "&#39;").replaceAll("\"", "&quot;");*/
		
		return info;
		
	}
	
	/**
	 * 防止XSS攻击，对于用户输入的参数值展现在HTML正文中或者属性值中的情况，需要将用户输入做如下的转码(即将`< > ' " &` 转成HTML实体)：
	 */
	public static String changeSqlXSS(String info){
		String result;
		if(isEmpty(info))
			return info;
		
		result = info.replaceAll("<", "＜").replaceAll(">", "＞").
				replaceAll("'", "‘").replaceAll("\"", "“").
				replaceAll("#", "＃").replaceAll(",", "，");
		
		return result;
		
	}
	
	/**
     * 利用正则表达式判断字符串是否是数字
     */
    public static boolean isNumeric(String str){
           Pattern pattern = Pattern.compile("[0-9]*");
           Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
    
    /**
     * 验证日期格式
     * @return {boolean}
     */
    public static boolean isValidDate(String date,String format) {
    	
    	if(isBlank(date))
    		return true;
    	
    	SimpleDateFormat sf = new SimpleDateFormat(format);
    	
    	try {
    		sf.setLenient(false);
    		sf.parse(date);
    		return true;
    	}catch(Exception e) {
    		return false;
    	}
    }
    
	/**
	 * 根据参数进行替换
	 */
	public static String replaceMsg(String xxnr,String...params){
		if(params != null && params.length > 0){
			for(int i=0;i<params.length;i++){
				if(params[i]!=null)
					xxnr = xxnr.replace("#{" + i +"}", params[i]);
			}
		}
		return xxnr;
	}
	
	/**
	 * 根据分号分割字符串加入集合
	 */
	public static List<String> splitMsg(List<String> list ,String xxnr){
		return splitMsg(list, xxnr, "；|;");
	}
	
	/**
	 * 根据分隔符分割字符串加入集合
	 */
	public static List<String> splitMsg(List<String> list , String xxnr, String fgf){
		if(isNotBlank(xxnr)){
			String[] split = xxnr.split(fgf);
            for (String s : split) {
                if (isNotBlank(s)) {
                    list.add(s);
                }
            }
        }
		return list;
	}
	
	/**
	 * 根据反射进行替换
	 */
	public static String replaceMsgByObject(String xxnr,Object object){
		try {
			for (int i = 0; i < xxnr.length(); i++) {
				int start = xxnr.indexOf("#{{", i);
				if(start != -1){
					int end = xxnr.indexOf("}}", start);
					String substring = xxnr.substring(start+3, end);
					//替换
					Method method = object.getClass().getMethod("get"+ substring);
					String value = (String) method.invoke(object);
					xxnr = xxnr.replace("#{{"+substring+"}}", value);
					i = end;
				}else{
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return xxnr;
	}
	
	/**
	 * 字节数组转16进制
	 * @param bytes 需要转换的byte数组
	 * @param separator 间隔字符
	 * @return  转换后的Hex字符串
	 */
	public static String bytesToHex(byte[] bytes,String separator) {
		if(bytes == null)
			return null;
		StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
            if (separator != null) {
                sb.append(separator);
            }
        }
		return sb.toString();
	}

	/**
	 * 按照指定长度转换byte到ascII
	 */
	public static String bytesToAscii(byte[] bytes, int offset, int dateLen) {
		if ((bytes == null) || (bytes.length == 0) || (offset < 0) || (dateLen <= 0)) {
			return null;
		}
		if ((offset >= bytes.length) || (bytes.length - offset < dateLen)) {
			return null;
		}

		String asciiStr = null;
		byte[] data = new byte[dateLen];
		System.arraycopy(bytes, offset, data, 0, dateLen);
		try {
			asciiStr = new String(data, "ISO8859-1");
		} catch (Exception e) {
			//Log.e("bytesToAscii",e.toString());
		}
		return asciiStr;
	}

	/**
	 * 把字符串转换成ascii,组成byte数组返回
	 */
	public static byte[] stringToByte(String str,int limitLength){
		char[] chars = str.toCharArray();
		if(limitLength < 0 )
			limitLength = chars.length;
		byte[] result = new byte[limitLength];
		for (int i = 0; i < chars.length; i++) {
			if(i >= limitLength)
				break;
			result[i] = (byte) chars[i];
		}
		return result;
	}

	/**
	 * 把int 转换成 byte ,在Java里 低位数据在最开始，
	 * 所以需要 高低位转换
	 */
	public static byte[] int2byteFromHigh(int i_num,int length){
		int b_length = Math.min(length, 4);
		byte[] result = new byte[b_length];
		for (int i = b_length -1;i>=0;i--){
			result[i] = (byte) (i_num >> 8 * (b_length - i - 1)  & 0xFF);
		}
		return result;
	}

	/**
	 * 把int 转换成 byte ,在Java里 低位数据在最开始，
	 * 所以需要 高低位转换
	 */
	public static byte[] int2byteFromLow(int i_num,int length){
		int b_length = Math.min(length, 4);
		byte[] result = new byte[b_length];
		for (int i=0;i<b_length;i++){
			result[i] = (byte) (i_num >> 8 * i & 0xFF);
		}
		return result;
	}

	/**
	 * 把 byte 转换成  int,在Java里 低位数据在最开始，
	 */
	public static int byte2int(byte[] b_num){
		int length = b_num.length;
		int result = 0;
		for(int i =0 ;i<length;i++){
			result = ((b_num[i]&0xFF) << (length-1 -i)*8)|result;
		}
		return result;
	}
	
	/**
	 * 根据格式化标准返回 相应的开始时间和结束时间，现主要用于自定义的SQL语句查询替换用
	 * 默认间隔为 1,一天或者1个月或者一年
	 * @param ymdFormat 时间格式 YYYY 代表年   YYYY-MM 代表月  YYYY-MM-dd 代表日
	 */
	public static List<String> getNearDate(String ymdFormat)
	{
		return getNearDate(ymdFormat,1);
	}
	
	/**
	 * 根据格式化标准返回 相应的开始时间和结束时间，现主要用于自定义的SQL语句查询替换用
	 * @param ymdFormat 时间格式 YYYY 代表年   YYYY-MM 代表月  YYYY-MM-dd 代表日
	 * @param space 代表间隔时间
	 */
	public static List<String> getNearDate(String ymdFormat,int space){

		SimpleDateFormat sf = new SimpleDateFormat(ymdFormat);
		Calendar cal = Calendar.getInstance();
		List<String> result = new ArrayList<>();

        switch (ymdFormat) {
            case "YYYY":
                //月份是从0开始的，2其实代表小于3月的时候
                if (cal.get(Calendar.MONTH) < 2) {
                    cal.add(Calendar.YEAR, -1);
                    result.add(sf.format(cal.getTime()));
                    cal.add(Calendar.YEAR, -space + 1);
                    result.add(0, sf.format(cal.getTime()));
                } else {
                    result.add(sf.format(cal.getTime()));
                    cal.add(Calendar.YEAR, -space + 1);
                    result.add(0, sf.format(cal.getTime()));
                }
                break;
            case "YYYY-MM":
            case "YYYYMM":
                if (cal.get(Calendar.DAY_OF_MONTH) <= 10) {
                    cal.add(Calendar.MONTH, -1);
                    result.add(sf.format(cal.getTime()));
                    cal.add(Calendar.MONTH, -space + 1);
                    result.add(0, sf.format(cal.getTime()));
                } else {
                    result.add(sf.format(cal.getTime()));
                    cal.add(Calendar.MONTH, -space + 1);
                    result.add(0, sf.format(cal.getTime()));
                }
                break;
            case "YYYY-MM-dd":
            case "YYYYMMdd":
                result.add(sf.format(cal.getTime()));
                cal.add(Calendar.DATE, -space + 1);
                result.add(0, sf.format(cal.getTime()));
                break;
            default:
                return null;
        }
		return result;
	}
	public static boolean isObjectBank(Object cs) {
        return cs == null || "".equals(cs);
	}
	public static boolean isNotObjectBank(Object cs) {
		return !isObjectBank(cs);
	}
}