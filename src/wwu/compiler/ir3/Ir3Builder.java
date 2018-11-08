package wwu.compiler.ir3;

import java.util.*;

import wwu.compiler.util.Pair;
import wwu.compiler.arm.ArmProgram;
import wwu.compiler.common.TypeHelper;
import wwu.compiler.exception.*;
import wwu.compiler.exception.ClassNotFoundException;
import wwu.compiler.exception.TypeCheckException;


public class Ir3Builder implements ClassTypeProvider {
    private Map<String, Ir3ClassBuilder> classToBuildersMap;
    private Ir3ClassBuilder currClsBuilder;
    private Ir3MdBuilder currMdBuilder;
    private int labelIdx;
    private int tempVarIdx;

    public Ir3Builder(List<ClassBundle> classBundles) throws TypeCheckException {
        classToBuildersMap = new LinkedHashMap<>();
        for (ClassBundle classBundle : classBundles) {
            addClass(classBundle);
        }
        validate();
    }

    public boolean isVarClassField(String varName) {
        if (varName.equals("this")) {
            return false;
        }

        if (currMdBuilder != null) {
            if (currMdBuilder.hasLocalDecl(varName) || 
                    currMdBuilder.hasParam(varName)) {
                return false;
            }
        }

        return true;
    }

    public String getTypeForVar(String varName) throws TypeCheckException {
        if (varName.equals("this")) {
            return getCurrentClassType();
        }
        if (currMdBuilder != null) {
            String type = currMdBuilder.getLocalType(varName);
            if (type != null) {
                return type;
            }
            type = currMdBuilder.getParamType(varName);
            if (type != null) {
                return type;
            }
        }
        return currClsBuilder.getTypeForField(varName);
    }

    public String getTypeForField(String className, String fieldName) 
            throws TypeCheckException {
        Ir3ClassBuilder cb = classToBuildersMap.getOrDefault(className, null);
        if (cb == null) {
            throw new ClassNotFoundException(className);
        }
        return cb.getTypeForField(fieldName);
    }

    public String getReturnTypeForMethod(String className, String methodName) 
            throws TypeCheckException {
        if (TypeHelper.isBuiltInType(className)) {
            throw new TypeCheckException(
                    className + " is a built-in type and does not have any methods to call");
        }

        Ir3ClassBuilder cb = classToBuildersMap.getOrDefault(className, null);
        if (cb == null) {
            throw new ClassNotFoundException(className);
        }
        return cb.getReturnTypeForMethod(methodName);
    }

    public String getCurrentClassType() {
        return currClsBuilder != null ? currClsBuilder.getClassName() : null;
    }

    public String getCurrentClassName() {
        return currClsBuilder != null ? currClsBuilder.getClassName() : null;
    }

    public String getCurrentMethodName() {
        return currMdBuilder != null ? currMdBuilder.getMethodName() : null;
    }

    public String getCurrentMethodReturnType() {
        return currMdBuilder != null ? currMdBuilder.getReturnType() : null;
    }

    public boolean isValidUserDefinedType(String type) {
        return classToBuildersMap.containsKey(type);
    }

    /**
     * Originally this checks that the declared variable is not of type Void 
     * but apparently spec allows variable declarations of Void type.
     */
    public boolean isValidDeclType(String type) {
        return isValidType(type);
    }

    public boolean isValidType(String type) {
        return TypeHelper.isBuiltInType(type) || 
                classToBuildersMap.containsKey(type);
    }

    public String getEncodedNameForMethod(String className, 
            String methodName, List<String> argTypes) {
        Ir3ClassBuilder cd = classToBuildersMap.get(className);
        return cd.getEncodedNameForMethod(methodName, argTypes);
    }

    public void cgBeginClass(String className) {
        currClsBuilder = classToBuildersMap.get(className);
    }

    public void cgEndClass() {
        currClsBuilder = null;
    }

    public void cgBeginMethod(String methodName, List<String> argTypes) 
            throws TypeCheckException {
        currMdBuilder = currClsBuilder.getMdBuilderForSignature(
                    methodName, argTypes);
    }

    public void cgEndMethod() {
        currMdBuilder = null;
    }

    public String cgNewLabelName() {
        return String.valueOf(++labelIdx);
    }

    public String cgNewTemporaryName() {
        return "_t" + String.valueOf(++tempVarIdx);
    }

    public void cgLocalDecl(String varName, String varType) {
        if (!isValidDeclType(varType)) {
            throw new VariableInvalidTypeException(currClsBuilder.getClassName(), 
                    currMdBuilder.getMethodName(), varName, varType);
        }
        currMdBuilder.addLocalDecl(varName, varType);
    }

    public void cgCode(Ir3Stmt stmt) {
        currMdBuilder.addCode(stmt);
    }

    public String toCode() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("=============== CData3 ===============\n\n");
        
        for (Ir3ClassBuilder cb : classToBuildersMap.values()) {
            sb.append("class ")
                .append(cb.getClassName())
                .append("{\n");
            for (Map.Entry<String, String> entry : cb.classFieldToTypeMap.entrySet()) {
                sb.append("    ")
                    .append(entry.getValue())
                    .append(" ")
                    .append(entry.getKey())
                    .append(";\n");
            }
            sb.append("}\n\n");
        }

        sb.append("=============== CMtd3 ================\n\n");

        for (Ir3ClassBuilder cb : classToBuildersMap.values()) {
            for (Map<String, Ir3MdBuilder> mbs : cb.methodToBuildersMap.values()) {
                for (Ir3MdBuilder mb : mbs.values()) {
                    sb.append(mb.toCode());
                }
            }
        }

        sb.append("========= End of IR3 Program =========\n\n");
        
        return sb.toString();
    }

    @Override
    public int getClassFieldOffset(String className, String fieldName) {
        return classToBuildersMap.get(className).getOffsetForField(fieldName);
    }

    public ArmProgram toArm() {

        // for (Ir3ClassBuilder cb : classToBuildersMap.values()) {
        //     for (Map<String, Ir3MdBuilder> mbs : cb.methodToBuildersMap.values()) {
        //         for (Ir3MdBuilder mb : mbs.values()) {
        //             mb.testOpt();
        //         }
        //     }
        // }
        return null;
    }

    private void addClass(ClassBundle classBundle) throws TypeCheckException {
        // No two classes can be declared in a program with the same name.
        if (classToBuildersMap.containsKey(classBundle.className)) {
            throw new ClassRedeclaredException(classBundle.className);
        }
        
        Ir3ClassBuilder cb = new Ir3ClassBuilder(classBundle);
        classToBuildersMap.put(cb.getClassName(), cb);
    }

    private void validate() throws TypeCheckException {
        for (Ir3ClassBuilder cb : classToBuildersMap.values()) {
            for (Map.Entry<String, String> entry : cb.classFieldToTypeMap.entrySet()) {
                if (!isValidDeclType(entry.getValue())) {
                    throw new ClassFieldInvalidTypeException(cb.getClassName(), 
                            entry.getKey(), entry.getValue());
                }
            }
            for (Map<String, Ir3MdBuilder> mdBuilders : cb.methodToBuildersMap.values()) {
                for (Ir3MdBuilder mdBuilder : mdBuilders.values()) {
                    validateMethod(mdBuilder);
                }
            }
        }
    }

    private void validateMethod(Ir3MdBuilder mdBuilder) throws TypeCheckException {
        if (!isValidType(mdBuilder.getReturnType())) {
            throw new ClassMethodInvalidReturnTypeException(mdBuilder.getClassName(), 
                    mdBuilder.getMethodKey(), mdBuilder.getReturnType());
        }

        for (Map.Entry<String, String> param : mdBuilder.getAllParams().entrySet()) {
            if (!isValidDeclType(param.getValue())) {
                throw new VariableInvalidTypeException(mdBuilder.getClassName(), 
                        mdBuilder.getMethodKey(), param.getKey(), param.getValue());
            }
        }

        for (Map.Entry<String, String> local : mdBuilder.getAllLocalDecls().entrySet()) {
            if (!isValidDeclType(local.getValue())) {
                throw new VariableInvalidTypeException(mdBuilder.getClassName(), 
                        mdBuilder.getMethodKey(), local.getKey(), local.getValue());
            }
        }
    }
}
