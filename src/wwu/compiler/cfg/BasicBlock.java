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

    CFGraph graph;

    public BasicBlock(String key) {
        this.key = key;
        preds = new HashMap<>();
        succs = new HashMap<>();
    }

    public void addBasicBlockStmt(BasicBlockStmt bbStmt) {
        if (firstStmt == null) {
            firstStmt = bbStmt;
        } else {
            lastStmt.setSucc(bbStmt);
            bbStmt.setPred(lastStmt);
        }
        lastStmt = bbStmt;
        size++;
    }

    public void initState(CFGraph graph) {
        this.graph = graph;
        Set<String> symbols = this.graph.getSymbols();

        inState = new NodeState(symbols);
        
        NodeState state = inState;
        NodeState nextState = new NodeState(symbols);
        BasicBlockStmt stmt = firstStmt;

        while (stmt != null) {
            stmt.setBasicBlock(this);
            stmt.initState(state, nextState);
            if (stmt.succ != null) {
                state = nextState;
                nextState = new NodeState(symbols);
            }
            stmt = stmt.succ;
        }

        outState = nextState;
    }

    public void addPred(BasicBlock bb) {
        preds.put(bb.key, bb);
    }

    public void addSucc(BasicBlock bb) {
        succs.put(bb.key, bb);
    }

    public String getKey() {
        return key;
    }

    // Returns true if basic block exits the function
    boolean exits() {
        return succs.containsKey(CFGraph.EXIT_KEY);
    }

    // Returns all live-on-in symbols
    Set<String> getLiveIn() {
        return inState.getAllLive();
    }

    // Returns all live-on-out symbols
    Set<String> getLiveOut() {
        return outState.getAllLive();
    }
}