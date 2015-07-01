import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ptri7957
 * @SID: 312160461
 * 
 *       COMP3308 Assignment 2
 * 
 */

public class Node {

	List<Node> children = new ArrayList<Node>();
	List<Node> parent = new ArrayList<Node>();
	List<Double> keyValues = new ArrayList<Double>();
	Map<String, List<Double>> table = new HashMap<String, List<Double>>();

	String name;

	/**
	 * Constructor - assigns the node
	 * its name
	 * 
	 * @param name
	 */
	public Node(String name) {
		this.name = name;
	}

	/**
	 * Return the name of this node
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the list of child nodes
	 * of this node
	 * 
	 * @return
	 */
	public List<Node> getChildren() {
		return children;
	}

	/**
	 * Return the list of parent nodes
	 * of this node
	 * 
	 * @return
	 */
	public List<Node> getParent() {
		return parent;
	}

	/**
	 * Returns the probability table for
	 * the node
	 * 
	 * @return
	 */
	public Map<String, List<Double>> getTable() {
		return table;
	}

	/**
	 * Add a child node to the node
	 * The node will become the parent of
	 * node n
	 * 
	 * @param n
	 */
	public void addChild(Node n) {
		children.add(n);
		n.getParent().add(this);
	}

	/**
	 * Set the conditional probability for the node
	 * i.e. the probability table for the node
	 * 
	 * @param probabilities
	 */
	public void setProbabilities(Map<String, List<Double>> probabilities) {
		if (!probabilities.containsKey("P")) {
			System.out.println("No probability present");
			return;
		}
		// If this node has no indegree it is a parent node
		if (this.getParent().isEmpty()) {
			keyValues = (probabilities.get("P"));
			table.put("P", keyValues);
		} else {
			
			// This will allow us to choose the value
		    // from the node's probability table
			// that corresponds to the key
			String key = "";    
		
			// The key will have a value corresponding to the proposition
			// E.g. S = 1 && A = 0 will produce a key of 10
			for (int i = 0; i < probabilities.get("P").size(); i++) {
				for (int j = 0; j < getParent().size(); j++) {
					double temp = probabilities.get(getParent().get(j).getName()).get(i);
					key = key + String.valueOf((int)temp);
				}
	
				List<Double> temp = new ArrayList<Double>();
				temp.add(probabilities.get("P").get(i));
				table.put(key, temp);
		
				key = "";
			}
		}
	}

}
