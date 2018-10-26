package wwu.compiler.ast;

import java.io.*;
import java.util.*;

import wwu.compiler.ir3.*;
import wwu.compiler.common.*;
import wwu.compiler.exception.*;
import wwu.compiler.util.Pair;

public class MdDeclNode extends Node {
    public String returnType;
    public String name;
    public List<VarDeclNode> params;
    public List<VarDeclNode> locals;
    public List<StmtNode> stmts;
    
    public MdDeclNode(String rType, String id, List<VarDeclNode> params, 
            List<VarDeclNode> locals, List<StmtNode> stmts) {
        this.returnType = rType;
        this.name = id;
        this.params = params;
        this.locals = locals;
        this.stmts = stmts;
    }

    @Override
    public List<String> getLines() {
        List<String> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %s(", returnType, name));
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).toString());
            if (i < params.size() - 1)
                sb.append(",");
        }
        sb.append("){");
        res.add(sb.toString());
        for (VarDeclNode v : locals) {
            res.add(String.format("\t%s;", v));
        }
        for (StmtNode stmt : stmts) {
            for (String s : stmt.getLines()) {
                res.add("\t" + s);
            }
        }
        res.add("}");
        return res;
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        env.cgBeginMethod(name, getParamTypes());
        
        TypeCheckResult resStmts = TypeCheckHelper.checkType(env, stmts);
        if (!TypeHelper.isASuperclassOfB(returnType, resStmts.type)) {
            String errMsg = String.format(
                    "Class %s method %s requires %s return type but method body has type %s",
                    env.getCurrentClassName(), 
                    env.getCurrentMethodName(),  
                    returnType, 
                    resStmts.type);
            throw new TypeCheckException(errMsg);
        }

        env.cgEndMethod();

        return new TypeCheckResult(Type.VOID);
    }

    public MethodBundle toMethodBundle() {
        return new MethodBundle(name, returnType, getParams(), getLocals());
    }

    List<Pair<String, String>> getParams() {
        List<Pair<String, String>> paramList = new ArrayList<>();
        for (VarDeclNode param : params) {
            paramList.add(param.toPair());
        }
        return paramList;
    }

    List<Pair<String, String>> getLocals() {
        List<Pair<String, String>> localList = new ArrayList<>();
        for (VarDeclNode local : locals) {
            localList.add(local.toPair());
        }
        return localList;
    }

    List<String> getParamTypes() {
        List<String> paramTypes = new ArrayList<>();
        for (VarDeclNode param : params) {
            paramTypes.add(param.type);
        }
        return paramTypes;
    }
}