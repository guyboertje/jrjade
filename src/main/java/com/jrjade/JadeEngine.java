/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrjade;

import java.io.IOException;
import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyHash;
import org.jruby.RubyObject;
import org.jruby.RubyString;
import org.jruby.RubySymbol;
import org.jruby.anno.JRubyClass;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.Block;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.Visibility;
import org.jruby.runtime.builtin.IRubyObject;

/**
 *
 * @author guy
 */
@JRubyClass(name = "JrJade::JadeEngine", parent = "Object")
public class JadeEngine extends RubyObject {

    private IRubyObject filename;
    private IRubyObject safeLevel;
    private IRubyObject outvar;
    private IRubyObject src;
    
    private JadeBaseCompiler compiler;

    public JadeEngine(final Ruby runtime, RubyClass rubyClass) {
        super(runtime, rubyClass);
    }

    @JRubyMethod(name = "new", required = 1, optional = 2, meta = true)
    public static JadeEngine newInstance(ThreadContext context, IRubyObject self, IRubyObject[] args) {
        JadeEngine engine = (JadeEngine) ((RubyClass) self).allocate();
        engine.callInit(args, Block.NULL_BLOCK);
        return engine;
    }

    @JRubyMethod(required = 1, optional = 2, visibility = Visibility.PRIVATE)
    public void initilize(ThreadContext context, IRubyObject[] args) {
        final Ruby ruby = context.getRuntime();
        
        src = ruby.getNil();

        IRubyObject pretty;
        IRubyObject string = args[0];

        safeLevel = args.length > 1 ? args[1] : ruby.getNil();

        RubyHash options = (args.length > 2 && args[2] instanceof RubyHash)
                ? (RubyHash) args[2] : RubyHash.newSmallHash(ruby);

        outvar = options.op_aref(context, asSym(ruby, "outvar"));
        filename = options.op_aref(context, asSym(ruby, "path"));
        pretty = options.op_aref(context, asSym(ruby, "pretty"));
        compiler = isGiven(filename)
                ? new JadeFileCompiler(filename.toString())
                : new JadeStringCompiler(string.toString());
        try {
            compiler.setPretty(
                    isGiven(pretty) ? pretty.isTrue() : true
            );
        } catch (IOException e) {
            throw ruby.newIOErrorFromException(e);
        }
        compiler.setOutvar(
                isGiven(outvar) ? outvar.toString() : "_jadeout"
        );
    }
    
    @JRubyMethod(name = "src")
    public IRubyObject getSrc(ThreadContext context) {
        if (src.isNil()) {
            try {
               src = rubyString(context, compiler.convert()); 
            } catch (IOException e) {
                context.getRuntime().newIOErrorFromException(e);
            }
        }
        return src;
    }

    @JRubyMethod(meta = true, required = 1, optional = 1)
    public static RubyString render(final ThreadContext context, IRubyObject self, IRubyObject[] args) {
        final Ruby ruby = context.getRuntime();
        IRubyObject string = args[0];
        RubyHash options = RubyHash.newSmallHash(ruby);
        if (args.length > 1) {
            options = args[1].convertToHash();
        }
        IRubyObject level = options.op_aref(context, toSym(ruby, "safe_level"));

        JadeEngine instance = (JadeEngine) newInstance(context, self,
                new IRubyObject[]{string, level, options}
        );

        return instance.result(context);
    }

    public RubyString result(ThreadContext context) {
        return rubyString(context, "");
    }

    private static boolean isGiven(IRubyObject val) {
        return !val.isNil();
    }

    private static RubySymbol toSym(Ruby runtime, String string) {
        return runtime.newSymbol(string);
    }

    private RubySymbol asSym(Ruby runtime, String string) {
        return runtime.newSymbol(string);
    }

    private RubyString rubyString(ThreadContext context, String string) {
        return RubyUtils.rubyString(context.getRuntime(), string);
    }

}
