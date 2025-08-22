package com.matridx.springboot.util.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 正则匹配工具
 * @author zhangqy
 * 
 */
public class RegexUtil {
    public static long parseRecordsNumInHtml(String content, String regex)
        throws Exception {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        String recordContent;

        if (matcher.find()) {
            recordContent = matcher.group();
        } else {
            throw new Exception("找不到匹配结果: content:" + content + " regex:" +
                regex);
        }

        pattern = Pattern.compile("[\\d]+"); //提取数字
        matcher = pattern.matcher(recordContent);

        StringBuilder recordBuffer = new StringBuilder();

        while (matcher.find()) {
            recordBuffer.append(matcher.group());
        }

        if (recordBuffer.toString().isEmpty()) {
            throw new Exception("找不到匹配结果: content:" + content + " regex:" +
                regex);
        }

        return Long.parseLong(recordBuffer.toString());
    }
    
    public static boolean isContentMatche(String content,String regex) {
    	Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        return matcher.matches();
    }
    
    public void testMatches(){
    	System.out.println(RegexUtil.matches("2015-02-16", RegexUtil.REGEX_DATE_1));
    	System.out.println(RegexUtil.matches("2015-2-6", RegexUtil.REGEX_DATE_1));
    	System.out.println(RegexUtil.matches("2005/02/16", RegexUtil.REGEX_DATE_1));
    	System.out.println(RegexUtil.matches("2005/2/16", RegexUtil.REGEX_DATE_1));
    	System.out.println(RegexUtil.matches("2015/2/45", RegexUtil.REGEX_DATE_1));
    	System.out.println(RegexUtil.matches("15/2/5", RegexUtil.REGEX_DATE_1));
    	System.out.println(RegexUtil.matches("2005.2.16", RegexUtil.REGEX_DATE_1));
    	System.out.println(RegexUtil.matches("2005.2.16", RegexUtil.REGEX_DATE_2));
    	System.out.println();
    	System.out.println(RegexUtil.matches("201.5", "\\-?[1-9]\\d+(\\.\\d+)?"));
    	System.out.println(RegexUtil.matches("201", RegexUtil.REGEX_NUMBER));
    	System.out.println(RegexUtil.matches("-1.1565", RegexUtil.REGEX_NUMBER));
    	System.out.println(RegexUtil.matches("-201", RegexUtil.REGEX_NUMBER));
    	System.out.println(RegexUtil.matches("201f", RegexUtil.REGEX_NUMBER));
    	System.out.println(RegexUtil.matches("-20E3", RegexUtil.REGEX_NUMBER));
    }
    
    /**
     * 正则表达式匹配
     * @param str 要进行匹配的字符串
     * @param regex 正则表达式
     */
	public static boolean matches(String str, String regex) {
        return Pattern.matches(regex, str);
	}
    
	/**
	 * 正负整数或正负浮点数
	 */
	public static final String REGEX_NUMBER = "\\-?\\d+(\\.\\d+)?";
	/** 
	 * 日期横杠或斜杠分割
	 */
	public static final String REGEX_DATE_1 = "^((((19){1}|(20){1})\\d{2})|\\d{2})[-][0,1]?\\d{1}[-][0-3]?\\d{1}$";
	/**
	 * 日期点分割
	 */
	public static final String REGEX_DATE_2 = "^((((19){1}|(20){1})\\d{2})|\\d{2}).[0,1]?\\d{1}.[0-3]?\\d{1}$";
	/** 
	 * 日期斜杠分割
	 */
	public static final String REGEX_DATE_3 = "^((((19){1}|(20){1})\\d{2})|\\d{2})[/][0,1]?\\d{1}[/][0-3]?\\d{1}$";
}
