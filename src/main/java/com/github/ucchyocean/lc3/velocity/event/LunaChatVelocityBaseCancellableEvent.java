/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.velocity.event;

/**
 * 基底のキャンセル可能イベントクラス
 * @author ucchy
 */
public class LunaChatVelocityBaseCancellableEvent extends LunaChatVelocityBaseEvent {

    private boolean isCancelled;

    /**
     * コンストラクタ
     * @param channelName チャンネル名
     */
    public LunaChatVelocityBaseCancellableEvent(String channelName) {
        super(channelName);
    }

    /**
     * イベントがキャンセルされたかどうかをかえす
     * @see org.bukkit.event.Cancellable#isCancelled()
     */
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * イベントをキャンセルするかどうかを設定する
     * @see org.bukkit.event.Cancellable#setCancelled(boolean)
     */
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}
