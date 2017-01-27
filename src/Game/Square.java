package Game;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Square extends JLabel implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int column;
	private int row;
	
	public Square(int c, int r) {
		column = c;
		row = r;
		addMouseListener(this);
	}

	public void mousePressed(MouseEvent e) {
		GameArea.game.dropPiece(column);
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}
	
	public void updateImage(Image image) {
		this.setIcon(new ImageIcon(image.getScaledInstance(100,100,Image.SCALE_SMOOTH)));
	}
}
