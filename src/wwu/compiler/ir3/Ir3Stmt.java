package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.cfg.*;
import wwu.compiler.exception.CodeGenerationException;

public abstract class Ir3Stmt extends Ir3Base implements BasicBlockStmt.Context {

    Ir3Stmt prev;
    Ir3Stmt next;
    BasicBlockStmt basicBlockStmt;

    @Override
    // Return a collection of symbols that are used by this stmt
    public Set<String> getUse() {
        return Collections.emptySet();
    }

    @Override
    // Return a symbol that is defined by this stmt, if any
    public String getDef() {
        return null;
    }

    @Override
    public void setBasicBlockStmt(BasicBlockStmt basicBlockStmt) {
        this.basicBlockStmt = basicBlockStmt;
    }

    @Override
    public boolean hasAdditionalSideEffects() {
        return false;
    }

    void setPrev(Ir3Stmt prev) {
        this.prev = prev;
    }

    void setNext(Ir3Stmt next) {
        this.next = next;
    }

    boolean hasPrev() {
        return prev != null;
    }

    boolean hasNext() {
        return next != null;
    }
    
    abstract void buildArm(ArmMdBuilder mdBuilder, ClassTypeProvider classTypeProvider) 
            throws CodeGenerationException;
}
