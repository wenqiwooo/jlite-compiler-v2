package wwu.compiler.ast;

import java.util.*;

public abstract class Node implements TypeCheckable {
    /**
     * For pretty printing
     */
    public List<String> getLines(){
        return new ArrayList<>();            
    };
}
