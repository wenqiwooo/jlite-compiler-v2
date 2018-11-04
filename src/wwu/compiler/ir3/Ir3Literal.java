package wwu.compiler.ir3;

import java.util.HashSet;

import wwu.compiler.common.*;

public class Ir3Literal extends Ir3Id {
    Object value;
    String valueType;

    public Ir3Literal(Object value, String valueType) {
        this.value = value;
        this.valueType = valueType;
    }

    @Override
    public String toString() {
        if (valueType.equals(Type.STRING)) {
            return "\"" + (String)value +  "\"";
        }
        return String.valueOf(value);
    }
}