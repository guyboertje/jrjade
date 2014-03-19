package de.neuland.jade4j.parser.node;

import de.neuland.jade4j.compiler.IndentWriter;
import de.neuland.jade4j.compiler.ScriptWriter;
import de.neuland.jade4j.exceptions.ExpressionException;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.expression.ExpressionHandler;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.template.JadeTemplate;

public class AssigmentNode extends Node {

    @Override
    public void execute(IndentWriter writer, JadeModel model, JadeTemplate template) throws JadeCompilerException {

        Object result;
        try {
            result = ExpressionHandler.evaluateExpression(value, model);
        } catch (ExpressionException e) {
            throw new JadeCompilerException(this, template.getTemplateLoader(), e);
        }
        model.put(name, result);
    }

    @Override
    public void execute(ScriptWriter writer, JadeModel model, JadeTemplate template) throws JadeCompilerException {
        writer.insert(value);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
    }
}
