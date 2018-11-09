package wwu.compiler.arm;

public enum ArmCondition {
    EQ,
    NE,
    GT,
    LT,
    GE,
    LE;

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