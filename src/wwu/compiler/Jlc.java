package wwu.compiler;

import java.io.*;

import wwu.compiler.ast.*;
import wwu.compiler.ir3.*;
import wwu.compiler.exception.*;

// JLite compiler
class Jlc {

    public static void main(String[] args) throws Exception {
        try {
            Lexer lexer = new Lexer(new FileReader(args[0]));
            parser p = new parser(lexer);
            ProgramNode pg = (ProgramNode)p.parse().value;

            // System.out.print(pg);
            
            Ir3Builder irBuilder = new Ir3Builder();
            irBuilder.initialize(pg.toClassBundles());
            TypeCheckHelper.checkType(irBuilder, pg);
            System.out.println(irBuilder.toCode());
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}