package de.neuland.jade4j.parser.node;

import org.apache.commons.lang3.StringEscapeUtils;

import de.neuland.jade4j.compiler.IndentWriter;
import de.neuland.jade4j.compiler.ScriptWriter;
import de.neuland.jade4j.exceptions.ExpressionException;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.expression.ExpressionHandler;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.template.JadeTemplate;

public class ExpressionNode extends Node {

    private boolean escape;
    private boolean buffer;

    public void setEscape(boolean escape) {
        this.escape = escape;
    }

    public void setBuffer(boolean buffer) {
        this.buffer = buffer;
    }

    @Override
    public void execute(IndentWriter writer, JadeModel model, JadeTemplate template) throws JadeCompilerException {
        try {
            String string = getValue();
            Object result = ExpressionHandler.evaluateStringExpression(getValue(), model);
            if (result == null || !buffer) {
                return;
            }
            string = result.toString();
            if (escape) {
                string = StringEscapeUtils.escapeHtml4(string);
            }
            writer.append(string);
            if (hasBlock()) {
                writer.increment();
                block.execute(writer, model, template);
                writer.decrement();
                writer.newline();
            }
        } catch (ExpressionException e) {
            throw new JadeCompilerException(this, template.getTemplateLoader(), e);
        }
    }

    @Override
    public void execute(ScriptWriter writer, JadeModel model, JadeTemplate template) throws JadeCompilerException {
        String string = getValue();
        String val = (String) model.get(string);
        if (val != null) {
            writer.concat(val);
        } else {
            if (buffer) {
                if (escape) {
                    writer.escapedInsert(string);
                } else {
                    writer.insert(string);
                }
            } else {
                writer.express(string, hasBlock());
            }
        }
        if (hasBlock()) {
            writer.increment();
            block.execute(writer, model, template);
            if (buffer) {
                writer.decrement();
            } else {
                writer.decr();
            }
            writer.newline();
        }
    }

    @Override
    public void setValue(String value) {
        super.setValue(value.trim());
    }
}
