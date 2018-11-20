package wwu.compiler.ir3;

import wwu.compiler.arm.*;

/**
 * This class is used for memmory addressing representation.
 */
public class Ir3Mem {
    public ArmReg baseReg;
    public int offset;

    public Ir3Mem(ArmReg baseReg, int offset) {
        this.baseReg = baseReg;
        this.offset = offset;
    }

    public Ir3Mem(Ir3Mem mem, int offset) {
        this.baseReg = mem.baseReg;
        this.offset = mem.offset + offset;
    }

    /**
     * Sometimes the offset may not be a valid offset immediate.
     * In that case, the address is calculated and stored in the backup register.
     * It is the caller's responsibility to ensure that baseReg and backupReg are not the same.
     */
    public ArmMem getArmMem(ArmReg backupReg, ArmMdBuilder mdBuilder) {
        if (ArmHelper.isValidOffsetImmediate(offset)) {
            return new ArmMem(baseReg, new ArmImmediate(offset));
        }
        else if (backupReg == null) {
            return null;
        }

        if (offset > 0 && ArmHelper.isValidDataImmediate(offset)) {
            mdBuilder.addInsn(new ArmArithOp(ArmArithOp.Operator.ADD, backupReg, baseReg, new ArmImmediate(offset)));
        }
        else if (offset < 0 && ArmHelper.isValidDataImmediate(-offset)) {
            mdBuilder.addInsn(new ArmArithOp(ArmArithOp.Operator.SUB, backupReg, baseReg, new ArmImmediate(-offset)));
        }
        else {
            mdBuilder.addInsn(new ArmLdr(backupReg, new ArmLdrConstruct(offset)))
                .addInsn(new ArmArithOp(ArmArithOp.Operator.ADD, backupReg, baseReg, backupReg));
        }
        return new ArmMem(backupReg);
    }
}