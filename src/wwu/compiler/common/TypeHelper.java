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
}