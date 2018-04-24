import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

/**
 * Creates a panel of buttons to make up the mine field. Has a 2D array that keeps all the
 * buttons.
 * 
 * @author Zach Luciano
 *
 */
@SuppressWarnings("serial")
public class MineFieldPanel extends JPanel {

	MineFieldButton[][] buttons;
	int gridSize;
	                 
	/**
	 * Creates the 2D array of buttons and sets their parameters.
	 * @param listener - The listener for each button
	 * @param width - The width of the panel
	 * @param height - The height of the panel
	 * @param gridSize - The size of the grid, also the size of the 2D array
	 */
	public MineFieldPanel(ActionListener listener, int width, int height, int gridSize) {
		
		this.gridSize = gridSize;
		this.setPreferredSize(new Dimension(width, height));
		this.setLayout(new GridLayout(gridSize, gridSize));
		
		buttons = new MineFieldButton[gridSize][gridSize];
		for(int row = 0; row < gridSize; row++) {
			for(int column = 0; column < gridSize; column++) {
				buttons[row][column] = new MineFieldButton();
				buttons[row][column].setName("(" + row + ", " + column + ")");
				buttons[row][column].x = column;
				buttons[row][column].y = row;
				
				if(row == gridSize-1 && column == 0) {
					buttons[row][column].setBackground(Color.CYAN);
				}
				
				if(row == 0 && column == gridSize-1) {
					buttons[row][column].setBackground(Color.MAGENTA);
				}
				
				
				buttons[row][column].setPreferredSize(new Dimension((width/gridSize), (height/gridSize)));
				buttons[row][column].addActionListener(listener);
				this.add(buttons[row][column]);
			}
		}
	}
	
	/**
	 * Finds the button with the name that matches what is given
	 * @param name - String in the form of "(num, num)"
	 * @return Returns a button object
	 */
	public MineFieldButton findButton(String name) {
		MineFieldButton returned = new MineFieldButton();
		for(int row = 0; row < gridSize; row++) {
			for(int column = 0; column < gridSize; column++) {
				if(name.equals(buttons[row][column].getName())) {
					returned = buttons[row][column];
				}
			}
		}
		return returned;
	}
}
