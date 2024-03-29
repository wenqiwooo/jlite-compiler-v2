.data
D0:
.asciz "Square of d smaller than sum of squares"
D1:
.asciz "Square of d larger than sum of squares"

.text
.global main

main:
stmfd sp!,{fp,lr}
stmfd sp!,{v1,v2,v3,v4,v5}
mov fp,sp
mov a2,#1
mov a1,#2
mov a3,#3
mov a4,#4
stmfd sp!,{a1,a2,a3,a4,ip}
mov a1,#8
bl malloc(PLT)
mov v1,a1
ldmfd sp!,{a1,a2,a3,a4,ip}
stmfd sp!,{a2,a3,a4,ip}
mov a3,a1
mov a1,v1
bl Func_Compute_2(PLT)
ldmfd sp!,{a2,a3,a4,ip}
stmfd sp!,{a1,a2,a4,ip}
mov a1,v1
mov a2,a3
bl Func_Compute_0(PLT)
mov a3,a1
ldmfd sp!,{a1,a2,a4,ip}
add a3,a1,a3
stmfd sp!,{a2,a3,a4,ip}
mov a1,v1
mov a2,a4
bl Func_Compute_0(PLT)
ldmfd sp!,{a2,a3,a4,ip}
cmp a1,a3
bgt L2
stmfd sp!,{a1,a2,a3,a4,ip}
ldr a1,=D0
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
b L1

L2:
stmfd sp!,{a1,a2,a3,a4,ip}
ldr a1,=D1
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}

L1:
mov a1,#0

main_exit:
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}

Func_Compute_0:
stmfd sp!,{fp,lr}
stmfd sp!,{v1,v2,v3,v4,v5}
mov fp,sp
mov ip,a2
mul a2,ip,a2
mov a1,a2
b Func_Compute_0_exit

Func_Compute_0_exit:
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}

Func_Compute_1:
stmfd sp!,{fp,lr}
stmfd sp!,{v1,v2,v3,v4,v5}
mov fp,sp
add a3,a2,a3
mov a1,a3
b Func_Compute_1_exit

Func_Compute_1_exit:
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}

Func_Compute_2:
stmfd sp!,{fp,lr}
stmfd sp!,{v1,v2,v3,v4,v5}
mov fp,sp
ldr ip,[a1,#0]
mov v5,#0
cmp ip,v5
bne L4
mov ip,#1
str ip,[a1,#0]
stmfd sp!,{a1,a3,a4,ip}
bl Func_Compute_0(PLT)
mov a2,a1
ldmfd sp!,{a1,a3,a4,ip}
stmfd sp!,{a1,a2,a4,ip}
mov a2,a3
bl Func_Compute_0(PLT)
mov a3,a1
ldmfd sp!,{a1,a2,a4,ip}
stmfd sp!,{a2,a3,a4,ip}
bl Func_Compute_1(PLT)
ldmfd sp!,{a2,a3,a4,ip}
b Func_Compute_2_exit
b L3

L4:
ldr a1,[a1,#4]
b Func_Compute_2_exit

L3:

Func_Compute_2_exit:
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}


