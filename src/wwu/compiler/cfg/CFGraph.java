package wwu.compiler.cfg;

import java.util.*;

import wwu.compiler.util.Pair;

public class CFGraph {
    public static final String ENTRY_KEY = "entry";
    public static final String EXIT_KEY = "exit";

    BasicBlock entry;
    BasicBlock exit;

    Map<String, BasicBlock> basicBlocks;
    Set<String> symbols;

    public CFGraph(Set<String> symbols) {
        this.symbols = new HashSet<>(symbols);

        entry = new BasicBlock(ENTRY_KEY);
        exit = new BasicBlock(EXIT_KEY);
        basicBlocks = new HashMap<>();

        entry.initState(this);
        exit.initState(this);
    }

    public void addBasicBlock(BasicBlock bb) {
        basicBlocks.put(bb.key, bb);
        bb.initState(this);
    }

    public void addEdge(String predKey, String succKey) {
        BasicBlock pred = predKey.equals(ENTRY_KEY)
            ? entry
            : basicBlocks.get(predKey);
        BasicBlock succ = succKey.equals(EXIT_KEY) 
            ? exit 
            : basicBlocks.get(succKey);

        pred.addSucc(succ);
        succ.addPred(pred);
    }

    public Set<String> getLiveIn() {
        return entry.getLiveOut();
    }

    Set<String> getSymbols() {
        return symbols;
    }

    // TODO: Forward analysis

    // Backward analysis
    public void backwardAnalysis(TransferFunction f) {
        f.initEntryState(entry);
        f.initExitState(exit);

        for (BasicBlock bb : basicBlocks.values()) {
            f.initBasicBlockState(bb);
        }

        List<BasicBlock> stack = new ArrayList<>();
        for (BasicBlock bb : exit.preds.values()) {
            stack.add(bb);
        }
        
        while (!stack.isEmpty()) {
            BasicBlock bb = stack.remove(stack.size() - 1);
            boolean shouldContinue = f.backwardUpdate(bb, this);
            // Mark only after update is done
            bb.mark();
            for (BasicBlock nextBb : bb.preds.values()) {
                if (shouldContinue || !nextBb.marked) {
                    stack.add(nextBb);
                }
            }
        }

        // After analysis, unmark all basic blocks
        for (BasicBlock bb : basicBlocks.values()) {
            bb.unmark();
        }

        // TODO: After analysis, check live state of entry block. 
        // Mark those parameters that are not live
        
    }

    /**
     * Ensure CFGraph has up-to-date liveness info before calling this.
     */
    public Pair<Map<String, Integer>, Set<String>> allocRegisters(int regCount) {
        Map<String, Set<String>> rig = getRIG();
        Set<String> spills = new HashSet<>();
        Map<String, Integer> assignment;

        do {
            assignment = GraphOps.colorGraph(rig, regCount);
            
            if (assignment == null) {
                String spillVar = null;
                int nSpillNbrs = 0;

                for (Map.Entry<String, Set<String>> entry : rig.entrySet()) {
                    String v = entry.getKey();
                    int nbrCnt = entry.getValue().size();
                    if (spillVar == null || nbrCnt > nSpillNbrs) {
                        spillVar = v;
                        nSpillNbrs = nbrCnt;
                    }
                }

                spills.add(spillVar);
                rig.remove(spillVar);
                for (Map.Entry<String, Set<String>> entry : rig.entrySet()) {
                    entry.getValue().remove(spillVar);
                }
            }
        } 
        while (assignment == null);

        return new Pair<>(assignment, spills);
    }

    private Map<String, Set<String>> getRIG() {    
        Map<String, Set<String>> rig = new HashMap<>();
        for (String sym : symbols) {
            rig.put(sym, new HashSet<>());
        }
        basicBlocks.forEach((key, basicBlock) -> {
            basicBlock.buildRIG(rig);
        });

        Set<String> liveParams = entry.getLiveOut();
        for (String x : liveParams) {
            for (String y : liveParams) {
                if (!x.equals(y)) {
                    rig.get(x).add(y);
                    rig.get(y).add(x);
                }
            }
        }

        return rig;
    }
}