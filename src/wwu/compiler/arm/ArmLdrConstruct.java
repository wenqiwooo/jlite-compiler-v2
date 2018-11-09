package wwu.compiler.arm;

public class ArmLdrConstruct extends ArmLoadable {
    String ref;

    public ArmLdrConstruct(String ref) {
        this.ref = ref;
    }
}