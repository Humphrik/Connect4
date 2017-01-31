package Game;

public class GameArea {
	public static PhysicalBoard game;
	public static AIPlayer player1;
	
	public static void main(String[] args) {
		game = new PhysicalBoard();
		//Auto-sets player 2 to be an AI.
		player1 = new AIPlayer(game,1);
		game.setVisible(true);
	}
}
