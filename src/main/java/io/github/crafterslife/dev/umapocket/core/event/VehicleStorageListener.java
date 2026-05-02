/*
 * UmaPocket
 *
 * Copyright (c) 2025. すだち
 *                     Contributors []
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.crafterslife.dev.umapocket.core.event;

import io.github.crafterslife.dev.umapocket.core.utility.PluginScheduler;
import io.github.crafterslife.dev.umapocket.core.vehicle.VehicleStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;

/**
 * プレイヤーの乗り物に関するイベントリスナー。
 */
@NullMarked
public final class VehicleStorageListener implements Listener {

    private final VehicleStorage vehicleStorage;
    private final PluginScheduler scheduler;

    /**
     * VehicleStorageListenerのコンストラクタ。
     *
     * @param vehicleStorage 乗り物ストレージ
     * @param scheduler      スケジューラー
     */
    public VehicleStorageListener(final VehicleStorage vehicleStorage, final PluginScheduler scheduler) {
        this.vehicleStorage = vehicleStorage;
        this.scheduler = scheduler;
    }

    /**
     * プレイヤーがログアウトした時に乗り物を保存する。
     *
     * @param event PlayerQuitEvent
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        // プレイヤーが乗り物に乗っているかチェック
        if (player.isInsideVehicle()) {
            this.vehicleStorage.saveVehicle(player);
        }
    }

    /**
     * プレイヤーがログインした時に保存された乗り物を復元する。
     *
     * @param event PlayerJoinEvent
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        // 保存された乗り物があれば復元
        if (this.vehicleStorage.hasStoredVehicle(player)) {
            // 少し遅延させて復元（プレイヤーのスポーンが完了してから）
            this.scheduler.runTaskLater(
                    () -> this.vehicleStorage.restoreVehicle(player),
                    20L // 1秒後
            );
        }
    }
}

