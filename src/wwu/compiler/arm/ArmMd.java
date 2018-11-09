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

        ArmInsn insn = firstInsn;
        while (insn != null) {
            sb.append(insn.toString());
            insn = insn.next;
        }

        return sb.toString();
    }
}