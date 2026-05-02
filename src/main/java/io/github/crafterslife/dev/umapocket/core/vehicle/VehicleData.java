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

import org.jspecify.annotations.NullMarked;

/**
 * 乗り物のデータを保持するレコード。
 *
 * @param serializedData Base64エンコードされたシリアライズデータ
 * @param worldName      ワールド名
 * @param x              X座標
 * @param y              Y座標
 * @param z              Z座標
 * @param yaw            Yaw角度
 * @param pitch          Pitch角度
 */
@NullMarked
public record VehicleData(
        String serializedData,
        String worldName,
        double x,
        double y,
        double z,
        float yaw,
        float pitch
) { }

