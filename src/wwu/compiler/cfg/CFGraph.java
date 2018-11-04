package wwu.compiler.cfg;

import java.util.*;

public class CFGraph {
    public static final String ENTRY_KEY = "entry";
    public static final String EXIT_KEY = "exit";

    BasicBlock entry;
    BasicBlock exit;

    Map<String, BasicBlock> basicBlocks;

    public CFGraph() {
        entry = new BasicBlock(ENTRY_KEY);
        exit = new BasicBlock(EXIT_KEY);
        basicBlocks = new HashMap<>();
    }

    public void addBasicBlock(BasicBlock bb) {
        basicBlocks.put(bb.key, bb);
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

    // Forward analysis


    // Backward analysis
    public void backwardAnalysis(TransferFunction f) {
        f.initEntryState(entry);
        for (BasicBlock bb : basicBlocks.values()) {
            f.initBasicBlockState(bb);
        }
        f.initExitState(exit);

        List<BasicBlock> stack = new ArrayList<>();
        for (BasicBlock bb : exit.preds.values()) {
            stack.add(bb);
        }
        
        while (!stack.isEmpty()) {
            BasicBlock bb = stack.remove(stack.size() - 1);
            boolean shouldContinue = f.backwardUpdate(bb);
            if (shouldContinue) {
                for (BasicBlock nextBb : bb.preds.values()) {
                    stack.add(nextBb);
                }
            }
        }
    }
}