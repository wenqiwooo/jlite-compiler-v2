package wwu.compiler.ast;

import java.util.*;

import wwu.compiler.ir3.*;
import wwu.compiler.exception.*;

public class CallStmtNode extends StmtNode {
    CallNode atom;

    public CallStmtNode(CallNode atom) {
        this.atom = atom;
    }

    @Override
    public List<String> getLines() {
        List<String> res = new ArrayList<>();
        res.add(atom + ";");
        return res;
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        TypeCheckResult res = TypeCheckHelper.checkType(env, atom, false);
        Ir3Call callExpr = (Ir3Call)res.ir3Obj;
        env.cgCode(new Ir3CallStmt(callExpr));

        return new TypeCheckResult(res.type);
    }
}