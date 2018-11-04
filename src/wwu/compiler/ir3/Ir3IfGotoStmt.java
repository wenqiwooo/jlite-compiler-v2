package wwu.compiler.ir3;

import java.util.*;

public class Ir3IfGotoStmt extends Ir3Stmt {
    Ir3Expr pred;
    String label;
    
    Set<String> useSymbols;

    public Ir3IfGotoStmt(Ir3Expr pred, String label) {
        this.pred = pred;
        this.label = label;

        useSymbols = new HashSet<>();
        this.pred.addUseSymbols(useSymbols);
    }

    @Override
    public String toString() {
        return String.format("if (%s) goto %s;", pred, label);
    }

    @Override
    public Collection<String> getUse() {
        return useSymbols;
    }
}
