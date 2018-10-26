package wwu.compiler.ast;

import java.util.List;

import wwu.compiler.common.*;
import wwu.compiler.ir3.*;
import wwu.compiler.exception.*;

/**
 * Helper class for type checking.
 */
public class TypeCheckHelper {

    public static TypeCheckResult checkType(Ir3Builder env, TypeCheckable tc) {
        return tc.checkType(env, true, null);
    }

    static TypeCheckResult checkType(Ir3Builder env, TypeCheckable tc, boolean shouldReduce) {
        return tc.checkType(env, shouldReduce, null);
    }

    static TypeCheckResult checkType(Ir3Builder env, TypeCheckable tc, boolean shouldReduce, 
            String nextLabel) {
        return tc.checkType(env, shouldReduce, nextLabel);
    }

    static TypeCheckResult checkType(Ir3Builder env, List<StmtNode> stmts) 
            throws TypeCheckException {
        return checkType(env, stmts, null);
    }

    /**
     * Type checks a list of Stmts (usually a block).
     */
    static TypeCheckResult checkType(Ir3Builder env, List<StmtNode> stmts, String nextLabel) 
            throws TypeCheckException {
        TypeCheckResult resStmt = null;
        
        for (int i = 0; i < stmts.size(); i++) {
            StmtNode stmtNode = stmts.get(i);
            String label = null;

            if (stmtNode instanceof IfStmtNode || stmtNode instanceof WhileStmtNode) {
                label = i == stmts.size() - 1 && nextLabel != null 
                    ? nextLabel 
                    : env.cgNewLabelName();
            }

            TypeCheckResult res = checkType(env, stmtNode, false, label);

            if (label != null && label != nextLabel) {
                env.cgCode(new Ir3Label(label));
            }

            resStmt = res;
            if (stmtNode instanceof ReturnStmtNode) {
                if (!TypeHelper.isASuperclassOfB(env.getCurrentMethodReturnType(), resStmt.type)) {
                    String errMsg = String.format(
                            "Class %s: method %s requires return type %s but %s is of type %s.",
                            env.getCurrentClassName(), 
                            env.getCurrentMethodName(), 
                            env.getCurrentMethodReturnType(),
                            resStmt.ir3Obj, 
                            resStmt.type);
                    throw new TypeCheckException(errMsg);
                }
            }
        }

        if (resStmt != null) {
            return resStmt;
        }
        return new TypeCheckResult(Type.VOID);
    }
}