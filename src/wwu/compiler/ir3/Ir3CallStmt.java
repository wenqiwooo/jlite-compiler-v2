package wwu.compiler.ir3;

import java.util.List;

import wwu.compiler.arm.*;

public class Ir3CallStmt extends Ir3Stmt {
    Ir3Call call;

    public Ir3CallStmt(Ir3Call call) {
        this.call = call;
    }

    @Override
    public String toString() {
        return call.toString() + ";";
    }

    @Override
    public void addToArmMd(ArmMd armMd) {

    }
}