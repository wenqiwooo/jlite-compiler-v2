package wwu.compiler.ir3;

public class Ir3Constructor extends Ir3Expr {
    String type;

    public Ir3Constructor(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("new %s()", type);
    }
}