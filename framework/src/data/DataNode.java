package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/18/13
 */
public class DataNode {
	private String index;
	private int dataNodeType;
	private List<DataArc> arcs;
	private Map<Integer, List<DataArc>> typeArcs;

	public DataNode(String index, int dataNodeType) {
		this.index = index;
		this.dataNodeType = dataNodeType;
		arcs = new ArrayList<DataArc>();
		typeArcs = new HashMap<Integer, List<DataArc>>();
	}

	public void addNeighbour(DataArc arc) {
		DataArcContent arcContent =  arc.getArcContent();
		if (arcContent == null) {
			arcs.add(arc);
		} else {
			// It is a heterogeneous network.
			int type = arcContent.getArcType();
			if (typeArcs.get(type) != null) {
				typeArcs.get(type).add(arc);
			} else {
				List<DataArc> newArcs = new ArrayList<DataArc>();
				newArcs.add(arc);
				typeArcs.put(type, newArcs);
			}
		}
	}

	public boolean equals(Object object) {
		if (!(object instanceof DataNode))
			return  false;
		DataNode node = (DataNode) object;
		if (this.dataNodeType == node.dataNodeType && this.index == node.index) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		return new Integer(index).hashCode() + dataNodeType * 23;
	}

	public List<DataArc> getNeighbors(int type) {
		return typeArcs.get(type);
	}

	public String getIndex() {
		return index;
	}
}
