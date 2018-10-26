package wwu.compiler.ir3;

public class Ir3Label extends Ir3Stmt {
    String label;
    
    public Ir3Label(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Label " + label + ":";
    }
}
