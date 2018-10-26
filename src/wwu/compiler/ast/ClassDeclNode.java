package wwu.compiler.ast;

import java.util.*;

import wwu.compiler.ir3.*;
import wwu.compiler.common.*;
import wwu.compiler.exception.*;
import wwu.compiler.util.Pair;

public class ClassDeclNode extends Node {
    public String name;
    public List<VarDeclNode> fields;
    public List<MdDeclNode> methods;
    
    public ClassDeclNode(String name, List<VarDeclNode> fields, List<MdDeclNode> methods) {
        this.name = name;
        this.fields = fields;
        this.methods = methods;
    }

    @Override
    public List<String> getLines() {
        List<String> res = new ArrayList<>();
        res.add(String.format("class %s {", name));
        for (VarDeclNode attr : fields) {
            res.add(String.format("\t%s;", attr));
        }
        for (MdDeclNode md : methods) {
            for (String s : md.getLines()) {
                res.add("\t" + s);
            }
        }
        res.add("}");
        return res;
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        env.cgBeginClass(name);
        
        for (MdDeclNode methodNode : methods) {
            TypeCheckHelper.checkType(env, methodNode);
        }

        env.cgEndClass();
        return new TypeCheckResult(Type.VOID);
    }

    public List<Pair<String, String>> getFields() {
        List<Pair<String, String>> result = new ArrayList<>();
        for (VarDeclNode field : fields) {
            result.add(field.toPair());
        }
        return result;
    }

    public List<MethodBundle> getMethods() {
        List<MethodBundle> result = new ArrayList<>();
        for (MdDeclNode method : methods) {
            result.add(method.toMethodBundle());
        }
        return result;
    }

    public ClassBundle toClassBundle() {
        return new ClassBundle(name, getFields(), getMethods());
    }
}