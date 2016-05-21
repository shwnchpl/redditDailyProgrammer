My solution in x86_64 assembly to [this problem](https://www.reddit.com/r/dailyprogrammer/comments/378h44/20150525_challenge_216_easy_texas_hold_em_1_of_3/) (problem by [u/Coder_d00d](https://www.reddit.com/user/Coder_d00d)).    

Can be assembled and linked with the following commands on 64-bit Linux:

    nasm -f elf64 ./TexasHoldEmDeal.asm
    gcc -o ./TexasHoldEmDeal ./TexasHoldEmDeal.o
    
Theoretically, since relies on libc for IO calls, it should be somewhat portable to other 64-bit operating systems with libc.

Throws numbers 0-51 up on the stack and then shuffles them with a series of calls to libc random. Calculates suit as modulus. "Unrolls" the flop/turn/river loop because it should theoretically run faster that way anyhow (by avoiding branch projection, although the difference has to be trivial in this case).
