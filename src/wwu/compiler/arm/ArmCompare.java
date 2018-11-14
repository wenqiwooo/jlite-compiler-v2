package wwu.compiler.arm;

public class ArmCompare extends ArmInsn {
    
    ArmReg reg;
    ArmOperand operand;

    public ArmCompare(ArmReg reg, ArmOperand operand) {
        this.reg = reg;
        this.operand = operand;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("cmp ")
            .append(reg.toString())
            .append(",")
            .append(operand.toString())
            .append("\n");

        return sb.toString();
    }
}