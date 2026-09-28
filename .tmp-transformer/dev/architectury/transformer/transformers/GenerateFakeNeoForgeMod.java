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

import dev.architectury.transformer.input.FileAccess;
import dev.architectury.transformer.transformers.base.edit.TransformerContext;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Generates a fake neoforge mod.
 */
public class GenerateFakeNeoForgeMod extends AbstractFakeMod {
    @Override
    public void doEdit(TransformerContext context, FileAccess output) throws Exception {
        String fakeModId = generateModId();
        output.addFile("META-INF/mods.toml",
                "modLoader = \"javafml\"\n" +
                "loaderVersion = \"[1,)\"\n" +
                "license = \"Generated\"\n" +
                "[[mods]]\n" +
                "modId = \"" + fakeModId + "\"\n");
        output.addFile("META-INF/neoforge.mods.toml",
                "modLoader = \"javafml\"\n" +
                        "loaderVersion = \"[1,)\"\n" +
                        "license = \"Generated\"\n" +
                        "[[mods]]\n" +
                        "modId = \"" + fakeModId + "\"\n");
        output.addFile("pack.mcmeta",
                "{\"pack\":{\"description\":\"Generated\",\"pack_format\":" + System.getProperty(BuiltinProperties.MCMETA_VERSION, "4") + "}}");
        output.addFile("generated" + fakeModId + "/" + fakeModId + ".class", generateClass(fakeModId));
    }
    
    private byte[] generateClass(String fakeModId) {
        ClassWriter writer = new ClassWriter(0);
        writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "generated" + fakeModId + "/" + fakeModId, null, "java/lang/Object", null);
        AnnotationVisitor modAnnotation = writer.visitAnnotation("Lnet/neoforged/fml/common/Mod;", false);
        modAnnotation.visit("value", fakeModId);
        modAnnotation.visitEnd();
        {
            MethodVisitor method = writer.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, new String[0]);
            method.visitVarInsn(Opcodes.ALOAD, 0);
            method.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            method.visitInsn(Opcodes.RETURN);
            method.visitMaxs(1, 1);
            method.visitEnd();
        }
        writer.visitEnd();
        return writer.toByteArray();
    }
}