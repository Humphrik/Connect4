package Game;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PhysicalBoard extends PlayingField {
	// Tangible connect-4 game

	private JFrame frame;
	private JPanel panel;
	private GridBagConstraints c;
	// labels correspond with squareStatuses
	private Square[][] squareLabels;
	private static Image empty, red, red2, yellow, yellow2, redWin, yellowWin;
	// Storage place for squares.

	// Blank board.
	public PhysicalBoard() {
		playerTurn = 1;
		try {
			empty = ImageIO.read(PhysicalBoard.class.getResource("Images/Empty.png"));
			red = ImageIO.read(PhysicalBoard.class.getResource("Images/Red.png"));
			red2 = ImageIO.read(PhysicalBoard.class.getResource("Images/Red2.png"));
			yellow = ImageIO.read(PhysicalBoard.class.getResource("Images/Yellow.png"));
			yellow2 = ImageIO.read(PhysicalBoard.class.getResource("Images/Yellow2.png"));
			redWin = ImageIO.read(PhysicalBoard.class.getResource("Images/Red_Win.png"));
			yellowWin = ImageIO.read(PhysicalBoard.class.getResource("Images/Yellow_Win.png"));
		} catch (IOException e) {
		}
		// Calls no-Args constructor of PlayingField
		frame = new JFrame();
		panel = new JPanel(new GridBagLayout());
		frame.setSize(730, 640);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(panel);
		panel.setFocusable(true);
		c = new GridBagConstraints();
		squareLabels = new Square[COLUMNS][ROWS];
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				// [0][0] will APPEAR to be the bottom left square.
				squareLabels[i][j] = new Square(i, j, this);
				squareLabels[i][j].setPreferredSize(new Dimension(100, 100));
				c.gridx = (i);
				c.gridy = ROWS - (j + 1);
				panel.add(squareLabels[i][j], c);
				squareLabels[i][j].updateImage(empty);
			}
		}
	}
	
	
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}

	@Override
	public int dropPiece(int column) {

		if (piecesPlaced[column] < ROWS) {
			System.out.println("Placing at: " + column +  "," + piecesPlaced[column]);
			if (playerTurn == 1)
				squareLabels[column][piecesPlaced[column]].updateImage(Math.random()<=0.95?red:red2);
			else if (playerTurn == -1)
				squareLabels[column][piecesPlaced[column]].updateImage(Math.random()<=0.95?yellow:yellow2);
			return super.dropPiece(column);
		}
		return 0;

	}
	
	public void declareWinner(int[][] winSet) {
		if(winSet[0][0] == -1)
			JOptionPane.showMessageDialog(frame, "It's a draw!");
		else {
			for(int[] piece : winSet)
				squareLabels[piece[0]][piece[1]].updateImage((playerTurn == 1?redWin:yellowWin));
			JOptionPane.showMessageDialog(frame, (playerTurn == 1?"Red":"Yellow") + " wins!");
		}
	}

}
