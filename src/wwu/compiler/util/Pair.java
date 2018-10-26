package wwu.compiler.util;

public class Pair<U, T> {
    private U first;
    private T second;

    public Pair(U first, T second) {
        this.first = first;
        this.second = second;
    }

    public U first() {
        return first;
    }

    public T second() {
        return second;
    }
}
