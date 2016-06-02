My heavily engineered solution in Java to [this problem](https://www.reddit.com/r/dailyprogrammer/comments/4kz0e0/20160525_challenge_268_intermediate_network_and/) (problem by [u/fvandepitte](https://www.reddit.com/user/fvandepitte)).

#### Overview

In developing this solution, I sought to maintain and expand upon all functionality from week 268's easy challenge.  The end result of this is a simple and efficient multi-threaded chat and blackjack client and server.  Blackjack games allow for up to eight players and are run in their own thread so that normal chat can carry on at the same time as the game.  The blackjack thread recieves commands from client input processor threads on the server that check and make sure the input is apporpriate before sending it so that the blackjack game itself never gets bogged down.  If a user logs off when it's their turn to play, the game is cancelled.

#### Key Features

* Seperate threads for input, output, and processing for each client connection.
* Heartbeat.
* Error-safe individual usernames, private messages, and messages to all.
* "listusers" command to list all users connected.
* Private messaging between users.
* Group messages to all users.
* Broadcast of presence on network using UDP multicast.
* Automatic client seek-and-connect over local network.
* Blackjack game that can be started by an individual users, up to eight players.

#### Client-Server Protocol

All messages from the client to the server are scrutinized as commands, with the exception of "CON" which is a client-to-server heartbeat confirmation.  All messages from the server to the client are printed directly, unless they are one of the following keywords:

    GRT       - Greeting from server to client
    UNNF      - Requested username taken/invalid
    WLK       - Successfully connected
    CONCHECK  - Heartbeat request
    SCT       - Server connection termination notifier
  
#### Command Listing

The server accepts the following commands:

    quit                      -   Disconnects user
    sendall <message>         -   sends <message> to all connected users
    senduser <user> <message> -   sends the message <message> to user <user>
    listusers                 -   prints a list of currently connected users
    blackjack                 -   starts a new blackjack game if a game is not already pending or in progress
    join                      -   joins a pending blackjack game
    start                     -   begins a pending blackjack game (that the user has initialized)
    take                      -   takes a card from the dealer (on a player's turn)
    pass                      -   passes (on a player's turn)
    
For example client and server sessions, see the readme files in the client and server directories.
