package wwu.compiler.arm;

import java.util.*;

public class ArmLdm extends ArmInsn {
    List<ArmReg> destRegs;
    // If srcReg is null, this is a pop operation.
    ArmReg srcReg;

    public ArmLdm(List<ArmReg> destRegs, ArmReg srcReg) {
        this.destRegs = destRegs;
        this.srcReg = srcReg;
    }

    public static ArmLdm pop(List<ArmReg> destRegs) {
        return new ArmLdm(destRegs, null);
    }

    private boolean isPop() {
        return srcReg == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (isPop()) {
            sb.append("pop ");
        } else {
            sb.append("ldm ")
                .append(srcReg.toString())
                .append(",");
        }
        sb.append("{");
        for (int i = 0; i < destRegs.size(); i++) {
            sb.append(destRegs.get(i).toString());
            if (i < destRegs.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("}\n");

        return sb.toString();
    }
}