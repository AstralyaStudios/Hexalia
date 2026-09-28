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

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class NIOFileAccess extends BaseFileAccess {
    public NIOFileAccess(boolean shouldCache) {
        super(shouldCache);
    }
    
    private Path resolve(String path) {
        return rootPath().resolve(path);
    }
    
    @Override
    protected boolean exists(String path) {
        return Files.exists(resolve(path));
    }
    
    @Override
    protected byte[] read(String path) throws IOException {
        return Files.readAllBytes(resolve(path));
    }
    
    @Override
    protected void write(String path, byte[] bytes) throws IOException {
        Path p = resolve(path);
        Path parent = p.normalize().getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        Files.write(p, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    @Override
    public boolean deleteFile(String path) throws IOException {
        return Files.deleteIfExists(resolve(path));
    }
    
    @Override
    protected Stream<String> walk(@Nullable String path) throws IOException {
        return Files.walk(path == null ? rootPath() : resolve(path))
                .filter(((Predicate<Path>) Files::isDirectory).negate())
                .map(Path::toString);
    }
    
    protected abstract Path rootPath();
}
