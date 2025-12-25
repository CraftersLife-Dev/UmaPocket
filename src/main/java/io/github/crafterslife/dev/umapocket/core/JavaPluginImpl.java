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
package io.github.crafterslife.dev.umapocket.core;

import io.github.crafterslife.dev.umapocket.core.event.VehicleStorageListener;
import io.github.crafterslife.dev.umapocket.core.resource.Config;
import io.github.crafterslife.dev.umapocket.core.resource.Messages;
import io.github.crafterslife.dev.umapocket.core.vehicle.VehicleStorage;
import io.github.crafterslife.dev.umapocket.infrastructure.configuration.ConfigurationHolder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * <p>プラグインのロジックやライフサイクルを管理するメインクラス。</p>
 */
@NullMarked
public final class JavaPluginImpl extends JavaPlugin {

    private final ConfigurationHolder<Config> configHolder;
    private final Messages messages;
    private @Nullable VehicleStorage vehicleStorage;

    JavaPluginImpl(
            final ConfigurationHolder<Config> configHolder,
            final Messages messages
    ) {
        this.configHolder = configHolder;
        this.messages = messages;
    }

    @Override
    public void onEnable() {
        // VehicleStorageの初期化
        this.vehicleStorage = new VehicleStorage(getDataFolder().toPath(), getLogger());

        // リスナーの登録
        Bukkit.getPluginManager().registerEvents(new VehicleStorageListener(this.vehicleStorage, this), this);
    }

    /**
     * VehicleStorageを取得する。
     *
     * @return VehicleStorage
     */
    public @Nullable VehicleStorage getVehicleStorage() {
        return this.vehicleStorage;
    }
}
