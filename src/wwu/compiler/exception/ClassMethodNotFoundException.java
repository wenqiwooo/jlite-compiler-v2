package wwu.compiler.exception;

public class ClassMethodNotFoundException extends TypeCheckException {

    static final long serialVersionUID = 1835L;
    String className;
    String methodName;

    public ClassMethodNotFoundException(String className, String methodName) {
        super(String.format("Class %s method %s: no declaration that matches the argument types is found", 
                className, methodName));
        this.className = className;
        this.methodName = methodName;
    }

}