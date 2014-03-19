package de.neuland.jade4j.compiler;

import java.io.StringWriter;
import java.io.Writer;

import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.parser.node.Node;
import de.neuland.jade4j.template.JadeTemplate;

public class Compiler {

    private final Node rootNode;
    private boolean prettyPrint;
    private boolean scriptMode = false;
    private JadeTemplate template = new JadeTemplate();

    public Compiler(Node rootNode) {
        this.rootNode = rootNode;
    }

    public String compileToString(JadeModel model) throws JadeCompilerException {
        StringWriter writer = new StringWriter();
        compile(model, writer);
        return writer.toString();
    }

    public void compile(JadeModel model, Writer w) throws JadeCompilerException {
        if (scriptMode) {
            _compile(model, new ScriptWriter(w));
        } else {
            _compile(model, new IndentWriter(w));
        }
    }

    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public void setScriptMode(boolean mode) {
        this.scriptMode = mode;
    }

    public void setTemplate(JadeTemplate jadeTemplate) {
        this.template = jadeTemplate;
    }

    private void _compile(JadeModel model, IndentWriter writer) throws JadeCompilerException {
        writer.setUseIndent(prettyPrint);
        rootNode.execute(writer, model, template);
    }

    private void _compile(JadeModel model, ScriptWriter writer) throws JadeCompilerException {
        writer.setUseIndent(prettyPrint);
        rootNode.accept(writer, model, template);
    }
}
