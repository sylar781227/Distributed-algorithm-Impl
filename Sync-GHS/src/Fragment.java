/**
 @author 
 Group Members:
 Janmejaya Sahoo
 Kaushik Vallabhaneni
 Apoorv Kumar
 */


import java.util.ArrayList;

public class Fragment {
	
	private ArrayList<Node> nodes;
	private int  leader;
	private double mwoe;
	private int fragmentLevel;
	private Node Leader;
	private static int ids = 0;
	
	
	public Fragment(ArrayList<Node> nodes, int leader, double mwoe) {
		super();
		
		this.nodes = nodes;
		this.leader = leader;
		this.mwoe = mwoe;
	}
	
	public Fragment(Double mwoe,int leader) {
		super();
		
		this.mwoe = mwoe;
		this.leader=leader;
		
	}
	
	public Fragment(Node node) {
		leader = ids++;
		nodes = new ArrayList<Node>();
		nodes.add(node);
		setLeader(node);

	}

	
	public void setLeader(Node leader) {
		Leader = leader;
	}

	public double getMwoe() {
		return mwoe;
	}

	public void setMwoe(double weightMWOE) {
		this.mwoe = weightMWOE;
	}

	public Fragment() { 
		 	
				nodes = new ArrayList<Node>(); 
		 	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodesInFragment) {
		this.nodes = nodesInFragment;
	}

	public int getLeader() {
		return leader;
	}

	public synchronized void setLeader(int leader) {
		this.leader = leader;
	}
	public int getFragmentLevel() {
		return fragmentLevel;
	}

	public void setFragmentLevel(int fragmentLevel) {
		this.fragmentLevel = fragmentLevel;
	}


	public boolean isInFragment(int id) {
		for (Node n : nodes) {
			if (n.getId() == id)
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Fragment [nodes=" + nodes + ", leader=" + leader + ", mwoe="
				+ mwoe + ", fragmentLevel=" + fragmentLevel + ", Leader="
				+ Leader + "]";
	}

	
	
	
	
	
}
