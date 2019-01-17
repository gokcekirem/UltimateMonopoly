package kapitalMonopolyObservers;


public interface TurnListener {

	public void onTurnEvent(Object source, String name, int playerId);
}
