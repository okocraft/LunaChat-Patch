/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3;

import com.github.ucchyocean.lc3.bridge.LuckPermsBridge;
import com.github.ucchyocean.lc3.channel.ChannelManager;
import com.github.ucchyocean.lc3.velocity.JapanizeCommandVelocity;
import com.github.ucchyocean.lc3.velocity.LunaChatCommandVelocity;
import com.github.ucchyocean.lc3.velocity.MessageCommandVelocity;
import com.github.ucchyocean.lc3.velocity.ReplyCommandVelocity;
import com.github.ucchyocean.lc3.velocity.VelocityEventListener;
import com.github.ucchyocean.lc3.velocity.VelocityEventSender;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

/**
 * LunaChatのVelocity実装
 * @author ucchy
 */
public class LunaChatVelocity implements PluginInterface {

    private static LunaChatVelocity instance;

    public final ProxyServer server;
    public final Logger logger;
    public final Path dataDirectory;

    private HashMap<String, String> history;
    private LunaChatConfig config;
    private ChannelManager manager;
    private UUIDCacheData uuidCacheData;
    private LunaChatLogger normalChatLogger;

    private LuckPermsBridge luckperms;

    @Inject
    public LunaChatVelocity(@NotNull ProxyServer server, @NotNull Logger logger,
                            @DataDirectory Path dataDirectory) {
        instance = this;
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onEnable(ProxyInitializeEvent e) {
        LunaChat.setPlugin(this);
        LunaChat.setMode(LunaChatMode.VELOCITY);

        // 初期化
        config = new LunaChatConfig(getDataFolder(), getPluginJarFile());
        uuidCacheData = new UUIDCacheData(getDataFolder());
        Messages.initialize(new File(getDataFolder(), "messages"), getPluginJarFile(), config.getLang());
        history = new HashMap<String, String>();

        manager = new ChannelManager();
        normalChatLogger = new LunaChatLogger("==normalchat");

        // チャンネルチャット無効なら、デフォルト発言先をクリアする
        if ( !config.isEnableChannelChat() ) {
            manager.removeAllDefaultChannels();
        }

        if ( this.server.getPluginManager().isLoaded("luckperms") ) {
            luckperms = LuckPermsBridge.load();
        }

        // コマンド登録
        this.server.getCommandManager().register(
                this.server.getCommandManager().metaBuilder("lunachat").aliases("lc", "ch").plugin(this).build(),
                new LunaChatCommandVelocity()
        );
        this.server.getCommandManager().register(
                this.server.getCommandManager().metaBuilder("tell").aliases("msg", "message", "t", "w").plugin(this).build(),
                new MessageCommandVelocity()
        );
        this.server.getCommandManager().register(
                this.server.getCommandManager().metaBuilder("reply").aliases("r").plugin(this).build(),
                new ReplyCommandVelocity()
        );
        this.server.getCommandManager().register(
                this.server.getCommandManager().metaBuilder("japanize").aliases("jp").plugin(this).build(),
                new JapanizeCommandVelocity()
        );

        // リスナー登録
        this.server.getEventManager().register(this, new VelocityEventListener(this));

        // イベント実行クラスの登録
        LunaChat.setEventSender(new VelocityEventSender(this.server));

        // プラグインチャンネル登録
        this.server.getChannelRegistrar().register(MinecraftChannelIdentifier.from(LunaChat.PMC_MESSAGE));
    }

    /**
     * LunaChatのインスタンスを返す
     * @return LunaChat
     */
    public static LunaChatVelocity getInstance() {
        return instance;
    }

    /**
     * コンフィグを返す
     * @return コンフィグ
     */
    public LunaChatConfig getConfig() {
        return config;
    }

    /**
     * プライベートメッセージの受信履歴を記録する
     * @param reciever 受信者
     * @param sender 送信者
     */
    protected void putHistory(String reciever, String sender) {
        history.put(reciever, sender);
    }

    /**
     * プライベートメッセージの受信履歴を取得する
     * @param reciever 受信者
     * @return 送信者
     */
    protected String getHistory(String reciever) {
        return history.get(reciever);
    }

    /**
     * このプラグインのJarファイル自身を示すFileクラスを返す。
     * @return Jarファイル
     * @see PluginInterface#getPluginJarFile()
     */
    @Override
    public File getPluginJarFile() {
        return new File("./plugins/LunaChat.jar"); // TODO
    }

    /**
     * LunaChatConfigを取得する
     * @return LunaChatConfig
     * @see PluginInterface#getLunaChatConfig()
     */
    @Override
    public LunaChatConfig getLunaChatConfig() {
        return config;
    }

    /**
     * LunaChatAPIを取得する
     * @return LunaChatAPI
     * @see PluginInterface#getLunaChatAPI()
     */
    @Override
    public LunaChatAPI getLunaChatAPI() {
        return manager;
    }

    /**
     * 通常チャット用のロガーを返す
     * @return normalChatLogger
     */
    @Override
    public LunaChatLogger getNormalChatLogger() {
        return normalChatLogger;
    }

    /**
     * オンラインのプレイヤー名一覧を取得する
     * @return オンラインのプレイヤー名一覧
     */
    @Override
    public Set<String> getOnlinePlayerNames() {
        Set<String> list = new TreeSet<>();
        for ( Player p : this.server.getAllPlayers() ) {
            list.add(p.getUsername());
        }
        return list;
    }
    // okocraft start - add name stream
    @Override
    public java.util.stream.Stream<String> getOnlinePlayerNameStream() {
        return this.server.getAllPlayers().stream()
                .map(Player::getUsername)
                .sorted(java.util.Comparator.comparing(String::length).reversed());
    }
    // okocraft end

    /**
     * このプラグインのログを記録する
     * @param level ログレベル
     * @param msg ログメッセージ
     */
    @Override
    public void log(Level level, String msg) {
        if (level == Level.INFO) {
            this.logger.info(msg);
        } else        if (level == Level.WARNING) {
            this.logger.warn(msg);
        } else if (level == Level.SEVERE) {
            this.logger.error(msg);
        }
    }

    /**
     * UUIDキャッシュデータを取得する
     * @return UUIDキャッシュデータ
     * @see PluginInterface#getUUIDCacheData()
     */
    @Override
    public UUIDCacheData getUUIDCacheData() {
        return uuidCacheData;
    }

    /**
     * 非同期タスクを実行する
     * @param task タスク
     * @see PluginInterface#runAsyncTask(Runnable)
     */
    @Override
    public void runAsyncTask(Runnable task) {
        this.server.getScheduler().buildTask(this, task).schedule();
    }

    /**
     * LuckPerms連携クラスを取得する
     * @return LuckPerms連携クラス
     */
    public LuckPermsBridge getLuckPerms() {
        return luckperms;
    }
    // okocraft start - Set the default setting of japanize to false for those who do not use Japanese as a client language
    public boolean isUsingJapanese(String playerName) {
        Player player = this.server.getPlayer(playerName).orElse(null);
        return player != null && java.util.Locale.JAPANESE.equals(player.getEffectiveLocale());
    }
    // okocraft end

    @Override
    public File getDataFolder() {
        return this.dataDirectory.toFile();
    }
}
