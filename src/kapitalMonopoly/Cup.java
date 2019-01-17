package kapitalMonopoly;

import kapitalMonopoly.MonopolyGame.CupInputs;
import kapitalMonopolyObservers.MoveListener;
import kapitalMonopolyObservers.RollListener;
import kapitalNetwork.Network;

import java.io.Serializable;
import java.util.ArrayList;

public class Cup implements Serializable{

	//Observer pattern starts here:
	private static ArrayList<RollListener> rollListeners = new ArrayList<>();
	private static ArrayList<MoveListener> moveListeners = new ArrayList<>();
	
	int[] rollingSequence = new int[3];

	public void addRollListener(RollListener lis) {
		rollListeners.add(lis);
	}

	public void addMoveListener(MoveListener lis) {
		moveListeners.add(lis);
	}

	public void publishPropertyEventRoll(String name, String value){
		for(int i=0; i<rollListeners.size();i++){
			rollListeners.get(i).onRollEvent(this, name, value);
		}
	}
	public void publishPropertyEventMove(String name, String value){
		System.out.println("Moving indeed");
		System.out.println(MonopolyGame.getInstance().getTurn());
		for(int i=0; i<moveListeners.size();i++){
			moveListeners.get(i).onMoveEvent(this, name, value);
		}
	}

	//Observer pattern finished

	RegularDie r1;
	RegularDie r2;
	RegularDie r3;

	SpeedDie s1;

	public Cup() {
		r1 = new RegularDie();
		r2 = new RegularDie();
		r3 = new RegularDie();

		s1 = new SpeedDie();
	}

	public int[] getRRSDice() {
		return rollingSequence;
	}

	@SuppressWarnings("static-access")
	public int[] rollDice(CupInputs cupInput) {
		
		int reg1 = 0;
		int reg2 = 0;
		int reg3 = 0;
		int speed = 0;

		switch(cupInput){
		case RRS:
//			reg1 = r1.rollDie();
//			reg2 = r2.rollDie();
//			speed = s1.rollDie();

			System.out.print("Enter roll:");
			String input = System.console().readLine();
			reg1 = Integer.parseInt(input.charAt(0) + "");
			reg2 = Integer.parseInt(input.charAt(1) + "");
			speed = Integer.parseInt(input.charAt(2) + "");

			rollingSequence[0] = reg1;
			rollingSequence[1] = reg2;
			rollingSequence[2] = speed;
			MonopolyGame.getInstance().getAnimation().animateRollDice(reg1, reg2, speed, (reg1==reg2));
			/*ArrayList<int[]> movePath= DomainController.getPiecePath(DomainController.getCurrentPlayerID());
			int currentPlayerId = DomainController.getCurrentPlayerID();
			MonopolyGame.getInstance().getAnimation().animatePiece(movePath, reg1==reg2, currentPlayerId);*/
			publishPropertyEventRoll("RollDice", 
					Integer.toString(reg1)+Integer.toString(reg2)+Integer.toString(speed));
			publishPropertyEventMove("MovePlayer", 
					Integer.toString(reg1)+Integer.toString(reg2)+Integer.toString(speed));			
			String value = Integer.toString(reg1)+Integer.toString(reg2)+Integer.toString(speed);
			Network.getInstance().sendMessage("ROLLED"+value);
			break; 
		case RRR: //Called by Roll Three 
			reg1 = r1.rollDie();
			reg2 = r2.rollDie();
			reg3 = r3.rollDie();
			rollingSequence[0] = reg1;
			rollingSequence[1] = reg2;
			rollingSequence[2] = reg3;
			break; 
		case RR: 
			reg1 = r1.rollDie();
			reg2 = r2.rollDie();
			rollingSequence[0] = reg1;
			rollingSequence[1] = reg2;
			rollingSequence[2] = 0;

			value = Integer.toString(reg1)+Integer.toString(reg2)+Integer.toString(speed);
			publishPropertyEventRoll("RollDice", value);

			if ((reg1 == reg2) && (MonopolyGame.getInstance().getCurrentPlayer().inJailTurn < 3)) {
				MonopolyGame.getInstance().getCurrentPlayer().setFreeFromJail();
				MonopolyGame.getInstance().getCurrentPlayer().setFreeFromJail();
				System.out.print("rolled double to get out of jail");
				MonopolyGame.getInstance().getCurrentPlayer().inJailTurn=0;
				publishPropertyEventMove("MovePlayer", 
						Integer.toString(reg1)+Integer.toString(reg2)+Integer.toString(speed));		
			}else if(MonopolyGame.getInstance().getCurrentPlayer().inJailTurn == 3) {
				MonopolyGame.getInstance().getCurrentPlayer().payJailBail();
				System.out.print("couldnt roll double 3 times, so paid the bail");
				MonopolyGame.getInstance().getCurrentPlayer().inJailTurn=0;
			}
			else {
				MonopolyGame.getInstance().getCurrentPlayer().inJailTurn++;
			}
			Network.getInstance().sendMessage("ROLLED" + value);
			break; 
		case R: 
			reg1 = r1.rollDie();
			//rollingSequence[0] = reg1;
			break; 	
		}

		return rollingSequence;


	}


}

