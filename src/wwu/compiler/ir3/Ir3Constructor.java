package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.arm.*;

public class Ir3Constructor extends Ir3Expr {
    private static final String ALLOC_BL_TARGET = "_Znwj(PLT)";

    String type;

    public Ir3Constructor(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("new %s()", type);
    }

    @Override
    void buildArmForAssignStmt(ArmReg destReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        List<ArmReg> regs = mdBuilder.getScratchRegs();
        regs.remove(destReg);
        mdBuilder.addInsn(ArmStm.push(regs));

        ArmReg r0 = mdBuilder.getReg(ArmRegisterType.REG_0);
        ArmOperand allocSize = new ArmImmediate(classTypeProvider.getClassSize(type));
        mdBuilder.addInsn(new ArmMov(r0, allocSize));
        mdBuilder.addInsn(new ArmBranch(ArmBranch.Mode.BL, ALLOC_BL_TARGET));
        mdBuilder.addInsn(new ArmMov(destReg, r0));
        
        mdBuilder.addInsn(ArmLdm.pop(regs));
    }
}