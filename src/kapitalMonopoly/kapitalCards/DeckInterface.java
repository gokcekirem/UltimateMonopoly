package kapitalMonopoly.kapitalCards;

import kapitalMonopolyObservers.DrawListener;

public interface DeckInterface {

	public Card drawCard();
	public Card fDraw();
	public void addDrawListener(DrawListener lis);
	public void publishDrawEvent(String name, String value);
}
