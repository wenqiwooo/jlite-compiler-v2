package wwu.compiler.exception;

public class VariableInvalidTypeException extends TypeCheckException {

    static final long serialVersionUID = 1840L;
    String className;
    String methodName;
    String varName;
    String varType;

    public VariableInvalidTypeException(String className, String methodName, String varName, String varType) {
        super(String.format("Class %s method %s: variable %s has invalid type %s", 
                className,        
                methodName, 
                varName, 
                varType));
        this.className = className;
        this.methodName = methodName;
        this.varName = varName;
        this.varType = varType;
    }

}