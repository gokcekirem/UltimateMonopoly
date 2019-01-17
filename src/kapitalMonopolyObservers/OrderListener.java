package kapitalMonopolyObservers;

public interface OrderListener {

	public void onOrderEvent(Object source, String name, String value);
	
}
