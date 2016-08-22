My solution in Haskell to [this problem](https://www.reddit.com/r/dailyprogrammer/comments/4ybbcz/20160818_challenge_279_intermediate_text_reflow/) (problem by [u/slampropp](https://www.reddit.com/user/slampropp)).

Reads a block of text from standard input and reformats the text to be [justified](https://en.wikipedia.org/wiki/Typographic_alignment#Justified) at a width specified by command line argument.  Handles all possible edge cases safely.

Sample input and output:

    $ ./justify 
    
    Usage: justify [# of chars/line]
    
    $ cat ./testInput.txt ; echo
    
    In the beginning God created the heavens and the earth. Now the earth was
    formless and empty, darkness was over the surface of the deep, and the Spirit of
    God was hovering over the waters.
    
    And God said, "Let there be light," and there was light. God saw that the light
    was good, and he separated the light from the darkness. God called the light
    "day," and the darkness he called "night." And there was evening, and there was
    morning - the first day.
    
    $ cat ./testInput.txt | ./justify 40
    
    In the beginning God created the heavens
    and   the   earth.  Now  the  earth  was
    formless and  empty,  darkness  was over
    the surface of the  deep, and the Spirit
    of God  was  hovering  over  the waters.
    
    And God said, "Let  there be light," and
    there was light. God  saw that the light
    was good,  and  he  separated  the light
    from the darkness.  God called the light
    "day,"  and   the   darkness  he  called
    "night."  And  there  was  evening,  and
    there  was   morning  -  the  first day.
    
    $ cat ./testInput.txt | ./justify 20
    
    In the beginning God
    created  the heavens
    and  the  earth. Now
    the     earth    was
    formless  and empty,
    darkness   was  over
    the  surface  of the
    deep, and the Spirit
    of God was  hovering
    over   the   waters.
    
    And  God  said, "Let
    there be light," and
    there was light. God
    saw  that  the light
    was   good,  and  he
    separated  the light
    from  the  darkness.
    God called the light
    "day,"    and    the
    darkness  he  called
    "night."  And  there
    was   evening,   and
    there was  morning -
    the    first    day.
    
    $ cat ./testInput.txt | ./justify 100
    
    In the beginning God created  the  heavens  and  the  earth.  Now  the earth was formless and empty,
    darkness was over the surface of  the  deep,  and  the  Spirit  of God was hovering over the waters.
    
    And God said, "Let there be light,"  and  there  was  light. God saw that the light was good, and he
    separated the light from the  darkness.  God  called  the  light  "day,"  and the darkness he called
    "night."    And    there    was    evening,    and    there   was   morning   -   the   first   day.
    
    $ cat ./testInput2.txt ; echo
    
    This is a slightly shorter test to demonstrate some edge cases.
    
    $ cat ./testInput2.txt | ./justify -500000
    
    This
    is
    a
    slightly
    shorter
    test
    to
    demonstrate
    some
    edge
    cases.
    
    $ cat ./testInput2.txt | ./justify 500000 
    
    This is a slightly shorter test to demonstrate some edge cases.

Some of the building-block functions used in the program demonstrated in a GHCI session:

#####splitOn*

    Prelude> :l justify.hs 
    [1 of 1] Compiling Main             ( justify.hs, interpreted )
    Ok, modules loaded: Main.
    *Main> :t splitOn
    splitOn                    splitOnSpaceNearest        splitOnSpaceNearestCenter  splitOnSubNearest
    *Main> :t splitOnSubNearest 
    splitOnSubNearest :: String -> Int -> String -> (String, String)
    *Main> let exampleString = "This |% is an #| example so &% foo, bar, b%az, and whatnot."
    
    *Main> splitOnSpaceNearestCenter exampleString 
    Loading package split-0.2.3.1 ... linking ... done.
    ("This |% is an #| example so &%","foo, bar, b%az, and whatnot.")
    
    *Main> splitOnSubNearest "&" 20 exampleString 
    ("This |% is an #| example so ","% foo, bar, b%az, and whatnot.")
    
    *Main> splitOnSubNearest "#|" 20 exampleString 
    ("This |% is an "," example so &% foo, bar, b%az, and whatnot.")
    
    *Main> splitOnSubNearest "|" 200000 exampleString 
    ("This |% is an #"," example so &% foo, bar, b%az, and whatnot.")
    
    *Main> splitOnSubNearest "|" (-200000) exampleString 
    ("This ","% is an #| example so &% foo, bar, b%az, and whatnot.")
    
    *Main> splitOnSubNearest "NOTINTHERE" 0 exampleString 
    ("This |% is an #| example so &% foo, bar, b%az, and whatnot.","")

##### padStrTo    
    
    *Main> :t padStrTo
    padStrTo :: Int -> String -> String
    
    *Main> padStrTo 20 exampleString 
    "This |% is an #| example so &% foo, bar, b%az, and whatnot."
    
    *Main> padStrTo 100 exampleString 
    "This   |%    is    an    #|    example    so    &%      foo,     bar,     b%az,     and     whatnot."
    
    *Main> padStrTo 80 exampleString 
    "This  |%  is  an  #|   example   so   &%    foo,   bar,   b%az,   and   whatnot."
    
    *Main> padStrTo 1000 exampleString 
    "This |% is an #| example so &% foo, bar, b%az, and whatnot."
    
    *Main> padStrTo (-500) exampleString 
    "This |% is an #| example so &% foo, bar, b%az, and whatnot."
    
#####smartWrap    
    
    *Main> :t smartWrap 
    smartWrap :: Int -> String -> String
    *Main> exampleInput <- readFile "testInput.txt"
    *Main> putStrLn exampleInput 
    In the beginning God created the heavens and the earth. Now the earth was
    formless and empty, darkness was over the surface of the deep, and the Spirit of
    God was hovering over the waters.
    
    And God said, "Let there be light," and there was light. God saw that the light
    was good, and he separated the light from the darkness. God called the light
    "day," and the darkness he called "night." And there was evening, and there was
    morning - the first day.

    *Main> putStrLn $ smartWrap 40 exampleInput 
    In the beginning God created the heavens
    and the earth. Now the earth was
    formless and empty, darkness was over
    the surface of the deep, and the Spirit
    of God was hovering over the waters.
    
    And God said, "Let there be light," and
    there was light. God saw that the light
    was good, and he separated the light
    from the darkness. God called the light
    "day," and the darkness he called
    "night." And there was evening, and
    there was morning - the first day.
    
    *Main> putStrLn $ smartWrap 100 exampleInput 
    In the beginning God created the heavens and the earth. Now the earth was formless and empty,
    darkness was over the surface of the deep, and the Spirit of God was hovering over the waters.
    
    And God said, "Let there be light," and there was light. God saw that the light was good, and he
    separated the light from the darkness. God called the light "day," and the darkness he called
    "night." And there was evening, and there was morning - the first day.
    
    *Main> putStrLn $ smartWrap 20 exampleInput 
    In the beginning God
    created the heavens
    and the earth. Now
    the earth was
    formless and empty,
    darkness was over
    the surface of the
    deep, and the Spirit
    of God was hovering
    over the waters.
    
    And God said, "Let
    there be light," and
    there was light. God
    saw that the light
    was good, and he
    separated the light
    from the darkness.
    God called the light
    "day," and the
    darkness he called
    "night." And there
    was evening, and
    there was morning -
    the first day.
    
