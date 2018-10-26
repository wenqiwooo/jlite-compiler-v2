package wwu.compiler.ast;

import java.util.HashSet;
import java.util.Set;

import wwu.compiler.ir3.Ir3Expr;
import wwu.compiler.common.*;

public class TypeCheckResult {
    // Type or type expression, this should be non-null
    String type;

    Ir3Expr ir3Obj;

    // If refName has been reduced to a single variable (may be a temporary),
    // this is set to true.
    Boolean isReduced;

    public TypeCheckResult(String type) {
        this(type, null, true);
    }

    public TypeCheckResult(String type, Ir3Expr ir3Obj) {
        this(type, ir3Obj, true);
    }

    public TypeCheckResult(String type, Ir3Expr ir3Obj, Boolean isReduced) {
        this.type = type;
        this.ir3Obj = ir3Obj;
        this.isReduced = isReduced;
    }
}
