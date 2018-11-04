package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.common.*;

public class Ir3BinaryExpr extends Ir3BasicExpr {
    Op operator;
    Ir3Id operand1;
    Ir3Id operand2;

    public Ir3BinaryExpr(Op operator, Ir3Id operand1, Ir3Id operand2) {
        this.operator = operator;
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", 
                operand1.toString(), 
                operator.toString(), 
                operand2.toString());
    }

    @Override
    void addUseSymbols(Set<String> symbols) {
        operand1.addUseSymbols(symbols);
        operand2.addUseSymbols(symbols);
    }
}