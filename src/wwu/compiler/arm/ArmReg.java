package wwu.compiler.arm;


public class ArmReg extends ArmOperand {

    ArmRegister reg;

    public ArmReg(ArmRegister reg) {
        this.reg = reg;
    }

    public ArmRegister getType() {
        return reg;
    }
}