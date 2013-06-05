package graphical_models.sample;

import graphical_models.*;
import utils.common.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/30/13
 */
public class SampleFactorGraph extends FactorGraph {
	private Set<Node> roots;

	private Map<Pair<Integer, Integer>, Double> xyCountMap;
	private Map<Pair<Integer, Integer>, Double> yyCountMap;

	private final static Logger logger = Logger.getLogger(SampleFactorGraph.class.getName());

	public SampleFactorGraph(FactorGraphIndexer indexer, Set<Node> roots) {
		super(indexer);
		this.roots = roots;
		xyCountMap = new HashMap<Pair<Integer, Integer>, Double>();
		yyCountMap = new HashMap<Pair<Integer, Integer>, Double>();
	}

	public void setXyCountMap(Map<Pair<Integer, Integer>, Double> xyCountMap) {
		this.xyCountMap = xyCountMap;
	}

	public void setYyCountMap(Map<Pair<Integer, Integer>, Double> yyCountMap) {
		this.yyCountMap = yyCountMap;
	}

	@Override
	public Set<Node> getRoots() {
		return roots;
	}

	@Override
	public double getFactorValue(Node x, int xState, Node y, int yState) {
		// logger.info(x.getName() + " " + xState + ", " + y.getName() + " " + yState);
		double value = 0;
		if (x.getName().startsWith("x") && y.getName().startsWith("y")) {
			value = xyCountMap.get(new Pair<Integer, Integer>(xState, yState));
		} else if (x.getName().startsWith("y") && y.getName().startsWith("x")) {
			value = xyCountMap.get(new Pair<Integer, Integer>(yState, xState));
		} else if (x.getName().startsWith("y") && y.getName().startsWith("y")) {
			value = yyCountMap.get(new Pair<Integer, Integer>(xState, yState));
		}
		value = value / 10;
		// logger.info("value is " + value);
		return value;
	}

	public static void main(String[] args) {
		// Intialize the nodes.
		Node x = new VariableNode(1, "x", 3);
		Node y1 = new VariableNode(2, "y1", 2);
		Node y2 = new VariableNode(3, "y2", 2);
		Node x_y1 = new FactorNode(4, "x_y1");
		Node x_y2 = new FactorNode(5, "x_y2");
		Node y1_y2 = new FactorNode(6, "y1_y2");

		x.addNeighbor(x_y1);
		x.addNeighbor(x_y2);

		y1.addNeighbor(x_y1);
		y1.addNeighbor(y1_y2);

		y2.addNeighbor(x_y2);
		y2.addNeighbor(y1_y2);

		x_y1.addNeighbor(x);
		x_y1.addNeighbor(y1);

		x_y2.addNeighbor(x);
		x_y2.addNeighbor(y2);

		y1_y2.addNeighbor(y1);
		y1_y2.addNeighbor(y2);

		VariableNode var_x = (VariableNode) x;
		// x has 3 states. and the observed value is state 0.
		var_x.setJointProb(0, 1);
		var_x.setJointProb(1, 0);
		var_x.setJointProb(2, 0);
		var_x.setIsObserved(true);
		Set<Node> observedNodes = new HashSet<Node>();
		observedNodes.add(var_x);

		SampleFactorGraph sampleGraph = new SampleFactorGraph(null, observedNodes);

		Map<Pair<Integer, Integer>, Double> xyCountMap = new HashMap<Pair<Integer, Integer>, Double>();
		Map<Pair<Integer, Integer>, Double> yyCountMap = new HashMap<Pair<Integer, Integer>, Double>();

		xyCountMap.put(new Pair<Integer, Integer>(0, 0), 2.0);
		xyCountMap.put(new Pair<Integer, Integer>(1, 0), 2.0);
		xyCountMap.put(new Pair<Integer, Integer>(2, 0), 0.0);
		xyCountMap.put(new Pair<Integer, Integer>(0, 1), 1.0);
		xyCountMap.put(new Pair<Integer, Integer>(1, 1), 1.0);
		xyCountMap.put(new Pair<Integer, Integer>(2, 1), 3.0);

		yyCountMap.put(new Pair<Integer, Integer>(0, 0), 3.0);
		yyCountMap.put(new Pair<Integer, Integer>(0, 1), 1.0);
		yyCountMap.put(new Pair<Integer, Integer>(1, 0), 1.0);
		yyCountMap.put(new Pair<Integer, Integer>(1, 1), 3.0);

		sampleGraph.setXyCountMap(xyCountMap);
		sampleGraph.setYyCountMap(yyCountMap);

		FactorGraphInference inference = new FactorGraphInference(sampleGraph);
		inference.doBeliefPropogate();

		for (int i = 0; i < ((VariableNode) y1).getStateLength(); ++i) {
			double prob = 1;
			for (Node neighbor : y1.getNeighbors()) {
				prob *= neighbor.getMessage(y1).getStateVal(i);
			}
			((VariableNode) y1).setJointProb(i, prob);
			logger.info("state " + i + " probability is " + prob);
		}

		for (int i = 0; i < ((VariableNode) y2).getStateLength(); ++i) {
			double prob = 1;
			for (Node neighbor : y2.getNeighbors()) {
				prob *= neighbor.getMessage(y2).getStateVal(i);
			}
			((VariableNode) y2).setJointProb(i, prob);
			logger.info("state " + i + " probability is " + prob);
		}

		// logger.info("var x prob is " + var_x.getJointProb(0));
	}
}
