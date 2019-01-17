package kapitalMonopoly.kapitalCards;

import kapitalMonopoly.MonopolyGame;

import java.io.Serializable;
import java.util.ArrayList;

public class RollThreeDeck extends Deck implements DeckInterface, Serializable {

	private static final String CARD_NAMES_FILE = "resources/RollThreeCardNames.txt";
	
	private Deck rollThreeDeck = new Deck();
	private ArrayList<String> cardNames;

	int cardCount;
	
	public RollThreeDeck() {
		cardNames = readCardFile(CARD_NAMES_FILE);
		cardCount = cardNames.size();
		for(int i=0;i<cardCount;i++) {
			System.out.println(i);
			RollThreeCard rollThreeCard = new RollThreeCard(i, cardNames.get(i));
			rollThreeDeck.putUnder(rollThreeCard);
		}
		rollThreeDeck.shuffleDeck();
	}

	@SuppressWarnings("static-access")
	public RollThreeCard drawCard() {
		RollThreeCard temp = (RollThreeCard) rollThreeDeck.drawCard("Roll Three Deck");
		publishDrawEvent("DrawCard", temp.toString());
		MonopolyGame.getInstance().getCurrentPlayer().saveCard(temp);
		return temp;
	}
	
	public Deck getRoll3Deck(){
		return rollThreeDeck;
	}
	
	public ArrayList<String> getCardNames(){
		return cardNames;
	}
	

	public void putUnder(RollThreeCard rollThreeCard) {
		rollThreeDeck.putUnder(rollThreeCard);
	}

	@Override
	public Card fDraw() {
		RollThreeCard temp = (RollThreeCard) rollThreeDeck.drawCard("Roll Three Deck");
		// TODO Auto-generated method stub
		return temp;
	}
	
}
