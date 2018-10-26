package wwu.compiler.ast;

import wwu.compiler.common.*;
import wwu.compiler.ir3.*;
import wwu.compiler.exception.*;

public class LiteralNode extends ExprNode {
    String type;
    Object val;

    public LiteralNode(String type, Object val) {
        this.type = type;
        this.val = val;
    }

    @Override
    public String toString() {
        if (type.equals(Type.STRING)) {
            return "\"" + val + "\"";
        } else {
            return String.valueOf(val);
        }
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        Ir3Id expr = new Ir3Literal(val, type);
        return new TypeCheckResult(type, expr, true);
    }
}