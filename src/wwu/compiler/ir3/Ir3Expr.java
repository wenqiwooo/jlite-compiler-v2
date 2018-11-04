package wwu.compiler.ir3;

import java.util.*;

public abstract class Ir3Expr extends Ir3Base {

    void addUseSymbols(Set<String> symbols) {
        // Add a list of the symbols that are used by the expression.
    }
}