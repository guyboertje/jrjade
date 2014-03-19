/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrjade;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.jruby.Ruby;
import org.jruby.RubyBoolean;
import org.jruby.RubyClass;
import org.jruby.RubyHash;
import org.jruby.RubyObject;
import org.jruby.RubyString;
import org.jruby.RubySymbol;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.Block;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.Visibility;
import org.jruby.runtime.builtin.IRubyObject;

/**
 *
 * @author guy
 */
public class JadeCompiler extends RubyObject {

    private JadeBaseCompiler _compiler;

    public JadeCompiler(final Ruby runtime, RubyClass rubyClass) {
        super(runtime, rubyClass);
    }

    @JRubyMethod(name = "new", required = 1, optional = 1, meta = true)
    public static JadeCompiler newInstance(ThreadContext context, IRubyObject self, IRubyObject[] args) {
        JadeCompiler compiler = (JadeCompiler) ((RubyClass) self).allocate();
        compiler.callInit(args, Block.NULL_BLOCK);
        return compiler;
    }

    @JRubyMethod(required = 1, optional = 1, visibility = Visibility.PRIVATE)
    public void initialize(ThreadContext context, IRubyObject[] args) {
        Ruby ruby = context.getRuntime();
        IRubyObject _src = ruby.getNil();
        IRubyObject _path = ruby.getNil();
        IRubyObject _outvar = ruby.getNil();
        IRubyObject _pretty = ruby.getNil();

        if (args[0] instanceof RubyString) {
            _src = (RubyString) args[0];
        }

        RubyHash _opts;
        if (args.length > 1 && args[1] instanceof RubyHash) {
            _opts = (RubyHash) args[1];
            _path = _opts.op_aref(context, toSym(ruby, "path"));
            _outvar = _opts.op_aref(context, toSym(ruby, "outvar"));
            _pretty = _opts.op_aref(context, toSym(ruby, "pretty"));
        }
        if (isGiven(_src)) {
            _compiler = new JadeStringCompiler(_src.toString());
        } else if (isGiven(_path)) {
            _compiler = new JadeFileCompiler(_path.toString());
        } else {
            throw ruby.newStandardError("Must supply either source or path");
        }
        _compiler.setOutvar(
            isGiven(_outvar) ? _outvar.toString() : "_jadeout"
        );

        try {
            _compiler.setPretty(
                    isGiven(_pretty) ? _pretty.isTrue() : true
            );
        }
        catch (IOException e) {
            throw ruby.newIOErrorFromException(e);
        }
    }
    @JRubyMethod
    public RubyString convert(ThreadContext context){
        try {
            return rubyString(context, _compiler.convert());
        }
        catch (IOException e) {
            throw context.getRuntime().newIOErrorFromException(e);
        }
    }
    
    private boolean isGiven(IRubyObject val) {
        return !val.isNil();
    }
    
    private RubySymbol toSym(Ruby runtime, String string) {
        return runtime.newSymbol(string);
    }

    private RubyString rubyString(ThreadContext context, String string) {
        return RubyUtils.rubyString(context.getRuntime(), string);
    }

    private RubyBoolean rubyBoolean(ThreadContext context, boolean bool) {
        return RubyUtils.rubyBoolean(context.getRuntime(), bool);
    }

}
