//I used https://www.dreamincode.net/forums/topic/116864-how-to-make-a-poker-game-in-java/ by crazyjugglerdrummer
public class Card {
	private int suit, rank;
	
	private static String[] suits = {"Diamonds", "Hearts", "Spades", "Clubs"};
	private static String[] ranks = {"Ace", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "Jack", "Queen", "King"};
	
	public Card(int number, int suit) {
		this.rank = number;
		this.suit = suit;
	}

	public static String rankAsString(int _rank) {
		return ranks[_rank];
	}
	public String toString() {
		return ranks[rank] + " of " + suits[suit];
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getSuit() {
		return suit;
	}
	
	public void deal() {
		int[] deck = new int[52];
		for(int i = 0; i < 52; i++) {
			String suit = suits[deck[i] / 13];
			String rank = ranks[deck[i] % 13];
			System.out.println(rank + " of " + suit);
		}
	}
}


