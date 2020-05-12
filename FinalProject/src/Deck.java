// I used https://www.dreamincode.net/forums/topic/116864-how-to-make-a-poker-game-in-java/ by crazyjugglerdrummer
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Deck {
		ArrayList<Card> cards;
		public Deck() {
		
		cards = new ArrayList<Card>();
		int index_1, index_2;
		Random gen = new Random();
		Card temp;
		
		for(int s = 1; s <= 4; s++) {
			for(int r = 1; r <= 13; r ++) {
				cards.add(new Card(s, r));
			}
		}
		
		int size = cards.size()-1;
		
		for(int i = 0; i<100; i++) {
			index_1 = gen.nextInt(cards.size()-1);
			index_2 = gen.nextInt(cards.size()-1);
		
			temp = cards.get(index_2);
			cards.set(index_2, cards.get(index_1));
			cards.set(index_1, temp);
			
		}
		Collections.shuffle(this.cards);
	}

		public Card drawFromDeck() {
			return cards.remove(cards.size()-1);
		}
		
		public int getTotalCards() {
			return cards.size();
		}
		
		/*
		public void shuffle() {
			String[] suits = {"hearts", "diamonds", "clubs", "spades"};
			String[] ranks = {"Ace", "2","3","4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
			
			List<String> cards = new ArrayList<String>();
			for(int i = 0; i <= suits.length; i++) {
				for(int p = 0; p <= ranks.length; p++) {
					cards.add(suits[i] + " of " + ranks[p]);
				}
			}
			
			Collections.shuffle(cards);
			System.out.println("Enter the number of cards within: " + cards.size() + " = ");
			
			Scanner data = new Scanner(System.in);
			Integer inputString = data.nextInt();
			for(int q = 0; q <= inputString; q ++) {
				System.out.println(cards.get(q));
			}
		}
		*/
		
}	

