// My solution in Java to the problem posted here:
// https://www.reddit.com/r/dailyprogrammer/comments/4oylbo/20160620_challenge_272_easy_whats_in_the_bag/

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.IntStream;


class NoTilesRemainingException extends Exception {
	public final String errorTile;
	
	public NoTilesRemainingException(String tile) {
		super();
		errorTile = tile;
	}
}
class InvalidTileException extends Exception {
	public final String errorTile;
	
	public InvalidTileException(String tile) {
		super();
		errorTile = tile;
	}
}

class ScrabbleTiles extends HashMap<String, Integer> {
	private static final String[] letters =
		{"A", "B", "C", "D", "E", "F", "G", "H", "I",
		 "J", "K", "L", "M", "N", "O", "P", "Q", "R",
		 "S", "T", "U", "V", "W", "X", "Y", "Z", "_"};
	private static final Integer[] quantities =
		{9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6,
		 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1, 2};
	
	public ScrabbleTiles() {
		super(IntStream.range(0, letters.length).boxed()
				.collect(Collectors.toMap(i -> letters[i], i -> quantities[i])));
	}
	
	ScrabbleTiles(String tiles) {
		Stream.of(tiles.split("")).forEach((t) ->
			put(t, containsKey(t) ? get(t) + 1 : 1));
	}
	
	public void use(String tileLetter) throws InvalidTileException, NoTilesRemainingException {
		try {
			int update;
			if((update = this.get(tileLetter)) > 0)
				put(tileLetter, --update);
			else
				throw new NoTilesRemainingException(tileLetter);
		} catch (NullPointerException e) {
				throw new InvalidTileException(tileLetter);
		}
	}
	
	private static int getMultiplier(String tile) {
		switch(tile) {
			case "_": return 0;
			case "D": case "G":	return 2;
			case "B": case "C": case "M": case "P":	return 3;
			case "F": case "H": case "V": case "W": case "Y": return 4;
			case "K": return 5;
			case "J": case "X": return 8;
			case "Q": case "Z": return 10;
			default: return 1;
		}
	}
	
	public int getScore() {
		return keySet().stream().map((n) -> getMultiplier(n) * get(n)).reduce(0, (a, b) -> a + b);
	}
		
	@Override
	public String toString() {
		return (new HashMap<>(this.entrySet().stream()
							.collect(Collectors.groupingBy(Map.Entry::getValue))
								.values()
								.stream()
									.collect(Collectors.toMap(
										item -> item.get(0).getValue(),
										item -> new ArrayList<>(item.stream()
											.map(Map.Entry::getKey)
											.collect(Collectors.toList()))))))
												.entrySet()
												.stream()
												.map((e) -> e.getKey() + ": " + e.getValue())
												.reduce("", (a, b) -> a + "\n" + b);
	}
}

public class Challenge272EASY {
	public static void main(String[] args) {
		ScrabbleTiles tiles = new ScrabbleTiles();
		
		try {
			for(String tile : args[0].split(""))
				tiles.use(tile);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Usage: java Challenge272EASY <used scrabble tile string>");
			System.exit(1);
		} catch (InvalidTileException e) {
			System.err.println("Invalid Scrabble tile in used tile string.\n"
								+ e.errorTile + " is not a valid Scrabble Tile.");
			System.exit(1);
		} catch (NoTilesRemainingException e) {
			System.err.println("Invalid input. More " + e.errorTile 
								+ "'s have been taken from the bag than possible.");
			System.exit(1);
		}
		
		ScrabbleTiles used = new ScrabbleTiles(args[0]);
		
		System.out.println("Tiles still in the bag: " + tiles + "\n"
			+ tiles.getScore() + " points remain in the bag.\nTiles used:" + used
			+ "\n" + used.getScore() + " points are in play.");
	}
}
