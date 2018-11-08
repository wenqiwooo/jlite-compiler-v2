package wwu.compiler.arm;

public class ArmMov extends ArmInsn {
    ArmReg destReg;
    ArmOperand src;

    public ArmMov(ArmReg destReg, ArmOperand src) {
        this.destReg = destReg;
        this.src = src;
    }
}