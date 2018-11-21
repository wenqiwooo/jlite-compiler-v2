// Test objects

class MainClass {
    Void main() {
        TestClass tc;
        Int res;

        tc = new TestClass();

        res = tc.times(-301, 13); // -301 * 13 = -3913

        println(res); // Expect -3913 to be printed
    }
}

class TestClass {
    // y must be positive
    Int times(Int x, Int y) {
        Node ll;
        Int z;
        Int sum;

        ll = new Node();
        ll.setVal(-9999);
        
        z = 0;
        while (z < y) {
            ll = ll.push(x);
            z = z + 1;
        }

        sum = 0;
        while (z > 0) {
            sum = sum + ll.val;
            ll = ll.next;
            z = z - 1;
        }

        return sum;
    }
}

class Node {
    Node next;
    Int val;

    Void setVal(Int x) {
        this.val = x;
    }

    
    Node push(Int x) {
        Node nd;
        nd = new Node();
        nd.setVal(x);
        nd.next = this;
        return nd;
    }
}