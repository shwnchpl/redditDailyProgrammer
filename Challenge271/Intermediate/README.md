My solution in Java to [this problem](https://www.reddit.com/r/dailyprogrammer/comments/4o74p3/20160615_challenge_271_intermediate_making_waves/) (problem by [u/G33kDude](https://www.reddit.com/user/G33kDude)).


Can be run by passing arguments in the following format:

    java WaveGen.Main <sample rate> <note duration> <note string> [wave form]

Sample rate is in hertz and note duration is in milliseconds.  Note string should be formatted "A4 Bb4 F#6" etc. with "_" or any non-note character used to indicate a rest.  Wave form defaults to sinusoidal if no input is specified.  Supported wave forms are

    sine
    square
    sawtooth
    triangle

The program generates a 16bit wav file containing a sequence of waves of the selected wave form and of the specified frequencies that play for the specified duration.  This file is dumped to stdout.
    
For instance

    $ java WaveGen.Main 8000 300 "C4 D4 E4 F5 _" > test.wav
    
would generate an 8000 hertz sample rate wav file containing the notes c4, D4, E4, and F5 for 300ms each, each played as sine waves.  This output would be stored in a file called test.wav.  On systems supprting aplay

    $ java waveGen.Main 8000 300 "C4 D4 E4 F5 _" |aplay

could be run to play the wav file generated immediately without saivng it.

[This file](http://vocaroo.com/i/s0YW8TyviKJD) was generated with the following arguments (manually word-wrapped for readability):

    $ java WaveGen.Main 44100 300 "C4 A3 _  A3 C4 D4 G3 _ _ _ G4 A4 Bb4 F5 _
       F5 E5 C5 D5 C5 Bb4 A4 _ _ _ C5 D5 D5 D5 _ D5 G5 F5 E5 F5 D5 C5 _ _ _
       F4 G4 A4 D5 C5 C5 _ _ Bb5 A4 E4 F4 F4 _ _ _ C4 A3 _ A3 C5 D4 G3 _ _ _
       G4 A4 Bb4 F5 _ F5 E5 C5 D5 C5 Bb4 A4 _ _ _ C5 D5 D5 D5 _ D5 G5 F5 E5
       F5 D5 C5 _ _ _ F4 G4 A4 F5 F5 D5 _ C5 A4 F4 F4 F4 _ _ _" sawtooth > aPopularTune.wav
