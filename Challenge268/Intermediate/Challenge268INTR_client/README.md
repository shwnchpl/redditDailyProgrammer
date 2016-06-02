#### Example client session:

\#1:

    $ java Challenge268INTR_client.Main 192.168.1.109
    Please enter a username: Carlo
    Connected!
    > Shawxe has logged on.
    > Fizzy has logged on.
    > Shawxe: I'm gonna start a blackjack game guys.
    > Shawxe has started a game of blackjack. Type 'join' to join.
    join
    > Carlo has joined Shawxe's blackjack game.
    senduser Fizzy Don't you want to play too?
    > (PM) Carlo: Don't you want to play too?
    > (PM) Fizzy: Oh, I guess.
    > (PM) Fizzy: Shawxe always wins this stuff.
    > Fizzy has joined Shawxe's blackjack game.
    > Shawxe has started the game!
    > Shawxe has been dealt Queen of Spades, Ace of Hearts
    > Carlo has been dealt Four of Diamonds, Ace of Spades
    > Fizzy has been dealt Six of Spades, Eight of Spades
    > Shawxe: Hah! Look at that.
    > Shawxe has passed!
    > (PM) Carlo, take or pass?
    > (PM) Fizzy: See what I mean.
    take
    > Carlo takes a Two of Clubs
    > Fizzy takes a Nine of Hearts
    > Fizzy has gone bust.
    > (PM) Carlo, take or pass?
    senduser Fizzy Yeah...
    > (PM) Carlo: Yeah...
    take
    > Carlo takes a Eight of Clubs
    > (PM) Carlo, take or pass?
    sendall Ouch
    > Carlo: Ouch
    take
    > Carlo takes a Two of Hearts
    > (PM) Carlo, take or pass?
    take
    > Carlo takes a Seven of Spades
    > Carlo has gone bust.
    > The winner is Shawxe with Queen of Spades, Ace of Hearts!
    sendall Looks like you got us again, Shawxe
    > Carlo: Looks like you got us again, Shawxe
    > Shawxe has started a game of blackjack. Type 'join' to join.
    > Fizzy has joined Shawxe's blackjack game.
    > Shawxe: Actually, I have to go.  Sorry guys.
    > Shawxe has logged off.
    > It appears that Shawxe has logged out. Game cancelled.
    > Fizzy: Good ridance.
    > Fizzy has logged off.

\#2:

    $ java Challenge268INTR_client.Main 192.168.1.109
    Please enter a username: Shawxe
    Connected!
    > Fizzy has logged on.
    listusers
    > Shawxe
    > Carlo
    > Fizzy
    sendall I'm gonna start a blackjack game guys.
    > Shawxe: I'm gonna start a blackjack game guys.
    blackjack
    > Shawxe has started a game of blackjack. Type 'join' to join.
    > Carlo has joined Shawxe's blackjack game.
    > Fizzy has joined Shawxe's blackjack game.
    start
    > Shawxe has started the game!
    > Shawxe has been dealt Queen of Spades, Ace of Hearts
    > Carlo has been dealt Four of Diamonds, Ace of Spades
    > Fizzy has been dealt Six of Spades, Eight of Spades
    > (PM) Shawxe, take or pass?
    sendall Hah! Look at that.
    > Shawxe: Hah! Look at that.
    pass
    > Shawxe has passed!
    > Carlo takes a Two of Clubs
    > Fizzy takes a Nine of Hearts
    > Fizzy has gone bust.
    > Carlo takes a Eight of Clubs
    > Carlo: Ouch
    > Carlo takes a Two of Hearts
    > Carlo takes a Seven of Spades
    > Carlo has gone bust.
    > The winner is Shawxe with Queen of Spades, Ace of Hearts!
    > Carlo: Looks like you got us again, Shawxe
    blackjack
    > Shawxe has started a game of blackjack. Type 'join' to join.
    > Fizzy has joined Shawxe's blackjack game.
    sendall Actually, I have to go.  Sorry guys.
    > Shawxe: Actually, I have to go.  Sorry guys.
    quit

\#3:

    $ java Challenge268INTR_client.Main 192.168.1.109
    Please enter a username: Fizzy
    Connected!
    > Shawxe: I'm gonna start a blackjack game guys.
    > Shawxe has started a game of blackjack. Type 'join' to join.
    > Carlo has joined Shawxe's blackjack game.
    > (PM) Carlo: Don't you want to play too?
    senduser Carlo Oh, I guess.
    > (PM) Fizzy: Oh, I guess.
    senduser Carlo Shawxe always wins this stuff.
    > (PM) Fizzy: Shawxe always wins this stuff.
    join
    > Fizzy has joined Shawxe's blackjack game.
    > Shawxe has started the game!
    > Shawxe has been dealt Queen of Spades, Ace of Hearts
    > Carlo has been dealt Four of Diamonds, Ace of Spades
    > Fizzy has been dealt Six of Spades, Eight of Spades
    > Shawxe: Hah! Look at that.
    > Shawxe has passed!
    senduser Carlo See what I mean.
    > (PM) Fizzy: See what I mean.
    > Carlo takes a Two of Clubs
    > (PM) Fizzy, take or pass?
    take
    > Fizzy takes a Nine of Hearts
    > Fizzy has gone bust.
    > (PM) Carlo: Yeah...
    > Carlo takes a Eight of Clubs
    > Carlo: Ouch
    > Carlo takes a Two of Hearts
    > Carlo takes a Seven of Spades
    > Carlo has gone bust.
    > The winner is Shawxe with Queen of Spades, Ace of Hearts!
    > Carlo: Looks like you got us again, Shawxe
    > Shawxe has started a game of blackjack. Type 'join' to join.
    join
    > Fizzy has joined Shawxe's blackjack game.
    > Shawxe: Actually, I have to go.  Sorry guys.
    > Shawxe has logged off.
    > It appears that Shawxe has logged out. Game cancelled.
    sendall Good ridance.
    > Fizzy: Good ridance.
