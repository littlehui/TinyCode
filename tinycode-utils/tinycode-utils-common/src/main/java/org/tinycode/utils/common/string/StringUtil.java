package org.tinycode.utils.common.string;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.tinycode.utils.common.obj.ObjectUtil;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author littlehui
 * @date 2022/4/13 14:38
 * @return
 */
public class StringUtil  {

    private static final Pattern chinesePattern = Pattern.compile("[\\u4e00-\\u9fa5]");

    /**
     * 是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return (s == null || "".equals(s.trim()));
    }

    /**
     * 是否包含中文
     *
     * @param s
     * @return
     */
    public static boolean isContainZH(String s) {
        return isEmpty(s) || chinesePattern.matcher(s).find();
    }

    public static String trim(String s) {
        return s != null ? s.trim() : null;
    }

    public static String filterRegexChar(String s) {
        return s;
    }

    /**
     * 字符串是否存在中文.
     * @param str
     * @return
     * @author littlehui
     * @date 2012-9-21 下午03:24:33
     */
    public static boolean isExistZH(String str) {
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            return true;
        }
        return false;
    }

    public static String toString(Throwable e) {
        UnsafeStringWriter w = new UnsafeStringWriter();
        PrintWriter p = new PrintWriter(w);
        p.print(e.getClass().getName());
        if (e.getMessage() != null) {
            p.print(": " + e.getMessage());
        }

        p.println();

        String var3;
        try {
            e.printStackTrace(p);
            var3 = w.toString();
        } finally {
            p.close();
        }

        return var3;
    }

    /**
     * 字符串第一个字母大写.
     * @param s
     * @return
     * @author littlehui
     * @date 2012-9-27 下午03:10:46
     */
    public static String upperFirstChar(String s) {
        if (!ObjectUtil.isEmpty(s)) {
            return String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1);
        } else {
            return s;
        }
    }

    /**
     * 字符串第一个字母小写.
     * @param s
     * @return
     * @author littlehui
     * @date 2012-9-27 下午03:10:58
     */
    public static String lowerFirstChar(String s) {
        if (!ObjectUtil.isEmpty(s)) {
            return String.valueOf(s.charAt(0)).toLowerCase() + s.substring(1);
        } else {
            return s;
        }
    }

    /**
     * 获取第一个大写字母.
     * @param s
     * @return
     * @author littlehui
     * @date 2012-10-23 上午09:06:18
     */
    public static String getUpperFirstChar(String s) {
        if (!ObjectUtil.isEmpty(s)) {
            return String.valueOf(s.charAt(0)).toUpperCase();
        } else {
            return s;
        }
    }

    /**
     * 四舍五入并去掉科学计数法, 默认小数点2位.
     * @param value String, double, Double, BigDecimal
     * @return
     * @author littlehui
     * @date 2012-7-28 下午03:44:05
     */
    public static String toNuSicen(Object value) {
        return toNuSicen(value, 2);
    }

    /**
     * 四舍五入并去掉科学计数法.
     * @param value String, double, Double, BigDecimal
     * @param precision 保留几位小数
     * @return
     * @author littlehui
     * @date 2012-7-28 下午03:47:25
     */
    public static String toNuSicen(Object value, int precision) {
        Object result = "";
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(precision);
        df.setMaximumFractionDigits(precision);
        df.setGroupingUsed(false);
        if(ObjectUtil.isEmpty(value)){
            return df.format(0);
        }else if(value instanceof BigDecimal){
            result = value;
        }else if(value instanceof String){
            result = new BigDecimal(String.valueOf(value));
        }else if(value instanceof Number){
            result = Double.parseDouble(value.toString());
        }else{
            throw new IllegalArgumentException(value + "need extends Number or String");
        }
        return df.format(result);
    }

    /**
     * 获取不区分大小写正则Pattern<br>
     * .可代表换行符.<br>
     * 正则表达式的特殊字符,依然代表普通字符
     * @param value
     * @return
     * @author littlehui
     * @date 2012-10-9 下午03:32:33
     */
    public static Pattern getInsensitivePattern(String value){
        return Pattern.compile(ValueUtil.getString(value).replaceAll("([\\+\\-\\&\\.\\|\\!\\(\\)\\{\\}\\[\\]\\^\\\"\\~\\*\\?\\:])","\\\\$1"), Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
    }

    /**
     * 截取字符串的尾部.
     * @param value 源字符串
     * @param separator 分割符
     * @return
     * @author littlehui
     * @date 2013-2-19 下午6:17:23
     */
    public static String subLast(String value, String separator){
        return StringUtils.substringAfterLast(value, separator);
    }

    /**
     * 截取字符串的头部.
     * @param value 源字符串
     * @param separator 分割符
     * @return
     * @author littlehui
     * @date 2013-2-19 下午6:17:23
     */
    public static String subBefore(String value, String separator){
        return StringUtils.substringBeforeLast(value, separator);
    }

    /**
     * 字符串数组是否包含str.
     *
     * @param arry
     *            字符串数组
     * @param str
     *            目标字符
     * @return [true:包含,false:不包含]
     * @author qingwu
     * @date 2013-2-19 下午6:17:23
     */
    public static boolean contans(String[] arry, String str) {
        for (int i = 0; i < arry.length; i++) {
            if(arry[i].equals(str)){
                return true;
            }
        }
        return false;
    }

    /**
     * map[String,Object]转为map[String,String].
     *
     * @param sMap
     *            源map
     * @return <String,String>类型的参数map
     * @author qingwu
     * @date 2013-4-8 下午15:00:00
     */
    public static Map<String, String> castMap(Map<String, Object> sMap) {
        Map<String, String> map = new HashMap<String, String>();
        Set<String> set = sMap.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object obj = sMap.get(key);
            map.put(key, ValueUtil.getString(obj));
        }
        return map;
    }

    /**
     * 指定随机长度字符串.
     * @param length
     * @return
     */
    public static String randString(int length){
        if(length < 10){
            throw new RuntimeException("length must greater than 10 : " + length);
        }
        String result = "";
        String str = "qwertyuiopasdfghjklzxcvbnm1234567890";
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            result += str.charAt(rand.nextInt(36));
        }
        return result;
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNoneEmpty(final String... ss) {
        if (ArrayUtils.isEmpty(ss)) {
            return false;
        } else {
            String[] var1 = ss;
            int var2 = ss.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                String s = var1[var3];
                if (isEmpty(s)) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isAnyEmpty(final String... ss) {
        return !isNoneEmpty(ss);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEquals(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return true;
        } else {
            return s1 != null && s2 != null ? s1.equals(s2) : false;
        }
    }

    public static boolean isNumeric(String str, boolean allowDot) {
        if (str != null && !str.isEmpty()) {
            boolean hasDot = false;
            int sz = str.length();

            for(int i = 0; i < sz; ++i) {
                if (str.charAt(i) == '.') {
                    if (hasDot || !allowDot) {
                        return false;
                    }

                    hasDot = true;
                } else if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

}
