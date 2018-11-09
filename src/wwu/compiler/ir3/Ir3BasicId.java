package wwu.compiler.ir3;

import wwu.compiler.arm.*;

public abstract class Ir3BasicId extends Ir3Id {

    /**
     * These methods are used to get the ArmReg or ArmOperand (ArmReg or ArmImmediate)
     * for the variable or literal.
     * 
     * There are several cases that make this not so straight forward.
     *
     * 1. Get ArmReg when variable is in memory.
     *    If backupReg is available, variable is loaded into backupReg and backupReg 
     *    returned.
     * 
     * 2. Get ArmOperand when literal cannot be an immediate.
     *    If backupReg is available, literal is loaded into backupReg using ldr construct
     *    and backupReg is returned.
     * 
     * When backupReg is unavailable, null is returned if Ir3BasicId does not have 
     * a ArmReg or ArmImmediate.
     * 
     * For getArmOperand, Ir3Identifier always returns a ArmReg, 
     * Ir3Literal can return a ArmReg or ArmImmediate.
     * 
     */

    abstract ArmReg getArmReg(ArmReg backupReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider);

    abstract ArmReg tryGetArmReg(ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider);

    abstract ArmOperand getArmOperand(ArmReg backupReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider);

    abstract ArmOperand tryGetArmOperand(ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider);
    
    abstract String getType();
}