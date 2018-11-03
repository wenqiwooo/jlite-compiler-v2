package wwu.compiler.ir3;

import wwu.compiler.arm.*;

public class Ir3Label extends Ir3Stmt {
    String label;
    
    public Ir3Label(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Label " + label + ":";
    }

    @Override
    public void addToArmMd(ArmMd armMd) {

    }
}
