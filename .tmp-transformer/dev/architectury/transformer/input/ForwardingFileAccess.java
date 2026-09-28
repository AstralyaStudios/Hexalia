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

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public interface ForwardingFileAccess extends FileAccess, ForwardingFileView {
    @Override
    FileAccess parent() throws IOException;
    
    @Override
    default boolean addFile(String path, byte[] bytes) throws IOException {
        return parent().addFile(path, bytes);
    }
    
    @Override
    default byte[] modifyFile(String path, byte[] bytes) throws IOException {
        return parent().modifyFile(path, bytes);
    }
    
    @Override
    default byte[] modifyFile(String path, UnaryOperator<byte[]> action) throws IOException {
        return parent().modifyFile(path, action);
    }
    
    @Override
    default boolean deleteFile(String path) throws IOException {
        return parent().deleteFile(path);
    }
    
    @Override
    default void modifyFiles(Predicate<String> pathPredicate, BiFunction<String, byte[], byte[]> action) throws IOException {
        parent().modifyFiles(pathPredicate, action);
    }
    
    @Override
    default void modifyFiles(BiFunction<String, byte[], byte[]> action) throws IOException {
        parent().modifyFiles(action);
    }
    
    @Override
    default boolean addFile(String path, String text) throws IOException {
        return parent().addFile(path, text);
    }
    
    @Override
    default boolean addClass(String path, byte[] bytes) throws IOException {
        return parent().addClass(path, bytes);
    }
    
    @Override
    default void deleteFiles(BiPredicate<String, byte[]> filePredicate) throws IOException {
        parent().deleteFiles(filePredicate);
    }
    
    @Override
    default void deleteFiles(Predicate<String> pathPredicate) throws IOException {
        parent().deleteFiles(pathPredicate);
    }
    
    @Override
    default boolean deleteClass(String path) throws IOException {
        return parent().deleteClass(path);
    }
    
    @Override
    default byte[] getFile(String path) throws IOException {
        return parent().getFile(path);
    }
}
