package wwu.compiler.ast;

import wwu.compiler.ir3.*;
import wwu.compiler.exception.*;

public class IdNode extends AtomNode {
    public String name;
    
    public IdNode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        String type = env.getTypeForVar(name);

        if (env.isVarClassField(name)) {
            Ir3Identifier parent = new Ir3Identifier("this");
            Ir3Expr expr = new Ir3Field(parent, name);
            
            if (shouldReduce) {
                String tmpVarName = env.cgNewTemporaryName();
                env.cgLocalDecl(tmpVarName, type);

                Ir3Id tmpVar = new Ir3Identifier(tmpVarName);
                env.cgCode(new Ir3AssignStmt(tmpVar, expr));

                return new TypeCheckResult(type, tmpVar, true);
            } else {
                return new TypeCheckResult(type, expr, false);
            }
        } else {
            Ir3Id expr = new Ir3Identifier(name);
            return new TypeCheckResult(type, expr, true);
        }
    }
}
