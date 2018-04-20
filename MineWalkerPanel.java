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
	private int minesNearby;

	public MineWalkerPanel(int width, int height) {

		this.height = height - 300;
		this.width = width - 300;

		setLayout(new BorderLayout());

		east.setPreferredSize(new Dimension(150, height));
		east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
		fillEastPanel();

		west.setPreferredSize(new Dimension(150, height));
		west.setLayout(new GridLayout(8, 1));
		fillWestPanel();

		south.setPreferredSize(new Dimension(width, 50));
		south.setLayout(new FlowLayout());
		fillSouthPanel();

		mfp = new MineFieldPanel(new MineButtonListener(), width - 300, height - 300, gridSize);
		this.add(mfp, BorderLayout.CENTER);
		this.add(south, BorderLayout.SOUTH);
		this.add(east, BorderLayout.EAST);
		this.add(west, BorderLayout.WEST);

		newGridAndWalk(gridSize);
	}

	private void fillSouthPanel() {

		JButton showOrHideMines = new JButton("Show Mines");
		showOrHideMines.addActionListener(new ShowHideMinesListener());
		showOrHideMines.setPreferredSize(new Dimension(120, 30));
		south.add(showOrHideMines);

		JButton showOrHidePath = new JButton("Show Path");
		showOrHidePath.addActionListener(new ShowHidePathListener());
		showOrHidePath.setPreferredSize(new Dimension(120, 30));
		south.add(showOrHidePath);

		JButton newOrGiveUp = new JButton("New Game");
		newOrGiveUp.addActionListener(new NewOrGiveUpListener());
		newOrGiveUp.setPreferredSize(new Dimension(120, 30));
		south.add(newOrGiveUp);

		JTextField gridSizeField = new JTextField();
		gridSizeField.setPreferredSize(new Dimension(40, 30));
		gridSizeField.addActionListener(new TextListener());
		south.add(gridSizeField);

	}

	private void fillEastPanel() {

		east.setBorder(BorderFactory.createTitledBorder("Score Board"));

		JLabel lives = new JLabel("Lives: ");
		east.add(lives);

		JLabel score = new JLabel("Score: ");
		east.add(score);

	}

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

	private class MineButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			MineFieldButton mb = (MineFieldButton) e.getSource();
			
			if(mb.x == previouslyClicked.x+1 || mb.x == previouslyClicked.x - 1 && mb.y == previouslyClicked.y) {
				mb.setBackground(Color.PINK);
				mb.setCurrentPosition();
				previouslyClicked.setCurrentPosition();
				previouslyClicked = mb;
			}
			else if(mb.y == previouslyClicked.y+1 || mb.y == previouslyClicked.y - 1 && mb.x == previouslyClicked.x) {
				mb.setBackground(Color.PINK);
				mb.setCurrentPosition();
				previouslyClicked.setCurrentPosition();
				previouslyClicked = mb;
			}
			
			for (int row = 0; row < gridSize; row++) {
				for (int column = 0; column < gridSize; column++) {
					if(mfp.buttons[row][column].isCurrentPosition()) {
//						if(row++ < gridSize && mfp.buttons[row++][column].isMine())
//							minesNearby++;
						if(row-- >= 0 && mfp.buttons[row--][column].isMine())
							minesNearby++;
						if(column++ < gridSize && mfp.buttons[row][column++].isMine())
							minesNearby++;
						if(column-- >= 0 && mfp.buttons[row][column--].isMine())
							minesNearby++;
					}
					
					if(previouslyClicked.equals(mfp.buttons[row][column]) && mfp.buttons[row][column].isMine()) {
						int gameOverPane = JOptionPane.showConfirmDialog(null, "Game Over", "Game Over", JOptionPane.YES_NO_OPTION);
						
						if(gameOverPane == 0) {
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

	private class NewOrGiveUpListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton ngl = (JButton) e.getSource();
			if (ngl.getText() == "New Game")
				newGridAndWalk(gridSize);
			else {
				System.out.println(ngl.getText());
				ngl.setText("New Game");
			}
		}
	}

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

			newGridAndWalk(gridSize);
		}
	}

	private void newGridAndWalk(int gridSize) {
		this.gridSize = gridSize;
		main.remove(mfp);
		mfp.removeAll();
		mfp = new MineFieldPanel(new MineButtonListener(), width - 300, height - 300, gridSize);

		main.add(mfp, BorderLayout.CENTER);

		main.invalidate();
		main.revalidate();

		previouslyClicked = mfp.buttons[gridSize - 1][0];
		previouslyClicked.setCurrentPosition();
		previouslyClicked.doClick();

		rw = new RandomWalk(gridSize);
		rw.createWalk();
		this.path = rw.getPath();
		setPath();
		placeMines();
		startAnimation();
	}

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
							if(minesNearby == 0) 
								mfp.buttons[row][column].setBackground(Color.GREEN);
							if(minesNearby == 1) 
								mfp.buttons[row][column].setBackground(Color.YELLOW);
							if(minesNearby == 2) 
								mfp.buttons[row][column].setBackground(Color.ORANGE);
							if(minesNearby == 3) 
								mfp.buttons[row][column].setBackground(Color.RED);
						} else {
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
