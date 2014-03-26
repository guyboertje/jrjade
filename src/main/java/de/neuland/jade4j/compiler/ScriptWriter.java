package de.neuland.jade4j.compiler;

import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.parser.node.Node;
import de.neuland.jade4j.template.JadeTemplate;
import java.io.Writer;

public class ScriptWriter extends BaseWriter {

    public ScriptWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void visit(Node node, JadeModel model, JadeTemplate template) {
        node.execute(this, model, template);
    }

    @Override
    public ScriptWriter append(String string) {
        write(string);
        return this;
    }

    public String wrapForInsert(String string) {
        return "]; j_j.concat((" + string + ").to_s); j_j.concat %Q[";
    }

    public String wrapForEscapedValue(String string) {
        return "]; j_j.concat((" + string + ").to_s); j_j.concat %Q[";
    }

    public String wrapForEscapedAttribute(String string) {
        return "]; j_j.concat((" + string + ").to_s); j_j.concat %Q[";
    }

    public ScriptWriter insert(String string) {
        write("]; j_j.concat((");
        return _insert(string);
    }

    public ScriptWriter escapedInsert(String string) {
        write("]; j_j.concat((");
        return _insert(string);
    }

    private ScriptWriter _insert(String string) {
        write(string);
        write(").to_s); j_j.concat %Q[");
        return this;
    }

    public ScriptWriter express(String string, boolean block) {
        write("]; ");
        if (block) {
            write(";;");
            write(Integer.toString(indent));
            write("; ");
        }
        write(string);
        write("; j_j.concat %Q[");
        return this;
    }

//    keep original
//    public ScriptWriter express(String string, boolean block) {
//        write("]; ");
//        if (block) {
//            write(";;");
//            write(Integer.toString(indent));
//            write("; ");
//         }
//        write(string);
//        write(" ;");
//        write(Integer.toString(indent));
//        write(";; ");
//        write("; j_j.concat %Q[");
//        return this;
//    }
    public ScriptWriter concat(String string) {
        write(string);
        return this;
    }

    public void decr() {
        indent--;
        write("]; ");
        write(";");
        write(Integer.toString(indent));
        write(";; j_j.concat %Q[");
    }

}
