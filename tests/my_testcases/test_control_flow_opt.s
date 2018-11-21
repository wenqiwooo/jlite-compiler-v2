.data
D0:
.asciz "%i"

.text
.global main

main:
stmfd sp!,{fp,lr}
stmfd sp!,{v1,v2,v3,v4,v5}
mov fp,sp
stmfd sp!,{a2,a3,a4,ip}
mov a1,#0
bl malloc(PLT)
ldmfd sp!,{a2,a3,a4,ip}
stmfd sp!,{a1,a3,a4,ip}
mov a2,#1632
mov a3,#1
bl Func_TestClass_0(PLT)
mov a2,a1
ldmfd sp!,{a1,a3,a4,ip}
mov ip,#0
mov v5,#100
sub a3,ip,v5
stmfd sp!,{a2,a3,a4,ip}
mov a2,a3
mov a3,#0
bl Func_TestClass_0(PLT)
ldmfd sp!,{a2,a3,a4,ip}
add a1,a2,a1
stmfd sp!,{a1,a2,a3,a4,ip}
mov a2,a1
ldr a1,=D0
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
mov a1,#0

main_exit:
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}

Func_TestClass_0:
stmfd sp!,{fp,lr}
stmfd sp!,{v1,v2,v3,v4,v5}
mov fp,sp
mov a4,#20
mov v1,#0
mov v5,#0
cmp a3,v5
bne L2
mov ip,#0
sub v2,ip,a2
mov ip,#0
sub v2,ip,v2
mov ip,#0
sub v2,ip,v2
mov a1,v2
b Func_TestClass_0_exit
b L1

L2:
mov v5,#100
cmp a2,v5
blt L3
mov v5,#200
cmp a2,v5
beq L4
mov v1,#0

L6:
mov v5,#200
cmp a2,v5
movgt v2,#1
movle v2,#0
cmp v1,a4
movlt v3,#1
movge v3,#0
and v2,v3
mov v5,#0
cmp v2,v5
bne L7
b L5

L7:
sub a2,a2,#1
add v1,v1,#1
b L6

L5:
stmfd sp!,{a1,a2,a3,a4,ip}
bl Func_TestClass_0(PLT)
mov v2,a1
ldmfd sp!,{a1,a2,a3,a4,ip}
mov a1,v2
b Func_TestClass_0_exit
b L1

L4:
mov v2,#200
mov a1,v2
b Func_TestClass_0_exit
b L1

L3:
mov v1,#0

L9:
mov v5,#200
cmp a2,v5
movlt v3,#1
movge v3,#0
cmp v1,a4
movlt v2,#1
movge v2,#0
and v2,v3
mov v5,#0
cmp v2,v5
bne L10
b L8

L10:
add a2,a2,#1
add v1,v1,#1
b L9

L8:
stmfd sp!,{a1,a2,a3,a4,ip}
bl Func_TestClass_0(PLT)
mov v2,a1
ldmfd sp!,{a1,a2,a3,a4,ip}
mov a1,v2
b Func_TestClass_0_exit

L1:

Func_TestClass_0_exit:
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}


