package kapitalMonopoly.kapitalCards;

import kapitalMonopoly.MonopolyGame;
import kapitalMonopolyObservers.DrawListener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("hiding")
public class Deck extends ArrayList<Card> implements Serializable{

	private int total;

	private static final Long SEED = 123L;

	static ArrayList<DrawListener> drawListeners = new ArrayList<DrawListener>();

	private Node first, last;

	private class Node implements Serializable{
		private Card card;
		private Node next;
	}

	public void putUnder(Card usedCard){
		Node current = last;
		last = new Node();
		last.card = usedCard;

		if (total++ == 0) first = last;
		else current.next = last;

	}

	public Card drawCard(String type){
		if (total == 0) throw new java.util.NoSuchElementException();
		Card cardPulled = first.card;
		if(type == "Chance") {
			ChanceCard pulledCard = (ChanceCard) first.card;
			
			pulledCard.doAction();
		}else if(type == "Community Chest") {
			CommunityChestCard pulledCard = (CommunityChestCard) first.card;
			pulledCard.doAction();
		}else {
			RollThreeCard pulledCard = (RollThreeCard) first.card;
			pulledCard.doAction();
		}

		first = first.next;
		if (--total == 0) last = null;
		
		if(type == "Chance" && ((kapitalMonopoly.kapitalCards.Card) cardPulled).isSavable()) { // Community chestlere de iremin sozu var (irem has promised)
			MonopolyGame.getInstance().getCurrentPlayer().saveCard((kapitalMonopoly.kapitalCards.Card) cardPulled);
		}else {
			this.putUnder(cardPulled);
		}

		return cardPulled;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		Node tmp = first;
		while (tmp != null) {
			sb.append(tmp.card.toString()).append(", ");
			tmp = tmp.next;
		}
		return sb.toString();
	}

	public void shuffleDeck(){
		ArrayList<Card> cardArray = new ArrayList<Card>();

		while(total!=0) {
			Card card = first.card;
			first = first.next;
			if (--total == 0) last = null;
			cardArray.add(card);
		}

		total = 0;

		while(!cardArray.isEmpty()) {
			Card randomCard = pickRandom(cardArray);
			cardArray.remove(randomCard);
			this.putUnder(randomCard);
		}
	}

	public boolean isEmpty(){
		return total == 0;
	}

	public void addDrawListener(DrawListener lis) {
		drawListeners.add(lis);
	}

	public void publishDrawEvent(String name, String value){
		for(int i=0; i<drawListeners.size();i++){
			drawListeners.get(i).onDrawEvent(this, name, value);
		}
	}
	// Observer end

	private Card pickRandom(ArrayList<Card> cardArray) {
		Random rnd = new Random(SEED);
		int rndIndex = rnd.nextInt(cardArray.size());
		return cardArray.get(rndIndex);
	}

	public static ArrayList<String> readCardFile(String fileName) {
		ArrayList<String> filePaths = new ArrayList<String>();
		try {
			FileReader file = new FileReader(fileName);
			BufferedReader br = new BufferedReader(file);

			String name;
			while( (name=br.readLine()) != null ){
				filePaths.add(name);
			}
			file.close();
			br.close();
		}
		catch (IOException e) {
			System.out.println("The file is not a valid format for the reader!");

		}
		return filePaths;

	}
}
