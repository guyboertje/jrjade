package de.neuland.jade4j.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import de.neuland.jade4j.filter.CssFilter;
import de.neuland.jade4j.filter.JsFilter;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.neuland.jade4j.TestFileHelper;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.filter.MarkdownFilter;
import de.neuland.jade4j.filter.PlainFilter;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.parser.Parser;
import de.neuland.jade4j.parser.node.Node;
import de.neuland.jade4j.template.FileTemplateLoader;

public class CompilerScriptingTest {

    private String expectedFileNameExtension = ".txt";

    @Test
    public void scripting() {
        run("scripting", true);
    }

    private void run(String testName) {
        run(testName, false);
    }

    private void run(String testName, boolean pretty) {
        JadeModel model = new JadeModel(getModelMap(testName));
        run(testName, pretty, model);
    }

    private void run(String testName, boolean pretty, JadeModel model) {
        Parser parser = null;
        try {
            FileTemplateLoader loader = new FileTemplateLoader(
                    TestFileHelper.getCompilerResourcePath(""), "UTF-8");
            parser = new Parser(testName, loader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Node root = parser.parse();
        Compiler compiler = new Compiler(root);
        compiler.setPrettyPrint(pretty);
        compiler.setScriptMode(true);
        String expected = readFile(testName + expectedFileNameExtension);
        model.addFilter("markdown", new MarkdownFilter());
        model.addFilter("plain", new PlainFilter());
        model.addFilter("js", new JsFilter());
        model.addFilter("css", new CssFilter());
        model.addFilter("svg", new PlainFilter());
        String html;
        try {
            html = compiler.compileToString(model);
            System.out.println(html);
            assertEquals(testName, expected.trim(), html.trim());
        } catch (JadeCompilerException e) {
            e.printStackTrace();
            fail();
        }
    }

    @SuppressWarnings("unused")
    private void debugOutput(String testName) {
        System.out.println(testName + " >>>> ");
        System.out.println("[jade]");
        System.out.println(readFile(testName + ".jade").trim() + "\n");
        System.out.println("[model]");
        System.out.println(readFile(testName + ".json") + "\n");
        System.out.println("[html]");
        System.out.println(readFile(testName + ".html").trim() + "\n");
    }

    private Map<String, Object> getModelMap(String testName) {
        String json = readFile(testName + ".json");
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> model = gson.fromJson(json, type);
        if (model == null) {
            model = new HashMap<String, Object>();
        }
        return model;
    }

    private String readFile(String fileName) {
        try {
            return FileUtils.readFileToString(new File(TestFileHelper
                    .getCompilerResourcePath(fileName)));
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return "";
    }

}
