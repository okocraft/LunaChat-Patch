/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.member;

import com.github.ucchyocean.lc3.LunaChatVelocity;
import com.velocitypowered.api.permission.Tristate;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * ChannelMemberのVelocity-ConsoleCommandSource実装
 * @author ucchy
 */
public class ChannelMemberVelocityConsole extends ChannelMemberVelocity {

    private CommandSource sender;

    /**
     * コンストラクタ
     * @param sender ConsoleのCommandSource
     */
    public ChannelMemberVelocityConsole(CommandSource sender) {
        this.sender = sender;
    }

    /**
     * VelocityのPlayerを取得する
     * @return 常にnullが返される
     * @see ChannelMemberVelocity#getPlayer()
     */
    @Override
    public Player getPlayer() {
        return null;
    }

    /**
     * 発言者が今いるサーバーを取得する
     * @return 常にnullが返される
     * @see ChannelMemberVelocity#getServer()
     */
    @Override
    public ServerConnection getServer() {
        return null;
    }

    /**
     * 発言者がオンラインかどうかを取得する
     * @return 常にtrueが返される
     * @see ChannelMember#isOnline()
     */
    @Override
    public boolean isOnline() {
        return true;
    }

    /**
     * 発言者名を取得する
     * @return 発言者名
     * @see ChannelMember#getName()
     */
    @Override
    public String getName() {
        return "Console";
    }

    /**
     * 発言者の表示名を取得する
     * @return 発言者の表示名
     * @see ChannelMember#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return "Console";
    }

    /**
     * プレフィックスを返す
     * @return 常に空文字列
     * @see ChannelMember#getPrefix()
     */
    @Override
    public String getPrefix() {
        return null;
    }

    /**
     * サフィックスを返す
     * @return 常に空文字列
     * @see ChannelMember#getSuffix()
     */
    @Override
    public String getSuffix() {
        return null;
    }

    /**
     * 発言者にメッセージを送信する
     * @param message メッセージ
     * @see ChannelMember#sendMessage(String)
     */
    @Override
    public void sendMessage(String message) {
        if ( message == null || message.isEmpty() ) return;
        sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
    }

    /**
     * メッセージを送る
     * @param message 送るメッセージ
     * @see ChannelMember#sendMessage(BaseComponent[])
     */
    public void sendMessage(BaseComponent[] message) {
        if ( message == null || message.length == 0 ) return;
        sender.sendMessage(BungeeComponentSerializer.get().deserialize(message));
    }

    /**
     * 指定されたパーミッションノードの権限を持っているかどうかを取得する
     * @param node パーミッションノード
     * @return 権限を持っているかどうか
     * @see ChannelMember#hasPermission(String)
     */
    @Override
    public boolean hasPermission(String node) {
        return sender.hasPermission(node);
    }

    /**
     * 指定されたパーミッションノードが定義されているかどうかを取得する
     * @param node パーミッションノード
     * @return 定義を持っているかどうか
     * @see ChannelMember#isPermissionSet(String)
     */
    @Override
    public boolean isPermissionSet(String node) {
        return this.sender.getPermissionValue(node) != Tristate.UNDEFINED;
    }

    /**
     * 指定されたメッセージの内容を発言する
     * @param message メッセージ
     * @see ChannelMember#chat(String)
     */
    public void chat(String message) {
        LunaChatVelocity.getInstance().server.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("<" + getName() + ">" + message));
    }

    /**
     * IDを返す
     * @return 名前をそのまま返す
     * @see ChannelMember#toString()
     */
    @Override
    public String toString() {
        return getName();
    }
}
