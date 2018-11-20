package wwu.compiler.ir3;

import java.util.*;
import wwu.compiler.arm.*;
import wwu.compiler.common.*;


public class ArmMdBuilder {
    String className;
    String methodName;
    Map<String, String> paramToTypeMap;
    Map<String, String> localToTypeMap;

    Map<String, VarLocation> varToLocationMap;
    Map<ArmRegisterType, ArmReg> registerMap;
    // Variables that are spilled
    Set<String> spills;
    // Params whose values are live
    Set<String> liveParams;

    ArmInsn firstInsn;
    ArmInsn lastInsn;

    // Offset for parameters
    // lr, fp, r4, r5, r6, r7, r8 are saved above parameters
    int paramOffset = 7 * 4;
    // Offset for locals
    int localOffset = 0;

    ArmMdBuilder(String className, 
            String methodName, 
            Map<String, String> params, 
            Map<String, String> localDecls, 
            Map<String, Integer> assignment, 
            Set<String> spills, 
            Set<String> liveParams) {
        this.className = className;
        this.methodName = methodName;

        paramToTypeMap = new LinkedHashMap<>();
        paramToTypeMap.put("this", this.className);
        for (Map.Entry<String, String> paramEntry : params.entrySet()) {
            paramToTypeMap.put(paramEntry.getKey(), paramEntry.getValue());
        }

        localToTypeMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> localEntry : localDecls.entrySet()) {
            localToTypeMap.put(localEntry.getKey(), localEntry.getValue());
        }

        this.spills = spills;
        this.liveParams = liveParams;

        varToLocationMap = new HashMap<>();
        registerMap = new HashMap<>();

        Map<Integer, ArmReg> idxToRegisterMap = new HashMap<>();
        assignRegisters(assignment, idxToRegisterMap);

        // Detach existing arm insns
        ArmInsn procedureStart = firstInsn;
        firstInsn = null;
        lastInsn = null;

        // Add prologue
        List<ArmReg> nonScratchRegs = getNonScratchRegs();
        addInsn(ArmStm.push(Arrays.asList(getFPReg(), getLRReg())));
        addInsn(ArmStm.push(nonScratchRegs));
        addInsn(new ArmMov(getFPReg(), getSPReg()));
        addInsn(new ArmArithOp(ArmArithOp.Operator.SUB, getSPReg(), 
                getSPReg(), new ArmImmediate(localOffset)));

        // Reattach arm insns
        if (procedureStart != null) {
            addInsn(procedureStart);
        }
    }

    // Never call this twice
    ArmMd build() {
        // Add epilogue
        if (methodName.equals("main")) {
            addInsn(new ArmMov(getReg(ArmRegisterType.REG_0), new ArmImmediate(0)));
        }
        addInsn(new ArmLabel(getMethodExitLabel()));
        addInsn(new ArmArithOp(ArmArithOp.Operator.ADD, getSPReg(),
                getSPReg(), new ArmImmediate(localOffset)));
        List<ArmReg> nonScratchRegs = getNonScratchRegs();
        addInsn(ArmLdm.pop(nonScratchRegs));
        addInsn(ArmLdm.pop(Arrays.asList(getFPReg(), getPCReg())));
        
        return new ArmMd(methodName, firstInsn);
    }

    ArmReg getReg(ArmRegisterType regType) {
        ArmReg reg = registerMap.getOrDefault(regType, null);
        if (reg == null) {
            reg = new ArmReg(regType);
            registerMap.put(reg.getType(), reg);
        }
        return reg;
    }

    ArmReg getSPReg() {
        return getReg(ArmRegisterType.REG_SP);
    }

    ArmReg getFPReg() {
        return getReg(ArmRegisterType.REG_FP);
    }

    ArmReg getLRReg() {
        return getReg(ArmRegisterType.REG_LR);
    }

    ArmReg getPCReg() {
        return getReg(ArmRegisterType.REG_PC);
    }

    ArmReg getTempReg1() {
        return getReg(ArmRegisterType.REG_IP);
    }

    ArmReg getTempReg2() {
        return getReg(ArmRegisterType.REG_8);
    }

    ArmReg getTempReg3() {
        return getReg(ArmRegisterType.REG_7);   
    }

    String getMethodExitLabel() {
        return methodName + "_exit";
    }

    List<ArmReg> getScratchRegs() {
        List<ArmReg> res = new ArrayList<>();
        ArmRegisterType[] scratchRegTypes = {
            ArmRegisterType.REG_0, 
            ArmRegisterType.REG_1, 
            ArmRegisterType.REG_2,
            ArmRegisterType.REG_3,
            ArmRegisterType.REG_IP
        };
        for (ArmRegisterType regType : scratchRegTypes) {
            res.add(getReg(regType));
        }
        return res;
    }

    List<ArmReg> getNonScratchRegs() {
        List<ArmReg> res = new ArrayList<>();
        ArmRegisterType[] nonScratchRegTypes = {
            ArmRegisterType.REG_4,
            ArmRegisterType.REG_5,
            ArmRegisterType.REG_6,
            ArmRegisterType.REG_7,
            ArmRegisterType.REG_8
        };
        for (ArmRegisterType regType : nonScratchRegTypes) {
            res.add(getReg(regType));
        }
        return res;
    }

    VarLocation getLocationForSymbol(String sym) {
        return varToLocationMap.getOrDefault(sym, null);
    }

    ArmMdBuilder prependInsn(ArmInsn insn) {
        if (firstInsn == null) {
            addInsn(insn);
        } else {
            insn.setNext(firstInsn);
            firstInsn.setPrev(insn);
            firstInsn = insn;
        }
        return this;
    }

    ArmMdBuilder addInsn(ArmInsn insn) {
        if (firstInsn == null) {
            firstInsn = insn;
        } else {
            lastInsn.setNext(insn);
            insn.setPrev(lastInsn);
        }
        lastInsn = insn;
        while (lastInsn.next != null) {
            lastInsn = lastInsn.next;
        }
        return this;
    }

    private void assignRegisters(Map<String, Integer> assignment, 
            Map<Integer, ArmReg> idxToRegisterMap) {
        // Set parameters to their initial locations
        int cnt = 0;
        for (Map.Entry<String, String> paramEntry : paramToTypeMap.entrySet()) {
            String paramName = paramEntry.getKey();
            String paramType = paramEntry.getValue();
            VarLocation loc;

            if (cnt < ArmRegisterType.MAX_PARAM_REGS) {
                ArmReg reg = getReg(ArmRegisterType.getByIdx(cnt));
                loc = new VarLocation(paramName, reg);
            } 
            else {
                Ir3Mem mem = new Ir3Mem(getFPReg(), paramOffset);
                paramOffset += TypeHelper.getSizeForType(paramType);
                loc = new VarLocation(paramName, mem);
            }
            
            varToLocationMap.put(paramName, loc);
            cnt++;
        }

        // Reorder our params so that assignment is done for live params first
        Map<String, String> orderedParams = new LinkedHashMap<>();
        for (Map.Entry<String, String> paramEntry : paramToTypeMap.entrySet()) {
            if (liveParams.contains(paramEntry.getKey())) {
                orderedParams.put(paramEntry.getKey(), paramEntry.getValue());
            }
        }
        for (Map.Entry<String, String> paramEntry : paramToTypeMap.entrySet()) {
            if (!orderedParams.containsKey(paramEntry.getKey())) {
                orderedParams.put(paramEntry.getKey(), paramEntry.getValue());
            }
        }

        // Assign registers to parameters and move them if neccessary
        for (Map.Entry<String, String> paramEntry : orderedParams.entrySet()) {
            String paramName = paramEntry.getKey();
            String paramType = paramEntry.getValue();
            VarLocation loc = varToLocationMap.get(paramName);
            
            if (spills.contains(paramName)) { // Spill
                if (loc.inReg()) { // If param is in a register, move it to stack
                    localOffset += TypeHelper.getSizeForType(paramType);
                    Ir3Mem destMem = new Ir3Mem(getFPReg(), -localOffset);

                    ArmMem destArmMem = destMem.getArmMem(getTempReg1(), this);
                    addInsn(new ArmStr(destArmMem, loc.getReg()));
                    
                    loc.updateLocation(destMem);
                }
            }
            else { // Non-spill
                int assignIdx = assignment.get(paramName);
                ArmReg reg = idxToRegisterMap.getOrDefault(assignIdx, null);

                if (loc.inReg()) {
                    // Assign param to the register it was passed in if possible,
                    // otherwise move it to the assigned register
                    if (reg == null) {
                        reg = getNextAvailableReg(idxToRegisterMap, loc.getReg());
                        idxToRegisterMap.put(assignIdx, reg);
                    }
                    if (reg != loc.getReg()) {
                        // If param value is dead to begin with, there is no need to move its value
                        if (liveParams.contains(paramName)) {
                            addInsn(new ArmMov(reg, loc.getReg()));
                        }
                        loc.updateLocation(reg);
                    }
                }
                else {
                    // Param is passed via stack,
                    // assign it to a register since it is non-spill
                    if (reg == null) {
                        reg = getNextAvailableReg(idxToRegisterMap, null);
                        idxToRegisterMap.put(assignIdx, reg);
                    }
                    if (liveParams.contains(paramName)) {
                        ArmMem srcArmMem = loc.getMem().getArmMem(getTempReg1(), this);
                        addInsn(new ArmLdr(reg, srcArmMem));
                    }
                    loc.updateLocation(reg);
                }
            }
        }

        // Assign registers, allocate memory for local variables
        for (Map.Entry<String, String> localEntry : localToTypeMap.entrySet()) {
            String localName = localEntry.getKey();
            String localType = localEntry.getValue();
            VarLocation loc;

            if (spills.contains(localName)) { // Spill variable
                localOffset += TypeHelper.getSizeForType(localType);
                Ir3Mem destMem = new Ir3Mem(getFPReg(), -localOffset);
                loc = new VarLocation(localName, destMem);
            }
            else { 
                int assignIdx = assignment.get(localName);
                ArmReg reg = idxToRegisterMap.getOrDefault(assignIdx, null);
                
                if (reg == null) {
                    reg = getNextAvailableReg(idxToRegisterMap, null);
                    idxToRegisterMap.put(assignIdx, reg);
                }
                loc = new VarLocation(localName, reg);
            }
            
            varToLocationMap.put(localName, loc);
        }
    }

    /**
     * Returns preferredReg if it is available or the next available register
     */
    private ArmReg getNextAvailableReg(Map<Integer, ArmReg> idxToRegisterMap, 
            ArmReg preferredReg) {
        if (preferredReg != null && !idxToRegisterMap.containsValue(preferredReg)) {
            return preferredReg;
        }
        for (int i = 0; i < ArmRegisterType.MAX_ASSIGNABLE; i++) {
            ArmReg reg = getReg(ArmRegisterType.getByIdx(i));
            if (!idxToRegisterMap.containsValue(reg)) {
                return reg;
            }
        }
        return null;
    }
}