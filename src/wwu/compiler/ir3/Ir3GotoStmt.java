package wwu.compiler.ir3;

public class Ir3GotoStmt extends Ir3Stmt {
    String label;

    public Ir3GotoStmt(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "goto " + label + ";";
    }
}