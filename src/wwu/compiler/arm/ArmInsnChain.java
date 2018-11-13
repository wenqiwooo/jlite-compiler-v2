package wwu.compiler.arm;

/**
 * Data structure to make compositing ArmInsns easier
 */
public class ArmInsnChain {

    public ArmInsn firstInsn;
    public ArmInsn lastInsn;

    public ArmInsnChain() {
        this(null);
    }

    public ArmInsnChain(ArmInsn insn) {
        firstInsn = null;
        lastInsn = null;
        if (insn != null) {
            append(insn);
        }
    }

    public void prepend(ArmInsn insn) {
        if (firstInsn == null) {
            append(insn);
        } else {
            insn.setNext(firstInsn);
            firstInsn.setPrev(insn);
            firstInsn = insn;
        }
    }

    public void append(ArmInsn insn) {
        if (firstInsn == null) {
            firstInsn = insn;
        } else {
            lastInsn.setNext(insn);
            insn.setPrev(lastInsn);
        }
        lastInsn = insn;
        while (lastInsn.next != null) {
            lastInsn = lastInsn.next;
        }
    }

    public void concat(ArmInsnChain otherChain) {
        if (otherChain.firstInsn == null) {
            return;
        }
        if (firstInsn == null) {
            append(otherChain.firstInsn);
        } else {
            lastInsn.setNext(otherChain.firstInsn);
            lastInsn.next.setPrev(lastInsn);
            while (lastInsn.next != null) {
                lastInsn = lastInsn.next;
            }
        }
    }
}