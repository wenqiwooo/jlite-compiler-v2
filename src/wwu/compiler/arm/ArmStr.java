package wwu.compiler.arm;

import java.util.*;

public class ArmStr extends ArmInsn {
    // If destMem is null, this is a push operation
    ArmMem destMem;
    List<ArmReg> srcRegs;

    public ArmStr(ArmMem destMem, ArmReg srcReg) {
        this.destMem = destMem;
        this.srcRegs = new ArrayList<>();
        this.srcRegs.add(srcReg);
    }

    public ArmStr(ArmMem destMem, List<ArmReg> srcRegs) {
        this.destMem = destMem;
        this.srcRegs = srcRegs;
    }

    public static ArmStr push(ArmReg srcReg) {
        return new ArmStr(null, srcReg);
    }

    public static ArmStr push(List<ArmReg> srcRegs) {
        return new ArmStr(null, srcRegs);
    }
}