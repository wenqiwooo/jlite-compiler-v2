package wwu.compiler.exception;

public class TypeCheckException extends RuntimeException {

    static final long serialVersionUID = 1833L;

    public TypeCheckException(String msg) {
        super(msg);
    }
}