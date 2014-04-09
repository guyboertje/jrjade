/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrjade;

import java.io.IOException;
import org.jruby.Ruby;
import org.jruby.RubyArray;
import org.jruby.RubyClass;
import org.jruby.RubyHash;
import org.jruby.RubyInteger;
import org.jruby.RubyNil;
import org.jruby.RubyObject;
import org.jruby.RubyRegexp;
import org.jruby.RubyString;
import org.jruby.RubySymbol;
import org.jruby.anno.JRubyMethod;
import org.jruby.ast.Node;
import org.jruby.exceptions.RaiseException;
import org.jruby.parser.StaticScope;
import org.jruby.runtime.Block;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.Visibility;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.scope.ManyVarsDynamicScope;
import org.jruby.util.RegexpOptions;

/**
 *
 * @author guy
 */
public class JadeCompiler extends RubyObject {

    private static final long MIDDLE = 1;
    private static final String OUTVAR = "@j_j";

    private JadeBaseCompiler compiler;

    private IRubyObject src;
    private IRubyObject outvar;
    private IRubyObject path;
    private IRubyObject pretty;

    public JadeCompiler(final Ruby runtime, RubyClass rubyClass) {
        super(runtime, rubyClass);
    }

    @JRubyMethod(name = "new", required = 1, optional = 1, meta = true)
    public static JadeCompiler newInstance(IRubyObject self, IRubyObject[] args, Block block) {
        JadeCompiler compiler = (JadeCompiler) ((RubyClass) self).allocate();
        compiler.callInit(args, block);
        return compiler;
    }

    @JRubyMethod(required = 1, optional = 1, visibility = Visibility.PRIVATE)
    public void initialize(ThreadContext context, IRubyObject[] args) {
        boolean argsOk = false;
        RubyHash _opts;
        IRubyObject _src;

        if (args[0] instanceof RubyString) {
            _src = args[0];
            argsOk = true;
        } else {
            _src = context.nil;
        }

        if (args.length > 1 && args[1] instanceof RubyHash) {
            _opts = (RubyHash) args[1];
        } else {
            _opts = RubyHash.newSmallHash(context.runtime);
        }
        if (!argsOk) {
            throw context.runtime.newArgumentError("Wrong type/number of arguments, see the documentation");
        }
        useOptions(context, _src, _opts);
    }

    public void useOptions(ThreadContext context, IRubyObject src, RubyHash options) {

        path = options.op_aref(context, toSym(context.runtime, "path"));
        outvar = options.op_aref(context, toSym(context.runtime, "outvar"));
        pretty = options.op_aref(context, toSym(context.runtime, "pretty"));

        if (isGiven(src)) {
            this.src = (RubyString) src;
            compiler = new JadeStringCompiler(this.src.toString());
        } else if (isGiven(path)) {
            compiler = new JadeFileCompiler(path.toString());
        } else {
            throw context.runtime.newArgumentError("Must supply either source or path");
        }

        compiler.setPretty(isGiven(pretty) ? pretty.isTrue() : true);
    }

    @JRubyMethod(name = "set_outvar")
    public IRubyObject setOutvar(ThreadContext context, IRubyObject outvar) {
        this.outvar = outvar;
        return context.nil;
    }

    public String filename() {
        if (path.isNil()) {
            return "(jade)";
        }
        return path.convertToString().toString();
    }

    @JRubyMethod
    public RubyString convert(ThreadContext context) {
        String raw;
        String output = "";
        if (compiler == null) {
            throw context.runtime.newArgumentError("Must supply either source or path in the options");
        }
        try {
            output = compiler.render();
        } catch (IOException e) {
            throw context.runtime.newIOErrorFromException(e);
        }
        raw = "@j_j=''; @j_j.concat %Q[" + output + "]; @j_j";
        RubyString res = convertLevels(context, raw);
        return res;
    }

    private RubyString convertLevels(ThreadContext context, String raw) {
        RubyString str = rubyString(context.runtime, raw);
        RubyArray levels = findLevels(context, str);

        str = treat(context, str, levels);
        if (!outvar.isNil()) {
            str.gsub_bang(context, rubyString(context.runtime, OUTVAR), outvar, Block.NULL_BLOCK);
        }
        return str;
    }

    private RubyString treat(ThreadContext context, RubyString str, RubyArray levels) {
        IRubyObject isl = levels.shift(context);
        if (isl instanceof RubyNil) {
            return str;
        }
        RubyString sl = (RubyString)isl;
        long level = numberize(sl).getLongValue();
        LevelRegex rex = new LevelRegex(level);

        IRubyObject[] parts = partition(context, str, rex.parts(context.runtime));
        if (levels.size() > 0) {
            parts[1] = treat(context, (RubyString) parts[1], levels);
        }
        parts[1] = parseTreat(context, (RubyString) parts[1], rex);

        RubyString ret = unpartition(context, parts);
        return ret;
    }

    private RubyString parseTreat(ThreadContext context, RubyString subj, LevelRegex rex) {
        int looping = 0;
        while (!parses(context, subj)) {
            if (looping > 24) {
                throw context.runtime.newSyntaxError("Unable to parse template section");
            }
            RubyRegexp re = rex.choose(context.runtime, subj.getBytes());
            IRubyObject[] pieces = partition(context, subj, re);

            RubyString piece = (RubyString) pieces[1];
            piece.sub_bang(context, rex.for_end(context.runtime), rubyString(context.runtime, "end;"), Block.NULL_BLOCK);
            piece.gsub_bang(context, rex.for_all(context.runtime), RubyString.newEmptyString(context.runtime), Block.NULL_BLOCK);
            pieces[1] = piece;

            subj = unpartition(context, pieces);
            looping++;
        }
        return subj;
    }

    private boolean parses(ThreadContext context, RubyString subj) {
        try {
            StaticScope topStaticScope = context.runtime.getStaticScopeFactory().newLocalScope(null);
            Node node = context.runtime.parseEval(subj.toString(), "-- snippet --", new ManyVarsDynamicScope(topStaticScope, null), 0);
            return true;
        } catch (RaiseException e) {
        }
        return false;
    }

    private RubyArray findLevels(ThreadContext context, RubyString str) {
        RubyRegexp regex = RubyRegexp.newRegexp(context.runtime, ";;(\\d+);", RegexpOptions.NULL_OPTIONS);
        RubyArray arr = (RubyArray) str.scan(context, regex, Block.NULL_BLOCK);
        arr = (RubyArray) arr.flatten(context);
        arr = (RubyArray) arr.compact();
        arr = (RubyArray) arr.sort(context, Block.NULL_BLOCK);
        arr = (RubyArray) arr.uniq(context);
        return arr;
    }

    private RubyInteger numberize(RubyString str) {
        return (RubyInteger) str.to_i();
    }

    private boolean isGiven(IRubyObject val) {
        return !val.isNil();
    }

    private RubySymbol toSym(Ruby runtime, String string) {
        return runtime.newSymbol(string);
    }

    private RubyString rubyString(Ruby runtime, String string) {
        return RubyUtils.rubyString(runtime, string);
    }

    private IRubyObject[] partition(ThreadContext context, RubyString str, RubyRegexp re) {
        return ((RubyArray) str.partition(context, re, Block.NULL_BLOCK)).toJavaArray();
    }

    private RubyString unpartition(ThreadContext context, IRubyObject[] parts) {
        return RubyString.newUTF8String(context.runtime, "").concat(parts[0]).concat(parts[1]).concat(parts[2]);
    }

}
