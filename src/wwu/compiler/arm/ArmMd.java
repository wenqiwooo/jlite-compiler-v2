package wwu.compiler.arm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ArmMd {

    Map<String, ArmObj> params;
    Map<String, ArmObj> localDecls;
    List<ArmInsn> insns;

    int nextPseudoRegId = 50;

    public ArmMd() {
        params = new LinkedHashMap<>();
        localDecls = new LinkedHashMap<>();
        insns = new ArrayList<>();
    }

    public void addParam(ArmObj param) {
        params.put(param.name, param);
    }

    public void addLocal(ArmObj local) {
        localDecls.put(local.name, local);
    }

    public void addInsn(ArmInsn insn) {
        insns.add(insn);
    }

    public int getNewPseudoRegId() {
        return nextPseudoRegId++;
    }

    public ArmObj getArmObjForVarName(String name) {
        ArmObj obj = localDecls.getOrDefault(name, null);
        // if (obj == null) {
        //     obj = params.get(name)
        // }
        return obj;
    }
}