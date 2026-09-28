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

import dev.architectury.tinyremapper.IMappingProvider;
import dev.architectury.tinyremapper.TinyUtils;
import dev.architectury.transformer.transformers.base.TinyRemapperTransformer;
import dev.architectury.transformer.util.Logger;
import net.fabricmc.mapping.tree.TinyMappingFactory;
import net.fabricmc.mapping.tree.TinyTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TransformForgeEnvironment implements TinyRemapperTransformer {
    private TinyTree srg;
    private Map<String, IMappingProvider> mixinMappingCache = new HashMap<>();
    
    @Override
    public List<IMappingProvider> collectMappings() throws Exception {
        boolean fixMixins = System.getProperty(BuiltinProperties.FORGE_FIX_MIXINS, "true").equals("true");
        if (fixMixins) {
            List<IMappingProvider> providers = mapMixin();
            providers.add(remapEnvironment());
            return providers;
        } else return new ArrayList<>(Collections.singletonList(remapEnvironment()));
    }
    
    private IMappingProvider remapEnvironment() {
        return sink -> {
            // Stop shadow plugin from relocating this
            // net/fabricmc/api
            String fabricLoaderApiPackage = new String(new byte[]{0x6e, 0x65, 0x74, 0x2f, 0x66, 0x61, 0x62, 0x72, 0x69, 0x63, 0x6d, 0x63, 0x2f, 0x61, 0x70, 0x69}, StandardCharsets.UTF_8);
            sink.acceptClass(fabricLoaderApiPackage + "/Environment", "net/minecraftforge/api/distmarker/OnlyIn");
            sink.acceptClass(fabricLoaderApiPackage + "/EnvType", "net/minecraftforge/api/distmarker/Dist");
            sink.acceptField(
                    new IMappingProvider.Member(fabricLoaderApiPackage + "/EnvType", "SERVER", "L" + fabricLoaderApiPackage + "/EnvType" + ";"),
                    "DEDICATED_SERVER"
            );
        };
    }
    
    private List<IMappingProvider> mapMixin() throws IOException {
        String productionNamespace = System.getProperty(BuiltinProperties.FORGE_PRODUCTION_NAMESPACE, "srg");
        List<IMappingProvider> providers = new ArrayList<>();
        
        if (srg == null) {
            Path srgMappingsPath = Paths.get(System.getProperty(BuiltinProperties.MAPPINGS_WITH_SRG));
            try (BufferedReader reader = Files.newBufferedReader(srgMappingsPath)) {
                srg = TinyMappingFactory.loadWithDetection(reader);
            }
        }
        
        for (String path : System.getProperty(BuiltinProperties.MIXIN_MAPPINGS).split(File.pathSeparator)) {
            File mixinMapFile = Paths.get(path).toFile();
            if (mixinMapFile.exists()) {
                Logger.debug("Reading mixin mappings file: " + mixinMapFile.getAbsolutePath());
                providers.add(mixinMappingCache.computeIfAbsent(path, p -> sink -> {
                    TinyUtils.createTinyMappingProvider(mixinMapFile.toPath(), "named", "intermediary").load(new IMappingProvider.MappingAcceptor() {
                        @Override
                        public void acceptClass(String srcName, String dstName) {
                            String srgName = srg.getClasses()
                                    .stream()
                                    .filter(it -> Objects.equals(it.getName("intermediary"), dstName))
                                    .findFirst()
                                    .map(it -> it.getName(productionNamespace))
                                    .orElse(dstName);
                            sink.acceptClass(srcName, srgName);
                            Logger.debug("Remap mixin class %s -> %s", srcName, srgName);
                        }
                        
                        @Override
                        public void acceptMethod(IMappingProvider.Member method, String dstName) {
                            String srgName = srg.getClasses()
                                    .stream()
                                    .flatMap(it -> it.getMethods().stream())
                                    .filter(it -> Objects.equals(it.getName("intermediary"), dstName))
                                    .findFirst()
                                    .map(it -> it.getName(productionNamespace))
                                    .orElse(dstName);
                            sink.acceptMethod(method, srgName);
                            Logger.debug("Remap mixin method %s#%s%s -> %s", method.owner, method.name, method.desc, srgName);
                        }
                        
                        @Override
                        public void acceptField(IMappingProvider.Member field, String dstName) {
                            String srgName = srg.getClasses()
                                    .stream()
                                    .flatMap(it -> it.getFields().stream())
                                    .filter(it -> Objects.equals(it.getName("intermediary"), dstName))
                                    .findFirst()
                                    .map(it -> it.getName(productionNamespace))
                                    .orElse(dstName);
                            sink.acceptField(field, srgName);
                            Logger.debug("Remap mixin field %s#%s:%s -> %s", field.owner, field.name, field.desc, srgName);
                        }
                        
                        @Override
                        public void acceptMethodArg(IMappingProvider.Member method, int lvIndex, String dstName) {
                            
                        }
                        
                        @Override
                        public void acceptMethodVar(IMappingProvider.Member method, int lvIndex, int startOpIdx, int asmIndex, String dstName) {
                            
                        }
                    });
                }));
            }
        }
        
        return providers;
    }
}