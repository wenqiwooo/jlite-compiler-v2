package wwu.compiler.common;

public enum ArmMode {
    BYTE(1),
    HWORD(2),
    WORD(4);

    private int size;

    private ArmMode(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
