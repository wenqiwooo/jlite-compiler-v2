package wwu.compiler.exception;

public class ClassFieldRedeclaredException extends TypeCheckException {

    static final long serialVersionUID = 1836L;
    String className;
    String fieldName;

    public ClassFieldRedeclaredException(String className, String fieldName) {
        super(String.format("Class %s: field %s is already declared", className, fieldName));
        this.className = className;
        this.fieldName = fieldName;
    }

}