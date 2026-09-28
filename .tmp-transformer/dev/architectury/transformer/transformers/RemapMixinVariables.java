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

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemapMixinVariables implements TinyRemapperTransformer {
    private Map<String, IMappingProvider> mixinMappingCache = new HashMap<>();
    
    @Override
    public List<IMappingProvider> collectMappings() throws Exception {
        List<IMappingProvider> providers = new ArrayList<>();
        for (String path : System.getProperty(BuiltinProperties.MIXIN_MAPPINGS).split(File.pathSeparator)) {
            File mixinMapFile = Paths.get(path).toFile();
            if (mixinMapFile.exists()) {
                Logger.debug("Reading mixin mappings file: " + mixinMapFile.getAbsolutePath());
                providers.add(mixinMappingCache.computeIfAbsent(path, p ->
                        TinyUtils.createTinyMappingProvider(mixinMapFile.toPath(), "named", "intermediary"))
                );
            }
        }
        
        return providers;
    }
}