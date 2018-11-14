package wwu.compiler.arm;

public abstract class ArmInsn {
    public ArmInsn prev;
    public ArmInsn next;

    ArmInsn getPrev() {
        return prev;
    }

    ArmInsn getNext() {
        return next;
    }

    public void setPrev(ArmInsn prev) {
        this.prev = prev;
    }

    public void setNext(ArmInsn next) {
        this.next = next;
    }
}