package wwu.compiler.exception;

public class ClassNotFoundException extends TypeCheckException {

    static final long serialVersionUID = 1839L;
    String className;

    public ClassNotFoundException(String className) {
        super(String.format("Class %s: declaration not found", className));
        this.className = className;
    }

}