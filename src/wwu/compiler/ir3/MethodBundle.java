package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.util.Pair;

public class MethodBundle {
    String methodName;
    String returnType;
    List<Pair<String, String>> paramList;
    List<Pair<String, String>> localList;

    public MethodBundle(String methodName, String returnType, 
            List<Pair<String, String>> paramList, 
            List<Pair<String, String>> localList) {
        this.methodName = methodName;
        this.returnType = returnType;
        this.paramList = new ArrayList<>(paramList);
        this.localList = new ArrayList<>(localList);
    }
}