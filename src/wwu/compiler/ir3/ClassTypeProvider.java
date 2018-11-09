package wwu.compiler.ir3;

public interface ClassTypeProvider {
    // Returns field offset, relative to address of class object
    int getClassFieldOffset(String className, String fieldName);

    int getClassSize(String className);

    String addGlobalLiteral(String value);

    String addGlobalLiteral(int value);
}