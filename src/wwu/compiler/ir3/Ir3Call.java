package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.arm.*;
import wwu.compiler.common.ArmMode;

public class Ir3Call extends Ir3Expr {
    String methodName;
    List<Ir3BasicId> args;

    public Ir3Call(String methodName, List<Ir3BasicId> args) {
        this.methodName = methodName;
        this.args = args;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(methodName).append("(");
        for (int i = 0; i < args.size(); i++) {
            sb.append(args.get(i).toString());
            if (i < args.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")");

        return sb.toString();
    }

    @Override
    void addUseSymbols(Set<String> symbols) {
        for (Ir3Id arg : args) {
            arg.addUseSymbols(symbols);
        }
    }

    @Override
    void buildArmForAssignStmt(ArmReg destReg, Ir3MdBuilder.ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        // Save all scratch registers except for destReg
        List<ArmReg> scratchRegs = mdBuilder.getScratchRegs();
        if (destReg != null) {
            scratchRegs.remove(destReg);
        }
        mdBuilder.addInsn(ArmStr.push(scratchRegs));
        
        // Push all parameters to the stack in reverse order.
        for (int i = args.size() - 1; i >= 0; i--) {
            Ir3BasicId arg = args.get(i);
            ArmReg armReg = arg.getArmReg(mdBuilder.getTempReg1(), 
                    mdBuilder, classTypeProvider);
            mdBuilder.addInsn(ArmStr.push(armReg));
        }

        // Pop first 4 arguments into r0, r1, r2, r3
        // TODO: This can be made more efficient
        int regCnt = Math.min(ArmRegisterType.MAX_PARAM_REGS, args.size());
        for (int i = 0; i < regCnt; i++) {
            ArmReg reg = mdBuilder.getReg(ArmRegisterType.getByIdx(i));
            mdBuilder.addInsn(ArmLdr.pop(reg));
        }

        // Branch to method
        mdBuilder.addInsn(new ArmBranch(ArmBranch.Mode.BL, methodName));

        // Unwind the stack
        ArmReg sp = mdBuilder.getSPReg();
        int n = Math.max(args.size() - ArmRegisterType.MAX_PARAM_REGS, 0);
        n *= ArmMode.WORD.getSize();
        ArmOperand offset = new ArmImmediate(n);
        mdBuilder.addInsn(
                new ArmArithOp(ArmArithOp.Operator.SUB, sp, sp, offset));
        
        // Move result from r0 to destReg
        if (destReg != null) {
            ArmReg r0 = mdBuilder.getReg(ArmRegisterType.REG_0);
            mdBuilder.addInsn(new ArmMov(destReg, r0));
        }

        // Restore sratch registers
        mdBuilder.addInsn(ArmLdr.pop(scratchRegs));
    }
}