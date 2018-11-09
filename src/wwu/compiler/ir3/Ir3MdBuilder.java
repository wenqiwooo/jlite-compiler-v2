package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.arm.*;
import wwu.compiler.cfg.*;
import wwu.compiler.common.*;
import wwu.compiler.exception.*;
import wwu.compiler.util.Pair;

public class Ir3MdBuilder {
    String className;
    String methodName;
    String methodEncodedName;
    String methodKey;
    String returnType;
    // Parameter to type map
    Map<String, String> params;
    List<String> paramTypes;
    // Local decl to type map
    Map<String, String> localDecls;
    
    Ir3Stmt firstStmt;
    Ir3Stmt lastStmt;

    // Control flow graph
    CFGraph cfGraph;

    public Ir3MdBuilder(String className, 
            String methodName,
            String methodEncodedName, 
            String methodKey,
            String returnType,
            List<Pair<String, String>> paramList, 
            List<Pair<String, String>> localList) {
        this.className = className;
        this.methodName = methodName;
        this.methodEncodedName = methodEncodedName;
        this.methodKey = methodKey;
        this.returnType = returnType;

        params = new LinkedHashMap<>();
        paramTypes = new ArrayList<>();
        for (Pair<String, String> param : paramList) {
            // No two parameters in a method declaration can have the same name.
            if (params.containsKey(param.first())) {
                throw new MethodParamRedeclaredException(
                        this.className, this.methodName, param.first());
            }
            params.put(param.first(), param.second());
            paramTypes.add(param.second());
        }

        localDecls = new LinkedHashMap<>();
        for (Pair<String, String> local : localList) {
            localDecls.put(local.first(), local.second());
        }
    }

    public void addLocalDecl(String varName, String varType) {
        localDecls.put(varName, varType);
    }

    public void addCode(Ir3Stmt ir3Stmt) {
        if (firstStmt == null) {
            firstStmt = ir3Stmt;
        } else {
            lastStmt.setNext(ir3Stmt);
            ir3Stmt.setPrev(lastStmt);
        }
        lastStmt = ir3Stmt;
    }

    public String toCode() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(returnType)
            .append(" ")
            .append(methodEncodedName)
            .append("(")
            .append(className)
            .append(" this");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(",")
                .append(entry.getValue())
                .append(" ")
                .append(entry.getKey());
        }
        sb.append("){\n");

        for (Map.Entry<String, String> entry : localDecls.entrySet()) {
            sb.append("    ")
                .append(entry.getValue())
                .append(" ")
                .append(entry.getKey())
                .append(";\n");
        }

        Ir3Stmt ir3Stmt = firstStmt;
        while (ir3Stmt != null) {
            // sb.append("    In state: ")
            //     .append(ir3Stmt.basicBlockStmt.getLiveIn())
            //     .append("\n");

            sb.append("  ");
            if (!(ir3Stmt instanceof Ir3Label)) {
                sb.append("  ");
            }
            sb.append(ir3Stmt.toString())
                .append("\n");
            ir3Stmt = ir3Stmt.next;
        }
        
        sb.append("}\n\n");

        return sb.toString();
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getMethodEncodedName() {
        return methodEncodedName;
    }

    public String getMethodKey() {
        return methodKey;
    }

    public String getReturnType() {
        return returnType;
    }

    public Map<String, String> getAllParams() {
        return new LinkedHashMap(params);
    }

    public List<String> getParamTypes() {
        return new ArrayList<>(paramTypes);
    }

    public Map<String, String> getAllLocalDecls() {
        return new LinkedHashMap(localDecls);
    }

    public boolean hasLocalDecl(String varName) {
        return localDecls.containsKey(varName);
    }

    public boolean hasParam(String varName) {
        return params.containsKey(varName);
    }

    public String getLocalType(String varName) {
        return localDecls.getOrDefault(varName, null);
    }

    public String getParamType(String varName) {
        return params.getOrDefault(varName, null);
    }

    public void buildCFGraph() {
        cfGraph = new CFGraph(getAllSymbols());

        // { pred, succ }
        List<Pair<String, String>> edgesToAdd = new ArrayList<>();
        // { bb_key, label }
        List<Pair<String, String>> gotoLabels = new ArrayList<>();
        // label -> bb_key
        Map<String, String> labelToBlockMap = new HashMap<>();

        int bbKey = 0;
        BasicBlock bb = null;

        Ir3Stmt stmt = firstStmt;

        while (stmt != null) {
            if (bb == null) {
                bb = new BasicBlock(String.valueOf(bbKey++));
            }

            BasicBlockStmt bbStmt = new BasicBlockStmt(stmt);
            bb.addBasicBlockStmt(bbStmt);

            if (stmt.prev == null) { // First stmt
                edgesToAdd.add(new Pair<>(CFGraph.ENTRY_KEY, bb.getKey()));
            } 

            if (stmt.next == null) { // Last stmt
                edgesToAdd.add(new Pair<>(bb.getKey(), CFGraph.EXIT_KEY));
            } 
            else if (stmt instanceof Ir3ReturnStmt) { // Return
                edgesToAdd.add(new Pair<>(bb.getKey(), CFGraph.EXIT_KEY));
            }
            else if (stmt instanceof Ir3IfGotoStmt) { 
                // Current stmt is conditional jump and next stmt is not null,
                // so possible to get from current stmt to next
                edgesToAdd.add(new Pair<>(bb.getKey(), String.valueOf(bbKey)));
            }
            else if (stmt.next instanceof Ir3Label && 
                !(stmt instanceof Ir3GotoStmt || stmt instanceof Ir3ReturnStmt)) {
                // Current stmt is unconditional jump and next stmt is a label
                edgesToAdd.add(new Pair<>(bb.getKey(), String.valueOf(bbKey)));
            }

            if (stmt instanceof Ir3Label) { // Block starts with a label
                labelToBlockMap.put(((Ir3Label)stmt).label, bb.getKey());
            }
            else if (stmt instanceof Ir3GotoStmt) { // Unconditional jump
                gotoLabels.add(new Pair<>(bb.getKey(), ((Ir3GotoStmt)stmt).label));
            }
            else if (stmt instanceof Ir3IfGotoStmt) { // Conditional jump
                gotoLabels.add(new Pair<>(bb.getKey(), ((Ir3IfGotoStmt)stmt).label));
            }

            if (stmt.next == null ||
                /* Label begins a new basic block */
                stmt.next instanceof Ir3Label ||
                /* Unconditional jump ends a basic block */
                stmt instanceof Ir3GotoStmt ||
                /* Conditional jump ends a basic block  */ 
                stmt instanceof Ir3IfGotoStmt || 
                /* Return */
                stmt instanceof Ir3ReturnStmt) {
                cfGraph.addBasicBlock(bb);
                bb = null;
            }

            stmt = stmt.next;
        }

        for (Pair<String, String> gotoPair : gotoLabels) {
            String predKey = gotoPair.first();
            String succKey = labelToBlockMap.get(gotoPair.second());
            edgesToAdd.add(new Pair<>(predKey, succKey));
        }

        for (Pair<String, String> edge : edgesToAdd) {
            cfGraph.addEdge(edge.first(), edge.second());
        }
    }

    /**
     * 
     */
    public ArmMd getArmMd(ClassTypeProvider classTypeProvider) {
        buildCFGraph();
        cfGraph.backwardAnalysis(new LivenessFunction());
        Pair<Map<String, Integer>, Set<String>> res = 
                cfGraph.allocRegisters(ArmRegisterType.MAX_ASSIGNABLE);
        Map<String, Integer> assignment = res.first();
        Set<String> spills = res.second();
        
        /**
         * For spill variables, we need to have stack memory allocated for them 
         * for following cases:
         * - Variable is first 4 parameters (including 'this')
         * - Variable is local
         * 
         * 
         * higher address ---------------------------------------------- lower address
         *                [parameters][lr][fp][saved registers] [locals] 
         *                                                     ^ current fp points here 
         * 
         * 
         * Prologue:
         *   strfd {lr, fp} // strfd {fp} if the method does not call other methods
         *   strfd {non-scratch regs}
         *   mov fp, sp
         *   sub sp, sp, #<space needed for locals>
         * 
         * Epilogue:
         * func_name_exit:
         *   add sp, sp, #<space needed for locals>
         *   ldrfd {non-scratch regs}
         *   ldrfd {fp, pc} // ldrfd {fp}; bx lr if the method does not call other methods
         */

        ArmMdBuilder mdBuilder = new ArmMdBuilder(assignment, spills);

        Ir3Stmt stmt = firstStmt;
        while (stmt != null) {
            stmt.buildArm(mdBuilder, classTypeProvider);
            stmt = stmt.next;
        }

        return mdBuilder.build();
    }

    class ArmMdBuilder {
        Map<String, VarLocation> varToLocationMap;
        Map<ArmRegisterType, ArmReg> registerMap;
        // Variables that are spilled
        Set<String> spills;

        ArmInsn firstInsn;
        ArmInsn lastInsn;

        // Offset for parameters
        // lr, fp, r4, r5, r6, r7, r8 are saved above parameters
        int paramOffset = 7 * 4;
        // Offset for locals
        int localOffset = 0;

        ArmMdBuilder(Map<String, Integer> assignment, Set<String> spills) {
            this.spills = spills;
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
            return getReg(ArmRegisterType.REG_8);
        }

        ArmReg getTempReg2() {
            return getReg(ArmRegisterType.REG_IP);
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
            for (Map.Entry<String, String> paramEntry : params.entrySet()) {
                String paramName = paramEntry.getKey();
                String paramType = paramEntry.getValue();
                VarLocation loc;

                if (cnt < ArmRegisterType.MAX_PARAM_REGS) {
                    ArmReg reg = getReg(ArmRegisterType.getByIdx(cnt));
                    loc = new VarLocation(paramName, reg);
                } 
                else {
                    ArmImmediate offset = new ArmImmediate(paramOffset);
                    // Using a full descending stack so subtract offset here
                    ArmMem mem = new ArmMem(getFPReg(), offset);
                    paramOffset += TypeHelper.getSizeForType(paramType);
                    loc = new VarLocation(paramName, mem);
                }
                
                varToLocationMap.put(paramName, loc);
                cnt++;
            }

            // Assign registers to parameters and move them if neccessary
            for (Map.Entry<String, String> paramEntry : params.entrySet()) {
                String paramName = paramEntry.getKey();
                String paramType = paramEntry.getValue();
                VarLocation loc = varToLocationMap.get(paramName);
                
                if (spills.contains(paramName)) { // Spill
                    if (loc.inReg()) { // If param is in a register, move it to stack
                        localOffset += TypeHelper.getSizeForType(paramType);
                        ArmImmediate offset = new ArmImmediate(-localOffset);
                        ArmMem destMem = new ArmMem(getFPReg(), offset);

                        addInsn(new ArmStr(destMem, loc.getReg()));
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
                            reg = getNextAvailableReg(idxToRegisterMap);
                            idxToRegisterMap.put(assignIdx, reg);
                        }
                        if (reg != loc.getReg()) {
                            addInsn(new ArmMov(reg, loc.getReg()));
                            loc.updateLocation(reg);
                        }
                    }
                    else {
                        // Param is passed via stack,
                        // assign it to a register since it is non-spill
                        if (reg == null) {
                            reg = getNextAvailableReg(idxToRegisterMap);
                            idxToRegisterMap.put(assignIdx, reg);
                        }
                        addInsn(new ArmLdr(reg, loc.getMem()));
                        loc.updateLocation(reg);
                    }
                }
            }

            // Assign registers, allocate memory for local variables
            for (Map.Entry<String, String> localEntry : localDecls.entrySet()) {
                String localName = localEntry.getKey();
                String localType = localEntry.getValue();
                VarLocation loc;

                if (spills.contains(localName)) { // Spill variable
                    localOffset += TypeHelper.getSizeForType(localType);
                    ArmImmediate offset = new ArmImmediate(-localOffset);
                    ArmMem destMem = new ArmMem(getFPReg(), offset);
                    loc = new VarLocation(localName, destMem);
                }
                else { 
                    int assignIdx = assignment.get(localName);
                    ArmReg reg = idxToRegisterMap.getOrDefault(assignIdx, null);
                    
                    if (reg == null) {
                        reg = getNextAvailableReg(idxToRegisterMap);
                        idxToRegisterMap.put(assignIdx, reg);
                    }
                    loc = new VarLocation(localName, reg);
                }
                
                varToLocationMap.put(localName, loc);
            }
        }

        private ArmReg getNextAvailableReg(Map<Integer, ArmReg> idxToRegisterMap) {
            for (int i = 0; i < ArmRegisterType.MAX_ASSIGNABLE; i++) {
                ArmReg reg = getReg(ArmRegisterType.getByIdx(i));
                if (!idxToRegisterMap.containsValue(reg)) {
                    return reg;
                }
            }
            return null;
        }
    }

    private Set<String> getAllSymbols() {
        Set<String> res = new HashSet<>();
        res.add("this");
        params.forEach((name, type) -> res.add(name));
        localDecls.forEach((name, type) -> res.add(name));
        return res;
    }
}