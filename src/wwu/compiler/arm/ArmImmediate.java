package wwu.compiler.arm;

public class ArmImmediate extends ArmOperand {
    int value;

    public ArmImmediate(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "#" + value;
    }
}