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
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface ForwardingFileView extends FileView, ForwardingClosedIndicator {
    @Override
    FileView parent() throws IOException;
    
    @Override
    default void handle(BiConsumer<String, byte[]> action) throws IOException {
        parent().handle(action);
    }
    
    @Override
    default void handle(Consumer<String> action) throws IOException {
        parent().handle(action);
    }
    
    @Override
    default void handle(Predicate<String> pathPredicate, BiConsumer<String, byte[]> action) throws IOException {
        parent().handle(pathPredicate, action);
    }
    
    @Override
    default void copyTo(FileAccess output) throws IOException {
        parent().copyTo(output);
    }
    
    @Override
    default void copyTo(Predicate<String> pathPredicate, FileAccess output) throws IOException {
        parent().copyTo(pathPredicate, output);
    }
    
    @Override
    default byte[] getFile(String path) throws IOException {
        return parent().getFile(path);
    }
    
    @Override
    default byte[] asZipFile() throws IOException {
        return parent().asZipFile();
    }
    
    @Override
    default MemoryFileAccess remember() throws IOException {
        return parent().remember();
    }
}
