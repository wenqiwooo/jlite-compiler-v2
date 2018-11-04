package wwu.compiler.cfg;

import java.util.*;

public abstract class BasicBlockStmt extends Node {
    BasicBlock basicBlock;

    NodeState inState;
    NodeState outState;

    BasicBlock pred;
    BasicBlock succ;
    Context context;

    public BasicBlockStmt(Context context) {
        this.context = context;
    }

    public interface Context {
        // Return a collection of symbols that are used by this stmt
        Collection<String> getUse();

        // Return a symbol that is defined by this stmt, if any
        String getDef();
    }
}