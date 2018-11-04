package wwu.compiler.ir3;

import java.util.*;

public class Ir3Call extends Ir3Expr {
    String methodName;
    List<Ir3Id> args;

    public Ir3Call(String methodName, List<Ir3Id> args) {
        this.methodName = methodName;
        this.args = args;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(methodName).append("(");
        for (int i = 0; i < args.size(); i++) {
            sb.append(args.get(i).toString());
            if (i < args.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")");

        return sb.toString();
    }

    @Override
    void addUseSymbols(Set<String> symbols) {
        for (Ir3Id arg : args) {
            arg.addUseSymbols(symbols);
        }
    }
}