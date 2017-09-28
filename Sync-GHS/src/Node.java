/**
 @author 
 Group Members:
 Janmejaya Sahoo
 Kaushik Vallabhaneni
 Apoorv Kumar
 */


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Node implements Runnable {

	private int level;
	private int fragmentID;
	private int id;
	private double[] neighbours;
	private Fragment fragment;
	private BlockingQueue<Messege> queue = new LinkedBlockingQueue<Messege>(
			100000);
	private Boolean isLeader;

	public Boolean getIsLeader() {
		return isLeader;
	}

	public void setIsLeader(Boolean isLeader) {
		this.isLeader = isLeader;
	}

	private int countAccept;
	private int countMerge;
	private int countReject;

	public int getFragmentID() {
		return fragmentID;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setFragmentID(int fragmentID) {
		this.fragmentID = fragmentID;
	}

	public BlockingQueue<Messege> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<Messege> queue) {
		this.queue = queue;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNeighbours(double[] neighbours) {
		this.neighbours = neighbours;
	}

	public Node(int id, double[] neighbours) {
		super();
		this.id = id;
		this.neighbours = neighbours;

	}

	public Node(int i) {
		this.id = i;
	}

	public int getId() {
		return id;
	}

	public double[] getNeighbours() {
		return neighbours;
	}

	public void run() {


		try {
			fragment = FragmentListOfGraph.getFragment(this);
			rootInitialization();
			
			 while (FragmentListOfGraph.getAllFragments().size() > 1) {
				 
				 
				 if(FragmentListOfGraph.getAllFragments().size()==2)
				 {
					 System.out.println("Final 2 fragments"+FragmentListOfGraph.getAllFragments());
					 Fragment final1 = FragmentListOfGraph.getAllFragments().get(0);
					 Fragment final2 = FragmentListOfGraph.getAllFragments().get(1);
					System.out.println("Final fragment 1"+final1);
					System.out.println("Final fragment 2"+final2);
					System.out.println("Final spanning edges"+FragmentListOfGraph.spanningEdges);
					String finalOutput="";
					for(String tempStr: FragmentListOfGraph.spanningEdges)
					{
						
						String[] tempx=tempStr.split("-");
						String fromNode=tempx[0];
						String toNode=tempx[1];
						finalOutput=finalOutput+SychGHS.uidOfNodes[Integer.parseInt(fromNode)]+" - "+SychGHS.uidOfNodes[Integer.parseInt(toNode)]+",";
						
					}
					
					System.out.println("final output"+finalOutput);
					System.exit(0);
					 
					 
				 }

				fragment = FragmentListOfGraph.getFragment(this);
				System.out.println("Frag going for mwoe calculation" + fragment);

				fragmentMWOE(fragment);
				initiateMerge();
				Thread.sleep(5000);

			}
			 
		} catch (InterruptedException e) {

		}

	}
		
	
//root initialization
	public void rootInitialization() throws InterruptedException {
		fragment.setLeader(this.id);
		this.setFragmentID(this.getId());
		this.isLeader = true;
		Thread.sleep(100);
	}

	
//calculate fragment MWOE	
	public synchronized String fragmentMWOE(Fragment fragment)
			throws InterruptedException {
		Thread.sleep(5000);
		ArrayList<Node> nodesinThisFragment = fragment.getNodes();
		Thread.sleep(1000);
		ArrayList<HashMap<Integer, Double>> list = new ArrayList<>();
		Node fromNode = null;
		double min = 999;
		int value = 0;
		for (Node n : nodesinThisFragment) {
			HashMap<Integer, Double> map = new HashMap<Integer, Double>();
			map = getLocalMWOE(n);
			System.out.println("one last time map"+map);
			Thread.sleep(1000);
			if(map!=null && map.values()!=null)
			{
			System.out.println(map);
			list.add(map);
			}
		}
System.out.println("before !list.isEmpty() ");
		if (!list.isEmpty()) {
			System.out.println("after !list.isEmpty()");
			for (HashMap<Integer, Double> itr : list) {
				if (itr.values() != null) {
					System.out.println("inside itr.values() != null");
					if ((Double) (itr.values().toArray()[0]) < min) {
						System.out.println("inside (Double) (itr.values().toArray()[0]) < min ");
						min = (Double) (itr.values().toArray()[0]);

						value = (Integer) itr.keySet().toArray()[0];

					}
				}
			}
		}

		for (Node n : nodesinThisFragment) {
			if (n.getLocalMWOE(n).get(value) != null
					&& n.getLocalMWOE(n).get(value) == min) {
				fromNode = n;

			}
		}
		Thread.sleep(1000);

		System.out.println("Fragment :" + fragment.getLeader()
				+ "----Fragment MWOE--with neibhour nodeID: " + value
				+ " weight: " + min);

		Thread.sleep(5000);

		// Messaging part starts :

		System.out.println("Message queue for node " + this + " has size "
				+ this.queue.size());
		Thread.sleep(5000);
		String edge;
		edge=fromNode.getId()+"-"+value;
		if (this.id == fromNode.getId()) {
			sendMessage(new Messege(fromNode.getId(), value, min, "merge", this
					.getFragment().getLeader(), this.getLevel()));
			this.countMerge++;
			System.out.println("node:" + fromNode
					+ "is sending merge request to node:" + value);
			Thread.sleep(1000);
			fromNode.countMerge++;
			System.out.println("In node thread" + this.id + "node: " + fromNode
					+ "has " + fromNode.countMerge + "merge requests");
			Thread.sleep(5000);
		}
		return edge;

	}

	public synchronized void initiateMerge() throws InterruptedException {
		while (this.queue.size() > 0) {

			System.out.println("node :" + this.id + "has queue size of"
					+ this.queue.size());
			Thread.sleep(1000);
			Messege recieveMsg = this.queue.take();

			if (recieveMsg.flag.equalsIgnoreCase("merge")) {
				if (recieveMsg != null
						&& recieveMsg.getFragmentId() != this.getFragmentID()
					&& this.level == recieveMsg.level)
					sendMessage(new Messege(recieveMsg.toUid,
							recieveMsg.fromUid, recieveMsg.mwoe, "accept",
							this.fragmentID, this.level));

				System.out
						.println(recieveMsg.toUid
								+ "Node sending the accept msg to"
								+ recieveMsg.fromUid);
				Thread.sleep(1000);

			}

			if (recieveMsg.flag.equalsIgnoreCase("accept")) {
				countAccept++;
				this.fragment = FragmentListOfGraph.mergeFragments(
						recieveMsg.fromUid, recieveMsg.toUid);
				System.out.println(recieveMsg.fromUid + "node sending merge to"
						+ recieveMsg.toUid);

			}
		}

	}

	public synchronized HashMap<Integer, Double> getLocalMWOE(Node node)
			throws InterruptedException {
		HashMap<Integer, Double> actualNeibhours = new HashMap<>();
		double[] allNeibhours = node.getNeighbours();
		System.out.println("node:" + node + " in getlocalMWOE");
		Thread.sleep(1000);
		ArrayList<Integer> isIdInFragment = new ArrayList<Integer>();
		for (int x = 0; x < fragment.getNodes().size(); x++) {
			isIdInFragment.add(fragment.getNodes().get(x).id);

		}
		System.out.println("same fragment nodes" + node.id + ">>>>>"
				+ isIdInFragment);
		for (int i = 0; i < allNeibhours.length; i++) {
			if (allNeibhours[i] > 0) {
				if (isIdInFragment.contains(i)) {
					continue;
				}
				actualNeibhours.put(i, allNeibhours[i]);

			}
		}

		System.out.println("Node:" + node.getId() + "has these neibhours"
				+ actualNeibhours);

		if (!actualNeibhours.isEmpty()) {
			Double localMWOE = Collections.min(actualNeibhours.values());

			Set<Integer> indexes = actualNeibhours.keySet();
			int minIndex = 999999;
			HashMap<Integer, Double> map = new HashMap<>();
			for (Integer index : indexes) {

				if (actualNeibhours.get(index) == localMWOE) {
					map.put(index, localMWOE);
					System.out.println("Node:" + this.getId()
							+ "has localMWOE OF:" + localMWOE + "with neibhour"
							+ index);
					System.out.println("final map"+map);
					return map;

				}

			}
		}
		return null;

	}

	public int getCountAccept() {
		return countAccept;
	}

	public void setCountAccept(int countAccept) {
		this.countAccept = countAccept;
	}

	public int getCountMerge() {
		return countMerge;
	}

	public void setCountMerge(int countMerge) {
		this.countMerge = countMerge;
	}

	public int getCountReject() {
		return countReject;
	}

	public void setCountReject(int countReject) {
		this.countReject = countReject;
	}

	public static void sendMessage(Messege message) {

		SychGHS.nodeArray[message.getToUid()].queue.add(message);
	}

	public static Messege receiveMessage(int id) throws InterruptedException {
		return SychGHS.nodeArray[id].getQueue().take();
	}

	@Override
	public String toString() {
		return "Node [level=" + level + ", fragmentID=" + fragmentID
				+ ", nodeID=" + id + "]";
	}

}
