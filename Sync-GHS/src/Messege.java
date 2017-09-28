/**
 @author 
 Group Members:
 Janmejaya Sahoo
 Kaushik Vallabhaneni
 Apoorv Kumar
 */

public class Messege {
	
	int fromUid;
	int toUid;
	double mwoe;
	String flag;
	int leaderuid;
	int level;
	int fragmentId;
	
	
	public int getFragmentId() {
		return fragmentId;
	}


	public void setFragmentId(int fragmentId) {
		this.fragmentId = fragmentId;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public Messege(int fromUid, int toUid, double mwoe, String flag,
			int fragmentId, int level) {
		super();
		this.fromUid = fromUid;
		this.toUid = toUid;
		this.mwoe = mwoe;
		this.flag = flag;
		this.fragmentId = fragmentId;
		this.level = level;
	}




	public int getLeaderuid() {
		return leaderuid;
	}


	public void setLeaderuid(int leaderuid) {
		this.leaderuid = leaderuid;
	}


	public Messege(int from, int to, double weightMWOE) {
		this.fromUid=from;
		this.toUid=to;
		this.mwoe=weightMWOE;
	}
	
	
	public Messege(int fromUid, int toUid, double mwoe, String flag) {
		super();
		this.fromUid = fromUid;
		this.toUid = toUid;
		this.mwoe = mwoe;
		this.flag = flag;
	}


	public int getFromUid() {
		return fromUid;
	}
	public void setFromUid(int fromUid) {
		this.fromUid = fromUid;
	}
	public int getToUid() {
		return toUid;
	}
	public void setToUid(int toUid) {
		this.toUid = toUid;
	}
	public double getMwoe() {
		return mwoe;
	}
	public void setMwoe(double weightMWOE) {
		this.mwoe = weightMWOE;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}


	@Override
	public String toString() {
		return "Messege [fromUid=" + fromUid + ", toUid=" + toUid + ", mwoe="
				+ mwoe + ", flag=" + flag + "]";
	}
	
	
	

}
