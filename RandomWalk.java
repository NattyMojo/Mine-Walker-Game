import java.util.Random;
import java.util.ArrayList;
import java.awt.Point;


/**
 * This class holds all parts of the object RandomWalk. Includes 2 constructors for seeded and unseeded.
 * as well as all methods needed for the class with getters.
 * 
 * @author Zach Luciano
 *
 */
public class RandomWalk implements RandomWalkInterface{

	//FIELDS
	private int gridSize;
	private boolean done = false;
	private ArrayList<Point> path = new ArrayList<Point>();
	private Point start;
	private Point end;
	private Point currentPos;
	private int direction;
	private long seed;
	private Random rand = new Random();
	private Random randSeed = new Random(seed);
	
	//CONSTRUCTORS
	public RandomWalk(int gridSize) {
		this.gridSize = gridSize;
		this.start = new Point(0,gridSize-1);
		this.end = new Point(gridSize-1,0);
		this.path.add(this.start);
		this.currentPos = new Point (start.x, start.y);
	}
	
	public RandomWalk(int gridSize, long seed) {
		this.gridSize = gridSize;
		this.seed = seed;
		this.start = new Point(0,gridSize-1);
		this.end = new Point(gridSize-1,0);
		this.path.add(this.start);
		this.currentPos = new Point (start.x, start.y);
	}
	
	//METHODS
	
	/**
	 * The step() method makes the position increase by one step either North or East, will ONLY create one step
	 */
	public void step() {
		direction();
		if(done == false) {
			if(direction == 1 && currentPos.y != end.y) {
				currentPos.y--;
				Point p1 = new Point(currentPos.x, currentPos.y);
				this.path.add(p1);
			}
			else if(direction == 0 && currentPos.x != end.x) {
				currentPos.x++;
				Point p1 = new Point(currentPos.x, currentPos.y);
				this.path.add(p1);
			}
			else if(currentPos.equals(end)) {
				this.done = true;
				System.out.println("Reached the end");
			}	
		}
	}
	
	/**
	 * The createWalk() method is used to run the step() method until it reaches the end of the grid.
	 */
	public void createWalk() {
		while(done == false) {
			step();	
		}
		System.out.print(toString());
	}
	
	/**
	 * The getGridSize() method returns the size of the grid.
	 * @return gridSize The size of the grid
	 */
	public int getGridSize() {
		return gridSize;
	}
	
	/**
	 * the setGridSize() method is used to change the gridSize of the object without having to create a new object.
	 * @param number What the new grid size will be
	 * @return gridSize New size of grid entered by user
	 */
	public int setGridSize(int number) {
		this.gridSize = number;
		return gridSize;
	}
	
	/**
	 * The getStartPoint() method returns the starting position of the character.
	 * @return start The Start position (0, gridSize-1)
	 */
	public Point getStartPoint() {
		return start;
	}
	
	/**
	 * The getEndPoint() method returns the expected end point of the path.
	 * @return end The end position (gridSize-1,0)
	 */
	public Point getEndPoint() {
		return end;
	}
	
	/**
	 * The getCurrentPoint() method returns where the character currently is.
	 * @return currentPos The current Point that the character is at
	 */
	public Point getCurrentPoint() {
		return currentPos;
	}
	
	/**
	 * The getPath() method returns the ArrayList path.
	 * @return points The copy of the ArrayList path.
	 */
	public ArrayList<Point> getPath() {
		ArrayList<Point> points = new ArrayList<Point>(path);
		return points;
	}
	
	/**
	 * The isDone() method returns false if not reached end point yet and true if you have
	 * @return done Is true or false depending on if the character is at the end or not
	 */
	public boolean isDone() {
		return done;
	}
	
	/**
	 * The toString() method puts the information from the path ArrayList into a readable string of points.
	 * @return string All points visited by the character
	 */
	public String toString() {
		String string = "";
		for(Point p: path) {
			string = string + "[" + p.x + ", " + p.y + "] "; 
		}
		string += "\n";
		return string;
	}
	
	/**
	 * The direction() method picks a random direction of North or East using integers 1-0 respectively
	 * @return direction 1 or 0 
	 */
	public int direction() {
		if(seed == 0) {
			this.direction = rand.nextInt(2);
//			System.out.println(direction);
		}
		if(seed != 0) {
			this.direction = randSeed.nextInt(2);
//			System.out.println(direction);
		}
		return direction;
	}

	@Override
	public void stepEC() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createWalkEC() {
		// TODO Auto-generated method stub
		
	}
}
