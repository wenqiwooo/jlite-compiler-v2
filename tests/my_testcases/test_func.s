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
stmfd sp!,{a1,a3,a4,ip}
mov a1,#0
bl malloc(PLT)
mov a2,a1
ldmfd sp!,{a1,a3,a4,ip}
stmfd sp!,{a2,a3,a4,ip}
mov a1,a2
mov a2,#7
bl Func_TestClass_0(PLT)
add sp,sp,#0
mov a1,a1
ldmfd sp!,{a2,a3,a4,ip}
stmfd sp!,{a1,a2,a4,ip}
mov a1,a2
mov a2,#9
bl Func_TestClass_0(PLT)
add sp,sp,#0
mov a3,a1
ldmfd sp!,{a1,a2,a4,ip}
add a1,a1,a3
stmfd sp!,{a1,a2,a4,ip}
mov a1,a2
mov a2,#11
bl Func_TestClass_0(PLT)
add sp,sp,#0
mov a3,a1
ldmfd sp!,{a1,a2,a4,ip}
add a1,a1,a3
stmfd sp!,{a1,a3,a4,ip}
mov a1,a2
mov a2,#12
bl Func_TestClass_0(PLT)
add sp,sp,#0
mov a2,a1
ldmfd sp!,{a1,a3,a4,ip}
add a1,a1,a2
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
bl Func_TestClass_0(PLT)
add sp,sp,#0
mov a4,a1
ldmfd sp!,{a1,a2,a3,ip}
sub a3,a2,#2
stmfd sp!,{a1,a2,a4,ip}
mov a2,a3
bl Func_TestClass_0(PLT)
add sp,sp,#0
mov a3,a1
ldmfd sp!,{a1,a2,a4,ip}
add a3,a4,a3
mov a1,a3
b Func_TestClass_0_exit
b L1

L2:
mov a1,a2
b Func_TestClass_0_exit

L1:

Func_TestClass_0_exit:
add sp,sp,#0
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}


