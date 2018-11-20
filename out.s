.data
D0:
.asciz "Answer of Fib(11): "
D1:
.asciz "%i"
D2:
.asciz "\n"
D3:
.asciz "Answer of 100*100: "
D4:
.asciz "Loopy answer for x=400, z=True: "
D5:
.asciz "Loopy answer for x=400, z=False: "
D6:
.asciz "New loopy with x="
D7:
.asciz " z="
D8:
.asciz "false"
D9:
.asciz "true"

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
stmfd sp!,{a1,a2,a3,a4,ip}
ldr a1,=D4
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
stmfd sp!,{a2,a3,a4,ip}
mov a2,#400
mov a3,#1
bl Func_Math_2(PLT)
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
ldr a1,=D5
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
stmfd sp!,{a2,a3,a4,ip}
mov a2,#400
mov a3,#0
bl Func_Math_2(PLT)
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
stmfd sp!,{a1,a2,a4,ip}
mov a2,a3
bl Func_Math_0(PLT)
sub sp,sp,#0
mov a3,a1
ldmfd sp!,{a1,a2,a4,ip}
sub a2,a2,#2
stmfd sp!,{a1,a3,a4,ip}
bl Func_Math_0(PLT)
sub sp,sp,#0
mov a2,a1
ldmfd sp!,{a1,a3,a4,ip}
add a2,a3,a2
mov a1,a2
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
mov a1,#0

L7:
mov v5,#0
cmp v1,v5
bgt L8
b L6

L8:
ldr a3,[a4,#4]
add a1,a1,a3
ldr a4,[a4,#0]
sub v1,v1,#1
b L7

L6:
b Func_Math_1_exit

Func_Math_1_exit:
add sp,sp,#0
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}

Func_Math_2:
stmfd sp!,{fp,lr}
stmfd sp!,{v1,v2,v3,v4,v5}
mov fp,sp
sub sp,sp,#0
mov a4,#20
stmfd sp!,{a1,a2,a3,a4,ip}
ldr a1,=D6
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
stmfd sp!,{a1,a2,a3,a4,ip}
ldr a1,=D1
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
stmfd sp!,{a1,a2,a3,a4,ip}
ldr a1,=D7
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
mov v5,#0
cmp a3,v5
bne L10
stmfd sp!,{a1,a2,a3,a4,ip}
ldr a1,=D8
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
b L9

L10:
stmfd sp!,{a1,a2,a3,a4,ip}
ldr a1,=D9
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}

L9:
stmfd sp!,{a1,a2,a3,a4,ip}
ldr a1,=D2
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
mov v5,#0
cmp a3,v5
bne L12
mov ip,#0
sub v2,ip,a2
mov ip,#0
sub v2,ip,v2
mov ip,#0
sub v2,ip,v2
mov a1,v2
b Func_Math_2_exit
b L11

L12:
mov v5,#100
cmp a2,v5
blt L13
mov v5,#200
cmp a2,v5
beq L14
mov v1,#0

L16:
mov v5,#200
cmp a2,v5
movgt v3,#1
movle v3,#0
cmp v1,a4
movlt v2,#1
movge v2,#0
and v2,v3
mov v5,#0
cmp v2,v5
bne L17
b L15

L17:
sub a2,a2,#1
add v1,v1,#1
b L16

L15:
stmfd sp!,{a1,a2,a3,a4,ip}
bl Func_Math_2(PLT)
sub sp,sp,#0
mov v2,a1
ldmfd sp!,{a1,a2,a3,a4,ip}
mov a1,v2
b Func_Math_2_exit
b L11

L14:
mov v2,#200
mov a1,v2
b Func_Math_2_exit
b L11

L13:
mov v1,#0

L19:
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
bne L20
b L18

L20:
add a2,a2,#1
add v1,v1,#1
b L19

L18:
stmfd sp!,{a1,a2,a3,a4,ip}
bl Func_Math_2(PLT)
sub sp,sp,#0
mov v2,a1
ldmfd sp!,{a1,a2,a3,a4,ip}
mov a1,v2
b Func_Math_2_exit

L11:

Func_Math_2_exit:
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


