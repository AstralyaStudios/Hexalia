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

/**
 * Handle @ForgeEvent and @ForgeEventCancellable and promote @Environment from being an invisible annotation to being an visible annotation.
 */
public class TransformForgeAnnotations extends TransformForgeLikeAnnotations {
    public static final String FORGE_EVENT_LEGACY = "Lme/shedaniel/architectury/ForgeEvent;";
    public static final String FORGE_EVENT = "Ldev/architectury/annotations/ForgeEvent;";
    public static final String FORGE_EVENT_CANCELLABLE_LEGACY = "Lme/shedaniel/architectury/ForgeEventCancellable;";
    public static final String FORGE_EVENT_CANCELLABLE = "Ldev/architectury/annotations/ForgeEventCancellable;";
    public static final String CANCELABLE = "Lnet/minecraftforge/eventbus/api/Cancelable;";
    
    private static final String ONLY_IN = "net/minecraftforge/api/distmarker/OnlyIn";
    
    public TransformForgeAnnotations() {
        super(ONLY_IN);
    }
}