import java.text.DecimalFormat;
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

public class Main {
	
	/**
	 * Find the mean of a list
	 * 
	 * @param array
	 * @return mean
	 */
	public double mean(List<Double> array) {
		double avg = 0.0;
		for (int i = 0; i < array.size(); i++) {
			avg += array.get(i);
		}
		return avg / array.size();
	}

	/**
	 * Find the variance of the list
	 * 
	 * @param array
	 * @return variance
	 */
	public double stdev(List<Double> array) {
		double avg = mean(array);
		double var = 0.0;
		double stdev = 0.0;

		if (array.size() == 1) {
			return 0;
		}

		for (int i = 0; i < array.size(); i++) {
			var += Math.pow((array.get(i) - avg), 2);
		}

		stdev = var / (array.size() - 1);

		return stdev;
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		Graph g = new Graph();

		Node C = new Node("Cloudy");
		Node S = new Node("Sprinkling");
		Node R = new Node("Raining");
		Node W = new Node("Wet grass");		

		List<Double> slistvar = new ArrayList<Double>();
		slistvar.add((double) 1);
		slistvar.add((double) 0);
		List<Double> rlistvar = new ArrayList<Double>();
		rlistvar.add((double) 1);
		rlistvar.add((double) 0);
		List<Double> wlistvar1 = new ArrayList<Double>();
		wlistvar1.add((double) 1);
		wlistvar1.add((double) 1);
		wlistvar1.add((double) 0);
		wlistvar1.add((double) 0);
		List<Double> wlistvar2 = new ArrayList<Double>();
		wlistvar2.add((double) 1);
		wlistvar2.add((double) 0);
		wlistvar2.add((double) 1);
		wlistvar2.add((double) 0);

		List<Double> clistprob = new ArrayList<Double>();
		clistprob.add(0.5);
		List<Double> slistprob = new ArrayList<Double>();
		slistprob.add(0.1);
		slistprob.add(0.5);
		List<Double> rlistprob = new ArrayList<Double>();
		rlistprob.add(0.8);
		rlistprob.add(0.2);
		List<Double> wlistprob = new ArrayList<Double>();	
		wlistprob.add(0.99);
		wlistprob.add(0.9);
		wlistprob.add(0.9);
		wlistprob.add(0.0);

		g.add(C);
		g.add(S);
		g.add(R);
		g.add(W);

		g.addEdge(C, S);
		g.addEdge(C, R);
		g.addEdge(S, W);
		g.addEdge(R, W);
		
		Map<String, List<Double>> probC = new HashMap<String, List<Double>>();
		probC.put("P", clistprob);
		Map<String, List<Double>> probS = new HashMap<String, List<Double>>();
		probS.put(S.getParent().get(0).getName(), slistvar);
		probS.put("P", slistprob);
		Map<String, List<Double>> probR = new HashMap<String, List<Double>>();
		probR.put(R.getParent().get(0).getName(), rlistvar);
		probR.put("P", rlistprob);
		Map<String, List<Double>> probW = new HashMap<String, List<Double>>();
		probW.put(W.getParent().get(0).getName(), wlistvar1);
		probW.put(W.getParent().get(1).getName(), wlistvar2);
		probW.put("P", wlistprob);
		
		C.setProbabilities(probC);
		S.setProbabilities(probS);
		R.setProbabilities(probR);
		W.setProbabilities(probW);
		
		Map<String, Double> evidence = new HashMap<String, Double>();
		evidence.put("Sprinkling", 1.0);
		evidence.put("Wet grass", 1.0);
		
		Map<String, Double> outcome = new HashMap<String, Double>();
		outcome.put("Cloudy", 1.0);
		
		int m = Integer.parseInt(args[0]);
		int n = Integer.parseInt(args[1]);
		
		List<Double> results = new ArrayList<Double>();
		
		for(int i = 0; i < n; i++){
			results.add(g.likelihoodWeighting(evidence, outcome, m));
		}
		
		double mean = main.mean(results);
		double stdev = main.stdev(results);
		
		DecimalFormat df = new DecimalFormat("#.######");
		System.out.println(df.format(mean).toString() + " " + df.format(stdev).toString());

	}

}
