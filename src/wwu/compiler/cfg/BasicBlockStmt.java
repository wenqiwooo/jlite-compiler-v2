package wwu.compiler.cfg;

import java.util.*;

public class BasicBlockStmt extends Node {
    BasicBlock basicBlock;

    NodeState inState;
    NodeState outState;

    BasicBlockStmt pred;
    BasicBlockStmt succ;
    Context context;

    public BasicBlockStmt(Context context) {
        this.context = context;
    }

    void setPred(BasicBlockStmt pred) {
        this.pred = pred;
    }

    void setSucc(BasicBlockStmt succ) {
        this.succ = succ;
    }

    public interface Context {
        // Return a collection of symbols that are used by this stmt
        Collection<String> getUse();

        // Return a symbol that is defined by this stmt, if any
        String getDef();
    }
}