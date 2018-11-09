package wwu.compiler.ast;

import java.util.*;

import wwu.compiler.common.*;
import wwu.compiler.ir3.*;
import wwu.compiler.exception.*;

public class ReadStmtNode extends StmtNode {
    String name;

    public ReadStmtNode(String name) {
        this.name = name;
    }

    @Override
    public List<String> getLines() {
        List<String> res = new ArrayList<>();
        res.add(String.format("readln(%s);", name));
        return res;
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        String type = env.getTypeForVar(name);
        
        if (!TypeHelper.isNonVoidBuiltInType(type)) {
            String errMsg = String.format(
                    "Class %s method %s: invalid parameter %s passed to readln()",
                    env.getCurrentClassName(), 
                    env.getCurrentMethodName(), 
                    name);
            throw new TypeCheckException(errMsg);
        }

        Ir3Id arg = null;

        if (env.isVarClassField(name)) {
            Ir3Identifier cls = new Ir3Identifier("this", type);
            Ir3Id field = new Ir3Field(cls, name);

            String tmpVarName = env.cgNewTemporaryName();
            env.cgLocalDecl(tmpVarName, type);

            arg = new Ir3Identifier(tmpVarName, type);
            env.cgCode(new Ir3AssignStmt(arg, field));
        } else {
            arg = new Ir3Identifier(name, type);
        }
        
        env.cgCode(new Ir3ReadlnStmt(arg));

        return new TypeCheckResult(Type.VOID);
    }
}