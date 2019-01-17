package kapitalMonopolyObservers;

public interface AnimationMoveListener {
	void onAnimationMoveEvent(Object source, String name, int currentPlayerId, int coordinateX, int coordinateY);
}