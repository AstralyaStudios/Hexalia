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

import dev.architectury.transformer.util.FileSystemReference;
import dev.architectury.transformer.util.Logger;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.WeakHashMap;

public class JarFileAccess extends NIOFileAccess {
    private static final WeakHashMap<Path, JarFileAccess> INTERFACES = new WeakHashMap<>();
    protected final Path path;
    private FileSystemReference fs;
    
    protected JarFileAccess(Path path) {
        super(true);
        this.path = path;
        
        try {
            this.fs = FileSystemReference.openJar(path, true);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    public static JarFileAccess of(Path root) throws IOException {
        synchronized (INTERFACES) {
            if (INTERFACES.containsKey(root)) {
                return INTERFACES.get(root);
            }
            
            for (Map.Entry<Path, JarFileAccess> entry : INTERFACES.entrySet()) {
                if (Files.isSameFile(entry.getKey(), root)) {
                    return entry.getValue();
                }
            }
            
            JarFileAccess outputInterface = new JarFileAccess(root);
            INTERFACES.put(root, outputInterface);
            return outputInterface;
        }
    }
    
    @Override
    protected Path rootPath() {
        return getFS().getPath("/");
    }
    
    @Override
    public void close() throws IOException {
        super.close();
        if (fs.closeIfPossible()) {
            Logger.debug("Closed File Systems for " + path);
        }
        INTERFACES.remove(path, this);
    }
    
    protected FileSystem getFS() {
        validateCloseState();
        if (fs.isClosed() || !fs.fs().isOpen()) {
            try {
                clearCache();
                fs = FileSystemReference.openJar(path, true);
                return fs.fs();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        return fs.fs();
    }
    
    @Override
    public String toString() {
        return path.toString();
    }
}
