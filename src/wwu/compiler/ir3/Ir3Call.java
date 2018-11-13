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
    void buildArmForAssignStmt(ArmReg destReg, ArmMdBuilder mdBuilder, 
            ClassTypeProvider classTypeProvider) {
        // Save all scratch registers except for destReg
        List<ArmReg> scratchRegs = mdBuilder.getScratchRegs();
        if (destReg != null) {
            scratchRegs.remove(destReg);
        }
        mdBuilder.addInsn(ArmStm.push(scratchRegs));
        
        setupParams(mdBuilder, classTypeProvider);

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
        mdBuilder.addInsn(ArmLdm.pop(scratchRegs));
    }


    private void setupParams(ArmMdBuilder mdBuilder, ClassTypeProvider classTypeProvider) {
        // Push all parameters of idx >= 4 to the stack in reverse order.
        for (int i = args.size() - 1; i >= ArmRegisterType.MAX_PARAM_REGS; i--) {
            Ir3BasicId arg = args.get(i);
            ArmReg armReg = arg.getArmReg(mdBuilder.getTempReg1(), 
                    mdBuilder, classTypeProvider);
            mdBuilder.addInsn(ArmStr.push(armReg));
        }

        // Populate registers whose values are already in registers
        Map<ArmReg, ArmReg> destToSrcMap = new HashMap<>();
        int argInRegCnt = Math.min(args.size(), ArmRegisterType.MAX_PARAM_REGS);

        for (int i = 0; i < argInRegCnt; i++) {
            Ir3BasicId arg = args.get(i);
            if (!(arg instanceof Ir3Identifier)) {
                continue;
            }
            Ir3Identifier argId = (Ir3Identifier)arg;
            VarLocation loc = mdBuilder.getLocationForSymbol(argId.varName);
            if (loc.inReg()) {
                ArmReg destReg = mdBuilder.getReg(ArmRegisterType.getByIdx(i));
                ArmReg srcReg = argId.tryGetArmReg(mdBuilder, classTypeProvider);
                destToSrcMap.put(destReg, srcReg);
            }
        }

        Set<ArmReg> visited = new HashSet<>();
        Map<ArmReg, ArmReg> destToTempMap = new HashMap<>();
        ArmInsnChain insnChain = new ArmInsnChain();

        for (Map.Entry<ArmReg, ArmReg> entry : destToSrcMap.entrySet()) {
            ArmReg destReg = entry.getKey();
            if (!visited.contains(destReg)) {
                visited.add(destReg);
                insnChain.concat(
                    mapArgToReg(destReg, 
                        destToSrcMap, 
                        visited, 
                        destToTempMap, 
                        mdBuilder));
            }
        }

        if (insnChain.firstInsn != null) {
            mdBuilder.addInsn(insnChain.firstInsn);
        }

        // Populate registers whose values are literals or in memory
        for (int i = 0; i < argInRegCnt; i++) {
            ArmReg destReg = mdBuilder.getReg(ArmRegisterType.getByIdx(i));
            if (destToSrcMap.containsKey(destReg)) {
                continue;
            }
            args.get(i).getArmReg(destReg, mdBuilder, classTypeProvider);
        }
    }

    private ArmInsnChain mapArgToReg(ArmReg destReg,
            Map<ArmReg, ArmReg> destToSrcMap,
            Set<ArmReg> visited, 
            Map<ArmReg, ArmReg> destToTempMap,
            ArmMdBuilder mdBuilder) {
        ArmReg srcReg = destToSrcMap.get(destReg);
        if (srcReg == destReg) {
            // Don't need to do anything, return empty chain.
            return new ArmInsnChain();
        }
        if (visited.contains(srcReg)) {
            // There is a cycle, break it here.
            ArmReg tempReg = mdBuilder.getTempReg1();
            destToTempMap.put(srcReg, tempReg);
            return new ArmInsnChain(new ArmMov(destReg, tempReg));
        }
        else {
            ArmInsnChain insnChain = new ArmInsnChain(new ArmMov(destReg, srcReg));
            if (destToSrcMap.containsKey(srcReg)) {
                visited.add(srcReg);
                insnChain.concat(
                    mapArgToReg(srcReg, destToSrcMap, visited, destToTempMap, mdBuilder));
                ArmReg tempReg = destToTempMap.getOrDefault(destReg, null);
                if (tempReg != null) {
                    insnChain.prepend(new ArmMov(tempReg, destReg));
                    destToTempMap.remove(destReg);
                }
            }
            return insnChain;
        }
    }
}