My solution in rustc 1.0.0-beta (9854143cb 2015-04-02) to [this problem](https://www.reddit.com/r/dailyprogrammer/comments/40rs67/20160113_challenge_249_intermediate_hello_world/) (problem by [u/pantsforbirds](https://www.reddit.com/user/pantsforbirds)).

Since characters in rust are UTF-32 encoded, I narrowed things down a little bit, limiting my random characters to 0x20 through 0x7e.  Doesn't error handle to make sure no one enters a "UTF-32" string outside of ascii characters in the first place.

Output:

    Enter a string: 
    Hello, World!
    Gen: 0 | Fitness: 404   | CaK5?9sTl>b#?
    Gen: 1 | Fitness: 373   | CaK5^9sTl>b#?
    Gen: 4 | Fitness: 319   | CaK5^9sTl>bo?
    Gen: 6 | Fitness: 279   | CaK5^9sTlfbo?
    Gen: 12 | Fitness: 272  | CaK5^&sTlfbo?
    Gen: 20 | Fitness: 240  | Cam5^&sTlfbo?
    Gen: 27 | Fitness: 237  | Cam5^&sTlfbl?
    Gen: 32 | Fitness: 192  | Camb^&sTlfbl?
    Gen: 41 | Fitness: 181  | Camb^&sTlsbl?
    Gen: 60 | Fitness: 145  | Camb^&OTlsbl?
    Gen: 70 | Fitness: 142  | Camb}&OTlsbl?
    Gen: 89 | Fitness: 140  | Cambc&OTlsbl?
    Gen: 90 | Fitness: 137  | Cambc&OWlsbl?
    Gen: 95 | Fitness: 134  | Cambc/OWlsbl?
    Gen: 99 | Fitness: 130  | Cambc/OWlsfl?
    Gen: 110 | Fitness: 128 | Ccmbc/OWlsfl?
    Gen: 114 | Fitness: 126 | Ccmtc/OWlsfl?
    Gen: 136 | Fitness: 125 | Ccmtz/OWlsfl?
    Gen: 141 | Fitness: 124 | Ccmtz.OWlsfl?
    Gen: 142 | Fitness: 121 | Ccmtg.OWlsfl?
    Gen: 146 | Fitness: 117 | Ccmpg.OWlsfl?
    Gen: 148 | Fitness: 114 | Ccmpg.OWlsf_?
    Gen: 165 | Fitness: 97  | Ccmpg.OWlsf_.
    Gen: 169 | Fitness: 87  | Ccmpg.EWlsf_.
    Gen: 176 | Fitness: 80  | Ccmpg.>Wlsf_.
    Gen: 177 | Fitness: 67  | Ccmpg.>Wlsf_!
    Gen: 178 | Fitness: 64  | Ccmkg.>Wlsf_!
    Gen: 199 | Fitness: 61  | Ccmkj.>Wlsf_!
    Gen: 230 | Fitness: 57  | Ccmkj.>Wlsfc!
    Gen: 247 | Fitness: 53  | Ccmkj.:Wlsfc!
    Gen: 272 | Fitness: 52  | Ccmkj.:Wlsfd!
    Gen: 278 | Fitness: 31  | Ccmkj.%Wlsfd!
    Gen: 291 | Fitness: 29  | Kcmkj.%Wlsfd!
    Gen: 301 | Fitness: 28  | Kclkj.%Wlsfd!
    Gen: 319 | Fitness: 26  | Iclkj.%Wlsfd!
    Gen: 399 | Fitness: 24  | Iclkj.%Wlspd!
    Gen: 458 | Fitness: 23  | Iclkj.%Wlrpd!
    Gen: 461 | Fitness: 21  | Iclkj.%Wlrjd!
    Gen: 472 | Fitness: 19  | Iclkr.%Wlrjd!
    Gen: 599 | Fitness: 18  | Iclkr.$Wlrjd!
    Gen: 642 | Fitness: 17  | Iflkr.$Wlrjd!
    Gen: 793 | Fitness: 15  | Iflkr,$Wlrjd!
    Gen: 810 | Fitness: 13  | Iflkp,$Wlrjd!
    Gen: 814 | Fitness: 9   | Iflkp, Wlrjd!
    Gen: 870 | Fitness: 8   | Iflko, Wlrjd!
    Gen: 896 | Fitness: 7   | Iflko, Wmrjd!
    Gen: 900 | Fitness: 6   | Iflko, Wnrjd!
    Gen: 1013 | Fitness: 5  | Iflko, Worjd!
    Gen: 1068 | Fitness: 4  | Iflko, Wormd!
    Gen: 1082 | Fitness: 3  | Ifllo, Wormd!
    Gen: 1146 | Fitness: 2  | Hfllo, Wormd!
    Gen: 1499 | Fitness: 1  | Hello, Wormd!
    Gen: 1780 | Fitness: 0  | Hello, World!
    Elapsed time: 34 milliseconds
