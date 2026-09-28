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

package dev.architectury.transformer.transformers;

import dev.architectury.transformer.Transform;
import dev.architectury.transformer.util.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@FunctionalInterface
public interface ClasspathProvider {
    static ClasspathProvider fromProperties() {
        return () -> Stream.of(Transform.getClasspath())
                .map(Paths::get)
                .filter(Files::exists)
                .toArray(Path[]::new);
    }
    
    static ClasspathProvider of(Path... paths) {
        return () -> paths;
    }
    
    static ClasspathProvider of(Collection<Path> paths) {
        return () -> paths.toArray(new Path[0]);
    }
    
    Path[] provide();
    
    default ClasspathProvider filter(Predicate<Path> filter) {
        return () -> Arrays.stream(provide())
                .filter(filter)
                .toArray(Path[]::new);
    }
    
    default ClasspathProvider logging() {
        return () -> {
            Path[] paths = provide();
            Logger.debug("Provided " + paths.length + " classpath jar(s):");
            for (Path path : paths) {
                Logger.debug(" - " + path.toString());
            }
            return paths;
        };
    }
    
    default ClasspathProvider joining(ClasspathProvider... providers) {
        List<ClasspathProvider> providerList = new ArrayList<>();
        providerList.add(this);
        Collections.addAll(providerList, providers);
        return () -> {
            List<Path> paths = new ArrayList<>();
            for (ClasspathProvider provider : providerList) {
                Collections.addAll(paths, provider.provide());
            }
            return paths.toArray(new Path[0]);
        };
    }
}
