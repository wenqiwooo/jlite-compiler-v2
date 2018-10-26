package wwu.compiler.exception;

public class ClassMethodRedeclaredException extends TypeCheckException {

    static final long serialVersionUID = 1837L;
    String className;
    String methodName;

    public ClassMethodRedeclaredException(String className, String methodName) {
        super(String.format(
                "Class %s: method %s is already declared. Overloaded methods must have different signatures.", 
                className, methodName));
        this.className = className;
        this.methodName = methodName;
    }

}