package wwu.compiler.arm;

import wwu.compiler.exception.*;

public class ArmArithOp extends ArmInsn {
    ArmReg destReg;
    ArmReg srcReg;
    ArmOperand srcOperand;

    public enum Operator {
        ADD("add"),
        SUB("sub"),
        MUL("mul");

        private String value;

        private Operator(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(operator.toString())
            .append(" ")
            .append(destReg.toString())
            .append(",")
            .append(srcReg.toString())
            .append(",")
            .append(srcOperand.toString())
            .append("\n");
        
        return sb.toString();
    }

    
}