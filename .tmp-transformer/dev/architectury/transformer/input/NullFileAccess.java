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
import java.util.function.UnaryOperator;

public final class NullFileAccess implements FileAccess {
    private static final NullFileAccess INSTANCE = new NullFileAccess();
    
    private NullFileAccess() {}
    
    public static FileAccess of() {
        return INSTANCE;
    }
    
    @Override
    public boolean addFile(String path, byte[] bytes) throws IOException {
        return false;
    }
    
    @Override
    public byte[] modifyFile(String path, byte[] bytes) throws IOException {
        return null;
    }
    
    @Override
    public byte[] modifyFile(String path, UnaryOperator<byte[]> action) throws IOException {
        return null;
    }
    
    @Override
    public boolean deleteFile(String path) throws IOException {
        return false;
    }
    
    @Override
    public void close() throws IOException {
        
    }
    
    @Override
    public void handle(BiConsumer<String, byte[]> action) throws IOException {
        
    }
    
    @Override
    public boolean isClosed() {
        return false;
    }
}
