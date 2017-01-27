package Game;

public class GameArea {
	public static PhysicalBoard game;
	
	public static void main(String[] args) {
		game = new PhysicalBoard();
		game.setVisible(true);
	}
}
