package Game;

import java.util.ArrayList;
import java.util.Arrays;

public class AIPlayer {
	private PhysicalBoard linkedGame;
	private int playerTurn;

	public AIPlayer(PhysicalBoard game, int p) {
		linkedGame = game;
		linkedGame.linkAIPlayer(this);
		playerTurn = p;
		if (linkedGame.getPlayerTurn() == p)
			linkedGame.dropPiece(3);
	}

	public void move() {

		int[] scores = calculateBestMove(linkedGame, 1, linkedGame.getPlayerTurn(), 4);
		for (int i = 0; i < scores.length; i++)
			System.out.println(scores[i]);
		System.out.println("Best Score:" + findBestScore(scores));
		int column = findGoodMove(scores, findBestScore(scores));
		if (testImminentLoss(scores)) {
			int[] enemyMoves = calculateBestMove(linkedGame, 1, linkedGame.getPlayerTurn() * -1, 1);
			column = findGoodMove(enemyMoves, findBestScore(enemyMoves));
		}
		if (column < 0)
			do
				column = ((int) (Math.random() * 7));
			while (linkedGame.getPiecesPlaced()[column] >= linkedGame.ROWS);
		linkedGame.dropPiece(column);
		System.out.println("done.");
	}

	public int[] calculateBestMove(PlayingField p, int depth, int pT, int maxLayer) {
		int scores[] = new int[p.COLUMNS];
		// negative is loss, positive is win.
		PlayingField pI;
		for (int i = 0; i < p.COLUMNS; i++) {
			if (p.getPiecesPlaced()[i] < p.ROWS) {
				pI = new PlayingField(p.getSquareStatuses(), pT);
				int returned1;
				// System.out.println("Pieces Placed: " +
				// pI.getPiecesPlaced()[i]);

				returned1 = pI.dropPiece(i);
				if (returned1 != 0)
					scores[i] = depth;
				else if (Math.abs(depth) < maxLayer)
					scores[i] = findLargestThreat(
							calculateBestMove(pI, depth * -1 + (depth < 0 ? 1 : -1), pT * -1, maxLayer));
			} else {
				// System.out.println("Row filled");
				scores[i] = -999;
			}
		}
		return scores;
	}

	public int findLargestThreat(int[] array) {
		int minPos = 999;
		int maxNeg = -999;
		// boolean hasZero = false;
		for (int i = 0; i < array.length; i++) {
			if (array[i] > 0)
				minPos = Math.min(minPos, array[i]);
			else if (array[i] < 0)
				maxNeg = Math.max(maxNeg, array[i]);
			// else
			// hasZero = true;
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
			else if (array[i] < 0 && array[i] != -999)
				minNeg = Math.min(minNeg, array[i]);
			else if (array[i] == 0)
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
			// System.out.println("Temp: " + index);
			result = a[index];
			if (overflow >= 999)
				break;
			overflow++;
		} while (result != best || linkedGame.getPiecesPlaced()[index] >= linkedGame.ROWS);
		if (overflow < 999)
			return index;
		else
			return -1;
	}

	public boolean testImminentLoss(int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] != -2 && array[i] != -999)
				return false;
		}
		System.out.println("Imminent Loss Found");
		return true;
	}

	public void start() {
		if (linkedGame.getPlayerTurn() == playerTurn)
			move();
	}
}
