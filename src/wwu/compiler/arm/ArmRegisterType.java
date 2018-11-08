package wwu.compiler.arm;

public enum ArmRegisterType {
    REG_0("r0"),
    REG_1("r1"),
    REG_2("r2"),
    REG_3("r3"),
    REG_4("r4"),
    REG_5("r5"),
    REG_6("r6"),
    REG_7("r7"),
    REG_8("r8"),
    REG_SB("sb"), // r9
    REG_SL("sl"), // r10
    REG_FP("fp"), // r11
    REG_IP("ip"), // r12
    REG_SP("sp"), // r13
    REG_LR("lr"), // r14
    REG_PC("pc"); // r15

    // Max number of param registers
    public static final int MAX_PARAM_REGS = 4;
    // Max number of assignable registers
    public static final int MAX_ASSIGNABLE = 9;

    private String value;

    private ArmRegisterType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
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