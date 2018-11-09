package wwu.compiler.arm;

import java.util.*;

public class ArmLdr extends ArmInsn {
    List<ArmReg> destRegs;
    // If srcMem is null, this is a pop operation.
    ArmLoadable srcMem;

    public ArmLdr(ArmReg destReg, ArmLoadable srcMem) {
        this.destRegs = new ArrayList<>();
        destRegs.add(destReg);
        this.srcMem = srcMem;
    }

    public ArmLdr(List<ArmReg> destRegs, ArmMem srcMem) {
        this.destRegs = destRegs;
        this.srcMem = srcMem;
    }

    public static ArmLdr pop(ArmReg destReg) {
        return new ArmLdr(destReg, null);
    }

    public static ArmLdr pop(List<ArmReg> destRegs) {
        return new ArmLdr(destRegs, null);
    }
}