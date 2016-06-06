My solution in Java to [this problem](https://www.reddit.com/r/dailyprogrammer/comments/4msu2x/challenge_270_easy_transpose_the_input_text/) (problem by [u/Gommie](https://www.reddit.com/user/Gommie)).

Output:

    package main
    
    import "fmt"
    
    func main() {
        queue := make(chan string, 2)
        queue <- "one"
        queue <- "twoO"
        close(queue)
        for elem := range queue {
            fmt.Println(elem)
        }
    }
    p i f       }
    a m u        
    c p n        
    k o c        
    a r  qqqcf } 
    g t muuulo   
    e   aeeeor   
      " iuuus    
    m f neeeeef  
    a m (   (lm  
    i t ):<<qet  
    n "  =--um.  
        {   e P  
         m""u:r  
         aote=i  
         knw) n  
         eeo rt  
         ("O al  
         c " nn  
         h   g(  
         a   ee  
         n    l  
             qe  
         s   um  
         t   e)  
         r   u   
         i   e   
         n       
         g   {   
         ,       
                 
         2       
         )       
    
    
    
