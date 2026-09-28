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

package dev.architectury.transformer.transformers.classpath;

import dev.architectury.transformer.Transform;
import dev.architectury.transformer.input.MemoryFileAccess;
import dev.architectury.transformer.transformers.ClasspathProvider;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ReadClasspathProviderImpl implements ReadClasspathProvider {
    private final ClasspathProvider provider;
    private Map<String, Integer> map = new HashMap<>();
    private byte[][] classpaths;
    
    public ReadClasspathProviderImpl(ClasspathProvider provider) {
        this.provider = provider;
    }
    
    @Override
    public byte[][] provide() {
        synchronized (this) {
            if (classpaths == null) {
                map.clear();
                try {
                    Transform.logTime(() -> {
                        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
                        List<CompletableFuture<List<Map.Entry<String, byte[]>>>> futures = new ArrayList<>();
                        List<Closeable> fsToClose = Collections.synchronizedList(new ArrayList<>());
                        
                        for (Path path : provider.provide()) {
                            futures.add(read(new FilePathEntry(path), threadPool, fsToClose, true));
                        }
                        
                        CompletableFuture<List<Map.Entry<String, byte[]>>> future = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                                .thenApply(unused -> futures
                                        .stream()
                                        .map(CompletableFuture::join)
                                        .flatMap(Collection::stream)
                                        .collect(Collectors.toList()));
                        
                        List<Map.Entry<String, byte[]>> bytes = future.get(60, TimeUnit.SECONDS);
                        int[] i = {0};
                        classpaths = bytes.stream().peek(entry -> {
                            map.put(Transform.trimLeadingSlash(entry.getKey()), i[0]++);
                        }).map(Map.Entry::getValue).toArray(byte[][]::new);
                        threadPool.awaitTermination(0, TimeUnit.SECONDS);
                        
                        for (Closeable system : fsToClose) {
                            system.close();
                        }
                    }, "Read classpath");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            
            return classpaths;
        }
    }
    
    @Override
    public int indexOf(String type) {
        provide();
        return map.getOrDefault(type, -1);
    }
    
    private CompletableFuture<List<Map.Entry<String, byte[]>>> read(PathEntry path, ExecutorService service, List<Closeable> fsToClose, boolean isParentLevel) {
        if (path.toString().endsWith(".class")) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    byte[] bytes = path.read();
                    String s = path.toString();
                    return Collections.singletonList(new AbstractMap.SimpleImmutableEntry<>(s.substring(0, s.length() - 6), bytes));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                return Collections.emptyList();
            }, service);
        } else if (path.isArchive()) {
            try {
                List<CompletableFuture<List<Map.Entry<String, byte[]>>>> futures = new ArrayList<>();
                Closeable closeable = path.walkArchive(f -> {
                    futures.add(read(f, service, fsToClose, false));
                });
                fsToClose.add(closeable);
                return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenApply(unused -> futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return CompletableFuture.completedFuture(Collections.emptyList());
    }
    
    private interface PathEntry {
        byte[] read() throws IOException;
        
        boolean isArchive();
        
        Closeable walkArchive(Consumer<PathEntry> callback) throws IOException;
    }
    
    private static class FilePathEntry implements PathEntry {
        private final Path path;
        
        public FilePathEntry(Path path) {
            this.path = path;
        }
        
        @Override
        public String toString() {
            return this.path.toString();
        }
        
        @Override
        public byte[] read() throws IOException {
            return Files.readAllBytes(path);
        }
        
        @Override
        public boolean isArchive() {
            return toString().endsWith(".zip") || toString().endsWith(".jar");
        }
        
        @Override
        public Closeable walkArchive(Consumer<PathEntry> callback) throws IOException {
            if (!isArchive()) throw new IllegalStateException();
            MemoryFileAccess access = MemoryFileAccess.ofZipFile(Files.readAllBytes(path));
            Lock lock = new ReentrantLock();
            access.handle((name) -> {
                callback.accept(new PathEntry() {
                    @Override
                    public String toString() {
                        return Transform.trimSlashes(name);
                    }
                    
                    @Override
                    public byte[] read() throws IOException {
                        lock.lock();
                        try {
                            return access.getFile(toString());
                        } finally {
                            lock.unlock();
                        }
                    }
                    
                    @Override
                    public boolean isArchive() {
                        return false;
                    }
                    
                    @Override
                    public Closeable walkArchive(Consumer<PathEntry> callback) throws IOException {
                        throw new UnsupportedOperationException();
                    }
                });
            });
            return access;
        }
    }
}
