import java.awt.Color;
import javax.swing.JButton;

/**
 * Is a button to be used in a grid to create a mine field, has boolean attributes to keep track of
 * mines and if it is on the path, as well as if it is the current position of the player.
 * 
 * @author Zach Luciano
 *
 */
@SuppressWarnings("serial")
public class MineFieldButton extends JButton {
	
	private boolean hasMine;
	private boolean onPath;
	private boolean isCurrentPosition;
	private boolean isStartOrEnd;
	private boolean alreadyClicked;
	public int x = 0;
	public int y = 0;

	/**
	 * Creates a new button with all attributes set to false, these are set in other classes.
	 */
	public MineFieldButton() {
		setBackground(Color.WHITE);
		hasMine = false;
		onPath = false;
		isCurrentPosition = false;
		isStartOrEnd = false;
		alreadyClicked = false;
		
	}
	
	/**
	 * Checks to see if the button has been already clicked
	 * @return alreadyClicked - boolean set to true when button is clicked
	 */
	public boolean wasAlreadyClicked() {
		return alreadyClicked;
	}
	
	public void setClicked() {
		alreadyClicked = true;
	}
	
	/**
	 * Checks to see if the button in question is a mine or not
	 * @return hasMine - boolean for if the button is a mine or not.
	 */
	public boolean isMine() {
		return hasMine;
	}
	
	/**
	 * Sets the hasMine boolean variable for the button to the opposite of itself so it can be turned
	 * on and off
	 */
	public void setMine() {
		hasMine = !hasMine;
	}
	
	/**
	 * Checks to see if the button is on the ensured path to safety
	 * @return onPath - boolean for if it is on the path or not
	 */
	public boolean isOnPath() {
		return onPath;
	}
	
	/**
	 * Sets the button to be on the path. This is for ease of access to highlight in other classes.
	 */
	public void setOnPath() {
		onPath = true;
	}
	
	/**
	 * Checks to see if the button is where the player currently is on the game board
	 * @return isCurrentPosition - boolean of if that is the last clicked button by the user.
	 */
	public boolean isCurrentPosition() {
		return isCurrentPosition;
	}
	
	/**
	 * Sets the value of isCurrentPosition to the opposite of itself so that this attribute can
	 * be switched on and off
	 */
	public void setCurrentPosition() {
		isCurrentPosition = !isCurrentPosition;
	}
	
	/**
	 * Checks to see if the button is either the bottom-left (start) or top-right (end)
	 * @return isStartOrEnd - boolean if the button is a start or end position
	 */
	public boolean isStartOrEnd() {
		return isStartOrEnd;
	}
	
	/**
	 * Sets the start or end attribute of the button to true.
	 */
	public void setStartOrEnd() {
		isStartOrEnd = true;
	}
}
