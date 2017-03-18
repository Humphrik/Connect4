package Game;

public class PlayingField {
	// A connect 4 game without any GUI elements.
	private int[][] squareStatuses;
	// Number of game pieces put in each column (Left to right).

	protected int[] piecesPlaced;
	protected int playerTurn;
	// -1 is yellow, 1 is red (default player 1), 0 is neither
	public final int ROWS = 6;
	public final int COLUMNS = 7;
	// Compatible with any size!

	protected AIPlayer linkedAI;

	public PlayingField(int pT) {
		// Creates an empty field. Used in the PhysicalBoard class.
		playerTurn = pT;
		squareStatuses = new int[COLUMNS][ROWS];
		piecesPlaced = new int[COLUMNS];
	}

	public PlayingField(int[][] sS, int pT) {
		// Creates a field with pre-set squares)
		playerTurn = pT;
		piecesPlaced = new int[COLUMNS];
		squareStatuses = new int[COLUMNS][ROWS];
		for (int i = 0; i < COLUMNS; i++)
			for (int j = 0; j < ROWS; j++) {
				squareStatuses[i][j] = sS[i][j];
				// Sets field's square with corresponding value.
				if (squareStatuses[i][j] != 0)
					piecesPlaced[i]++;
				// Increase pieces placed in different columns.
			}
	}

	public int dropPiece(int column) {
		// Puts a piece in the given column.
		if (piecesPlaced[column] < ROWS) {
			// Only places it the column if it is open.
			squareStatuses[column][piecesPlaced[column]] = playerTurn;
			piecesPlaced[column]++;
			int[][] winSet = checkPlayerWin();
			// Looks for a win after the piece is placed.
			if (winSet != null && playerTurn != 0) {
				// Declare a winner if any, and prevent future turns.
				declareWinner(winSet);
				// Does nothing unless for a physical board.
				int temp = playerTurn;
				playerTurn = 0;
				return temp;
				// Return the winning player.
			} else if (checkDraw()) {
				// In case of a draw.
				declareWinner(new int[][] { { -1 } });
				playerTurn = 0;
			}
			playerTurn *= -1;
		}
		return 0;
		// If nobody has won after this turn.
	}

	public int[][] checkPlayerWin() {
		// Checks for a 4 or 5 in a row
		// Horizontal
		for (int i = 0; i < COLUMNS - 3; i++)
			for (int j = 0; j < ROWS; j++)
				if (squareStatuses[i][j] + squareStatuses[i + 1][j] + squareStatuses[i + 2][j]
						+ squareStatuses[i + 3][j] == squareStatuses[i][j] * 4 && squareStatuses[i][j] != 0)
					if (i < COLUMNS - 4 && squareStatuses[i + 4][j] == squareStatuses[i][j])
						// A 5-in a row.
						return new int[][] { { i, j }, { i + 1, j }, { i + 2, j }, { i + 3, j }, { i + 4, j } };
					else
						return new int[][] { { i, j }, { i + 1, j }, { i + 2, j }, { i + 3, j } };
		// Vertical
		for (int i = 0; i < COLUMNS; i++)
			for (int j = 0; j < ROWS - 3; j++)
				if (squareStatuses[i][j] + squareStatuses[i][j + 1] + squareStatuses[i][j + 2]
						+ squareStatuses[i][j + 3] == squareStatuses[i][j] * 4 && squareStatuses[i][j] != 0)
					if (j < ROWS - 4 && squareStatuses[i][j + 4] == squareStatuses[i][j])
						return new int[][] { { i, j }, { i, j + 1 }, { i, j + 2 }, { i, j + 3 }, { i, j + 4 } };
					else
						return new int[][] { { i, j }, { i, j + 1 }, { i, j + 2 }, { i, j + 3 } };

		// Diagonal (Right)
		for (int i = 0; i < COLUMNS - 3; i++)
			for (int j = 0; j < ROWS - 3; j++)
				if (squareStatuses[i][j] + squareStatuses[i + 1][j + 1] + squareStatuses[i + 2][j + 2]
						+ squareStatuses[i + 3][j + 3] == squareStatuses[i][j] * 4 && squareStatuses[i][j] != 0)
					if (i < COLUMNS - 4 && j < ROWS - 4 && squareStatuses[i + 4][j + 4] == squareStatuses[i][j])
						return new int[][] { { i, j }, { i + 1, j + 1 }, { i + 2, j + 2 }, { i + 3, j + 3 },
								{ i + 4, j + 4 } };
					else
						return new int[][] { { i, j }, { i + 1, j + 1 }, { i + 2, j + 2 }, { i + 3, j + 3 } };
		// Diagonal (Left)
		for (int i = 3; i < COLUMNS; i++)
			for (int j = 0; j < ROWS - 3; j++)
				if (squareStatuses[i][j] + squareStatuses[i - 1][j + 1] + squareStatuses[i - 2][j + 2]
						+ squareStatuses[i - 3][j + 3] == squareStatuses[i][j] * 4 && squareStatuses[i][j] != 0)
					if (i > 4 && j < ROWS - 4 && squareStatuses[i - 4][j + 4] == squareStatuses[i][j])
						return new int[][] { { i, j }, { i - 1, j + 1 }, { i - 2, j + 2 }, { i - 3, j + 3 },
								{ i - 4, j + 4 } };
					else
						return new int[][] { { i, j }, { i - 1, j + 1 }, { i - 2, j + 2 }, { i - 3, j + 3 } };
		return null;
	}

	public void declareWinner(int[][] winSet) {
		// Should do nothing unless a physical board.
		/*
		 * if (winSet[0][0] == -1) System.out.println(
		 * "This scenario leads to a draw."); if (playerTurn == 1)
		 * System.out.println("Red wins in this scenario"); else
		 * System.out.println("Yellow wins in this scenario");
		 */
	}

	public boolean checkDraw() {
		// If every column is full, return true.
		for (int i : piecesPlaced)
			if (i < ROWS)
				return false;
		return true;
	}

	public int[][] getSquareStatuses() {
		return squareStatuses;
	}

	public int getPlayerTurn() {
		return playerTurn;
	}

	public int[] getPiecesPlaced() {
		return piecesPlaced;
	}

	public void linkAIPlayer(AIPlayer p) {
		// Called by the AIPlayer itself.
		linkedAI = p;
	}

	public AIPlayer getAIPlayer() {
		return linkedAI;
	}

}
