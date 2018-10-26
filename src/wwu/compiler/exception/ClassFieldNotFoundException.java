package wwu.compiler.exception;

public class ClassFieldNotFoundException extends TypeCheckException {

    static final long serialVersionUID = 1834L;
    String className;
    String fieldName;

    public ClassFieldNotFoundException(String className, String fieldName) {
        super(String.format("Class %s: declaration of field %s not found", className, fieldName));
        this.className = className;
        this.fieldName = fieldName;
    }

}