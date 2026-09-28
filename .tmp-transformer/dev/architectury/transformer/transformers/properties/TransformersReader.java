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

package dev.architectury.transformer.transformers.properties;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.architectury.transformer.Transformer;
import dev.architectury.transformer.util.TransformerEntry;
import dev.architectury.transformer.util.TransformerPair;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TransformersReader extends Reader {
    private BufferedReader out;
    private Scanner scanner;
    
    public TransformersReader(Reader out) {
        this.out = out instanceof BufferedReader ? (BufferedReader) out : new BufferedReader(out);
        this.scanner = new Scanner(out);
        this.scanner.useDelimiter("(?<!\\\\)" + Pattern.quote(File.pathSeparator));
    }
    
    @Nullable
    public TransformerEntry readNext() {
        try {
            if (this.scanner.hasNext()) {
                String str = this.scanner.next();
                String[] split = str.split("\\|");
                Path path = Paths.get(split[0]);
                Class<? extends Transformer> clazz = (Class<? extends Transformer>) Class.forName(split[1]);
                JsonObject properties = null;
                if (split.length > 2) {
                    properties = new JsonParser().parse(str.substring(split[0].length() + split[1].length() + 2).replace("\\" + File.pathSeparatorChar, File.pathSeparator)).getAsJsonObject();
                }
                return new TransformerEntry(path, new TransformerPair(clazz, properties));
            }
            
            return null;
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    public Stream<TransformerEntry> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<TransformerEntry>() {
            @Override
            public boolean hasNext() {
                return TransformersReader.this.scanner.hasNext();
            }
            
            @Override
            public TransformerEntry next() {
                return TransformersReader.this.readNext();
            }
        }, Spliterator.IMMUTABLE), false);
    }
    
    public Map<Path, List<TransformerPair>> readAll() {
        return stream().collect(Collectors.groupingBy(TransformerEntry::getPath,
                Collectors.mapping(TransformerEntry::getTransformer,
                        Collectors.toList())));
    }
    
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return out.read(cbuf, off, len);
    }
    
    @Override
    public void close() throws IOException {
        out.close();
    }
}
