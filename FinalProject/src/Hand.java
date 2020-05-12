// I used https://www.dreamincode.net/forums/topic/116864-how-to-make-a-poker-game-in-java/ by crazyjugglerdrummer
public class Hand {
		private Card[]cards;
		private int[]value;
		
		Hand(Deck d){
		value = new int[6];
		cards = new Card[5];
		for(int i = 0; i < 5; i++) {
			cards[i] = d.drawFromDeck(); //fills up the cards array
		}
		//rest of code here
		int[] ranks = new int[14];
		int[] orderedRanks = new int[5];
		
		int sameCards = 1, sameCards2 = 0;
		int largeGroupRank = 0, smallGroupRank = 0;
		boolean flush = true, straight = false;
		int index = 0;
		int topStraightValue = 0;
		
		for(int i = 0; i <= 13; i++) {
			ranks[i] = 0;
		}
		
		for(int i = 0; i <= 4; i ++) {
			ranks[cards[i].getRank()]++;
		}
		
		for (int i = 0; i < 4; i++) {
			if ( cards[i].getSuit() != cards[i+1].getSuit() )
				flush=false;
		}
		
		for (int x=13; x>=1; x--) {
			if (ranks[x] > sameCards) {
				if (sameCards == 1) {
					largeGroupRank = x;
				}
				else {
					sameCards2 = sameCards;
					smallGroupRank = x;  
				}
				
				sameCards = ranks[x];
			} else if (ranks[x] > sameCards2) {
				sameCards2 = ranks[x];
				smallGroupRank = x;
				}
		}

		
		 if (ranks[1] == 1) { 		
			 orderedRanks[index]=14;
			 index++;
		}
		 
		 for (int i = 13; i >= 2; i--) {
			 if (ranks[i]==1){	
				 orderedRanks[index]=i;
				 index++;
			 }
		 }

		 for (int x=1; x<=9; x++) {
			 if (ranks[x]==1 && ranks[x+1]==1 && ranks[x+2]==1 && ranks[x+3]==1 && ranks[x+4]==1) {
				 straight=true;
				 topStraightValue=x+4;
				 break;
			 }
		 }
		 	if (ranks[10]==1 && ranks[11]==1 && ranks[12]==1 && ranks[13]==1 && ranks[1]==1) {
		 		straight=true;
		 		topStraightValue=14;
		 		}

		 	for (int x=0; x<=5; x++) {
		 		value[x]=0;
		 	}
		 	
		 	if ( sameCards==1 ) {
		 		value[0]=1;
		 		value[1]=orderedRanks[0];
		 		value[2]=orderedRanks[1];
		 		value[3]=orderedRanks[2];
		 		value[4]=orderedRanks[3];
		 		value[5]=orderedRanks[4];
		 		}

		 	if (sameCards==2 && sameCards2==1) {
		 		value[0]=2;
		 		value[1]=largeGroupRank;
		 		value[2]=orderedRanks[0];
		 		value[3]=orderedRanks[1];
		 		value[4]=orderedRanks[2];
		 	}
		 	if (sameCards==2 && sameCards2==2) {
		 		value[0]=3;
		 		value[1]= largeGroupRank>smallGroupRank ? largeGroupRank : smallGroupRank;
		 		value[2]= largeGroupRank<smallGroupRank ? largeGroupRank : smallGroupRank;
		 		value[3]=orderedRanks[0];
		 	}
		 	if (sameCards==3 && sameCards2!=2) {
		 		value[0]=4;
		 		value[1]= largeGroupRank;
		 		value[2]=orderedRanks[0];
		 		value[3]=orderedRanks[1];
		 	}
		 	if (straight && !flush) {
		 		value[0]=5;
		 		value[1]=topStraightValue;
		 	}
		 		        
		 	if (flush && !straight) {
		 		value[0]=6;
		 		value[1]=orderedRanks[0];
		 		value[2]=orderedRanks[1];
		 		value[3]=orderedRanks[2];
		 		value[4]=orderedRanks[3];
		 		value[5]=orderedRanks[4];
		 	}
		 	if (sameCards==3 && sameCards2==2) {
		 		value[0]=7;
		 		value[1]=largeGroupRank;
		 		value[2]=smallGroupRank;
		 	}
		 	if (sameCards==4) {
		 		value[0]=8;
		 		value[1]=largeGroupRank;
		 		value[2]=orderedRanks[0];
		 	}
		 	if (straight && flush) {
		 		value[0]=9;
		 		value[1]=topStraightValue;
		 	}
		}

		boolean display() {
			String s;
			switch( value[0] ) {
			case 1:
				s="high card";
				break;
			case 2:
				s="pair of " + Card.rankAsString(value[1]) + "\'s";
				break;
			case 3:
				s="two pair " + Card.rankAsString(value[1]) + " " + Card.rankAsString(value[2]);
				break;
			case 4:
				s="three of a kind " + Card.rankAsString(value[1]) + "\'s";
				break;
			case 5:
				s=Card.rankAsString(value[1]) + " high straight";
				break;
			case 6:
					s="flush";
					break;
			case 7:
				s="full house " + Card.rankAsString(value[1]) + " over " + Card.rankAsString(value[2]);
				break;
			case 8:
				s="four of a kind " + Card.rankAsString(value[1]);
				break;
			case 9:
				s="straight flush " + Card.rankAsString(value[1]) + " high";
				break;
				default:
					s="error in Hand.display: value[0] contains invalid value";
			}

			s = "               " + s;
			System.out.println(s);
			boolean isDisplay = true;
			return isDisplay;
		    }

	
		/*void displayAll() {
			for(int i = 0; i < 5; i++) {
				System.out.println(cards[i]);
			}
		}
		*/
		int compareTo(Hand t) {
			for(int i = 0; i < 6; i ++) {
				if(this.value[i] > t.value[i]) {
					return 1;
				}
				else if(this.value[i] < t.value[i]) {
					return -1;
				}
			}
			return 0;
		}
		
		public static void main(String[]args) {
			for(int i = 0; i < 10; i++) {
				Deck deck = new Deck();
				Hand hand = new Hand(deck);
				Hand hand2 = new Hand(deck);
				
				hand.display();
				//hand.displayAll();
				hand2.display();
				//hand2.displayAll();
				System.out.println(hand.compareTo(hand2));
			}
		}
}
