/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrjade;

import de.neuland.jade4j.Jade4J;
import de.neuland.jade4j.template.JadeTemplate;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author guy
 */
public abstract class JadeBaseCompiler {

    protected static final String EXERC1 = "\\*(\\d+)\\^(\\s*)\\+\\:\\*\\^\\1\\*";
    protected static final String EXCRE2 = "%\\(\\*=(.*?)\\*\\)%";
    protected static final String EXCOR3 = "%\\(\\*(.*?)\\*\\)%";
    protected static final String EXSCC4 = "\\*\\:\\+\\+\\:\\*";
    protected static final String EXPCS5 = "\\+\\:\\*";
    protected static final String EXSCP6 = "\\*\\:\\+";
    protected static final String EXCAR7 = "\\^\\d+\\*";
    protected static final String EXSTA8 = "\\*\\d+\\^";
    protected static final Pattern EXBOR = Pattern.compile("\\\\*(\\\\d+)\\\\^(\\\\s*)\\\\+\\\\:\\\\*\\\\^\\\\1\\\\*|%\\\\(\\\\*=(.*?)\\\\*\\\\)%|%\\\\(\\\\*(.*?)\\\\*\\\\)%|\\\\*\\\\:\\\\+\\\\+\\\\:\\\\*|\\\\+\\\\:\\\\*|\\\\*\\\\:\\\\+|\\\\^\\\\d+\\\\*|\\\\*\\\\d+\\\\^");
    protected static final String SXSIS4 = "; ";
    protected static final String SXBIS5 = "]; ";
    protected static final String SXMER1 = "$2]; ";
    protected static final String SXCAR7 = "";
    protected boolean pretty;
    protected boolean substitutionsBuilt = false;
    protected Map<String, String> tokens = new HashMap<String, String>();
    protected String src;
    protected String path;
    protected String raw;
    protected String outPre;
    protected String outSuf;
    protected String sxCom2;
    protected String sxCma3;
    protected String sxScp6;
    protected String sxSta8;
    protected Map<String, Object> options;
    protected String outvar;

    public JadeBaseCompiler() {
    }

    public String convert() throws IOException {
        if (raw == null) {
            render();
        }
        if (!substitutionsBuilt) {
            ensureOutvar();
        }
        Matcher matcher = EXBOR.matcher(raw);
        StringBuffer sb = new StringBuffer();
        sb.append(outPre);
        while (matcher.find()) {
            System.out.println("-- matched: 0 - " + matcher.group(0));
            System.out.println("-- matched: 1 - " + matcher.group(1));
            System.out.println("-- matched: 2 - " + matcher.group(2));
            System.out.println("-- buffer:    - " + sb.toString());
            
            matcher.appendReplacement(sb, tokens.get(matcher.group(1)));
        }
        matcher.appendTail(sb);
        sb.append(outSuf);
        return sb.toString();
    }

    public void setOutvar(String outvar) {
        this.outvar = outvar;
        substitutionsBuilt = false;
        ensureOutvar();
    }

    public String getOutvar() {
        return outvar;
    }

    public boolean isPretty() {
        return pretty;
    }

    public void setPretty(boolean pretty) throws IOException {
        this.pretty = pretty;
        render();
    }

    public String getPath() {
        return path;
    }

    protected void render() throws IOException {
        raw = Jade4J.render(loadTemplate(), pretty, true);
    }

    protected abstract JadeTemplate loadTemplate() throws IOException;

    protected void ensureOutvar() {
        if (outvar == null) {
            outvar = "_jadeout";
        }
        buildSubstitutions();
    }

    protected void buildSubstitutions() {
        if (substitutionsBuilt) {
            return;
        }
        tokens.put(EXERC1, SXMER1);
        tokens.put(EXSCC4, SXSIS4);
        tokens.put(EXPCS5, SXBIS5);
        tokens.put(EXCAR7, SXCAR7);
        
        sxCom2 = "]; " + outvar + ".concat(($1).to_s); " + outvar + ".concat %Q[";
        tokens.put(EXCRE2, sxCom2);
        sxCma3 = "]; " + outvar + ".concat(($1).to_s); " + outvar + ".concat %Q[";
        tokens.put(EXCOR3, sxCma3);
        sxScp6 = "; " + outvar + ".concat %Q[";
        tokens.put(EXSCP6, sxScp6);
        sxSta8 = "]; end;  " + outvar + ".concat %Q[";
        tokens.put(EXSTA8, sxSta8);
        outPre = outvar + "=''; " + outvar + ".concat %Q[";
        outSuf = "]; " + outvar;
        substitutionsBuilt = true;
    }

}
