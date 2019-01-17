package kapitalMonopoly;

import kapitalBot.BotInterface;
import kapitalMonopolyObservers.AnimationMoveListener;
import kapitalMonopolyObservers.AnimationRollListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;



public class Animation implements Serializable {
	
	protected static final int SLEEP_TIME = 100;
	protected static final int STEP_NUMBER = 10;
	public static Thread moveThread;
	public static Thread diceThread;

	
	private static ArrayList<AnimationRollListener> animationRollListeners = new ArrayList<>();
	public void addAnimationRollListener(AnimationRollListener lis) {
		animationRollListeners.add(lis);
	}
	public void publishAnimationRollEvent(String name, int value1, int value2, int value3, int step, int step_number){
		for(int i=0; i<animationRollListeners.size();i++){
			animationRollListeners.get(i).onAnimationRollEvent(this, name, value1, value2, value3, step, step_number);
		}
	}

	private static ArrayList<AnimationMoveListener> animationMoveListeners = new ArrayList<>();
	public void addAnimationMoveListener(AnimationMoveListener lis) {
		animationMoveListeners.add(lis);
	}
	public void publishAnimationMoveEvent(String name, int currentPlayerId, int coordinateX, int coordinateY) {
		for(int i=0; i<animationMoveListeners.size();i++){
			animationMoveListeners.get(i).onAnimationMoveEvent(this, name, currentPlayerId, coordinateX, coordinateY);
		}
	}
	
	public synchronized void animateRollDice(int reg1, int reg2, int speed, Boolean evenDie) {
		Random rand = new Random();
		diceThread = new Thread(new Runnable() {
			public void run() {
				for(int j=1; j<STEP_NUMBER;j++){
					try {
						publishAnimationRollEvent("Animation Roll", rand.nextInt(8)+1, rand.nextInt(8)+1, rand.nextInt(8)+1, j, STEP_NUMBER);
						Thread.sleep(SLEEP_TIME);
					} catch (InterruptedException e) {
						System.out.println("There is a problem with thread in Animation");
					}
				}
				
				try {
					publishAnimationRollEvent("Animation Roll", reg1, reg2, speed, STEP_NUMBER, STEP_NUMBER);
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					System.out.println("There is a problem with thread in Animation");
				}
			}
		});
		diceThread.start();
	}

	public synchronized void animatePiece(ArrayList<int[]> movePath, Boolean evenDie, int currentPlayerId) {
		moveThread = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(SLEEP_TIME*STEP_NUMBER);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int coordinateX=0;
				int coordinateY=0;
				int tempX=0;
				int tempY=0;
				int nextX=0;
				int nextY=0;
				System.out.println("Movepath size : " + movePath.size());
				for(int i=0; i<movePath.size()-1;i++) {
					tempX=movePath.get(i)[0];
					tempY= movePath.get(i)[1];
					nextX = movePath.get(i+1)[0];
					nextY = movePath.get(i+1)[1];
					for(int j=1; j<STEP_NUMBER+1;j++) {
						coordinateX=tempX+j*(nextX-tempX)/STEP_NUMBER;
						coordinateY=tempY+j*(nextY-tempY)/STEP_NUMBER;
						try {
							Thread.sleep(SLEEP_TIME/STEP_NUMBER);
						} catch (InterruptedException e) {
							System.out.println("There is a problem with thread in Animation");
						}
						publishAnimationMoveEvent("Animation Move", currentPlayerId, coordinateX, coordinateY);
					}
				}
				DomainController.clearPiecePath(currentPlayerId);
				if(!evenDie) {
					if(MonopolyGame.getInstance().getCurrentPlayer().isBot()) {
						System.out.println("Current player: "+DomainController.getCurrentPlayerID());
						try {
							Thread.sleep(SLEEP_TIME*STEP_NUMBER);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if(((BotInterface) MonopolyGame.getInstance().getCurrentPlayer()).isAdmin()){
							MonopolyGame.updateCurrentPlayer();
						}
					}
				} else {
					if(MonopolyGame.getInstance().getCurrentPlayer().isBot()) {
						System.out.println("Current player: " + DomainController.getCurrentPlayerID());
						try {
							Thread.sleep(SLEEP_TIME*STEP_NUMBER*2);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						MonopolyGame.botAct();
					}
				}
			}
		});
		moveThread.start();
		
	}

	



}


