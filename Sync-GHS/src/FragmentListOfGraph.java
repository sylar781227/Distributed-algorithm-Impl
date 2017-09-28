/**
 @author 
 Group Members:
 Janmejaya Sahoo
 Kaushik Vallabhaneni
 Apoorv Kumar
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentListOfGraph {
	static ArrayList<Fragment> allFragments = new ArrayList<Fragment>();
	static List<Node> allNodes = new ArrayList<Node>();
	static Set<String> spanningEdges = new HashSet<String>();

	public static List<Node> getAllNodes() {
		return allNodes;
	}

	public static Fragment getFragment(int id) {
		for (Fragment f : allFragments) {
			for (Node n : f.getNodes()) {
				if (n.getId() == id) {
					return f;
				}
			}
		}
		return null;
	}

	public static Fragment getFragment(Node n) {
		for (Fragment f : allFragments) {
			if (f.getNodes().contains(n)) {
				return f;
			}
		}
		return null;
	}

	public static synchronized void setAllNodes(List<Node> allNodes) {
		FragmentListOfGraph.allNodes = allNodes;
	}

	public static ArrayList<Fragment> getFragments() {
		return allFragments;
	}
	
	
	//merge Fragments
	synchronized public static Fragment mergeFragments(int a, int b) {
		System.out.println("inside merge");

		System.out.println(a + " merges with  " + b);

		spanningEdges.add(a + "-" + b);
		System.out.println("spanning endges are: " + spanningEdges);

		System.out.println("ListOfFrag:" + " " + allFragments);
		Fragment fragment1 = null;
		Fragment fragment2 = null;

		for (Fragment f : allFragments) {
			if (f != null && f.isInFragment(a)) {
				fragment1 = f;
			}
			if (f != null && f.isInFragment(b)) {
				fragment2 = f;
			}
		}
		if (fragment1 == null || fragment2 == null)
			return null;
		if (fragment1 == fragment2)
			return fragment1;
		Fragment newFrag = new Fragment();
		System.out.println("Two fragments merging 1:" + fragment1.getNodes());
		System.out.println("Two fragments merging 2:" + fragment2.getNodes());
		if (SychGHS.nodeArray[a].getQueue().isEmpty()) {

		}
		newFrag.getNodes().addAll(fragment1.getNodes());
		newFrag.getNodes().addAll(fragment2.getNodes());
		newFrag.setLeader(a > b ? a : b);
		for (Node n : newFrag.getNodes()) {
			if (n.getId() != a || n.getId() != b) {
				n.setFragment(newFrag);
				n.setFragmentID(newFrag.getLeader());
				n.setLevel(n.getLevel() + 1);
			}

			else {
				n.setFragment(newFrag);
				n.setFragmentID(newFrag.getLeader());
				if (n.getId() == a) {
					ArrayList<Node> listNode = fragment1.getNodes();
					for (Node node : listNode) {
						if (node.getId() == a) {
							if (node.getCountAccept() == node.getCountMerge()) {
								node.setLevel(node.getLevel() + 1);
								break;

							}

						}
					}
				} else if (n.getId() == b) {

					ArrayList<Node> listNode = fragment1.getNodes();
					for (Node node : listNode) {
						if (node.getId() == b) {
							if (node.getCountAccept() == node.getCountMerge()) {
								node.setLevel(node.getLevel() + 1);
								break;

							}

						}
					}

				}
			}

		}
		allFragments.remove(fragment1);
		allFragments.remove(fragment2);
		allFragments.add(newFrag);
		return newFrag;
	}

	public static void setAllFragments(Fragment fragment) {
		allFragments.add(fragment);

	}

	public static synchronized ArrayList<Fragment> getAllFragments() {
		return allFragments;
	}

}
