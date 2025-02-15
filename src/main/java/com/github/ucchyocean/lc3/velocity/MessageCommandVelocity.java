/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.velocity;

import com.github.ucchyocean.lc3.command.LunaChatMessageCommand;
import com.github.ucchyocean.lc3.member.ChannelMember;
import com.velocitypowered.api.command.SimpleCommand;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Tellコマンドの処理クラス（Velocity実装）
 * @author ucchy
 */
public class MessageCommandVelocity implements SimpleCommand {

    private LunaChatMessageCommand command;

    public MessageCommandVelocity() {
        command = new LunaChatMessageCommand();
    }

    @Override
    public void execute(Invocation invocation) {
        command.execute(ChannelMember.getChannelMember(invocation.source()), "msg", invocation.arguments());
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return CompletableFuture.completedFuture(command.onTabComplete(ChannelMember.getChannelMember(invocation.source()), "msg", invocation.arguments()));
    }
}
