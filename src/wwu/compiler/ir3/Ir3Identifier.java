package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.arm.*;
import wwu.compiler.exception.*;

public class Ir3Identifier extends Ir3BasicId {
    String varName;
    String type;

    public Ir3Identifier(String varName, String type) {
        this.varName = varName;
        this.type = type;
    }

    @Override
    public String toString() {
        return varName;
    }

    @Override
    void addUseSymbols(Set<String> symbols) {
        symbols.add(varName);
    }

    @Override
    String getType() {
        return type;
    }

    @Override 
    ArmReg getArmReg(ArmReg backupReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        VarLocation loc = mdBuilder.getLocationForSymbol(varName);
        if (loc.inReg()) {
            return loc.getReg();
        } else if (backupReg != null) {
            // Identifier will be refed by sp or fp
            ArmMem armMem = loc.getMem().getArmMem(backupReg, mdBuilder);
            mdBuilder.addInsn(new ArmLdr(backupReg, armMem));
            return backupReg;
        }
        return null;
    }

    @Override
    ArmReg tryGetArmReg(ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        return getArmReg(null, mdBuilder, classTypeProvider);
    }

    @Override
    ArmOperand getArmOperand(ArmReg backupReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        return getArmReg(backupReg, mdBuilder, classTypeProvider);
    }

    @Override
    ArmOperand tryGetArmOperand(ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        return tryGetArmReg(mdBuilder, classTypeProvider);
    }

    void buildArmForAssignStmt(ArmReg destReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) throws CodeGenerationException {
        ArmReg src = getArmReg(mdBuilder.getTempReg1(), 
                mdBuilder, classTypeProvider);
        mdBuilder.addInsn(new ArmMov(destReg, src));
    }
}