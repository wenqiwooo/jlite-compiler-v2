package wwu.compiler.ir3;

public interface ClassTypeProvider {
    // Returns field offset, relative to address of class object
    int getClassFieldOffset(String className, String fieldName);
}