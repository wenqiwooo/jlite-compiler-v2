package wwu.compiler.arm;

public enum ArmCondition {
    EQ("eq"),
    NE("ne"),
    GT("gt"),
    LT("lt"),
    GE("ge"),
    LE("le");

    private String value;

    private ArmCondition(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public ArmCondition getInverse() {
        switch (this) {
            case EQ:
                return NE;
            case NE:
                return EQ;
            case GT:
                return LE;
            case LT:
                return GE;
            case GE: 
                return LT;
            case LE:
                return GT;
            default:
                return null;
        }
    }
}