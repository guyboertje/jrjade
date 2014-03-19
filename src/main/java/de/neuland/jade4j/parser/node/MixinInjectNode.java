package de.neuland.jade4j.parser.node;

import java.util.ArrayList;
import java.util.List;

import de.neuland.jade4j.compiler.IndentWriter;
import de.neuland.jade4j.compiler.ScriptWriter;
import de.neuland.jade4j.compiler.Utils;
import de.neuland.jade4j.exceptions.ExpressionException;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.expression.ExpressionHandler;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.template.JadeTemplate;

public class MixinInjectNode extends AttributedNode {

    protected List<String> arguments = new ArrayList<String>();

    @Override
    public void execute(IndentWriter writer, JadeModel model, JadeTemplate template) throws JadeCompilerException {
        MixinNode mixin = prepare(model, template);
        model.pushScope();

        writeVariables(model, mixin, template);
        writeAttributes(model, mixin, template);
        mixin.getBlock().accept(writer, model, template);

        model.popScope();
    }

    @Override
    public void execute(ScriptWriter writer, JadeModel model, JadeTemplate template) throws JadeCompilerException {
        MixinNode mixin = prepare(model, template);
        model.pushScope();

        writeVariables(model, mixin, template, writer);
        writeAttributes(model, mixin, template);
        mixin.getBlock().accept(writer, model, template);

        model.popScope();
    }

    private MixinNode prepare(JadeModel model, JadeTemplate template) {
        MixinNode mixin = model.getMixin(getName());
        if (mixin == null) {
            throw new JadeCompilerException(this, template.getTemplateLoader(), "mixin " + getName() + " is not defined");
        }
        // Clone mixin
        try {
            mixin = (MixinNode) mixin.clone();
        } catch (CloneNotSupportedException e) {
            // Can't happen
            throw new IllegalStateException(e);
        }

        if (hasBlock()) {
            List<BlockNode> injectionPoints = getInjectionPoints(mixin.getBlock());
            for (BlockNode point : injectionPoints) {
                point.getNodes().add(block);
            }
        }
        return mixin;
    }

    private List<BlockNode> getInjectionPoints(Node block) {
        List<BlockNode> result = new ArrayList<BlockNode>();
        for (Node node : block.getNodes()) {
            if (node instanceof BlockNode && !node.hasNodes()) {
                result.add((BlockNode) node);
            } else if (node instanceof ConditionalNode) {
                for (IfConditionNode condition : ((ConditionalNode) node).getConditions()) {
                    result.addAll(getInjectionPoints(condition.getBlock()));
                }
            } else if (node instanceof CaseNode) {
                for (CaseConditionNode condition : ((CaseNode) node).getCaseConditionNodes()) {
                    result.addAll(getInjectionPoints(condition.getBlock()));
                }
            } else if (node.hasBlock()) {
                result.addAll(getInjectionPoints(node.getBlock()));
            }
        }
        return result;
    }

    private void writeVariables(JadeModel model, MixinNode mixin, JadeTemplate template, ScriptWriter writer) {
        List<String> names = mixin.getArguments();
        List<String> values = arguments;
        if (names == null) {
            return;
        }
        for (int i = 0; i < names.size(); i++) {
            String key = names.get(i);
            Object value = null;
            String pre = "";

            if (i < values.size()) {
                value = values.get(i);
            }
            if (value != null) {
                pre = (String) value;
                String post = pre.replaceAll("^[\"']|['\"]$", "");
                if (pre.equals(post)) {
                    value = writer.wrapForEscapedValue(pre);
                } else {
                    try {
                        value = Utils.interpolate(post, false, writer);
                    } catch (ExpressionException e) {
                        throw new JadeCompilerException(this, template.getTemplateLoader(), e);
                    }
                }
            }
            if (key != null) {
                model.put(key, value);
            }
        }
    }

    private void writeVariables(JadeModel model, MixinNode mixin, JadeTemplate template) {
        List<String> names = mixin.getArguments();
        List<String> values = arguments;
        if (names == null) {
            return;
        }
        for (int i = 0; i < names.size(); i++) {
            String key = names.get(i);
            Object value = null;

            if (i < values.size()) {
                value = values.get(i);
            }
            if (value != null) {
                try {
                    value = ExpressionHandler.evaluateExpression(values.get(i), model);
                } catch (Throwable e) {
                    throw new JadeCompilerException(this, template.getTemplateLoader(), e);
                }
            }
            if (key != null) {
                model.put(key, value);
            }
        }
    }

    private void writeAttributes(JadeModel model, MixinNode mixin, JadeTemplate template) {
        model.put("attributes", mergeInheritedAttributes(model));
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public void setArguments(String arguments) {
        this.arguments.clear();
        for (String argument : arguments.split(",")) {
            this.arguments.add(argument.trim());
        }
    }
}
