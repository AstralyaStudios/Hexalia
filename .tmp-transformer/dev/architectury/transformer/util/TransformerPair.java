/*
 * This file is licensed under the MIT License, part of architectury-transformer.
 * Copyright (c) 2020, 2021, 2022 architectury
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.architectury.transformer.util;

import com.google.common.base.MoreObjects;
import com.google.gson.JsonObject;
import dev.architectury.transformer.Transformer;
import org.jetbrains.annotations.Nullable;

public class TransformerPair {
    private final Class<? extends Transformer> clazz;
    @Nullable
    private final JsonObject properties;
    
    public TransformerPair(Class<? extends Transformer> clazz, @Nullable JsonObject properties) {
        this.clazz = clazz;
        this.properties = properties;
    }
    
    public Class<? extends Transformer> getClazz() {
        return clazz;
    }
    
    @Nullable
    public JsonObject getProperties() {
        return properties;
    }
    
    public Transformer construct() {
        try {
            Transformer transformer = clazz.getConstructor().newInstance();
            transformer.supplyProperties(MoreObjects.firstNonNull(properties, new JsonObject()));
            return transformer;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}