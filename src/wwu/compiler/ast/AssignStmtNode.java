package wwu.compiler.ast;

import java.util.*;

import wwu.compiler.common.*;
import wwu.compiler.exception.*;
import wwu.compiler.ir3.*;

public class AssignStmtNode extends StmtNode {
    public AtomNode atom;
    public ExprNode expr;

    public AssignStmtNode(AtomNode atom, ExprNode expr) {
        this.atom = atom;
        this.expr = expr;
    }

    @Override
    public List<String> getLines() {
        List<String> res = new ArrayList<>();
        res.add(String.format("%s=%s;", atom, expr));
        return res;
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
        throws TypeCheckException {
        TypeCheckResult resAtom = TypeCheckHelper.checkType(env, atom, false);
        TypeCheckResult resExpr = TypeCheckHelper.checkType(env, expr, false);
        
        if (!TypeHelper.isASuperclassOfB(resAtom.type, resExpr.type)) {
            String errMsg = String.format(
                    "Class %s method %s: assigned variable %s and expression %s have conflicting types %s and %s",
                    env.getCurrentClassName(), 
                    env.getCurrentMethodName(), 
                    resAtom.ir3Obj, 
                    resExpr.ir3Obj, 
                    resAtom.type, 
                    resExpr.type);
            throw new TypeCheckException(errMsg);
        }

        Ir3Id assignee = null;
        if (!resAtom.isReduced && 
                !(atom instanceof IdNode || atom instanceof FdNode)) {
            String tmpVarName = env.cgNewTemporaryName();
            env.cgLocalDecl(tmpVarName, resAtom.type);

            Ir3Id tmpVar = new Ir3Identifier(tmpVarName);
            env.cgCode(new Ir3AssignStmt(tmpVar, resAtom.ir3Obj));

            assignee = tmpVar;
        } else {
            assignee = (Ir3Id)resAtom.ir3Obj;
        }

        env.cgCode(new Ir3AssignStmt(assignee, resExpr.ir3Obj));

        return new TypeCheckResult(Type.VOID);
    }
}
