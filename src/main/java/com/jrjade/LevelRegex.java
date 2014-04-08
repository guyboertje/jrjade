
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrjade;

import org.joni.Option;
import org.joni.Regex;
import org.joni.Matcher;
import org.jcodings.specific.UTF8Encoding;

import org.jruby.Ruby;
import org.jruby.RubyRegexp;
import org.jruby.RubyString;

/**
 *
 * @author guy
 */
public class LevelRegex {

    private final String lup;
    private final String lui;
    private final String lud;
    private final String lue;
    private final String lua;
    private final Regex luu;

    public LevelRegex(long num) {
        String s = Long.toString(num);
        lup = ";;" + s + ";.+;" + s + ";;";
        lui = ";;" + s + ";\\s*if.+els(e|if).+?;" + s + ";;";
        lud = ";;" + s + ";.+?;" + s + ";;";
        lue = ";" + s + ";;\\z";

        lua = ";;" + s + ";|;" + s + ";;";
        byte[] buu = (";;" + s + ";\\s*if").getBytes();
        luu = new Regex(buu, 0, buu.length, Option.NONE, UTF8Encoding.INSTANCE);
    }

    private RubyRegexp newRubyRegex(Ruby runtime, String str, int option) {
        RubyString pattern = RubyUtils.rubyString(runtime, str);
        return RubyRegexp.newDRegexp(runtime, pattern, option);
    }

    public RubyRegexp choose(Ruby runtime, byte[] str) {
        Matcher m = luu.matcher(str);
        if (m.search(0, str.length, Option.DEFAULT) != -1) {
            return newRubyRegex(runtime, lui, Option.MULTILINE);
        }
        return newRubyRegex(runtime, lud, Option.MULTILINE);
    }

    public RubyRegexp parts(Ruby runtime) {
        return newRubyRegex(runtime, lup, Option.MULTILINE);
    }

    public RubyRegexp for_ifelse(Ruby runtime) {
        return newRubyRegex(runtime, lui, Option.MULTILINE);
    }

    public RubyRegexp for_rest(Ruby runtime) {
        return newRubyRegex(runtime, lud, Option.MULTILINE);
    }

    public RubyRegexp for_end(Ruby runtime) {
        return newRubyRegex(runtime, lue, Option.MULTILINE);
    }

    public RubyRegexp for_all(Ruby runtime) {
        return newRubyRegex(runtime, lua, Option.NONE);
    }
}
