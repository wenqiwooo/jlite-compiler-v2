package wwu.compiler.ast;

import java.util.*;

import wwu.compiler.common.*;
import wwu.compiler.ir3.*;
import wwu.compiler.exception.*;
import wwu.compiler.exception.TypeCheckException;

public class ReturnStmtNode extends StmtNode {
    ExprNode expr;

    public ReturnStmtNode(ExprNode expr) {
        this.expr = expr;
    }

    @Override
    public List<String> getLines() {
        List<String> res = new ArrayList<>();
        if (expr != null) {
            res.add("return " + expr + ";");
        } else {
            res.add("return;");
        }
        return res;
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        if (expr != null) {
            TypeCheckResult resExpr = TypeCheckHelper.checkType(env, expr, true);
            
            Ir3Id arg = null;
            if (!(resExpr.ir3Obj instanceof Ir3Identifier)) {
                String tmpVarName = env.cgNewTemporaryName();
                
                /**
                 * We cannot just take the type expression of whatever we are returning. 
                 * That can be problematic, e.g. a null literal but we cannot declare a null temporary.
                 * 
                 * Instead, we will check whether the type expression is a valid subclass of 
                 * the method's return type and declare a temporary with the method's return type.
                 */
                if (!TypeHelper.isASuperclassOfB(env.getCurrentMethodReturnType(), resExpr.type)) {
                    String errMsg = String.format(
                            "Class %s method %s: return statement of type %s conflicts with method's expected return type of %s",
                            env.getCurrentClassName(), 
                            env.getCurrentMethodName(), 
                            resExpr.type,
                            env.getCurrentMethodReturnType());
                    throw new TypeCheckException(errMsg);
                }
                env.cgLocalDecl(tmpVarName, env.getCurrentMethodReturnType());

                Ir3Identifier tmpVar = new Ir3Identifier(tmpVarName);
                env.cgCode(new Ir3AssignStmt(tmpVar, resExpr.ir3Obj));

                arg = tmpVar;
            } else {
                arg = (Ir3Id)resExpr.ir3Obj;
            }

            env.cgCode(new Ir3ReturnStmt(arg));
            return resExpr;
        } else {
            env.cgCode(new Ir3ReturnStmt(null));
            return new TypeCheckResult(Type.VOID);
        }
    }
}