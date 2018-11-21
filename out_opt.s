.data
D0:
.asciz "2 + 3 = "
D1:
.asciz "%i"
D2:
.asciz "\n"

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
stmfd sp!,{a1,a2,a3,a4,ip}
ldr a1,=D0
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
stmfd sp!,{a2,a3,a4,ip}
mov ip,#3
str ip,[sp, #-4]!
mov ip,#2
str ip,[sp, #-4]!
mov ip,#1
str ip,[sp, #-4]!
mov ip,#1
str ip,[sp, #-4]!
mov ip,#1
str ip,[sp, #-4]!
mov a2,#1
mov a3,#1
mov a4,#1
bl Func_FuncTest_0(PLT)
add sp,sp,#20
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
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}

Func_FuncTest_0:
stmfd sp!,{fp,lr}
stmfd sp!,{v1,v2,v3,v4,v5}
mov fp,sp
ldr a1,[fp,#40]
ldr a2,[fp,#44]
add a2,a1,a2
mov a1,a2
b Func_FuncTest_0_exit

Func_FuncTest_0_exit:
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}


