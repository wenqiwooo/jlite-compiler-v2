package wwu.compiler.ast;

import java.util.*;

import wwu.compiler.common.*;
import wwu.compiler.ir3.*;
import wwu.compiler.exception.*;

public class PrintStmtNode extends StmtNode {
    ExprNode expr;

    public PrintStmtNode(ExprNode expr) {
        this.expr = expr;
    }

    @Override
    public List<String> getLines() {
        List<String> res = new ArrayList<>();
        res.add(String.format("println(%s);", expr));
        return res;
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        TypeCheckResult resExpr = TypeCheckHelper.checkType(env, expr, true);

        if (!TypeHelper.isNonVoidBuiltInType(resExpr.type)) {
            String errMsg = String.format(
                    "Class %s method %s: invalid parameter %s passed to println()",
                    env.getCurrentClassName(), 
                    env.getCurrentMethodName(), 
                    resExpr.ir3Obj);
            throw new TypeCheckException(errMsg);
        }

        env.cgCode(new Ir3PrintlnStmt((Ir3BasicId)resExpr.ir3Obj));

        return new TypeCheckResult(Type.VOID);
    }
}