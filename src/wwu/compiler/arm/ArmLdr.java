package wwu.compiler.arm;

import java.util.*;

public class ArmLdr extends ArmInsn {
    ArmReg destReg;
    // If srcMem is null, this is a pop operation.
    ArmLoadable srcMem;

    public ArmLdr(ArmReg destReg, ArmLoadable srcMem) {
        this.destReg = destReg;
        this.srcMem = srcMem;
    }

    public static ArmLdr pop(ArmReg destReg) {
        return new ArmLdr(destReg, null);
    }

    private boolean isPop() {
        return srcMem == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (isPop()) {
            sb.append("ldr ")
                .append(destReg.toString())
                .append(",[sp],#4\n");
        } else {
            sb.append("ldr ")
                .append(destReg.toString())
                .append(",")
                .append(srcMem.toString())
                .append("\n");
        }
        
        return sb.toString();
    }
}