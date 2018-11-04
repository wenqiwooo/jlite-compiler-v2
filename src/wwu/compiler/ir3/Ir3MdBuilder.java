package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.cfg.*;
import wwu.compiler.common.*;
import wwu.compiler.exception.MethodParamRedeclaredException;
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
        cfGraph = new CFGraph();

        List<Pair<String, String>> edges = new ArrayList<>();
        int bbKey = 0;
        BasicBlock bb;

        // for (int i = 0; i < ir3Stmts.size(); i++) {
        //     if (bb == null) {
        //         bb = new BasicBlock(String.valueOf(bbKey++));
        //     }
        //     Ir3Stmt ir3 = ir3Stmts.get(i);

        // }
    }

    public void allocRegisters() {

    }
}