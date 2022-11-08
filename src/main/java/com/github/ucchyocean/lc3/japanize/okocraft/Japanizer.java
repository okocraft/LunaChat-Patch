/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2014
 */
package com.github.ucchyocean.lc3.japanize.okocraft;

import com.github.ucchyocean.lc3.japanize.JapanizeType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * ローマ字表記を漢字変換して返すユーティリティ
 *
 * @author ucchy
 */
public class Japanizer {

    private static final Pattern HALF_WIDTH_KANA_PATTERN = Pattern.compile("[ \\uFF61-\\uFF9F]+");
    private static final Pattern URL_PATTERN = Pattern.compile("(?:https?://)?(?:[\\w-]+\\.)+[?:\\w-]+(?:/[\\w- ./?%&=~]*)?");

    private static final Map<Integer, String> LOCK_KEY_CACHE = new ConcurrentHashMap<>();

    public static String japanize(String original, JapanizeType type, Map<String, String> dictionary) {
        // 変換不要なら空文字列を返す
        if ( type == JapanizeType.NONE || !isNeedToJapanize(original) ) {
            return "";
        }

        var temp = original;
        var lockMap = new HashMap<String, String>();
        var counter = new AtomicInteger();

        temp = URL_PATTERN.matcher(original).replaceAll(result -> {
            var count = counter.decrementAndGet();
            var lockKey = getLockKey(count);
            lockMap.put(lockKey, result.group());
            return lockKey;
        });

        counter.set(0);

        for (var keywordEntry : dictionary.entrySet()) {
            if (temp.contains(keywordEntry.getKey())) {
                var count = counter.incrementAndGet();
                var lockKey = getLockKey(count);
                lockMap.put(lockKey, keywordEntry.getValue());
                temp = temp.replace(keywordEntry.getKey(), lockKey);
            }
        }

        // カナ変換
        String japanized = YukiKanaConverter.convert(temp);

        // IME変換
        if (type == JapanizeType.GOOGLE_IME) {
            try {
                japanized = GoogleIME.convert(japanized);
            } catch (Exception e) {
                e.printStackTrace(); // I know it is dirty, but I can't get a logger, so I have no choice.
                return original; // return original
            }
        }

        for (var entry : lockMap.entrySet()) {
            japanized = japanized.replace(entry.getKey(), entry.getValue());
        }

        return japanized.trim();
    }

    private static  boolean isNeedToJapanize(@NotNull String original) {
        return original.getBytes().length == original.length() && !HALF_WIDTH_KANA_PATTERN.matcher(original).matches();
    }

    private static @NotNull String getLockKey(int digit) {
        return LOCK_KEY_CACHE.computeIfAbsent(digit, Japanizer::createDictionaryKey);
    }

    private static @NotNull String createDictionaryKey(int digit) {
        String half = Integer.toString(digit);
        StringBuilder result = new StringBuilder("＜");

        for (int index = 0; index < half.length(); index++) {
            switch ( half.charAt(index) ) {
                case '-' : result.append("‐"); break;
                case '0' : result.append("０"); break;
                case '1' : result.append("１"); break;
                case '2' : result.append("２"); break;
                case '3' : result.append("３"); break;
                case '4' : result.append("４"); break;
                case '5' : result.append("５"); break;
                case '6' : result.append("６"); break;
                case '7' : result.append("７"); break;
                case '8' : result.append("８"); break;
                case '9' : result.append("９"); break;
            }
        }
        return result.append("＞").toString();
    }
}
