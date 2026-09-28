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

package dev.architectury.transformer.handler;

import com.google.common.base.MoreObjects;
import dev.architectury.transformer.input.FileAccess;
import dev.architectury.transformer.transformers.classpath.ReadClasspathProvider;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

public class TransformerClassWriter extends ClassWriter {
    private final ReadClasspathProvider classpath;
    private final FileAccess output;
    
    public TransformerClassWriter(ReadClasspathProvider classpath, FileAccess output, int flags) {
        super(flags);
        this.classpath = classpath;
        this.output = output;
    }
    
    public TransformerClassWriter(ReadClasspathProvider classpath, FileAccess output, ClassReader classReader, int flags) {
        super(classReader, flags);
        this.classpath = classpath;
        this.output = output;
    }
    
    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        ClassLoader classLoader = getClassLoader();
        ClassEntry class1 = get(classpath, classLoader, output, type1);
        ClassEntry class2 = get(classpath, classLoader, output, type2);
        if (isAssignableFrom(class1, class2)) {
            return type1;
        }
        if (isAssignableFrom(class2, class1)) {
            return type2;
        }
        if (class1.isInterface() || class2.isInterface()) {
            return "java/lang/Object";
        } else {
            do {
                class1 = class1.getSuperclass();
            } while (!isAssignableFrom(class1, class2));
            return class1.getName().replace('.', '/');
        }
    }
    
    private static ClassEntry get(ReadClasspathProvider classpath, ClassLoader classLoader, FileAccess output, String type) {
        if (type.equals("java/lang/Object")) return ObjectClassEntry.INSTANCE;
        try {
            return new ReflectionClassEntry(Class.forName(type.replace('/', '.'), false, classLoader));
        } catch (ClassNotFoundException e) {
            int indexOf = classpath.indexOf(type);
            if (indexOf != -1) {
                byte[] bytes = classpath.provide()[indexOf];
                return get(classpath, classLoader, output, bytes);
            }
            try {
                byte[] bytes = output.getFile(type + ".class");
                if (bytes != null) {
                    return get(classpath, classLoader, output, bytes);
                }
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
            throw new TypeNotPresentException(type, e);
        }
    }
    
    private static ClassEntry get(ReadClasspathProvider classpath, ClassLoader classLoader, FileAccess output, byte[] bytes) {
        ClassReader reader = new ClassReader(bytes);
        return new StoredClassEntry(classpath, classLoader, output, reader.getClassName(), MoreObjects.firstNonNull(reader.getSuperName(), "java/lang/Object"), (reader.getAccess() & Opcodes.ACC_INTERFACE) != 0);
    }
    
    
    interface ClassEntry {
        String getName();
        
        ClassEntry getSuperclass();
        
        boolean isInterface();
    }
    
    public static boolean isAssignableFrom(ClassEntry base, ClassEntry impl) {
        if (base.getName().equals(impl.getName())) return true;
        while (!Objects.equals(impl.getName(), ObjectClassEntry.INSTANCE.getName())) {
            impl = impl.getSuperclass();
            if (base.getName().equals(impl.getName())) return true;
        }
        return false;
    }
    
    enum ObjectClassEntry implements ClassEntry {
        INSTANCE;
        
        @Override
        public String getName() {
            return "java/lang/Object";
        }
        
        @Override
        public ClassEntry getSuperclass() {
            return this;
        }
        
        @Override
        public boolean isInterface() {
            return false;
        }
    }
    
    static class ReflectionClassEntry implements ClassEntry {
        private Class<?> c;
        
        public ReflectionClassEntry(Class<?> c) {
            this.c = Objects.requireNonNull(c);
        }
        
        @Override
        public String getName() {
            return c.getName();
        }
        
        @Override
        public ClassEntry getSuperclass() {
            Class<?> superclass = c.getSuperclass();
            if (superclass == null) return ObjectClassEntry.INSTANCE;
            return new ReflectionClassEntry(superclass);
        }
        
        @Override
        public boolean isInterface() {
            return c.isInterface();
        }
    }
    
    static class StoredClassEntry implements ClassEntry {
        private final ReadClasspathProvider classpath;
        private final ClassLoader classLoader;
        private final FileAccess output;
        private String name;
        private String superClassName;
        private ClassEntry superClass;
        private boolean isInterface;
        
        public StoredClassEntry(ReadClasspathProvider classpath, ClassLoader classLoader, FileAccess output, String name, String superClassName, boolean isInterface) {
            this.classpath = classpath;
            this.classLoader = classLoader;
            this.output = output;
            this.name = name;
            this.superClassName = superClassName;
            this.isInterface = isInterface;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public ClassEntry getSuperclass() {
            if (superClass == null) {
                superClass = get(classpath, classLoader, output, superClassName);
            }
            
            return superClass;
        }
        
        @Override
        public boolean isInterface() {
            return isInterface;
        }
    }
}
