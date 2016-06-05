My solution in Java to [this problem](https://www.reddit.com/r/dailyprogrammer/comments/4m3ddb/20160601_challenge_269_intermediate_mirror/) (problem by [u/fvandepitte](https://www.reddit.com/user/fvandepitte)).  Stores pathways through mirror key in a hashmap for increased efficiency.  Doesn't check for input errors.

Output:

    Enter a mirror diagram (followed by ^D): 
       \\  /\    
                \
       /         
          \     \
        \        
      /      /   
    \  /      \  
         \       
    \/           
    /            
              \  
        \/       
       /       / 
    Enter (in order on seperate lines) the top, bottom, left, and right sides: 
    abcdefghijklm
    NOPQRSTUVWXYZ
    ABCDEFGHIJKLM
    nopqrstuvwxyz
    Enter string to be encoded/decoded: 
    TpnQSjdmZdpoohd
    DailyProgrammer
