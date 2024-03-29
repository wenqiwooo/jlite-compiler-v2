package wwu.compiler.arm;

public enum ArmRegisterType {
    REG_0("a1", 0),
    REG_1("a2", 1),
    REG_2("a3", 2),
    REG_3("a4", 3),
    REG_4("v1", 4),
    REG_5("v2", 5),
    REG_6("v3", 6),
    REG_7("v4", 7),
    REG_8("v5", 8),
    REG_SB("sb", 9), // r9
    REG_SL("sl", 10), // r10
    REG_FP("fp", 11), // r11
    REG_IP("ip", 12), // r12
    REG_SP("sp", 13), // r13
    REG_LR("lr", 14), // r14
    REG_PC("pc", 15); // r15

    // Max number of param registers
    public static final int MAX_PARAM_REGS = 4;
    // Max number of assignable registers
    public static final int MAX_ASSIGNABLE = 7;

    private String value;
    private int idx;

    private ArmRegisterType(String value, int idx) {
        this.value = value;
        this.idx = idx;
    }

    @Override
    public String toString() {
        return value;
    }

    public int getIdx() {
        return idx;
    }

    public static ArmRegisterType getByIdx(int idx) {
        switch (idx) {
            case 0:
                return REG_0;
            case 1:
                return REG_1;
            case 2:
                return REG_2;
            case 3:
                return REG_3;
            case 4:
                return REG_4;
            case 5:
                return REG_5;
            case 6:
                return REG_6;
            case 7:
                return REG_7;
            case 8:
                return REG_8;
            case 9:
                return REG_SB;
            case 10:
                return REG_SL;
            case 11:
                return REG_FP;
            case 12:
                return REG_IP;
            case 13:
                return REG_SP;
            case 14:
                return REG_LR;
            case 15:
                return REG_PC;
            default:
                return null;
        }
    }
}