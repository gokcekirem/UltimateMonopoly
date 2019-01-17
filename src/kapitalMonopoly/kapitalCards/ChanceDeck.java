package kapitalMonopoly.kapitalCards;

import kapitalNetwork.Network;

import java.util.ArrayList;

public class ChanceDeck extends Deck implements DeckInterface {

	private static final String CHANCE_CARD_NAMES_FILE = "resources/ChanceCardNames.txt";
	
	private static Deck chanceDeck = new Deck();
	private static ArrayList<String> cardNames;
	
	private static int cardCount;
	
	public ChanceDeck() {
		cardNames = readCardFile(CHANCE_CARD_NAMES_FILE);
		cardCount = cardNames.size();
		for(int i=0;i<cardCount;i++) {
			ChanceCard chanceCard;
			chanceCard = new ChanceCard(i, cardNames.get(i));
			if (cardNames.get(i).equals("Get_Out_of_Jail_Free")){
				((kapitalMonopoly.kapitalCards.Card) chanceCard).setSavable();
			}
					
			chanceDeck.putUnder(chanceCard);
		}
		//chanceDeck.shuffleDeck();
	}
	
	public static String getChanceDeckPath() {
		return CHANCE_CARD_NAMES_FILE;
	}
	
	
	public ChanceCard drawCard() {
		ChanceCard temp = (ChanceCard) chanceDeck.drawCard("Chance");
		publishDrawEvent("DrawCard", temp.toString());
		Network.getInstance().sendMessage("CHANCE" + temp.toString());
		return temp;
	}
	
	
	public void putUnder(ChanceCard chanceCard) {
		chanceDeck.putUnder(chanceCard);
	}


	@Override
	public Card fDraw() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void putBack(String chanceCardName) {
		// TODO Auto-generated method stub
		cardNames = readCardFile(CHANCE_CARD_NAMES_FILE);
		cardCount = cardNames.size();
		for(int i=0;i<cardCount;i++) {
			ChanceCard chanceCard;
			chanceCard = new ChanceCard(i, cardNames.get(i));
			if (cardNames.get(i).contains(chanceCardName)){
				chanceDeck.putUnder(chanceCard);
				if (cardNames.get(i).contains("Get_Out_of_Jail"))
					chanceCard.setSavable();
				System.out.println("Put under "+chanceCard.toString());
				}
		}
	}

}
