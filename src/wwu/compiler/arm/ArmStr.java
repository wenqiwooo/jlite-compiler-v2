package wwu.compiler.arm;

import java.util.*;

public class ArmStr extends ArmInsn {
    // If destMem is null, this is a push operation
    ArmMem destMem;
    ArmReg srcReg;

    public ArmStr(ArmMem destMem, ArmReg srcReg) {
        this.destMem = destMem;
        this.srcReg = srcReg;
    }

    public static ArmStr push(ArmReg srcReg) {
        return new ArmStr(null, srcReg);
    }

    private boolean isPush() {
        return destMem == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        if (isPush()) {
            sb.append("push ")
                .append(srcReg)
                .append("\n");
        } else {
            sb.append("str ")
                .append(srcReg.toString())
                .append(",")
                .append(destMem.toString())
                .append("\n");
        }

        return sb.toString();
    }
}