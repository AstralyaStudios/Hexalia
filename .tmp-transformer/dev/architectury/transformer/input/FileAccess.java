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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public interface FileAccess extends FileView {
    /**
     * Adds a file, overrides the existing file if it already exists.
     *
     * @param path  the path to the file
     * @param bytes the file bytes
     * @return whether it was able to add the file
     */
    boolean addFile(String path, byte[] bytes) throws IOException;
    
    /**
     * Modifies a file, overrides the existing file if it already exists.
     *
     * @param path  the path to the file
     * @param bytes the new file bytes
     * @return the modified file, or {@code null} if unable to modify
     */
    byte[] modifyFile(String path, byte[] bytes) throws IOException;
    
    /**
     * Modifies a file, that is transformed from the old file.
     * Does nothing if it does not exist.
     *
     * @param path   the path to the file
     * @param action the transformer which transforms the old file
     * @return the modified file, or {@code null} if unable to modify
     */
    byte[] modifyFile(String path, UnaryOperator<byte[]> action) throws IOException;
    
    /**
     * Deletes a file, if it exists.
     *
     * @param path the path to the file
     * @return whether it was able to delete the file
     */
    boolean deleteFile(String path) throws IOException;
    
    default void modifyFiles(Predicate<String> pathPredicate, BiFunction<String, byte[], byte[]> action) throws IOException {
        try {
            handle(path -> {
                if (pathPredicate.test(path)) {
                    try {
                        modifyFile(path, bytes -> action.apply(path, bytes));
                    } catch (IOException exception) {
                        throw new UncheckedIOException(exception);
                    }
                }
            });
        } catch (UncheckedIOException exception) {
            throw exception.getCause();
        }
    }
    
    default void modifyFiles(BiFunction<String, byte[], byte[]> action) throws IOException {
        modifyFiles(path -> true, action);
    }
    
    default boolean addFile(String path, String text) throws IOException {
        return addFile(path, text.getBytes(StandardCharsets.UTF_8));
    }
    
    default boolean addClass(String path, byte[] bytes) throws IOException {
        return addFile(path + ".class", bytes);
    }
    
    default void deleteFiles(BiPredicate<String, byte[]> filePredicate) throws IOException {
        try {
            handle((path, bytes) -> {
                if (filePredicate.test(path, bytes)) {
                    try {
                        deleteFile(path);
                    } catch (IOException exception) {
                        throw new UncheckedIOException(exception);
                    }
                }
            });
        } catch (UncheckedIOException exception) {
            throw exception.getCause();
        }
    }
    
    default void deleteFiles(Predicate<String> pathPredicate) throws IOException {
        deleteFiles((path, bytes) -> pathPredicate.test(path));
    }
    
    default boolean deleteClass(String path) throws IOException {
        return deleteFile(path + ".class");
    }
    
    @Override
    default byte[] getFile(String path) throws IOException {
        AtomicReference<byte[]> bytes = new AtomicReference<>(null);
        String trimLeadingSlash = Transform.trimLeadingSlash(path);
        bytes.set(modifyFile(path, b -> b));
        if (bytes.get() != null) {
            handle(p -> Transform.trimLeadingSlash(p).equals(trimLeadingSlash), ($, b) -> {
                bytes.set(b);
            });
        }
        return bytes.get();
    }
}
