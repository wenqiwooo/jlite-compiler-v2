package wwu.compiler.cfg;

import java.util.*;

public abstract class BasicBlockStmt extends Node {
    BasicBlock basicBlock;

    NodeState inState;
    NodeState outState;

    BasicBlock pred;
    BasicBlock succ;

    public BasicBlockStmt() {
        
    }

    // Return list of symbols that are used by this stmt
    public abstract List<String> getUse();

    // Return a symbol that is defined by this stmt, if any
    public abstract String getDef();
}