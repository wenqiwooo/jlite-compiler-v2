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
mov ip,#1
add a1,ip,#2
add a1,a1,#3
add a1,a1,#4
add a1,a1,#5
add a1,a1,#6
add a1,a1,#7
add a1,a1,#8
add a1,a1,#9
add a1,a1,#10
add a1,a1,#11
add a1,a1,#12
add a1,a1,#13
add a1,a1,#14
add a1,a1,#15
add a1,a1,#16
add a1,a1,#17
add a1,a1,#18
add a1,a1,#19
add a1,a1,#20
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


