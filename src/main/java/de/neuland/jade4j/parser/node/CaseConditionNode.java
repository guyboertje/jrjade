package de.neuland.jade4j.parser.node;

import de.neuland.jade4j.compiler.BaseWriter;
import de.neuland.jade4j.compiler.IndentWriter;
import de.neuland.jade4j.compiler.ScriptWriter;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.template.JadeTemplate;

public class CaseConditionNode extends Node {

    private boolean defaultNode = false;

    @Override
    public void execute(IndentWriter writer, JadeModel model, JadeTemplate template) throws JadeCompilerException {
        _execute(writer, model, template);
    }

    @Override
    public void execute(ScriptWriter writer, JadeModel model, JadeTemplate template) throws JadeCompilerException {
        _execute(writer, model, template);
    }

    private void _execute(BaseWriter writer, JadeModel model, JadeTemplate template) throws JadeCompilerException {
        block.accept(writer, model, template);
    }

    public void setDefault(boolean defaultNode) {
        this.defaultNode = defaultNode;
    }

    public boolean isDefault() {
        return defaultNode;
    }
}
