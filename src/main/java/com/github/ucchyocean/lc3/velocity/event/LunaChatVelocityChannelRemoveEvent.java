/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.velocity.event;

import com.github.ucchyocean.lc3.member.ChannelMember;

/**
 * チャンネル削除イベント
 * @author ucchy
 */
public class LunaChatVelocityChannelRemoveEvent extends LunaChatVelocityBaseCancellableEvent {

    private ChannelMember member;

    public LunaChatVelocityChannelRemoveEvent(String channelName, ChannelMember member) {
        super(channelName);
        this.member = member;
    }

    /**
     * チャンネルを削除した人を取得する。
     * @return チャンネルを削除したChannelMember
     */
    public ChannelMember getMember() {
        return member;
    }
}
