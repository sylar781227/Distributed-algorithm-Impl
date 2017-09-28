/**
 * 
 * @author
 * janmejaya sahoo
 *
 *
 */

//class to implement the messaging framework

public class Messege {
	
	public enum MessageType
	{
		SEARCH,ACK,NACK,NEWPARENT;
	}
	
	int sourceId;
	int destId;
	int distFromRoot;
	MessageType msgType;
	
	
	public Messege(int sourceId, int destId, int distFromRoot,
			MessageType msgType) {
		super();
		this.sourceId = sourceId;
		this.destId = destId;
		this.distFromRoot = distFromRoot;
		this.msgType = msgType;
	}
	public Messege(int sourceID, int destID, MessageType mssgType) {

		super();
		this.sourceId = sourceID;
		this.destId = destID;
		this.msgType = mssgType;
	
	
	}
	public int getSourceId() {
		return sourceId;
	}
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	public int getDestId() {
		return destId;
	}
	public void setDestId(int destId) {
		this.destId = destId;
	}
	public int getDistFromRoot() {
		return distFromRoot;
	}
	public void setDistFromRoot(int distFromRoot) {
		this.distFromRoot = distFromRoot;
	}
	public MessageType getMsgType() {
		return msgType;
	}
	public void setMsgType(MessageType msgType) {
		this.msgType = msgType;
	}
	

	public static void sendMessage(Messege message) {

		Main.nodeArray[message.getDestId()].getQueue().add(message);
	}
	@Override
	public String toString() {
		return "Messege [sourceId=" + sourceId + ", destId=" + destId
				+ ", distFromRoot=" + distFromRoot + ", msgType=" + msgType
				+ "]";
	}


}
