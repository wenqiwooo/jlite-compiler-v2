package wwu.compiler.ir3;

import wwu.compiler.arm.*;

public class Ir3IfGotoStmt extends Ir3Stmt {
    Ir3Expr pred;
    String label;
    
    public Ir3IfGotoStmt(Ir3Expr pred, String label) {
        this.pred = pred;
        this.label = label;
    }

    @Override
    public String toString() {
        return String.format("if (%s) goto %s;", pred, label);
    }

    @Override
    public void addToArmMd(ArmMd armMd) {

    }
}
