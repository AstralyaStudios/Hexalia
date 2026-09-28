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
import dev.architectury.refmapremapper.RefmapRemapper;
import dev.architectury.refmapremapper.remapper.MappingsRemapper;
import dev.architectury.refmapremapper.remapper.Remapper;
import dev.architectury.refmapremapper.remapper.SimpleReferenceRemapper;
import dev.architectury.transformer.Transform;
import dev.architectury.transformer.input.FileAccess;
import dev.architectury.transformer.transformers.base.AssetEditTransformer;
import dev.architectury.transformer.transformers.base.edit.TransformerContext;
import dev.architectury.transformer.util.Logger;
import net.fabricmc.mapping.tree.*;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.Manifest;

/**
 * Adds mixins to the MixinConfigs field in the manifest, and remap intermediary refmap to srg.
 */
public class FixForgeMixin implements AssetEditTransformer {
    private TinyTree srg;
    private Map<String, Mapped> srgMap;
    
    @Override
    public void doEdit(TransformerContext context, FileAccess output) throws Exception {
        String productionNamespace = System.getProperty(BuiltinProperties.FORGE_PRODUCTION_NAMESPACE, "srg");
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        List<String> mixinConfigs = new ArrayList<>();
        String refmap = System.getProperty(BuiltinProperties.REFMAP_NAME);
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
                        if (json.has("package") && (hasMixins || hasClient || hasServer)) {
                            mixinConfigs.add(trimmedPath);
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        });
        if (!mixinConfigs.isEmpty()) {
            Logger.debug("Found mixin config(s): " + String.join(",", mixinConfigs));
        }
        if (context.canModifyAssets()) {
            output.modifyFile("META-INF/MANIFEST.MF", bytes -> {
                try {
                    Logger.debug("Injecting MixinConfigs into /META-INF/MANIFEST.MF");
                    Manifest manifest = new Manifest(new ByteArrayInputStream(bytes));
                    manifest.getMainAttributes().putValue("MixinConfigs", String.join(",", mixinConfigs));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    manifest.write(stream);
                    return stream.toByteArray();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (context.canAppendArgument()) {
            for (String config : mixinConfigs) {
                context.appendArgument("--mixin.config", config);
            }
        } else {
            Logger.error("Failed to inject mixin config!");
        }
        if (refmap != null) {
            Logger.debug("Remapping refmap from intermediary to srg: " + refmap);
            output.modifyFile(refmap, bytes -> {
                try {
                    readSrg();
                    
                    SimpleReferenceRemapper referenceRemapper = new SimpleReferenceRemapper(new SimpleReferenceRemapper.Remapper() {
                        @Override
                        @Nullable
                        public String mapClass(String value) {
                            Mapped classDef = srgMap.get(value);
                            return classDef == null ? null : classDef.getName(productionNamespace);
                        }
                        
                        @Override
                        @Nullable
                        public String mapMethod(@Nullable String className, String methodName, String methodDescriptor) {
                            String methodId = "M " + methodName + " " + methodDescriptor;
                            Mapped mapped;
                            if (className != null) {
                                mapped = srgMap.get(className + " " + methodId);
                                if (mapped != null) return mapped.getName(productionNamespace);
                            }
                            mapped = srgMap.get(methodId);
                            return mapped == null ? null : mapped.getName(productionNamespace);
                        }
                        
                        @Override
                        @Nullable
                        public String mapField(@Nullable String className, String fieldName, String fieldDescriptor) {
                            String fieldId = "F " + fieldName + " " + fieldDescriptor;
                            Mapped mapped;
                            if (className != null) {
                                mapped = srgMap.get(className + " " + fieldId);
                                if (mapped != null) return mapped.getName(productionNamespace);
                            }
                            mapped = srgMap.get(fieldId);
                            return mapped == null ? null : mapped.getName(productionNamespace);
                        }
                    }) {
                        @Override
                        public String remapSimple(String key, String value) {
                            String remapped = super.remapSimple(key, value);
                            Logger.debug("Remapped refmap value " + value + " -> " + remapped);
                            return remapped;
                        }
                    };
                    
                    return RefmapRemapper.remap(new Remapper() {
                        @Override
                        @Nullable
                        public MappingsRemapper remapMappings() {
                            return className -> referenceRemapper;
                        }
                        
                        @Override
                        @Nullable
                        public Map.Entry<String, @Nullable MappingsRemapper> remapMappingsData(String data) {
                            if (Objects.equals(data, "named:intermediary")) {
                                return new AbstractMap.SimpleEntry<>("searge", remapMappings());
                            }
                            
                            return null;
                        }
                    }, new String(bytes, StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
    
    private void readSrg() throws IOException {
        if (srg == null) {
            Path srgMappingsPath = Paths.get(System.getProperty(BuiltinProperties.MAPPINGS_WITH_SRG));
            try (BufferedReader reader = Files.newBufferedReader(srgMappingsPath)) {
                srg = TinyMappingFactory.loadWithDetection(reader);
            }
        }
        
        if (srgMap == null) {
            srgMap = new HashMap<>();
            for (ClassDef srgClass : srg.getClasses()) {
                String intermediary = srgClass.getName("intermediary");
                srgMap.put(intermediary, srgClass);
                
                for (MethodDef method : srgClass.getMethods()) {
                    String methodId = "M " + method.getName("intermediary") + " " + method.getDescriptor("intermediary");
                    srgMap.put(intermediary + " " + methodId, method);
                    
                    if (!srgMap.containsKey(methodId)) {
                        srgMap.put(methodId, method);
                    }
                }
                
                for (FieldDef field : srgClass.getFields()) {
                    String fieldId = "F " + field.getName("intermediary") + " " + field.getDescriptor("intermediary");
                    srgMap.put(intermediary + " " + fieldId, field);
                    
                    if (!srgMap.containsKey(fieldId)) {
                        srgMap.put(fieldId, field);
                    }
                }
            }
        }
    }
}