; x86_64 assembly using libc
; To assemble and link (on 64-bit Linux), use:
; nasm -f elf64 ./TexasHoldEmDeal.asm
; gcc -o ./TexasHoldEmDeal ./TexasHoldEmDeal.o
; Solution to the problem posted here: https://www.reddit.com/r/dailyprogrammer/comments/378h44/20150525_challenge_216_easy_texas_hold_em_1_of_3/

section .data

suit:   db  "of Hearts", 00h, 00h, 00h, 00h, 00h, 00h, 00h
        db  "of Spades", 00h, 00h,  00h, 00h, 00h, 00h, 00h
        db  "of Clubs", 00h, 00h, 00h, 00h, 00h, 00h, 00h, 00h
        db  "of Diamonds", 00h

card:
        db  "23456789XJQKA"

outFormat:  db  "%c %s", 0Ah, 00h
yourHand:   db  "Your hand is:", 0Ah, 00h
cpuHand:    db  "CPU %d's hand is:", 0Ah, 00h
newLine:    db  0Ah, 00h
flopMSG:    db  "Flop:", 0Ah, 00h
turnMSG:    db  0Ah, "Turn:", 0Ah, 00h
rivrMSG:    db  0Ah, "River:", 0Ah, 00h
hmPLRS:     db  "How many players?: ", 00h
plrsIN:     db  "%d", 00h
tmPLRS:     db  "Texas Hold 'em is no fun with more than 8 players!", 0Ah, 00h

section .bss

players     resq    1

section .text

global main
extern printf, scanf, rand, time, srand

main:
    push    rbp
    mov     rbp, rsp

    lea     rdi, [hmPLRS]
    xor     rax, rax
    call    printf

    lea     rdi, [plrsIN]
    lea     rsi, [players]
    xor     rax, rax
    call    scanf

    mov     rax, [players]
    cmp     rax, 08h
    jg      too_many_players

    xor     rdi, rdi
    call    time
    mov     rdi, rax
    call    srand

    xor     rcx, rcx
load_stack:
    push    rcx
    inc     rcx
    cmp     rcx, 52
    jnz     load_stack

    xor     rbx, rbx
shuffle:
    call    rand
    mov     rcx, 52
    xor     rdx, rdx
    div     rcx
    shl     rdx, 03h
    lea     r15, [rsp + rdx]
    call    rand
    mov     rcx, 52
    xor     rdx, rdx
    div     rcx
    shl     rdx, 03h
    lea     r14, [rsp + rdx]    ; + rdx
    mov     rax, [r14]
    mov     rdx, [r15]
    mov     [r15], rax
    mov     [r14], rdx
    inc     rbx
    cmp     rbx, 100h           ; Number of times to shuffle.
    jnz     shuffle

                                ; At this point the stack is loaded with our cards

    xor     rbx, rbx
    xor     rcx, rcx
    lea     rax, [yourHand]
some_more:
    mov     rdi, cpuHand
    cmp     rbx, 00h
    cmovz   rdi, rax
    mov     rsi, rbx
    xor     rax, rax
    call    printf

    xor     r15, r15
again:
    pop     rax
    mov     rcx, 04h
    xor     rdx, rdx
    div     rcx
    lea     rdi, [outFormat]
    mov     rsi, [card + rax]
    shl     rdx, 04h
    lea     rdx, [suit + rdx]
    xor     rax, rax
    call    printf
    inc     r15
    cmp     r15, 02h
    jnz     again

    xor     rax, rax
    mov     rdi, newLine
    call    printf

    inc     rbx
    cmp     rbx, [players]
    jnz     some_more

    lea     rdi, [flopMSG]
    xor     rax, rax
    call    printf
    xor     ebx, ebx
flop_loop:                        ; Could likely implement this mess as a loop but whatever
    pop     rax
    mov     rcx, 04h
    xor     rdx, rdx
    div     rcx
    lea     rdi, [outFormat]
    mov     rsi, [card + rax]
    shl     rdx, 04h
    lea     rdx, [suit + rdx]
    xor     rax, rax
    call    printf
    inc     ebx
    cmp     ebx, 03h
    jnz     flop_loop

    lea     rdi, [turnMSG]
    xor     rax, rax
    call    printf

    pop     rax
    mov     rcx, 04h
    xor     rdx, rdx
    div     rcx
    lea     rdi, [outFormat]
    mov     rsi, [card + rax]
    shl     rdx, 04h
    lea     rdx, [suit + rdx]
    xor     rax, rax
    call    printf

    lea     rdi, [rivrMSG]
    xor     rax, rax
    call    printf

    pop     rax
    mov     rcx, 04h
    xor     rdx, rdx
    div     rcx
    lea     rdi, [outFormat]
    mov     rsi, [card + rax]
    shl     rdx, 04h
    lea     rdx, [suit + rdx]
    xor     rax, rax
    call    printf
    jmp     end

too_many_players:
    lea     rdi, [tmPLRS]
    xor     rax, rax
    call    printf

end:

    leave
    ret
