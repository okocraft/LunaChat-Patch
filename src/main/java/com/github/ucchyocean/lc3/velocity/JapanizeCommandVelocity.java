/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.velocity;

import com.github.ucchyocean.lc3.command.LunaChatJapanizeCommand;
import com.github.ucchyocean.lc3.member.ChannelMember;
import com.velocitypowered.api.command.SimpleCommand;

/**
 * Japanizeコマンドの処理クラス（Velocity実装）
 * @author ucchy
 */
public class JapanizeCommandVelocity implements SimpleCommand {

    private LunaChatJapanizeCommand command;

    public JapanizeCommandVelocity() {
        command = new LunaChatJapanizeCommand();
    }

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        command.execute(ChannelMember.getChannelMember(invocation.source()), "jp", invocation.arguments());
    }

}
