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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.transformer.transformers.base.ClassEditTransformer;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.Map;

public class TransformForgeLikeToNeoForge implements ClassEditTransformer {
    private static final String FORGE_API = "net/minecraftforge/api/";
    private static final String NEOFORGE_API = "net/neoforged/api/";
    private static final String FORGE_BUS = "net/minecraftforge/eventbus/";
    private static final String NEOFORGE_BUS = "net/neoforged/bus/";
    private static final String FORGE_FML = "net/minecraftforge/fml/";
    private static final String NEOFORGE_FML = "net/neoforged/fml/";
    private static final String FORGE_BASE = "net/minecraftforge/";
    private static final String NEOFORGE_BASE = "net/neoforged/neoforge/";
    private static final String FORGE = "net/minecraftforge/common/MinecraftForge";
    private static final String NEOFORGE = "net/neoforged/neoforge/common/NeoForge";
    private final Map<String, String> extraMappings = new HashMap<>();
    
    @Override
    public void supplyProperties(JsonObject json) {
        if (json.has(BuiltinProperties.NEOFORGE_LIKE_REMAPS)) {
            JsonObject remaps = json.getAsJsonObject(BuiltinProperties.NEOFORGE_LIKE_REMAPS);
            for (Map.Entry<String, JsonElement> entry : remaps.entrySet()) {
                extraMappings.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
    }
    
    @Override
    public ClassNode doEdit(String name, ClassNode node) {
        ClassNode newNode = new ClassNode();
        node.accept(new ClassRemapper(newNode, new Remapper() {
            @Override
            public String map(String internalName) {
                if (extraMappings.containsKey(internalName)) {
                    return extraMappings.get(internalName);
                } else if (internalName.equals(FORGE)) {
                    return NEOFORGE;
                } else if (internalName.startsWith(FORGE_API)) {
                    return NEOFORGE_API + internalName.substring(FORGE_API.length());
                } else if (internalName.startsWith(FORGE_BUS)) {
                    return NEOFORGE_BUS + internalName.substring(FORGE_BUS.length());
                } else if (internalName.startsWith(FORGE_FML)) {
                    return NEOFORGE_FML + internalName.substring(FORGE_FML.length());
                } else if (internalName.startsWith(FORGE_BASE)) {
                    return NEOFORGE_BASE + internalName.substring(FORGE_BASE.length());
                }
                
                return super.map(internalName);
            }
        }));
        return newNode;
    }
}
