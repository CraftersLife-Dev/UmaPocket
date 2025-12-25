/*
 * PaperTemplate
 *
 * Copyright (c) 2025. Namiu (うにたろう)
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
import org.bukkit.UnsafeValues;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

import java.util.Base64;

/**
 * エンティティのシリアライズ・デシリアライズを担当するクラス。
 */
@NullMarked
public final class VehicleSerializer {

    private final UnsafeValues unsafeValues;

    /**
     * VehicleSerializerのコンストラクタ。
     */
    public VehicleSerializer() {
        this.unsafeValues = Bukkit.getUnsafe();
    }

    /**
     * エンティティをシリアライズしてVehicleDataを作成する。
     *
     * @param entity シリアライズするエンティティ
     * @return シリアライズされたVehicleData
     */
    public VehicleData serialize(final Entity entity) {
        // エンティティをバイト配列にシリアライズ
        final byte[] serializedData = this.unsafeValues.serializeEntity(entity);

        // Base64でエンコードしてJSONに保存可能な形式に
        final String encodedData = Base64.getEncoder().encodeToString(serializedData);

        // 乗り物の位置情報を保存
        final Location location = entity.getLocation();
        return new VehicleData(
                encodedData,
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    /**
     * VehicleDataからエンティティをデシリアライズする。
     *
     * @param vehicleData デシリアライズするデータ
     * @param world       エンティティを生成するワールド
     * @return デシリアライズされたエンティティ
     */
    public Entity deserialize(final VehicleData vehicleData, final World world) {
        final byte[] serializedData = Base64.getDecoder().decode(vehicleData.serializedData());
        return this.unsafeValues.deserializeEntity(serializedData, world);
    }

    /**
     * VehicleDataからLocationを復元する。
     *
     * @param vehicleData 位置データを持つVehicleData
     * @param world       ワールド
     * @return 復元されたLocation
     */
    public Location getLocation(final VehicleData vehicleData, final World world) {
        return new Location(
                world,
                vehicleData.x(),
                vehicleData.y(),
                vehicleData.z(),
                vehicleData.yaw(),
                vehicleData.pitch()
        );
    }
}

