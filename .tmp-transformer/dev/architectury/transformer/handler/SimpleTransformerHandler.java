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

import dev.architectury.tinyremapper.IMappingProvider;
import dev.architectury.tinyremapper.TinyRemapper;
import dev.architectury.transformer.Transformer;
import dev.architectury.transformer.input.FileAccess;
import dev.architectury.transformer.input.MemoryFileAccess;
import dev.architectury.transformer.transformers.base.AssetEditTransformer;
import dev.architectury.transformer.transformers.base.ClassDeleteTransformer;
import dev.architectury.transformer.transformers.base.ClassEditTransformer;
import dev.architectury.transformer.transformers.base.TinyRemapperTransformer;
import dev.architectury.transformer.transformers.base.edit.TransformerContext;
import dev.architectury.transformer.transformers.classpath.ReadClasspathProvider;
import dev.architectury.transformer.util.Logger;
import dev.architectury.transformer.util.LoggerFilter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.util.*;

public class SimpleTransformerHandler implements TransformHandler {
    protected ReadClasspathProvider classpath;
    protected TransformerContext context;
    protected boolean nested;
    protected boolean closed = false;
    
    public SimpleTransformerHandler(ReadClasspathProvider classpath, TransformerContext context, boolean nested) {
        this.classpath = classpath;
        this.context = context;
        this.nested = nested;
    }
    
    @Override
    public void handle(String input, FileAccess output, List<Transformer> transformers) throws Exception {
        if (closed) throw new IllegalStateException("Cannot transform when the handler is closed already!");
        Logger.debug("Transforming from " + input + " to " + output + " with " + transformers.size() + " transformer(s) on " + getClass().getName());
        
        final Set<IMappingProvider> mappingProviders = collectMappings(transformers);
        
        if (!mappingProviders.isEmpty()) {
            Logger.debug("Remapping with " + mappingProviders.size() + " mapping provider(s):");
            for (IMappingProvider provider : mappingProviders) {
                Logger.debug(" - " + provider);
            }
            remapTR(mappingProviders, input, output);
        }
        
        if (anyTransformerModifiesClass(transformers)) {
            Logger.debug("Found class transformer");
            output.handle(path -> path.endsWith(".class"), (path, bytes) -> {
                try {
                    applyTransforms(transformers, path, bytes, output);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } else {
            Logger.debug("No class transformer");
        }
        
        if (nested) {
            output.modifyFiles(path -> path.endsWith(".jar"), (path, bytes) -> {
                try (MemoryFileAccess zipFile = MemoryFileAccess.ofZipFile(bytes)) {
                    handle(path, zipFile, transformers);
                    return zipFile.asZipFile();
                } catch (IOException exception) {
                    throw new UncheckedIOException(exception);
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            });
        }
        
        editFiles(transformers, output);
    }
    
    private Set<IMappingProvider> collectMappings(List<Transformer> transformers) throws Exception {
        final Set<IMappingProvider> mappings = new HashSet<>();
        
        for (Transformer transformer : transformers) {
            if (transformer instanceof TinyRemapperTransformer) {
                mappings.addAll(((TinyRemapperTransformer) transformer).collectMappings());
            }
        }
        return mappings;
    }
    
    private void remapTR(Set<IMappingProvider> mappingProviders, String input, FileAccess output) throws Exception {
        TinyRemapper remapper = getRemapper(mappingProviders);
        
        LoggerFilter.replaceSystemOut();
        try {
            List<byte[]> classes = new ArrayList<>();
            output.handle((path, bytes) -> {
                if (path.endsWith(".class")) {
                    classes.add(bytes);
                }
            });
            remapper.readInputs(classes.toArray(new byte[][]{}));
            remapper.apply((path, bytes) -> {
                try {
                    output.addClass(path, bytes);
                } catch (IOException exception) {
                    throw new UncheckedIOException(exception);
                }
            });
            debugRemapper(remapper);
        } catch (Exception e) {
            throw new RuntimeException("Failed to remap " + input + " to " + output, e);
        } finally {
            closeRemapper(remapper);
        }
    }
    
    private void debugRemapper(TinyRemapper remapper) throws Exception {
        Field classMapField = remapper.getClass().getDeclaredField("classMap");
        classMapField.setAccessible(true);
        Logger.debug("Remapping Classes:");
        ((Map<?, ?>) classMapField.get(remapper)).forEach((from, to) -> Logger.debug(from + " -> " + to));
    }
    
    protected TinyRemapper getRemapper(Set<IMappingProvider> providers) throws Exception {
        TinyRemapper.Builder builder = TinyRemapper.newRemapper();
        builder.threads(Runtime.getRuntime().availableProcessors());
        for (IMappingProvider provider : providers) {
            builder.withMappings(provider);
        }
        builder.skipConflictsChecking(true);
        builder.logUnknownInvokeDynamic(false);
        TinyRemapper remapper = builder.build();
        
        remapper.readClassPath(classpath.provide());
        return remapper;
    }
    
    protected void closeRemapper(TinyRemapper remapper) throws Exception {
        remapper.finish();
    }
    
    private boolean anyTransformerModifiesClass(List<Transformer> transformers) {
        for (Transformer transformer : transformers) {
            if (transformer instanceof ClassEditTransformer || transformer instanceof ClassDeleteTransformer) {
                return true;
            }
        }
        return false;
    }
    
    private void applyTransforms(List<Transformer> transformers, String path, byte[] bytes, FileAccess output) throws IOException {
        ClassReader reader = new ClassReader(bytes);
        if ((reader.getAccess() & Opcodes.ACC_MODULE) == 0) {
            ClassNode node = new ClassNode(Opcodes.ASM9);
            reader.accept(node, ClassReader.EXPAND_FRAMES);
            
            if (shouldDelete(transformers, path, node)) {
                output.deleteFile(path);
                return;
            }
            
            class State {
                boolean computeMaxs = false, computeFrames = false;
            }
            State state = new State();
            ClassEditTransformer.Options options = new ClassEditTransformer.Options() {
                @Override
                public void computeMaxs() {
                    state.computeMaxs = true;
                }
                
                @Override
                public void computeFrames() {
                    state.computeFrames = true;
                }
            };
            output.modifyFile(path, toByteArray(output, editNode(transformers, path, node, options), state.computeMaxs, state.computeFrames));
        }
    }
    
    private boolean shouldDelete(List<Transformer> transformers, String path, ClassNode node) {
        for (Transformer transformer : transformers) {
            if (transformer instanceof ClassDeleteTransformer) {
                if (((ClassDeleteTransformer) transformer).shouldDelete(path, node)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private ClassNode editNode(List<Transformer> transformers, String path, ClassNode node, ClassEditTransformer.Options options) {
        for (Transformer transformer : transformers) {
            if (transformer instanceof ClassEditTransformer) {
                node = Objects.requireNonNull(((ClassEditTransformer) transformer).doEdit(path, node, options));
            }
        }
        
        return node;
    }
    
    private byte[] toByteArray(FileAccess output, ClassNode node, boolean computeMaxs, boolean computeFrames) {
        Logger.debug("Writing " + node.name + " with maxs=" + computeMaxs + " frames=" + computeFrames);
        final ClassWriter writer = new TransformerClassWriter(classpath, output, (computeMaxs ? ClassWriter.COMPUTE_MAXS : 0) | (computeFrames ? ClassWriter.COMPUTE_FRAMES : 0));
        node.accept(writer);
        return writer.toByteArray();
    }
    
    private void editFiles(List<Transformer> transformers, FileAccess output) {
        for (Transformer transformer : transformers) {
            if (transformer instanceof AssetEditTransformer) {
                try {
                    ((AssetEditTransformer) transformer).doEdit(context, output);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public void close() throws IOException {
        this.context = null;
        this.classpath = null;
        this.closed = true;
    }
}
