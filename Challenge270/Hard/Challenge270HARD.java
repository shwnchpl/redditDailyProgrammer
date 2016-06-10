// My solution to the problem posted here
// https://www.reddit.com/r/dailyprogrammer/comments/4nga90/20160610_challenge_270_hard_alien_invasion/

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.stream.Collectors;

public class Challenge270HARD {
	public static void main(String[] args) {
		Boolean[][] fieldDiagram = (new BufferedReader(
				             new InputStreamReader(System.in)))
						.lines()
						.map(Object::toString)
						.map(l ->
							l.chars()
							.mapToObj(i -> (char)i)
							.map(i -> (i == '-'))
							.collect(Collectors.toList())
							.toArray(new Boolean[0]))
						.collect(Collectors.toList())
						.toArray(new Boolean[0][0]);

		int diagramSize = fieldDiagram.length;  // making an asumption here that input is valid
		int maxSquareDim = 0;
		
		for(int initialY = 0; initialY < diagramSize - maxSquareDim; initialY++)
			for(int initialX = 0; initialX < diagramSize - maxSquareDim; initialX++) {
				int offset;
				square_search:
				for(offset = 1; ((initialX + offset) < diagramSize)
						&& ((initialY + offset) < diagramSize); offset++)
					for(int i = 0; i <= offset; i++)
						for(int j = 0; j <= offset; j++)
							if(!fieldDiagram[initialY + i][initialX + j])
								break square_search;
				if(offset > maxSquareDim) maxSquareDim = offset;
			}

		System.out.println(maxSquareDim * maxSquareDim + " dropships!");
	}
}
