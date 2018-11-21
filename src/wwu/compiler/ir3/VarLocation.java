package wwu.compiler.ir3;

import wwu.compiler.arm.*;

class VarLocation {
    String varName;
    /**
     * For now, armReg and mem must be exclusively set, i.e.
     * one must be null, one must be non-null.
     */
    ArmReg armReg;
    Ir3Mem mem;
    
    VarLocation(String varName, ArmReg reg) {
        this(varName, reg, null);
    }

    VarLocation(String varName, Ir3Mem mem) {
        this(varName, null, mem);
    }

    private VarLocation(String varName, ArmReg reg, Ir3Mem mem) {
        this.varName = varName;
        this.armReg = reg;
        this.mem = mem;
    }

    void updateLocation(ArmReg reg) {
        this.armReg = reg;
        this.mem = null;
    }

    void updateLocation(Ir3Mem mem) {
        this.mem = mem;
        this.armReg = null;
    }

    boolean inReg() {
        return armReg != null;
    }

    boolean inMem() {
        return mem != null;
    }

    ArmReg getReg() {
        return armReg;
    }

    Ir3Mem getMem() {
        return mem;
    }
}