package wwu.compiler.ir3;

import wwu.compiler.arm.*;

public class Ir3ReturnStmt extends Ir3Stmt {
    Ir3Id arg;

    public Ir3ReturnStmt(Ir3Id arg) {
        this.arg = arg;
    }

    @Override
    public String toString() {
        if (arg == null) {
            return "return;";
        }
        return "return " + arg.toString() + ";"; 
    }
}