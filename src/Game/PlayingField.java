package Game;

public class PlayingField {
	// A connect 4 game without any GUI elements.

	// -1 is player 2, 1 is player 1, 0 is neither
	protected int[][] squareStatuses;

	// Number of game pieces put in each column (Left to right).
	protected int[] piecesPlaced;

	protected int playerTurn;
	protected final int ROWS = 6;
	protected final int COLUMNS = 7;

	public PlayingField() {
		int playerTurn = 1;
		squareStatuses = new int[COLUMNS][ROWS];
		piecesPlaced = new int[COLUMNS];
	}

	public PlayingField(int[][] sS, int pT) {
		int playerTurn = pT;
		piecesPlaced = new int[COLUMNS];
		squareStatuses = sS;
		for (int i = 0; i < COLUMNS; i++)
			for (int j : squareStatuses[i])
				if (j > 0)
					piecesPlaced[i]++;

	}

	public void dropPiece(int column) {
		squareStatuses[column][piecesPlaced[column]] = playerTurn;
		piecesPlaced[column]++;
		int[][] winSet = checkPlayerWin();
		if (checkDraw()) {
			declareWinner(new int[][]{{-1}});
			playerTurn = 0;
		}
		else if (winSet != null && playerTurn != 0) {
			declareWinner(winSet);
			playerTurn = 0;
		}
		playerTurn *= -1;
	}

	public int[][] checkPlayerWin() {
		// Horizontal
		for (int i = 0; i < COLUMNS - 3; i++)
			for (int j = 0; j < ROWS; j++)
				if (squareStatuses[i][j] + squareStatuses[i + 1][j] + squareStatuses[i + 2][j]
						+ squareStatuses[i + 3][j] == squareStatuses[i][j] * 4 && squareStatuses[i][j] != 0)
					return new int[][] { { i, j }, { i + 1, j }, { i + 2, j }, { i + 3, j } };
		// Vertical
		for (int i = 0; i < COLUMNS; i++)
			for (int j = 0; j < ROWS - 3; j++)
				if (squareStatuses[i][j] + squareStatuses[i][j + 1] + squareStatuses[i][j + 2]
						+ squareStatuses[i][j + 3] == squareStatuses[i][j] * 4 && squareStatuses[i][j] != 0)
					return new int[][] { { i, j }, { i, j + 1 }, { i, j + 2 }, { i, j + 3 } };
		// Diagonal (Right)
		for (int i = 0; i < COLUMNS - 3; i++)
			for (int j = 0; j < ROWS - 3; j++)
				if (squareStatuses[i][j] + squareStatuses[i + 1][j + 1] + squareStatuses[i + 2][j + 2]
						+ squareStatuses[i + 3][j + 3] == squareStatuses[i][j] * 4 && squareStatuses[i][j] != 0)
					return new int[][] { { i, j }, { i + 1, j + 1 }, { i + 2, j + 2 }, { i + 3, j + 3 } };
		// Diagonal (Left)
		for (int i = 3; i < COLUMNS; i++)
			for (int j = 0; j < ROWS - 3; j++)
				if (squareStatuses[i][j] + squareStatuses[i - 1][j + 1] + squareStatuses[i - 2][j + 2]
						+ squareStatuses[i - 3][j + 3] == squareStatuses[i][j] * 4 && squareStatuses[i][j] != 0)
					return new int[][] { { i, j }, { i - 1, j + 1 }, { i - 2, j + 2 }, { i - 3, j + 3 } };
		return null;
	}

	public void declareWinner(int[][] winSet) {
		if (winSet[0][0] == -1)
			System.out.println("Nobody wins! You have both failed!");
		if (playerTurn == 1)
			System.out.println("Red wins in this scenario");
		else
			System.out.println("Yellow wins in this scenario");
	}
	
	public boolean checkDraw() {
		for(int i : piecesPlaced)
			if(i < ROWS)
				return false;
		return true;
	}
}
