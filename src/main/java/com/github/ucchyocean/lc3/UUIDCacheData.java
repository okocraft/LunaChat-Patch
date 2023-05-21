/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3;

import java.io.File;
import java.io.IOException;

import org.jetbrains.annotations.Nullable;

import com.github.ucchyocean.lc3.util.YamlConfig;

/**
 * UUIDのキャッシュデータを管理するクラス
 * @author ucchy
 */
public class UUIDCacheData {

    private static final String FILE_NAME = "uuidcache.yml";

    // キャッシュデータ key=UUID文字列、value=プレイヤー名
    // okocraft start - Rewrite UUIDCacheData
    // private YamlConfig cache;
    // private File dataFolder;
    private final java.nio.file.Path dataFile;
    private final com.google.common.collect.BiMap<String, String> backing;
    private final java.util.concurrent.atomic.AtomicBoolean dirty = new java.util.concurrent.atomic.AtomicBoolean();
    // okocraft end
    /**
     * コンストラクタ
     * @param dataFolder プラグインのデータ格納フォルダ
     */
    public UUIDCacheData(File dataFolder) {
        // okocraft start - Rewrite UUIDCacheData
        // cache = new YamlConfig();
        // this.dataFolder = dataFolder;
        this.dataFile = new File(dataFolder, FILE_NAME).toPath();

        com.google.common.collect.BiMap<String, String> loaded;
        try {
            loaded = net.okocraft.lunachat.UUIDCacheFile.load(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
            loaded = com.google.common.collect.HashBiMap.create();
        }
        this.backing = com.google.common.collect.Maps.synchronizedBiMap(loaded);
        // okocraft - end
    }

    /**
     * キャッシュデータを読み込む
     */
    /* okocraft - Rewrite UUIDCacheData
    public void reload() {
        File file = new File(dataFolder, FILE_NAME);
        if ( !file.exists() ) {
            // キャッシュファイルがまだ無いなら、からファイルを作成しておく。
            cache = new YamlConfig();
            try {
                cache.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        cache = YamlConfig.load(file);
    }
    */ // okocraft

    /**
     * キャッシュデータをファイルに保存する
     */
    public void save() {
        /* okocraft - Rewrite UUIDCacheData
        File file = new File(dataFolder, FILE_NAME);
        try {
            cache.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // okocraft start - Rewrite UUIDCacheData
        */
        if (dirty.getAndSet(false)) {
            try {
                net.okocraft.lunachat.UUIDCacheFile.save(dataFile, java.util.Map.copyOf(backing));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // okocraft end
    }

    /**
     * プレイヤーのUUIDとプレイヤー名を追加する。
     * @param uuid UUID
     * @param name プレイヤー名
     */
    public void put(String uuid, String name) {
        // okocraft start - Rewrite UUIDCacheData
        var current = backing.get(uuid);
        if (current == null || !current.equalsIgnoreCase(name)) {
            backing.forcePut(uuid, name);
            dirty.set(true);
        }
        // okocraft end
    }

    /**
     * プレイヤーのUUIDからプレイヤー名を取得する。
     * @param uuid UUID
     * @return プレイヤー名（キャッシュされていない場合はnullが返される）
     */
    public @Nullable String get(String uuid) {
        return backing.get(uuid); // okocraft - Rewrite UUIDCacheData
    }

    /**
     * プレイヤー名からUUIDを取得する
     * @param name プレイヤー名
     * @return UUID（キャッシュされていない場合はnullが返される）
     */
    public @Nullable String getUUIDFromName(String name) {
        if ( name == null ) return null;
        /* okocraft - Rewrite UUIDCacheData
        for ( String uuid : cache.getKeys(false) ) {
            String n = cache.getString(uuid);
            if ( name.equalsIgnoreCase(n) ) {
                return uuid;
            }
        }
        // okocraft start - Rewrite UUIDCacheData
        */
        return backing.inverse().get(name);
        // okocraft end
    }
}
