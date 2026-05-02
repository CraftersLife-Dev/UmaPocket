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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * 乗り物データのファイル読み書きを担当するクラス。
 * 各プレイヤーのデータはUUID.jsonとして個別のファイルに保存される。
 */
@NullMarked
public final class VehicleFileManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path vehiclesFolder;
    private final Logger logger;

    /**
     * VehicleFileManagerのコンストラクタ。
     *
     * @param dataFolder プラグインのデータフォルダ
     * @param logger     ロガー
     */
    public VehicleFileManager(final Path dataFolder, final Logger logger) {
        this.vehiclesFolder = dataFolder.resolve("vehicles");
        this.logger = logger;
        this.ensureDirectoryExists();
    }

    /**
     * プレイヤーのデータファイルパスを取得する。
     *
     * @param playerUuid プレイヤーのUUID
     * @return データファイルのパス
     */
    private Path getPlayerDataFile(final UUID playerUuid) {
        return this.vehiclesFolder.resolve(playerUuid + ".json");
    }

    /**
     * 保存フォルダが存在することを確認する。
     */
    private void ensureDirectoryExists() {
        try {
            Files.createDirectories(this.vehiclesFolder);
        } catch (final IOException e) {
            this.logger.log(Level.SEVERE, "乗り物データフォルダの作成に失敗しました。", e);
        }
    }

    /**
     * プレイヤーに保存されたデータがあるかチェックする。
     *
     * @param playerUuid プレイヤーのUUID
     * @return 保存されたデータがある場合はtrue
     */
    public boolean exists(final UUID playerUuid) {
        return Files.exists(this.getPlayerDataFile(playerUuid));
    }

    /**
     * プレイヤーのデータをJSONファイルから読み込む。
     *
     * @param playerUuid プレイヤーのUUID
     * @return 読み込んだデータ、存在しない場合はnull
     */
    public @Nullable VehicleData load(final UUID playerUuid) {
        final Path dataFile = this.getPlayerDataFile(playerUuid);
        if (!Files.exists(dataFile)) {
            return null;
        }

        try (Reader reader = Files.newBufferedReader(dataFile)) {
            return GSON.fromJson(reader, VehicleData.class);
        } catch (final IOException e) {
            this.logger.log(Level.SEVERE, "プレイヤー " + playerUuid + " の乗り物データの読み込みに失敗しました。", e);
            return null;
        }
    }

    /**
     * プレイヤーのデータをJSONファイルに保存する。
     *
     * @param playerUuid  プレイヤーのUUID
     * @param vehicleData 保存する乗り物データ
     */
    public void save(final UUID playerUuid, final VehicleData vehicleData) {
        final Path dataFile = this.getPlayerDataFile(playerUuid);
        try (Writer writer = Files.newBufferedWriter(dataFile)) {
            GSON.toJson(vehicleData, writer);
        } catch (final IOException e) {
            this.logger.log(Level.SEVERE, "プレイヤー " + playerUuid + " の乗り物データの保存に失敗しました。", e);
        }
    }

    /**
     * プレイヤーのデータファイルを削除する。
     *
     * @param playerUuid プレイヤーのUUID
     */
    public void delete(final UUID playerUuid) {
        final Path dataFile = this.getPlayerDataFile(playerUuid);
        try {
            Files.deleteIfExists(dataFile);
        } catch (final IOException e) {
            this.logger.log(Level.SEVERE, "プレイヤー " + playerUuid + " の乗り物データの削除に失敗しました。", e);
        }
    }
}

