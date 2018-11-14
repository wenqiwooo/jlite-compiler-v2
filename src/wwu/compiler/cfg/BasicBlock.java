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

    // We need to traverse all the nodes in the graph at least once
    boolean marked = false;

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
        outState = new NodeState(symbols);
        
        NodeState state;
        NodeState nextState = inState;
        BasicBlockStmt stmt = firstStmt;

        while (stmt != null) {
            stmt.setBasicBlock(this);
            state = nextState;
            nextState = stmt.succ == null ? outState : new NodeState(symbols);
            stmt.initState(state, nextState);
            stmt = stmt.succ;
        }
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

    void mark() {
        marked = true;
    }

    void unmark() {
        marked = false;
    }

    // Builds register interference graph
    void buildRIG(Map<String, Set<String>> rig) {
        addEdgesForRIG(inState, rig);
        BasicBlockStmt stmt = firstStmt;
        while (stmt != null) {
            addEdgesForRIG(stmt.outState, rig);
            stmt = stmt.succ;
        }
    }

    private void addEdgesForRIG(NodeState state, Map<String, Set<String>> rig) {
        Set<String> liveSet = state.getAllLive();
        for (String x : liveSet) {
            for (String y : liveSet) {
                if (!x.equals(y)) {
                    rig.get(x).add(y);
                    rig.get(y).add(x);
                }
            }
        }
    }
}