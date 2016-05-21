; x86_64 assembly using libc for printf, scanf, malloc, and free.
; To assemble and link (on 64-bit Linux), use:
; nasm -f elf64 ./SortNetworkValidate.asm
; gcc -o ./SortNetworkValidate ./SortNetworkValidate.o
; Solution to the problem posted here: https://www.reddit.com/r/dailyprogrammer/comments/36m83a/20150520_challenge_215_intermediate_validating/

extern printf
extern scanf
extern malloc
extern free

section .data

inoutFormat:    db  "%lu %lu", 00h
newLine:        db  0Ah, 00h
validNet:       db  "Valid network!", 0Ah, 00h
invalidNet:     db  "Invalid network.", 0Ah, 00h

section .text
global main

main:
    push    rbp
    mov     rbp, rsp                  ; Set up a local stack

    sub     rsp, 10h                  ; Space for two local variables

    mov     rdi, inoutFormat
    mov     rsi, rsp
    mov     rdx, rsi
    add     rdx, 08h
    call    scanf
                                      ; Allocate memory for rsp + 8 lines
                                      ; of input; [rsp + 8] * 2 * 8
    mov     rdi, [rsp + 8]
    shl     rdi, 04h                  ; multiply by 16
    call    malloc

    mov     r15, rax                  ; r15 presserved across function
                                      ; calls in this ABI; safe space

    xor     rbx, rbx                  ; how many lines we've read

read_inputs:
    cmp     rbx, [rsp + 8]
    jz      done_read
    mov     rdi, inoutFormat
    mov     rsi, r15
    mov     rdx, rbx
    shl     rdx, 04h                  ; get us to appropriate offset
    add     rsi, rdx
    mov     rdx, rsi
    add     rdx, 08h
    call    scanf
                                      ; could/should error handle here
    inc     rbx                       ; rbx preserved across calls in this ABI

    jmp     read_inputs

done_read:
    mov     rdi, newLine
    xor     rax, rax
    call    printf    

    mov     rdi, [rsp]                ; Allocate some memory for our binary permutations
    shl     rdi, 03h
    call    malloc
    mov     r14, rax                  ; r14 is preserved across function calls in
                                      ; system V ABI
    xor     rbx, rbx
    mov     r13, 01h
    mov     rcx, [rsp]
    shl     r13, cl
sort_validation:
    cmp     rbx, r13
    je      done_sort_validation
    xor     rcx, rcx
inner_sort_validation:
    cmp     rcx, [rsp]
    jz      ready_to_sort
    mov     rax, rbx
    shr     rax, cl
    and     rax, 01h
    mov     [r14 + rcx * 8], rax
    inc     rcx
    jmp     inner_sort_validation
ready_to_sort:
    mov     rdi, r14
    mov     rsi, r15
    mov     rdx, [rsp + 8]
    call    _sort_by_network

    mov     rdi, r14
    mov     rsi, [rsp]
    call    _is_sorted
    cmp     rax, 00h
    jz      not_sorted_fail

    inc     rbx
    jmp     sort_validation
done_sort_validation:
    jmp     sort_success

sort_success:
    mov     rdi, validNet
    xor     rax, rax
    call    printf
    jmp     time_to_bail

not_sorted_fail:
   mov     rdi, invalidNet
   xor     rax, rax
   call    printf

time_to_bail:
                                      ; Now let's free the memory
    mov     rdi, r15
    call    free

    mov     rdi, r14
    call    free

    mov     rsp, rbp
    pop     rbp
    xor     rax, rax                  ; return value of 0; exit success!

    ret

_sort_by_network:                     ; ideally, this function should error check somehow
                                      ; to make sure that instructions aren't going out
                                      ; of bounds.  But oh well.
    push    rbp
    mov     rbp, rsp                  ; Set up a local stack

    xor     rcx, rcx
lets_sort:
    cmp     rcx, rdx
    jz      done_sort
    mov     rax, rsi
    mov     r9, rcx
    shl     r9, 04h
    add     rax, r9
    mov     r8, [rax]                 
    mov     r11, [rax + 8]            ; r8 and r11 now contain the instruction tuple we need

    mov     r9, [rdi + r8 * 8]        ; load two numbers needed
    mov     r10, [rdi + r11 * 8]
    cmp     r9, r10
    jle     no_swap
    mov     [rdi + r8 * 8], r10       ; Make the needed swap
    mov     [rdi + r11 * 8], r9
no_swap:
    inc     rcx
    jmp     lets_sort

done_sort:
;   fix stack frame
    mov     rsp, rbp
    pop     rbp
    ret

_is_sorted:
    push    rbp
    mov     rbp, rsp                  ; Set up a local stack

    xor     rcx, rcx                  ; number of elements checked
    dec     rsi                       ; no element after the last one
check_sort:
    cmp     rcx, rsi                  ; All done at this point
    jz      yes_sorted
    mov     rdx, [rdi + rcx * 8]
    cmp     rdx, [rdi + rcx * 8 + 8]
    jnle    not_sorted                ; if rdx is greater than next element, we're not sorted
    inc     rcx
    jmp     check_sort

yes_sorted:
    mov     rax, 01h
    jmp     is_sorted_done
not_sorted:
    xor     rax, rax

is_sorted_done:
    mov     rsp, rbp
    pop     rbp
    ret
