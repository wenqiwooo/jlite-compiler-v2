package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.arm.*;
import wwu.compiler.exception.*;

public class Ir3IfGotoStmt extends Ir3Stmt {
    Ir3Expr pred;
    String label;
    
    Set<String> useSymbols;

    public Ir3IfGotoStmt(Ir3Expr pred, String label) {
        this.pred = pred;
        this.label = label;

        useSymbols = new HashSet<>();
        this.pred.addUseSymbols(useSymbols);
    }

    @Override
    public String toString() {
        return String.format("if (%s) goto %s;", pred, label);
    }

    @Override
    public Set<String> getUse() {
        return useSymbols;
    }

    @Override
    void buildArm(Ir3MdBuilder.ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) throws CodeGenerationException {
        if (pred instanceof Ir3BasicId) {
            ArmReg armReg = ((Ir3BasicId)pred).getArmReg(mdBuilder.getTempReg1(), 
                    mdBuilder, classTypeProvider);

            mdBuilder.addInsn(new ArmMov(mdBuilder.getTempReg2(), 
                            new ArmImmediate(0)))
                    .addInsn(new ArmCompare(armReg, mdBuilder.getTempReg2()))
                    .addInsn(new ArmBranch(ArmBranch.Mode.B, ArmCondition.NE, label));
        }  
        else if (pred instanceof Ir3BinaryExpr) {
            ((Ir3BinaryExpr)pred).buildArmForIfGotoStmt(label, 
                    mdBuilder, classTypeProvider);
        }
    }
}
