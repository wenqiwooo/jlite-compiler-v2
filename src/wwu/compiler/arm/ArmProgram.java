package wwu.compiler.arm;

import java.util.*;

import wwu.compiler.util.Pair;

public class ArmProgram {
    
    List<ArmMd> methods;

    public ArmProgram() {
        methods = new ArrayList<>();
    }

    public void addMethod(ArmMd method) {
        methods.add(method);
    }

    public void addMethods(List<ArmMd> methods) {
        this.methods.addAll(methods);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (ArmMd method : methods) {
            sb.append(method.toString());
        }

        return sb.toString();
    }
}