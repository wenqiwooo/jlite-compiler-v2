package wwu.compiler.arm;

public class ArmLdr extends ArmInsn {
    ArmReg destReg;
    ArmMem srcMem;

    public ArmLdr(ArmReg destReg, ArmMem srcMem) {
        this.destReg = destReg;
        this.srcMem = srcMem;
    } 
}