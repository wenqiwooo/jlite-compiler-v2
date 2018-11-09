package wwu.compiler.ir3;

import wwu.compiler.arm.*;
import wwu.compiler.exception.*;

public class Ir3GotoStmt extends Ir3Stmt {
    String label;

    public Ir3GotoStmt(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "goto " + label + ";";
    }

    void buildArm(Ir3MdBuilder.ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) throws CodeGenerationException {
        mdBuilder.addInsn(new ArmBranch(ArmBranch.Mode.B, label));
    }
}