package wwu.compiler.arm;

public class ArmLabel extends ArmInsn {
    String label;

    public ArmLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label + ":\n";
    }
}