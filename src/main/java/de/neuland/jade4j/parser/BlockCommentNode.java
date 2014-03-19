package de.neuland.jade4j.parser;

import de.neuland.jade4j.compiler.BaseWriter;
import de.neuland.jade4j.compiler.IndentWriter;
import de.neuland.jade4j.compiler.ScriptWriter;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.template.JadeTemplate;

public class BlockCommentNode extends CommentNode {

    @Override
    public void execute(IndentWriter writer, JadeModel model, JadeTemplate template) throws JadeCompilerException {
        _execute(writer, model, template);
    }

    @Override
    public void execute(ScriptWriter writer, JadeModel model, JadeTemplate template) throws JadeCompilerException {
        _execute(writer, model, template);
    }

    private void _execute(BaseWriter writer, JadeModel model, JadeTemplate template) throws JadeCompilerException {
        if (!isBuffered()) {
            return;
        }
        writer.newline();
        if (value.startsWith("if")) {
            writer.append("<!--[" + value + "]>");
            block.accept(writer, model, template);
            writer.append("<![endif]-->");
        } else {
            writer.append("<!--" + value);
            block.accept(writer, model, template);
            writer.append("-->");
        }
    }
}
