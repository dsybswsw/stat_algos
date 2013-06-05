package graphical_models;

import java.util.Set;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/30/13
 */
public abstract class FactorGraph {
	private FactorGraphIndexer indexer;

	public FactorGraph(FactorGraphIndexer indexer) {
		this.indexer = indexer;
	}

	public abstract Set<Node> getRoots();

	public abstract double getFactorValue(Node x, int xState, Node y, int yState);
}
