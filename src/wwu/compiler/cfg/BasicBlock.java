package wwu.compiler.cfg;

import java.util.*;

public class BasicBlock extends Node {
    String key;

    // Number of statements
    int size = 0;
    BasicBlockStmt firstStmt;
    BasicBlockStmt lastStmt;
    
    Map<String, BasicBlock> preds;
    Map<String, BasicBlock> succs;

    NodeState inState;
    NodeState outState;

    public BasicBlock(String key) {
        this.key = key;
        preds = new HashMap<>();
        succs = new HashMap<>();
    }

    public void addPred(BasicBlock bb) {
        preds.put(bb.key, bb);
    }

    public void addSucc(BasicBlock bb) {
        succs.put(bb.key, bb);
    }
}