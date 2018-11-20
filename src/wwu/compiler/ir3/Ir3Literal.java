package wwu.compiler.ir3;

import java.util.HashSet;

import wwu.compiler.common.*;
import wwu.compiler.arm.*;
import wwu.compiler.exception.*;

public class Ir3Literal extends Ir3BasicId {
    Object value;
    String valueType;

    public Ir3Literal(Object value, String valueType) {
        this.value = value;
        this.valueType = valueType;
    }

    @Override
    public String toString() {
        if (valueType.equals(Type.STRING)) {
            return "\"" + (String)value +  "\"";
        }
        return String.valueOf(value);
    }

    @Override
    String getType() {
        return valueType;
    }

    @Override
    ArmReg getArmReg(ArmReg backupReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        ArmOperand armOperand = getArmOperand(backupReg, mdBuilder, classTypeProvider);
        if (armOperand instanceof ArmReg) {
            return (ArmReg)armOperand;
        } else {
            mdBuilder.addInsn(new ArmMov(backupReg, armOperand));
            return backupReg;
        }
    }

    @Override
    ArmReg tryGetArmReg(ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        ArmOperand armOperand = tryGetArmOperand(mdBuilder, classTypeProvider);
        if (armOperand != null && armOperand instanceof ArmReg) {
            return (ArmReg)armOperand;
        }
        return null;
    }

    @Override
    ArmOperand getArmOperand(ArmReg backupReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        if (valueType.equals(Type.BOOL)) {
            return new ArmImmediate((boolean)value ? 1 : 0);
        }
        
        if (valueType.equals(Type.INT)) {
            if (ArmHelper.isValidDataImmediate(value)) {
                return new ArmImmediate((int)value);
            }
            if (backupReg != null) {
                ArmLdrConstruct ldrOperand = new ArmLdrConstruct((int)value);
                mdBuilder.addInsn(new ArmLdr(backupReg, ldrOperand));
            }
            return backupReg;
        } 
        
        if (backupReg != null) { // String
            ArmLdrConstruct ldrOperand = new ArmLdrConstruct(
                    classTypeProvider.addGlobalLiteral((String)value));
            mdBuilder.addInsn(new ArmLdr(backupReg, ldrOperand));
        }
        return backupReg;
    }

    @Override
    ArmOperand tryGetArmOperand(ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        return getArmOperand(null, mdBuilder, classTypeProvider);
    }

    @Override
    void buildArmForAssignStmt(ArmReg destReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) throws CodeGenerationException {
        getArmReg(destReg, mdBuilder, classTypeProvider);
    }
}