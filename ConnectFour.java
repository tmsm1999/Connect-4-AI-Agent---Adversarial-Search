import java.util.Scanner;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.io.Console;
import java.util.LinkedList;
import java.util.Random;

public class ConnectFour {

	private static Console cons = System.console();

	static final String BOLD = "\033[1m"; //starts use of bold
	static final String RESET = "\033[0m"; //stops writing in bold
	static final String RED = "\33[31m";
	static final String YELLOW = "\33[33m";

	static final double C = Math.sqrt(2.0);

	public static int nodesExpanded = 0;

	public static void main(String[] args) throws Exception {
		System.out.print("\033[H\033[2J");  
		System.out.flush();

		startFirst();
	}

	static boolean makeMove(Board b) {

		Scanner userInput = new Scanner (System.in);
		String selectCol = ""; //Saves selected column inserted by the user.

		System.out.println();
		System.out.println(BOLD + "YOUR TURN!" + RESET);
		System.out.print("Enter column Number: ");

		selectCol = userInput.next();

		if(selectCol.length() == 1 && Character.isDigit(selectCol.charAt(0))) {

			int choice = Integer.parseInt(selectCol); //convert selectCol string to integer

			if(choice <= 0 || choice > 7) {
				return false;
			}

			if(b.state[0][choice - 1] == 'X' || b.state[0][choice - 1] == 'O') {
				return false;
			}

			for(int i = Constants.h - 1; i >= 0; i--) {
				if(b.state[i][choice - 1] == '-') {
					b.state[i][choice - 1] = 'X';
					break;
				}
			}
			return true;
		}
		
		System.out.println();
		System.out.println(BOLD + "INVALID choice!" + RESET);
		return false;
	}

	static void startFirst() throws Exception {
		int choice = 0;

		char[][] emptyBoard = {{'-', '-', '-', '-', '-', '-', '-'},
			     		       {'-', '-', '-', '-', '-', '-', '-'},
			     		       {'-', '-', '-', '-', '-', '-', '-'},
			     		       {'-', '-', '-', '-', '-', '-', '-'},
			     		       {'-', '-', '-', '-', '-', '-', '-'},
			     		       {'-', '-', '-', '-', '-', '-', '-'}};

		Scanner userInput = new Scanner(System.in);

		while(choice != 1 || choice != 2) {
			System.out.println(RED + "CONNECT 4 - BOARD GAME" + RESET);
			System.out.println();

			System.out.println(BOLD + "Who is the first to play?" + RESET);
			System.out.println();
			System.out.println("1 - Human");
			System.out.println("2 - Computer");
			System.out.println();

			try { choice = Integer.parseInt(cons.readLine("Enter your choice: ")); }
			catch(Exception e) { choice = 0; }

			if(choice == 1) {
				humanFirst(emptyBoard);
			}
			else if(choice == 2) {
				computerFirst(emptyBoard);
			}

			System.out.print("\033[H\033[2J");  
			System.out.flush();
		}
	}

	static void humanFirst(char[][] state) {

		Board human;
		Board computer = new Board(null, state, 'O', 0);

		for(int i = 0; i < 21; i++) {

			human = new Board(computer, computer.state, 'X', computer.nPieces + 1);
			human.printBoard();
			System.out.println();
			System.out.println("Nodes expanded: " + nodesExpanded);

			while(!makeMove(human)) {
				human.printBoard();
				System.out.println();
				System.out.println("Nodes expanded: " + nodesExpanded);
				System.out.println();
				System.out.println(BOLD + "INVALID choice!" + RESET);
				System.out.println(BOLD + "Insert a number from 1 to 7!" + RESET);
			}
			human.printBoard();

			if(human.evaluateBoard() == -512) {
				System.out.println();
				System.out.println(BOLD + "Human Player has won!" + RESET);
				System.exit(0);
			}

			try {
				System.out.println();
				System.out.println(BOLD + "Computer is thinking..." + RESET);
				TimeUnit.SECONDS.sleep(2);
			} catch(InterruptedException e) {}

			computer = new Board(human, miniMaxDecision(human).state, 'O', human.nPieces + 1);
			computer.printBoard();

			if(computer.evaluateBoard() == 512) {
				System.out.println();
				System.out.println("Nodes expanded: " + nodesExpanded);
				System.out.println();
				System.out.println(BOLD + "Computer has won!" + RESET);
				System.exit(0);
			}
		}

		System.out.println();
		System.out.println(BOLD + "It is a draw!" + RESET);
		System.exit(0);
	}

	static void computerFirst(char[][] state) {

		Board computer;
		Board human = new Board(null, state, 'X', 0);

		for(int i = 0; i < 21; i++) {

			human.printBoard();

			try {
				System.out.println();
				System.out.println(BOLD + "Computer is thinking..." + RESET);
				TimeUnit.SECONDS.sleep(1);
			} catch(InterruptedException e) {}

			computer = new Board(human, miniMaxDecision(human).state, 'O', human.nPieces + 1);
			computer.printBoard();

			if(computer.evaluateBoard() == 512) {
				System.out.println();
				System.out.println("Nodes expanded: " + nodesExpanded);
				System.out.println();
				System.out.println(BOLD + "Computer has won!" + RESET);
				System.exit(0);
			}

			human = new Board(computer, computer.state, 'X', computer.nPieces + 1);
			human.printBoard();
			System.out.println();
			System.out.println("Nodes expanded: " + nodesExpanded);

			while(!makeMove(human)) {
				human.printBoard();
				System.out.println("Nodes expanded: " + nodesExpanded);
				System.out.println();
				System.out.println(BOLD + "INVALID choice!" + RESET);
				System.out.println(BOLD + "Insert a number from 1 to 7!" + RESET);
			}
			human.printBoard();

			if(human.evaluateBoard() == -512) {
				System.out.println();
				System.out.println(BOLD + "Human Player has won!" + RESET);
				System.exit(0);
			}
		}

		System.out.println();
		System.out.println(BOLD + "It is a draw!" + RESET);
		System.exit(0);
	}

	//----------------------------------------MiniMax-----------------------------------------

	static Board miniMaxDecision(Board b) {
		nodesExpanded = 0;

		int utility = Integer.MIN_VALUE;
		Board choice = b;
		Board[] sucessors = b.generateSucessors();

		for(Board s : sucessors) {
			if(s != null) {
				s.utility = minValue(s, 1);
				if(s.utility > utility) {
					utility = s.utility;
					choice = s;
				}
			}
		}
		return choice;
	}

	static int maxValue(Board b, int depth) {
		int utility = b.evaluateBoard();
		int blank = b.numberOfBlank();

		if(utility == 512 || utility == -512 || depth == Constants.maxDepth || blank == 0) {
			return utility;
		}

		utility = Integer.MIN_VALUE;
		Board[] sucessors = b.generateSucessors();

		for(Board s : sucessors) {
			if(s != null) {
				nodesExpanded++;
				utility = Math.max(utility, minValue(s, depth + 1));
			}
		}
		return utility;
	}

	static int minValue(Board b, int depth) {
		int utility = b.evaluateBoard();
		int blank = b.numberOfBlank();

		if(utility == 512 || utility == -512 || depth == Constants.maxDepth || blank == 0) {
			return utility;
		}

		utility = Integer.MAX_VALUE;

		Board[] sucessors = b.generateSucessors();
		Board choice = null;

		for(Board s : sucessors) {
			if(s != null) {
				nodesExpanded++;
				utility = Math.min(utility, maxValue(s, depth + 1));
			}	
		}
		return utility;
	}

	//----------------------------------------Alpha - Beta-----------------------------------------//

	static Board alpha_beta_search(Board b) {
		nodesExpanded = 0;

		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;

		int utility = Integer.MIN_VALUE;
		Board choice = b;
		Board[] sucessors = b.generateSucessors();

		for(Board s : sucessors) {
			if(s != null) {
				s.utility = minValue_alphaBeta(s, 1, alpha, beta);
				if(s.utility > utility) {
					utility = s.utility;
					choice = s;
				}
			}
		}
		return choice;
	}

	static int maxValue_alphaBeta(Board b, int depth, int alpha, int beta) {
		int utility = b.evaluateBoard();
		int blank = b.numberOfBlank();

		if(utility == 512 || utility == -512 || depth == Constants.maxDepth || blank == 0) {
			return utility;
		}

		utility = Integer.MIN_VALUE;
		Board[] sucessors = b.generateSucessors();

		for(Board s : sucessors) {
			if(s != null) {
				nodesExpanded++;
				utility = Math.max(utility, minValue_alphaBeta(s, depth + 1, alpha, beta));

				if(utility >= beta) {
					return utility;
				}
				alpha = Math.max(alpha, utility);
			}
		}
		return utility;
	}

	static int minValue_alphaBeta(Board b, int depth, int alpha, int beta) {
		int utility = b.evaluateBoard();
		int blank = b.numberOfBlank();

		if(utility == 512 || utility == -512 || depth == Constants.maxDepth || blank == 0) {
			return utility;
		}

		utility = Integer.MAX_VALUE;
		Board[] sucessors = b.generateSucessors();

		for(Board s : sucessors) {
			if(s != null) {
				nodesExpanded++;
				utility = Math.min(utility, maxValue_alphaBeta(s, depth + 1, alpha, beta));

				if(utility <= alpha) {
					return utility;
				}
				beta = Math.min(beta, utility);
			}	
		}
		return utility;
	}


	//-----------------------------------Monte Carlo Search Tree------------------------------------//

	static Board MonteCarloST(Board b) {
		nodesExpanded = 0;
		int mcstIterations = 200000;
		MonteCarloNode root = new MonteCarloNode(b);

		for(int i = 0; i < mcstIterations; i++) {

			MonteCarloNode n = root;
			LinkedList<MonteCarloNode> visitedBoards = new LinkedList<MonteCarloNode>();

			while(n.children != null) {
				if(n.children.isEmpty()) {
					break;
				}
				n = select(n);
				visitedBoards.add(n);
			}

			if(n.children == null || !n.children.isEmpty()) {
				n = expand(n);
			}
			visitedBoards.add(n);

			int v = rollOut(n);
			for(MonteCarloNode s : visitedBoards) {
				if(v == 512) {
					s.numberOfVictories++;
				}
				s.numberOfVisits++;
			}
		}

		double max = Double.NEGATIVE_INFINITY;
		Board best = null;
		
		for(MonteCarloNode n : root.children) {
			double ucb = (double) n.numberOfVictories / n.numberOfVisits + C * Math.sqrt(2 * Math.log(root.numberOfVisits) / n.numberOfVisits);
			if(Double.compare(ucb, max) > 0) {
				max = ucb;
				best = n.state;
			}
		}
		return best;
	}

	static MonteCarloNode expand(MonteCarloNode p) { //receives parent node
		p.children = new LinkedList<MonteCarloNode>();
		Board[] sucessors = p.state.generateSucessors();

		for(Board s : sucessors) {
			if(s != null) {
				nodesExpanded++;
				p.children.add(new MonteCarloNode(s));
			}
		}

		if(p.children.size() == 0) {
			return p;
		}

		int randomChoice = new Random().nextInt(p.children.size());
		return p.children.get(randomChoice);
	}

	static MonteCarloNode select(MonteCarloNode p) { //receives parent node
		int index = 0;

		index = new Random().nextInt(p.children.size());
		return p.children.get(index);
	}

	static int rollOut(MonteCarloNode s) { 
		Board b = s.state;
		int utility = b.evaluateBoard();

		while(b.numberOfBlank() != 0 && utility != -512 && utility != 512) {
			if(s.children != null) {
				nodesExpanded += s.children.size();
			}
			LinkedList<Board> children = new LinkedList<Board>();
			Board[] sucessors = b.generateSucessors();

			for(int i = 0; i < 7; i++) {
				if(sucessors[i] != null) {
					children.add(sucessors[i]);
				}
			}
			int index = new Random().nextInt(children.size());
			b = children.get(index);
			utility = b.evaluateBoard();
		}

		return utility;
	}
}