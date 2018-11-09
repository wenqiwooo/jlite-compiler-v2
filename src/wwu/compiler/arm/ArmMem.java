package wwu.compiler.arm;

/**
 * There are 3 offset forms and 3 addressing modes in ARM.
 * 
 * Offset forms:
 *  - Immediate
 *  - Register
 *  - Scaled register
 * 
 * Addressing modes:
 *  - Offset
 *  - Pre-indexed
 *  - Post-indexed
 * 
 */
public class ArmMem extends ArmLoadable {
    public static final int ADDR_MODE_OFFSET = 0;
    public static final int ADDR_MODE_PRE_INDEX = 1;
    public static final int ADDR_MODE_POST_INDEX = 2;

    private static final int OFFSET_FORM_NONE = 10;
    private static final int OFFSET_FORM_IMMEDIATE = 11;
    private static final int OFFSET_FORM_REG = 12;
    private static final int OFFSET_FORM_SCALED_REG = 13;

    public ArmReg baseReg;
    public ArmOperand offset;
    public ArmImmediate lshift;

    int addrMode;
    int offsetForm;
    
    public ArmMem(ArmReg baseReg) {
        this(baseReg, null, null, ADDR_MODE_OFFSET, OFFSET_FORM_NONE);
    }

    public ArmMem(ArmReg baseReg, ArmOperand offset) {
        this(baseReg, offset, ADDR_MODE_OFFSET);
    }

    public ArmMem(ArmReg baseReg, ArmOperand offset, int addrMode) {
        this(baseReg, offset, null, addrMode, 
                offset instanceof ArmReg ? OFFSET_FORM_REG : OFFSET_FORM_IMMEDIATE);
    }

    public ArmMem(ArmReg baseReg, ArmReg offset, ArmImmediate lshift) {
        this(baseReg, offset, lshift, ADDR_MODE_OFFSET);
    }

    public ArmMem(ArmReg baseReg, ArmReg offset, ArmImmediate lshift, int addrMode) {
        this(baseReg, offset, lshift, addrMode, OFFSET_FORM_SCALED_REG);
    }

    public ArmReg getBaseReg() {
        return baseReg;
    }

    private ArmMem(ArmReg baseReg, ArmOperand offset, ArmImmediate lshift, int addrMode, int offsetForm) {
        this.baseReg = baseReg;
        this.offset = offset;
        this.lshift = lshift;
        this.addrMode = addrMode;
        this.offsetForm = offsetForm;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[")
            .append(baseReg.toString());
        if (offset != null) {
            sb.append(",")
                .append(offset.toString());
        }
        if (lshift != null) {
            sb.append(",LSL")
                .append(lshift.toString());
        }
        sb.append("]");

        return sb.toString();
    }
}