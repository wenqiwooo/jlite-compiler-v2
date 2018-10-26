package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.util.Pair;

public class ClassBundle {
    public String className;
    public List<Pair<String, String>> classFields;
    public List<MethodBundle> classMethods;

    public ClassBundle(String className, 
            List<Pair<String, String>> classFields, 
            List<MethodBundle> classMethods) {
        this.className = className;
        this.classFields = new ArrayList<>(classFields);
        this.classMethods = new ArrayList<>(classMethods);
    }
}