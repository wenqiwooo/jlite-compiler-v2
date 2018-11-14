package wwu.compiler.arm;


public class ArmReg extends ArmOperand {

    ArmRegisterType regType;

    public ArmReg(ArmRegisterType regType) {
        this.regType = regType;
    }

    public ArmRegisterType getType() {
        return regType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ArmReg) {
            return regType == ((ArmReg)obj).regType;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return regType.toString().hashCode();
    }

    @Override
    public String toString() {
        return regType.toString();
    }
}