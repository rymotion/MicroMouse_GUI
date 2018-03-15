
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;
import java.lang.*;
import java.math.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

class AI extends Thread
{
	int speed = 100;
	boolean sleep = false;
	Maze maze;
	char logicalMaze [][] = new char [16] [16];

	AI(Maze m)
	{
		maze = m;
		init();
	}

	public void setSpeed(int spd)
	{
		speed = spd;
	}
	public void run(int iteration)
	{
		while(true)
		{
			try
			{
				Thread.sleep(1000-speed);
			}catch(InterruptedException e)
			{
			}

			try {
				int exit = move(iteration);

				if(exit == 1) {
					break;
				}
			} catch (Exception e) {
				if (e instanceof IOException) {
					System.out.println("\n!!!!!!!!!!!!!!!Exception!!!!!!!!!\n");
					e.printStackTrace();
				}
			}

			maze.repaint();

			while(sleep)
			{
				try
				{
					Thread.sleep(10);
				}catch(InterruptedException e)
				{
				}
			}
		}
	}

	int position[][] = new int[16][16];
	int middleFloodAr[][] = new int[16][16];
	int startAr[][] = new int[16][16];
	int repeat[][] = new int[16][16];


	private void init()
	{

		int max = 14;
		int min = 7;

		for(int y = 0; y < 16; y++)
		{
			int cnt = min;
			for(int x = 0; x < 16; x++)
			{
				if(x < 8)
				{
					middleFloodAr[x][y] = max-x;
				}else
				{
					middleFloodAr[x][y] = cnt;
					cnt++;
				}
			}
			if(y < 7)
			{
				max--;
				min--;
			}else if(y > 7)
			{
				max++;
				min++;
			}
		}

		for(int y = 0; y < 16; y++)
		{
			int start = 15 - y;
			for(int x = 0; x < 16; x++)
			{
				repeat[x][y] = 0;
				startAr[x][y] = start + x;
			}
		}
		position = middleFloodAr;
	}

	int state = 0;
	int last = 0;
	int i = 0;

// TODO: mark with char keys
	/*
		P = pocket
		T = traveled
		X = Intersection
		G = Goal
	*/
	private void mark(boolean condition, int someLast){
		switch ((repeat[maze.Rx][maze.Ry])) {
			case 1:
			logicalMaze[maze.Rx][maze.Ry] = 'X';
			break;
			case 2:
			logicalMaze[maze.Rx][maze.Ry] = 'Q';
			break;
			case 3:
			logicalMaze[maze.Rx][maze.Ry] = 'W';
			break;
			case 4:
			logicalMaze[maze.Rx][maze.Ry] = 'P';
			break;
			default:
			logicalMaze[maze.Rx][maze.Ry] = 'T';
			break;
		}
	}

	private void check(boolean condition, int someLast){
		switch ((repeat[maze.Rx][maze.Ry])) {
			case 1:
			logicalMaze[maze.Rx][maze.Ry] = 'X';
			break;
			case 2:
			logicalMaze[maze.Rx][maze.Ry] = 'Q';
			break;
			case 3:
			logicalMaze[maze.Rx][maze.Ry] = 'W';
			break;
			case 4:
			logicalMaze[maze.Rx][maze.Ry] = 'P';
			break;
			default:
			logicalMaze[maze.Rx][maze.Ry] = 'T';
			break;
		}
	}

	private int move(int iteration) throws IOException
	{
		// MARK: Increment of int array
		repeat[maze.Rx][maze.Ry]++;

		// MARK: Init handler
		if(state == 0 && position[maze.Rx][maze.Ry] == 0)
		{
			PrintWriter out = new PrintWriter(new FileWriter("../InputTextFiles/result" + (iteration) + ".txt"));
			System.out.println("Mouse reached to its destination");
			System.out.println("");
			System.out.println("Path of the Mouse and number of times mouse entered each box.");
			System.out.println("");
			state = 1;
			position = startAr;

			// MARK: Letter maze output
			for(int y = 0; y < 16; y++)
			{
				int start = 15 - y;
				for(int x = 0; x < 16; x++)
				{

					System.out.print(logicalMaze[x][y] + " ");
					out.print(logicalMaze[x][y] + " ");
				}
				System.out.println("");
				out.println("");
			}

			System.out.println("");

			// MARK: Number maze output
			for(int y = 0; y < 16; y++)
			{
				int start = 15 - y;
				for(int x = 0; x < 16; x++)
				{
					if (repeat[x][y] != 0){
						System.out.print(repeat[x][y] + " ");
						out.print(repeat[x][y] + " ");
					} else {
						System.out.print(" " + " ");
						out.print(" " + " ");
					}
				}
				System.out.println("");
				out.println("");
			}

			out.close();

			return 1;
		}

		boolean right = false;
		boolean left = false;
		boolean up = false;
		boolean down = false;
		boolean pocket = false;
		//boolean TopBlock = false;

		//breaks the program at certain point
		
		/*if(repeat[maze.Rx][maze.Ry-1]> 11000 && maze.getRight(maze.Rx, maze.Ry)
		&& maze.getLeft(maze.Rx, maze.Ry)){
				//TopBlock = true;
				repeat[maze.Rx][maze.Ry]+=20000;
				maze.Ry++;
			}*/
		if (maze.TopPocket()== true){
			pocket = true;
		}
		if(!maze.getTop(maze.Rx, maze.Ry))
		{
			up = true;
		}
		if(!maze.getRight(maze.Rx, maze.Ry))
		{
			right = true;
		}
		if(!maze.getLeft(maze.Rx, maze.Ry))
		{
			left = true;
		}
		if(!maze.getBottom(maze.Rx, maze.Ry))
		{
			down = true;
		}


		if(state == 0 || state == 1)
		{


			int best = 10000;

			/*if(TopBlock){
				repeat[maze.Rx][maze.Ry]+=20000;
				best = repeat[maze.Rx][maze.Ry+1];
			}*/
			if(pocket){
				repeat[maze.Rx][maze.Ry]+=20000;
				best = repeat[maze.Rx][maze.Ry+1];
			}

			if(up)
			{
				if(repeat[maze.Rx][maze.Ry-1] < best)
				{
					best = repeat[maze.Rx][maze.Ry-1];
				}else if(repeat[maze.Rx][maze.Ry-1] > best)
				{
					up = false;
				}
			}
			if(right)
			{
				if(repeat[maze.Rx+1][maze.Ry] < best)
				{
					best = repeat[maze.Rx+1][maze.Ry];
					up = false;
				}else if(repeat[maze.Rx+1][maze.Ry] > best)
				{
					right = false;
				}
			}
			if(left)
			{
				if(repeat[maze.Rx-1][maze.Ry] < best)
				{
					up = false;
					right = false;
					best = repeat[maze.Rx-1][maze.Ry];
				}else if(repeat[maze.Rx-1][maze.Ry] > best)
				{
					left = false;
				}
			}
			if(down)
			{
				if(repeat[maze.Rx][maze.Ry+1] < best)
				{
					up = false;
					right = false;
					left = false;
					best = repeat[maze.Rx][maze.Ry+1];
				}else if(repeat[maze.Rx][maze.Ry+1] > best)
				{
					down = false;
				}
			}

			best = 35;

			/*if(TopBlock){
				position[maze.Rx][maze.Ry]+=20000;
				best = position[maze.Rx][maze.Ry+1];
			}*/
			if(pocket){
				position[maze.Rx][maze.Ry]+=20000;
				best = position[maze.Rx][maze.Ry+1];
			}

			if(up)
			{
				if(position[maze.Rx][maze.Ry-1] < best)
				{
					best = position[maze.Rx][maze.Ry-1];
				}else if(position[maze.Rx][maze.Ry-1] > best)
				{
					up = false;
				}
			}
			if(right)
			{
				if(position[maze.Rx+1][maze.Ry] < best)
				{
					up = false;
					best = position[maze.Rx+1][maze.Ry];
				}else if(position[maze.Rx+1][maze.Ry] > best)
				{
					right = false;
				}
			}
			if(left)
			{
				if(position[maze.Rx-1][maze.Ry] < best)
				{
					up = false;
					right = false;
					best = position[maze.Rx-1][maze.Ry];
								if(i == 167)
								{
									System.out.println("Is Best:" + best);
			}
				}else if(position[maze.Rx-1][maze.Ry] > best)
				{
					left = false;
				}
			}
			if(down)
			{
				if(position[maze.Rx][maze.Ry+1] < best)
				{
					up = false;
					right = false;
					left = false;
					best = position[maze.Rx][maze.Ry+1];
				}else if(position[maze.Rx][maze.Ry+1] > best)
				{
					down = false;
				}
			}


		}


		if(i == 167)
		{
			System.out.println(i + ": " + left + " look here");
		}

		// MARK: move logic
		if(up && last == 1)
		{
			System.out.print("flag 1");
			right = false;
			left = false;
			down = false;
		}else if(right && last == 2)
		{
			System.out.print("flag 2");
			up = false;
			left = false;
			down = false;
		}else if(left && last == 3)
		{
			System.out.print("flag 3");
			up = false;
			right = false;
			down = false;
		}else if(down && last == 4)
		{
			System.out.print("flag 4");
			up = false;
			right = false;
			left = false;
		}


		i++;
		char arb;
		if(up)
		{
			last = 1;
			 maze.TopPocket();
			if (maze.runs != 0) {
				System.out.println("multicheck");
				check(maze.moveUp(), last);
			} else {
				mark(maze.moveUp(), last);
			}
			System.out.println("Direction : " + Direction.getHeadDirection(true, false, false, false));
		}else if(right)
		{
			last = 2;
			// maze.RightPocket();
			if (maze.runs != 0) {
				System.out.println("multicheck");
				check(maze.moveRight(), last);
			} else {
				mark(maze.moveRight(), last);
			}
			System.out.println("Direction : " + Direction.getHeadDirection(false, true, false, false));
		}else if(left)
		{
			last = 3;
			// maze.LeftPocket();
			if (maze.runs != 0) {
				System.out.println("multicheck");
				check(maze.moveLeft(), last);
			} else {
				mark(maze.moveLeft(), last);
			}
			System.out.println("Direction : " + Direction.getHeadDirection(false, false, false, true));
		}else
		{
			last = 4;
			// maze.BottomPocket();
			if (maze.runs != 0) {
				System.out.println("multicheck");
				check(maze.moveDown(), last);
			} else {
				mark(maze.moveDown(), last);
			}
			System.out.println("Direction : " + Direction.getHeadDirection(false, false, true, false));
		}
		return 0;
	}
}

class MicroMouse extends JFrame implements ActionListener, ChangeListener
{
	JFrame main;
	JPanel btn;
	Maze maze;
	AI ai;

	int speed = 100;

	public MicroMouse()
	{
		main = new JFrame("MicroMouse");
		btn = new JPanel();
		main.setSize(450, 550);
		main.setLayout(new BorderLayout());
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Maze maze = new Maze();
		main.getContentPane().add(maze, BorderLayout.LINE_START);

		JSlider spd = new JSlider(JSlider.HORIZONTAL, 0, 1000, 650);

		spd.addChangeListener(this);
		main.getContentPane().add(spd, BorderLayout.PAGE_END);

		main.getContentPane().add(btn, BorderLayout.LINE_END);

        main.setVisible(false);

        int maxIter = 5;
        int currIter = 0;

        System.out.println("What maze would you like to run?");
		Scanner tyIn = new Scanner(System.in);
		String file = tyIn.nextLine();

		main.setVisible(true);

        while (currIter <= maxIter) {

            System.out.println("\nCurrent iteration running: " + currIter + "\n");

        	if (currIter == 0) {
        		maze.LoadMaze(file);

	            maze.moveUp();
	            maze.Rx = 0;
	            maze.Ry = 15;
	            ai = new AI(maze);
	            ai.speed = speed;
	            ai.run(currIter);
        	} else {
        		maze.LoadMultiRun(file, currIter);

        		maze.moveUp();
	            maze.Rx = 0;
	            maze.Ry = 15;
	            ai = new AI(maze);
	            ai.speed = speed;
	            ai.run(currIter);
        	}

	        currIter = currIter + 1;
        }
	}


	public static void main(String arg[])
	{
		new MicroMouse();
	}

	public void actionPerformed(ActionEvent e)
	{
	}

	public void stateChanged(ChangeEvent e)
	{
	    JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting())
	    {
	    	speed = (int)source.getValue();
	    	ai.setSpeed(speed);
	    }

	}
}


class Maze extends JPanel
{
	boolean top[][] = new boolean[16][17];
	boolean side[][] = new boolean [17][16];
	int Rx = 0;
	int Ry = 15;
	int Sx = 0;
	int Sy = 15;

	int runs = 0;
	int x = 300;

	Map<Integer,Integer> mouseMap = new HashMap<Integer, Integer>();

	public boolean redrawPath = false;

	protected void paintComponent(Graphics g)
	{
		setSize(430,430);
		g.setColor(Color.WHITE);
	    g.fillRect(0, 0, getWidth(), getHeight());
	    g.setColor(Color.BLACK);
	    for(int y = 0; y < 17; y++)
	    {
			for(int x = 0; x < 16; x++)
			{
				if(top[x][y])
				{
					g.drawLine(x*26+5,y*26+5, x*26+31,y*26+5);
				}
			}
		}

		for(int y = 0; y < 16; y++)
		{
			for(int x = 0; x < 17; x++)
			{
				if(side[x][y])
				{
					g.drawLine(x*26+5,y*26+5, x*26+5,y*26+31);
				}
			}
		}

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(6+(Sx*26), 6+(Sy*26), 25, 25);

		g.setColor(Color.CYAN);
		g.fillRect(6+(7*26), 6+(7*26), 51, 51);

		g.setColor(Color.ORANGE);
		g.fillRect(9+(Rx*26), 9+(Ry*26), 19, 19);

		if(Direction.mouseHead.equals("N")) {
			g.setColor(Color.BLACK);
			g.fillRect(9+(Rx*26), 9+(Ry*26), 19, 5);
		}
		if(Direction.mouseHead.equals("E")) {
			g.setColor(Color.BLACK);
			g.fillRect(24+(Rx*26), 9+(Ry*26), 5, 19);
		}
		if(Direction.mouseHead.equals("W")) {
			g.setColor(Color.BLACK);
			g.fillRect(5+(Rx*26), (9+Ry*26), 5, 19);
		}
		if(Direction.mouseHead.equals("S")) {
			g.setColor(Color.BLACK);
			g.fillRect((9+Rx*26), (24+Ry*26), 19,5);
		}
	}

	void setStart(int x, int y)
	{
		Sx = x;
		Sy = y;
	}
	boolean[][] getTop()
	{
		return top;
	}

	boolean[][] getSide()
	{
		return side;
	}

	void printMaze()
	{
		for(int y = 0; y < 16; y++)
		{
			System.out.print(" ");
			for(int x = 0; x < 16; x++)
			{
				if(top[x][y])
				{
					System.out.print("- ");
				}else
				{
					System.out.print("  ");
				}
			}
			System.out.println("");
			for(int x = 0; x < 17; x++)
			{
				if(x == Rx && y == Ry)
				{
					if(side[x][y])
					{
						System.out.print("|0");
					}else
					{
						System.out.print(" 0");
					}
				}else
				{
					if(side[x][y])
					{
						System.out.print("| ");
					}else
					{
						System.out.print("  ");
					}
				}
			}
			System.out.println("");
		}

		System.out.print(" ");
		for(int x = 0; x < 16; x++)
		{
			if(top[x][16])
			{
				System.out.print("- ");
			}else
			{
				System.out.print("  ");
			}
		}
		System.out.println("");
	}

	void LoadMaze(String file)
	{
        File fin = new File("../InputTextFiles/" + file);
		try
		{
			FileInputStream in = new FileInputStream (fin);
			char let;
			let = (char)in.read();
			boolean flag = true;
			boolean spaces = false;
			int x = 0;
			int y = 0;
			while((byte)let != -1)
			{
				if(flag && x < 16)
				{
					if(spaces)
					{
						if(let == '-' || let == '_')
						{
							top[x][y] = true;
							x++;
						}else
						{
							top[x][y] = false;
							x++;
						}
						spaces = false;
					}else
					{
						spaces = true;
					}
				}else if(!flag && x < 17)
				{
					if(!spaces)
					{
						if(let == '|')
						{
							side[x][y] = true;
							x++;
						}else
						{
							side[x][y] = false;
							x++;
						}
						spaces = true;
					}else
					{

					spaces = false;
					}
				}
				if(let == '\n')
				{
					flag = !flag;
					if(flag == true)
					{
						y++;
					}
					x = 0;
					spaces = false;
				}
				let = (char)in.read();
			}
			// fin.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		for(int y = 0; y < 16; y++)
		{
			for(int x = 0; x < 17; x++)
			{
				if(side[x][y])
				{
				}else
				{
				}
			}
		}

	}

	void LoadMultiRun(String file, int multi)
	{
        File fin = new File("../InputTextFiles/" + file);
        File result = new File("../InputTextFiles/result"+ (multi - 1) +".txt");
		try
		{
			FileInputStream in = new FileInputStream (fin);
			FileInputStream comp = new FileInputStream (result);

			runs = multi;
			char let;

			int weight;
			let = (char)in.read();
			weight = (int)comp.read();
			boolean flag = true;
			boolean spaces = false;
			int x = 0;
			int y = 0;

			while((byte)let != -1)
			{
				if(flag && x < 16)
				{
					if(spaces)
					{
						if(let == '-' || let == '_')
						{
							top[x][y] = true;
							x++;
						}else
						{
							top[x][y] = false;
							x++;
						}
						spaces = false;
					}else
					{
						spaces = true;
					}
				}else if(!flag && x < 17)
				{
					if(!spaces)
					{
						if(let == '|')
						{
							side[x][y] = true;
							x++;
						}else
						{
							side[x][y] = false;
							x++;
						}
						spaces = true;
					}else
					{

					spaces = false;
					}
				}
				if(let == '\n')
				{
					flag = !flag;
					if(flag == true)
					{
						y++;
					}
					x = 0;
					spaces = false;
				}
				let = (char)in.read();
			}
			// fin.close();
			// result.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		for(int y = 0; y < 16; y++)
		{
			for(int x = 0; x < 17; x++)
			{
				if(side[x][y])
				{
				}else
				{
				}
			}
		}
	}


	boolean getTop(int x, int y)
	{
		return top[x][y];
	}

	boolean getBottom(int x, int y)
	{
		return top[x][y+1];
	}

	boolean getLeft(int x, int y)
	{
		return side[x][y];
	}

	boolean getRight(int x, int y)
	{
		return side[x+1][y];
	}

	void setRx(int x)
	{
		Rx = x;
	}

	void setRy(int y)
	{
		Ry = y;
	}

	void setPos(int x, int y)
	{
		Ry = y;
		Rx = x;
	}

	int getRx()
	{
		return Rx;
	}

	int getRy()
	{
		return Ry;
	}

	boolean moveUp()
	{
		if(!getTop(Rx, Ry))
		{
			Ry--;
			return true;
		}else
		{
			return false;
		}
	}

	boolean moveDown()
	{
		if(!getBottom(Rx, Ry))
		{
			Ry++;
			return true;
		}else
		{
			return false;
		}
	}

	boolean moveRight()
	{
		if(!getRight(Rx, Ry))
		{
			Rx++;
			return true;
		}else
		{
			return false;
		}
	}

	boolean moveLeft()
	{
		if(!getLeft(Rx, Ry))
		{
			Rx--;
			return true;
		}else
		{
			return false;
		}
	}
	boolean TopPocket()
	{
		if (getTop(Rx, Ry) && getRight(Rx, Ry) && getLeft(Rx, Ry)) {
			Ry++;
			return true;
		}
		else {
			return false;
		}
	}


}
