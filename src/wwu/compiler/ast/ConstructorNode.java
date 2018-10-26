package wwu.compiler.ast;

import wwu.compiler.exception.*;
import wwu.compiler.exception.ClassNotFoundException;
import wwu.compiler.ir3.*;

public class ConstructorNode extends AtomNode {
    public String type;

    public ConstructorNode(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("[new %s]", type);
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        if (!env.isValidDeclType(type)) {
            throw new ClassNotFoundException(type);
        }

        Ir3Expr expr = new Ir3Constructor(type);

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
