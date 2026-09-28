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

import com.google.common.io.ByteStreams;
import dev.architectury.transformer.Transform;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MemoryFileAccess extends BaseFileAccess {
    private final Map<String, byte[]> data;
    
    protected MemoryFileAccess(Map<String, byte[]> data) {
        super(false);
        this.data = data;
    }
    
    public static MemoryFileAccess of() throws IOException {
        return of(new HashMap<>());
    }
    
    public static MemoryFileAccess of(Map<String, byte[]> data) throws IOException {
        return new MemoryFileAccess(data);
    }
    
    public static MemoryFileAccess ofZipFile(byte[] data) throws IOException {
        MemoryFileAccess anInterface = MemoryFileAccess.of();
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(data))) {
            ZipEntry e;
            while ((e = zis.getNextEntry()) != null) {
                if (!Transform.trimSlashes(e.getName()).isEmpty()) {
                    anInterface.addFile(e.getName(), ByteStreams.toByteArray(zis));
                }
            }
        }
        return anInterface;
    }
    
    private String format(String path) {
        return Transform.trimSlashes(path);
    }
    
    @Override
    protected boolean exists(String path) {
        return data.containsKey(format(path));
    }
    
    @Override
    protected byte[] read(String path) throws IOException {
        byte[] bytes = data.get(format(path));
        if (bytes == null) throw new FileNotFoundException(path);
        return bytes;
    }
    
    @Override
    protected void write(String path, byte[] bytes) throws IOException {
        data.put(format(path), bytes);
    }
    
    @Override
    public boolean deleteFile(String path) throws IOException {
        return data.remove(format(path)) != null;
    }
    
    @Override
    protected Stream<String> walk(@Nullable String path) throws IOException {
        Stream<String> stream = data.keySet().stream();
        if (path != null) {
            String trimSlashes = format(path) + "/";
            stream = stream.filter(s -> s.startsWith(trimSlashes));
        }
        return stream;
    }
    
    @Override
    public void close() throws IOException {
        super.close();
        data.clear();
    }
}
