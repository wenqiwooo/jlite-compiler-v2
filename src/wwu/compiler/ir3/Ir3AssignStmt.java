package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.cfg.*;
import wwu.compiler.arm.*;

public class Ir3AssignStmt extends Ir3Stmt {
    Ir3Id assignee;
    Ir3Expr expr;

    String defSymbol;
    Set<String> useSymbols;

    public Ir3AssignStmt(Ir3Id assignee, Ir3Expr expr) {
        this.assignee = assignee;
        this.expr = expr;

        useSymbols = new HashSet<>();
        this.expr.addUseSymbols(useSymbols);
        
        Set<String> defSymbols = new HashSet<>();
        this.assignee.addUseSymbols(defSymbols);
        defSymbol = !defSymbols.isEmpty() 
                ? defSymbols.iterator().next()
                : null;
    }

    public Ir3AssignStmt(String assigneeName, Ir3Expr expr) {
        this(new Ir3Identifier(assigneeName), expr);
    }

    @Override
    public String toString() {
        return assignee.toString() + " = " + expr.toString() + ";";
    }

    @Override
    public String getDef() {
        return defSymbol;
    }

    @Override
    public Set<String> getUse() {
        return useSymbols;
    }
}