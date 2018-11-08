package wwu.compiler.ir3;

import wwu.compiler.arm.*;

class VarLocation {
    String varName;
    /**
     * For now, armReg and armMem must be exclusively set, i.e.
     * one must be null, one must be non-null.
     */
    ArmReg armReg;
    ArmMem armMem;
    
    VarLocation(String varName, ArmReg reg) {
        this(varName, reg, null);
    }

    VarLocation(String varName, ArmMem mem) {
        this(varName, null, mem);
    }

    private VarLocation(String varName, ArmReg reg, ArmMem mem) {
        this.varName = varName;
        this.armReg = reg;
        this.armMem = mem;
    }

    void updateLocation(ArmReg reg) {
        this.armReg = reg;
        this.armMem = null;
    }

    void updateLocation(ArmMem mem) {
        this.armMem = mem;
        this.armReg = null;
    }

    boolean inReg() {
        return armReg != null;
    }

    boolean inMem() {
        return armMem != null;
    }

    ArmReg getReg() {
        return armReg;
    }

    ArmMem getMem() {
        return armMem;
    }
}