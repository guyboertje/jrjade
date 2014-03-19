package com.jrjade;

import de.neuland.jade4j.Jade4J;
import de.neuland.jade4j.template.JadeTemplate;
import java.io.IOException;
import java.io.StringReader;

/**
 *
 * @author guy
 */
public class JadeStringCompiler extends JadeBaseCompiler {

    public JadeStringCompiler(String src) {
        this.src = src;
    }

    @Override
    protected JadeTemplate loadTemplate() throws IOException {
        return Jade4J.getTemplate(new StringReader(src), "__TEMPLATE__");
    }
}
