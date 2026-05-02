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
package io.github.crafterslife.dev.umapocket.infrastructure.translation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * {@link net.kyori.adventure.translation.TranslationStore} を識別するための一意な名前を格納するアノテーション。
 */
@Retention(RetentionPolicy.RUNTIME)
@NullMarked
public @interface TranslationStoreName {

    /**
     * 名前空間。
     *
     * @return 名前空間
     * @see Key#namespace()
     */
    String namespace() default "";

    /**
     * 値。
     *
     * @return 値
     * @see Key#value()
     */
    String value();
}
