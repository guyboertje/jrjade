/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jrjade;

import de.neuland.jade4j.Jade4J;
import de.neuland.jade4j.template.JadeTemplate;
import java.io.IOException;

/**
 *
 * @author guy
 */
public class JadeFileCompiler extends JadeBaseCompiler {

    public JadeFileCompiler(String path) {
        this.path = path;
    }
   
    @Override
    protected JadeTemplate loadTemplate() throws IOException {
      return Jade4J.getTemplate(path);
    }
}
