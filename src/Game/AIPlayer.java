package Game;

import java.util.ArrayList;
import java.util.Random;

public class AIPlayer {
	private PhysicalBoard linkedGame;
	private int playerTurn;

	public AIPlayer(PhysicalBoard game, int p) {
		linkedGame = game;
		linkedGame.linkAIPlayer(this);
		// Construct AI with a linked Phyiscal Board.
		playerTurn = p;
		if (linkedGame.getPlayerTurn() == p)
			// Always drop in the middle first if player 1.
			linkedGame.dropPiece(3);
	}

	public void move() {
		// The "main" method of the AI: decides on a move and then performs it.
		int[] scores = calculateBestMove(linkedGame, 1, linkedGame.getPlayerTurn(), 6);
		// Finds worst-case scenario for each branch of movements.
		for (int i = 0; i < scores.length; i++)
			System.out.println(scores[i]);
		System.out.println("Best Score:" + findBestScore(scores));
		int column = findGoodMove(scores, findBestScore(scores));
		// Finds a valid column for the best possible score.
		if (testImminentLoss(scores)) {
			// If the AI is about to lose...
			int[] enemyMoves = calculateBestMove(linkedGame, 1, linkedGame.getPlayerTurn() * -1, 1);
			column = findGoodMove(enemyMoves, findBestScore(enemyMoves));
			// ... Do what the opponent would do to win.
		}
		if (column < 0)
			// If there was no valid moves
			// (Hopefully, if the logic is correct, this should never be true.)
			do
				column = normalRandom(linkedGame.COLUMNS / 2.0, 1, 0, linkedGame.COLUMNS);
			while (linkedGame.getPiecesPlaced()[column] >= linkedGame.ROWS);
		linkedGame.dropPiece(column);
		// Make the determined "best" move.
		System.out.println("done.");
	}

	public int[] calculateBestMove(PlayingField p, int depth, int pT, int maxLayer) {
		int scores[] = new int[p.COLUMNS];
		// Negative is loss, positive is win.
		PlayingField pI;
		for (int i = 0; i < p.COLUMNS; i++) {
			// For every column....
			if (p.getPiecesPlaced()[i] < p.ROWS) {
				pI = new PlayingField(p.getSquareStatuses(), pT);
				// Make a new playing field for each open column.
				int returned;
				returned = pI.dropPiece(i);
				// Drop a piece in given column.
				// Usually, but not always, corresponding player's turn.
				if (returned != 0)
					scores[i] = depth;
				// Did you win or lose? Return number of turns taken.
				else if (Math.abs(depth) < maxLayer)
					// If no win/loss, and not max level
					// find the worst-case scenario of the next level.
					scores[i] = findLargestThreat(
							calculateBestMove(pI, depth * -1 + (depth < 0 ? 1 : -1), pT * -1, maxLayer));
				// Recursive ;-)
			} else {
				scores[i] = -999;
				// Row is full? Make it -999
			}
		}
		return scores;
	}

	public int findLargestThreat(int[] array) {
		// Picks the MOST DANGEROUS move to NOT take.
		int minPos = 999;
		int maxNeg = -999;
		// Technically the negative with the least magnitude.
		for (int i = 0; i < array.length; i++) {
			if (array[i] > 0)
				minPos = Math.min(minPos, array[i]);
			else if (array[i] < 0)
				maxNeg = Math.max(maxNeg, array[i]);
		}
		if (minPos == 1)
			return minPos;
		else if (maxNeg == -2)
			return maxNeg;
		else if (maxNeg != -999)
			return maxNeg;
		else if (minPos != 999)
			return minPos;
		else
			return 0;
		// Priority: Immediate win, immediate loss,
		// earliest loss, earliest win, neutral.
	}

	public int findBestScore(int[] array) {
		// Picks the BEST move to take.
		int minPos = 999;
		int minNeg = 0;
		// Technically the most negative number.
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
		else if (minNeg == -2)
			return minNeg;
		else if (minPos != 999)
			return minPos;
		else if (hasZero)
			return 0;
		else
			return minNeg;
		// Priority: Immediate win, immediate loss,
		// latest loss, neutral, latest loss (again).
	}

	public int findGoodMove(int[] a, int best) {
		// Choose a column with the given "best" score. 
		int result = 0;
		int index = 0;
		ArrayList<Integer> tested = new ArrayList<Integer>();
		do {
			index = normalRandom(a.length / 2.0, 1.0, 0, a.length);
			if (tested.indexOf(index) < 0)
				tested.add(index);
			// System.out.println("Temp: " + index);
			result = a[index];
		} while (result != best || linkedGame.getPiecesPlaced()[index] >= linkedGame.ROWS && tested.size() < a.length);
		if (tested.size() < a.length)
			return index;
		else
			return -1;
	}

	public boolean testImminentLoss(int[] array) {
		// Will the AI lose no matter what?
		for (int i = 0; i < array.length; i++) {
			if (array[i] != -2 && array[i] != -999)
				//If a value is NOT -2 (immediate loss) or 
				return false;
		}
		System.out.println("Imminent Loss Found");
		return true;
	}

	public int normalRandom(double avg, double sd, int min, int max) {
		// Picks a random number based on a normal distribution
		// Constrained to a minimum and maximum.

		Random r = new Random();
		int randNum = (int) (r.nextGaussian() * sd + avg);
		if (randNum <= min)
			randNum = 0;
		else if (randNum >= max)
			randNum = max - 1;
		return randNum;

	}

	public void start() {
		if (linkedGame.getPlayerTurn() == playerTurn)
			move();
	}

}
