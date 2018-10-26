package wwu.compiler.ast;

import wwu.compiler.exception.*;
import wwu.compiler.ir3.*;


public interface TypeCheckable {
    /**
     * 
     * Any implementor should perform type checking and code generation when 
     * this method is called.
     * 
     * @param env: Ir3Builder instance. This is the main interface you will use to obtain 
     * information about the type environment and class descriptors, as well as to add new 
     * declarations or code statements in the form of Ir3Expr objects.
     * 
     * @param shouldReduce: if true, caller expects any code expression returned to be in 
     * a reduced form, i.e. an identifier or literal.
     * 
     * @param nextLabel: next label that points to the code immediately after the current AST node. 
     * If this is null, it means that no such label as been generated and it is up 
     * to the callee to do the generation. This is required for control flow statements.
     * 
     * @return TypeCheckResult instance. Should contain the type expression, an Ir3Expr object (optional), 
     * and a isReduced flag (set to true if the Ir3Expr object returned is of a reduced form).
     */
    TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException;
}