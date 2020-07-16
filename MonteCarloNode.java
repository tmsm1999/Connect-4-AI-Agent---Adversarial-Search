import java.util.LinkedList;

class MonteCarloNode {

	Board state;
	int numberOfVisits = 0;
	int numberOfVictories = 0;
	boolean expanded;

	LinkedList<MonteCarloNode> children = null;

	MonteCarloNode(Board b) {
		state = b;
		numberOfVictories = 1;
		numberOfVisits = 1;
	}
}