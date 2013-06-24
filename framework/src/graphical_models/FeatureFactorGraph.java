package graphical_models;

import utils.common.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/30/13
 */
public class FeatureFactorGraph extends FactorGraph {
	private Set<Node> roots;

	private Map<Pair<Integer, Integer>, Double> xyCountMap;
	private Map<Pair<Integer, Integer>, Double> yyCountMap;

	private final static Logger logger = Logger.getLogger(FeatureFactorGraph.class.getName());

	public FeatureFactorGraph(FactorGraphIndexer indexer, Set<Node> roots) {
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
}
