package wwu.compiler.cfg;

import java.util.*;

public abstract class Node {
    NodeState inState;
    NodeState outState;

    // Returns all live-on-in symbols
    public Set<String> getLiveIn() {
        return inState.getAllLive();
    }

    // Returns all live-on-out symbols
    public Set<String> getLiveOut() {
        return outState.getAllLive();
    }
}