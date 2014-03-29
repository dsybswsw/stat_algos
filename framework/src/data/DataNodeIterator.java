package data;

import java.util.Iterator;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/19/13
 */
public class DataNodeIterator implements Iterator<DataNode> {
	private DataGraph graph;
	private Iterator<String> indexIterator;

	public DataNodeIterator(DataGraph graph) {
		this.graph = graph;
		this.indexIterator = graph.getUserNodes().iterator();
	}

	@Override
	public boolean hasNext() {
		return indexIterator.hasNext();
	}

	@Override
	public DataNode next() {
		return graph.getUserNode(indexIterator.next());
	}

	@Override
	public void remove() {
		// TOOD (Shiwei Wu) : Do not change the iterator.
	}
}
