package wwu.compiler.ir3;

import wwu.compiler.common.*;

public class Ir3UnaryExpr extends Ir3BasicExpr {
    Op operator;
    Ir3Id operand;

    public Ir3UnaryExpr(Op operator, Ir3Id operand) {
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public String toString() {
        return operator.toString() + operand.toString();
    }
}