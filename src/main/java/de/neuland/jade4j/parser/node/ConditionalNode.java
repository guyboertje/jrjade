package de.neuland.jade4j.parser.node;

import de.neuland.jade4j.compiler.BaseWriter;
import java.util.LinkedList;
import java.util.List;

import de.neuland.jade4j.compiler.IndentWriter;
import de.neuland.jade4j.compiler.ScriptWriter;
import de.neuland.jade4j.exceptions.ExpressionException;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.expression.ExpressionHandler;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.template.JadeTemplate;

public class ConditionalNode extends Node {

    private List<IfConditionNode> conditions = new LinkedList<IfConditionNode>();

    @Override
    public void execute(IndentWriter writer, JadeModel model, JadeTemplate template) throws JadeCompilerException {
        _execute(writer, model, template);
    }

    @Override
    public void execute(ScriptWriter writer, JadeModel model, JadeTemplate template) throws JadeCompilerException {
        _execute(writer, model, template);
    }

    private void _execute(BaseWriter writer, JadeModel model, JadeTemplate template) throws JadeCompilerException {
        for (IfConditionNode conditionNode : this.conditions) {
            try {
                if (conditionNode.isDefault() || checkCondition(model, conditionNode.getValue()) ^ conditionNode.isInverse()) {
                    conditionNode.getBlock().accept(writer, model, template);
                    return;
                }
            } catch (ExpressionException e) {
                throw new JadeCompilerException(conditionNode, template.getTemplateLoader(), e);
            }
        }
    }

    private boolean checkCondition(JadeModel model, String condition) throws ExpressionException {
        Boolean value = ExpressionHandler.evaluateBooleanExpression(condition, model);
        return (value == null) ? false : value;
    }

    public List<IfConditionNode> getConditions() {
        return conditions;
    }

    public void setConditions(List<IfConditionNode> conditions) {
        this.conditions = conditions;
    }

    @Override
    public ConditionalNode clone() throws CloneNotSupportedException {
        ConditionalNode clone = (ConditionalNode) super.clone();

        clone.conditions = new LinkedList<IfConditionNode>();
        for (IfConditionNode condition : conditions) {
            clone.conditions.add((IfConditionNode) condition.clone());
        }

        return clone;
    }
}
