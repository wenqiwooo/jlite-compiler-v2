.data
D0:
.asciz "Answer of Fib(11) is: "
D1:
.asciz "%i"
D2:
.asciz "\n"
D3:
.asciz "Answer of 100*100 is: "

.text
.global main

main:
stmfd sp!,{fp,lr}
stmfd sp!,{v1,v2,v3,v4,v5}
mov fp,sp
sub sp,sp,#0
stmfd sp!,{a2,a3,a4,ip}
mov a1,#0
bl malloc(PLT)
mov a1,a1
ldmfd sp!,{a2,a3,a4,ip}
stmfd sp!,{a1,a2,a3,a4,ip}
ldr a1,=D0
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
stmfd sp!,{a2,a3,a4,ip}
mov a2,#11
bl Func_Math_0(PLT)
sub sp,sp,#0
mov a1,a1
ldmfd sp!,{a2,a3,a4,ip}
stmfd sp!,{a1,a2,a3,a4,ip}
mov a2,a1
ldr a1,=D1
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
stmfd sp!,{a1,a2,a3,a4,ip}
ldr a1,=D2
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
stmfd sp!,{a1,a2,a3,a4,ip}
ldr a1,=D3
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
stmfd sp!,{a2,a3,a4,ip}
mov a2,#100
mov a3,#100
bl Func_Math_1(PLT)
sub sp,sp,#0
mov a1,a1
ldmfd sp!,{a2,a3,a4,ip}
stmfd sp!,{a1,a2,a3,a4,ip}
mov a2,a1
ldr a1,=D1
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
stmfd sp!,{a1,a2,a3,a4,ip}
ldr a1,=D2
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
mov a1,#0

main_exit:
add sp,sp,#0
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}

Func_Math_0:
stmfd sp!,{fp,lr}
stmfd sp!,{v1,v2,v3,v4,v5}
mov fp,sp
sub sp,sp,#0
mov v5,#0
cmp a2,v5
moveq a3,#1
movne a3,#0
mov v5,#1
cmp a2,v5
moveq a4,#1
movne a4,#0
orr a3,a4
mov v5,#0
cmp a3,v5
bne L2
sub a3,a2,#1
stmfd sp!,{a1,a2,a3,ip}
mov a2,a3
bl Func_Math_0(PLT)
sub sp,sp,#0
mov a4,a1
ldmfd sp!,{a1,a2,a3,ip}
sub a3,a2,#2
stmfd sp!,{a1,a2,a4,ip}
mov a2,a3
bl Func_Math_0(PLT)
sub sp,sp,#0
mov a3,a1
ldmfd sp!,{a1,a2,a4,ip}
add a3,a4,a3
mov a1,a3
b Func_Math_0_exit
b L1

L2:
mov a1,a2
b Func_Math_0_exit

L1:

Func_Math_0_exit:
add sp,sp,#0
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}

Func_Math_1:
stmfd sp!,{fp,lr}
stmfd sp!,{v1,v2,v3,v4,v5}
mov fp,sp
sub sp,sp,#0
stmfd sp!,{a1,a2,a3,ip}
mov a1,#8
bl malloc(PLT)
mov a4,a1
ldmfd sp!,{a1,a2,a3,ip}
mov ip,#0
ldr v5,=9999
sub a1,ip,v5
stmfd sp!,{a1,a2,a3,a4,ip}
mov a2,a1
mov a1,a4
bl Func_Node_0(PLT)
sub sp,sp,#0
ldmfd sp!,{a1,a2,a3,a4,ip}
mov v1,#0

L4:
cmp v1,a3
blt L5
b L3

L5:
stmfd sp!,{a1,a2,a3,ip}
mov a1,a4
bl Func_Node_1(PLT)
sub sp,sp,#0
mov a4,a1
ldmfd sp!,{a1,a2,a3,ip}
add v1,v1,#1
b L4

L3:
mov a3,#0

L7:
mov v5,#0
cmp v1,v5
bgt L8
b L6

L8:
ldr a1,[a4,#4]
add a3,a3,a1
ldr a4,[a4,#0]
sub v1,v1,#1
b L7

L6:
mov a1,a3
b Func_Math_1_exit

Func_Math_1_exit:
add sp,sp,#0
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}

Func_Node_0:
stmfd sp!,{fp,lr}
stmfd sp!,{v1,v2,v3,v4,v5}
mov fp,sp
sub sp,sp,#0
mov ip,a2
str ip,[a1,#4]

Func_Node_0_exit:
add sp,sp,#0
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}

Func_Node_1:
stmfd sp!,{fp,lr}
stmfd sp!,{v1,v2,v3,v4,v5}
mov fp,sp
sub sp,sp,#0
stmfd sp!,{a1,a2,a4,ip}
mov a1,#8
bl malloc(PLT)
mov a3,a1
ldmfd sp!,{a1,a2,a4,ip}
stmfd sp!,{a1,a2,a3,a4,ip}
mov a1,a3
bl Func_Node_0(PLT)
sub sp,sp,#0
ldmfd sp!,{a1,a2,a3,a4,ip}
mov ip,a1
str ip,[a3,#0]
mov a1,a3
b Func_Node_1_exit

Func_Node_1_exit:
add sp,sp,#0
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}


