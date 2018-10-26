package wwu.compiler.exception;

public class ClassRedeclaredException extends TypeCheckException {

    static final long serialVersionUID = 1843L;
    String className;

    public ClassRedeclaredException(String className) {
        super(String.format("Class %s is already declared", className));
        this.className = className;
    }

}