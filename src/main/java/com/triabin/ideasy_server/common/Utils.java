package com.triabin.ideasy_server.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类描述：一些零散的工具方法
 *
 * @author Triabin
 * @date 2026-08-27 12:22:14
 */
public class Utils {
    /**
     * 方法描述：中文为主的文本字数计数，其中一个英文单词、一个emoji算一个字，忽略标点符号，统计的是真正看过去的文字数
     *
     * @param text {@link String} 文本内容
     * @return {@link int} 字数计数
     * @date 2026-08-27 12:23:08
     */
    public static int cnWordCount(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        // Step 1: 英文单词数（连续字母算 1 个）
        Pattern englishPattern = Pattern.compile("[a-zA-Z]+");
        Matcher englishMatcher = englishPattern.matcher(text);
        int englishCount = 0;
        while (englishMatcher.find()) {
            englishCount++;
        }
        // Step 2: 把英文单词替换掉，剩下的文本逐 code point 判断
        String remaining = englishMatcher.reset(text).replaceAll("");
        int chineseCount = 0;
        int emojiCount = 0;
        for (int offset = 0; offset < remaining.length(); ) {
            int codePoint = remaining.codePointAt(offset);
            offset += Character.charCount(codePoint);
            if (isChinese(codePoint)) {
                chineseCount++;
            } else if (isEmoji(codePoint)) {
                emojiCount++;
            }
            // 其他（标点、数字、空格等）直接忽略
        }
        return chineseCount + englishCount + emojiCount;
    }

    /**
     * 判断是否为中文汉字（基本区 + 扩展A~F，覆盖绝大多数场景）
     */
    private static boolean isChinese(int cp) {
        return (cp >= 0x4E00 && cp <= 0x9FA5)      // 基本区
                || (cp >= 0x3400 && cp <= 0x4DBF)      // 扩展A
                || (cp >= 0x20000 && cp <= 0x2A6DF)    // 扩展B
                || (cp >= 0x2A700 && cp <= 0x2B73F)    // 扩展C
                || (cp >= 0x2B740 && cp <= 0x2B81F)    // 扩展D
                || (cp >= 0x2B820 && cp <= 0x2CEAF);   // 扩展E
    }

    /**
     * 判断是否为 emoji（覆盖常见 emoji 范围）
     */
    private static boolean isEmoji(int cp) {
        return (cp >= 0x1F600 && cp <= 0x1F64F)    // 表情符号 😀😁😂
                || (cp >= 0x1F300 && cp <= 0x1F5FF)    // 符号 & 杂项 🌍🎌
                || (cp >= 0x1F680 && cp <= 0x1F6FF)    // 交通 & 地图 🚗✈️
                || (cp >= 0x1F1E0 && cp <= 0x1F1FF)    // 国旗 🇨🇳🇺🇸
                || (cp >= 0x2600 && cp <= 0x26FF)     // 杂项符号 ☀️☁️
                || (cp >= 0x2700 && cp <= 0x27BF)     // 装饰符号 ✂️✅
                || (cp >= 0x1F900 && cp <= 0x1F9FF)    // 补充符号 🤳🤲
                || (cp >= 0x1FA00 && cp <= 0x1FA6F)    // 国际象棋等 ♟️
                || (cp >= 0x1FA70 && cp <= 0x1FAFF)    // 更多符号 🫠🫡
                || (cp == 0x200D)                      // ZWJ（零宽连接符，通常不算独立字）
                || (cp >= 0xFE00 && cp <= 0xFE0F);     // 变体选择符（通常不算独立字）
    }

}
