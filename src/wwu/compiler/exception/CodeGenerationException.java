package wwu.compiler.exception;

public class CodeGenerationException extends RuntimeException {

    static final long serialVersionUID = 2033L;

    public CodeGenerationException(String msg) {
        super(msg);
    }
}