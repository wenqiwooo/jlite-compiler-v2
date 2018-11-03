package wwu.compiler.common;

public class Type {
    public static final String INT = "Int";
    public static final String BOOL = "Bool";
    public static final String STRING = "String";
    public static final String VOID = "Void";
    public static final String NULL = "Null";

    public static final int BYTE_SIZE = 1;
    public static final int WORD_SIZE = 4;

    public static final int INT_SIZE = WORD_SIZE;
    public static final int BOOL_SIZE = BYTE_SIZE;
    public static final int STRING_SIZE = WORD_SIZE;
    public static final int VOID_SIZE = 0;
}