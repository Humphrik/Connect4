package Game;

import java.util.ArrayList;
import java.util.Arrays;

public class AIPlayer {
	private PhysicalBoard linkedGame;
	private int playerTurn;
	private int MAX_LAYER = 2;

	public AIPlayer(PhysicalBoard game, int p) {
		linkedGame = game;
		linkedGame.linkAIPlayer(this);
		playerTurn = p;
		if (linkedGame.getPlayerTurn() == p)
			this.start();
	}

	public void move() {
		int[] scores = calculateBestMove(linkedGame, 1, linkedGame.getPlayerTurn());
		for (int i = 0; i < scores.length; i++)
			System.out.println(scores[i]);
		System.out.println("Best Score:" + findBestScore(scores));
		int column = findGoodMove(scores, findBestScore(scores));
		if (column < 0)
			do
				column = ((int) (Math.random() * 7));
			while (linkedGame.getPiecesPlaced()[column] >= linkedGame.ROWS);
		linkedGame.dropPiece(column);
		System.out.println("done.");
		//// System.out.println("Placing at: " + column + ", " +
		//// linkedGame.getPiecesPlaced()[column]);
	}

	public int[] calculateBestMove(PlayingField p, int depth, int pT) {
		int scores[] = new int[p.COLUMNS];
		// negative is loss, positive is win.
		// int[] scores = new int[p.getDimensions()[0]];
		PlayingField pI;
		//System.out.println(depth);
		for (int i = 0; i < p.COLUMNS; i++) {
			if (p.getPiecesPlaced()[i] < p.ROWS) {
				pI = new PlayingField(p.getSquareStatuses(), pT);
				int returned1 = pI.dropPiece(i);
				if (returned1 != 0)
					scores[i] = depth;
				else if(Math.abs(depth) < MAX_LAYER)
					scores[i] = findLargestThreat(calculateBestMove(pI, depth*-1 + (depth < 0 ? 1:-1), pT * -1));
			}
		}
		/*
		for (int i = 0; i < p.COLUMNS; i++) {
			if (p.getPiecesPlaced()[i] < p.ROWS) {
				pI = new PlayingField(p.getSquareStatuses(), pT*-1);
				int returned1 = pI.dropPiece(i);
				if (returned1 != 0 && scores[i] == 0)
					scores[i] = -depth;
			}
		}
		*/
		return scores;
	}

	public int findLargestThreat(int[] array) {
		int minPos = 999;
		int maxNeg = -999;
		//boolean hasZero = false;
		for (int i = 0; i < array.length; i++) {
			if (array[i] > 0)
				minPos = Math.min(minPos, array[i]);
			else if (array[i] < 0)
				maxNeg = Math.max(maxNeg, array[i]);
			//else
				//hasZero = true;
		}
		if (minPos == 1)
			return minPos;
		else if (maxNeg == -1)
			return maxNeg;
		else if (maxNeg != -999)
			return maxNeg;
		else if (minPos != 999)
			return minPos;
		else
			return 0;
	}
	
	public int findBestScore(int[] array) {
		int minPos = 999;
		int minNeg = 0;
		boolean hasZero = false;
		for (int i = 0; i < array.length; i++) {
			if (array[i] > 0)
				minPos = Math.min(minPos, array[i]);
			else if (array[i] < 0)
				minNeg = Math.min(minNeg, array[i]);
			else
				hasZero = true;
		}
		if (minPos == 1)
			return minPos;
		else if (minNeg == -1)
			return minNeg;
		else if (minPos != 999)
			return minPos;
		else if (hasZero)
			return 0;
		else
			return minNeg;
	}

	public int findGoodMove(int[] a, int best) {
		int result = 0;
		int index = 0;
		int overflow = 0;
		do {
			index = (int) (Math.random() * a.length);
			//System.out.println("Temp: " + index);
			result = a[index];
			if(overflow >= 999)
				break;
			overflow++;
		} while (result != best || linkedGame.getPiecesPlaced()[index] >= linkedGame.ROWS);
		if(overflow < 999)
			return index;
		else
			return -1;
	}

	public void start() {
		if (linkedGame.getPlayerTurn() == playerTurn)
			move();
	}
}
