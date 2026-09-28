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
import dev.architectury.transformer.transformers.base.TinyRemapperTransformer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransformNeoForgeEnvironment implements TinyRemapperTransformer {
    @Override
    public List<IMappingProvider> collectMappings() throws Exception {
        return new ArrayList<>(Collections.singletonList(remapEnvironment()));
    }
    
    private IMappingProvider remapEnvironment() {
        return sink -> {
            // Stop shadow plugin from relocating this
            // net/fabricmc/api
            String fabricLoaderApiPackage = new String(new byte[]{0x6e, 0x65, 0x74, 0x2f, 0x66, 0x61, 0x62, 0x72, 0x69, 0x63, 0x6d, 0x63, 0x2f, 0x61, 0x70, 0x69}, StandardCharsets.UTF_8);
            sink.acceptClass(fabricLoaderApiPackage + "/Environment", "net/neoforged/api/distmarker/OnlyIn");
            sink.acceptClass(fabricLoaderApiPackage + "/EnvType", "net/neoforged/api/distmarker/Dist");
            sink.acceptField(
                    new IMappingProvider.Member(fabricLoaderApiPackage + "/EnvType", "SERVER", "L" + fabricLoaderApiPackage + "/EnvType" + ";"),
                    "DEDICATED_SERVER"
            );
        };
    }
}