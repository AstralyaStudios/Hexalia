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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.WeakHashMap;

public class DirectoryFileAccess extends NIOFileAccess {
    private static final WeakHashMap<Path, DirectoryFileAccess> INTERFACES = new WeakHashMap<>();
    protected final Path root;
    
    protected DirectoryFileAccess(Path root) {
        super(false);
        this.root = root;
    }
    
    public static DirectoryFileAccess of(Path root) throws IOException {
        synchronized (INTERFACES) {
            if (INTERFACES.containsKey(root)) {
                return INTERFACES.get(root);
            }
            
            for (Map.Entry<Path, DirectoryFileAccess> entry : INTERFACES.entrySet()) {
                if (Files.isSameFile(entry.getKey(), root)) {
                    return entry.getValue();
                }
            }
            
            DirectoryFileAccess outputInterface = new DirectoryFileAccess(root);
            INTERFACES.put(root, outputInterface);
            return outputInterface;
        }
    }
    
    @Override
    protected Path rootPath() {
        return root;
    }
    
    @Override
    public void close() throws IOException {
        super.close();
        INTERFACES.remove(root, this);
    }
    
    @Override
    public String toString() {
        return root.toString();
    }
}
