/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.velocity;

import com.github.ucchyocean.lc3.event.EventResult;
import com.github.ucchyocean.lc3.event.EventSenderInterface;
import com.github.ucchyocean.lc3.member.ChannelMember;
import com.github.ucchyocean.lc3.velocity.event.LunaChatVelocityChannelChatEvent;
import com.github.ucchyocean.lc3.velocity.event.LunaChatVelocityChannelCreateEvent;
import com.github.ucchyocean.lc3.velocity.event.LunaChatVelocityChannelMemberChangedEvent;
import com.github.ucchyocean.lc3.velocity.event.LunaChatVelocityChannelMessageEvent;
import com.github.ucchyocean.lc3.velocity.event.LunaChatVelocityChannelOptionChangedEvent;
import com.github.ucchyocean.lc3.velocity.event.LunaChatVelocityChannelRemoveEvent;
import com.github.ucchyocean.lc3.velocity.event.LunaChatVelocityPostJapanizeEvent;
import com.github.ucchyocean.lc3.velocity.event.LunaChatVelocityPreChatEvent;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.List;
import java.util.Map;

/**
 * Velocityのイベント実行クラス
 * @author ucchy
 */
public class VelocityEventSender implements EventSenderInterface {

    private final ProxyServer server;

    public VelocityEventSender(ProxyServer server) {
        this.server = server;
    }

    /**
     * チャンネルチャットのチャットイベント
     * @param channelName チャンネル名
     * @param member 発言者
     * @param originalMessage 発言内容
     * @param ngMaskedMessage 発言内容（NGマスク後）
     * @param messageFormat 発言に適用されるフォーマット
     * @return イベント実行結果
     * @see EventSenderInterface#sendLunaChatChannelChatEvent(String, ChannelMember, String, String, String)
     */
    @Override
    public EventResult sendLunaChatChannelChatEvent(String channelName, ChannelMember member, String originalMessage,
            String ngMaskedMessage, String messageFormat) {

        LunaChatVelocityChannelChatEvent event =
                new LunaChatVelocityChannelChatEvent(
                        channelName, member, originalMessage, ngMaskedMessage, messageFormat);
        event = this.server.getEventManager().fire(event).join();

        EventResult result = new EventResult();
        result.setCancelled(event.isCancelled());
        result.setNgMaskedMessage(event.getNgMaskedMessage());
        result.setMessageFormat(event.getMessageFormat());
        return result;
    }

    /**
     * チャンネル作成イベント
     * @param channelName チャンネル名
     * @param member 作成した人
     * @return イベント実行結果
     * @see EventSenderInterface#sendLunaChatChannelCreateEvent(String, ChannelMember)
     */
    @Override
    public EventResult sendLunaChatChannelCreateEvent(String channelName, ChannelMember member) {

        LunaChatVelocityChannelCreateEvent event =
                new LunaChatVelocityChannelCreateEvent(channelName, member);
        event = this.server.getEventManager().fire(event).join();

        EventResult result = new EventResult();
        result.setCancelled(event.isCancelled());
        result.setChannelName(event.getChannelName());
        return result;
    }

    /**
     * メンバー変更イベント
     * @param channelName チャンネル名
     * @param before 変更前のメンバー
     * @param after 変更後のメンバー
     * @return イベント実行結果
     * @see EventSenderInterface#sendLunaChatChannelMemberChangedEvent(String, List, List)
     */
    @Override
    public EventResult sendLunaChatChannelMemberChangedEvent(String channelName, List<ChannelMember> before,
            List<ChannelMember> after) {

        LunaChatVelocityChannelMemberChangedEvent event =
                new LunaChatVelocityChannelMemberChangedEvent(channelName, before, after);
        event = this.server.getEventManager().fire(event).join();

        EventResult result = new EventResult();
        result.setCancelled(event.isCancelled());
        return result;
    }

    /**
     * チャンネルチャットのメッセージイベント。このイベントはキャンセルできない。
     * @param channelName チャンネル名
     * @param member 発言者
     * @param message 発言内容（NGマスクやJapanizeされた後の内容）
     * @param recipients 受信者
     * @param displayName 発言者の表示名
     * @param originalMessage 発言内容（元々の内容）
     * @return イベント実行結果
     * @see EventSenderInterface#sendLunaChatChannelMessageEvent(String, ChannelMember, String, List, String, String)
     */
    @Override
    public EventResult sendLunaChatChannelMessageEvent(String channelName, ChannelMember member, String message,
            List<ChannelMember> recipients, String displayName, String originalMessage) {

        LunaChatVelocityChannelMessageEvent event =
                new LunaChatVelocityChannelMessageEvent(
                        channelName, member, message, recipients, displayName, originalMessage);
        event = this.server.getEventManager().fire(event).join();

        EventResult result = new EventResult();
        result.setMessage(event.getMessage());
        result.setRecipients(event.getRecipients());
        return result;
     }

    /**
     * オプション変更イベント
     * @param channelName チャンネル名
     * @param member オプションを変更した人
     * @param options 変更後のオプション
     * @return イベント実行結果
     * @see EventSenderInterface#sendLunaChatChannelOptionChangedEvent(String, ChannelMember, Map)
     */
    @Override
    public EventResult sendLunaChatChannelOptionChangedEvent(String channelName, ChannelMember member,
            Map<String, String> options) {

        LunaChatVelocityChannelOptionChangedEvent event =
                new LunaChatVelocityChannelOptionChangedEvent(channelName, member, options);
        event = this.server.getEventManager().fire(event).join();

        EventResult result = new EventResult();
        result.setCancelled(event.isCancelled());
        result.setOptions(event.getOptions());
        return result;
    }

    /**
     * チャンネル削除イベント
     * @param channelName チャンネル名
     * @param member 削除を実行した人
     * @return イベント実行結果
     * @see EventSenderInterface#sendLunaChatChannelRemoveEvent(String, ChannelMember)
     */
    @Override
    public EventResult sendLunaChatChannelRemoveEvent(String channelName, ChannelMember member) {

        LunaChatVelocityChannelRemoveEvent event = new LunaChatVelocityChannelRemoveEvent(channelName, member);
        event = this.server.getEventManager().fire(event).join();

        EventResult result = new EventResult();
        result.setCancelled(event.isCancelled());
        result.setChannelName(event.getChannelName());
        return result;
    }

    /**
     * Japanize変換が行われた後に呼び出されるイベント
     * @param channelName チャンネル名
     * @param member 発言したメンバー
     * @param original 変換前の文字列
     * @param japanized 変換後の文字列
     * @return イベント実行結果
     * @see EventSenderInterface#sendLunaChatPostJapanizeEvent(String, ChannelMember, String, String)
     */
    @Override
    public EventResult sendLunaChatPostJapanizeEvent(String channelName, ChannelMember member, String original,
            String japanized) {

        LunaChatVelocityPostJapanizeEvent event =
                new LunaChatVelocityPostJapanizeEvent(channelName, member, original, japanized);
        event = this.server.getEventManager().fire(event).join();

        EventResult result = new EventResult();
        result.setCancelled(event.isCancelled());
        result.setJapanized(event.getJapanized());
        return result;
    }

    /**
     * チャンネルチャットへの発言前に発生するイベント
     * @param channelName チャンネル名
     * @param member 発言したメンバー
     * @param message 発言内容
     * @return イベント実行結果
     * @see EventSenderInterface#sendLunaChatPreChatEvent(String, ChannelMember, String)
     */
    @Override
    public EventResult sendLunaChatPreChatEvent(String channelName, ChannelMember member, String message) {

        LunaChatVelocityPreChatEvent event =
                new LunaChatVelocityPreChatEvent(channelName, member, message);
        event = this.server.getEventManager().fire(event).join();

        EventResult result = new EventResult();
        result.setCancelled(event.isCancelled());
        result.setMessage(event.getMessage());
        return result;
    }
}
