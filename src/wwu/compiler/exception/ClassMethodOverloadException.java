package wwu.compiler.exception;

public class ClassMethodOverloadException extends TypeCheckException {

    static final long serialVersionUID = 1838L;
    String className;
    String methodName;

    public ClassMethodOverloadException(String className, String methodName) {
        super(String.format(
                "Class %s: overloaded methods for %s for have different return types. Overloaded methods must return the same type.", 
                className, methodName));
                
        this.className = className;
        this.methodName = methodName;
    }
}