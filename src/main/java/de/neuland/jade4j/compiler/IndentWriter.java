package de.neuland.jade4j.compiler;

import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.parser.node.Node;
import de.neuland.jade4j.template.JadeTemplate;
import java.io.Writer;

public class IndentWriter extends BaseWriter {

    public IndentWriter(Writer writer) {
        this.writer = writer;
    }
    
    @Override
    public IndentWriter append(String string) {
        write(string);
        return this;
    }
    
    @Override
    public void visit(Node node, JadeModel model, JadeTemplate template){
        node.execute(this, model, template);
    }
}
