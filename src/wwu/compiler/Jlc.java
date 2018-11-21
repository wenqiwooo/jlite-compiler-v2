package wwu.compiler;

import java.io.*;

import wwu.compiler.ast.*;
import wwu.compiler.ir3.*;
import wwu.compiler.arm.*;
import wwu.compiler.exception.*;

// JLite compiler
class Jlc {

    public static void main(String[] args) throws Exception {
        try {
            boolean shouldOptimize = false;
            for (int i = 1; i < args.length; i++) {
                if (args[i].equals("-opt")) {
                    shouldOptimize = true;
                }
            }

            Lexer lexer = new Lexer(new FileReader(args[0]));
            parser p = new parser(lexer);
            ProgramNode ast = (ProgramNode)p.parse().value;
            
            Ir3Builder ir3 = new Ir3Builder(ast.toClassBundles());
            TypeCheckHelper.checkType(ir3, ast);

            ArmProgram armProgram = ir3.getArmProgram();
            if (shouldOptimize) {
                armProgram.optimize();
            }
            
            System.out.println(armProgram.toString());
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}