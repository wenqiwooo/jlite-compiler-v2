package wwu.compiler.ast;

import java.util.ArrayList;
import java.util.List;

import wwu.compiler.ir3.*;
import wwu.compiler.exception.*;

public class CallNode extends AtomNode {
    /**
     * Parent can be null here.
     */
    public AtomNode parent;
    public String name;
    public List<ExprNode> params;

    public CallNode(AtomNode parent, String name, List<ExprNode> params) {
        this.parent = parent;
        this.name = name;
        this.params = params;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[")
            .append(parent.toString())
            .append(".")
            .append(name)
            .append("(");
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).toString());
            if (i < params.size() - 1)
                sb.append(",");
        }
        sb.append(")]");
        return sb.toString();
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        List<String> argTypes = new ArrayList<>();
        List<Ir3Id> argExprs = new ArrayList<>();

        for (ExprNode exprNode : params) {
            TypeCheckResult resArg = TypeCheckHelper.checkType(env, exprNode, true);
            argTypes.add(resArg.type);
            argExprs.add((Ir3Id)resArg.ir3Obj);
        }

        String parentType = null;
        Ir3Id parentIr3 = null;
        
        if (parent != null) {
            TypeCheckResult resPar = TypeCheckHelper.checkType(env, parent, true);
            parentType = resPar.type;
            parentIr3 = (Ir3Id)resPar.ir3Obj;
        } else {
            parentType = env.getCurrentClassType();
            parentIr3 = new Ir3Identifier("this");
        }

        String type = env.getReturnTypeForMethod(parentType, name);
        String methodId = env.getEncodedNameForMethod(parentType, name, argTypes);
        
        argExprs.add(0, parentIr3);

        Ir3Expr expr = new Ir3Call(methodId, argExprs);
        if (shouldReduce) {
            String tmpVarName = env.cgNewTemporaryName();
            env.cgLocalDecl(tmpVarName, type);

            Ir3Id tmpVar = new Ir3Identifier(tmpVarName);
            env.cgCode(new Ir3AssignStmt(tmpVar, expr));

            return new TypeCheckResult(type, tmpVar, true);
        }
        return new TypeCheckResult(type, expr, false);
    }
}