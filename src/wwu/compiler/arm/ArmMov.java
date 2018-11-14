package wwu.compiler.arm;

public class ArmMov extends ArmInsn {
    ArmReg destReg;
    ArmOperand src;
    ArmCondition cond;

    public ArmMov(ArmReg destReg, ArmOperand src) {
        this(destReg, src, null);
    }

    public ArmMov(ArmReg destReg, ArmOperand src, ArmCondition cond) {
        this.destReg = destReg;
        this.src = src;
        this.cond = cond;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("mov");
        if (cond != null) {
            sb.append(cond.toString());
        }
        sb.append(" ")
            .append(destReg.toString())
            .append(",")
            .append(src.toString())
            .append("\n");

        return sb.toString();
    }
}