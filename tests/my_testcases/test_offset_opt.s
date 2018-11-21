.data
D0:
.asciz "%i"

.text
.global main

main:
stmfd sp!,{fp,lr}
stmfd sp!,{v1,v2,v3,v4,v5}
mov fp,sp
stmfd sp!,{a1,a3,a4,ip}
mov a1,#6400
bl malloc(PLT)
mov a2,a1
ldmfd sp!,{a1,a3,a4,ip}
ldr ip,=301
str ip,[a2,#0]
mov ip,#1
ldr v5,=6132
add v5,a2,v5
str ip,[v5]
ldr v5,=6132
add v5,a2,v5
ldr a1,[v5]
ldr v5,=6132
add v5,a2,v5
ldr a3,[v5]
add a1,a1,a3
ldr a3,[a2,#0]
add ip,a1,a3
str ip,[a2,#4]
ldr a1,[a2,#4]
stmfd sp!,{a1,a2,a3,a4,ip}
mov a2,a1
ldr a1,=D0
bl printf(PLT)
ldmfd sp!,{a1,a2,a3,a4,ip}
mov a1,#0

main_exit:
ldmfd sp!,{v1,v2,v3,v4,v5}
ldmfd sp!,{fp,pc}


