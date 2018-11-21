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

    // Simple peephole optimizations.
    void optimize() {
        ArmInsnChain chain = new ArmInsnChain();
        ArmInsn insn = firstInsn;

        while (insn != null) {
            ArmInsn nextInsn = insn.next;

            if (insn instanceof ArmMov) {
                if (((ArmMov)insn).destAndSrcSame()) {
                    // Redundant insn, do nothing
                } else {
                    insn.clearPrevNext();
                    chain.append(insn);
                }
            }
            else if (insn instanceof ArmArithOp) {
                ArmArithOp mathInsn = (ArmArithOp)insn;
                if (mathInsn.srcOperand instanceof ArmImmediate) {
                    ArmImmediate immd = (ArmImmediate)mathInsn.srcOperand;
                    ArmArithOp.Operator op = mathInsn.operator;

                    if ((op == ArmArithOp.Operator.ADD || op == ArmArithOp.Operator.SUB) && 
                            immd.value == 0) {
                        // Redundant insn, do nothing
                    }
                    else {
                        insn.clearPrevNext();
                        chain.append(insn);
                    }
                } 
                else {
                    insn.clearPrevNext();
                    chain.append(insn);
                }
            }
            else {
                insn.clearPrevNext();
                chain.append(insn);
            }

            insn = nextInsn;
        }

        firstInsn = chain.firstInsn;
        lastInsn = chain.lastInsn;
    }
}