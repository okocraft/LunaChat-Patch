/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.velocity;

import com.github.ucchyocean.lc3.command.LunaChatReplyCommand;
import com.github.ucchyocean.lc3.member.ChannelMember;
import com.velocitypowered.api.command.SimpleCommand;

/**
 * Replyコマンドの処理クラス（Velocity実装）
 * @author ucchy
 */
public class ReplyCommandVelocity implements SimpleCommand {

    private LunaChatReplyCommand command;

    public ReplyCommandVelocity() {
        command = new LunaChatReplyCommand();
    }

    @Override
    public void execute(Invocation invocation) {
        command.execute(ChannelMember.getChannelMember(invocation.source()), "r", invocation.arguments());
    }
}
