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

import dev.architectury.transformer.Transform;
import dev.architectury.transformer.util.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public interface FileView extends ClosedIndicator {
    default void handle(Consumer<String> action) throws IOException {
        handle((path, bytes) -> action.accept(path));
    }
    
    default void handle(Predicate<String> pathPredicate, BiConsumer<String, byte[]> action) throws IOException {
        handle((path, bytes) -> {
            if (pathPredicate.test(path)) {
                action.accept(path, bytes);
            }
        });
    }
    
    void handle(BiConsumer<String, byte[]> action) throws IOException;
    
    default void copyTo(FileAccess output) throws IOException {
        copyTo(path -> true, output);
    }
    
    default void copyTo(Predicate<String> pathPredicate, FileAccess output) throws IOException {
        try {
            handle((path, bytes) -> {
                try {
                    if (pathPredicate.test(path)) {
                        if (!output.addFile(path, bytes)) {
                            Logger.debug("Failed to copy %s from %s to %s", path, this, output);
                        }
                    }
                } catch (IOException exception) {
                    throw new UncheckedIOException(exception);
                }
            });
        } catch (UncheckedIOException exception) {
            throw exception.getCause();
        }
    }
    
    default byte[] getFile(String path) throws IOException {
        AtomicReference<byte[]> bytes = new AtomicReference<>(null);
        String trimLeadingSlash = Transform.trimLeadingSlash(path);
        handle(p -> Transform.trimLeadingSlash(p).equals(trimLeadingSlash), ($, b) -> {
            bytes.set(b);
        });
        return bytes.get();
    }
    
    default byte[] asZipFile() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream(8192);
        try (ZipOutputStream zos = new ZipOutputStream(stream)) {
            handle((path, bytes) -> {
                try {
                    zos.putNextEntry(new ZipEntry(path));
                    zos.write(bytes);
                    zos.closeEntry();
                } catch (IOException exception) {
                    throw new UncheckedIOException(exception);
                }
            });
        } catch (UncheckedIOException exception) {
            throw exception.getCause();
        }
        return stream.toByteArray();
    }
    
    default MemoryFileAccess remember() throws IOException {
        MemoryFileAccess access = MemoryFileAccess.of();
        copyTo(access);
        return access;
    }
}