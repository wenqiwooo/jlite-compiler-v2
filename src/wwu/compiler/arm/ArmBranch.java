package wwu.compiler.arm;

public class ArmBranch extends ArmInsn {
    public enum Mode {
        B("b"),
        BL("bl"),
        BX("bx");

        private String value;

        private Mode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(mode.toString());
        if (cond != null) {
            sb.append(cond.toString());
        }
        sb.append(" ")
            .append(target);
        if (mode == Mode.BL) {
            sb.append("(PLT)");
        }
        sb.append("\n");
        return sb.toString();
    }
}