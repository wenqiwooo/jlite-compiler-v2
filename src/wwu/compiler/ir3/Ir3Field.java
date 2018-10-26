package wwu.compiler.ir3;

public class Ir3Field extends Ir3Id {
    Ir3Id parent;
    String fieldName;

    public Ir3Field(Ir3Id parent, String fieldName) {
        this.parent = parent;
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return parent.toString() + "." + fieldName;
    }
}