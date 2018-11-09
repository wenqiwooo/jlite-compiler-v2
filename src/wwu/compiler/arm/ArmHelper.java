package wwu.compiler.arm;

import java.lang.*;

public class ArmHelper {
    public static boolean isValidDataImmediate(int x) {
        for (int j = 0; j < 32; j += 2) {
            int y = (255 >> j | 255 << 32 - j) ^ -1;
            if ((x & y) == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidDataImmediate(Object input) {
        if (!(input instanceof Integer)) {
            return false;
        }
        int x = (int)input;
        return isValidDataImmediate(x);
    }
}