; x86_64 assembly using libc for printf and scanf
; To assemble and link (on 64-bit Linux), use:
; nasm -f elf64 ./SadCycles.asm
; gcc -o ./SadCycles ./SadCycles.o
; Solution to the problem posted here: https://www.reddit.com/r/dailyprogrammer/comments/36cyxf/20150518_challenge_215_easy_sad_cycles/

extern printf
extern scanf

section .data
outFormat:  db  "%lu, ", 00h
inFormat:   db  "%lu", 0Ah, "%lu", 00h
newLine:    db  0Ah, 00h

section .bss
inBuffer:   resq    2        ; Space for two unsigned long ints

section .text
global main

main:
    push    rbp
    mov     rbp, rsp

    mov     rdi, inFormat
    mov     rsi, inBuffer
    mov     rdx, inBuffer + 8
    call    scanf

    xor     rbx, rbx            ; keep track of what we have on stack to print
    mov     rax, [inBuffer + 8]
    xor     r8, r8

get_next_num:
    xor     rdx, rdx
    mov     rcx, 10
    div     rcx
    push    rax                ; _exponent function mutilates rax
    mov     rsi, [inBuffer]
    mov     rdi, rdx
    call    _exponent
    add     r8, rax
    pop     rax                 ; get our old rax backs
    cmp     rax, 00h
    jz      got_num
    jmp     get_next_num
got_num:
    push    r8
    inc     rbx
    mov     rcx, rbx
repeat_check:
    cmp     [rsp + 8 * rcx], r8  ; check the stack for a repeat
    jz      print_then           ; found one, let's print
    cmp     rcx, 01h             ; Don't wanna check the thing we just put on
    jz      continue_then
    dec     rcx
    jmp     repeat_check
continue_then:
    mov     rax, r8
    xor     r8, r8
    jmp     get_next_num

print_then:
    mov     rax, rbx          ; We only want to print up to the repeat
    sub     rax, rcx          ; So we'll calculate how many that is
    sub     rbx, rax          ; and reset rbx to that

print_loop:
    cmp     rbx, 00h
    jz      done_print
    mov     rdi, outFormat
    pop     rsi
    xor     rax, rax
    call    printf
    dec     rbx
    jmp     print_loop
done_print:
    mov     rdi, newLine    ; we don't need to fix the stack at all here because
    xor     rax, rax        ; we're about to ditch this whole stack frame.
    call    printf

    mov     rsp, rbp
    pop     rbp
    mov     rax, 0
    ret

_exponent:                  ; rdi to the rsi power
    push    rbp
    mov     rbp, rsp
    sub     rsp, 8h         ; A nice local varaible
    mov     [rsp], rdi
get_power:
    cmp     rsi, 01h
    jz      done_power
    imul    rdi, [rsp]
    dec     rsi
    jmp     get_power
done_power:
    mov     rsp, rbp        ; Fixing the stack
    pop     rbp
    mov     rax, rdi
    ret
