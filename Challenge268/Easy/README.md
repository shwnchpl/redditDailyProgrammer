My heavily engineered solution in Java to [this problem](https://www.reddit.com/r/dailyprogrammer/comments/4knivr/20160523_challenge_268_easy_network_and_cards/) (problem by [u/smapti](https://www.reddit.com/user/fvandepitte)).

#### Overview

The solution I've developed is highly multi-threaded and relatively scalable.  The code makes use of a PriorityQueue based system for both console and client/server IO.  Thus far, I've employed a relatively simple protocol in which server keywords are distinguished from regular messages by the client.

#### Key Features

* Seperate threads for input, output, and processing for each client connection.
* Heartbeat
* Error-safe individual usernames, private messages, and messages to all
* "listusers" command to list all users connected
* Broadcast of presence on network using UDP multicast.
* Automatic client seek-and-connect over local network.

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

For example client and server sessions, see the readme files in the client and server directories.
