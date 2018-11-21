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
stmfd sp!,{a2,a3,a4,ip}
mov ip,#100
str ip,[sp, #-4]!
mov ip,#2
str ip,[sp, #-4]!
mov ip,#4
str ip,[sp, #-4]!
mov ip,#2
str ip,[sp, #-4]!
mov ip,#3
str ip,[sp, #-4]!
mov ip,#1
str ip,[sp, #-4]!
mov a2,#1
mov a3,#1
mov a4,#1
bl Func_TestClass_0(PLT)
add sp,sp,#24
ldmfd sp!,{a2,a3,a4,ip}
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
ldr a1,[fp,#32]
ldr a2,[fp,#36]
ldr a3,[fp,#40]
ldr a4,[fp,#44]
mov v1,a1
mov a1,a4
mov ip,#0
sub v1,ip,v1
mov ip,#0
sub v1,ip,v1
add a2,v1,a2
mov ip,#0
sub a2,ip,a2
mov ip,#0
sub a2,ip,a2
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mov ip,#0
sub a3,ip,a3
mul a3,a2,a3
mov ip,#0
sub a1,ip,a1
mov ip,#0
sub a1,ip,a1
mul a1,a3,a1
b Func_TestClass_0_exit

Func_TestClass_0_exit:
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}


