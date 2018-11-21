// Test function

class MainClass {
    Void main() {
        TestClass tc;
        Int res;        
        tc = new TestClass();

        res = tc.addMulMul(1,1,1,1,3,2,4,2,100); // (3 + 2) * 4 * 2 = 40
        println(res); // Expect 40 to be printed
    }
}

class TestClass {
    Int addMulMul(Int v1, Int v2, Int v3, Int v4, Int v5, Int v6, Int v7, Int v8, Int v9) {
        Int v1;
        Int v2;
        Int v3;
        Int v4;
        
        v1 = v5;
        v2 = v6;
        v3 = v7;
        v4 = v8;

        return --(--v1 + v2) * ------------------v3 * --v4;
    }
}