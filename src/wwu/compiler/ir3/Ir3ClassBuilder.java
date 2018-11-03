package wwu.compiler.ir3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import wwu.compiler.common.*;
import wwu.compiler.exception.*;
import wwu.compiler.ir3.Ir3MdBuilder;
import wwu.compiler.util.Pair;

public class Ir3ClassBuilder {
    public Map<String, String> classFieldToTypeMap;
    // Mapping of method name to map of overloaded methods
    public Map<String, Map<String, Ir3MdBuilder>> methodToBuildersMap;

    private Map<String, String> methodToReturnTypeMap;
    private String className;
    // Byte offset of class fields
    private Map<String, Integer> classFieldToOffsetMap;
    // Size of class object in bytes
    private int sizeBytes = 0;
    private int nextMethodEncodeId = 0;

    public Ir3ClassBuilder(ClassBundle classBundle) throws TypeCheckException {
        classFieldToTypeMap = new LinkedHashMap<>();
        methodToBuildersMap = new LinkedHashMap<>();
        methodToReturnTypeMap = new HashMap<>();
        className = classBundle.className;

        classFieldToOffsetMap = new LinkedHashMap<>();

        for (Pair<String, String> field : classBundle.classFields) {
            addField(field);
        }

        for (MethodBundle method : classBundle.classMethods) {
            addMethod(method);
        }
    }

    public String getClassName() {
        return className;
    }

    public int getSizeBytes() {
        return sizeBytes;
    }

    public String getTypeForField(String fieldName) throws TypeCheckException {
        String fieldType = classFieldToTypeMap.getOrDefault(fieldName, null);
        if (fieldType == null) {
            throw new ClassFieldNotFoundException(className, fieldName);
        }
        return fieldType;
    }

    public String getReturnTypeForMethod(String methodName) throws TypeCheckException {
        String returnType = methodToReturnTypeMap.getOrDefault(methodName, null);
        if (returnType == null) {
            throw new ClassMethodNotFoundException(className, methodName);
        }
        return returnType;
    }

    public Ir3MdBuilder getMdBuilderForSignature(String methodName, List<String> argTypes) {
        Map<String, Ir3MdBuilder> methods = methodToBuildersMap.getOrDefault(methodName, null);
        if (methods == null) {
            return null;
        }
        String key = getMethodKey(methodName, argTypes);
        return methods.getOrDefault(key, null);
    }

    public String getEncodedNameForMethod(String methodName, List<String> argTypes) 
            throws TypeCheckException {
        Map<String, Ir3MdBuilder> methods = methodToBuildersMap.getOrDefault(methodName, null);
        if (methods == null) {
            throw new ClassMethodNotFoundException(className, methodName);
        }

        /**
         * We cannot simply calculate the key of a method we want to call, 
         * since there can be null types. 
         * 
         * If a null argument is found, we compare the argument types directly 
         * with the method's parameter types.
         */
        boolean containsNullArgs = false;
        for (String type : argTypes) {
            if (type.equals(Type.NULL)) {
                containsNullArgs = true;
                break;
            }
        }
        
        Ir3MdBuilder mdBuilder = null;
        
        if (!containsNullArgs) {
            String key = getMethodKey(methodName, argTypes);
            mdBuilder = methods.getOrDefault(key, null);
        } else {
            for (Map.Entry<String, Ir3MdBuilder> entry : methods.entrySet()) {
                Ir3MdBuilder mb = entry.getValue();
                List<String> paramTypes = mb.getParamTypes();
                
                if (paramTypes.size() != argTypes.size()) {
                    continue;
                }

                for (int i = 0; i < paramTypes.size(); i++) {
                    if (!TypeHelper.isASuperclassOfB(
                                paramTypes.get(i), argTypes.get(i))) {
                        break;
                    }
                    if (i == paramTypes.size() - 1) {
                        mdBuilder = methods.get(entry.getKey());
                    }
                }

                if (mdBuilder != null) {
                    break;
                }
            }
        }
        
        if (mdBuilder == null) {
            throw new ClassMethodNotFoundException(className, methodName);
        }

        return mdBuilder.getMethodEncodedName();
    }

    private void addField(Pair<String, String> field) throws TypeCheckException {
        // No two fields in a class can have the same name.
        if (classFieldToTypeMap.containsKey(field.first())) {
            throw new ClassFieldRedeclaredException(className, field.first());
        }
        classFieldToTypeMap.put(field.first(), field.second());
        classFieldToOffsetMap.put(field.first(), sizeBytes);
        sizeBytes += TypeHelper.getArmModeForType(field.second()).getSize();
    }

    /**
     * 
     * No two methods within a class declaration can have the same name and method signature. 
     * 
     * Overloading of methods is supported. Overloaded methods must have the same name, same 
     * return type and unique tuple of parameter types (different from all other methods which 
     * it overloads).
     * 
     * NOTE ON IMPLEMENTATION OF METHOD OVERLOADING:
     * 
     * Every method is assigned a unique key when adding it to the ClassBuilder, using its 
     * simple name and its parameter types. For example the method Void run(Bool x) is assigned 
     * the key func_run_Bool.
     * 
     * Whenever a new method is added, its key is generated and checked to ensure there is no 
     * redeclaration. As long as methods with same name and return type have different argument 
     * lists (which is required for overloading), every valid method has a unique key, i.e. 
     * one-to-one mapping.
     * 
     * If a method overloads existing methods, we need to ensure that the method's return type 
     * is the same as that of the methods which it overloads.
     * 
     * Whenever a method is called and no null arguments is passed, we can use the method's 
     * simple name and its argument types to generate the same key to retrieve the 
     * corresponding method from the method table.
     * 
     * However, if there is a null object in the argument list, it may be ambiguious which 
     * method to call if the argument list is valid for multiple methods, since null is a subtype 
     * of String and all user-defined types. For example given the following two overloaded 
     * methods:
     * 
     * Void doWork(Dog x);
     * 
     * Void doWork(Person x);
     * 
     * doWork(null) matches both methods' signatures. In this scenario, the compiler will assign
     * the method that is declared first in the source file.
     * 
     */
    private void addMethod(MethodBundle methodBundle) throws TypeCheckException {
        Map<String, Ir3MdBuilder> methods = null;
        
        String returnType = methodToReturnTypeMap.getOrDefault(methodBundle.methodName, null);
        if (returnType != null) {
            if (!returnType.equals(methodBundle.returnType)) {
                throw new ClassMethodOverloadException(className, methodBundle.methodName);
            }
            methods = methodToBuildersMap.get(methodBundle.methodName);
        } else {
            methodToReturnTypeMap.put(methodBundle.methodName, methodBundle.returnType);
            methods = new LinkedHashMap<>();
            methodToBuildersMap.put(methodBundle.methodName, methods);
        }

        List<String> paramTypes = new ArrayList<>();
        for (Pair<String, String> param : methodBundle.paramList) {
            paramTypes.add(param.second());
        }
        String key = getMethodKey(methodBundle.methodName, paramTypes);

        if (methods.containsKey(key)) {
            throw new ClassMethodRedeclaredException(className, key);
        }

        String encodedName = methodBundle.methodName.equals("main") 
                ? "main" 
                : generateMethodEncodedName();

        Ir3MdBuilder mdBuilder = new Ir3MdBuilder(className, 
                                    methodBundle.methodName, 
                                    encodedName,
                                    key, 
                                    methodBundle.returnType, 
                                    methodBundle.paramList, 
                                    methodBundle.localList);
        methods.put(key, mdBuilder);
    }

    private String generateMethodEncodedName() {
        return String.format("%%%s_%s", className, nextMethodEncodeId++);
    }

    private static String getMethodKey(String methodName, List<String> paramTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append("func_").append(methodName);
        if (paramTypes.isEmpty()) {
            sb.append("_").append(Type.VOID);
        } else {
            for (String paramType : paramTypes) {
                sb.append("_").append(paramType);
            }
        }
        return sb.toString();
    }
}