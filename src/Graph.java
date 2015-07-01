import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * @author ptri7957
 * @SID: 312160461
 * 
 *       COMP3308 Assignment 2
 * 
 */

public class Graph {

	List<Node> nodes = new ArrayList<Node>();

	/**
	 * Check if the graph is empty
	 * 
	 * @return true, or false
	 */
	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	/**
	 * Number of nodes in the graph
	 * 
	 * @return graph size
	 */
	public int size() {
		return nodes.size();
	}

	/**
	 * Return the nodes of the graph
	 * 
	 * @return
	 */
	public List<Node> getNodes() {
		return nodes;
	}

	/**
	 * Add nodes to the graph
	 * 
	 * @param n
	 */
	public void add(Node n) {
		nodes.add(n);
	}

	/**
	 * Remove nodes from the graph
	 * 
	 * @param n
	 */
	public void remove(Node n) {
		nodes.remove(n);
	}

	/**
	 * Create edges between nodes u and v
	 * 
	 * @param u
	 * @param v
	 */
	public void addEdge(Node u, Node v) {
		u.addChild(v);
	}

	/**
	 * Calculate the weight
	 * 
	 * @param given
	 * @param outcome
	 * @return The flag for when the sample for
	 *        an outcome node is the same
	 *        as the value sampled and weight
	 */
	public WeightedSample weightedSample(Map<String, Double> given,
			Map<String, Double> outcome) {
		
		double weight = 1.0;
		
		// Our samples
		Map<String, Double> result = new HashMap<String, Double>();
		
		boolean equalOutcome = true;
		
		// Allows us to choose a random sample
		// if the sample is not in evidence
		Random r = new Random();
		
		for (Node n : getNodes()) {
			
			// If the parents list of the node is
			// empty, it is a root node
			if (n.getParent().isEmpty()) {
				// If the node is in evidence,
				// and its value is 1, add it to the
				// sample dictionary as "Name" : 1.0,
				// else, add it as "Name" : 0.0
				if (given.containsKey(n.getName())) {
					if (given.get(n.getName()) == 1.0) {
						result.put(n.getName(), 1.0);
						weight *= n.getTable().get("P").get(0);
					} else {
						result.put(n.getName(), 0.0);
						weight *= (1 - n.getTable().get("P").get(0));
					}
					
				// If the node is not in evidence,
			    // randomly sample the node and return either
				// 1.0 or 0.0
				} else {
					if (r.nextDouble() <= n.getTable().get("P")
							.get(0)) {
						result.put(n.getName(), 1.0);
					} else {
						result.put(n.getName(), 0.0);
					}
				}
			
			// The node has a parent
			} else {
				String key = "";
				for (int i = 0; i < n.getParent().size(); i++)
					if (result.containsKey(n.getParent().get(i).getName())) {
						double temp = result.get(n.getParent().get(i).getName());
						key = key + String.valueOf((int) temp);		
					}
				
				//System.out.println(key);
				if(given.containsKey(n.getName())){
					if(given.get(n.getName()) == 1.0){
						result.put(n.getName(), 1.0);
						weight *= n.getTable().get(key).get(0);		
					}else{
						result.put(n.getName(), 0.0);
						double inverse = (1 - n.getTable().get(key).get(0));
						weight *= inverse;
					}
				}else{
					if(r.nextDouble() <= n.getTable().get(key).get(0)){
						result.put(n.getName(), 1.0);
					}else{
						result.put(n.getName(), 0.0);
					}
				}
			}
		}
		//System.out.println(result);
		// Check if the sampled value is equal to the value of the
		// outcome. Example if we are given the conditional value 
		// P(C = 1| W = 1, S = 1), we check if sampled value of C is 1
		for(Entry<String, Double> entry : result.entrySet()){
			if(outcome.containsKey(entry.getKey())){
				if(outcome.containsValue(entry.getValue())){
					equalOutcome = true;
				}else{
					equalOutcome = false;
				}
			}
		}
		//System.out.println(outcome.get("Cloudy") + " " + result.get("Cloudy") + " " + equalOutcome);
		// Return the weight and outcome of the sampled values
		return new WeightedSample(weight, equalOutcome);
	}

	/**
	 * Finds the probability of the outcome node
	 * through likelihood weighting
	 * 
	 * @param given
	 * @param outcome
	 * @param m
	 * @return The probability of the outcome node
	 */
	public double likelihoodWeighting(Map<String, Double> given,
			Map<String, Double> outcome, int m) {
		
		// The total weight calculated from
		// weightedSample()
		double totalWeight = 0.0;
		
		// The weight of the each sample
		// where the a sample value is equal
		// to the outcome value
		// Example: Sample(C = 1, S = 0, W = 1, R = 1)
	    // Outcome(C = 1)
		// C is equal to 1 in the sample, which is the same
		// as the outcome node, so we add the weight of the sample
		double weightWithOutcomeVal = 0.0;
		
		for (int i = 0; i < m; i++) {
			WeightedSample weight = weightedSample(given, outcome);
			totalWeight += weight.getWeight();
			if(weight.getE()){
				weightWithOutcomeVal += weight.getWeight();
			}
		}
		
		return weightWithOutcomeVal / totalWeight;
	}
}
