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

package dev.architectury.transformer.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;


/*
 * Copyright (c) 2016, 2018, Player, asie
 * Copyright (c) 2019, 2022, FabricMC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public final class FileSystemReference implements Closeable {
    private static final Map<FileSystem, Set<FileSystemReference>> OPEN_FS_MAP = new IdentityHashMap<>();
    private final FileSystem fileSystem;
    private volatile boolean closed;
    
    public static FileSystemReference openJar(Path path) throws IOException {
        return openJar(path, false);
    }
    
    public static FileSystemReference openJar(Path path, boolean create) throws IOException {
        return open(toJarUri(path), create);
    }
    
    private static URI toJarUri(Path path) {
        URI uri = path.toUri();
        
        try {
            return new URI("jar:" + uri.getScheme(), uri.getHost(), uri.getPath(), uri.getFragment());
        } catch (URISyntaxException e) {
            throw new RuntimeException("can't convert path " + path + " to uri", e);
        }
    }
    
    public static FileSystemReference open(URI uri) throws IOException {
        return open(uri, false);
    }
    
    public static FileSystemReference open(URI uri, boolean create) throws IOException {
        synchronized (OPEN_FS_MAP) {
            boolean opened = false;
            FileSystem fs = null;
            
            try {
                fs = FileSystems.getFileSystem(uri);
            } catch (FileSystemNotFoundException e) {
                try {
                    fs = FileSystems.newFileSystem(uri, create ? Collections.singletonMap("create", "true") : Collections.emptyMap());
                    opened = true;
                } catch (FileSystemAlreadyExistsException f) {
                    fs = FileSystems.getFileSystem(uri);
                }
            }
            
            FileSystemReference ret = new FileSystemReference(fs);
            Set<FileSystemReference> refs = OPEN_FS_MAP.get(fs);
            
            if (refs == null) {
                refs = Collections.newSetFromMap(new IdentityHashMap<>());
                OPEN_FS_MAP.put(fs, refs);
                if (!opened) refs.add(null);
            } else if (opened) {
                throw new IllegalStateException("opened but already in refs?");
            }
            
            refs.add(ret);
            
            return ret;
        }
    }
    
    private FileSystemReference(FileSystem fs) {
        this.fileSystem = fs;
    }
    
    public boolean isReadOnly() {
        if (closed) throw new ClosedFileSystemException();
        
        return fileSystem.isReadOnly();
    }
    
    public Path getPath(String first, String... more) {
        if (closed) throw new ClosedFileSystemException();
        
        return fileSystem.getPath(first, more);
    }
    
    public FileSystem fs() {
        if (closed) throw new ClosedFileSystemException();
        
        return fileSystem;
    }
    
    public boolean isClosed() {
        return closed;
    }
    
    @Override
    public void close() throws IOException {
        closeIfPossible();
    }
    
    public boolean closeIfPossible() throws IOException {
        synchronized (OPEN_FS_MAP) {
            if (closed) return false;
            closed = true;
            
            Set<FileSystemReference> refs = OPEN_FS_MAP.get(fileSystem);
            if (refs == null || !refs.remove(this)) throw new IllegalStateException("fs " + fileSystem + " was already closed");
            
            if (refs.isEmpty()) {
                OPEN_FS_MAP.remove(fileSystem);
                fileSystem.close();
                return true;
            } else if (refs.size() == 1 && refs.contains(null)) { // only null -> not opened by us, just abandon
                OPEN_FS_MAP.remove(fileSystem);
            }
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        synchronized (OPEN_FS_MAP) {
            Set<FileSystemReference> refs = OPEN_FS_MAP.getOrDefault(fileSystem, Collections.emptySet());
            return String.format("%s=%dx,%s", fileSystem, refs.size(), refs.contains(null) ? "existing" : "new");
        }
    }
}