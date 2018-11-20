package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.arm.*;
import wwu.compiler.common.*;
import wwu.compiler.exception.*;

public class Ir3BinaryExpr extends Ir3BasicExpr {
    Op operator;
    Ir3BasicId operand1;
    Ir3BasicId operand2;

    public Ir3BinaryExpr(Op operator, Ir3BasicId operand1, Ir3BasicId operand2) {
        this.operator = operator;
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", 
                operand1.toString(), 
                operator.toString(), 
                operand2.toString());
    }

    @Override
    void addUseSymbols(Set<String> symbols) {
        operand1.addUseSymbols(symbols);
        operand2.addUseSymbols(symbols);
    }

    void buildArmForIfGotoStmt(String label, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) throws CodeGenerationException {
        ArmCondition cond = armCondForRelOp(operator);
        
        ArmReg armReg1 = operand1.getArmReg(mdBuilder.getTempReg1(), 
                mdBuilder, classTypeProvider);
        ArmReg armReg2 = operand2.getArmReg(mdBuilder.getTempReg2(), 
                mdBuilder, classTypeProvider);

        mdBuilder.addInsn(new ArmCompare(armReg1, armReg2))
                .addInsn(new ArmBranch(ArmBranch.Mode.B, cond, label));
    }

    @Override
    void buildArmForAssignStmt(ArmReg destReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) throws CodeGenerationException {
        if (operator.isArithmetic()) {
            armForArithExprAssign(destReg, mdBuilder, classTypeProvider);
        } 
        else if (operator.isRelational()) {
            armForRelExprAssign(destReg, mdBuilder, classTypeProvider);
        }
        else if (operator.isBoolean()) {
            armForBoolExprAssign(destReg, mdBuilder, classTypeProvider);
        }
    }

    private void armForArithExprAssign(ArmReg destReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        ArmReg armReg = operand1.getArmReg(mdBuilder.getTempReg1(), 
                mdBuilder, classTypeProvider);
        ArmArithOp.Operator armOp = armOpForArithOp(operator);

        ArmOperand armOperand = armOp == ArmArithOp.Operator.MUL
                ? operand2.getArmReg(mdBuilder.getTempReg2(), 
                    mdBuilder, classTypeProvider)
                : operand2.getArmOperand(mdBuilder.getTempReg2(), 
                    mdBuilder, classTypeProvider);
        
        // Rd and Rm should be different in mul (for ARM target)
        if (armOp == ArmArithOp.Operator.MUL) {
            if (destReg == armReg) {
                ArmReg tempReg;
                if (armReg != armOperand) {
                    tempReg = (ArmReg)armOperand;
                    armOperand = armReg;
                    armReg = tempReg;
                } else {
                    if (destReg != mdBuilder.getTempReg1()) {
                        tempReg = mdBuilder.getTempReg1();
                    } else {
                        tempReg = mdBuilder.getTempReg2();
                    }
                    mdBuilder.addInsn(new ArmMov(tempReg, armReg));
                    armReg = tempReg;
                }
            }
        }

        mdBuilder.addInsn(new ArmArithOp(armOp, destReg, armReg, armOperand));
    }

    private void armForRelExprAssign(ArmReg destReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        ArmReg armReg1 = operand1.getArmReg(mdBuilder.getTempReg1(), 
                mdBuilder, classTypeProvider);
        ArmReg armReg2 = operand2.getArmReg(mdBuilder.getTempReg2(),
                mdBuilder, classTypeProvider);

        ArmCondition thenCond = armCondForRelOp(operator);
        ArmCondition elseCond = thenCond.getInverse();
        
        mdBuilder.addInsn(new ArmCompare(armReg1, armReg2))
                .addInsn(new ArmMov(destReg, new ArmImmediate(1), thenCond))
                .addInsn(new ArmMov(destReg, new ArmImmediate(0), elseCond));
    }

    private void armForBoolExprAssign(ArmReg destReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        ArmReg armReg1 = operand1.tryGetArmReg(mdBuilder, classTypeProvider);
        ArmReg armReg2 = operand2.tryGetArmReg(mdBuilder, classTypeProvider);
        ArmBoolOp.Operator armOp = armOpForBoolOp(operator);
        if (armReg1 == destReg) {
            if (armReg2 == null) {
                armReg2 = operand2.getArmReg(mdBuilder.getTempReg1(), 
                        mdBuilder, classTypeProvider);
            }
            mdBuilder.addInsn(new ArmBoolOp(armOp, armReg1, armReg2));
        }
        else if (armReg2 == destReg) {
            if (armReg1 == null) {
                armReg1 = operand1.getArmReg(mdBuilder.getTempReg1(), 
                        mdBuilder, classTypeProvider);
            }
            mdBuilder.addInsn(new ArmBoolOp(armOp, armReg2, armReg1));
        }
        else {
            if (armReg1 == null) {
                armReg1 = operand1.getArmReg(destReg, 
                        mdBuilder, classTypeProvider);
            }
            if (armReg2 == null) {
                armReg2 = operand2.getArmReg(mdBuilder.getTempReg1(), 
                        mdBuilder, classTypeProvider);
            }
            if (armReg1 != destReg) {
                mdBuilder.addInsn(new ArmMov(destReg, armReg1));
            }
            mdBuilder.addInsn(new ArmBoolOp(armOp, armReg1, armReg2));
        }

        // ArmReg armReg1 = operand1.getArmReg(destReg, mdBuilder, classTypeProvider);
        // // if (armReg1 != destReg) {
        // //     mdBuilder.addInsn(new ArmMov(destReg, armReg1));
        // //     armReg1 = destReg;
        // // }
        // ArmReg armReg2 = operand2.getArmReg(mdBuilder.getTempReg2(), 
        //         mdBuilder, classTypeProvider);
                
        // ArmBoolOp.Operator armOp = armOpForBoolOp(operator);
        // mdBuilder.addInsn(new ArmBoolOp(armOp, armReg1, armReg2));
    }

    private static ArmCondition armCondForRelOp(Op op) {
        if (op == Op.LT) {
            return ArmCondition.LT;
        } 
        else if (op == Op.GT) {
            return ArmCondition.GT;
        }
        else if (op == Op.LEQ) {
            return ArmCondition.LE;
        }
        else if (op == Op.GEQ) {
            return ArmCondition.GE;
        }
        else if (op == Op.EQ) {
            return ArmCondition.EQ;
        }
        else if (op == Op.NEQ) {
            return ArmCondition.NE;
        }
        return null;
    }

    private static ArmArithOp.Operator armOpForArithOp(Op op) {
        if (op == Op.ADD) {
            return ArmArithOp.Operator.ADD;
        } 
        else if (op == Op.SUB) {
            return ArmArithOp.Operator.SUB;
        } 
        else if (op == Op.MUL) {
            return ArmArithOp.Operator.MUL;
        }
        else if (op == Op.DIV) {
            // Not supporting division
            return null;
        } 
        return null;
    }

    private static ArmBoolOp.Operator armOpForBoolOp(Op op) {
        if (op == Op.ADD) {
            return ArmBoolOp.Operator.AND;
        }
        else if (op == Op.OR) {
            return ArmBoolOp.Operator.ORR;
        }
        return null;
    }
}