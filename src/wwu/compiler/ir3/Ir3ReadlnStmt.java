package wwu.compiler.ir3;

import wwu.compiler.arm.*;

public class Ir3ReadlnStmt extends Ir3Stmt {
    Ir3Id arg;
    
    public Ir3ReadlnStmt(Ir3Id arg) {
        this.arg = arg;
    }

    @Override
    public String toString() {
        return "readln(" + arg.toString() + ");";
    }

    @Override
    public void addToArmMd(ArmMd armMd) {

    }
}
