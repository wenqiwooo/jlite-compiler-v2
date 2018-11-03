package wwu.compiler.ir3;

import wwu.compiler.arm.ArmInsn;
import wwu.compiler.arm.ArmMd;

public abstract class Ir3Stmt extends Ir3Base {
    
    public abstract void addToArmMd(ArmMd armMd);
}