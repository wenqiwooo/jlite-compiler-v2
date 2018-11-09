package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.exception.*;
import wwu.compiler.arm.*;

public abstract class Ir3Expr extends Ir3Base {

    void addUseSymbols(Set<String> symbols) {
        // Add a list of the symbols that are used by the expression.
    }

    abstract void buildArmForAssignStmt(ArmReg destReg, 
            Ir3MdBuilder.ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) 
            throws CodeGenerationException;
}