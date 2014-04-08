package com.jrjade;

import de.neuland.jade4j.Jade4J;
import de.neuland.jade4j.template.JadeTemplate;
import java.io.IOException;

/**
 *
 * @author guy
 */
public abstract class JadeBaseCompiler {

    protected boolean pretty;
    protected String src;
    protected String path;

    public JadeBaseCompiler() {
    }

    public boolean isPretty() {
        return pretty;
    }

    public void setPretty(boolean pretty) {
        this.pretty = pretty;
    }

    public String getPath() {
        return path;
    }

    public String render() throws IOException {
        return Jade4J.render(loadTemplate(), pretty, true);
    }

    protected abstract JadeTemplate loadTemplate() throws IOException;

}
