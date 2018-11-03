package wwu.compiler.common;

public class TypeHelper {
    public static boolean isASuperclassOfB(String A, String B) {
        if (A.equals(Type.INT) || 
                A.equals(Type.BOOL) || 
                A.equals(Type.VOID) || 
                A.equals(Type.NULL)) {
            return A.equals(B);
        }
        return A.equals(B) || B.equals(Type.NULL);
    }

    public static boolean isNonVoidBuiltInType(String type) {
        return type.equals(Type.INT) || 
                type.equals(Type.BOOL) ||
                type.equals(Type.STRING);
    }

    public static boolean isBuiltInType(String type) {
        return isNonVoidBuiltInType(type) ||
                type.equals(Type.VOID);
    }

    /**
     * Gets the size in bytes required for variable of type type.
     */
    public static int getVarSizeForType(String type) {
        switch(type) {
            case Type.INT:
                return Type.INT_SIZE;
            case Type.BOOL:
                return Type.BOOL_SIZE;
            case Type.STRING:
                return Type.STRING_SIZE;
            case Type.VOID:
                return Type.VOID_SIZE;
            case Type.NULL:
                // Should throw here.
                return 0;
            default:
                // User-defined class are all dynamically allocated,
                // so return size of mem addr
                return Type.WORD_SIZE;
        }
    }
}