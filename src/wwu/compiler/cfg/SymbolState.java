package wwu.compiler.cfg;

public class SymbolState {
    String name;
    boolean isLive;

    public SymbolState(String name) {
        this.name = name;
        isLive = false;
    }

    void setLive(boolean isLive) {
        this.isLive = isLive;
    }
}
