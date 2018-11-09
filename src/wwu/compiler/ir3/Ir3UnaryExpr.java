package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.arm.*;
import wwu.compiler.common.*;
import wwu.compiler.exception.*;

public class Ir3UnaryExpr extends Ir3BasicExpr {
    Op operator;
    Ir3BasicId operand;

    public Ir3UnaryExpr(Op operator, Ir3BasicId operand) {
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public String toString() {
        return operator.toString() + operand.toString();
    }

    @Override
    void addUseSymbols(Set<String> symbols) {
        operand.addUseSymbols(symbols);
    }

    @Override
    void buildArmForAssignStmt(ArmReg destReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) throws CodeGenerationException {
        mdBuilder.addInsn(new ArmMov(mdBuilder.getTempReg1(), new ArmImmediate(0)));
        ArmReg armReg = operand.getArmReg(mdBuilder.getTempReg2(), 
                mdBuilder, classTypeProvider);

        // TODO: Use pseudo instructions for these
        if (operator == Op.SUB) {
            mdBuilder.addInsn(new ArmArithOp(ArmArithOp.Operator.SUB, 
                    destReg, mdBuilder.getTempReg1(), armReg));
        }
        else if (operator == Op.NOT) {
            mdBuilder.addInsn(new ArmCompare(armReg, mdBuilder.getTempReg1()))
                    .addInsn(new ArmMov(destReg, new ArmImmediate(1), 
                            ArmCondition.EQ))
                    .addInsn(new ArmMov(destReg, new ArmImmediate(0), 
                            ArmCondition.NE));
        }
    }
}