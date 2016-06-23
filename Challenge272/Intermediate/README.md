My solution in Java to [this problem](https://www.reddit.com/r/dailyprogrammer/comments/4paxp4/20160622_challenge_272_intermediate_dither_that/) (problem by [u/skeeto](https://www.reddit.com/user/skeeto)).

Applies error diffusion dithereing to an image file. Supports jpeg, png, gif, and anything else supported by the Java ImageIO and BufferedImage classes.  Supports several dithering algorithms.

Self contained documentation:

    $ java Challenge272INTR --help
    Usage: java Challenge272INTR <in_file> [out_file] [dithering_alg] [theshold #]
    
    * If no file name is specified, defaults to input_image.dithered.
    * If no algorithm is supplied, defaults to Floyd-Steinberg.
    * If no threshold value is suppied, defaults to the image mean.
    A number greater than 255 defaults to 255 and a number less than 0 defaults to 0.
    
    Supported algorithms:
    	Argument:	Full Algorithm Name:
    	fs		    Floyd-Steinberg [default]
    	jjn		    Jarvis, Judice, Ninke
    	stucki		Stucki
    	atkinson	Atkinson
    	burkes		Burkes
    	s3		    Sierra-3
    	s2		    2-Line Sierra
    	sl		    Sierra Lite
    	threshold	Threshold (average) -- no error diffusion

Sample inputs and outputs:

    $ java Challenge272INTR me.jpg floydsteinberg.jpg fs

![floydsteinberg.jpg](https://github.com/shwnchpl/redditDailyProgrammer/blob/master/Challenge272/Intermediate/sierra3.jpg?raw=true)

    $ java Challenge272INTR me.jpg sierra3.jpg s3

![sierra3.jpg](https://github.com/shwnchpl/redditDailyProgrammer/blob/master/Challenge272/Intermediate/sierra3.jpg?raw=true)

    $ java Challenge272INTR me.jpg threshold90.jpg 90
    
![theshold90.jpg](https://github.com/shwnchpl/redditDailyProgrammer/blob/master/Challenge272/Intermediate/threshold90.jpg?raw=true)
