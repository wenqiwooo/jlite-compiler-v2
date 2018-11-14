package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.arm.*;
import wwu.compiler.exception.*;

public class Ir3Field extends Ir3Id {
    Ir3Identifier parent;
    String fieldName;

    public Ir3Field(Ir3Identifier parent, String fieldName) {
        this.parent = parent;
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return parent.toString() + "." + fieldName;
    }

    @Override
    void addUseSymbols(Set<String> symbols) {
        parent.addUseSymbols(symbols);
    }

    @Override
    void buildArmForAssignStmt(ArmReg destReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) throws CodeGenerationException {
        ArmReg armReg = parent.getArmReg(mdBuilder.getTempReg1(), 
                mdBuilder, classTypeProvider);
        
        int n = classTypeProvider.getClassFieldOffset(parent.type, fieldName);
        ArmMem srcMem = new ArmMem(armReg, new ArmImmediate(n));
        
        mdBuilder.addInsn(new ArmLdr(destReg, srcMem));
    }

    ArmReg getFieldInReg(ArmReg destReg, ArmReg backupReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        ArmReg parentReg = parent.getArmReg(backupReg, mdBuilder, classTypeProvider);
        int offset = classTypeProvider.getClassFieldOffset(parent.type, fieldName);
        ArmMem srcMem = new ArmMem(parentReg, new ArmImmediate(offset));
        mdBuilder.addInsn(new ArmLdr(destReg, srcMem));
        return destReg;
    }
}