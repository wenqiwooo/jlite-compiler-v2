package wwu.compiler.ir3;

public class Ir3PrintlnStmt extends Ir3Stmt {
    Ir3Id arg;
    
    public Ir3PrintlnStmt(Ir3Id arg) {
        this.arg = arg;
    }

    @Override
    public String toString() {
        return "println(" + arg.toString() + ");";
    }
}
