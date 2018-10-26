package wwu.compiler.ast;

import wwu.compiler.ir3.*;
import wwu.compiler.exception.*;

public class AtomExprNode extends AtomNode {
    public ExprNode expr;

    public AtomExprNode(ExprNode expr) {
        this.expr = expr;
    } 

    @Override
    public String toString() {
        return expr.toString();
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        return TypeCheckHelper.checkType(env, expr, shouldReduce);
    }
}
