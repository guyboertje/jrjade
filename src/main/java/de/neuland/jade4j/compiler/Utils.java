package de.neuland.jade4j.compiler;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;

import de.neuland.jade4j.exceptions.ExpressionException;
import de.neuland.jade4j.expression.ExpressionHandler;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.parser.node.ExpressionString;

public class Utils {

    public static Pattern interpolationPattern = Pattern.compile("(\\\\)?([#!])\\{(.*?)\\}");

    public static List<Object> ignoreInterpolate(String string, boolean xmlEscape) {
        List<Object> result = new LinkedList<Object>();
        result.add(string);
        return result;
    }

    public static List<Object> prepareInterpolate(String string, boolean xmlEscape) {
        List<Object> result = new LinkedList<Object>();

        Matcher matcher = interpolationPattern.matcher(string);
        int start = 0;
        while (matcher.find()) {
            String before = string.substring(start, matcher.start(0));
            if (xmlEscape) {
                before = escapeHTML(before);
            }
            result.add(before);

            boolean escape = matcher.group(1) != null;
            String flag = matcher.group(2);
            String code = matcher.group(3);

            if (escape) {
                String escapedExpression = matcher.group(0).substring(1);
                if (xmlEscape) {
                    escapedExpression = escapeHTML(escapedExpression);
                }
                result.add(escapedExpression);
            } else {
                ExpressionString expression = new ExpressionString(code);
                if (flag.equals("#")) {
                    expression.setEscape(true);
                }
                result.add(expression);
            }
            start = matcher.end(0);
        }
        String last = string.substring(start);
        if (xmlEscape) {
            last = escapeHTML(last);
        }
        result.add(last);

        return result;
    }

    public static String interpolate(List<Object> prepared, ScriptWriter writer) throws ExpressionException {
        StringBuilder result = new StringBuilder();
        for (Object entry : prepared) {
            if (entry instanceof String) {
                result.append((String) entry);
            } else if (entry instanceof ExpressionString) {
                ExpressionString expression = (ExpressionString) entry;
                if (expression.isEscape()) {
                   result.append(writer.wrapForEscapedValue(expression.getValue())); 
                } else {
                    result.append(writer.wrapForInsert(expression.getValue()));
                }              
            }
        }
        return result.toString();
    }

    public static String interpolate(List<Object> prepared, JadeModel model) throws ExpressionException {
        StringBuilder result = new StringBuilder();

        for (Object entry : prepared) {
            if (entry instanceof String) {
                result.append(entry);
            } else if (entry instanceof ExpressionString) {
                ExpressionString expression = (ExpressionString) entry;
                String stringValue = "";
                String value = ExpressionHandler.evaluateStringExpression(expression.getValue(), model);
                if (value != null) {
                    stringValue = value;
                }
                if (expression.isEscape()) {
                    stringValue = escapeHTML(stringValue);
                }
                result.append(stringValue);
            }
        }

        return result.toString();
    }

    private static String escapeHTML(String string) {
        return StringEscapeUtils.escapeHtml4(string);
    }

    public static String interpolate(String string, JadeModel model, boolean escape) throws ExpressionException {
        return interpolate(prepareInterpolate(string, escape), model);
    }

    public static String interpolate(String string, boolean escape, ScriptWriter writer) throws ExpressionException {
        return interpolate(prepareInterpolate(string, escape), writer);
    }
}
