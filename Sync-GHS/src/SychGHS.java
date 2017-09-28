/**
 @author 
 Group Members:
 Janmejaya Sahoo
 Kaushik Vallabhaneni
 Apoorv Kumar
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SychGHS {
	String filePath="src\\a.txt";
	int noOfNodes=0;
	static String  uidOfNodes[] = null;
	static double edgeWeight[][]=null;
	ThreadGroup threads;
	static Node[] nodeArray;
	Fragment[] fragments; 
	
	public static void main(String[] args) throws IOException {
		SychGHS ghs= new SychGHS();		
		ghs.readNoadDetails();
		ghs.nodeInitialization();
		ghs.startGHS();

		}
	
	//Reading the details from Input file
	public void readNoadDetails() throws IOException
	{
		Scanner sc = new Scanner(new File("a.txt"));
		noOfNodes = sc.nextInt();
		uidOfNodes= new String[noOfNodes];
		for(int i=0;i<noOfNodes;i++)
		{
			uidOfNodes[i]= sc.next();
		}
		edgeWeight = new double[noOfNodes][noOfNodes];
		for (int i = 0; i < noOfNodes; i++) {
			for (int j = 0; j < noOfNodes; j++) {
				edgeWeight[i][j] = sc.nextDouble();
			}
		}
		sc.close();

		
	}
	
	//initializing each node
	
public void nodeInitialization()
{
	
	nodeArray = new Node[noOfNodes];
	//fragments=new  Fragment[noOfNodes];
	for(int i=0;i<noOfNodes;i++)
	{
		nodeArray[i]= new Node(i, getNeighbours(i, edgeWeight)); 
	}
	FragmentListOfGraph.allFragments= new ArrayList<Fragment>(noOfNodes);
	for (Node node : nodeArray) {
		FragmentListOfGraph.allFragments.add(new Fragment(node));
	}

}

//starting the synchGHS
public void startGHS()
{
	threads = new ThreadGroup("simulation");
	for(int i=0;i<noOfNodes;i++)
	{
		new Thread(threads, nodeArray[i]).start();
		
	}
}


//get node neibhours
public double[] getNeighbours(int i,double[][] edgeWeight2)
{
	double[] neighbours = new double[noOfNodes];
	for(int j=0;j<noOfNodes;j++)
	{
		neighbours[j]=(edgeWeight2[i][j]);
	}
	
	return neighbours;
}
	
}