package wwu.compiler.common;

public enum Op {
    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/"),
    LT("<"), 
    GT(">"),
    LEQ("<="),
    GEQ(">="),
    EQ("=="),
    NEQ("!="),
    AND("&&"),
    OR("||"),
    NOT("!");

    private final String text;

    Op(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public boolean isArithmetic() {
        return this == ADD || this == SUB || this == MUL || this == DIV;
    }

    public boolean isRelational() {
        return this == LT || this == GT || this == LEQ || this == GEQ || this == EQ || this == NEQ;
    }

    public boolean isBoolean() {
        return this == AND || this == OR;
    }
}