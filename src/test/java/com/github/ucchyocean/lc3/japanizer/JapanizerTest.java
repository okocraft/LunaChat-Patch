package com.github.ucchyocean.lc3.japanizer;

import com.github.ucchyocean.lc3.japanize.JapanizeType;
import net.okocraft.lunachat.japanize.Japanizer;
import junit.framework.TestCase;

import java.util.LinkedHashMap;
import java.util.Map;

public class JapanizerTest extends TestCase {

    public void testJapanize() {
        // See https://github.com/ucchyocean/LunaChat/issues/231
        var original = "https://example.com tesuto example.com test neititoneitio neitiotoneiti";
        var dictionary = new LinkedHashMap<>(Map.of("test", "テスト", "neitio", "ネイティオ", "neiti", "ネイティ"));
        Japanizer.sortDictionary(dictionary);

        var expected = "https://example.com てすと example.com テスト ネイティとネイティオ ネイティオとネイティ";

        assertEquals(expected, Japanizer.japanize(original, JapanizeType.KANA, dictionary));

        var invalid = "test ＜1＞";
        assertEquals("", Japanizer.japanize(invalid, JapanizeType.KANA, dictionary));

        var halfKana = "ﾃｽﾄ";
        assertEquals("", Japanizer.japanize(halfKana, JapanizeType.KANA, dictionary));
    }
}
