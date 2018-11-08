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
     * Gets the machine mode required for variable of type type.
     */
    public static ArmMode getArmModeForType(String type) {
        switch(type) {
            case Type.INT:
                return ArmMode.WORD;
            case Type.BOOL:
                return ArmMode.BYTE;
            case Type.STRING:
                return ArmMode.WORD;
            // Should not be allocating for Void and Null.
            case Type.VOID:
            case Type.NULL:
                return ArmMode.WORD;
            default:
                // User-defined class are all dynamically allocated,
                // so return size of mem addr
                return ArmMode.WORD;
        }
    }

    public static int getSizeForType(String type) {
        return getArmModeForType(type).getSize();
    }
}