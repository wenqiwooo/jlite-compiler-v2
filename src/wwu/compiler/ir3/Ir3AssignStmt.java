package wwu.compiler.ir3;

import wwu.compiler.arm.*;

public class Ir3AssignStmt extends Ir3Stmt {
    Ir3Id assignee;
    Ir3Expr expr;

    public Ir3AssignStmt(Ir3Id assignee, Ir3Expr expr) {
        this.assignee = assignee;
        this.expr = expr;
    }

    public Ir3AssignStmt(String assigneeName, Ir3Expr expr) {
        this(new Ir3Identifier(assigneeName), expr);
    }

    @Override
    public String toString() {
        return assignee.toString() + " = " + expr.toString() + ";";
    }

    @Override
    public void addToArmMd(ArmMd armMd) {

    }
}