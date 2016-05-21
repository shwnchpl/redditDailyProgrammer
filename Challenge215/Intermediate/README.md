My solution in x86_64 assembly (using libc for printf, scanf, malloc, and free) to [this problem](https://www.reddit.com/r/dailyprogrammer/comments/36m83a/20150520_challenge_215_intermediate_validating/) (problem by [u/XenophonOfAthens](https://www.reddit.com/user/ XenophonOfAthens)).    

Can be assembled and linked with the following commands on 64-bit Linux:

    nasm -f elf64 ./SortNetworkValidate.asm
    gcc -o ./SortNetworkValidate ./SortNetworkValidate.o
    
Theoretically, since relies on libc for IO calls, it should be somewhat portable to other 64-bit operating systems with libc.

Makes use of bitwise logic to generate binary permutations.  Doesn't error check inputs, so a network parameter that called for going out of bounds would result in a segfault, although it wouldn't be hard to prevent this.
