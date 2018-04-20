import java.awt.Dimension;
import javax.swing.JFrame;

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
