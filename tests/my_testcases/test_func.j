//
// Test procedure calls
//

class MainClass {
    Void main() {
        TestClass tc;
        Int res;

        tc = new TestClass();
        res = tc.fib(7) + tc.fib(9) + tc.fib(11) + tc.fib(12); // 13 + 34 + 89 + 144 = 280

        println(res); //    Expect 280 to be printed
    }
}

class TestClass {
    Int fib(Int x) {
        if (x == 0 || x == 1) {
            return x;
        } else {
            return fib(x-1) + fib(x-2);
        }
    }
}