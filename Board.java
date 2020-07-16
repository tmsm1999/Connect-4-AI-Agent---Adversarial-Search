import java.util.Scanner;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Board {

	static final String RED = "\33[31m";
	static final String YELLOW = "\33[33m";
	static final String RESET = "\033[0m";

	char[][] state = new char[Constants.h][Constants.w];
	char player;
	Board parent;

	int nPieces;
	int utility;

	//create game board
	Board(Board p, char[][] s, char c, int n) { //constructor to initialize game
		parent = p;

		for(int i = 0; i < Constants.h; i++) {
			for(int j = 0; j < Constants.w; j++) {
				state[i][j] = s[i][j];
			}
		}
		
		player = c;
		nPieces = n;
	}

	void printBoard() {

		System.out.print("\033[H\033[2J");  
		System.out.flush();
		System.out.println(RED + "CONNECT 4 - BOARD GAME" + RESET);
		System.out.println();

		for(int i = 0; i < Constants.h; i++) {
			System.out.print(i + 1 + "  ");
			for(int j = 0; j < Constants.w; j++) {
				if(state[i][j] == 'X') {
					System.out.print(RED + state[i][j] + RESET + " ");
				}
				else if(state[i][j] == 'O') {
					System.out.print(YELLOW + state[i][j] + RESET + " ");
				}
				else {
					System.out.print(state[i][j] + " ");
				}
			}
			System.out.println();
		}
		System.out.println("   " + "1 2 3 4 5 6 7");
	}

	int numberOfX(String s) {
		int total = 0;

		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == 'X') {
				total++;
			}
		}
		return total;
	}

	int numberOfO(String s) {
		int total = 0;

		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == 'O') {
				total++;
			}
		}
		return total;
	}

	int numberOfBlank() {
		int total = 0;

		for(int i = 0; i < Constants.h; i++) {
			for(int j = 0; j < Constants.w; j++) {
				if(state[i][j] == '-') {
					total++;
				}
			}
		}
		return total;
	}

	int evaluateBoard() {
		int total = 0;
		int result = 0;

		String seg;
		int nOfX;
		int nOfO;

		//Evaluate columns
		for(int i = 0; i < Constants.w; i++) {
			for(int j = 0; j < 3; j++) {

				seg = Character.toString(state[j][i]) + Character.toString(state[j+1][i]) + Character.toString(state[j+2][i]) + Character.toString(state[j+3][i]);

				nOfX = numberOfX(seg);
				nOfO = numberOfO(seg);

				result = evaluationSeg(nOfX, nOfO);
				if(result == 512 || result == -512) {
					return result;
				}
				total += result;
			}
		}

		//Evaluate lines
		for(int i = 0; i < Constants.h; i++) {
			for(int j = 0; j < 4; j++) {

				seg = Character.toString(state[i][j]) + Character.toString(state[i][j+1]) + Character.toString(state[i][j+2]) + Character.toString(state[i][j+3]);

				nOfX = numberOfX(seg);
				nOfO = numberOfO(seg);

				result = evaluationSeg(nOfX, nOfO);
				if(result == 512 || result == -512) {
					return result;
				}
				total += result;
 			}
		}

		//Main Diagonal
		seg = Character.toString(state[0][0]) + Character.toString(state[1][1]) + Character.toString(state[2][2]) + Character.toString(state[3][3]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[1][1]) + Character.toString(state[2][2]) + Character.toString(state[3][3]) + Character.toString(state[4][4]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[2][2]) + Character.toString(state[3][3]) + Character.toString(state[4][4]) + Character.toString(state[5][5]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[0][1]) + Character.toString(state[1][2]) + Character.toString(state[2][3]) + Character.toString(state[3][4]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[1][2]) + Character.toString(state[2][3]) + Character.toString(state[3][4]) + Character.toString(state[4][5]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[2][3]) + Character.toString(state[3][4]) + Character.toString(state[4][5]) + Character.toString(state[5][6]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[0][2]) + Character.toString(state[1][3]) + Character.toString(state[2][4]) + Character.toString(state[3][5]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[1][3]) + Character.toString(state[2][4]) + Character.toString(state[3][5]) + Character.toString(state[4][6]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[0][3]) + Character.toString(state[1][4]) + Character.toString(state[2][5]) + Character.toString(state[3][6]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[1][0]) + Character.toString(state[2][1]) + Character.toString(state[3][2]) + Character.toString(state[4][3]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[2][1]) + Character.toString(state[3][2]) + Character.toString(state[4][3]) + Character.toString(state[5][4]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[2][0]) + Character.toString(state[3][1]) + Character.toString(state[4][2]) + Character.toString(state[5][3]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		//Secundary Diagonal

		seg = Character.toString(state[0][6]) + Character.toString(state[1][5]) + Character.toString(state[2][4]) + Character.toString(state[3][3]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[1][5]) + Character.toString(state[2][4]) + Character.toString(state[3][3]) + Character.toString(state[4][2]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[2][4]) + Character.toString(state[3][3]) + Character.toString(state[4][2]) + Character.toString(state[5][1]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[1][6]) + Character.toString(state[2][5]) + Character.toString(state[3][4]) + Character.toString(state[4][3]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[2][5]) + Character.toString(state[3][4]) + Character.toString(state[4][3]) + Character.toString(state[5][2]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[2][6]) + Character.toString(state[3][5]) + Character.toString(state[4][4]) + Character.toString(state[5][3]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[0][5]) + Character.toString(state[1][4]) + Character.toString(state[2][3]) + Character.toString(state[3][2]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[1][4]) + Character.toString(state[2][3]) + Character.toString(state[3][2]) + Character.toString(state[4][1]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[2][3]) + Character.toString(state[3][2]) + Character.toString(state[4][1]) + Character.toString(state[5][0]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[0][4]) + Character.toString(state[1][3]) + Character.toString(state[2][2]) + Character.toString(state[3][1]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[1][3]) + Character.toString(state[2][2]) + Character.toString(state[3][1]) + Character.toString(state[4][0]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		seg = Character.toString(state[0][3]) + Character.toString(state[1][2]) + Character.toString(state[2][1]) + Character.toString(state[3][0]);
		result = evaluationSeg(numberOfX(seg), numberOfO(seg));
		if(result == -512 || result == 512) return result;
		total += result;

		if(this.player == 'X') {
			this.utility = total - 16;
			return this.utility;
		}
		this.utility = total + 16;
		return utility;
	}

	int evaluationSeg(int nOfX, int nOfO) {

		if(nOfX == 1 && nOfO == 0) {
			return -1;
		}
		else if(nOfX == 2 && nOfO == 0) {
			return -10; 
		}
		else if(nOfX == 3 && nOfO == 0) {
			return -50;
		}
		else if(nOfX == 4 && nOfO == 0) {
			return -512;
		}
		else if(nOfX == 0 && nOfO == 1) {
			return 1;
		}
		else if(nOfX == 0 && nOfO == 2) {
			return 10; 
		}
		else if(nOfX == 0 && nOfO == 3) {
			return 50;
		}
		else if(nOfX == 0 && nOfO == 4) {
			return 512;
		}

		return 0;
	}

	Board[] generateSucessors() {

		char pieceToPlay = ' ';
 		if(this.player == 'X') {
 			pieceToPlay = 'O';
 		}
 		else if(this.player == 'O') {
 			pieceToPlay = 'X';
 		}

		Board[] sucessors = new Board[7];

		int i;
		for(i = 0; i < 7; i++) {
			sucessors[i] = new Board(this, this.state, pieceToPlay, this.nPieces + 1);
		}

		for(i = 0; i < 7; i++) {
			if(!playColumn(i, sucessors[i])) {
				sucessors[i] = null;
			}
		}
		return sucessors;
 	}

 	boolean playColumn(int c, Board s) {

 		for(int i = 5; i >= 0; i--) {
 			if(s.state[i][c] == '-') {
 				s.state[i][c] = s.player;
 				return true;
 			}
 		}
 		return false;
 	}
}