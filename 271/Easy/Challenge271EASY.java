// My solution in Java to the problem posted here:
// https://www.reddit.com/r/dailyprogrammer/comments/4nvrnx/20160613_challenge_271_easy_critical_hit/

public class Challenge271EASY {
	public static void main(String[] args) {
		double sides = 1;
		double health = 1;
				
		try {
			sides = (double)Integer.parseInt(args[0]);
			health = (double)Integer.parseInt(args[1]);
		} catch (Exception e) {
			System.err.println("Prints probability of killing an enemy with a ce rtain amount of health\nremaining" +
				" given a roll of an N-sided die with the possibility of a critical hit.\n" +
					"Usage: java Challenge271EASY <# of sides> <enemy health>");
			System.exit(1);
		}
				
		System.out.println(Math.pow(1.0 / sides, Math.floor(health/sides)) * 
			((health % sides != 0) ? (((sides - (health % sides)) + 1)/sides) : 1));
	}
}
