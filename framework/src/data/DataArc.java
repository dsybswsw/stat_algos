package data;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/18/13
 */
public class DataArc {
	private DataNode startNode;
	private DataNode endNode;
	private DataArcContent arcContent;

	public DataArc(DataNode startIndex, DataNode dstIndex, DataArcContent arcContent) {
		this.startNode = startIndex;
		this.endNode = dstIndex;
		this.arcContent = arcContent;
	}

	public DataNode getStartNode() {
		return startNode;
	}

	public DataNode getEndNode() {
		return endNode;
	}

	public  DataArcContent getArcContent() {
		return arcContent;
	}

	public boolean equals(Object object) {
		if (!(object instanceof DataArc))
			return false;
		DataArc compareArc = (DataArc) object;
		if (startNode.equals(compareArc.getStartNode()) && endNode.equals(compareArc.getEndNode())) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		return startNode.hashCode() * 31 + endNode.hashCode() * 43;
	}
}
