/**
 * @author kaushik vallabhaneni 
 * Janmejaya sahoo
 * apoorv kumar
 */

// class to read input from text file and initialize node. Starts the bfs. 



import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	int noOfNodes;
	int rootNode;
	int[][] connectivity;
	ThreadGroup threads;
	static Node[] nodeArray;
	public static int setCount=0;
	public static int awakeCount=1;

	public static void main(String[] args) throws IOException {

		Main main = new Main();
		main.readNoadDetails();
		main.nodeInitialization();
		main.startAsynchBFS();
		
	}

	public void readNoadDetails() throws IOException {
		Scanner sc = new Scanner(new File("src\\a.txt"));
		String s = sc.nextLine();
		System.out.println(s);
		String[] arr = s.split(",");
		noOfNodes = Integer.parseInt(arr[0]);
		rootNode = Integer.parseInt(arr[1].trim());
		connectivity = new int[noOfNodes][noOfNodes];
		for (int i = 0; i < noOfNodes; i++) {
			for (int j = 0; j < noOfNodes; j++) {
				connectivity[i][j] = sc.nextInt();
			}
		}
		sc.close();

	}

	public void nodeInitialization() {
		nodeArray = new Node[noOfNodes];

		for (int i = 0; i < noOfNodes; i++) {
			nodeArray[i] = new Node(i, getNeighbours(i, connectivity));
			if (i == rootNode) {
				nodeArray[i].distFromSource = 0;
				nodeArray[i].isAwake = true;
				nodeArray[i].parent = -1;
				//nodeArray[i].set = false;
			} else {
				nodeArray[i].distFromSource = 999;
				nodeArray[i].isComplete = true;
				nodeArray[i].isSearchSent = true;
				nodeArray[i].parent = -999;
				//nodeArray[i].set = false;
			}
		}

	}

	public int[] getNeighbours(int i, int[][] connectivity) {
		int[] neighbours = new int[noOfNodes];
		for (int j = 0; j < noOfNodes; j++) {
			neighbours[j] = (connectivity[i][j]);
		}

		return neighbours;
	}

	public void startAsynchBFS() {
		threads = new ThreadGroup("simulation");
		for (int i = 0; i < noOfNodes; i++) {
			System.out.println("Before node creation" + nodeArray[i]);
			Thread node = new Thread(threads, nodeArray[i]);
			System.out.println(node);
			node.start();

		}
	}

}
