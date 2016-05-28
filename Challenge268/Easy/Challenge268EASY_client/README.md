
#### Example client session:

\#1:

    $ java Challenge268EASY_client.Main
    Searching for server on network...
    Received server address 192.168.1.177
    Attempting to connect.
    Please enter a username: Shawxe
    Connected!
    listusers
    > Shawxe
    help
    > Command not found.
    > Jimmy has logged on.
    > Jimmy: Hey guys!
    > (PM) Jimmy: You're the only one here, huh?
    senduser Jimmy I think you're talking to yourself, bub.
    > (PM) Shawxe: I think you're talking to yourself, bub.
    sendall I'm out of here guys.
    > Shawxe: I'm out of here guys.
    quit
    
\#2:

    $ java Challenge268EASY_client.Main 192.168.1.177
    Please enter a username: Jimmy
    Connected!
    listusers
    > Shawxe
    > Jimmy
    sendall Hey guys!
    > Jimmy: Hey guys!
    senduser Shawxe You're the only one here, huh?
    > (PM) Jimmy: You're the only one here, huh?
    > (PM) Shawxe: I think you're talking to yourself, bub.
    > Shawxe: I'm out of here guys.
    > Shawxe has logged off.
    sendall Me too, I guess.
    > Jimmy: Me too, I guess.
    quit
