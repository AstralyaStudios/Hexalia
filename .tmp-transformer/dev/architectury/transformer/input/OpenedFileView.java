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

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OpenedFileView extends ClosableChecker implements ForwardingFileView {
    private final Provider provider;
    private final Lock lock = new ReentrantLock();
    private FileView fileView;
    
    protected OpenedFileView(Provider provider) {
        this.provider = provider;
    }
    
    public static OpenedFileView of(Provider provider) {
        return new OpenedFileView(provider);
    }
    
    @FunctionalInterface
    public interface Provider {
        FileView provide() throws IOException;
    }
    
    @Override
    public FileView parent() throws IOException {
        validateCloseState();
        
        try {
            lock.lock();
            if (fileView == null || fileView.isClosed()) {
                return fileView = provider.provide();
            }
            
            return fileView;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void close() throws IOException {
        closeAndValidate();
        if (fileView != null) {
            fileView.close();
        }
        fileView = null;
    }
}
