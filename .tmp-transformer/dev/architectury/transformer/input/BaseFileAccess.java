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

package dev.architectury.transformer.input;

import dev.architectury.transformer.util.ClosableChecker;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseFileAccess extends ClosableChecker implements FileAccess {
    private final Map<String, byte[]> cache = new HashMap<>();
    private final boolean shouldCache;
    
    public BaseFileAccess(boolean shouldCache) {
        this.shouldCache = shouldCache;
    }
    
    protected void clearCache() {
        this.cache.clear();
    }
    
    @Override
    public void handle(Consumer<String> action) throws IOException {
        validateCloseState();
        try (Stream<String> stream = walk(null)) {
            for (String path : stream.collect(Collectors.toList())) {
                action.accept(path);
            }
        }
    }
    
    private byte[] cacheRead(String path) {
        if (this.shouldCache) {
            return cache.computeIfAbsent(path, this::_cacheRead);
        }
        
        return _cacheRead(path);
    }
    
    private byte[] _cacheRead(String path) {
        try {
            return read(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    @Override
    public void handle(BiConsumer<String, byte[]> action) throws IOException {
        validateCloseState();
        try (Stream<String> stream = walk(null)) {
            for (String path : stream.collect(Collectors.toList())) {
                action.accept(path, cacheRead(path));
            }
        }
    }
    
    @Override
    public boolean addFile(String path, byte[] bytes) throws IOException {
        validateCloseState();
        if (bytes == null) return false;
        write(path, bytes);
        clearCache();
        return true;
    }
    
    @Override
    public byte[] modifyFile(String path, byte[] bytes) throws IOException {
        return addFile(path, bytes) ? bytes : null;
    }
    
    @Override
    public byte[] modifyFile(String path, UnaryOperator<byte[]> action) throws IOException {
        validateCloseState();
        
        if (exists(path)) {
            byte[] bytes = read(path);
            try {
                bytes = action.apply(bytes);
            } catch (Exception e) {
                throw new RuntimeException("Failed to modify " + path, e);
            }
            return modifyFile(path, bytes);
        }
        
        return null;
    }
    
    @Override
    public void close() throws IOException {
        closeAndValidate();
        clearCache();
    }
    
    @Override
    public byte[] getFile(String path) throws IOException {
        validateCloseState();
        return exists(path) ? cacheRead(path) : null;
    }
    
    protected abstract boolean exists(String path) throws IOException;
    
    protected abstract byte[] read(String path) throws IOException;
    
    protected abstract void write(String path, byte[] bytes) throws IOException;
    
    protected abstract Stream<String> walk(@Nullable String path) throws IOException;
}
