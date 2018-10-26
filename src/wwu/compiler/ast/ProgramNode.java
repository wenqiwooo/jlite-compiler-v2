package wwu.compiler.ast;

import java.io.*;
import java.util.*;

import wwu.compiler.common.*;
import wwu.compiler.exception.*;
import wwu.compiler.ir3.*;

public class ProgramNode extends Node {
    public ClassDeclNode mainClass;
    public List<ClassDeclNode> otherClasses;

    public ProgramNode(ClassDeclNode mainClass, List<ClassDeclNode> otherClasses) {
        this.mainClass = mainClass;
        this.otherClasses = otherClasses;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : mainClass.getLines()) {
            sb.append(s).append("\n");
        }
        for (ClassDeclNode cls : otherClasses) {
            for (String s : cls.getLines()) {
                sb.append(s).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public TypeCheckResult checkType(Ir3Builder env, boolean shouldReduce, String nextLabel) 
            throws TypeCheckException {
        TypeCheckHelper.checkType(env, mainClass);
        for (ClassDeclNode classNode : otherClasses) {
            TypeCheckHelper.checkType(env, classNode);
        }
        return new TypeCheckResult(Type.VOID);
    }

    public List<ClassBundle> toClassBundles() {
        List<ClassBundle> result = new ArrayList<>();
        result.add(mainClass.toClassBundle());
        for (ClassDeclNode classNode : otherClasses) {
            result.add(classNode.toClassBundle());
        }
        return result;
    }
}