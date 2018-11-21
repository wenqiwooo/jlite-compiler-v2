.data
D0:
.asciz "%i"

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
mov ip,#0
ldr v5,=301
sub a2,ip,v5
stmfd sp!,{a2,a3,a4,ip}
mov a3,#13
bl Func_TestClass_0(PLT)
add sp,sp,#0
mov a1,a1
ldmfd sp!,{a2,a3,a4,ip}
stmfd sp!,{a1,a2,a3,a4,ip}
mov a2,a1
ldr a1,=D0
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
mov a1,#0

main_exit:
add sp,sp,#0
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}

Func_TestClass_0:
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
add sp,sp,#0
ldmfd sp!,{a1,a2,a3,a4,ip}
mov v1,#0

L2:
cmp v1,a3
blt L3
b L1

L3:
stmfd sp!,{a1,a2,a3,ip}
mov a1,a4
bl Func_Node_1(PLT)
add sp,sp,#0
mov a4,a1
ldmfd sp!,{a1,a2,a3,ip}
add v1,v1,#1
b L2

L1:
mov a3,#0

L5:
mov v5,#0
cmp v1,v5
bgt L6
b L4

L6:
ldr a1,[a4,#4]
add a3,a3,a1
ldr a4,[a4,#0]
sub v1,v1,#1
b L5

L4:
mov a1,a3
b Func_TestClass_0_exit

Func_TestClass_0_exit:
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
add sp,sp,#0
ldmfd sp!,{a1,a2,a3,a4,ip}
mov ip,a1
str ip,[a3,#0]
mov a1,a3
b Func_Node_1_exit

Func_Node_1_exit:
add sp,sp,#0
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}


