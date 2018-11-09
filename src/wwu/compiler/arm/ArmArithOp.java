package wwu.compiler.arm;

import wwu.compiler.exception.*;

public class ArmArithOp extends ArmInsn {
    ArmReg destReg;
    ArmReg srcReg;
    ArmOperand srcOperand;

    public enum Operator {
        ADD,
        SUB,
        MUL;
    }

    Operator operator;

    public ArmArithOp(Operator operator, ArmReg destReg, ArmReg srcReg, ArmOperand srcOperand) {
        this.operator = operator;
        this.destReg = destReg;
        this.srcReg = srcReg;
        this.srcOperand = srcOperand;
        
        if (this.operator == Operator.MUL && !(srcOperand instanceof ArmReg)) {
            throw new CodeGenerationException("Second operand must be a register");
        }
    }
}