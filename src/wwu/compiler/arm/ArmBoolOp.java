package wwu.compiler.arm;

import wwu.compiler.exception.*;

public class ArmBoolOp extends ArmInsn {
    ArmReg destReg;
    ArmReg srcReg;

    public enum Operator {
        AND,
        ORR;
    }

    Operator operator;

    // Boolean operation between destReg and srcReg with result stored in destReg
    public ArmBoolOp(Operator operator, ArmReg destReg, ArmReg srcReg) {
        this.operator = operator;
        this.destReg = destReg;
        this.srcReg = srcReg;
    }
}