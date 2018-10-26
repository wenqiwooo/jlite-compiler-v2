package wwu.compiler.ast;

import wwu.compiler.common.*;
import wwu.compiler.util.Pair;
import wwu.compiler.exception.*;
import wwu.compiler.ir3.*;

public class VarDeclNode extends Node {
    public String name;
    public String type;

    public VarDeclNode(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s %s", type, name);
    }

    public Pair<String, String> toPair() {
        return new Pair<>(name, type);
    }

    // This is not required for declarations
    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        return new TypeCheckResult(Type.VOID);
    }
}