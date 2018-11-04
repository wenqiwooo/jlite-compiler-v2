package wwu.compiler.ir3;

import java.util.*;

public class Ir3ReturnStmt extends Ir3Stmt {
    Ir3Id arg;

    Set<String> useSymbols;

    public Ir3ReturnStmt(Ir3Id arg) {
        this.arg = arg;

        useSymbols = new HashSet<>();
        this.arg.addUseSymbols(useSymbols);
    }

    @Override
    public String toString() {
        if (arg == null) {
            return "return;";
        }
        return "return " + arg.toString() + ";"; 
    }

    @Override
    public Collection<String> getUse() {
        return useSymbols;
    }
}