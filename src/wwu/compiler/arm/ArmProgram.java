package wwu.compiler.arm;

import java.util.*;

import wwu.compiler.util.Pair;

public class ArmProgram {
    
    Map<String, String> stringLiteralMap;
    Map<Integer, String> wordLiteralMap;
    List<ArmMd> methods;

    public ArmProgram() {
        methods = new ArrayList<>();
        stringLiteralMap = new LinkedHashMap<>();
        wordLiteralMap = new LinkedHashMap<>();
    }

    public void addMethod(ArmMd method) {
        methods.add(method);
    }

    public void addMethods(List<ArmMd> methods) {
        this.methods.addAll(methods);
    }

    public void addLiteral(String label, String x) {
        stringLiteralMap.put(x, label);
    }

    public void addLiteral(String label, int x) {
        wordLiteralMap.put(x, label);
    }

    public String getLabelForLiteral(String x) {
        return stringLiteralMap.getOrDefault(x, null);
    }

    public String getLabelForLiteral(int x) {
        return wordLiteralMap.getOrDefault(x, null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(".data\n");

        for (Map.Entry<String, String> entry : stringLiteralMap.entrySet()) {
            sb.append(entry.getValue())
                .append(":\n")
                .append(".asciz ")
                .append("\"")
                .append(entry.getKey())
                .append("\"\n");
        }
        for (Map.Entry<Integer, String> entry : wordLiteralMap.entrySet()) {
            sb.append(entry.getValue())
                .append(":\n")
                .append(".word ")
                .append(entry.getKey())
                .append("\n");
        }

        sb.append("\n.text\n.global main\n\n");
        
        for (ArmMd method : methods) {
            sb.append(method.toString());
        }

        return sb.toString();
    }
}