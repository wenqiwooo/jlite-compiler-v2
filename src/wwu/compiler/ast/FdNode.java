package wwu.compiler.ast;

import wwu.compiler.ir3.*;
import wwu.compiler.exception.*;

public class FdNode extends AtomNode {
    public AtomNode parent;
    public String name;

    public FdNode(AtomNode parent, String name) {
        assert(parent != null);
        this.parent = parent;
        this.name = name;
    }

    @Override
    public String toString() {
        return parent.toString() + "." + name;
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        TypeCheckResult resPar =  TypeCheckHelper.checkType(env, parent, true);
        
        String type = env.getTypeForField(resPar.type, name);

        Ir3Id expr = new Ir3Field((Ir3Identifier)resPar.ir3Obj, name);

        if (shouldReduce) {
            String tmpVarName = env.cgNewTemporaryName();
            env.cgLocalDecl(tmpVarName, type);

            Ir3Id tmpVar = new Ir3Identifier(tmpVarName, type);
            env.cgCode(new Ir3AssignStmt(tmpVar, expr));

            return new TypeCheckResult(type, tmpVar, true);
        }
        return new TypeCheckResult(type, expr, false);
    }
}
