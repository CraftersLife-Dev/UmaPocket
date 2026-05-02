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
package io.github.crafterslife.dev.umapocket.core.vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import java.nio.file.Path;
import java.util.logging.Logger;
import org.jspecify.annotations.NullMarked;

/**
 * プレイヤーの乗り物をシリアライズして保存・復元するファサードクラス。
 * 実際の処理は {@link VehicleSerializer} と {@link VehicleFileManager} に委譲する。
 */
@NullMarked
public final class VehicleStorage {

    private final VehicleSerializer serializer;
    private final VehicleFileManager fileManager;
    private final Logger logger;

    /**
     * VehicleStorageのコンストラクタ。
     *
     * @param dataFolder プラグインのデータフォルダ
     * @param logger     ロガー
     */
    public VehicleStorage(final Path dataFolder, final Logger logger) {
        this.serializer = new VehicleSerializer();
        this.fileManager = new VehicleFileManager(dataFolder, logger);
        this.logger = logger;
    }

    /**
     * プレイヤーの乗り物をシリアライズして保存する。
     *
     * @param player 対象のプレイヤー
     */
    public void saveVehicle(final Player player) {
        final Entity vehicle = player.getVehicle();
        if (vehicle == null || vehicle instanceof Player) {
            return;
        }

        // プレイヤーを乗り物から降ろす
        vehicle.eject();

        // エンティティをシリアライズ
        final VehicleData vehicleData = this.serializer.serialize(vehicle);

        // 乗り物エンティティを削除
        vehicle.remove();

        // プレイヤー個別のファイルに保存
        this.fileManager.save(player.getUniqueId(), vehicleData);
        this.logger.info("プレイヤー " + player.getName() + " の乗り物を保存しました。");
    }

    /**
     * 保存された乗り物を復元してプレイヤーを乗せる。
     *
     * @param player 対象のプレイヤー
     */
    public void restoreVehicle(final Player player) {
        final VehicleData vehicleData = this.fileManager.load(player.getUniqueId());
        if (vehicleData == null) {
            return;
        }

        final World world = Bukkit.getWorld(vehicleData.worldName());
        if (world == null) {
            this.logger.warning("ワールド " + vehicleData.worldName() + " が見つかりません。");
            return;
        }

        // エンティティをデシリアライズ
        final Entity vehicle = this.serializer.deserialize(vehicleData, world);
        final Location location = this.serializer.getLocation(vehicleData, world);

        // エンティティをスポーンさせてプレイヤーを乗せる
        vehicle.spawnAt(location);
        vehicle.addPassenger(player);

        // データファイルを削除
        this.fileManager.delete(player.getUniqueId());

        this.logger.info("プレイヤー " + player.getName() + " の乗り物を復元しました。");
    }

    /**
     * プレイヤーに保存された乗り物があるかチェックする。
     *
     * @param player 対象のプレイヤー
     * @return 保存された乗り物がある場合はtrue
     */
    public boolean hasStoredVehicle(final Player player) {
        return this.fileManager.exists(player.getUniqueId());
    }
}
