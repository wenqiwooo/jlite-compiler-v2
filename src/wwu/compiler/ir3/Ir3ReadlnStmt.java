package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.arm.*;
import wwu.compiler.exception.CodeGenerationException;

public class Ir3ReadlnStmt extends Ir3Stmt {
    Ir3Id arg;
    
    String defSymbol;

    public Ir3ReadlnStmt(Ir3Id arg) {
        this.arg = arg;

        Set<String> defSymbols = new HashSet<>();
        this.arg.addUseSymbols(defSymbols);
        defSymbol = !defSymbols.isEmpty() 
            ? defSymbols.iterator().next() 
            : null;
    }

    @Override
    public String toString() {
        return "readln(" + arg.toString() + ");";
    }

    @Override
    public String getDef() {
        return defSymbol;
    }

    @Override
    void buildArm(Ir3MdBuilder.ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) throws CodeGenerationException {
        throw new CodeGenerationException("readln() is not supported");
    }
}
