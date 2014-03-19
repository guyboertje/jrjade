/*
 * The MIT License
 *
 * Copyright 2014 guy.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package de.neuland.jade4j.compiler;

import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.parser.node.Node;
import de.neuland.jade4j.template.JadeTemplate;
import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author guy
 */
public abstract class BaseWriter {
    protected int indent = 0;
    protected boolean useIndent = false;
    protected boolean empty = true;
    protected Writer writer;

    public BaseWriter() {
    }

    public BaseWriter add(String string) {
        return append(string);
    }

    public abstract BaseWriter append(String string);
    
    public abstract void visit(Node node, JadeModel model, JadeTemplate template);

    public void increment() {
        indent++;
    }

    public void decrement() {
        indent--;
    }

    protected void write(String string) {
        try {
            writer.write(string);
            empty = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String toString() {
        return writer.toString();
    }

    public void newline() {
        if (useIndent && !empty) {
            write("\n" + StringUtils.repeat("  ", indent));
        }
    }

    public void setUseIndent(boolean useIndent) {
        this.useIndent = useIndent;
    }
    
}
