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

package dev.architectury.transformer.transformers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.architectury.transformer.Transform;
import dev.architectury.transformer.input.FileAccess;
import dev.architectury.transformer.transformers.base.AssetEditTransformer;
import dev.architectury.transformer.transformers.base.edit.TransformerContext;
import dev.architectury.transformer.util.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class RuntimeMixinRefmapDetector implements AssetEditTransformer {
    @Override
    public void doEdit(TransformerContext context, FileAccess output) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        output.handle((path, bytes) -> {
            String trimmedPath = Transform.trimSlashes(path);
            if (trimmedPath.endsWith(".json") && !trimmedPath.contains("/") && !trimmedPath.contains("\\")) {
                Logger.debug("Checking whether " + path + " is a mixin config.");
                try (InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(bytes))) {
                    JsonObject json = gson.fromJson(reader, JsonObject.class);
                    if (json != null) {
                        boolean hasMixins = json.has("mixins") && json.get("mixins").isJsonArray();
                        boolean hasClient = json.has("client") && json.get("client").isJsonArray();
                        boolean hasServer = json.has("server") && json.get("server").isJsonArray();
                        if (json.has("package") && json.has("refmap") && (hasMixins || hasClient || hasServer)) {
                            Logger.error("Mixin Config [%s] contains 'refmap', please remove it so it works in development environment!", trimmedPath);
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        });
    }
}
