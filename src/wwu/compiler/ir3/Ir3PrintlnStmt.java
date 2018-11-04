package wwu.compiler.ir3;

import java.util.*;

public class Ir3PrintlnStmt extends Ir3Stmt {
    Ir3Id arg;

    Set<String> useSymbols;
    
    public Ir3PrintlnStmt(Ir3Id arg) {
        this.arg = arg;

        useSymbols = new HashSet<>();
        this.arg.addUseSymbols(useSymbols);
    }

    @Override
    public String toString() {
        return "println(" + arg.toString() + ");";
    }

    public Set<String> getUseSymbols() {
        return useSymbols;
    }
}
