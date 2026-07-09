package com.triabin.ideasy_server.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 类描述：数字相关工具类
 *
 * @author Triabin
 * @date 2026-01-14 17:12:35
 */
public class MyNumberUtils {

    /**
     * 方法描述：获取指定正数范围指定长度的随机元素数组
     *
     * @param min    元素最小值，≥0
     * @param max    元素最大值，≥0
     * @param length 数组长度
     * @return {@link int[]}
     * @date 2026-01-14 17:19:28
     */
    public static int[] genRandomArray(int min, int max, int length) {
        if (min < 0 || max < 0 || min > max || length <= 1) {
            throw new IllegalArgumentException("参数错误，min、max不能小于0，min不能小于max，length不能小于1");
        }
        int[] arr = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(max - min + 1) + min;
        }
        return arr;
    }

    /**
     * 方法描述：获取指定正数范围指定长度的随机元素集合
     *
     * @param min    元素最小值，≥0
     * @param max    元素最大值，≥0
     * @param length 数组长度
     * @return {@link List}
     * @date 2026-01-16 12:16:09
     */
    public static List<Integer> genRandomList(int min, int max, int length) {
        if (min < 0 || max < 0 || min > max || length <= 1) {
            throw new IllegalArgumentException("参数错误，min、max不能小于0，min不能小于max，length不能小于1");
        }
        List<Integer> list = new ArrayList<>(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            list.add(random.nextInt(max - min + 1) + min);
        }
        return list;
    }

    // 创建映射中文数字字符到阿拉伯数字
    private static final Map<Character, Character> CN_NUM_MAP = new HashMap<>();
    // 创建映射到中文数值级别单位到数值
    private static final Map<Character, Integer> CN_NUM_UNIT_MAP = new HashMap<>();
    // 中文数字基础验证正则
    private static Pattern CN_NUM_PTN = null;
    // 中文数字连续数字检查正则（中文数字小数点前不存在除了零以外的连续的2个及以上数字）
    private static final Pattern CN_NUM_SEQ_PTN = Pattern.compile("[一二两三四五六七八九]{2,}");
    // 中文数字0顺序检查正则（中文数字小数点前不存在0在数字后面的这种情况）
    private static final Pattern CN_NUM_0SEQ_PTN = Pattern.compile("[一二两三四五六七八九]零");
    static {
        CN_NUM_MAP.put('零', '0');
        CN_NUM_MAP.put('一', '1');
        CN_NUM_MAP.put('二', '2');
        CN_NUM_MAP.put('两', '2');
        CN_NUM_MAP.put('三', '3');
        CN_NUM_MAP.put('四', '4');
        CN_NUM_MAP.put('五', '5');
        CN_NUM_MAP.put('六', '6');
        CN_NUM_MAP.put('七', '7');
        CN_NUM_MAP.put('八', '8');
        CN_NUM_MAP.put('九', '9');

        CN_NUM_UNIT_MAP.put('个', 1);
        CN_NUM_UNIT_MAP.put('十', 10);
        CN_NUM_UNIT_MAP.put('百', 100);
        CN_NUM_UNIT_MAP.put('千', 1000);
        CN_NUM_UNIT_MAP.put('万', 10000);
        CN_NUM_UNIT_MAP.put('亿', 100000000);
        // 更大的单位自行扩展，注意大于兆以后得单位数值已经超出int范围，需要改为更大的数据类型，单位名称大于一个字符那种更不在考虑范围（例如恒河沙[10^52]、无量[10^68]、大数[10^72]之类的）

        CN_NUM_PTN = Pattern.compile("(负(的)?)?([" +
                CN_NUM_UNIT_MAP.keySet().stream().map(Object::toString).collect(Collectors.joining()).replace("个", "") +
                CN_NUM_MAP.keySet().stream().map(Object::toString).collect(Collectors.joining()) +
                "]+)");
    }

    /**
     * 方法描述：将中文数字转为阿拉伯数字（亿级）
     *
     * @param cnNum {@link String} 中文数字字符串，负数以“负”或“负的”开头
     * @return {@code int} 阿拉伯数字
     * @date 2024-12-19 14:09:36
     */
    public static int cnNumToArabic(String cnNum) {
        // 实现思路：按照单位，一个级别一个级别的计算，因为每个级别的计算方法相同，所以遇到更大的单位前可以直接套用
        // 1、变量定义
        // 当前单位级别
        int currUnit = 1;
        // 当前单位下的倍率（例如万级下的几百几十几万）
        int currFactor = 0;
        // 当前倍率的单位级别（万级以上出现的小于当前级别的级别单位）
        int currFacUnit = 1;
        // 结果值
        int val = 0;
        // 中文数字转阿拉伯数字临时存放
        StringBuilder temp = new StringBuilder();

        // 2、验证
        Matcher matcher = CN_NUM_PTN.matcher(cnNum);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("“" + cnNum + "”中文数字格式不正确");
        }
        int e0 = cnNum.indexOf("零两");
        if (e0 > -1) {
            throw new IllegalArgumentException("“" + cnNum + "”中文数字格式不正确：索引：" + e0 + "，零两");
        }
        String pureNum = matcher.group(3);
        // 如果在小数点后，忽略这两项验证
        Matcher seqMatcher = CN_NUM_SEQ_PTN.matcher(pureNum);
        Matcher seq0Matcher = CN_NUM_0SEQ_PTN.matcher(pureNum);
        if (seqMatcher.find()) {
            throw new IllegalArgumentException("“" + cnNum + "”中文数字格式不正确，索引：" + seqMatcher.start() + "，" + seqMatcher.group());
        }
        if (seq0Matcher.find()) {
            throw new IllegalArgumentException("“" + cnNum + "”中文数字格式不正确，索引：" + seq0Matcher.start() + "，" + seq0Matcher.group());
        }

        // 3、从后往前，逐个字符处理
        for (int i = pureNum.length() - 1; i >= 0; i--) {
            char ch = pureNum.charAt(i);
            if (CN_NUM_MAP.containsKey(ch)) {
                // 如果是数字，则转为相应的阿拉伯数字并前插
                temp.insert(0, CN_NUM_MAP.get(ch));
                continue;
            }
            if (CN_NUM_UNIT_MAP.containsKey(ch)) {
                Integer unit = CN_NUM_UNIT_MAP.get(ch);
                if (!temp.isEmpty()) {
                    currFactor += Integer.parseInt(temp.toString()) * currFacUnit;
                    temp.delete(0, temp.length());
                }
                // 如果是单位，则判断是否大于当前单位级别
                if (unit < currUnit) {
                    // 小于当前单位级别，则增加倍率单位级别
                    currFacUnit = unit;
                } else {
                    // 大于当前单位级别，通过倍率计算当前单位级别的值并累加到结果值中，重置其余辅助参数并改变当前单位级别，并更行当前单位级别以进入下一个单位级别的计算
                    val += currFactor * currUnit;
                    currUnit = unit;
                    currFactor = 0;
                    currFacUnit = 1;
                }
            }
        }
        if (!temp.isEmpty()) {
            currFactor += Integer.parseInt(temp.toString()) * currFacUnit;
        }
        if (currFactor != 0) {
            val += currFactor * currUnit;
        }
        // 处理10开头的
        if (pureNum.startsWith("十")) {
            val += 10 * (currUnit == 10 ? 1 : currUnit);
        }
        // 通过正则匹配结果判断是否为负数
        String negative = matcher.group(1);
        return (negative != null && !negative.isEmpty()) ? -val : val;
    }

    /**
     * 方法描述：将整数转为中文数字
     *
     * @param num 要转换的整数
     * @return {@link String} 中文数字
     * @date 2026-03-30 16:51:59
     */
    public static String arabicToCnNum(int num) {
        // 处理特殊情况：0
        if (num == 0) {
            return "零";
        }
        // 处理负数
        boolean isNegative = num < 0;
        long absNum = Math.abs((long) num);
        // 将数字转换为字符串
        String numStr = String.valueOf(absNum);
        String result = numStr2CnNum(numStr);
        // 添加负号
        if (isNegative) {
            result = "负" + result;
        }
        return result;
    }

    /**
     * 方法描述：将小数转为中文（注意：如果小数点后面全是0，将忽略）
     *
     * @param num 浮点型小数
     * @return {@link String} 中文数字
     * @date 2026-03-30 17:47:22
     */
    public static String arabicToCnNum(double num) {
        // 处理特殊情况：0
        if (num == 0) {
            return "零";
        }
        // 处理负数
        boolean isNegative = num < 0;
        BigDecimal bigNum = new BigDecimal(num).abs();
        String numStr = bigNum.toString();
        String result = numStr2CnNum(numStr);
        if (isNegative) {
            result = "负" + result;
        }
        return result;
    }

    /**
     * 方法描述：将数字字符串转为中文数字公共方法
     *
     * @param numStr {@link String} 数字字符串（非负）
     * @return {@link String} 中文数字
     * @date 2026-03-30 17:48:17
     */
    private static String numStr2CnNum(String numStr) {
        String[] strItems = numStr.split("\\.");
        String intNumStr = strItems[0];
        String decNumStr = null;
        if (strItems.length > 1) {
            decNumStr = strItems[1];
        }
        // 中文数字单位
        String[] units = {"", "十", "百", "千"};
        String[] bigUnits = {"", "万", "亿", "兆"};
        String[] digits = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        StringBuilder result = new StringBuilder();

        // 处理整数部分
        int length = intNumStr.length();
        // 从最高位开始处理
        for (int i = 0; i < length; i++) {
            int digit = intNumStr.charAt(i) - '0';
            // 当前位的单位索引（个十百千）
            int unitIndex = (length - i - 1) % 4;
            // 当前大单位索引（万/亿）
            int bigUnitIndex = (length - i - 1) / 4;
            // 处理当前位的数字
            if (digit != 0) {
                // 添加数字（"一十"通常简化为"十"）
                if (!(digit == 1 && unitIndex == 1 && i == 0)) {
                    result.append(digits[digit]);
                }
                result.append(units[unitIndex]);
            } else {
                // 当前位是0，添加"零"但需要去重
                if (!result.isEmpty() && result.charAt(result.length() - 1) != '零') {
                    result.append(digits[0]);
                }
            }
            // 添加大单位（万、亿）
            if (unitIndex == 0 && bigUnitIndex > 0) {
                // 去掉末尾可能的"零"
                while (!result.isEmpty() && result.charAt(result.length() - 1) == '零') {
                    result.deleteCharAt(result.length() - 1);
                }
                // 添加大单位
                result.append(bigUnits[bigUnitIndex]);
            }
        }
        // 清理多余的"零"
        String finalResult = result.toString();
        // 去掉末尾的"零"
        while (finalResult.endsWith("零")) {
            finalResult = finalResult.substring(0, finalResult.length() - 1);
        }
        // 处理连续的"零"
        finalResult = finalResult.replaceAll("零+", "零");
        // 特殊处理"一十"开头的简写
        if (finalResult.startsWith("一十")) {
            finalResult = finalResult.substring(1);
        }

        // 处理小数部分
        if (decNumStr != null && !decNumStr.matches("0+")) {
            result = new StringBuilder(finalResult);
            result.append("点");
            for (int i = 0; i < decNumStr.length(); i++) {
                result.append(decNumStr.charAt(i));
            }
        }
        finalResult = result.toString();
        // 去掉末尾的"零"
        while (finalResult.endsWith("零")) {
            finalResult = finalResult.substring(0, finalResult.length() - 1);
        }
        return finalResult;
    }
}
