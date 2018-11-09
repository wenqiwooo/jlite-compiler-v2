package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.arm.*;
import wwu.compiler.exception.*;

public class Ir3CallStmt extends Ir3Stmt {
    Ir3Call call;
    
    Set<String> useSymbols;

    public Ir3CallStmt(Ir3Call call) {
        this.call = call;

        useSymbols = new HashSet<>();
        this.call.addUseSymbols(useSymbols);
    }

    @Override
    public String toString() {
        return call.toString() + ";";
    }

    @Override
    public Set<String> getUse() {
        // TODO: Return all symbols in scope
        return useSymbols;
    }

    @Override
    void buildArm(ArmMdBuilder mdBuilder, ClassTypeProvider classTypeProvider) 
            throws CodeGenerationException {
        call.buildArmForAssignStmt(null, mdBuilder, classTypeProvider);
    }
}