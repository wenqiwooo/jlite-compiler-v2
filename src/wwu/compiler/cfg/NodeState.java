package wwu.compiler.cfg;

import java.util.*;

/**
 * This class is used to contain all the in/out states of basic blocks
 * and basic block stmts
 */
public class NodeState {
    
    Map<String, SymbolState> symbolStates;
    
    public NodeState(Set<String> symbols) {
        symbolStates = new HashMap<>();
        
        symbols.forEach(sym -> {
            symbolStates.put(sym, new SymbolState(sym));
        });
    }

    void setAllLive(boolean isLive) {
        symbolStates.forEach((name, state) -> {
            state.setLive(isLive);
        });
    }

    boolean setOnlyLive(Set<String> liveSymbols) {
        boolean stateChanged = false;
        
        for (SymbolState state : symbolStates.values()) {
            boolean liveness = liveSymbols.contains(state.name);
            if (state.isLive != liveness) {
                stateChanged = true;
                state.setLive(liveness);
            }
        }

        return stateChanged;
    }

    Set<String> getAllLive() {
        Set<String> res = new HashSet<>();
        symbolStates.forEach((name, state) -> {
            if (state.isLive) {
                res.add(state.name);
            }
        });
        return res;
    }
}