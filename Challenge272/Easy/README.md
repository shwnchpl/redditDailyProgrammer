My solution in Java to [this problem](https://www.reddit.com/r/dailyprogrammer/comments/4oylbo/20160620_challenge_272_easy_whats_in_the_bag/) (problem by [u/genderdoom](https://www.reddit.com/user/genderdoom)).

Sample inputs and outputs:

    $ java Challenge272EASY PQAREIOURSTHGWIOAE_
    Tiles still in the bag: 
    0: [Q]
    1: [H, J, K, P, W, X, Z, _]
    2: [B, C, F, G, M, V, Y]
    3: [S, U]
    4: [D, L, R]
    5: [T]
    6: [N, O]
    7: [A, I]
    10: [E]
    151 points remain in the bag.
    Tiles used:
    1: [G, H, P, Q, S, T, U, W, _]
    2: [A, E, I, O, R]
    36 points are in play.
    
    $ java Challenge272EASY LQTOONOEFFJZT      
    Tiles still in the bag: 
    0: [F, J, Q, Z]
    1: [K, X]
    2: [B, C, H, M, P, V, W, Y, _]
    3: [G, L]
    4: [D, S, T, U]
    5: [N, O]
    6: [R]
    9: [A, I]
    11: [E]
    143 points remain in the bag.
    Tiles used:
    1: [Q, E, J, Z, L, N]
    2: [T, F]
    3: [O]
    44 points are in play.
    
    $ java Challenge272EASY AXHDRUIOR_XHJZUQEE
    Invalid input. More X's have been taken from the bag than possible.
    
