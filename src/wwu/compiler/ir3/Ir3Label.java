package wwu.compiler.ir3;

import wwu.compiler.arm.*;
import wwu.compiler.exception.CodeGenerationException;
import wwu.compiler.ir3.Ir3MdBuilder.ArmMdBuilder;

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
    void buildArm(ArmMdBuilder mdBuilder, ClassTypeProvider classTypeProvider) 
            throws CodeGenerationException {
        mdBuilder.addInsn(new ArmLabel(label));
    }
}
