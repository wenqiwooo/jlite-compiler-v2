package wwu.compiler.arm;

public class ArmBranch extends ArmInsn {
    public enum Mode {
        B,
        BL,
        BX;
    }

    Mode mode;
    ArmCondition cond;
    String target;

    public ArmBranch(Mode mode, String target) {
        this(mode, null, target);
    }

    public ArmBranch(Mode mode, ArmCondition cond, String target) {
        this.mode = mode;
        this.cond = cond;
        this.target = target;
    }
}