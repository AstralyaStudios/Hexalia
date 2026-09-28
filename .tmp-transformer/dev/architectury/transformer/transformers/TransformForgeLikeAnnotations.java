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

import dev.architectury.transformer.transformers.base.ClassEditTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Handle @ForgeEvent and @ForgeEventCancellable and promote @Environment from being an invisible annotation to being an visible annotation.
 */
public class TransformForgeLikeAnnotations implements ClassEditTransformer {
    public static final String FORGE_EVENT_LEGACY = "Lme/shedaniel/architectury/ForgeEvent;";
    public static final String FORGE_EVENT = "Ldev/architectury/annotations/ForgeEvent;";
    public static final String FORGE_EVENT_CANCELLABLE_LEGACY = "Lme/shedaniel/architectury/ForgeEventCancellable;";
    public static final String FORGE_EVENT_CANCELLABLE = "Ldev/architectury/annotations/ForgeEventCancellable;";
    public static final String CANCELABLE = "Lnet/minecraftforge/eventbus/api/Cancelable;";
    
    private static final String ENVIRONMENT = "net/fabricmc/api/Environment";
    private static final String FORGE_ONLY_IN = "net/minecraftforge/api/distmarker/OnlyIn";
    protected static final String NEOFORGE_ONLY_IN = "net/neoforged/api/distmarker/OnlyIn";
    
    private final String onlyIn;
    
    public TransformForgeLikeAnnotations(String onlyIn) {
        this.onlyIn = onlyIn;
    }
    
    @Override
    public ClassNode doEdit(String name, ClassNode node) {
        if ((node.access & Opcodes.ACC_INTERFACE) == 0) {
            if (node.visibleAnnotations != null && node.visibleAnnotations.stream().anyMatch(
                    annotation -> Objects.equals(annotation.desc, FORGE_EVENT) || Objects.equals(annotation.desc, FORGE_EVENT_CANCELLABLE)
                            || Objects.equals(annotation.desc, FORGE_EVENT_LEGACY) || Objects.equals(annotation.desc, FORGE_EVENT_CANCELLABLE_LEGACY)
            )) {
                node.superName = "net/minecraftforge/eventbus/api/Event";
                for (MethodNode method : node.methods) {
                    if (Objects.equals(method.name, "<init>")) {
                        for (AbstractInsnNode insnNode : method.instructions) {
                            if (insnNode.getOpcode() == Opcodes.INVOKESPECIAL) {
                                MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;
                                if (Objects.equals(methodInsnNode.name, "<init>") && Objects.equals(methodInsnNode.owner, "java/lang/Object")) {
                                    methodInsnNode.owner = "net/minecraftforge/eventbus/api/Event";
                                    break;
                                }
                            }
                        }
                    }
                }
                if (node.signature != null) {
                    int index = node.signature.lastIndexOf('L');
                    String s = index == -1 ? node.signature : node.signature.substring(0, index);
                    node.signature = s + "Lnet/minecraftforge/eventbus/api/Event;";
                }
                // if @ForgeEventCancellable, add the cancellable annotation from forge
                if ((node.visibleAnnotations.stream().anyMatch(annotation -> Objects.equals(annotation.desc, FORGE_EVENT_CANCELLABLE_LEGACY))
                        || node.visibleAnnotations.stream().anyMatch(annotation -> Objects.equals(annotation.desc, FORGE_EVENT_CANCELLABLE))) &&
                        node.visibleAnnotations.stream().noneMatch(annotation -> Objects.equals(annotation.desc, CANCELABLE))) {
                    node.visibleAnnotations.add(new AnnotationNode(CANCELABLE));
                }
            }
        }
        if (node.visibleAnnotations == null) {
            node.visibleAnnotations = new ArrayList<>();
        }
        {
            Collection<AnnotationNode> invisibleEnvironments;
            if (node.invisibleAnnotations != null) {
                invisibleEnvironments = node.invisibleAnnotations.stream()
                        .filter(annotation -> Objects.equals(annotation.desc, "L" + ENVIRONMENT + ";") || Objects.equals(annotation.desc, "L" + onlyIn + ";"))
                        .collect(Collectors.toList());
                node.invisibleAnnotations.removeAll(invisibleEnvironments);
            } else {
                invisibleEnvironments = Collections.emptyList();
            }
            node.visibleAnnotations.addAll(invisibleEnvironments);
        }
        for (FieldNode field : node.fields) {
            if (field.visibleAnnotations == null) {
                field.visibleAnnotations = new ArrayList<>();
            }
            
            Collection<AnnotationNode> invisibleEnvironments;
            if (field.invisibleAnnotations != null) {
                invisibleEnvironments = field.invisibleAnnotations.stream()
                        .filter(annotation -> Objects.equals(annotation.desc, "L" + ENVIRONMENT + ";") || Objects.equals(annotation.desc, "L" + onlyIn + ";"))
                        .collect(Collectors.toList());
                field.invisibleAnnotations.removeAll(invisibleEnvironments);
            } else {
                invisibleEnvironments = Collections.emptyList();
            }
            field.visibleAnnotations.addAll(invisibleEnvironments);
        }
        for (MethodNode method : node.methods) {
            if (method.visibleAnnotations == null) {
                method.visibleAnnotations = new ArrayList<>();
            }
            
            Collection<AnnotationNode> invisibleEnvironments;
            if (method.invisibleAnnotations != null) {
                invisibleEnvironments = method.invisibleAnnotations.stream()
                        .filter(annotation -> Objects.equals(annotation.desc, "L" + ENVIRONMENT + ";") || Objects.equals(annotation.desc, "L" + onlyIn + ";"))
                        .collect(Collectors.toList());
                method.invisibleAnnotations.removeAll(invisibleEnvironments);
            } else {
                invisibleEnvironments = Collections.emptyList();
            }
            method.visibleAnnotations.addAll(invisibleEnvironments);
        }
        return node;
    }
}