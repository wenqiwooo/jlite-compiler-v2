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
        loadToReg(destReg, mdBuilder, classTypeProvider);
    }

    ArmReg getFieldInReg(ArmReg destReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        loadToReg(destReg, mdBuilder, classTypeProvider);
        return destReg;
    }

    private void loadToReg(ArmReg destReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        ArmReg armReg = parent.getArmReg(mdBuilder.getTempReg1(), 
                mdBuilder, classTypeProvider);
        int offset = classTypeProvider.getClassFieldOffset(parent.type, fieldName);

        ArmMem srcArmMem = new Ir3Mem(armReg, offset).getArmMem(mdBuilder.getTempReg2(), mdBuilder);
        mdBuilder.addInsn(new ArmLdr(destReg, srcArmMem));
    }
}