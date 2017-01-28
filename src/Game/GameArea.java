package Game;

public class GameArea {
	public static PhysicalBoard game;
	public static AIPlayer player1;
	
	public static void main(String[] args) {
		game = new PhysicalBoard();
		player1 = new AIPlayer(game,-1);
		game.setVisible(true);
	}
}
