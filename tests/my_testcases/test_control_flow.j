// Test control flow

class MainClass {
    Void main() {
        TestClass tc;
        Int res;

        tc = new TestClass();

        res = tc.loopy(1632, true) + tc.loopy(-100, false);  // 200 + 100 = 300
        println(res); // Expect 300 to be printed
    }
}

class TestClass {
    // This method looks confusing
    // but all it does is return 200 if z is true
    // else it returns -x

    Int loopy(Int x, Bool z) {
        Int limit;
        Int c;

        limit = 20;
        c = 0;

        if (z) {
            if (x < 100) {
                c = 0;
                while (x < 200 && c < limit) {
                    x = x + 1;
                    c = c + 1;
                }
                return loopy(x, z);
            }
            else {
                if (x == 200) {
                    return 200;
                }
                else {
                    c = 0;
                    while (x > 200 && c < limit) {
                        x = x - 1;
                        c = c + 1;
                    }
                    return loopy(x, z);
                }
            }
        } else {
            return ---x;
        }
    }
}