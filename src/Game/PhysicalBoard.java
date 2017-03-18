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
	public PhysicalBoard(int pT) {
		// Calls 1 parameter constructor of PlayingField
		super(pT);
		playerTurn = pT;
		// Starts with player 1 first (not tested with player 2 first).
		try {
			// Set every image.
			empty = ImageIO.read(PhysicalBoard.class.getResource("Images/Empty.png"));
			red = ImageIO.read(PhysicalBoard.class.getResource("Images/Red.png"));
			red2 = ImageIO.read(PhysicalBoard.class.getResource("Images/Red2.png"));
			yellow = ImageIO.read(PhysicalBoard.class.getResource("Images/Yellow.png"));
			yellow2 = ImageIO.read(PhysicalBoard.class.getResource("Images/Yellow2.png"));
			redWin = ImageIO.read(PhysicalBoard.class.getResource("Images/Red_Win.png"));
			yellowWin = ImageIO.read(PhysicalBoard.class.getResource("Images/Yellow_Win.png"));
		} catch (IOException e) {
			System.out.println("An image didn't load!");
		}
		frame = new JFrame();
		panel = new JPanel(new GridBagLayout());
		frame.setSize(COLUMNS * 100 + 30, ROWS * 100 + 40);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(panel);
		panel.setFocusable(true);
		// Sets properties and dimensions of the window.
		c = new GridBagConstraints();
		squareLabels = new Square[COLUMNS][ROWS];
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				// [0][0] will APPEAR to be the bottom left square.
				squareLabels[i][j] = new Square(i, this);
				squareLabels[i][j].setPreferredSize(new Dimension(100, 100));
				c.gridx = (i);
				c.gridy = ROWS - (j + 1);
				panel.add(squareLabels[i][j], c);
				squareLabels[i][j].updateImage(empty);
				// Add a square to the panel, and update its image.
			}
		}
	}

	public void setVisible(boolean b) {
		// Set the visibility.
		frame.setVisible(b);
	}

	@Override
	public int dropPiece(int column) {
		// Place a piece in next open slot of a column.
		// Returns a win for either player, or neither.
		if (piecesPlaced[column] < ROWS) {
			// Will only place is the column is not full.
			System.out.println("Placing at: " + column + "," + piecesPlaced[column]);
			if (playerTurn == 1)
				squareLabels[column][piecesPlaced[column]].updateImage(Math.random() <= 0.95 ? red : red2);
			else if (playerTurn == -1)
				squareLabels[column][piecesPlaced[column]].updateImage(Math.random() <= 0.95 ? yellow : yellow2);
			return super.dropPiece(column);
			// See the PlayingField class' dropPiece method.
		}
		return 0;
		// Returns 0 if move wasn't made.

	}

	public void declareWinner(int[][] winSet) {
		// Called by the PlayingField class' dropPiece method.
		// Displays winning player and the winning set.
		if (winSet[0][0] == -1)
			JOptionPane.showMessageDialog(frame, "It's a draw!");
		else {
			for (int[] piece : winSet)
				squareLabels[piece[0]][piece[1]].updateImage((playerTurn == 1 ? redWin : yellowWin));
			JOptionPane.showMessageDialog(frame, (playerTurn == 1 ? "Red" : "Yellow") + " wins!");
		}
	}

}
