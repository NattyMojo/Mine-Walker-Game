import java.awt.Color;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class MineFieldButton extends JButton {
	
	private boolean hasMine;
	private boolean onPath;
	private boolean isCurrentPosition;
	private boolean isStartOrEnd;
	public int x = 0;
	public int y = 0;

	public MineFieldButton() {
		setBackground(Color.WHITE);
		hasMine = false;
		onPath = false;
		isCurrentPosition = false;
		isStartOrEnd = false;
		
	}
	
	public boolean isMine() {
		return hasMine;
	}
	
	public void setMine() {
		hasMine = true;
	}
	
	public boolean isOnPath() {
		return onPath;
	}
	
	public void setOnPath() {
		onPath = true;
	}
	
	public boolean isCurrentPosition() {
		return isCurrentPosition;
	}
	
	public void setCurrentPosition() {
		isCurrentPosition = !isCurrentPosition;
	}
	
	public boolean isStartOrEnd() {
		return isStartOrEnd;
	}
	
	public void setStartOrEnd() {
		isStartOrEnd = true;
	}
}
