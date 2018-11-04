package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.cfg.*;

public abstract class Ir3Stmt extends Ir3Base 
        implements BasicBlockStmt.Context {
    
    @Override
    // Return a collection of symbols that are used by this stmt
    public Collection<String> getUse() {
        return Collections.emptyList();
    }

    @Override
    // Return a symbol that is defined by this stmt, if any
    public String getDef() {
        return null;
    }
}
