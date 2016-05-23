// Solution to the problem posted here:
// https://www.reddit.com/r/dailyprogrammer/comments/4jom3a/20160516_challenge_267_easy_all_the_places_your/

class Challenge267EASY {
    static final String[] stNdRd = {"st", "nd", "rd"};
    
    static String ordinalFormat(int n) {
        switch(n % 10) {
            case 1: case 2: case 3:
                if(!((Math.abs(n  - 10) % 100) <= 3))
                    return n + stNdRd[(n % 10) - 1];
        }
        return n + "th";
    }
    
    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("Usage: Challenge267EASY [finished place] [# of contestants]\n" +
                                "Prints all of the places your dog did not finish in.");
            return;
        }
        
        final int place = Integer.parseInt(args[0]);
        
        for(int i = 0; i <= Integer.parseInt(args[1]); i++) {
            if(i == place) continue;
            System.out.print(ordinalFormat(i) + " ");
        }
        
        System.out.print("\n");
    }
}
