package wwu.compiler.ast;

import wwu.compiler.common.*;
import wwu.compiler.ir3.*;
import wwu.compiler.exception.*;

public class NullNode extends AtomNode {
    static final String NULL_NAME = "null";

    public NullNode() {

    }

    @Override
    public String toString() {
        return NULL_NAME;
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        Ir3Id nullExpr = new Ir3Literal(NULL_NAME, NULL_NAME);
        
        return new TypeCheckResult(Type.NULL, nullExpr, true);
    }
}
