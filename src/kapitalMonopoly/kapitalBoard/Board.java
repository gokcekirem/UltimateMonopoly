package kapitalMonopoly.kapitalBoard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import kapitalMonopolyObservers.ConstructionListener;


public class Board implements Serializable {

	private final static int TOKEN_COUNT_FOR_DEEDS = 6;
	private final static String TOKEN_SEPARATOR = "-";
	private final static String SQUARE_BORDERS_FILE = "resources/SquarePositions.txt";
	private final static String SQUARES_LIST_FILE = "resources/SquaresList.txt";
	private final static int CENTER_TRACK_SQUARE_END_LINE = 40;
	private final static int INNER_TRACK_SQUARE_END_LINE = 64;

	private static HashMap<String,Square> squareMap = new HashMap<>(); // Square Name -> Square
	private static HashMap<String, String> squareTrackMap = new HashMap<>(); // Square Name -> Square

	private static Board board;

	public static synchronized Board getInstance(){
		if(board == null) {
			board = new Board();
			HashMap<String, int[]> squareBorders = readSquareBorders();
			readSquareFile(squareBorders);
		}
		return board;
	}
	

	public void addConstructionListener(ConstructionListener constructionListener) {
		for(String squareName : squareMap.keySet()){
			if(squareMap.get(squareName).isDeedSquare()) {
				((DeedSquare)squareMap.get(squareName)).addConstructionListener(constructionListener);
			}
		}
	}


	private static HashMap<String, int[]> readSquareBorders(){

		HashMap<String, int[]> squareBorders = new HashMap<>(); // Square Name -> Square Coordinates (x1,y1,x2,y2)

		try {
			FileReader file = new FileReader(SQUARE_BORDERS_FILE); 
			BufferedReader br = new BufferedReader(file);
			String line;
			while( (line=br.readLine()) != null ){
				String[] tokens = line.split(TOKEN_SEPARATOR);
				String name = tokens[0].trim();
				int[] borders = new int[4]; // Format: (x1,y1,x2,y2)
				for(int i=0; i<4; i++) {
					borders[i] = Integer.parseInt(tokens[i+1].trim());
				}
				squareBorders.put(name, borders);
			}
			file.close();
			br.close();
		} catch (IOException e) {
			System.out.println("The file is not a valid format for the reader!");
		}
		return squareBorders;
	}


	private static void readSquareFile(HashMap<String, int[]> squareBorders) {
		try {
			FileReader file = new FileReader(SQUARES_LIST_FILE); 
			//feed the FileReader into a BufferedReader to read line by line
			BufferedReader br = new BufferedReader(file);

			String line;

			//reads file until there are no more lines, using a null check
			int lineCount = 1;
			while( (line=br.readLine()) != null ){
				String[] tokens = line.split(TOKEN_SEPARATOR);
				// Format: Square's Name - NextSquare's Name - PreviousSquare's Name
				String name = tokens[0].trim();
				String nextName = tokens[1].trim();
				String nextName2 = tokens[2].trim();
				String prevName = tokens[3].trim();

				int[] borders;
				if(squareBorders.containsKey(name)) {
					borders = squareBorders.get(name);
				} else {
					System.out.println("Cannot find borders for SQUARE: " + name);
					borders = new int[4];
				}

				if(tokens.length < TOKEN_COUNT_FOR_DEEDS) {
					//System.out.println(name + "ACTION SQUARE");
					squareMap.put(name, new ActionSquare(name, nextName, nextName2, prevName, borders));
				} else {
					//System.out.println(name + "DEED SQUARE");
					Double price = Double.parseDouble(tokens[4].trim());
					System.out.println(price.toString());
					String color = tokens[5].trim();
					squareMap.put(name, new DeedSquare(name, color, price , nextName, nextName2, prevName, borders));
				}
				String trackType;
				if(lineCount < CENTER_TRACK_SQUARE_END_LINE+1){
					trackType = "CenterTrack";
				} else if(lineCount < INNER_TRACK_SQUARE_END_LINE+1){
					trackType = "InnerTrack";
				} else{
					trackType = "OuterTrack";
				}
				squareTrackMap.put(name,trackType);
				lineCount++;
			}
			file.close();
			br.close();
		}
		catch (IOException e) {
			System.out.println("The file is not a valid format for the reader!");

		}
	}

	public static Square getSquare(String squareName) {
		return squareMap.get(squareName);
	}

	public Square getNextSquare(String squareName) { // Query with a square's name
		return getNextSquare(squareMap.get(squareName));
	}

	public Square getNextSquare(Square currentSquare) {  // Query with a square
		return this.getSquare(currentSquare.getNextSquareName());
	}

	public Square getNextSquare2(Square currentSquare) {  // Query with a square
		return this.getSquare(currentSquare.getNextSquare2Name());
	}

	public Square getPrevSquare(String squareName) { // Query with a square's name
		return getPrevSquare(squareMap.get(squareName));
	}

	public Square getPrevSquare(Square currentSquare) { // Query with a square
		return this.getSquare(currentSquare.getPrevSquareName());
	}

	public HashMap<String,Square> getSquareMap(){
		return squareMap;
	}

	public void setSquareMap(HashMap<String,Square> sqmap){
		squareMap = sqmap;
	}

	public String getTrackType(Square square){
		return squareTrackMap.get(square.getName());
	}

	public static int getColorGroupPropertyNumber(String wantedColor) {
		HashMap<String, int[]> squareBorders = readSquareBorders();
		int i=0;
		try {
			FileReader file = new FileReader(SQUARES_LIST_FILE); 
			//feed the FileReader into a BufferedReader to read line by line
			BufferedReader br = new BufferedReader(file);

			String line;

			//reads file until there are no more lines, using a null check
			
			while( (line=br.readLine()) != null ){
				String[] tokens = line.split(TOKEN_SEPARATOR);
				// Format: Square's Name - NextSquare's Name - PreviousSquare's Name
				String name = tokens[0].trim();
				String nextName = tokens[1].trim();
				String nextName2 = tokens[2].trim();
				String prevName = tokens[3].trim();

				if(squareBorders.containsKey(name)) {
					//nothing
				} else {
					System.out.println("Cannot find borders for SQUARE: " + name);
				}

				if(tokens.length < TOKEN_COUNT_FOR_DEEDS) {
					//nothing
				} else {
					String color = tokens[5].trim();
					if (color.equals(wantedColor)) {
						i++;	
					}
				}
				
			}
			file.close();
			br.close();
		}
		catch (IOException e) {
			System.out.println("The file is not a valid format for the reader!");

		}
		return i;
	}
}
