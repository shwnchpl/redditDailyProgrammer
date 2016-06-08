// My solution to the problem posted here
// https://www.reddit.com/r/dailyprogrammer/comments/4n6hc2/20160608_challenge_270_intermediate_generating/


import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class strTuple {
	private String first;
	private String second;
	
	public strTuple(String f, String s) {
		first = f;
		second = s;
	}
	
	public String toString() {
		return "(" + first + ", " + second + ")";
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object tup) {
		return toString().equals(((strTuple)tup).toString());
	}
}

public class Challenge270INTR {
	public static void main(String[] args) {
		HashMap<strTuple, ArrayList<String>> mFreq = new HashMap<>();

		String[] inputWords = (new BufferedReader(
					new InputStreamReader(System.in)))
						.lines()
						.map(Object::toString)
						.collect(Collectors.joining(" "))
						.split("\\s+");
		
		for(int i = 1; i < inputWords.length - 1; i++) {
			strTuple tup;
			
			if(mFreq.containsKey((tup = new strTuple(inputWords[i - 1], inputWords[i]))))
				mFreq.get(tup).add(inputWords[i + 1]);
			else
				mFreq.put(tup, new ArrayList<String>(Arrays.asList(inputWords[i + 1])));		
		}
		
		mFreq.put(new strTuple(inputWords[inputWords.length - 2], inputWords[inputWords.length - 1]), null);
		
		ArrayList<String> output = new ArrayList<>(Arrays.asList(inputWords[0], inputWords[1]));
		
		Random rn = new Random();
		ArrayList<String> next;
		
		for(int i = 1; (next = mFreq.get(new strTuple(output.get(i - 1), output.get(i)))) != null; i++)
			output.add(next.get(rn.nextInt(next.size())));
		
		for(String s : output)
			System.out.print(s + " ");
		System.out.println("");
	}
}
