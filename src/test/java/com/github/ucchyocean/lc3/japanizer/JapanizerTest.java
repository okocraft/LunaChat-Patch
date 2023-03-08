package com.github.ucchyocean.lc3.japanizer;

import com.github.ucchyocean.lc3.japanize.JapanizeType;
import com.github.ucchyocean.lc3.japanize.okocraft.Japanizer;
import junit.framework.TestCase;

import java.util.Map;

public class JapanizerTest extends TestCase {

    public void testJapanize() {
        // See https://github.com/ucchyocean/LunaChat/issues/231
        var original = "https://example.com tesuto example.com test neititoneitio neitiotoneiti";
        var dictionary = Map.of("test", "テスト", "neitio", "ネイティオ", "neiti", "ネイティ");

        var expected = "https://example.com てすと example.com テスト ネイティとネイティオ ネイティオとネイティ";

        assertEquals(expected, Japanizer.japanize(original, JapanizeType.KANA, dictionary));

        var invalid = "test ＜1＞";
        assertEquals("", Japanizer.japanize(invalid, JapanizeType.KANA, dictionary));

        var halfKana = "ﾃｽﾄ";
        assertEquals("", Japanizer.japanize(halfKana, JapanizeType.KANA, dictionary));
    }
}
