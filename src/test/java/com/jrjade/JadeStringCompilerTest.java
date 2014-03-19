/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jrjade;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author guy
 */
public class JadeStringCompilerTest {
    
    @Test
    public void testConvert() throws Exception {
        System.out.println("convert");
        String input = "p.line= foo";
        JadeStringCompiler instance = new JadeStringCompiler(input);
        String expResult = "";
        String result = instance.convert();
        System.out.println(result);
        assertEquals(expResult, result);
    }
    
}
