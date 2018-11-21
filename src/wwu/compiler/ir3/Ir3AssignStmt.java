package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.cfg.*;
import wwu.compiler.exception.CodeGenerationException;
import wwu.compiler.arm.*;

public class Ir3AssignStmt extends Ir3Stmt {
    Ir3Id assignee;
    Ir3Expr expr;

    String defSymbol = null;
    Set<String> useSymbols;

    public Ir3AssignStmt(Ir3Id assignee, Ir3Expr expr) {
        this.assignee = assignee;
        this.expr = expr;

        useSymbols = new HashSet<>();
        this.expr.addUseSymbols(useSymbols);
        
        Set<String> defSymbols = new HashSet<>();
        this.assignee.addUseSymbols(defSymbols);
        String assigneeSym = !defSymbols.isEmpty() 
                ? defSymbols.iterator().next()
                : null;

        // Setting the field of an object does not redefine that object
        if (this.assignee instanceof Ir3Field) {
            useSymbols.add(assigneeSym);
        } else {
            defSymbol = assigneeSym;
        }
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
            VarLocation loc = mdBuilder.getLocationForSymbol(((Ir3Identifier)assignee).varName);
            if (loc.inReg()) {
                expr.buildArmForAssignStmt(loc.getReg(), mdBuilder, classTypeProvider);
            } 
            else {
                ArmReg tempReg1 = mdBuilder.getTempReg1();
                expr.buildArmForAssignStmt(tempReg1, mdBuilder, classTypeProvider);
                
                ArmMem destMem = loc.getMem().getArmMem(mdBuilder.getTempReg2(), mdBuilder);
                mdBuilder.addInsn(new ArmStr(destMem, tempReg1));
            }
        }
        else if (assignee instanceof Ir3Field) {
            Ir3Field field = (Ir3Field)assignee;
            // Location of parent class
            VarLocation loc = mdBuilder.getLocationForSymbol(field.parent.varName);
            int offset = classTypeProvider.getClassFieldOffset(field.parent.type, field.fieldName);
            
            ArmReg tempReg1 = mdBuilder.getTempReg1();
            ArmReg tempReg2 = mdBuilder.getTempReg2();
            ArmReg tempReg3 = mdBuilder.getTempReg3();

            // Result is saved in tempReg1, don't touch it anymore!
            expr.buildArmForAssignStmt(tempReg1, mdBuilder, classTypeProvider);
            
            ArmMem destMem;
            if (loc.inReg()) {
                destMem = new Ir3Mem(loc.getReg(), offset).getArmMem(tempReg2, mdBuilder);
            }
            else {
                ArmMem mem = loc.getMem().getArmMem(tempReg2, mdBuilder);
                mdBuilder.addInsn(new ArmLdr(tempReg2, mem));
                destMem = new Ir3Mem(tempReg2, offset).getArmMem(tempReg3, mdBuilder);
            }

            mdBuilder.addInsn(new ArmStr(destMem, tempReg1));
        }
        else {
            throw new CodeGenerationException("Assignee is invalid");
        }
    }
}