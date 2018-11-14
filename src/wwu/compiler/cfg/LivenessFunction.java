package wwu.compiler.cfg;

import java.util.*;

public class LivenessFunction extends TransferFunction {
    
    public LivenessFunction() {

    }

    @Override
    void initEntryState(BasicBlock entry) {
        
    }

    @Override
    void initExitState(BasicBlock exit) {
        
    }

    @Override
    void initBasicBlockState(BasicBlock basicBlock) {
        // Basic block must have at least one stmt
        BasicBlockStmt stmt = basicBlock.lastStmt;
        if (basicBlock.exits()) {
            Set<String> liveSymbols = stmt.context.getUse();
            stmt.outState.setOnlyLive(liveSymbols);
        } else {
            stmt.outState.setAllLive(false);
        }

        while (stmt != null) {
            stmt.inState.setAllLive(false);
            stmt = stmt.pred;
        }
    }

    @Override
    boolean backwardUpdate(BasicBlock basicBlock, CFGraph cfg) {
        if (!basicBlock.exits()) {
            Set<String> liveSet = new HashSet<>();
            basicBlock.succs.forEach((name, succ) -> {
                liveSet.addAll(succ.getLiveIn());
            });
            
            if (!basicBlock.outState.setOnlyLive(liveSet) && basicBlock.marked) {
                return false;
            }
        }

        BasicBlockStmt stmt = basicBlock.lastStmt;

        boolean stateChanged = false;

        while (stmt != null) {
            Set<String> liveSet = stmt.outState.getAllLive();
            liveSet.remove(stmt.context.getDef());
            if (stmt.context.hasAdditionalSideEffects()) {
                liveSet.addAll(cfg.getSymbols());
            } else {
                liveSet.addAll(stmt.context.getUse());
            }
            
            stateChanged = stmt.inState.setOnlyLive(liveSet);
            if (!stateChanged && basicBlock.marked) {
                return false;
            }
            stmt = stmt.pred;
        }

        return stateChanged;
    }
}