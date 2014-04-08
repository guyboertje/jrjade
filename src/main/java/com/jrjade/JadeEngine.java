/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrjade;

import org.jruby.Ruby;
import org.jruby.RubyBinding;
import org.jruby.RubyClass;
import org.jruby.RubyHash;
import org.jruby.RubyKernel;
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

    private JadeCompiler compiler;

    public JadeEngine(final Ruby runtime, RubyClass rubyClass) {
        super(runtime, rubyClass);
    }

    @JRubyMethod(name = "new", required = 1, optional = 2, meta = true)
    public static JadeEngine newInstance(IRubyObject self, IRubyObject[] args) {
        JadeEngine engine = (JadeEngine) ((RubyClass) self).allocate();
        engine.callInit(args, Block.NULL_BLOCK);
        return engine;
    }

    @JRubyMethod(required = 1, optional = 2, visibility = Visibility.PRIVATE)
    public void initialize(ThreadContext context, IRubyObject[] args) {

        src = context.nil;
        IRubyObject string = args[0];

        safeLevel = args.length > 1 ? args[1] : context.nil;

        RubyHash options = (args.length > 2 && args[2] instanceof RubyHash)
                ? (RubyHash) args[2] : RubyHash.newSmallHash(context.runtime);

        compiler = new JadeCompiler(context.runtime, context.runtime.getModule("JrJade").getClass("JadeCompiler"));
        compiler.useOptions(context, string, options);
    }

    @JRubyMethod(name = "src")
    public IRubyObject getSrc(ThreadContext context) {
        if (src.isNil()) {
            src = compiler.convert(context);
        }
        return src;
    }

    @JRubyMethod(meta = true, required = 1, optional = 1)
    public static RubyString render(final ThreadContext context, IRubyObject self, IRubyObject[] args) {

        IRubyObject string = args[0];
        RubyHash options = RubyHash.newSmallHash(context.runtime);
        if (args.length > 1) {
            options = args[1].convertToHash();
        }
        IRubyObject level = options.op_aref(context, toSym(context.runtime, "safe_level"));

        JadeEngine instance = (JadeEngine) newInstance(self,
                new IRubyObject[]{string, level, options}
        );
        
        IRubyObject[] rargs = {(IRubyObject) context.nil};

        return instance.result(context, rargs);
    }
    
    @JRubyMethod(optional = 1)
    public RubyString result(ThreadContext context, IRubyObject[] args) {
        RubyBinding binding;
        RubyString _result;
        if (args[0] instanceof RubyBinding) {
            binding = (RubyBinding) args[0];
        } else {
            binding = newTopLevel(context);
        }
        if (safeLevel != context.nil) {
            throw context.runtime.newNotImplementedError("safe_level not implemented yet");
        } else {
            IRubyObject _src = getSrc(context);
            IRubyObject[] kargs = {_src, (IRubyObject) binding};
            
            _result = RubyKernel.eval(context, this, kargs, Block.NULL_BLOCK).convertToString();
        }
        return _result;
    }

    @JRubyMethod(name = "set_outvar")
    public IRubyObject setOutvar(ThreadContext context, RubyString outvar) {
        this.outvar = outvar;
        compiler.setOutvar(context, outvar);
        src = compiler.convert(context);
        return context.nil;
    }

    @JRubyMethod
    public RubyString template() {
        return src.convertToString();
    }

    private static RubySymbol toSym(Ruby runtime, String string) {
        return runtime.newSymbol(string);
    }

    private RubyBinding newTopLevel(ThreadContext context) {
        return (RubyBinding) context.runtime.fetchGlobalConstant("TOPLEVEL_BINDING").dup();
    }

}
