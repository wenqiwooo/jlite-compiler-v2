package wwu.compiler.arm;

public class ArmStr extends ArmInsn {
    ArmMem destMem;
    ArmReg srcReg;

    public ArmStr(ArmMem destMem, ArmReg srcReg) {
        this.destMem = destMem;
        this.srcReg = srcReg;
    } 
}