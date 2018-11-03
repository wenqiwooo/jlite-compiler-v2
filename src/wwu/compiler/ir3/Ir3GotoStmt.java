package wwu.compiler.ir3;

import wwu.compiler.arm.*;

public class Ir3GotoStmt extends Ir3Stmt {
    String label;

    public Ir3GotoStmt(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "goto " + label + ";";
    }

    @Override
    public void addToArmMd(ArmMd armMd) {

    }
}