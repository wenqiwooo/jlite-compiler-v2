package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.arm.*;
import wwu.compiler.exception.CodeGenerationException;

public class Ir3ReturnStmt extends Ir3Stmt {
    Ir3Identifier arg;

    Set<String> useSymbols;

    public Ir3ReturnStmt(Ir3Identifier arg) {
        this.arg = arg;

        useSymbols = new HashSet<>();
        this.arg.addUseSymbols(useSymbols);
    }

    @Override
    public String toString() {
        if (arg == null) {
            return "return;";
        }
        return "return " + arg.toString() + ";"; 
    }

    @Override
    public Set<String> getUse() {
        return useSymbols;
    }

    @Override
    void buildArm(Ir3MdBuilder.ArmMdBuilder mdBuilder, ClassTypeProvider classTypeProvider) 
            throws CodeGenerationException {
        if (arg != null) {
            ArmReg r0 = mdBuilder.getReg(ArmRegisterType.REG_0);
            ArmReg armReg = arg.getArmReg(r0, mdBuilder, classTypeProvider);
            if (armReg != r0) {
                mdBuilder.addInsn(new ArmMov(r0, armReg));
            }
        }
        mdBuilder.addInsn(new ArmBranch(ArmBranch.Mode.B, mdBuilder.getMethodExitLabel()));
    }
}