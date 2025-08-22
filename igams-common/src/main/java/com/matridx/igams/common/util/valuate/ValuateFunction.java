package com.matridx.igams.common.util.valuate;

import com.matridx.igams.common.util.valuate.function.Function;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;

import java.util.Date;

/**
 * ValuateFunction
 *
 * @author hmz
 * @version 1.0
 * @date 2024/7/17 上午10:54
 */
public class ValuateFunction {

    /**
     * isNotBlank(a)
     * 判断 a 是否不为null且不为””
     */
    public static final Function isNotBlank = new Function() {
        public Object execute(Object... parameters) throws Exception {
            boolean result = false;
            try {
                result  = StringUtil.isNotBlank((String) parameters[0]);
            } finally {
                return result;
            }
        }
    };
    /**
     * isNotBlank(a)
     * 判断 a 是否为null或””
     */
    public static final Function isBlank = new Function() {
        public Object execute(Object... parameters) throws Exception {
            return StringUtil.isBlank((String) parameters[0]);
        }
    };

    /**
     * substring(string,beginIndex,endIndex)
     * @param      beginIndex   the beginning index, inclusive.
     * @param      endIndex     the ending index, exclusive.
     * @return     the specified substring.
     */
    public static final Function substring = new Function() {
        public Object execute(Object... parameters) throws Exception {
            return ((String)parameters[0]).substring(Integer.parseInt(parameters[1].toString()),Integer.parseInt(parameters[2].toString()));
        }
    };
    /**
     *
     * @return  the length of the sequence of characters represented by this
     *          object.
     */
    public static final Function length = new Function() {
        public Object execute(Object... parameters) throws Exception {
            return ((String)parameters[0]).length();
        }
    };
    /*
     * @param   str   the substring to search for.
     * @return  the index of the first occurrence of the specified substring,
     *          or {@code -1} if there is no such occurrence.
     */
    public static final Function indexOf = new Function() {
        public Object execute(Object... parameters) throws Exception {
            return ((String)parameters[0]).indexOf((String)parameters[1]);
        }
    };
    /**
     * 拼接字符串
     */
    public static final Function append = new Function() {
        public Object execute(Object... parameters) throws Exception {
            String result = "";
            for (Object parameter : parameters) {
                result += (String)parameter;
            }
            return result;
        }
    };
    /**
     * 比较字符串
     */
    public static final Function equals = new Function() {
        public Object execute(Object... parameters) throws Exception {
            return parameters[0].equals(parameters[1]);
        }
    };
    /**
     * replace(String str, String target, String replacement)
     */
    public static final Function replace = new Function() {
        public Object execute(Object... parameters) throws Exception {
            return ((String) parameters[0]).replace((String) parameters[1], (String) parameters[2]);
        }
    };

    /**
     * replaceAll(String str, String regex, String replacement)
     */
    public static final Function replaceAll = new Function() {
        public Object execute(Object... parameters) throws Exception {
            return ((String) parameters[0]).replaceAll((String) parameters[1], (String) parameters[2]);
        }
    };
    /**
     * 日期Str转日期date
     * dateParse(a,日期格式)
     */
    public static final Function dateParse = new Function() {
        public Object execute(Object... parameters) throws Exception {
            return DateUtils.parseDate((String)parameters[1],(String)parameters[0]);
        }
    };
    /**
     * 日期date转日期Str
     * dateFormat(a,日期格式)
     */
    public static final Function dateFormat = new Function() {
        public Object execute(Object... parameters) throws Exception {
            return DateUtils.format((Date) parameters[0],(String)parameters[1]);
        }
    };
    /**
     * List转数组并添加分割符
     * stringJoin(delimiter,elements)
     */
    public static final Function stringJoin = new Function() {
        public Object execute(Object... parameters) throws Exception {
            return String.join((CharSequence)parameters[0], (Iterable<? extends CharSequence>) parameters[1]);

        }
    };
}
