package wwu.compiler.exception;

public class MethodParamRedeclaredException extends TypeCheckException {

    static final long serialVersionUID = 1845L;
    String className;
    String methodName;
    String paramName;

    public MethodParamRedeclaredException(String className, String methodName, String paramName) {
        super(String.format("Class %s method %s: parameter %s is already declared", 
                className, methodName, paramName));
        
        this.className = className;
        this.methodName = methodName;
        this.paramName = paramName;
    }

}