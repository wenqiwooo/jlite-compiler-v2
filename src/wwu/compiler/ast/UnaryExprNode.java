package wwu.compiler.ast;

import wwu.compiler.common.*;
import wwu.compiler.ir3.*;
import wwu.compiler.exception.*;

public class UnaryExprNode extends ExprNode {
    Op op;
    Node x;

    public UnaryExprNode(Op op, Node x) {
        this.op = op;
        this.x = x;
    }

    @Override
    public String toString() {
        return String.format("(%s)[%s]", op, x);
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        TypeCheckResult resX = TypeCheckHelper.checkType(env, x, true);

        String type = null;
        if (op == Op.SUB && resX.type.equals(Type.INT)) {
            type = Type.INT;
        } else if (op == Op.NOT && resX.type.equals(Type.BOOL)) {
            type = Type.BOOL;
        }

        if (type == null) {
            String errMsg = String.format("Class %s method %s: operator %s cannot be applied to expression %s",
                    env.getCurrentClassName(), 
                    env.getCurrentMethodName(), 
                    op, 
                    resX.ir3Obj);
            throw new TypeCheckException(errMsg);
        }

        Ir3Expr expr = new Ir3UnaryExpr(op, (Ir3Id)resX.ir3Obj);

        if (shouldReduce) {
            String tmpVarName = env.cgNewTemporaryName();
            env.cgLocalDecl(tmpVarName, type);

            Ir3Id tmpVar = new Ir3Identifier(tmpVarName);
            env.cgCode(new Ir3AssignStmt(tmpVar, expr));

            return new TypeCheckResult(type, tmpVar, true);
        } else {
            return new TypeCheckResult(type, expr, false);
        }
    }
}