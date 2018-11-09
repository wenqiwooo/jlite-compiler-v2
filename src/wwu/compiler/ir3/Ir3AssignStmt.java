package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.cfg.*;
import wwu.compiler.exception.CodeGenerationException;
import wwu.compiler.arm.*;

public class Ir3AssignStmt extends Ir3Stmt {
    Ir3Id assignee;
    Ir3Expr expr;

    String defSymbol;
    Set<String> useSymbols;

    public Ir3AssignStmt(Ir3Id assignee, Ir3Expr expr) {
        this.assignee = assignee;
        this.expr = expr;

        useSymbols = new HashSet<>();
        this.expr.addUseSymbols(useSymbols);
        
        Set<String> defSymbols = new HashSet<>();
        this.assignee.addUseSymbols(defSymbols);
        defSymbol = !defSymbols.isEmpty() 
                ? defSymbols.iterator().next()
                : null;
    }

    @Override
    public String toString() {
        return assignee.toString() + " = " + expr.toString() + ";";
    }

    @Override
    public String getDef() {
        return defSymbol;
    }

    @Override
    public Set<String> getUse() {
        return useSymbols;
    }

    @Override
    void buildArm(ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) throws CodeGenerationException {
        if (assignee instanceof Ir3Identifier) {
            VarLocation loc = mdBuilder.getLocationForSymbol(
                    ((Ir3Identifier)assignee).varName);
            if (loc.inReg()) {
                expr.buildArmForAssignStmt(loc.getReg(), mdBuilder, classTypeProvider);
            } 
            else {
                ArmReg tempReg = mdBuilder.getTempReg1();
                expr.buildArmForAssignStmt(tempReg, mdBuilder, classTypeProvider);
                mdBuilder.addInsn(new ArmStr(loc.getMem(), tempReg));
            }
        }
        else if (assignee instanceof Ir3Field) {
            Ir3Field field = (Ir3Field)assignee;
            VarLocation loc = mdBuilder.getLocationForSymbol(field.parent.varName);
            int os = classTypeProvider.getClassFieldOffset(field.parent.type, field.fieldName);
            ArmImmediate offset = new ArmImmediate(os);
            ArmReg tempReg1 = mdBuilder.getTempReg1();
            
            expr.buildArmForAssignStmt(tempReg1, mdBuilder, classTypeProvider);

            if (loc.inReg()) {
                ArmMem destMem = new ArmMem(loc.getReg(), offset);
                mdBuilder.addInsn(new ArmStr(destMem, tempReg1));
            } 
            else {
                ArmReg tempReg2 = mdBuilder.getTempReg2();
                mdBuilder.addInsn(new ArmLdr(tempReg2, loc.getMem()));
                ArmMem destMem = new ArmMem(tempReg2, offset);
                mdBuilder.addInsn(new ArmStr(destMem, tempReg1));
            }
        }
        else {
            throw new CodeGenerationException("Assignee is invalid");
        }
    }
}