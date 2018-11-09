package wwu.compiler.arm;

public class ArmCompare extends ArmInsn {
    
    ArmReg reg;
    ArmOperand operand;

    public ArmCompare(ArmReg reg, ArmOperand operand) {
        this.reg = reg;
        this.operand = operand;
    }
}