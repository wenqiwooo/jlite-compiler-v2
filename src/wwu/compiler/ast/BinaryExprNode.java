package wwu.compiler.ast;

import wwu.compiler.common.*;
import wwu.compiler.exception.*;
import wwu.compiler.ir3.*;

public class BinaryExprNode extends ExprNode {
    Op op;
    Node x, y;

    public BinaryExprNode(Op op, Node x, Node y) {
        this.op = op;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("[%s,%s](%s)", x, y, op);
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        TypeCheckResult resX = TypeCheckHelper.checkType(env, x, true);
        TypeCheckResult resY = TypeCheckHelper.checkType(env, y, true);

        String type = null;
        if (op.isArithmetic() && 
                resX.type.equals(Type.INT) &&
                resY.type.equals(Type.INT)) {
            type = Type.INT;
        } 
        else if (op.isRelational() &&
                resX.type.equals(Type.INT) &&
                resY.type.equals(Type.INT)) {
            type = Type.BOOL;
        } 
        else if (op.isBoolean() &&
                resX.type.equals(Type.BOOL) &&
                resY.type.equals(Type.BOOL)) {
            type = Type.BOOL;
        }

        if (type == null) {
            String errMsg = String.format(
                    "Class %s method %s: operator %s cannot be applied on operands %s and %s",
                    env.getCurrentClassName(), 
                    env.getCurrentMethodName(), 
                    resX.ir3Obj, 
                    resY.ir3Obj);
            throw new TypeCheckException(errMsg);
        }

        Ir3BasicId resXExpr = (Ir3BasicId)resX.ir3Obj;
        Ir3BasicId resYExpr = (Ir3BasicId)resY.ir3Obj;
        Ir3BasicExpr expr = new Ir3BinaryExpr(op, resXExpr, resYExpr);

        if (shouldReduce) {
            String tmpVarName = env.cgNewTemporaryName();
            env.cgLocalDecl(tmpVarName, type);

            Ir3Id tmpVar = new Ir3Identifier(tmpVarName, type);
            env.cgCode(new Ir3AssignStmt(tmpVar, expr));

            return new TypeCheckResult(type, tmpVar, true);
        } else {
            return new TypeCheckResult(type, expr, false);
        }
    }
}
