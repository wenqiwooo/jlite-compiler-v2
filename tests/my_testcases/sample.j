class Main {
    Void main(){
        Int a;
        Int b;
        Int i;
        Int d;
        Int t1;
        Int t2;
        Compute help;
        a = 1;
        b = 2;
        i = 3;
        d = 4;
        help = new Compute();
        t1 = help.addSquares(a,b) + help.square(i);
        t2 = help.square(d); // Should be equal to 16
        if(t2>t1){
            println("Square of d larger than sum of squares");
            // Should be the output
        } else {
            println("Square of d smaller than sum of squares");
        }
    }
}

class Compute {
    Bool computedSquares;
    Int cachedValue;
   
    Int square(Int a) {
        return a*a;
    }

    Int add(Int a, Int b) {
        return a+b; 
    }
    
    Int addSquares(Int a, Int b){
        if (computedSquares){
            return cachedValue;
        } else {
            computedSquares = true;
            return add(square(a),square(b));
        } 
    }
}

/*
Method: main
Assignments: {help=3, a=4, b=0, d=2, this=0, i=1, _t2=1, _t1=0, t1=1, t2=0}
0 = a1
1 = a3
2 = a4
3 = v1
4 = a2

Spills: []
Code: 
Void main(Main this){
    Int a;
    Int b;
    Int i;
    Int d;
    Int t1;
    Int t2;
    Compute help;
    Int _t1;
    Int _t2;
    a = 1;
    b = 2;
    i = 3;
    d = 4;
    help = new Compute();
    _t1 = Func_Compute_2(help,a,b);
    _t2 = Func_Compute_0(help,i);
    t1 = _t1 + _t2;
    t2 = Func_Compute_0(help,d);
    if (t2 > t1) goto L2;
    println("Square of d smaller than sum of squares");
    goto L1;
  Label L2:
    println("Square of d larger than sum of squares");
  Label L1:
}




Method: square
Assignments: {a=0, this=0, _t3=0}
a2 = a2 * a2
Spills: []
Code: 
Int Func_Compute_0(Compute this,Int a){
    Int _t3;
    _t3 = a * a;
    return _t3;
}




Method: add
Assignments: {a=1, b=0, this=0, _t4=0}
Spills: []
Code: 
Int Func_Compute_1(Compute this,Int a,Int b){
    Int _t4;
    _t4 = a + b;
    return _t4;
}




Method: addSquares
Assignments: {_t6=1, _t5=2, a=2, _t8=0, b=1, _t7=0, this=0}
0 = a1
1 = a3
2 = a2


Spills: []
Code: 
Int Func_Compute_2(Compute this,Int a,Int b){
    Int _t5;
    Int _t6;
    Int _t7;
    Int _t8;
    if (this.computedSquares) goto L4;
    this.computedSquares = true;
    _t5 = Func_Compute_0(this,a);
    _t6 = Func_Compute_0(this,b);
    _t7 = Func_Compute_1(this,_t5,_t6);
    return _t7;
    goto L3;
  Label L4:
    _t8 = this.cachedValue;
    return _t8;
  Label L3:
}




Method: test
Assignments: {a=0, b=1, c=0, d=0, _t9=0, e=0, this=0}
Spills: []
Code: 
Int Func_Compute_3(Compute this,Int a,Int b,Int c,Int d,Int e){
    Int _t9;
    _t9 = b + d;
    return _t9;
}



Method: addSquares
Assignments: {_t6=1, _t5=2, a=2, _t8=0, b=1, _t7=0, this=0}
Spills: []
Code: 
Int Func_Compute_2(Compute this,Int a,Int b){
    Int _t5;
    Int _t6;
    Int _t7;
    Int _t8;
    if (this.computedSquares) goto L4;
    this.computedSquares = true;
    _t5 = Func_Compute_0(this,a);
    _t6 = Func_Compute_0(this,b);
    _t7 = Func_Compute_1(this,_t5,_t6);
    return _t7;
    goto L3;
  Label L4:
    _t8 = this.cachedValue;
    return _t8;
  Label L3:
}




Method: test
Assignments: {a=0, b=1, c=0, d=0, _t9=0, e=0, this=0}
Spills: []
Code: 
Int Func_Compute_3(Compute this,Int a,Int b,Int c,Int d,Int e){
    Int _t9;
    _t9 = b + d;
    return _t9;
}
*/