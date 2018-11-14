package wwu.compiler.arm;

import java.util.*;

public class ArmMd {
    String methodName;
    ArmInsn firstInsn;
    ArmInsn lastInsn;

    public ArmMd(String methodName, ArmInsn insnChain) {
        this.methodName = methodName;

        if (insnChain != null) {
            firstInsn = insnChain;
            lastInsn = insnChain;
            while (lastInsn.next != null) {
                lastInsn = lastInsn.next;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(methodName)
            .append(":\n");

        ArmInsn insn = firstInsn;
        while (insn != null) {
            if (insn instanceof ArmLabel) {
                sb.append("\n");
            }
            sb.append(insn.toString());
            insn = insn.next;
        }

        sb.append("\n");
        return sb.toString();
    }
}