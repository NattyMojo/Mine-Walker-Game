import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Main game GUI. All elements are added to this panel which is added to the JFrame of MineWalker.java
 * Has control over all buttons and labels of the game. Houses all event listeners.
 * @author Zach Luciano
 *
 */
@SuppressWarnings("serial")
public class MineWalkerPanel extends JPanel {

	private final int DELAY = 400;
	private int gridSize = 10;
	private int width;
	private int height;
	private ArrayList<Point> path;
	private MineFieldPanel mfp;
	private JPanel main = this;
	private JPanel east = new JPanel();
	private JPanel south = new JPanel();
	private JPanel west = new JPanel();
	private RandomWalk rw;
	private Random rand = new Random();
	private Timer timer;
	private boolean on = false;
	private MineFieldButton previouslyClicked;
	private MineFieldButton mineClicked = new MineFieldButton();
	private boolean mineWasClicked = false;
	private int minesNearby;
	private ArrayList<Color> previousColors = new ArrayList<Color>();
	private int previousColorsIndex = 0;
	private int livesLeft = 5;
	private int playerScore = -100;

	/**
	 * Creates the JPanel that all game elements go into and adds all of them to its BorderLayout.
	 * Also starts the first game as soon as it is run.
	 * @param width - The width of the panel
	 * @param height - The height of the panel
	 */
	public MineWalkerPanel(int width, int height) {

		this.height = height - 300;
		this.width = width - 300;

		setLayout(new BorderLayout());

		east.setPreferredSize(new Dimension(150, height));
		east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
		east.setBackground(Color.LIGHT_GRAY);
		fillEastPanel();

		west.setPreferredSize(new Dimension(150, height));
		west.setLayout(new GridLayout(8, 1));
		west.setBackground(Color.LIGHT_GRAY);
		fillWestPanel();

		south.setPreferredSize(new Dimension(width, 50));
		south.setLayout(new FlowLayout());
		south.setBackground(Color.LIGHT_GRAY);
		fillSouthPanel();

		mfp = new MineFieldPanel(new MineButtonListener(), width - 300, height - 300, gridSize);
		this.add(mfp, BorderLayout.CENTER);
		this.add(south, BorderLayout.SOUTH);
		this.add(east, BorderLayout.EAST);
		this.add(west, BorderLayout.WEST);

		newGridAndWalk(gridSize);
	}
	
	/**
	 * Adds buttons to the South Border of the MineWalkerPanel, these buttons can be used by the player
	 * Was created as a separate method for readability
	 */
	private void fillSouthPanel() {

		JButton showOrHideMines = new JButton("Show Mines");
		showOrHideMines.addActionListener(new ShowHideMinesListener());
		showOrHideMines.setPreferredSize(new Dimension(120, 30));
		south.add(showOrHideMines);

		JButton showOrHidePath = new JButton("Show Path");
		showOrHidePath.addActionListener(new ShowHidePathListener());
		showOrHidePath.setPreferredSize(new Dimension(120, 30));
		south.add(showOrHidePath);

		JButton newOrGiveUp = new JButton("Give Up?");
		newOrGiveUp.addActionListener(new NewOrGiveUpListener());
		newOrGiveUp.setPreferredSize(new Dimension(120, 30));
		south.add(newOrGiveUp);

		JTextField gridSizeField = new JTextField();
		gridSizeField.setPreferredSize(new Dimension(40, 30));
		gridSizeField.addActionListener(new TextListener());
		south.add(gridSizeField);

	}

	/**
	 * Adds elements to the East Border of the MineWalkerPanel, these elements pertain to the scoreboard
	 */
	private void fillEastPanel() {

		east.setBorder(BorderFactory.createTitledBorder("Score Board"));

		JLabel lives = new JLabel("Lives: " + livesLeft);
		east.add(lives);

		JLabel score = new JLabel("Score: " + playerScore);
		east.add(score);

	}

	/**
	 * Adds the color key to the West Border of the MineWalkerPanel with necessary JLabels
	 */
	private void fillWestPanel() {

		west.setBorder(BorderFactory.createTitledBorder("Color Key"));

		JLabel green = new JLabel("0 Nearby Mines");
		green.setBackground(Color.GREEN);
		green.setOpaque(true);
		west.add(green);

		JLabel yellow = new JLabel("1 Nearby Mine");
		yellow.setBackground(Color.YELLOW);
		yellow.setOpaque(true);
		west.add(yellow);

		JLabel orange = new JLabel("2 Nearby Mines");
		orange.setBackground(Color.ORANGE);
		orange.setOpaque(true);
		west.add(orange);

		JLabel red = new JLabel("3 Nearby Mines");
		red.setBackground(Color.RED);
		red.setOpaque(true);
		west.add(red);

		JLabel black = new JLabel("Exploded Mine");
		black.setBackground(Color.BLACK);
		black.setForeground(Color.WHITE);
		black.setOpaque(true);
		west.add(black);

		JLabel emptyCell = new JLabel();
		emptyCell.setOpaque(false);
		west.add(emptyCell);

		JLabel start = new JLabel("Start");
		start.setBackground(Color.CYAN);
		start.setOpaque(true);
		west.add(start);

		JLabel finish = new JLabel("Finish");
		finish.setBackground(Color.MAGENTA);
		finish.setOpaque(true);
		west.add(finish);

	}

	/**
	 * Listener for each MineFieldButton in the game panel.
	 * Checks to see if it is a valid move, as well as if the space that was clicked on was a mine or not
	 * Creates JOptionPanes based on if the game was won or lost.
	 * @author Zach Luciano
	 */
	private class MineButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			MineFieldButton mb = (MineFieldButton) e.getSource();
			minesNearby = 0;
			mineWasClicked = false;
			previouslyClicked.setBackground(previousColors.get(previousColorsIndex));
			mineClicked.setBackground(Color.BLACK);
			
			if((mb.x == previouslyClicked.x+1 || mb.x == previouslyClicked.x - 1) && mb.y == previouslyClicked.y) {
				mb.setBackground(Color.PINK);
				mb.setCurrentPosition();
				previouslyClicked.setCurrentPosition();
				previouslyClicked = mb;
				playerScore += 100;
			}
			else if((mb.y == previouslyClicked.y+1 || mb.y == previouslyClicked.y - 1) && mb.x == previouslyClicked.x) {
				mb.setBackground(Color.PINK);
				mb.setCurrentPosition();
				previouslyClicked.setCurrentPosition();
				previouslyClicked = mb;
				playerScore += 100;
			}
			System.out.println(playerScore);
			
			for (int row = 0; row < gridSize; row++) {
				for (int column = 0; column < gridSize; column++) {
					if(mfp.buttons[row][column].isCurrentPosition()) {
						if(row+1 < gridSize && mfp.buttons[row+1][column].isMine())
							minesNearby++;
						if(row-1 >= 0 && mfp.buttons[row-1][column].isMine())
							minesNearby++;
						if(column+1 < gridSize && mfp.buttons[row][column+1].isMine())
							minesNearby++;
						if(column-1 >= 0 && mfp.buttons[row][column-1].isMine())
							minesNearby++;
					}
					
					if(previouslyClicked.equals(mfp.buttons[0][gridSize-1])) {
						playerScore += 300;
						int gameWinPane = JOptionPane.showConfirmDialog(null,"You Win!\nWant to play again?", "You Win!", JOptionPane.YES_NO_OPTION);
						if(gameWinPane == 0) {
							timer.stop();
							newGridAndWalk(gridSize);
						}
						else {
							System.exit(0);
						}
					}
					
					if(previouslyClicked.equals(mfp.buttons[row][column]) && mfp.buttons[row][column].isMine()) {
						livesLeft--;
						playerScore -= 200;
						mfp.buttons[row][column].setMine();
						mineClicked = mfp.buttons[row][column];
						mineWasClicked = true;
						if(livesLeft == 0) {
							int gameOverPane = JOptionPane.showConfirmDialog(null, "Game Over, Care to try again?", "Game Over", JOptionPane.YES_NO_OPTION);
							if(gameOverPane == 0) {
								timer.stop();
								newGridAndWalk(gridSize);
							}
							else {
								System.exit(0);
							}
						}
					}		
				}
			}
		}
	}

	/**
	 * Button listener for showing and hiding the mines in the game, should be disabled for play but
	 * is enabled during play for debugging purposes.
	 * @author Zach Luciano
	 *
	 */
	private class ShowHideMinesListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton shm = (JButton) e.getSource();
			if (shm.getText() == "Show Mines") {
				for (int row = 0; row < gridSize; row++) {
					for (int column = 0; column < gridSize; column++) {
						if (mfp.buttons[row][column].isMine()) {
							mfp.buttons[row][column].setBackground(Color.BLACK);
						}
					}
				}
				shm.setText("Hide Mines");
			} else {
				for (int row = 0; row < gridSize; row++) {
					for (int column = 0; column < gridSize; column++) {
						if (mfp.buttons[row][column].isMine()) {
							mfp.buttons[row][column].setBackground(Color.WHITE);
						}
					}
				}
				shm.setText("Show Mines");
			}
		}
	}

	/**
	 * Button Listener for showing and hiding the path created by RandomWalk. This is used to ensure
	 * that there is a way to finish the grid every time. Path is created to show the user the ensured
	 * route to finish the game, should probably be disabled during gameplay but will stay enabled
	 * for debugging purposes.
	 * @author Zach Luciano
	 *
	 */
	private class ShowHidePathListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton shp = (JButton) e.getSource();
			if (shp.getText() == "Show Path") {
				for (int row = 0; row < gridSize; row++) {
					for (int column = 0; column < gridSize; column++) {
						if (mfp.buttons[row][column].isOnPath())
							mfp.buttons[row][column].setBackground(Color.BLUE);
					}
				}
				shp.setText("Hide Path");
			} else {
				for (int row = 0; row < gridSize; row++) {
					for (int column = 0; column < gridSize; column++) {
						if (mfp.buttons[row][column].isOnPath())
							mfp.buttons[row][column].setBackground(Color.WHITE);
					}
				}
				mfp.buttons[gridSize - 1][0].setBackground(Color.CYAN);
				mfp.buttons[gridSize - 1][0].setStartOrEnd();
				mfp.buttons[0][gridSize - 1].setBackground(Color.MAGENTA);
				mfp.buttons[0][gridSize - 1].setStartOrEnd();
				shp.setText("Show Path");
			}
		}
	}

	/**
	 * Button Listener for the "New Game" or "Give Up?" button. How it is currently set up,
	 * a new game is always automatically created so there is no need for the new game button.
	 * @author Zach Luciano
	 *
	 */
	private class NewOrGiveUpListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton ngl = (JButton) e.getSource();
			if (ngl.getText() == "New Game") {
				timer.stop();
				newGridAndWalk(gridSize);
				ngl.setText("Give Up?");
			}
			else {
				int gaveUpPane = JOptionPane.showConfirmDialog(null, "Sad to see you couldn't make it\n	Want to try again?", "You Gave Up", JOptionPane.YES_NO_OPTION);
				if(gaveUpPane == 0) {
					timer.stop();
					ngl.setText("Give Up?");
					newGridAndWalk(gridSize);
				}
				else {
					System.exit(0);
				}
			}
		}
	}

	/**
	 * Listener for the text field that resizes the playing grid, default is a 10x10 grid. Number
	 * entered must be a single number between 4 and 20. Will create a square grid.
	 * @author Zach Luciano
	 *
	 */
	private class TextListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			int gridSize;
			JTextField jtf = (JTextField) e.getSource();
			try {
				gridSize = Integer.parseInt(jtf.getText());
				jtf.setText(null);
				if (gridSize < 4 || gridSize > 20) {
					System.out.println("Grid size can only be from 4-20");
					return;
				}
			} catch (NumberFormatException nfe) {
				System.out.println("You must enter a number for the gridSize");
				return;
			}
			timer.stop();
			newGridAndWalk(gridSize);
		}
	}

	/**
	 * Removes the current grid of buttons and recreates it, invalidates and revalidates the pane.
	 * it then resets the game by creating a path and setting the mines.
	 * @param gridSize - Grid Size of the game board
	 */
	private void newGridAndWalk(int gridSize) {
		this.gridSize = gridSize;
		livesLeft = 5;
		playerScore = 0;
		main.remove(mfp);
		mfp.removeAll();
		mfp = new MineFieldPanel(new MineButtonListener(), width - 300, height - 300, gridSize);

		main.add(mfp, BorderLayout.CENTER);

		main.invalidate();
		main.revalidate();

		
		previouslyClicked = mfp.buttons[gridSize - 1][0];
		previouslyClicked.setCurrentPosition();
		previousColors.add(Color.CYAN);

		rw = new RandomWalk(gridSize);
		rw.createWalk();
		this.path = rw.getPath();
		setPath();
		placeMines();
		startAnimation();
		previouslyClicked.doClick();
	}

	/**
	 * Creates a random point and checks if it is on the path or already a mine; if not then it will
	 * become a mine. It runs through this loop for a quarter of the buttons in the grid.
	 */
	private void placeMines() {
		for (int i = 0; i < ((double) gridSize * gridSize) * 0.25; i++) {
			String randomCoords = "(" + rand.nextInt(gridSize) + ", " + rand.nextInt(gridSize) + ")";
			if (mfp.findButton(randomCoords).isOnPath() || mfp.findButton(randomCoords).isMine()) {
				i--;
			} else {
				mfp.findButton(randomCoords).setMine();
			}
		}
	}

	/**
	 * Takes the path created by the RandomWalk and finds every button on that path.
	 * Changes the boolean value for that specific button to set it as being on the path.
	 */
	private void setPath() {
		for (int i = 0; i < rw.getPath().size(); i++) {
			String pathPos = "(" + path.get(i).x + ", " + path.get(i).y + ")";
			for (int row = 0; row < gridSize; row++) {
				for (int column = 0; column < gridSize; column++) {
					if (pathPos.equals(mfp.buttons[row][column].getName())) {
						mfp.buttons[row][column].setOnPath();
					}
				}
			}
		}
	}

	/**
	 * Performs action when timer event fires.
	 */
	private class TimerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			on = !on;
			for (int row = 0; row < gridSize; row++) {
				for (int column = 0; column < gridSize; column++) {
					if (mfp.buttons[row][column].isCurrentPosition()) {
						if (on) {
							if(minesNearby == 0) {
								mfp.buttons[row][column].setBackground(Color.GREEN);
								previousColors.add(Color.GREEN);
								previousColorsIndex++;
							}
							if(minesNearby == 1) {
								mfp.buttons[row][column].setBackground(Color.YELLOW);
								previousColors.add(Color.YELLOW);
								previousColorsIndex++;
							}
							if(minesNearby == 2) {
								mfp.buttons[row][column].setBackground(Color.ORANGE);
								previousColors.add(Color.ORANGE);
								previousColorsIndex++;
							}
							if(minesNearby == 3) {
								mfp.buttons[row][column].setBackground(Color.RED);
								previousColors.add(Color.RED);
								previousColorsIndex++;
							}
							if(mineWasClicked) {
								mfp.buttons[row][column].setBackground(Color.BLACK);
								previousColors.add(Color.BLACK);
								previousColorsIndex++;
							}
						} 
						else {
							mfp.buttons[row][column].setBackground(Color.WHITE);
						}
					}
				}
			}
		}
	}

	/**
	 * Create an animation thread that runs periodically
	 */
	private void startAnimation() {
		TimerActionListener taskPerformer = new TimerActionListener();
		timer = new Timer(DELAY, taskPerformer);
		timer.start();
	}
}
