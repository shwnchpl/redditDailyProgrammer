My solution in Haskell to [this problem](https://www.reddit.com/r/dailyprogrammer/comments/4xy6i1/20160816_challenge_279_easy_uuencoding/) (problem by [u/EvgeniyZh](https://www.reddit.com/user/EvgeniyZh)).

Sample input and output (newlines added for readability):

    $ cat ./file.txt
    
    I feel very strongly about you doing duty. Would you give me a little more documentation about
    your reading in French? I am glad you are happy - but I never believe much in happiness. I never
    believe in misery either. Those are things you see on the stage or the screen or the printed pages,
    they never really happen to you in life.
    
    $ ./uuEncode file.txt 
    
    begin 644 file.txt
    M22!F965L('9E<GD@<W1R;VYG;'D@86)O=70@>6]U(&1O:6YG(&1U='DN(%=O
    M=6QD('EO=2!G:79E(&UE(&$@;&ET=&QE(&UO<F4@9&]C=6UE;G1A=&EO;B!A
    M8F]U="!Y;W5R(')E861I;F<@:6X@1G)E;F-H/R!)(&%M(&=L860@>6]U(&%R
    M92!H87!P>2 M(&)U="!)(&YE=F5R(&)E;&EE=F4@;75C:"!I;B!H87!P:6YE
    M<W,N($D@;F5V97(@8F5L:65V92!I;B!M:7-E<GD@96ET:&5R+B!4:&]S92!A
    M<F4@=&AI;F=S('EO=2!S964@;VX@=&AE('-T86=E(&]R('1H92!S8W)E96X@
    M;W(@=&AE('!R:6YT960@<&%G97,L('1H97D@;F5V97(@<F5A;&QY(&AA<'!E
    2;B!T;R!Y;W4@:6X@;&EF92X 
    `
    end
    
