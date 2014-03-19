/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrjade;

import java.io.IOException;
import org.jruby.Ruby;
import org.jruby.RubyModule;
import org.jruby.RubyClass;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.load.BasicLibraryService;

/**
 *
 * @author guy
 */
public class JrjadeService implements BasicLibraryService {

    private Ruby runtime;

    @Override
    public boolean basicLoad(final Ruby runtime) throws IOException {
        this.runtime = runtime;
        RubyModule jrjade = runtime.defineModule("JrJade");

        RubyClass jadeEngine = jrjade.defineClassUnder("JadeEngine", runtime.getObject(), JADE_ENGINE_ALLOCATOR);
        jadeEngine.defineAnnotatedMethods(JadeEngine.class);

        RubyClass jadeCompiler = jrjade.defineClassUnder("JadeCompiler", runtime.getObject(),JADE_COMPILER_ALLOCATOR );
        jadeCompiler.defineAnnotatedMethods(JadeCompiler.class);
        
        return true;
    }
    
    private static final ObjectAllocator JADE_ENGINE_ALLOCATOR = new ObjectAllocator() {
        @Override
        public IRubyObject allocate(Ruby ruby, RubyClass rc) {
            return new JadeEngine(ruby, rc);
        }
    };
    
    private static final ObjectAllocator JADE_COMPILER_ALLOCATOR = new ObjectAllocator() {
        @Override
        public IRubyObject allocate(Ruby ruby, RubyClass rc) {
            return new JadeCompiler(ruby, rc);
        }
    };
    
    
}
