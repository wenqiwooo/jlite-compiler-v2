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
mov a1,#6400
bl malloc(PLT)
mov a1,a1
ldmfd sp!,{a2,a3,a4,ip}
mov ip,#1
ldr v5,=6132
add v5,a1,v5
str ip,[v5]
ldr v5,=6132
add v5,a1,v5
ldr a1,[v5]
stmfd sp!,{a1,a2,a3,a4,ip}
mov a2,a1
ldr a1,=D0
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}

main_exit:
add sp,sp,#0
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}


