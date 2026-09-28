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

import com.google.common.base.MoreObjects;
import com.google.gson.JsonObject;
import dev.architectury.transformer.transformers.base.ClassEditTransformer;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;

/**
 * Remap architectury injectables calls to the injected classes.
 */
public class RemapInjectables implements ClassEditTransformer {
    public static final String EXPECT_PLATFORM_LEGACY = "Lme/shedaniel/architectury/ExpectPlatform;";
    public static final String EXPECT_PLATFORM_LEGACY2 = "Lme/shedaniel/architectury/annotations/ExpectPlatform;";
    public static final String EXPECT_PLATFORM = "Ldev/architectury/injectables/annotations/ExpectPlatform;";
    public static final String EXPECT_PLATFORM_TRANSFORMED = "Ldev/architectury/injectables/annotations/ExpectPlatform$Transformed;";
    public static final String PLATFORM_ONLY_LEGACY = "Lme/shedaniel/architectury/annotations/PlatformOnly;";
    public static final String PLATFORM_ONLY = "Ldev/architectury/injectables/annotations/PlatformOnly;";
    private static final String ARCHITECTURY_TARGET = "dev/architectury/injectables/targets/ArchitecturyTarget";
    private String uniqueIdentifier = null;
    
    @Override
    public void supplyProperties(JsonObject json) {
        uniqueIdentifier = json.has(BuiltinProperties.UNIQUE_IDENTIFIER) ?
                json.getAsJsonPrimitive(BuiltinProperties.UNIQUE_IDENTIFIER).getAsString() : null;
    }
    
    @Override
    public ClassNode doEdit(String name, ClassNode node) {
        if (!isInjectInjectables()) return node; // no need to edit the class
        String newName = MoreObjects.firstNonNull(uniqueIdentifier, getUniqueIdentifier()) + "/PlatformMethods";
        ClassNode newNode = new ClassNode();
        Remapper remapper = new Remapper() {
            @Override
            public String map(String internalName) {
                if (ARCHITECTURY_TARGET.equals(internalName)) {
                    return newName;
                }
                
                return internalName;
            }
        };
        ClassVisitor cv = new ClassRemapper(newNode, remapper);
        node.accept(cv);
        return newNode;
    }
    
    public static String getUniqueIdentifier() {
        return System.getProperty(BuiltinProperties.UNIQUE_IDENTIFIER);
    }
    
    public static boolean isInjectInjectables() {
        return System.getProperty(BuiltinProperties.INJECT_INJECTABLES, "true").equals("true");
    }
    
    public static String[] getClasspath() {
        return System.getProperty(BuiltinProperties.COMPILE_CLASSPATH, "true").split(File.pathSeparator);
    }
}