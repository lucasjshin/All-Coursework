import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Solves the 8-Puzzle Game (can be generalized to n-Puzzle)
 */

public class EightPlayer {

	static Scanner scan = new Scanner(System.in);
	static int size=3; //size=3 for 8-Puzzle.
	static int numnodes; //number of nodes generated
	static int nummoves; //number of moves required to reach goal
	public static void main(String[] args)
	{
		int numsolutions = 0;
		int boardchoice = getBoardChoice();
		int algchoice = getAlgChoice();
		//determine numiterations based on user's choices
		int numiterations=0;
		if(boardchoice==0)
			numiterations = 1;
		else {
			switch (algchoice){
			case 0:
				numiterations = 100;//BFS
				break;
			case 1:
				numiterations = 1000;//A* with Manhattan Distance heuristic
				break;
			case 2:
				numiterations = 1000;//A* with your new heuristic
				break;
			}
		}
		Node initNode;
		for(int i=0; i<numiterations; i++){
			if(boardchoice==0)
				initNode = getUserBoard();
			else
				initNode = generateInitialState();//create the random board for a new puzzle
			boolean result=false; //whether the algorithm returns a solution
			switch (algchoice){
			case 0:
				result = runBFS(initNode); //BFS
				break;
			case 1:
				result = runAStar(initNode, 0); //A* with Manhattan Distance heuristic
				break;
			case 2:
				result = runAStar(initNode, 1); //A* with your new heuristic
				break;
			}
			//if the search returns a solution
			if(result){
				numsolutions++;
				System.out.println("Number of nodes generated to solve: " + numnodes);
				System.out.println("Number of moves to solve: " + nummoves);
				System.out.println("Number of solutions so far: " + numsolutions);
				System.out.println("_______");
			}
			else
				System.out.print(".");
		}//for

		System.out.println();
		System.out.println("Number of iterations: " +numiterations);
		if(numsolutions > 0){
			System.out.println("Average number of moves for "+numsolutions+" solutions: "+nummoves/numsolutions);
			System.out.println("Average number of nodes generated for "+numsolutions+" solutions: "+numnodes/numsolutions);
		}
		else
			System.out.println("No solutions in "+numiterations+" iterations.");
	}
	public static int getBoardChoice()
	{
		System.out.println("single(0) or multiple boards(1)");
		int choice = Integer.parseInt(scan.nextLine());
		return choice;
	}
	public static int getAlgChoice()
	{
		System.out.println("BFS(0) or A* Manhattan Distance(1) or A* <Your New Heuristic>(2)");
		int choice = Integer.parseInt(scan.nextLine());
		return choice;
	}

	public static Node getUserBoard()
	{
		System.out.println("Enter board: ex. 012345678");
		String stbd = scan.nextLine();
		int[][] board = new int[size][size];
		int k=0;
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				System.out.println(stbd.charAt(k));
				board[i][j]= Integer.parseInt(stbd.substring(k, k+1));
				k++;
			}
		}
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				System.out.println(board[i][j]);
			}
			System.out.println();
		}
		Node newNode = new Node(null,0, board);

		return newNode;
	}


	/**
	 * Generates a new Node with the initial board
	 */
	public static Node generateInitialState()
	{
		int[][] board = getNewBoard();
		Node newNode = new Node(null,0, board);

		return newNode;
	}
	/**
	 * Creates a randomly filled board with numbers from 0 to 8.
	 * The '0' represents the empty tile.
	 */
	public static int[][] getNewBoard()
	{
		int[][] brd = new int[size][size];
		Random gen = new Random();
		int[] generated = new int[size*size];
		for(int i=0; i<generated.length; i++)
			generated[i] = -1;
		int count = 0;
		for(int i=0; i<size; i++)
		{
			for(int j=0; j<size; j++)
			{
				int num = gen.nextInt(size*size);
				while(contains(generated, num)){
					num = gen.nextInt(size*size);
				}
				generated[count] = num;
				count++;
				brd[i][j] = num;
			}
		}
		/*
//Case 1: 12 moves
brd[0][0] = 1;
brd[0][1] = 3;
brd[0][2] = 8;
brd[1][0] = 7;
brd[1][1] = 4;
brd[1][2] = 2;
brd[2][0] = 0;
brd[2][1] = 6;
brd[2][2] = 5;
		 */
		return brd;
	}
	/**
	 * Helper method for getNewBoard()
	 */
	public static boolean contains(int[] array, int x)
	{
		int i=0;
		while(i < array.length){
			if(array[i]==x)
				return true;
			i++;
		}
		return false;
	}
	/**
	 * TO DO:
	 * Prints out all the steps of the puzzle solution and sets the number of moves used to solve this board.
	 */
	public static void printSolution(Node node) {


		if(node.getparent() != null) {

			printSolution(node.getparent());
			nummoves++;

		}
		node.print();




	}
	/**
	 * TO DO:
	 * Runs Breadth First Search to find the goal state.
	 * Return true if a solution is found; otherwise returns false.
	 */
	public static boolean runBFS(Node initNode)
	{
		//init search with max depth of 13
		Queue<Node> Frontier = new LinkedList<Node>();
		Frontier.add(initNode);
		int maxDepth = 13;
		//check if starting at goal
		if(initNode.isGoal()) {
			System.out.println("goal");
			return (true);
		}
		ArrayList<Node> Explored = new ArrayList<Node>();
		//search while Frontier exists
		while(!Frontier.isEmpty()) {
			Node cur_state = Frontier.poll();
			//check for max depth and break if true
			if(cur_state.getdepth() >= maxDepth) {
				System.out.println("depth");
				printSolution(cur_state);
				return false;
			}
			//update cur_state and check for goal
			Explored.add(cur_state);
			if(cur_state.isGoal()) {
				System.out.println("goal2");
				printSolution(cur_state);
				return true;
			}
			//expand cur_state and add to Frontier to be searched
			else {
				for (int[][] i : cur_state.expand()) {
					Node cur_expand = new Node(cur_state, cur_state.getdepth()+1, i);
					//trap for nodes that are already explored or in Frontier
					if(!Explored.contains(cur_expand) && !Frontier.contains(cur_expand)) {
						Frontier.add(cur_expand);
					}
				}
			}
		}
		//no solution found/error
		System.out.println("error");
		return false;
		/*TO DO*/
	}//BFS
	/***************************A* Code Starts Here ***************************/
	/**
	 * TO DO:
	 * Runs A* Search to find the goal state.
	 * Return true if a solution is found; otherwise returns false.
	 * heuristic = 0 for Manhattan Distance, heuristic = 1 for your new heuristic
	 */
	public static boolean runAStar(Node initNode, int heuristic) {
		return true;
	}
}