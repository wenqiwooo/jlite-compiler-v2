package wwu.compiler.arm;

import java.util.*;

public class ArmStm extends ArmInsn {
    // If destReg is null, this is a push operation
    ArmReg destReg;
    List<ArmReg> srcRegs;

    public ArmStm(ArmReg destReg, List<ArmReg> srcRegs) {
        this.destReg = destReg;
        this.srcRegs = srcRegs;
    }

    public static ArmStm push(List<ArmReg> srcRegs) {
        return new ArmStm(null, srcRegs);
    }

    private boolean isPush() {
        return destReg == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (isPush()) {
            sb.append("push ");
        } else {
            sb.append("stm ")
                .append(destReg.toString())
                .append(",");
        }
        sb.append("{");
        for (int i = 0; i < srcRegs.size(); i++) {
            sb.append(srcRegs.get(i).toString());
            if (i < srcRegs.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("}\n");

        return sb.toString();
    }
}