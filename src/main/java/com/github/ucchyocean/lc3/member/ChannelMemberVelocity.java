/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.member;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;

/**
 * チャンネルメンバーのVelocity抽象クラス
 * @author ucchy
 */
public abstract class ChannelMemberVelocity extends ChannelMember {

    /**
     * VelocityのPlayerを取得する
     * @return Player
     */
    public abstract Player getPlayer();

    /**
     * 発言者が今いるサーバーを取得する
     * @return サーバー
     */
    public abstract ServerConnection getServer();

    /**
     * 発言者が今いるサーバーのサーバー名を取得する
     * @return サーバー名
     */
    public String getServerName() {
        ServerConnection server = getServer();
        if ( server != null ) {
            return server.getServerInfo().getName();
        }
        return "";
    }

    /**
     * 発言者が今いるワールド名を返す
     * @return 常に空文字列が返される
     * @see ChannelMember#getWorldName()
     */
    @Override
    public String getWorldName() {
        return "";
    }

    /**
     * CommandSenderから、ChannelMemberを作成して返す
     * @param sender
     * @return ChannelMember
     */
    public static ChannelMemberVelocity getChannelMemberVelocity(Object sender) {
        if ( sender == null || !(sender instanceof CommandSource) ) return null;
        if ( sender instanceof Player ) {
            return new ChannelMemberVelocityPlayer(((Player)sender).getUniqueId());
        } else {
            // Player以外のCommandSenderは、ConsoleSenderしかないはず
            return new ChannelMemberVelocityConsole((CommandSource)sender);
        }
    }
}
