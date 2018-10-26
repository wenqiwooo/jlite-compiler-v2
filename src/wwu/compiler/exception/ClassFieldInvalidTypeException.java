package wwu.compiler.exception;

public class ClassFieldInvalidTypeException extends TypeCheckException {

    static final long serialVersionUID = 1841L;
    String className;
    String fieldName;
    String fieldType;

    public ClassFieldInvalidTypeException(String className, String fieldName, String fieldType) {
        super(String.format("Class %s: field %s has invalid type %s", 
                className, fieldName, fieldType));
        this.className = className;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

}