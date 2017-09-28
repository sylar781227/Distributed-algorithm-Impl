/**
 * @author kaushik vallabhaneni
 * janmejaya sahoo
 * apoorv kumar
 */

// implement asynchronous BFS.
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Node implements Runnable {
	public int nodeId;
	public int distFromSource;
	private BlockingQueue<Messege> queue = new LinkedBlockingQueue<Messege>(100);
	boolean isAwake;
	int parent;
	Node[] children;
	Node[] neighbours;
	int[] neighbourIds;
	int convergecastCount;
	boolean isComplete;
	boolean isSearchSent;
	boolean set;
	int searchCount;
	int ackCount;
	int nackCount;
	int newParentCount;
	int count=0;
	boolean anyvariable = true;
	List<Integer> childList = new ArrayList<Integer>();

	public boolean isSearchSent() {
		return isSearchSent;
	}

	public int getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(int searchCount) {
		this.searchCount = searchCount;
	}

	public int getAckCount() {
		return ackCount;
	}

	public void setAckCount(int ackCount) {
		this.ackCount = ackCount;
	}

	public int getNackCount() {
		return nackCount;
	}

	public void setNackCount(int nackCount) {
		this.nackCount = nackCount;
	}

	public int getNewParentCount() {
		return newParentCount;
	}

	public void setNewParentCount(int newParentCount) {
		this.newParentCount = newParentCount;
	}

	public boolean isSet() {
		return set;
	}

	public void setSet(boolean set) {
		this.set = set;
	}

	public List<Integer> getChildList() {
		return childList;
	}

	public void setChildList(List<Integer> childList) {
		this.childList = childList;
	}

	public void setSearchSent(boolean isSearchSent) {
		this.isSearchSent = isSearchSent;
	}

	public Node(int nodeId, int distFromSource, BlockingQueue<Messege> queue, boolean isAwake, int parent,
			Node[] children, Node[] neighbours) {
		super();
		this.nodeId = nodeId;
		this.distFromSource = distFromSource;
		this.queue = queue;
		this.isAwake = isAwake;
		this.parent = parent;
		this.children = children;
		this.neighbours = neighbours;
	}

	public Node(int nodeId, int[] neighboursIds) {
		this.nodeId = nodeId;
		this.neighbourIds = neighboursIds;

	}

	public int getConvergecastCount() {
		return convergecastCount;
	}

	public void setConvergecastCount(int convergecastCount) {
		this.convergecastCount = convergecastCount;
	}

	public int[] getNeighbourIds() {
		return neighbourIds;
	}

	public void setNeighbourIds(int[] neighbourIds) {
		this.neighbourIds = neighbourIds;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getDistFromSource() {
		return distFromSource;
	}

	public void setDistFromSource(int distFromSource) {
		this.distFromSource = distFromSource;
	}

	public BlockingQueue<Messege> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<Messege> queue) {
		this.queue = queue;
	}

	public boolean isAwake() {
		return isAwake;
	}

	public void setAwake(boolean isAwake) {
		this.isAwake = isAwake;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public Node[] getChildren() {
		return children;
	}

	public void setChildren(Node[] children) {
		this.children = children;
	}

	public Node[] getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(Node[] neighbours) {
		this.neighbours = neighbours;
	}

	@Override
	public String toString() {
		return "Node [nodeId=" + this.nodeId + ", distFromSource=" + distFromSource + ",parent=" + parent
				+"]";
	}

	@Override
	public void run() {

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (this.distFromSource == 0) {
			System.out.println("Root node is " + this.nodeId);
			int[] neibhours = this.getNeighbourIds();
			for (int i = 0; i < neibhours.length; i++) {
				if (neibhours[i] == 1) {
					try {
						Thread.sleep(randNum());
						Messege rootMssg = new Messege(this.nodeId, i, 0, Messege.MessageType.SEARCH);
						Main.nodeArray[this.nodeId].searchCount++;
						Messege.sendMessage(rootMssg);
						if(this.nodeId!=i)
						{
							Main.nodeArray[this.nodeId].childList.add(i);
							
						}
						
						Main.nodeArray[this.nodeId].isSearchSent = false;
						Thread.sleep(15000);
						System.out.println("Root node: " + this + " sent search messege to: " + i);
						awakeNode(i);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			

		}

		// checks each and every node for new messages until the process is
		// complete
		while (isComplete) {
			try {
				if (!this.queue.isEmpty()) {
					Messege incomingMssg = this.queue.take();
					if (incomingMssg.msgType == Messege.MessageType.SEARCH) {
						if (incomingMssg.distFromRoot + 1 < Main.nodeArray[this.nodeId].distFromSource
								&& !this.childList.contains(new Integer(incomingMssg.sourceId))) {
							Main.nodeArray[this.nodeId].distFromSource = incomingMssg.distFromRoot + 1;
							Messege ackMssg = new Messege(this.nodeId, incomingMssg.sourceId, Messege.MessageType.ACK); // send
																														// Ack
																														// to
																														// parent
							System.out.println(this.nodeId + " sent ACK msg to " + incomingMssg.sourceId);
							Thread.sleep(randNum());
							Messege.sendMessage(ackMssg);
							Main.nodeArray[this.nodeId].ackCount++;
							if (Main.nodeArray[this.nodeId].parent != -999) {
								Messege newParent = new Messege(this.nodeId, Main.nodeArray[this.nodeId].parent,
										Messege.MessageType.NEWPARENT);
								Messege.sendMessage(newParent);
								Main.nodeArray[this.nodeId].newParentCount++;
								System.out.println("Node " + this + " sending new-parent messege to old parent: "
										+ Main.nodeArray[this.nodeId].parent);
							}
							Main.nodeArray[this.nodeId].parent = incomingMssg.sourceId;
						} else if (incomingMssg.distFromRoot + 1 >= Main.nodeArray[this.nodeId].distFromSource
								|| this.childList.contains(new Integer(incomingMssg.sourceId))) {
							Messege nackMssg = new Messege(this.nodeId, incomingMssg.sourceId,
									Messege.MessageType.NACK);
							Thread.sleep(randNum());
							Messege.sendMessage(nackMssg);
							Main.nodeArray[this.nodeId].nackCount++;
							System.out.println(this.nodeId + " sent NACK msg to " + incomingMssg.sourceId);

						}
					}
					if (incomingMssg.msgType == Messege.MessageType.ACK) {

						Main.nodeArray[this.nodeId].childList.add(new Integer(incomingMssg.getSourceId()));

					}

					if (incomingMssg.msgType == Messege.MessageType.NACK
							|| incomingMssg.msgType == Messege.MessageType.NEWPARENT) {
						{
							Main.nodeArray[this.nodeId].childList.remove(new Integer(incomingMssg.getSourceId()));

						}

					}

					System.out.println("Execution snapshot: " + this.nodeId);
					for (int i = 0; i < Main.nodeArray.length; i++) {
						System.out.println(Main.nodeArray[i]);
					}
				}
				
				// allows every node to send the search message only once
				if (Main.nodeArray[this.nodeId].isAwake) {

					if (Main.nodeArray[this.nodeId].isSearchSent) {
						int[] neibhours = this.getNeighbourIds();

						for (int i = 0; i < neibhours.length; i++) {
							if (neibhours[i] == 1 && !(Main.nodeArray[this.nodeId].parent == i)) {
								try {
									Thread.sleep(randNum());
									Messege searchMsg = new Messege(this.nodeId, i,
											Main.nodeArray[this.nodeId].distFromSource, Messege.MessageType.SEARCH);
									Thread.sleep(randNum());
									Messege.sendMessage(searchMsg);
									Main.nodeArray[this.nodeId].searchCount++;
									System.out.println("Node: " + this + "sent search messege to: " + i);
									Thread.sleep(15000);
									awakeNode(i);

								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}

					}
					Main.nodeArray[this.nodeId].isSearchSent = false;


				}
				
if(Main.awakeCount == Main.nodeArray.length )
{
			if(anyvariable)
			{
				if (Main.nodeArray[this.nodeId].ackCount + Main.nodeArray[this.nodeId].nackCount
						+ Main.nodeArray[this.nodeId].newParentCount >= Main.nodeArray[this.nodeId].searchCount
						&& Main.nodeArray[this.nodeId].isAwake) {
					Main.nodeArray[this.nodeId].set = true;
					Main.nodeArray[Main.nodeArray[this.nodeId].parent].set=true;
					Main.setCount++;
					Main.nodeArray[this.nodeId].anyvariable = false;
				}
			}
			
}

			
				if (count== Main.nodeArray.length) {
					System.exit(0);
				}
				
				for(Node n: Main.nodeArray)
				{
					if(n.set)
					{
						count++;
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isAnyvariable() {
		return anyvariable;
	}

	public void setAnyvariable(boolean anyvariable) {
		this.anyvariable = anyvariable;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public int randNum() {
		Random rNum = new Random();
		int randomNuber = 0;
		for (int i = 0; i < 12; i++) {
			randomNuber = rNum.nextInt(1500) + 1;
		}
		return randomNuber;
	}

	public void awakeNode(int nodeID) {
		if(Main.nodeArray[nodeID].isAwake==false)
		{
			Main.awakeCount++;
			Main.nodeArray[nodeID].isAwake = true;
		}
		
	}

}
