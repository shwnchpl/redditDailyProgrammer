// My solution to the problem posted here
// https://www.reddit.com/r/dailyprogrammer/comments/4msu2x/challenge_270_easy_transpose_the_input_text/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Challenge270EASY {
	public static void main(String[] args) {
		int maxLen = 0;
		
		String[] inputArray = (new BufferedReader(
					new InputStreamReader(System.in)))
						.lines()
						.collect(Collectors.toList())
						.toArray(new String[0]);
		
		for(String s : inputArray) if(s.length() > maxLen) maxLen = s.length();

		for(int i = 0; i < maxLen; i++) {
			for(int j = 0; j < inputArray.length; j++)
				try { System.out.print(inputArray[j].charAt(i)); } 
				catch (StringIndexOutOfBoundsException e) { System.out.print(' '); }
			System.out.print('\n');
		}
	}
}
