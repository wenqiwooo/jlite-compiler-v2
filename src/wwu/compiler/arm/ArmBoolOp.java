package wwu.compiler.arm;

import wwu.compiler.exception.*;

public class ArmBoolOp extends ArmInsn {
    ArmReg destReg;
    ArmReg srcReg;

    public enum Operator {
        AND("and"),
        ORR("orr");

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

    // Boolean operation between destReg and srcReg with result stored in destReg
    public ArmBoolOp(Operator operator, ArmReg destReg, ArmReg srcReg) {
        this.operator = operator;
        this.destReg = destReg;
        this.srcReg = srcReg;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(operator.toString())
            .append(" ")
            .append(destReg.toString())
            .append(",")
            .append(srcReg.toString())
            .append("\n");
        return sb.toString();
    }
}