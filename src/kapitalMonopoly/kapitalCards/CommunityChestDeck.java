package kapitalMonopoly.kapitalCards;

import kapitalNetwork.Network;

import java.io.Serializable;
import java.util.ArrayList;

public class CommunityChestDeck extends Deck implements DeckInterface, Serializable {
	
	private static final String CARD_NAMES_FILE = "resources/CommunityChestCardNames.txt";
	
	private Deck communityChestDeck = new Deck();
	private ArrayList<String> cardNames;

	private int cardCount;
	
	public CommunityChestDeck() {
		cardNames = readCardFile(CARD_NAMES_FILE);
		cardCount = cardNames.size();
		for(int i=0;i<cardCount;i++) {
			CommunityChestCard communityChestCard = new CommunityChestCard(i,cardNames.get(i));
			communityChestDeck.putUnder(communityChestCard);
		}
		//communityChestDeck.shuffleDeck();
	}
	
	public CommunityChestCard drawCard() {
		CommunityChestCard temp = (CommunityChestCard) communityChestDeck.drawCard("Community Chest");
		publishDrawEvent("DrawCard", temp.toString());
		Network.getInstance().sendMessage("COMMUNITY_CHEST" + temp.toString());
		return temp;
	}
	
	public void putUnder(CommunityChestCard communityChestCard) {
		communityChestDeck.putUnder(communityChestCard);
	}

	@Override
	public Card fDraw() {
		// TODO Auto-generated method stub
		return null;
	}
}
