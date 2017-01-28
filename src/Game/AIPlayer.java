package Game;

public class AIPlayer {
	private PhysicalBoard linkedGame;
	private int playerTurn;

	public AIPlayer(PhysicalBoard game, int p) {
		linkedGame = game;
		linkedGame.linkAIPlayer(this);
		playerTurn = p;
		if (linkedGame.getPlayerTurn() == p)
			this.start();
	}

	public void move() {
		int column = calculateBestMove(linkedGame);
		if (column < 0)
			do
			column = ((int) (Math.random() * 7));
			while(linkedGame.getPiecesPlaced()[column] >= linkedGame.ROWS);
		linkedGame.dropPiece(column);
		//// System.out.println("Placing at: " + column + ", " +
		//// linkedGame.getPiecesPlaced()[column]);
	}

	public int calculateBestMove(PlayingField p) {
		// negative is loss, positive is win.
		// int[] scores = new int[p.getDimensions()[0]];
		PlayingField pI;

		for (int i = 0; i < p.COLUMNS; i++) {
			if (p.getPiecesPlaced()[i] < p.ROWS) {
				pI = new PlayingField(p.getSquareStatuses(), p.getPlayerTurn());
				int returned1 = pI.dropPiece(i);
				if (returned1 != 0)
					return i;
			}
		}
		for (int i = 0; i < p.COLUMNS; i++) {
			if (p.getPiecesPlaced()[i] < p.ROWS) {
				pI = new PlayingField(p.getSquareStatuses(), p.getPlayerTurn()*-1);
				int returned1 = pI.dropPiece(i);
				if (returned1 != 0)
					return i;
			}
		}
		return -1;
	}

	public void start() {
		if (linkedGame.getPlayerTurn() == playerTurn)
			move();
	}
}
