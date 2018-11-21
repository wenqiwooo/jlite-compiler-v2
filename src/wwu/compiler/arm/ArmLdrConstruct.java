package wwu.compiler.arm;

public class ArmLdrConstruct extends ArmLoadable {
    Object val;

    public ArmLdrConstruct(String ref) {
        this.val = ref;
    }

    public ArmLdrConstruct(Integer val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "=" + val.toString();
    }
}