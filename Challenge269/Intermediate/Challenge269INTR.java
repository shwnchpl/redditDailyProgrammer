// Solution to the problem posted here:
// https://www.reddit.com/r/dailyprogrammer/comments/4m3ddb/20160601_challenge_269_intermediate_mirror/

import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

enum Heading {
	U, D, L, R
}

class MirrorKey {
	char[][] keyGrid;
	HashMap<Character,Character> encMap;
	
	public MirrorKey(char[][] kg) {
		keyGrid = kg;
		encMap = new HashMap<>();
	}
	
	public void calibrate() {		
		for(int cnt = 0; cnt < 4; cnt++)
			for(int i = 1; i < keyGrid[0].length - 1; i++) {
				int x = 0, y = 0;
				char inChar;
				Heading dir = Heading.D;
		
				switch(cnt) {
					case 0:  // top down
						x = i;
						y = 0;
						dir = Heading.D;
						break;
					case 1: // left to right
						x = 0;
						y = i;
						dir = Heading.R;
						break;
					case 2: // bottom up
						x = i;
						y = keyGrid.length - 1;
						dir = Heading.U;
						break;
					case 3: // right to left
						x = keyGrid[0].length - 1;
						y = i;
						dir = Heading.L;
						break;
				}
				
				inChar = keyGrid[y][x];
				
				do {
					switch(dir) {
						case D: y++; break;
						case U: y--; break;
						case L: x--; break;
						case R: x++; break;
					}
					
					if(keyGrid[y][x] == '\\') {
						switch(dir) {
							case U: dir = Heading.L; break;
							case D: dir = Heading.R; break;
							case L: dir = Heading.U; break;
							case R: dir = Heading.D; break;
						}	
					} else if(keyGrid[y][x] == '/') {
						switch(dir) {
							case U: dir = Heading.R; break;
							case D: dir = Heading.L; break;
							case L: dir = Heading.D; break;
							case R: dir = Heading.U; break;
						}
					}
				} while(keyGrid[y][x] == ' '
							|| keyGrid[y][x] == '/'
							|| keyGrid[y][x] == '\\');

				encMap.put(inChar, keyGrid[y][x]);
			}
	}
	
	public Character encChar(char c) {  // leave a space for anything that cannot be encoded
		Character retc;
		if((retc = encMap.get(c)) == null)
			retc = ' ';
		return retc;
	}
	
	public String encString(String s)  {
		return 	s.chars()
				.mapToObj(i -> (char)i)
				.map(c -> encChar(c).toString())
				.reduce("", String::concat);
	}
}

public class Challenge269INTR {	
	public static void main(String[] args) {		
		Scanner stdIn = new Scanner(System.in);
		ArrayList<String> mirrorDiag = new ArrayList<>();
		String[] sides = new String[4];
		
		System.out.println("Enter a mirror diagram (followed by ^D): ");

		while(stdIn.hasNext())
			mirrorDiag.add(stdIn.nextLine());
		stdIn = new Scanner(System.in);
				
		System.out.println("Enter (in order on seperate lines) the top, bottom, left, and right sides: ");

		for(int i = 0; i < 4; i++)
			sides[i] = stdIn.nextLine();
		
		int keySize = mirrorDiag.size() + 2;
		char[][] charKey = new char[keySize][keySize];
		
		charKey[0] = (" " + sides[0] + " ").toCharArray();
		for(int i = 1; i < keySize - 1; i++)
			charKey[i] = (sides[2].charAt(i - 1) + mirrorDiag.get(i - 1)
							+ sides[3].charAt(i - 1)).toCharArray();					
		charKey[keySize - 1] = (" " + sides[1] + " ").toCharArray();
		
		MirrorKey mKey = new MirrorKey(charKey);
		mKey.calibrate();
		
		System.out.println("Enter string to be encoded/decoded: ");
		System.out.println(mKey.encString(stdIn.nextLine()));
	}
}
