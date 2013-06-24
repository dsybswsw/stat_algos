package data;

import data.epinions.EpinionGraph;

import java.util.Iterator;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/19/13
 */
public class DataNodeIterator implements Iterator<DataNode> {
	private EpinionGraph epinionGraph;
	private Iterator<String> indexIterator;

	public DataNodeIterator(EpinionGraph epinionGraph) {
		this.epinionGraph = epinionGraph;
		this.indexIterator = epinionGraph.getUserIndexes().iterator();
	}

	@Override
	public boolean hasNext() {
		return indexIterator.hasNext();
	}

	@Override
	public DataNode next() {
		return epinionGraph.getUserNode(indexIterator.next());
	}

	@Override
	public void remove() {
		// TOOD (Shiwei Wu) : Do not change the iterator.
	}
}
