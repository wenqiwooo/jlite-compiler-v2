package wwu.compiler.arm;

import wwu.compiler.common.ArmMode;

public class ArmObj extends ArmObjBase {

    String name;
    int pseudoReg;
    ArmMode mode;

    public ArmObj(String name, int pseudoReg, ArmMode mode) {
        this.name = name;
        this.pseudoReg = pseudoReg;
        this.mode = mode;
    }
}