package wwu.compiler.ir3;

public class Ir3Identifier extends Ir3Id {
    String varName;

    public Ir3Identifier(String varName) {
        this.varName = varName;
    }

    @Override
    public String toString() {
        return varName;
    }
}