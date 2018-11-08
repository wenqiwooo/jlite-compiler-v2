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
            Lexer lexer = new Lexer(new FileReader(args[0]));
            parser p = new parser(lexer);
            ProgramNode ast = (ProgramNode)p.parse().value;
            // System.out.print(ast);
            
            Ir3Builder ir3 = new Ir3Builder(ast.toClassBundles());
            TypeCheckHelper.checkType(ir3, ast);

            ArmProgram armProgram = ir3.toArm();

            System.out.println(ir3.toCode());
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}