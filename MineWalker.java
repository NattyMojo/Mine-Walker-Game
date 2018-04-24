import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * Creates a new JFrame for the MineWalker Game. Game is played by starting in the lower left
 * corner of a grid and trying to get to the upper right without losing all lives, score is kept
 * by adding 100 each time a valid space is clicked, 100 points are subtracted when a mine is stepped
 * on and the player loses a life. Reaching the final spot adds 300 points to the player's score.
 * 
 * Get the highest score you can!
 * 
 * @author Zach Luciano
 *
 */
public class MineWalker{

	public static void main(String[] args) {
		
		int height = 800;
		int width = 1000;
		Dimension size = new Dimension(width, height);
		
		JFrame frame = new JFrame("MineWalker");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(size);
				
		frame.getContentPane().add(new MineWalkerPanel(width, height));	
		
		frame.pack();
		frame.setVisible(true);
	}
	
}
