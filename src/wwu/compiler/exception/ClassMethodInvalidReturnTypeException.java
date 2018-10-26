package wwu.compiler.exception;

public class ClassMethodInvalidReturnTypeException extends TypeCheckException {

    static final long serialVersionUID = 1842L;
    String className;
    String methodName;
    String returnType;

    public ClassMethodInvalidReturnTypeException(String className, String methodName, String returnType) {
        super(String.format("Class %s: method %s has invalid return type %s", 
                className, methodName, returnType));
        this.className = className;
        this.methodName = methodName;
        this.returnType = returnType;
    }

}