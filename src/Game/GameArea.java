package Game;

public class GameArea {
	public static PhysicalBoard game;
	public static AIPlayer player1;

	public static void main(String[] args) {
		game = new PhysicalBoard(1);
		// 1 is red first, -1 is yellow first.
		// Any other number will end the game before it starts.
		player1 = new AIPlayer(game, 1);
		// Auto-sets player 1 (red) to be an AI.
		game.setVisible(true);
	}
}
