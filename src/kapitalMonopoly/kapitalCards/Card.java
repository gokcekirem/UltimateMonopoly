package kapitalMonopoly.kapitalCards;

import java.io.Serializable;

public abstract class Card implements Serializable{

	private boolean cardSavable;
	private String cardName;
	public abstract void doAction();

	public Card(String cardName) {
		this.cardName = cardName;
		cardSavable = false;
	}

	public Card(String cardName, boolean cardSavable) {
		this.cardName = cardName;
		this.cardSavable = cardSavable;
	}

	@Override
	public String toString(){
		return cardName;
	}
	
	public boolean isSavable() {
		return cardSavable;
	}

	public boolean isRollThreeCard() {
		return cardName.contains(RollThreeCard.getRoll3Path());
	}
	
	public boolean isChanceCard() {
		return cardName.contains(ChanceCard.getChanceCardPath());
	}
	
	public boolean isCommunityChestCard() {
		return cardName.contains(CommunityChestCard.getCommunityChestPath());
	}

	public void setSavable() {
		cardSavable = true;
	}
}
