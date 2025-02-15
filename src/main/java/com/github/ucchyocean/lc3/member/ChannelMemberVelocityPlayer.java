/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.member;

import com.github.ucchyocean.lc3.LunaChat;
import com.github.ucchyocean.lc3.LunaChatBungee;
import com.github.ucchyocean.lc3.LunaChatVelocity;
import com.github.ucchyocean.lc3.bridge.BungeePermsBridge;
import com.github.ucchyocean.lc3.bridge.LuckPermsBridge;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * ChannelMemberのVelocity-Player実装
 * @author ucchy
 */
public class ChannelMemberVelocityPlayer extends ChannelMemberVelocity {

    private UUID id;

    /**
     * コンストラクタ
     * @param id プレイヤーID
     */
    public ChannelMemberVelocityPlayer(String id) {
        this.id = UUID.fromString(id);
    }

    /**
     * コンストラクタ
     * @param id UUID
     */
    public ChannelMemberVelocityPlayer(UUID id) {
        this.id = id;
    }

    /**
     * プレイヤー名からUUIDを取得してChannelMemberVelocityPlayerを作成して返す
     * @param nameOrUuid 名前、または、"$" + UUID
     * @return ChannelMemberVelocityPlayer
     */
    public static ChannelMember getChannelMember(String nameOrUuid) { // okocraft - support offline player
        if ( nameOrUuid.startsWith("$") ) {
            return new ChannelMemberVelocityPlayer(UUID.fromString(nameOrUuid.substring(1)));
        } else {
            Player player = LunaChatVelocity.getInstance().server.getPlayer(nameOrUuid).orElse(null);
            if ( player != null ) return new ChannelMemberVelocityPlayer(player.getUniqueId());
        }
        // okocraft start - support offline player (don't return null)
        String strUuid = LunaChat.getUUIDCacheData().getUUIDFromName(nameOrUuid);

        if (strUuid == null) {
            return new ChannelMemberOther(nameOrUuid);
        }

        UUID uuid;

        try {
            uuid = UUID.fromString(strUuid);
        } catch (IllegalArgumentException e) {
            return new ChannelMemberOther(nameOrUuid);
        }

        return new ChannelMemberVelocityPlayer(uuid);
        // okocraft end
    }

    /**
     * オンラインかどうか
     * @return オンラインかどうか
     */
    @Override
    public boolean isOnline() {
        return LunaChatVelocity.getInstance().server.getPlayer(id).isPresent();
    }

    /**
     * プレイヤー名を返す
     * @return プレイヤー名
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#getName()
     */
    @Override
    public String getName() {
        String cache = LunaChat.getUUIDCacheData().get(id.toString());
        if ( cache != null ) {
            return cache;
        }
        Player player = getPlayer();
        if ( player != null ) {
            return player.getUsername();
        }
        return id.toString();
    }

    /**
     * プレイヤー表示名を返す
     * @return プレイヤー表示名
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        Player player = getPlayer();
        if ( player != null ) {
            return player.getUsername();
        }
        return getName();
    }

    /**
     * プレフィックスを返す
     * @return プレフィックス
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#getPrefix()
     */
    @Override
    public String getPrefix() {

        LuckPermsBridge luckperms = LunaChatBungee.getInstance().getLuckPerms();
        if ( luckperms != null ) {
            return luckperms.getPlayerPrefix(id);
        }
        BungeePermsBridge bungeeperms = LunaChatBungee.getInstance().getBungeePerms();
        if ( bungeeperms != null ) {
            return bungeeperms.userPrefix(id.toString(), null, null);
        }
        return "";
    }

    /**
     * サフィックスを返す
     * @return サフィックス
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#getSuffix()
     */
    @Override
    public String getSuffix() {

        LuckPermsBridge luckperms = LunaChatBungee.getInstance().getLuckPerms();
        if ( luckperms != null ) {
            return luckperms.getPlayerSuffix(id);
        }
        BungeePermsBridge bungeeperms = LunaChatBungee.getInstance().getBungeePerms();
        if ( bungeeperms != null ) {
            return bungeeperms.userSuffix(id.toString(), null, null);
        }
        return "";
    }

    /**
     * メッセージを送る
     * @param message 送るメッセージ
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#sendMessage(String)
     */
    @Override
    public void sendMessage(String message) {
        if ( message == null || message.isEmpty() ) return;
        Player player = getPlayer();
        if ( player != null ) {
            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
        }
    }

    /**
     * メッセージを送る
     * @param message 送るメッセージ
     * @see ChannelMember#sendMessage(BaseComponent[])
     */
    public void sendMessage(BaseComponent[] message) {
        if ( message == null || message.length == 0 ) return;
        Player player = getPlayer();
        if ( player != null ) {
            player.sendMessage(BungeeComponentSerializer.get().deserialize(message));
        }
    }

    /**
     * 指定されたパーミッションノードの権限を持っているかどうかを取得する
     * @param node パーミッションノード
     * @return 権限を持っているかどうか
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#hasPermission(String)
     */
    @Override
    public boolean hasPermission(String node) {
        Player player = getPlayer();
        if ( player == null ) {
            return false;
        } else {
            return player.hasPermission(node);
        }
    }

    /**
     * 指定されたパーミッションノードが定義されているかどうかを取得する
     * @param node パーミッションノード
     * @return 定義を持っているかどうか
     * @see ChannelMember#isPermissionSet(String)
     */
    @Override
    public boolean isPermissionSet(String node) {
        Player player = getPlayer();
        if ( player != null ) {
            return player.getPermissionValue(node) != Tristate.UNDEFINED;
        }
        return false;
    }

    /**
     * 指定されたメッセージの内容を発言する
     * @param message メッセージ
     * @see ChannelMember#chat(String)
     */
    public void chat(String message) {
    }

    /**
     * IDを返す
     * @return "$" + UUID を返す
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#getID()
     */
    @Override
    public String toString() {
        return "$" + id.toString();
    }

    /**
     * Playerを取得して返す
     * @return Player
     */
    @Override
    public @Nullable Player getPlayer() {
        return LunaChatVelocity.getInstance().server.getPlayer(id).orElse(null);
    }

    /**
     * 発言者が今いるサーバーを取得する
     * @return サーバー
     * @see ChannelMemberVelocity#getServer()
     */
    @Override
    public @Nullable ServerConnection getServer() {
        Player player = getPlayer();
        if ( player != null ) {
            return player.getCurrentServer().orElse(null);
        }
        return null;
    }
}
