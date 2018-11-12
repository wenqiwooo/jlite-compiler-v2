package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.arm.*;
import wwu.compiler.common.*;
import wwu.compiler.exception.*;

public class Ir3PrintlnStmt extends Ir3Stmt {
    Ir3BasicId arg;

    Set<String> useSymbols;
    
    public Ir3PrintlnStmt(Ir3BasicId arg) {
        this.arg = arg;

        useSymbols = new HashSet<>();
        this.arg.addUseSymbols(useSymbols);
    }

    @Override
    public String toString() {
        return "println(" + arg.toString() + ");";
    }

    // @Override
    // public boolean hasAdditionalSideEffects() {
    //     return true;
    // }

    public Set<String> getUseSymbols() {
        return useSymbols;
    }

    @Override
    void buildArm(ArmMdBuilder mdBuilder, ClassTypeProvider classTypeProvider) 
            throws CodeGenerationException {
        String argType = arg.getType();
        ArmReg r0 = mdBuilder.getReg(ArmRegisterType.REG_0);

        // Save scratch registers
        List<ArmReg> scratchRegs = mdBuilder.getScratchRegs();
        mdBuilder.addInsn(ArmStm.push(scratchRegs));

        if (argType.equals(Type.STRING)) {
            ArmReg armReg = arg.getArmReg(r0, mdBuilder, classTypeProvider);
            if (armReg != r0) {
                mdBuilder.addInsn(new ArmMov(r0, armReg));
            }
        }
        else { // Bool or Int
            ArmReg r1 = mdBuilder.getReg(ArmRegisterType.REG_1);
            ArmReg armReg = arg.getArmReg(r1, mdBuilder, classTypeProvider);
            if (armReg != r1) {
                mdBuilder.addInsn(new ArmMov(r1, armReg));
            }
            
            String label = classTypeProvider.addGlobalLiteral("%i");
            ArmLdrConstruct ldrOperand = new ArmLdrConstruct(label);
            mdBuilder.addInsn(new ArmLdr(r0, ldrOperand));
        }

        mdBuilder.addInsn(new ArmBranch(ArmBranch.Mode.BL, "printf(PLT)"))
                .addInsn(ArmLdm.pop(scratchRegs));
    }
}
